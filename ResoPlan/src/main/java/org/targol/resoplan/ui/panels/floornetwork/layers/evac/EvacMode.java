package org.targol.resoplan.ui.panels.floornetwork.layers.evac;

public enum EvacMode {
	HOME_OUT(100),
	DESCENT_32(32),
	DESCENT_40(40),
	DESCENT_100(100),
	RISE_32(32),
	RISE_40(40),
	RISE_100(100),
	TUBE_32(32),
	TUBE_40(40),
	TUBE_100(100),
	DELETE(0);

	private final int diam;

	private EvacMode(final int diam) {
		this.diam = diam;
	}

	public int getDiam() {
		return this.diam;
	}
}
