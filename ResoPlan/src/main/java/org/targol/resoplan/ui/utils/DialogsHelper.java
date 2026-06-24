package org.targol.resoplan.ui.utils;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.ui.dialogs.ProjectEditDialogControler;
import org.targol.resoplan.utils.ProjectParams;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;

public class DialogsHelper {

	private static final ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", Locale.getDefault()); //$NON-NLS-1$

	/**
	 * Shows the project edition dialogs and waits for its closure.
	 *
	 * @param prj initial project data, set to <code>null</code> in case of project
	 *            creation
	 * @return modified (or created) project data, empty if dialog has been closed
	 *         with "cancel" button.
	 */
	public static Optional<ProjectParams> showProjectEditorDialog(final Window owner, final ProjectParams prj) {
		final FXMLLoader loader = new FXMLLoader(DialogsHelper.class.getResource("/gui/dialogs/ProjectEditDialog.fxml"), //$NON-NLS-1$
				bundle);
		GridPane dialogContent;
		try {
			dialogContent = loader.load();
		} catch (final IOException e) {
			e.printStackTrace();
			return Optional.empty();
		}
		final ProjectEditDialogControler controller = loader.getController();
		final Dialog<ProjectParams> dialog = new Dialog<>();
		if (prj != null) {
			controller.setProjectData(prj);
			dialog.setTitle(Messages.getString("Dialog.project.title.edit")); //$NON-NLS-1$
		} else {
			dialog.setTitle(Messages.getString("Dialog.project.title.new")); //$NON-NLS-1$
		}
		dialog.initOwner(owner);
		dialog.getDialogPane().setContent(dialogContent);
		final ButtonType buttonTypeOk = new ButtonType(Messages.getString("Generic.dialog.Ok"), //$NON-NLS-1$
				ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);
		final Button lookupButtonOk = (Button) dialog.getDialogPane().lookupButton(buttonTypeOk);

		// Le bouton est désactivé SI la propriété "text" du champ est vide
		lookupButtonOk.disableProperty().bind(controller.getNameTextField().textProperty().isEmpty());

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == buttonTypeOk) {
				return controller.getResult();
			}
			return null;
		});
		return dialog.showAndWait();
	}
}
