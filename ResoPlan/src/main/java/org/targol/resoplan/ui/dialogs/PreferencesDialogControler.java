package org.targol.resoplan.ui.dialogs;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.ui.utils.ThemesManager;
import org.targol.resoplan.ui.utils.ThemesManager.Theme;
import org.targol.resoplan.ui.utils.events.ThemeChangeEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;
import org.targol.resoplan.utils.PreferencesManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.StringConverter;

public class PreferencesDialogControler extends Dialog<Void> {

	@FXML
	private ButtonType okButtonType;
	@FXML
	private ChoiceBox<Theme> themeChoiceBox;

	private final Window parentWindow;

	private final ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", Locale.getDefault()); //$NON-NLS-1$

	public PreferencesDialogControler(final Window owner) {
		UiEventBus.register(ThemeChangeEvent.THEME_CHANGE, (event) -> themeChanged(event.getTheme()));
		this.parentWindow = owner;
		try {

			final FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/dialogs/PreferencesDialog.fxml"), //$NON-NLS-1$
					this.bundle);
			loader.setController(this);
			initOwner(owner);
			initModality(Modality.APPLICATION_MODAL);
			setResizable(false);
			setTitle(Messages.getString("Dialog.prefs.title")); //$NON-NLS-1$
			final DialogPane dialogPane = loader.load();
			setDialogPane(dialogPane);
			final Button okButton = (Button) dialogPane.lookupButton(this.okButtonType);
			okButton.addEventFilter(ActionEvent.ACTION, this::onBtnOkClick);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@FXML
	private void initialize() {
		final PreferencesManager prefsMgr = PreferencesManager.getInstance();
//		this.outdirPath.setText(prefsMgr.getPreference(PreferencesManager.PREF_OUTDIR));
		initThemesChoicBox(prefsMgr);
//		this.chkGetSetAuto.setSelected(prefsMgr.getBoolPreference(PreferencesManager.PREF_GENERATE_GETSET));
//		this.chkGetSetAuto.setOnAction(event -> {
//			prefsMgr.setBoolPreference(PreferencesManager.PREF_GENERATE_GETSET, this.chkGetSetAuto.isSelected());
//		});
//		this.chkSvcRepoAuto.setSelected(prefsMgr.getBoolPreference(PreferencesManager.PREF_JPA_GENERATE_REPO_AND_SRV));
//		this.chkSvcRepoAuto.setOnAction(event -> {
//			prefsMgr.setBoolPreference(PreferencesManager.PREF_JPA_GENERATE_REPO_AND_SRV,
//					this.chkSvcRepoAuto.isSelected());
//		});
	}

	@FXML
	private void browseOutdir(final ActionEvent event) {
	}

	@FXML
	private void onBtnOkClick(final ActionEvent event) {
	}

	private void initThemesChoicBox(final PreferencesManager prefsMgr) {
		this.themeChoiceBox.getItems().addAll(Theme.values());
		this.themeChoiceBox.setConverter(new StringConverter<Theme>() {
			@Override
			public String toString(final Theme theme) {
				return theme == null ? "" : theme.getName(); //$NON-NLS-1$
			}

			@Override
			public Theme fromString(final String string) {
				return Theme.valueOf(string);
			}
		});
		this.themeChoiceBox.setValue(prefsMgr.getCurrentTheme());
		this.themeChoiceBox.valueProperty().addListener((obs, oldTheme, newTheme) -> {
			if (newTheme != null) {
				final ThemesManager tm = ThemesManager.getInstance();
				prefsMgr.setCurrentTheme(newTheme);
			}
		});
	}

	public void themeChanged(final Theme newTheme) {
		final String styleSheet = getClass().getResource(newTheme.getCssfilePath()).toExternalForm();
		getDialogPane().getStylesheets().clear();
		getDialogPane().getStylesheets().add(styleSheet);
		this.parentWindow.getScene().getStylesheets().clear();
		this.parentWindow.getScene().getStylesheets().add(styleSheet);
	}
}