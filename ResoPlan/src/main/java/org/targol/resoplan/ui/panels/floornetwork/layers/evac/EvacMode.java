package org.targol.resoplan.ui.panels.floornetwork.layers.evac;

public enum EvacMode {
	DESCENT, // Pour tracer une descente de niveau à l'endroit du clic
	RISE, // Pour tracer une montée de niveau à l'endroit du clic
	TUBE, // pour tracer un tuyau "horizontal" qui reste dans le même étage
	DELETE; // Pour effecer l'élément cliqué
}
