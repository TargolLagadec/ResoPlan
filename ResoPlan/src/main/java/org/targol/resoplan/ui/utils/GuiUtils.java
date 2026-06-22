package org.targol.resoplan.ui.utils;

import java.util.Optional;

import org.targol.resoplan.i18n.Messages;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

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

}
