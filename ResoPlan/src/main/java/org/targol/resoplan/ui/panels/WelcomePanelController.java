package org.targol.resoplan.ui.panels;

import java.util.List;
import java.util.function.Consumer;

import org.targol.resoplan.model.Project;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class WelcomePanelController {

	@FXML
	private ListView<Project> lstProjects;

	private Consumer<Project> projectOpenListener;
	private Runnable newProjectListener;

	@FXML
	private void initialize() {
		this.lstProjects.setCellFactory(list -> {
			final ListCell<Project> cell = new ListCell<Project>() {
				@Override
				protected void updateItem(final Project project, final boolean empty) {
					super.updateItem(project, empty);
					if (empty || project == null) {
						setText(null);
					} else {
						setText(project.getName());
					}
				}
			};
			cell.setOnMouseClicked(event -> {
				final Project project = cell.getItem();
				if (project != null && this.projectOpenListener != null) {
					this.projectOpenListener.accept(project);
				}
			});
			return cell;
		});
	}

	@FXML
	private void onNewProjectClick() {
		if (this.newProjectListener != null) {
			this.newProjectListener.run();
		}
	}

	public void setProjects(final List<Project> projects) {
		this.lstProjects.getItems().setAll(projects);
	}

	public void setProjectOpenListener(final Consumer<Project> listener) {
		this.projectOpenListener = listener;
	}

	public void setNewProjectListener(final Runnable listener) {
		this.newProjectListener = listener;
	}
}