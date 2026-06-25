package org.targol.resoplan.ui.toolbars;

import org.targol.resoplan.ui.components.CustomButton;
import org.targol.resoplan.ui.utils.AppActionEvent;
import org.targol.resoplan.ui.utils.AppState;
import org.targol.resoplan.ui.utils.AppStateManager;
import org.targol.resoplan.utils.PreferencesManager;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;

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

		final ObjectProperty<AppState> state = AppStateManager.getInstance().currentAppStateProperty();
		this.getItems().addAll(this.toolCatalog, this.toolAlign, this.toolNetworks, this.toolDebit, new Separator());
		this.toolAlign.disableProperty().bind(state.isEqualTo(AppState.NO_PROJECT));
		this.toolNetworks.disableProperty()
				.bind(state.isEqualTo(AppState.NO_PROJECT).or(state.isEqualTo(AppState.PROJECT_WITHOUT_IMAGES)));
		this.toolDebit.disableProperty()
				.bind(state.isEqualTo(AppState.NO_PROJECT).or(state.isEqualTo(AppState.PROJECT_WITHOUT_IMAGES)));
		this.getItems().stream().filter(node -> node instanceof CustomButton)
				.forEach(node -> PreferencesManager.getInstance().addThemeChangeListener((CustomButton) node));
	}
}
