package org.targol.resoplan.ui.utils;

import java.util.HashMap;
import java.util.Map;

import org.targol.resoplan.model.AbstractNode;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.MetaNode;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.services.FloorsService;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Region;

public class AppStateManager {

	private static final AppStateManager instance = new AppStateManager();

	public static AppStateManager getInstance() {
		return instance;
	}

	private final ObjectProperty<Map<Integer, Integer>> nodeIdToFloorId = new SimpleObjectProperty<>(new HashMap<>());
	private final ObjectProperty<Project> currentProject = new SimpleObjectProperty<>(null);
	private final ObjectProperty<AppState> currentAppState = new SimpleObjectProperty<>(AppState.NO_PROJECT);

	private final ObjectProperty<Floor> activeFloor = new SimpleObjectProperty<>(null);
	private final ObjectProperty<LayerType> activeNetworkLayer = new SimpleObjectProperty<>(null);

	private final ObjectProperty<Region> currentMainPanel = new SimpleObjectProperty<>(null);

	private final FloorsService floorsSvc = SpringContextHelper.getBean(FloorsService.class);

	private AppStateManager() {
		this.currentProject.addListener((obs, oldProj, newProj) -> {
			if (newProj == null) {
				this.currentAppState.set(AppState.NO_PROJECT);
				this.activeFloor.set(null);
				this.activeNetworkLayer.set(null);
			} else {
				updateProjectState(newProj);
			}
		});
	}

	public void updateProjectState(final Project project) {
		if (project == null) {
			this.currentAppState.set(AppState.NO_PROJECT);
			return;
		}
		recalculateNodeIdToFloorId();
		final boolean missingImage = project.getFloors().stream()
				.anyMatch(f -> f.getImgPath() == null || f.getImgPath().isEmpty());

		if (missingImage || project.getPlansScale() == 0) {
			this.currentAppState.set(AppState.PROJECT_INCOMPLETE);
		} else {
			this.currentAppState.set(AppState.PROJECT_READY);
		}
	}

	public ObjectProperty<Project> currentProjectProperty() {
		return this.currentProject;
	}

	public ObjectProperty<AppState> currentAppStateProperty() {
		return this.currentAppState;
	}

	public ObjectProperty<Map<Integer, Integer>> nodeIdToFloorId() {
		return this.nodeIdToFloorId;
	}

	public void recalculateNodeIdToFloorId() {
		Map<Integer, Integer> nodeToFloorMap = new HashMap<>();
		for (Floor floor : this.currentProject.get().getFloors()) {
			Floor realFloor = this.floorsSvc.reloadWithNodes(floor).get();
			for (AbstractNode node : realFloor.getNodes()) {
				nodeToFloorMap.put(node.getId(), floor.getId());
				// Si c'est un MetaNode, on indexe aussi ses sous-nœuds manuellement
				if (node instanceof MetaNode meta) {
					for (Node subNode : meta.getNodes()) {
						nodeToFloorMap.put(subNode.getId(), floor.getId());
					}
				}
			}
		}
	}

	public ObjectProperty<Floor> activeFloorProperty() {
		return this.activeFloor;
	}

	public ObjectProperty<LayerType> activeNetworkLayerProperty() {
		return this.activeNetworkLayer;
	}

	public void setOpenedProject(final Project project) {
		this.currentProject.set(project);
		updateProjectState(project);
	}

	public void setActiveFloor(final Floor floor) {
		this.activeFloor.set(floor);
	}

	public void setActiveNetworkLayer(final LayerType layer) {
		this.activeNetworkLayer.set(layer);
	}

	public ObjectProperty<Region> currentMainPanelProperty() {
		return this.currentMainPanel;
	}

	public void setCurrentOpenedMainPanel(final Region panel) {
		this.currentMainPanel.set(panel);
	}

	public Region getCurrentOpenedMainPanel() {
		return this.currentMainPanel.get();
	}

}