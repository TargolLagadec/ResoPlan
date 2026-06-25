package org.targol.resoplan.utils;

public record ProjectParams(String name, int nbFloors, int hsp, boolean generateAttic, boolean generateBasement,
		int margin) {

}
