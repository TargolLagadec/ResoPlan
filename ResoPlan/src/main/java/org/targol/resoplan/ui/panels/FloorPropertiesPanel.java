package org.targol.resoplan.ui.panels;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.services.FloorsService;
import org.targol.resoplan.services.ProjectsService;
import org.targol.resoplan.ui.utils.AppStateManager;
import org.targol.resoplan.ui.utils.events.AjustEvent;
import org.targol.resoplan.utils.IoHelper;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

public class FloorPropertiesPanel extends GridPane {

	private final static Map<Integer, Color> colors = new HashMap<>();
	static {
		colors.put(0, Color.BLACK);
		colors.put(1, Color.RED);
		colors.put(2, Color.BLUE);
		colors.put(3, Color.GREEN);
		colors.put(4, Color.DARKORANGE);
		colors.put(5, Color.BLUEVIOLET);
	}

	private final ProjectsService projectsService = SpringContextHelper.getBean(ProjectsService.class);
	private final FloorsService floorsService = SpringContextHelper.getBean(FloorsService.class);

	@FXML
	private TextField fileNameTextField;
	@FXML
	private CheckBox visibleCheck;
	@FXML
	private Slider transparencySlider;
	@FXML
	private Slider teintSlider;
	@FXML
	private Rectangle teintSample;
	@FXML
	private Spinner<Double> zoomSpinner;
	@FXML
	private Spinner<Double> shiftXSpinner;
	@FXML
	private Spinner<Double> shiftYSpinner;

	private final Floor floor;
	private final ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", Locale.getDefault()); //$NON-NLS-1$

	// Pause waiters to avoid saving or redrawing too often sliders or spinners
	// changes.
	private final PauseTransition autoSaveTimer = new PauseTransition(Duration.millis(500));
	private final PauseTransition autoRedrawTimer = new PauseTransition(Duration.millis(500));

