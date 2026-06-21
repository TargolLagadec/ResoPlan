package org.targol.resoplan.ui.utils;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.ui.panels.LayeredTab;

import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;

public class PanelBuilder {

	public static TabPane buildProjectTabPane(final Project proj) {
		final TabPane ret = new TabPane();
		for (final Floor floor : proj.getFloors()) {
			final LayeredTab tab = new LayeredTab(Messages.getString("ProjectPane.floorname", floor.getNumber())); //$NON-NLS-1$
			ret.getTabs().add(tab);
		}
		ret.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		return ret;
	}
}
