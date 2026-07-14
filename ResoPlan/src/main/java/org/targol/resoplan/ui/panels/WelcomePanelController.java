package org.targol.resoplan.ui.panels;

import java.util.List;

import org.targol.resoplan.model.Project;
import org.targol.resoplan.services.ProjectsService;
import org.targol.resoplan.ui.utils.events.ProjectUpdatedEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class WelcomePanelController {

	private static final ProjectsService SVC_PROJECTS = SpringContextHelper.getBean(ProjectsService.class);

	@FXML
	private ListView<Project> lstProjects;

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
				final Project project = SVC_PROJECTS.openProjectWithFloorsAndNodes(cell.getItem()).get();
				UiEventBus.send(ProjectUpdatedEvent.firechange(project));
			});
			return cell;
		});
	}

	@FXML
	private void onNewProjectClick() {
		UiEventBus.send(ProjectUpdatedEvent.firechange(null));
	}

	public void setProjects(final List<Project> projects) {
		this.lstProjects.getItems().setAll(projects);
	}
}