	public FloorPropertiesPanel(final Floor floor) {
		this.floor = floor;
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/FloorPropertiesPanel.fxml"), //$NON-NLS-1$
				this.bundle);
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (final IOException e) {
			throw new RuntimeException("Impossible de charger le FXML de FloorPropertiesPanel", e); //$NON-NLS-1$
		}

	}

	@FXML
	public void initialize() {
		this.zoomSpinner
				.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(10.0d, 200.0d, 100.0d, 5.0d));
		this.shiftXSpinner
				.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-1500.0d, 1500.0d, 0.0d, 20.0d));
		this.shiftYSpinner
				.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-1500.0d, 1500.0d, 0.0d, 20.0d));
		this.transparencySlider.setValue(1.0);
		this.teintSlider.setValue(0);
		this.visibleCheck.setSelected(true);
		if (this.floor.getNumber() == 0) {
			this.visibleCheck.setDisable(true);
			this.transparencySlider.setDisable(true);
			this.zoomSpinner.setDisable(true);
			this.shiftXSpinner.setDisable(true);
			this.shiftYSpinner.setDisable(true);
		}
		final double zoom = this.floor.getZoomFactor() < 0.1d ? 1.0d : this.floor.getZoomFactor();
		this.zoomSpinner.getValueFactory().setValue(zoom * 100);
		this.shiftXSpinner.getValueFactory().setValue(this.floor.getShiftX());
		this.shiftYSpinner.getValueFactory().setValue(this.floor.getShiftY());

		initializeSaveTimer();
		initializeRedrawTimer();
		this.teintSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			changeColor(newValue.intValue());
		});
		final Color targetColor = colors.get((int) this.teintSlider.getValue());
		this.teintSample.setFill(targetColor);
		if (this.floor.getImgPath() != null) {
			final File imageFile = new File(this.floor.getImgPath());
			if (imageFile.exists()) {
				this.fileNameTextField.setText(imageFile.getAbsolutePath());
			}
		}
	}

	/**
	 * temporisation de la sauvegarde
	 */
	private void initializeSaveTimer() {
		this.autoSaveTimer.setOnFinished(event -> {
			// On récupère les valeurs actuelles des contrôles graphiques
			this.floor.setZoomFactor(this.zoomSpinner.getValueFactory().getValue() / 100);
			this.floor.setShiftX(this.shiftXSpinner.getValueFactory().getValue());
			this.floor.setShiftY(this.shiftYSpinner.getValueFactory().getValue());
			this.floorsService.update(this.floor);
			checkVirtualAboveOrBellowFloor();
			this.fireEvent(AjustEvent.fireFloorUpdated(this.floor));
			informStateManager();
		});
		this.zoomSpinner.getValueFactory().valueProperty()
				.addListener((obs, old, newVal) -> this.autoSaveTimer.playFromStart());
		this.shiftXSpinner.getValueFactory().valueProperty()
				.addListener((obs, old, newVal) -> this.autoSaveTimer.playFromStart());
		this.shiftYSpinner.getValueFactory().valueProperty()
				.addListener((obs, old, newVal) -> this.autoSaveTimer.playFromStart());
	}

	/**
	 * temporisation du rafraichissement graphique
	 */
	private void initializeRedrawTimer() {
		this.autoRedrawTimer.setOnFinished(event -> {
			// On récupère les valeurs actuelles des contrôles graphiques
			final double opacity = this.transparencySlider.getValue();
			final boolean visible = this.visibleCheck.isSelected();
			final Color paint = colors.get((int) this.teintSlider.getValue());
			this.fireEvent(AjustEvent.fireVisualChanged(this.floor, opacity, visible, paint));
		});
		this.visibleCheck.selectedProperty().addListener((obs, old, newVal) -> this.autoRedrawTimer.playFromStart());
		this.transparencySlider.valueProperty().addListener((obs, old, newVal) -> this.autoRedrawTimer.playFromStart());
		this.teintSlider.valueProperty().addListener((obs, old, newVal) -> this.autoRedrawTimer.playFromStart());
	}

	private void informStateManager() {
		AppStateManager.getInstance().updateProjectState(this.projectsService.getOpenedProject());
	}

	private void changeColor(final int newValue) {
		final Color targetColor = colors.get(newValue);
		this.teintSample.setFill(targetColor);
	}

	@FXML
	private void browseImage(final ActionEvent evt) {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(Messages.getString("FloorPropertiesPanel.filename.open")); //$NON-NLS-1$
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter(Messages.getString("FloorPropertiesPanel.filename.types"), "*.png", "*.jpg")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final File file = fileChooser.showOpenDialog(this.getScene().getWindow());
		final String newFileName = IoHelper.generateFloorPlanImageName(file,
				this.projectsService.getOpenedProject().getId(), this.floor.getId());
		final File result = IoHelper.copyFile(file, newFileName);
		this.fileNameTextField.setText(result.getAbsolutePath());
		this.floor.setImgPath(result.getAbsolutePath());
		this.floorsService.update(this.floor);
		checkVirtualAboveOrBellowFloor();
		informStateManager();
		this.fireEvent(AjustEvent.fireFloorUpdated(this.floor));
	}

	/**
	 * Les étages virtuels au dessous du niveau 0 ou au dessus du niveau max, ont la
	 * même image que le niveau non virtuel le plus proche avec le même niveau de
	 * zoom et le mème décalage. Elles seront affichées dans le panneau de pose des
	 * Nodes avec une transparence à 50%
	 */
	private void checkVirtualAboveOrBellowFloor() {
		final int floorNum = this.floor.getNumber();
		final Project proj = this.projectsService.getOpenedProject();
		if (floorNum == 0) {
			final Optional<Floor> basmnt = proj.getFloorByNumber(-1);
			if (basmnt.isPresent()) {
				final Floor basement = basmnt.get();
				if (basement.isVirtual()) {
					basement.setImgPath(this.floor.getImgPath());
					basement.setShiftX(this.floor.getShiftX());
					basement.setShiftY(this.floor.getShiftY());
					basement.setZoomFactor(this.floor.getZoomFactor());
					this.floorsService.update(basement);
				}
			}
		} else {
			final Optional<Floor> attc = proj.getFloorByNumber(this.floor.getNumber() + 1);
			if (attc.isPresent()) {
				final Floor attic = attc.get();
				if (attic.isVirtual()) {
					attic.setImgPath(this.floor.getImgPath());
					attic.setShiftX(this.floor.getShiftX());
					attic.setShiftY(this.floor.getShiftY());
					attic.setZoomFactor(this.floor.getZoomFactor());
					this.floorsService.update(attic);
				}
			}
		}
	}
}
