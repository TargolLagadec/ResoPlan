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
import org.targol.resoplan.services.NodesService;
import org.targol.resoplan.ui.utils.events.ProblemsUpdatedEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class AppStateManager {

	private static final AppStateManager instance = new AppStateManager();

	public static AppStateManager getInstance() {
		return instance;
	}

	private final ObjectProperty<Map<Integer, Integer>> nodeIdToFloorId = new SimpleObjectProperty<>(new HashMap<>());
	private final ObjectProperty<Project> currentProject = new SimpleObjectProperty<>(null);
//	private final ObjectProperty<AppState> currentAppState = new SimpleObjectProperty<>(AppState.NO_PROJECT);

//	private final ObjectProperty<Floor> activeFloor = new SimpleObjectProperty<>(null);
	private final ObjectProperty<LayerType> activeNetworkLayer = new SimpleObjectProperty<>(null);

	private final FloorsService floorsSvc = SpringContextHelper.getBean(FloorsService.class);
	private final NodesService nodesSvc = SpringContextHelper.getBean(NodesService.class);

	private AppStateManager() {
		this.currentProject.addListener((obs, oldProj, newProj) -> {
			if (newProj == null) {
//				this.activeFloor.set(null);
				this.activeNetworkLayer.set(null);
			}
		});
		UiEventBus.register(null, ProblemsUpdatedEvent.REBUILD_MAP, (evt) -> recalculateNodeIdToFloorId());
	}

	public ObjectProperty<Project> currentProjectProperty() {
		return this.currentProject;
	}

	public ObjectProperty<Map<Integer, Integer>> nodeIdToFloorId() {
		return this.nodeIdToFloorId;
	}

	private void recalculateNodeIdToFloorId() {
		final Map<Integer, Integer> nodeToFloorMap = new HashMap<>();
		if (this.currentProject.get() != null) {
			for (final Floor floor : this.currentProject.get().getFloors()) {
				final Floor realFloor = this.floorsSvc.reloadWithNodes(floor).get();
				for (final AbstractNode node : realFloor.getNodes()) {
					nodeToFloorMap.put(node.getId(), floor.getId());
					// Si c'est un MetaNode, on indexe aussi ses sous-nœuds manuellement
					if (node instanceof final MetaNode meta) {
						final MetaNode fullMeta = this.nodesSvc.getfullMetaNodeWithChidrenNodes(meta).get();
						for (final Node subNode : fullMeta.getNodes()) {
							nodeToFloorMap.put(subNode.getId(), floor.getId());
						}
					}
				}
			}
		}
		this.nodeIdToFloorId.set(nodeToFloorMap);
		UiEventBus.send(ProblemsUpdatedEvent.fireCheck(this.currentProject.get()));
	}

//	public ObjectProperty<Floor> activeFloorProperty() {
//		return this.activeFloor;
//	}

	public ObjectProperty<LayerType> activeNetworkLayerProperty() {
		return this.activeNetworkLayer;
	}

	public void setOpenedProject(final Project project) {
		this.currentProject.set(project);
	}

//	public void setActiveFloor(final Floor floor) {
//		this.activeFloor.set(floor);
//	}

	public void setActiveNetworkLayer(final LayerType layer) {
		this.activeNetworkLayer.set(layer);
	}
}