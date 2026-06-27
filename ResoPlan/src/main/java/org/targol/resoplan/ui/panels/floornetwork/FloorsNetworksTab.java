package org.targol.resoplan.ui.panels.floornetwork;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.ui.utils.AppStateManager;
import org.targol.resoplan.ui.utils.events.LinkTracingEvent;
import org.targol.resoplan.ui.utils.events.NodePlacementEvent;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

public class FloorsNetworksTab extends TabPane {

	private final List<LayeredFloorTab> floorTabs = new ArrayList<>();
	private boolean isSyncing = false;

	public FloorsNetworksTab(final Project proj) {
		this.addEventHandler(NodePlacementEvent.PLACEMENT_ANY, event -> {
			System.out.println(
					"Dans FloorsNetworksTab, réception d'un event de type NodePlacementEvent : " + event.getModel());
			final Tab activeTab = this.getSelectionModel().getSelectedItem();
			if (activeTab instanceof final LayeredFloorTab layeredTab) {
				layeredTab.handleEvent(event);
			}
			event.consume();
		});
		this.addEventHandler(LinkTracingEvent.HOOK_ANY, event -> {
			System.out.println("Dans FloorsNetworksTab, réception d'un event de type LinkTracingEvent.HOOK_ANY : "
					+ event.getHook());
			final Tab activeTab = this.getSelectionModel().getSelectedItem();
			if (activeTab instanceof final LayeredFloorTab layeredTab) {
				layeredTab.handleEvent(event);
			}
			event.consume();
		});

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
	void syncZoom(final double zoomFactor, final double mouseX, final double mouseY) {
		if (this.isSyncing) {
			return;
		}
		try {
			this.isSyncing = true;
			for (final LayeredFloorTab tab : this.floorTabs) {
				final ScrollPane scrollPane = tab.getCenterScrollPane();
				final Pane mainPane = tab.getMainNetworkPane();
				// Position de la souris au moment du zoom poue centrer dessus.
				final double hBoundsWidth = scrollPane.getContent().getBoundsInLocal().getWidth();
				final double vBoundsHeight = scrollPane.getContent().getBoundsInLocal().getHeight();
				if (hBoundsWidth == 0 || vBoundsHeight == 0) {
					continue;
				}
				mainPane.setScaleX(mainPane.getScaleX() * zoomFactor);
				mainPane.setScaleY(mainPane.getScaleY() * zoomFactor);
				tab.getCenterScrollPane().requestLayout();
				Platform.runLater(() -> {
					final double hValue = scrollPane.getHvalue();
					final double vValue = scrollPane.getVvalue();
					final double newH = hValue + mouseX / hBoundsWidth * (zoomFactor - 1)
							* (scrollPane.getViewportBounds().getWidth() / hBoundsWidth);
					final double newV = vValue + mouseY / vBoundsHeight * (zoomFactor - 1)
							* (scrollPane.getViewportBounds().getHeight() / vBoundsHeight);
					scrollPane.setHvalue(Math.max(0, Math.min(1, newH)));
					scrollPane.setVvalue(Math.max(0, Math.min(1, newV)));
				});
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
