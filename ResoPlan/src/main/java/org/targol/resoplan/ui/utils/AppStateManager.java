package org.targol.resoplan.ui.utils;

import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.Project;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class AppStateManager {

	private static final AppStateManager instance = new AppStateManager();

	public static AppStateManager getInstance() {
		return instance;
	}

	private final ObjectProperty<Project> currentProject = new SimpleObjectProperty<>(null);
	private final ObjectProperty<LayerType> activeNetworkLayer = new SimpleObjectProperty<>(null);

	private AppStateManager() {
		this.currentProject.addListener((obs, oldProj, newProj) -> {
			if (newProj == null) {
//				this.activeFloor.set(null);
				this.activeNetworkLayer.set(null);
			}
		});
	}

	public ObjectProperty<Project> currentProjectProperty() {
		return this.currentProject;
	}

	public ObjectProperty<LayerType> activeNetworkLayerProperty() {
		return this.activeNetworkLayer;
	}

	public void setOpenedProject(final Project project) {
		this.currentProject.set(project);
	}

	public void setActiveNetworkLayer(final LayerType layer) {
		this.activeNetworkLayer.set(layer);
	}
}