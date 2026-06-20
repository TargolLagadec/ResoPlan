package org.targol.resoplan.ui.panels;

import org.targol.resoplan.model.Project;

import javafx.fxml.FXML;

public class ProjectPanelController {

	private Project project;

	@FXML
	private void initialize() {
	}

	public void setProject(final Project project) {
		this.project = project;
	}

}