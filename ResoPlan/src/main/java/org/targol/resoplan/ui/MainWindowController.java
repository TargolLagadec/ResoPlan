package org.targol.resoplan.ui;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;
import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.services.ProjectsService;
import org.targol.resoplan.ui.components.CustomButton;
import org.targol.resoplan.ui.components.LayerRadioType;
import org.targol.resoplan.ui.dialogs.PreferencesDialogControler;
import org.targol.resoplan.ui.panels.FloorsAdjustmentPanel;
import org.targol.resoplan.ui.panels.WelcomePanelController;
import org.targol.resoplan.ui.panels.floornetwork.FloorsNetworksTab;
import org.targol.resoplan.ui.utils.AppState;
import org.targol.resoplan.ui.utils.AppStateManager;
import org.targol.resoplan.ui.utils.DialogsHelper;
import org.targol.resoplan.ui.utils.GuiUtils;
import org.targol.resoplan.utils.PreferencesManager;
import org.targol.resoplan.utils.ProjectParams;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
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
	private MenuItem mnuDebit;
	@FXML
	private CustomButton toolAlign;
	@FXML
	private CustomButton toolNetworks;
	@FXML
	private CustomButton toolEvacSortie;
	@FXML
	private CustomButton toolEvacDown3;
	@FXML
	private CustomButton toolEvacDown4;
	@FXML
	private CustomButton toolEvacDown10;
	@FXML
	private CustomButton toolEvacUp3;
	@FXML
	private CustomButton toolEvacUp4;
	@FXML
	private CustomButton toolEvacUp10;
	@FXML
	private CustomButton toolEvacTube3;
	@FXML
	private CustomButton toolEvacTube4;
	@FXML
	private CustomButton toolEvacTube10;

	@FXML
	private StackPane contentPane;

	private final ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", Locale.getDefault()); //$NON-NLS-1$

	public MainWindowController(final ProjectsService service) {
		this.service = service;
	}

	@FXML
	private void initialize() {
		refreshRecentProjectsMenu();
		loadWelcomeView();
		manageAccesses();
	}

	private void manageAccesses() {
		final AppStateManager stateMgr = AppStateManager.getInstance();
		final ObjectProperty<AppState> state = stateMgr.currentAppStateProperty();
		this.mnuClose.disableProperty().bind(state.isEqualTo(AppState.NO_PROJECT));
		this.mnuAlign.disableProperty().bind(state.isEqualTo(AppState.NO_PROJECT));
		this.toolAlign.disableProperty().bind(state.isEqualTo(AppState.NO_PROJECT));
		PreferencesManager.getInstance().addThemeChangeListener(this.toolAlign);
		this.mnuNetwork.disableProperty()
				.bind(state.isEqualTo(AppState.NO_PROJECT).or(state.isEqualTo(AppState.PROJECT_WITHOUT_IMAGES)));
		this.toolNetworks.disableProperty()
				.bind(state.isEqualTo(AppState.NO_PROJECT).or(state.isEqualTo(AppState.PROJECT_WITHOUT_IMAGES)));
		PreferencesManager.getInstance().addThemeChangeListener(this.toolNetworks);
		this.mnuDebit.disableProperty()
				.bind(state.isEqualTo(AppState.NO_PROJECT).or(state.isEqualTo(AppState.PROJECT_WITHOUT_IMAGES)));

		final BooleanBinding baseEvacDisable = stateMgr.currentAppStateProperty().isNotEqualTo(AppState.PROJECT_READY)
				.or(stateMgr.activeFloorProperty().isNull())
				.or(stateMgr.activeNetworkLayerProperty().isNotEqualTo(LayerRadioType.EAU_EVAC));
		final BooleanBinding sortieDisable = baseEvacDisable
				.or(Bindings.createBooleanBinding(
						() -> stateMgr.activeFloorProperty().get() == null
								|| stateMgr.activeFloorProperty().get().getNumber() != 0,
						stateMgr.activeFloorProperty()));
		final BooleanBinding downDisable = baseEvacDisable
				.or(Bindings.createBooleanBinding(
						() -> stateMgr.activeFloorProperty().get() != null
								&& stateMgr.activeFloorProperty().get().getNumber() == 0,
						stateMgr.activeFloorProperty()));
		final BooleanBinding upDisable = baseEvacDisable.or(Bindings.createBooleanBinding(() -> {
			final Floor activeFloor = stateMgr.activeFloorProperty().get();
			final Project activeProj = stateMgr.currentProjectProperty().get();
			if (activeFloor == null) {
				return true;
			}
			final int lastFloorNumber = activeProj.getFloors().size() - 1;
			return activeFloor.getNumber() == lastFloorNumber;
		}, stateMgr.activeFloorProperty()));

		final List<CustomButton> tubeTools = List.of(this.toolEvacTube3, this.toolEvacTube4, this.toolEvacTube10);
		tubeTools.forEach(btn -> {
			PreferencesManager.getInstance().addThemeChangeListener(btn);
			btn.disableProperty().bind(baseEvacDisable);
		});

		PreferencesManager.getInstance().addThemeChangeListener(this.toolEvacSortie);
		this.toolEvacSortie.disableProperty().bind(sortieDisable);

		final List<CustomButton> downTools = List.of(this.toolEvacDown3, this.toolEvacDown4, this.toolEvacDown10);
		downTools.forEach(btn -> {
			PreferencesManager.getInstance().addThemeChangeListener(btn);
			btn.disableProperty().bind(downDisable);
		});

		final List<CustomButton> upTools = List.of(this.toolEvacUp3, this.toolEvacUp4, this.toolEvacUp10);
		upTools.forEach(btn -> {
			PreferencesManager.getInstance().addThemeChangeListener(btn);
			btn.disableProperty().bind(upDisable);
		});
	}

	@FXML
	private void newProject() {
		final Optional<ProjectParams> resu = DialogsHelper
				.showProjectEditorDialog(this.contentPane.getScene().getWindow(), null);
		resu.ifPresent(param -> {
			final Project proj = this.service.createProject(param.name(), param.nbFloors());
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

	@FXML
	private void displayAdjustPanel() {
		this.contentPane.getChildren().setAll(new FloorsAdjustmentPanel(this.service.getOpenedProject()));
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
		this.contentPane.getChildren().setAll(new FloorsNetworksTab(this.service.getOpenedProject()));
	}

	@FXML
	private void displayDebitPanel() {

	}

	@FXML
	private void closeProject() {
		this.service.setOpenedProject(null);
		AppStateManager.getInstance().setOpenedProject(null);
		loadWelcomeView();
		final Stage stage = (Stage) this.contentPane.getScene().getWindow();
		stage.setTitle(Messages.getString("MainWindow.title")); //$NON-NLS-1$
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
			final MenuItem emptyItem = new MenuItem("(aucun projet récent)");
			emptyItem.setDisable(true);
			this.mnuRecentProjects.getItems().add(emptyItem);
		}
	}

	private void openProject(final Project project) {
		openProject(project, false);
	}

	private void openProject(final Project project, final boolean newProj) {
		this.service.setOpenedProject(project);
		AppStateManager.getInstance().setOpenedProject(project);
		if (newProj) {
			displayAdjustPanel();
		} else {
			displayNetworksPanel();
		}
		final Stage stage = (Stage) this.contentPane.getScene().getWindow();
		stage.setTitle(Messages.getString("MainWindow.title.withProj", project.getName())); //$NON-NLS-1$
	}

	private void loadWelcomeView() {
		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/WelcomePanel.fxml"), //$NON-NLS-1$
					this.bundle);
			final Parent root = loader.load();
			this.contentPane.getChildren().setAll(root);
			final WelcomePanelController controller = loader.getController();
			controller.setProjects(this.service.getAllProjects());
			controller.setProjectOpenListener(this::openProject);
			controller.setNewProjectListener(this::newProject);
			this.mnuClose.setDisable(true);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}