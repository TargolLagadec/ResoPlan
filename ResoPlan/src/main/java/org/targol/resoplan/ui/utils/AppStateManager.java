package org.targol.resoplan.ui.utils;

import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.ui.components.LayerRadioType;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class AppStateManager {

	private static final AppStateManager instance = new AppStateManager();

	public static AppStateManager getInstance() {
		return instance;
	}

	private final ObjectProperty<Project> currentProject = new SimpleObjectProperty<>(null);
	private final ObjectProperty<AppState> currentAppState = new SimpleObjectProperty<>(AppState.NO_PROJECT);

	private final ObjectProperty<Floor> activeFloor = new SimpleObjectProperty<>(null);
	private final ObjectProperty<LayerRadioType> activeNetworkLayer = new SimpleObjectProperty<>(null);

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
		final boolean missingImage = project.getFloors().stream()
				.anyMatch(f -> f.getImgPath() == null || f.getImgPath().isEmpty());

		if (missingImage) {
			this.currentAppState.set(AppState.PROJECT_WITHOUT_IMAGES);
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

	public ObjectProperty<LayerRadioType> activeNetworkLayerProperty() {
		return this.activeNetworkLayer;
	}

	public void setOpenedProject(final Project project) {
		this.currentProject.set(project);
		updateProjectState(project);
	}

	public void setActiveFloor(final Floor floor) {
		this.activeFloor.set(floor);
	}

	public void setActiveNetworkLayer(final LayerRadioType layer) {
		this.activeNetworkLayer.set(layer);
	}
}