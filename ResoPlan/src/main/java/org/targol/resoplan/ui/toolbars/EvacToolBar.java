package org.targol.resoplan.ui.toolbars;

import java.util.List;

import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.model.enums.LayerType;
import org.targol.resoplan.ui.components.CustomButton;
import org.targol.resoplan.ui.panels.floornetwork.layers.evac.EvacMode;
import org.targol.resoplan.ui.utils.AppActionEvent;
import org.targol.resoplan.ui.utils.AppState;
import org.targol.resoplan.ui.utils.AppStateManager;
import org.targol.resoplan.utils.PreferencesManager;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Separator;

public class EvacToolBar extends DefaultToolBar {
	private CustomButton toolEvacSortie;
	private CustomButton toolEvacDown3;
	private CustomButton toolEvacDown4;
	private CustomButton toolEvacDown10;
	private CustomButton toolEvacUp3;
	private CustomButton toolEvacUp4;
	private CustomButton toolEvacUp10;
	private CustomButton toolEvacTube3;
	private CustomButton toolEvacTube4;
	private CustomButton toolEvacTube10;

	public EvacToolBar(final Project proj) {
		super();
		final AppStateManager stateMgr = AppStateManager.getInstance();

		this.toolEvacSortie = new CustomButton("evac_sortie", //$NON-NLS-1$
				() -> new AppActionEvent(AppActionEvent.EVAC_MODE_CHANGED, EvacMode.HOME_OUT));
		this.toolEvacDown3 = new CustomButton("evac_descente3", //$NON-NLS-1$
				() -> new AppActionEvent(AppActionEvent.EVAC_MODE_CHANGED, EvacMode.DESCENT_32));
		this.toolEvacDown4 = new CustomButton("evac_descente4", //$NON-NLS-1$
				() -> new AppActionEvent(AppActionEvent.EVAC_MODE_CHANGED, EvacMode.DESCENT_40));
		this.toolEvacDown10 = new CustomButton("evac_descente10", //$NON-NLS-1$
				() -> new AppActionEvent(AppActionEvent.EVAC_MODE_CHANGED, EvacMode.DESCENT_100));
		this.toolEvacUp3 = new CustomButton("evac_montee3", //$NON-NLS-1$
				() -> new AppActionEvent(AppActionEvent.EVAC_MODE_CHANGED, EvacMode.RISE_32));
		this.toolEvacUp4 = new CustomButton("evac_montee4", //$NON-NLS-1$
				() -> new AppActionEvent(AppActionEvent.EVAC_MODE_CHANGED, EvacMode.RISE_40));
		this.toolEvacUp10 = new CustomButton("evac_montee10", //$NON-NLS-1$
				() -> new AppActionEvent(AppActionEvent.EVAC_MODE_CHANGED, EvacMode.RISE_100));
		this.toolEvacTube3 = new CustomButton("evac_tube3", //$NON-NLS-1$
				() -> new AppActionEvent(AppActionEvent.EVAC_MODE_CHANGED, EvacMode.TUBE_32));
		this.toolEvacTube4 = new CustomButton("evac_tube4", //$NON-NLS-1$
				() -> new AppActionEvent(AppActionEvent.EVAC_MODE_CHANGED, EvacMode.TUBE_40));
		this.toolEvacTube10 = new CustomButton("evac_tube10", //$NON-NLS-1$
				() -> new AppActionEvent(AppActionEvent.EVAC_MODE_CHANGED, EvacMode.TUBE_100));

		this.getItems().addAll(this.toolEvacSortie, new Separator(), this.toolEvacDown3, this.toolEvacDown4,
				this.toolEvacDown10, new Separator(), this.toolEvacUp3, this.toolEvacUp4, this.toolEvacUp10,
				new Separator(), this.toolEvacTube3, this.toolEvacTube4, this.toolEvacTube10);

		final BooleanBinding baseEvacDisable = stateMgr.currentAppStateProperty().isNotEqualTo(AppState.PROJECT_READY)
				.or(stateMgr.activeFloorProperty().isNull())
				.or(stateMgr.activeNetworkLayerProperty().isNotEqualTo(LayerType.WATER_EVAC));

		final BooleanBinding sortieDisable = baseEvacDisable.or(Bindings.createBooleanBinding(() -> {
			final Floor f = stateMgr.activeFloorProperty().get();
			return f == null || f.getNumber() != 0;
		}, stateMgr.activeFloorProperty()));

		final BooleanBinding downDisable = baseEvacDisable.or(Bindings.createBooleanBinding(() -> {
			final Floor f = stateMgr.activeFloorProperty().get();
			return f != null && f.getNumber() == 0;
		}, stateMgr.activeFloorProperty()));

		final BooleanBinding upDisable = baseEvacDisable.or(Bindings.createBooleanBinding(() -> {
			final Floor f = stateMgr.activeFloorProperty().get();
			if (f == null) {
				return true;
			}
			return f.getNumber() == proj.getFloors().size() - 1;
		}, stateMgr.activeFloorProperty()));

		List.of(this.toolEvacTube3, this.toolEvacTube4, this.toolEvacTube10)
				.forEach(b -> b.disableProperty().bind(baseEvacDisable));
		List.of(this.toolEvacDown3, this.toolEvacDown4, this.toolEvacDown10)
				.forEach(b -> b.disableProperty().bind(downDisable));
		List.of(this.toolEvacUp3, this.toolEvacUp4, this.toolEvacUp10)
				.forEach(b -> b.disableProperty().bind(upDisable));
		this.toolEvacSortie.disableProperty().bind(sortieDisable);

		this.getItems().stream().filter(node -> node instanceof CustomButton)
				.forEach(node -> PreferencesManager.getInstance().addThemeChangeListener((CustomButton) node));
	}
}
