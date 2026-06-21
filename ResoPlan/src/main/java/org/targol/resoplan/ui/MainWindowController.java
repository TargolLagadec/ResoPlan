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
import org.targol.resoplan.ui.panels.WelcomePanelController;
import org.targol.resoplan.ui.utils.DialogsManager;
import org.targol.resoplan.ui.utils.PanelBuilder;
import org.targol.resoplan.utils.ProjectParams;

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
	private MenuItem mnuAjust;
	@FXML
	private MenuItem mnuNetwork;
	@FXML
	private MenuItem mnuDebit;
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
	}

	@FXML
	private void newProject() {
		final Optional<ProjectParams> resu = DialogsManager
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
		this.contentPane.getChildren().setAll(PanelBuilder.buildProjectTabPane(this.service.getOpenedProject()));
	}

	@FXML
	private void displayDebitPanel() {

	}

	@FXML
	private void closeProject() {
		this.service.setOpenedProject(null);
		this.mnuClose.setDisable(true);
		this.mnuAjust.setDisable(true);
		this.mnuNetwork.setDisable(true);
		this.mnuDebit.setDisable(true);

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
		if (newProj) {
			displayAdjustPanel();
		} else {
			displayNetworksPanel();
		}
		this.mnuClose.setDisable(false);
		this.mnuAjust.setDisable(false);
		this.mnuNetwork.setDisable(false);
		this.mnuDebit.setDisable(false);
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