package org.targol.resoplan.ui.toolbars;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.ui.components.CustomButton;
import org.targol.resoplan.ui.utils.AppActionEvent;
import org.targol.resoplan.ui.utils.AppState;
import org.targol.resoplan.ui.utils.BindingBuilder;
import org.targol.resoplan.utils.PreferencesManager;

import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DefaultToolBar extends ToolBar {
	private final CustomButton toolCatalog;
	private final CustomButton toolAlign;
	private final CustomButton toolNetworks;
	private final CustomButton toolDebit;

	public DefaultToolBar() {
		super();
		this.toolCatalog = new CustomButton("catalog", () -> new AppActionEvent(AppActionEvent.TRIGGER_CATALOG)); //$NON-NLS-1$
		this.toolAlign = new CustomButton("alignment", () -> new AppActionEvent(AppActionEvent.TRIGGER_ALIGN)); //$NON-NLS-1$
		this.toolNetworks = new CustomButton("reseaux", () -> new AppActionEvent(AppActionEvent.TRIGGER_NETWORKS)); //$NON-NLS-1$
		this.toolDebit = new CustomButton("debit", () -> new AppActionEvent(AppActionEvent.TRIGGER_DEBIT)); //$NON-NLS-1$
		final VBox ret = new VBox(5);
		ret.getStyleClass().add("toolgroup"); //$NON-NLS-1$
		ret.setAlignment(Pos.CENTER_LEFT);
		final Label label = new Label(Messages.getString("MainWindow.mnu_aff")); //$NON-NLS-1$
		final HBox buttons = new HBox(5);
		buttons.getChildren().addAll(this.toolCatalog, this.toolAlign, this.toolNetworks, this.toolDebit);
		buttons.getChildren().stream().filter(node -> node instanceof CustomButton)
				.forEach(node -> PreferencesManager.getInstance().addThemeChangeListener((CustomButton) node));
		ret.getChildren().add(label);
		ret.getChildren().add(buttons);
		this.getItems().addAll(ret, new Separator());
		this.toolAlign.disableProperty().bind(BindingBuilder.disableWhen().stateIs(AppState.NO_PROJECT).build());
		final BooleanBinding alimEtDebitDisable = BindingBuilder.disableWhen()
				.stateIs(AppState.NO_PROJECT, AppState.PROJECT_WITHOUT_IMAGES).build();
		this.toolNetworks.disableProperty().bind(alimEtDebitDisable);
		this.toolDebit.disableProperty().bind(alimEtDebitDisable);
	}
}
