package org.targol.resoplan.ui.panels.floornetwork;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.services.ProjectsService;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

public class FloorsNetworksTab extends TabPane {

	private final Project project;
	private static final ProjectsService SVC_PROJ = SpringContextHelper.getBean(ProjectsService.class);

	private final List<LayeredFloorTab> floorTabs = new ArrayList<>();
	private boolean isSyncing = false;

	public FloorsNetworksTab(final Project project) {
		this.project = SVC_PROJ.openProjectWithFloorsAndNodes(project).get();
		setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
			if (newTab instanceof final LayeredFloorTab layeredTab) {
				// UiEventBus.send(RefreshFloorLayerEvent.floorChanged(layeredTab.getFloor()));
				layeredTab.enableHeaderRadioButtons();
			}
			if (oldTab instanceof final LayeredFloorTab layeredTab) {
				layeredTab.disableHeaderRadioButtons();
			}
		});

		final List<Floor> sortedFloors = project.getFloors().stream().sorted(Comparator.comparingInt(Floor::getNumber))
				.toList();
		boolean first = true;
		for (final Floor floor : sortedFloors) {
			final LayeredFloorTab tab = new LayeredFloorTab(this, this.project, floor, first);
			if (first) {
				first = false;
			}
			this.floorTabs.add(tab);
			getTabs().add(tab);
			final ScrollPane sp = tab.getCenterScrollPane();
			sp.hvalueProperty().addListener((obs, oldVal, newVal) -> syncScrollHorizontal(tab, newVal.doubleValue()));
			sp.vvalueProperty().addListener((obs, oldVal, newVal) -> syncScrollVertical(tab, newVal.doubleValue()));
		}
		getSelectionModel().select(0);
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

				// 1. Récupérer les dimensions du contenu (Group) AVANT le zoom
				final Bounds boundsBefore = scrollPane.getContent().getBoundsInLocal();
				final double widthBefore = boundsBefore.getWidth();
				final double heightBefore = boundsBefore.getHeight();

				if (widthBefore == 0 || heightBefore == 0) {
					continue;
				}

				// 2. Appliquer le zoom de manière strictement identique sur tous les onglets
				mainPane.setScaleX(mainPane.getScaleX() * zoomFactor);
				mainPane.setScaleY(mainPane.getScaleY() * zoomFactor);

				// Forcer JavaFX à recalculer la nouvelle géométrie du Group immédiatement
				((Group) scrollPane.getContent()).layout();

				// 3. Calculer le décalage parfait pour maintenir le point sous la souris
				final double hValue = scrollPane.getHvalue();
				final double vValue = scrollPane.getVvalue();

				// Proportions de la fenêtre visible (viewport) par rapport au contenu
				final double viewportWidth = scrollPane.getViewportBounds().getWidth();
				final double viewportHeight = scrollPane.getViewportBounds().getHeight();

				// Formule magique de repositionnement du ScrollPane
				final double newH = hValue + mouseX * (zoomFactor - 1) / (widthBefore - viewportWidth);
				final double newV = vValue + mouseY * (zoomFactor - 1) / (heightBefore - viewportHeight);

				// Appliquer les nouvelles valeurs bornées entre 0.0 et 1.0
				scrollPane.setHvalue(Math.max(0, Math.min(1, newH)));
				scrollPane.setVvalue(Math.max(0, Math.min(1, newV)));
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
