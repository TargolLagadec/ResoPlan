package org.targol.resoplan.ui.utils;

import java.util.Optional;

import org.targol.resoplan.i18n.Messages;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GuiUtils {

	public static ColorAdjust buildColorFilter(final Color color) {
		final ColorAdjust monochrome = new ColorAdjust();
		monochrome.setHue(color.getHue());
		monochrome.setSaturation(color.getSaturation());
		monochrome.setContrast(0.5);
		return monochrome;
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
