package org.targol.resoplan.ui.panels;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.services.FloorsService;
import org.targol.resoplan.services.ProjectsService;
import org.targol.resoplan.ui.utils.GuiUtils;
import org.targol.resoplan.utils.IoHelper;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.animation.PauseTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
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
		colors.put(4, Color.AQUA);
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
	private Spinner<Integer> zoomSpinner;
	@FXML
	private Spinner<Integer> shiftXSpinner;
	@FXML
	private Spinner<Integer> shiftYSpinner;

	private final Floor floor;
	private final ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", Locale.getDefault()); //$NON-NLS-1$

	private final ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
	private final ObjectProperty<Color> colorProperty = new SimpleObjectProperty<>(Color.BLACK);
	private final ObjectProperty<ColorAdjust> effectProperty = new SimpleObjectProperty<>(new ColorAdjust());
	private final ObjectProperty<Double> zoomProperty = new SimpleObjectProperty<>(1.0d);
	private final ObjectProperty<Double> shiftXProperty = new SimpleObjectProperty<>(0.0d);
	private final ObjectProperty<Double> shiftYProperty = new SimpleObjectProperty<>(0.0d);

	// Pause waiter to avoid saving too often sliders or spinners changes.
	private final PauseTransition autoSaveTimer = new PauseTransition(Duration.millis(500));

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

		if (this.floor.getNumber() == 0) {
			this.visibleCheck.setSelected(true);
			this.visibleCheck.setDisable(true);
			this.transparencySlider.setDisable(true);
		}
		// TODO Mettre à jour les valeurs si existantes dans le Floor

		initializeSaveTimer();

		this.teintSample.setFill(this.colorProperty.get());
		if (this.floor.getImgPath() != null) {
			final File imageFile = new File(this.floor.getImgPath());

			if (imageFile.exists()) {
				this.fileNameTextField.setText(imageFile.getAbsolutePath());
				final Image img = new Image(imageFile.toURI().toString());
				this.imageProperty.set(img);
			}
		}
	}

	private void initializeSaveTimer() {
		this.autoSaveTimer.setOnFinished(event -> {
			// On récupère les valeurs actuelles des contrôles graphiques
			this.floor.setZoomFactor(this.zoomSpinner.getValueFactory().getValue());
			this.zoomProperty.set((double) this.zoomSpinner.getValueFactory().getValue() / 100);
			this.floor.setShiftX(this.shiftXSpinner.getValueFactory().getValue());
			this.shiftXProperty.set((double) this.shiftXSpinner.getValueFactory().getValue());
			this.floor.setShiftY(this.shiftYSpinner.getValueFactory().getValue());
			this.shiftYProperty.set((double) this.shiftYSpinner.getValueFactory().getValue());
			changeColor((int) this.teintSlider.valueProperty().get());
			this.floorsService.updateFloor(this.floor);
			System.out.println("Sauvegarde automatique de l'étage effectuée en BDD.");
		});
		this.zoomSpinner.getValueFactory().valueProperty()
				.addListener((obs, old, newVal) -> this.autoSaveTimer.playFromStart());
		this.shiftXSpinner.getValueFactory().valueProperty()
				.addListener((obs, old, newVal) -> this.autoSaveTimer.playFromStart());
		this.shiftYSpinner.getValueFactory().valueProperty()
				.addListener((obs, old, newVal) -> this.autoSaveTimer.playFromStart());
		this.transparencySlider.valueProperty().addListener((obs, old, newVal) -> this.autoSaveTimer.playFromStart());
		this.teintSlider.valueProperty()
				.addListener((observable, oldValue, newValue) -> this.autoSaveTimer.playFromStart());

	}

	private void changeColor(final int newValue) {
		this.colorProperty.set(colors.get(newValue));
		this.effectProperty.set(GuiUtils.buildColorFilter(this.colorProperty.get()));
		this.teintSample.setFill(this.colorProperty.get());
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
		this.floorsService.updateFloor(this.floor);
		final Image newImage = new Image(result.toURI().toString());
		this.imageProperty.set(newImage);
	}

	public ObjectProperty<Image> imageProperty() {
		return this.imageProperty;
	}

	public ObjectProperty<Double> zoomProperty() {
		return this.zoomProperty;
	}

	public ObjectProperty<Double> shiftXProperty() {
		return this.shiftXProperty;
	}

	public ObjectProperty<Double> shiftYProperty() {
		return this.shiftYProperty;
	}

	public CheckBox getVisibleCheck() {
		return this.visibleCheck;
	}

	public Slider getTransparencySlider() {
		return this.transparencySlider;
	}
}
