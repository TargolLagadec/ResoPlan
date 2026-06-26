package org.targol.resoplan.ui.toolbars;

import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.model.catalog.enums.NodeCategory;
import org.targol.resoplan.services.HookTypesService;
import org.targol.resoplan.services.NodeModelsService;
import org.targol.resoplan.ui.utils.AppStateManager;
import org.targol.resoplan.ui.utils.GuiUtils;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.scene.control.ToggleGroup;

public class EvacToolBar extends DefaultToolBar {
	private final ToggleGroup placementGroup = new ToggleGroup();

	private final NodeModelsService modelsService = SpringContextHelper.getBean(NodeModelsService.class);
	private final HookTypesService hooksService = SpringContextHelper.getBean(HookTypesService.class);

	public EvacToolBar(final Project proj) {
		super();
		final AppStateManager stateMgr = AppStateManager.getInstance();
		for (final NodeCategory cat : NodeCategory.values()) {
			if (cat.toString().contains("PVC")) { //$NON-NLS-1$
				getItems().add(GuiUtils.buildCategorizedNodeModelsToolbar(LayerType.WATER_EVAC, cat, this.modelsService,
						this.placementGroup));
			}
		}
		getItems().add(GuiUtils.buildMiscNodeModelsToolbarFilteredByHookTypes(LayerType.WATER_EVAC, this.modelsService,
				this.placementGroup));
		getItems().add(GuiUtils.buildLinksToolbar(LayerType.WATER_EVAC, this.placementGroup));
	}
}
