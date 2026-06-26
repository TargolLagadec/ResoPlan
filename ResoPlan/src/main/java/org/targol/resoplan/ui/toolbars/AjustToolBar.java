package org.targol.resoplan.ui.toolbars;

import org.targol.resoplan.model.Project;
import org.targol.resoplan.services.NodeModelsService;
import org.targol.resoplan.ui.components.CustomButton;
import org.targol.resoplan.ui.utils.events.AjustEvent;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.scene.control.ToggleGroup;

public class AjustToolBar extends DefaultToolBar {
	private final ToggleGroup placementGroup = new ToggleGroup();

	private final NodeModelsService modelsService = SpringContextHelper.getBean(NodeModelsService.class);

	public AjustToolBar(final Project proj) {
		super();
		getItems().add(new CustomButton("echelle", () -> AjustEvent.fireScaleLineStartRequired())); //$NON-NLS-1$
	}
}
