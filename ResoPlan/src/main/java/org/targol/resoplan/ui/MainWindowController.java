package org.targol.resoplan.ui;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;
import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.services.ProjectsService;
import org.targol.resoplan.ui.dialogs.PreferencesDialogControler;
import org.targol.resoplan.ui.panels.FloorsAdjustmentPanel;
import org.targol.resoplan.ui.panels.ProblemsTitledPane;
import org.targol.resoplan.ui.panels.WelcomePanelController;
import org.targol.resoplan.ui.panels.floornetwork.FloorsNetworksSplitPane;
import org.targol.resoplan.ui.toolbars.DefaultToolBar;
import org.targol.resoplan.ui.toolbars.EvacToolBar;
import org.targol.resoplan.ui.utils.AppState;
import org.targol.resoplan.ui.utils.AppStateManager;
import org.targol.resoplan.ui.utils.DialogsHelper;
import org.targol.resoplan.ui.utils.GuiUtils;
import org.targol.resoplan.ui.utils.events.GenericActionEvent;
import org.targol.resoplan.ui.utils.events.ProblemsUpdatedEvent;
import org.targol.resoplan.ui.utils.events.ProjectUpdatedEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;
import org.targol.resoplan.utils.MiscUtils;
import org.targol.resoplan.utils.ProjectParams;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

@Component
public class MainWindowController {

	private final ProjectsService service;

	@FXML
	private Menu mnuRecentProjects;
	@FXML
	private MenuItem mnuClose;
	@FXML
	private MenuItem mnuAlign;
	@FXML
	private MenuItem mnuNetwork;
	@FXML
	private MenuItem mnuCatalog;
	@FXML
	private MenuItem mnuDebit;
	@FXML
	private HBox toolbarContainer;
	@FXML
	private StackPane contentPane;
	@FXML
	private ProblemsTitledPane problemsTitledPane;

	private final ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", Locale.getDefault()); //$NON-NLS-1$

	public MainWindowController(final ProjectsService service) {
		this.service = service;
	}

	@FXML
	private void initialize() {
		refreshRecentProjectsMenu();
		displayWelcomePanel();
		manageEvents();
		manageAccesses();
		manageDynamicToolbar();
		UiEventBus.send(ProblemsUpdatedEvent.fireMapRebuild());
	}

	private void manageEvents() {
		UiEventBus.register(this.contentPane, GenericActionEvent.TRIGGER_CATALOG, e -> displayCatalogPanel());
		UiEventBus.register(this.contentPane, GenericActionEvent.TRIGGER_NETWORKS, e -> displayNetworksPanel());
		UiEventBus.register(this.contentPane, GenericActionEvent.TRIGGER_ALIGN, e -> displayAdjustPanel());
		UiEventBus.register(this.contentPane, GenericActionEvent.TRIGGER_DEBIT, e -> displayDebitPanel());
		UiEventBus.register(this.contentPane, ProjectUpdatedEvent.PROJECT_UPDATE, e -> manageProjectChangeEvent(e));
	}

	private void manageProjectChangeEvent(final ProjectUpdatedEvent e) {
		if (e.getProject() != null) {
			openProject(e.getProject());
		}
		manageAccesses();
	}

	private void manageDynamicToolbar() {
		final AppStateManager stateMgr = AppStateManager.getInstance();
		this.toolbarContainer.getChildren().clear();
		final DefaultToolBar defBar = new DefaultToolBar();
		this.toolbarContainer.getChildren().setAll(defBar);
		stateMgr.activeNetworkLayerProperty().addListener((obs, oldLayer, newLayer) -> {
			this.toolbarContainer.getChildren().clear();
			if (newLayer == null || stateMgr.currentProjectProperty().get() == null) {
				this.toolbarContainer.getChildren().setAll(defBar);
				return;
			}
			final Project currentProject = stateMgr.currentProjectProperty().get();
			switch (newLayer) {
			case WATER_EVAC:
				final EvacToolBar evacBar = new EvacToolBar(currentProject);
				this.toolbarContainer.getChildren().setAll(evacBar);
				break;

			case WATER_ALIM:
				// TODO Mettre la bonne toolbar
				this.toolbarContainer.getChildren().setAll(defBar);
				break;

			case ELEC:
				// TODO Mettre la bonne toolbar
				this.toolbarContainer.getChildren().setAll(defBar);
				break;
			case NET:
				// TODO Mettre la bonne toolbar
				this.toolbarContainer.getChildren().setAll(defBar);
				break;

			default:
				break;
			}
		});
	}

