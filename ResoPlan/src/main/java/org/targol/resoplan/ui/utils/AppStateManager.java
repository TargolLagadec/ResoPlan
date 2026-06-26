package org.targol.resoplan.ui.utils;

import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.Project;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Region;

public class AppStateManager {

	private static final AppStateManager instance = new AppStateManager();

	public static AppStateManager getInstance() {
		return instance;
	}

	private final ObjectProperty<Project> currentProject = new SimpleObjectProperty<>(null);
	private final ObjectProperty<AppState> currentAppState = new SimpleObjectProperty<>(AppState.NO_PROJECT);

	private final ObjectProperty<Floor> activeFloor = new SimpleObjectProperty<>(null);
	private final ObjectProperty<LayerType> activeNetworkLayer = new SimpleObjectProperty<>(null);

	private final ObjectProperty<Region> currentMainPanel = new SimpleObjectProperty<>(null);

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