package org.targol.resoplan.ui.dialogs;

import org.targol.resoplan.utils.ProjectParams;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class ProjectEditDialogControler {

	@FXML
	private TextField nameTextField;
	@FXML
	private Slider nbFloorsSlider;

	/**
	 * Sets initial values when in edit mode
	 *
	 * @param params
	 */
	public void setProjectData(final ProjectParams params) {
		this.nameTextField.setText(params.name());
		this.nbFloorsSlider.setValue(params.nbFloors());
	}

	public ProjectParams getResult() {
		return new ProjectParams(this.nameTextField.getText(), (int) this.nbFloorsSlider.getValue());
	}

	public TextField getNameTextField() {
		return this.nameTextField;
	}
}