	private void manageAccesses() {
		final AppState state = MiscUtils.getAppState(this.service.getOpenedProject());
		final boolean noProject = AppState.NO_PROJECT.equals(state);
		this.mnuClose.setDisable(noProject);
		this.mnuAlign.setDisable(noProject);
		final boolean incompleteProject = AppState.NO_PROJECT.equals(state)
				|| AppState.PROJECT_INCOMPLETE.equals(state);
		this.mnuNetwork.setDisable(incompleteProject);
		this.mnuDebit.setDisable(incompleteProject);
	}

	@FXML
	private void newProject() {
		final Optional<ProjectParams> resu = DialogsHelper
				.showProjectEditorDialog(this.contentPane.getScene().getWindow(), null);
		resu.ifPresent(param -> {
			final Project proj = this.service.createProject(param);
			refreshRecentProjectsMenu();
			openProject(proj, true);
		});
	}

	@FXML
	private void showPrefs() {
		final PreferencesDialogControler dialog = new PreferencesDialogControler(
				this.contentPane.getScene().getWindow());
		dialog.showAndWait();
	}

	private void openProject(final Project project) {
		openProject(project, false);
	}

	private void openProject(final Project project, final boolean newProj) {
		final Project prjWithFloors = this.service.setOpenedProject(project);
		UiEventBus.send(ProblemsUpdatedEvent.fireMapRebuild());
		if (newProj) {
			displayAdjustPanel();
		} else {
			final boolean isIncomplete = AppState.PROJECT_INCOMPLETE.equals(MiscUtils.getAppState(prjWithFloors));
			if (isIncomplete) {
				displayAdjustPanel();
			} else {
				displayNetworksPanel();
			}
		}
		final Stage stage = (Stage) this.contentPane.getScene().getWindow();
		stage.setTitle(Messages.getString("MainWindow.title.withProj", project.getName())); //$NON-NLS-1$
	}

	private void displayWelcomePanel() {
		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/WelcomePanel.fxml"), //$NON-NLS-1$
					this.bundle);
			final Parent root = loader.load();
			this.contentPane.getChildren().setAll(root);
			final WelcomePanelController controller = loader.getController();
			controller.setProjects(this.service.getAllProjects());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void displayAdjustPanel() {
		final FloorsAdjustmentPanel panel = new FloorsAdjustmentPanel(this.service.getOpenedProject());
		this.contentPane.getChildren().setAll(panel);
	}

	@FXML
	private void displayNetworksPanel() {
		final boolean hasMissingImage = this.service.getOpenedProject().getFloors().stream()
				.anyMatch(floor -> floor.getImgPath() == null || floor.getImgPath().isEmpty());
		if (hasMissingImage) {
			GuiUtils.errorAlert(Messages.getString("LayeredTab.floorWithNoImage")); //$NON-NLS-1$
			displayAdjustPanel();
			return;
		}
		final FloorsNetworksSplitPane panel = new FloorsNetworksSplitPane(this.service.getOpenedProject());
		this.contentPane.getChildren().setAll(panel);
	}

	@FXML
	private void displayCatalogPanel() {
//		Voici les champs du détail :
//			 * nom : TextField
//			 * description : Textfield mais éventuellement texte long
//			 * Catégorie : ChoiceBox valeurs de l'enum NodeCategory que j'ai ajoutée
//			 * Points d'accroche : Liste variable de 1 à n constantes, représentant les différentes valeurs de HookType possibles. Cette liste peut contenir plusieurs fois la même valeur.
//			 * AlimConstraint
	}

	@FXML
	private void displayDebitPanel() {

	}

	@FXML
	private void closeProject() {
		this.service.setOpenedProject(null);
		AppStateManager.getInstance().setOpenedProject(null);
		displayWelcomePanel();
		final Stage stage = (Stage) this.contentPane.getScene().getWindow();
		stage.setTitle(Messages.getString("MainWindow.title")); //$NON-NLS-1$
		UiEventBus.send(ProjectUpdatedEvent.firechange(null));
	}

	private void refreshRecentProjectsMenu() {
		this.mnuRecentProjects.getItems().clear();
		final List<Project> projects = this.service.getLastTenProjects();
		for (final Project project : projects) {
			final MenuItem item = new MenuItem(project.getName());
			item.setOnAction(event -> openProject(project));
			this.mnuRecentProjects.getItems().add(item);
		}
		if (projects.isEmpty()) {
			final MenuItem emptyItem = new MenuItem(Messages.getString("MainWindow.mnu_proj.itm_recent.none")); //$NON-NLS-1$
			emptyItem.setDisable(true);
			this.mnuRecentProjects.getItems().add(emptyItem);
		}
	}
}