package org.targol.resoplan.ui.toolbars;

import org.targol.resoplan.model.Project;
import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.model.catalog.enums.NodeCategory;
import org.targol.resoplan.services.NodeModelsService;
import org.targol.resoplan.ui.components.CustomButton;
import org.targol.resoplan.ui.utils.AppStateManager;
import org.targol.resoplan.ui.utils.GuiUtils;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class EvacToolBar extends DefaultToolBar {
	private ChoiceBox<NodeModel> pvc100ChoiceBox;
	private ChoiceBox<NodeModel> pvc40ChoiceBox;
	private ChoiceBox<NodeModel> pvc32ChoiceBox;

	private CustomButton toolEvacTube3;
	private CustomButton toolEvacTube4;
	private CustomButton toolEvacTube10;
	private final ToggleGroup placementGroup = new ToggleGroup();

	private final NodeModelsService modelsService = SpringContextHelper.getBean(NodeModelsService.class);

	public EvacToolBar(final Project proj) {
		super();
		final AppStateManager stateMgr = AppStateManager.getInstance();
		for (final NodeCategory cat : NodeCategory.values()) {
			if (cat.toString().contains("PVC")) { //$NON-NLS-1$
				final HBox tool = GuiUtils.buildCategorizeNodeModelsToolbar(cat, this.modelsService,
						this.placementGroup);
				getItems().add(tool);
			}
		}

//		this.toolEvacTube3 = new CustomButton("evac_tube3", //$NON-NLS-1$
//				() -> new AppActionEvent(AppActionEvent.EVAC_MODE_CHANGED, EvacMode.TUBE_32));
//		this.toolEvacTube4 = new CustomButton("evac_tube4", //$NON-NLS-1$
//				() -> new AppActionEvent(AppActionEvent.EVAC_MODE_CHANGED, EvacMode.TUBE_40));
//		this.toolEvacTube10 = new CustomButton("evac_tube10", //$NON-NLS-1$
//				() -> new AppActionEvent(AppActionEvent.EVAC_MODE_CHANGED, EvacMode.TUBE_100));
//
//		this.getItems().addAll(this.toolEvacSortie, new Separator(), this.toolEvacDown3, this.toolEvacDown4,
//				this.toolEvacDown10, new Separator(), this.toolEvacUp3, this.toolEvacUp4, this.toolEvacUp10,
//				new Separator(), this.toolEvacTube3, this.toolEvacTube4, this.toolEvacTube10);
//
//		final BooleanBinding baseEvacDisable = stateMgr.currentAppStateProperty().isNotEqualTo(AppState.PROJECT_READY)
//				.or(stateMgr.activeFloorProperty().isNull())
//				.or(stateMgr.activeNetworkLayerProperty().isNotEqualTo(LayerType.WATER_EVAC));
//
//		final BooleanBinding sortieDisable = baseEvacDisable.or(Bindings.createBooleanBinding(() -> {
//			final Floor f = stateMgr.activeFloorProperty().get();
//			return f == null || f.getNumber() != 0;
//		}, stateMgr.activeFloorProperty()));
//
//		final BooleanBinding downDisable = baseEvacDisable.or(Bindings.createBooleanBinding(() -> {
//			final Floor f = stateMgr.activeFloorProperty().get();
//			return f != null && f.getNumber() == 0;
//		}, stateMgr.activeFloorProperty()));
//
//		final BooleanBinding upDisable = baseEvacDisable.or(Bindings.createBooleanBinding(() -> {
//			final Floor f = stateMgr.activeFloorProperty().get();
//			if (f == null) {
//				return true;
//			}
//			return f.getNumber() == proj.getFloors().size() - 1;
//		}, stateMgr.activeFloorProperty()));
//
//		List.of(this.toolEvacTube3, this.toolEvacTube4, this.toolEvacTube10)
//				.forEach(b -> b.disableProperty().bind(baseEvacDisable));
//		List.of(this.toolEvacDown3, this.toolEvacDown4, this.toolEvacDown10)
//				.forEach(b -> b.disableProperty().bind(downDisable));
//		List.of(this.toolEvacUp3, this.toolEvacUp4, this.toolEvacUp10)
//				.forEach(b -> b.disableProperty().bind(upDisable));
//		this.toolEvacSortie.disableProperty().bind(sortieDisable);
//
//		this.getItems().stream().filter(node -> node instanceof CustomButton)
//				.forEach(node -> PreferencesManager.getInstance().addThemeChangeListener((CustomButton) node));
	}
}
