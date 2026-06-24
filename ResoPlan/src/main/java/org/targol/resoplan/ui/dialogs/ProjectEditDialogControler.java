package org.targol.resoplan.ui.dialogs;

import org.targol.resoplan.utils.ProjectParams;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class ProjectEditDialogControler {

	@FXML
	private TextField nameTextField;
	@FXML
	private Slider nbFloorsSlider;
	@FXML
	private CheckBox atticCheck;
	@FXML
	private CheckBox basementCheck;
	@FXML
	private Slider marginSlider;

	/**
	 * Sets initial values when in edit mode
	 *
	 * @param params
	 */
	public void setProjectData(final ProjectParams params) {
		this.nameTextField.setText(params.name());
		this.nbFloorsSlider.setValue(params.nbFloors());
		this.atticCheck.setSelected(params.generateAttic());
		this.basementCheck.setSelected(params.generateBasement());
		this.marginSlider.setValue(params.margin());
	}

	public ProjectParams getResult() {
		return new ProjectParams(this.nameTextField.getText(), (int) this.nbFloorsSlider.getValue(),
				this.atticCheck.isSelected(), this.basementCheck.isSelected(), (int) this.marginSlider.getValue());
	}

	public TextField getNameTextField() {
		return this.nameTextField;
	}
}