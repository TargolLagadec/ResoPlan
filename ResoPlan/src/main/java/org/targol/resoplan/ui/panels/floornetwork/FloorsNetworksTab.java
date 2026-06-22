package org.targol.resoplan.ui.panels.floornetwork;

import java.util.Comparator;
import java.util.List;

import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.ui.utils.AppStateManager;

import javafx.scene.control.TabPane;

public class FloorsNetworksTab extends TabPane {

	public FloorsNetworksTab(final Project proj) {
		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		final AppStateManager state = AppStateManager.getInstance();
		this.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
			if (newTab instanceof final LayeredFloorTab layeredTab) {
				state.setActiveFloor(layeredTab.getFloor());
				state.activeNetworkLayerProperty().set(layeredTab.getCurrentLayer());
				layeredTab.enableHeaderRadioButtons();
			}
			if (oldTab instanceof final LayeredFloorTab layeredTab) {
				layeredTab.disableHeaderRadioButtons();
			}
		});

		final List<Floor> sortedFloors = proj.getFloors().stream().sorted(Comparator.comparingInt(Floor::getNumber))
				.toList();
		boolean first = true;
		for (final Floor floor : sortedFloors) {
			final LayeredFloorTab tab = new LayeredFloorTab(floor, first);
			if (first) {
				first = false;
			}
			this.getTabs().add(tab);
		}
	}
}
