package org.targol.resoplan.ui.utils;

import java.util.List;
import java.util.Optional;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.model.catalog.enums.NodeCategory;
import org.targol.resoplan.services.NodeModelsService;
import org.targol.resoplan.ui.components.CatalogButton;
import org.targol.resoplan.utils.PreferencesManager;

import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

public class GuiUtils {

	public static Image colorizeImage(final Image originalImage, final Color targetColor) {
		if (originalImage == null) {
			return originalImage;
		}
		final int width = (int) originalImage.getWidth();
		final int height = (int) originalImage.getHeight();
		// if image not yet fully loaded, exit
		if (width <= 0 || height <= 0) {
			return originalImage;
		}
		// Colo black : no change in original image
		if (Color.BLACK.equals(targetColor)) {
			return originalImage;
		}
		final WritableImage tintedImage = new WritableImage(width, height);
		final PixelReader reader = originalImage.getPixelReader();
		final PixelWriter writer = tintedImage.getPixelWriter();

		// Changing black or dark grey pixels with pixels of chosen color
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				final Color pixelColor = reader.getColor(x, y);
				if (pixelColor.getBrightness() < 0.6 && pixelColor.getOpacity() > 0.1) {
					final Color newPixel = new Color(targetColor.getRed(), targetColor.getGreen(),
							targetColor.getBlue(), pixelColor.getOpacity());
					writer.setColor(x, y, newPixel);
				} else {
					writer.setColor(x, y, pixelColor);
				}
			}
		}
		return tintedImage;
	}

	public static Image changeColorInImage(final Image originalImage, final Color initialColor,
			final Color targetColor) {
		if (originalImage == null) {
			return originalImage;
		}
		final int width = (int) originalImage.getWidth();
		final int height = (int) originalImage.getHeight();
		// if image not yet fully loaded, exit
		if (width <= 0 || height <= 0) {
			return originalImage;
		}
		final WritableImage resultImage = new WritableImage(width, height);
		final PixelReader reader = originalImage.getPixelReader();
		final PixelWriter writer = resultImage.getPixelWriter();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				final Color pixelColor = reader.getColor(x, y);
				if (areEqualsColorsExceptTransparency(initialColor, pixelColor)) {
					final Color newColor = new Color(targetColor.getRed(), targetColor.getGreen(),
							targetColor.getBlue(), pixelColor.getOpacity());
					writer.setColor(x, y, newColor);
				} else {
					writer.setColor(x, y, pixelColor);
				}
			}
		}
		return resultImage;
	}

	private static boolean areEqualsColorsExceptTransparency(final Color col1, final Color col2) {
		return col1.getRed() == col2.getRed() && col1.getGreen() == col2.getGreen() && col1.getBlue() == col2.getBlue();
	}

	public static Effect buildColorFilter(final Color color) {
		// Black color means no effect
		if (Color.BLACK.equals(color)) {
			return null;
		}
		final ColorAdjust colorAdjust = new ColorAdjust();
//		final double hueForJavaFX = color.getHue() / 180.0 - 1.0;
//		colorAdjust.setHue(hueForJavaFX);
		colorAdjust.setHue(color.getHue());
		colorAdjust.setSaturation(1.0);
		colorAdjust.setBrightness(0.5);
		return colorAdjust;
	}

	public static Image createImageViewFromInternalPng(final String imageInternalPath) {
		return new Image(GuiUtils.class.getResourceAsStream(imageInternalPath));
	}

	public static void errorAlert(final String msg) {
		genericAlert(Messages.getString("Generic.error.title"), msg, Alert.AlertType.ERROR); //$NON-NLS-1$
	}

	public static void infoAlert(final String msg) {
		genericAlert(Messages.getString("Generic.info.title"), msg, Alert.AlertType.INFORMATION); //$NON-NLS-1$
	}

	public static void testAlert(final String msg) {
		infoAlert(msg);
	}

	public static Optional<String> getTextFromInputDialog(final String title, final String msg,
			final String initialValue, final String regExp) {
		final String ret = null;
		final TextInputDialog prompt;
		if (initialValue == null) {
			prompt = new TextInputDialog();
		} else {
			prompt = new TextInputDialog(initialValue);
		}
		prompt.setTitle(title);
		prompt.setContentText(msg);
//		ThemesManager.getInstance().setTheme(prompt.getDialogPane());
		if (regExp != null) {
			final TextField textField = prompt.getEditor();
			textField.setTextFormatter(new TextFormatter<String>(change -> {
				if (change.getControlNewText().matches(regExp)) {
					return change;
				}
				return null;
			}));
		}
		return prompt.showAndWait();
	}

	private static void genericAlert(final String title, final String contentText, final Alert.AlertType type) {
		final Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setContentText(contentText);
		alert.showAndWait();
	}

	public static HBox buildCategorizeNodeModelsToolbar(final NodeCategory cat, final NodeModelsService modelsService,
			final ToggleGroup placementGroup) {
		final HBox ret = new HBox(5);
		ret.setAlignment(Pos.CENTER_LEFT);
		final Label catLab = new Label(cat.getLabel() + " : ");
		catLab.setStyle("-fx-font-weight: bold;");
		ret.getChildren().add(catLab);
		final List<NodeModel> models = modelsService.getAllByCategory(cat);
		for (final NodeModel model : modelsService.getAllByCategory(cat)) {
			final CatalogButton btn = new CatalogButton(model,
					() -> new AppActionEvent(new EventType<>(AppActionEvent.ANY, model.getName())));
			btn.setToggleGroup(placementGroup); // Partagé pour qu'un seul outil soit actif à la fois
			btn.setUserData(model);
			ret.getChildren().add(btn);
		}
		ret.getChildren().stream().filter(node -> node instanceof CatalogButton)
				.forEach(node -> PreferencesManager.getInstance().addThemeChangeListener((CatalogButton) node));

		return ret;
	}

	// TODO à supprimer ?
	public static HBox buildCategorizeNodeModelsChoiceBox(final NodeCategory cat,
			final NodeModelsService modelsService) {
		final HBox ret = new HBox(10);
		final Label catLab = new Label(cat.getLabel());
		final String desc = cat.getDescription();
		catLab.setTooltip(new Tooltip(desc));
		ret.getChildren().add(catLab);
		final List<NodeModel> models = modelsService.getAllByCategory(cat);
		final ChoiceBox<NodeModel> choice = new ChoiceBox<>();
		choice.setPrefWidth(100);
		choice.getItems().setAll(models);
		choice.setConverter(new StringConverter<NodeModel>() {
			@Override
			public String toString(final NodeModel model) {
				if (model != null) {
					return model.getName();
				}
				return "";
			}

			@Override
			// not used...
			public NodeModel fromString(final String s) {
				return null;
			}
		});
		choice.setTooltip(new Tooltip(desc));
		ret.getChildren().add(choice);

		return ret;
	}

}
