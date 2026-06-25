package org.targol.resoplan.ui.panels.floornetwork;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.ui.utils.AppStateManager;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

public class FloorsNetworksTab extends TabPane {

	private final List<LayeredFloorTab> floorTabs = new ArrayList<>();
	private boolean isSyncing = false;

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
			final LayeredFloorTab tab = new LayeredFloorTab(this, floor, first);
			if (first) {
				first = false;
			}
			this.floorTabs.add(tab);
			this.getTabs().add(tab);
			final ScrollPane sp = tab.getCenterScrollPane();
			sp.hvalueProperty().addListener((obs, oldVal, newVal) -> syncScrollHorizontal(tab, newVal.doubleValue()));
			sp.vvalueProperty().addListener((obs, oldVal, newVal) -> syncScrollVertical(tab, newVal.doubleValue()));
		}
	}

	/**
	 * Zoom Syncro on all panels whatever the source panel is.
	 *
	 * @param hValue zoom value
	 */
	void syncZoom(final double zoomFactor) {
		if (this.isSyncing) {
			return;
		}
		try {
			this.isSyncing = true;
			for (final LayeredFloorTab tab : this.floorTabs) {
				final Pane mainPane = tab.getMainNetworkPane();
				mainPane.setScaleX(mainPane.getScaleX() * zoomFactor);
				mainPane.setScaleY(mainPane.getScaleY() * zoomFactor);
				tab.getCenterScrollPane().layout();
			}
		} finally {
			this.isSyncing = false;
		}
	}

	private void syncScrollHorizontal(final LayeredFloorTab sourceTab, final double hValue) {
		if (this.isSyncing) {
			return;
		}
		try {
			this.isSyncing = true;
			for (final LayeredFloorTab tab : this.floorTabs) {
				if (tab != sourceTab) {
					tab.getCenterScrollPane().setHvalue(hValue);
				}
			}
		} finally {
			this.isSyncing = false;
		}
	}

	private void syncScrollVertical(final LayeredFloorTab sourceTab, final double vValue) {
		if (this.isSyncing) {
			return;
		}
		try {
			this.isSyncing = true;
			for (final LayeredFloorTab tab : this.floorTabs) {
				if (tab != sourceTab) {
					tab.getCenterScrollPane().setVvalue(vValue);
				}
			}
		} finally {
			this.isSyncing = false;
		}
	}

}
