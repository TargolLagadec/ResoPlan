package org.targol.resoplan.ui.dialogs;

import org.targol.resoplan.utils.ProjectParams;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

public class ProjectEditDialogControler {

	private static final int HSP_MIN = 230;
	private static final int HSP_MAX = 400;
	private static final int HSP_STANDARD = 250;

	@FXML
	private TextField nameTextField;
	@FXML
	private Slider nbFloorsSlider;
	@FXML
	private Spinner<Integer> hspSpinner;
	@FXML
	private CheckBox atticCheck;
	@FXML
	private CheckBox basementCheck;
	@FXML
	private Slider marginSlider;

	@FXML
	private void initialize() {
		this.hspSpinner
				.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(HSP_MIN, HSP_MAX, HSP_STANDARD, 5));
	}

	/**
	 * Sets initial values when in edit mode
	 *
	 * @param params
	 */
	public void setProjectData(final ProjectParams params) {
		this.nameTextField.setText(params.name());
		this.nbFloorsSlider.setValue(params.nbFloors());
		this.hspSpinner.getValueFactory()
				.setValue(params.hsp() < HSP_MIN || params.hsp() > HSP_MAX ? HSP_STANDARD : params.hsp());
		this.atticCheck.setSelected(params.generateAttic());
		this.basementCheck.setSelected(params.generateBasement());
		this.marginSlider.setValue(params.margin());
	}

	public ProjectParams getResult() {
		return new ProjectParams(this.nameTextField.getText(), (int) this.nbFloorsSlider.getValue(),
				this.hspSpinner.getValue(), this.atticCheck.isSelected(), this.basementCheck.isSelected(),
				(int) this.marginSlider.getValue());
	}

	public TextField getNameTextField() {
		return this.nameTextField;
	}
}