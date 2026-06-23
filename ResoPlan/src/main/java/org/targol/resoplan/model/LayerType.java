package org.targol.resoplan.model;

public enum LayerType {
	ELEC("elec"), //$NON-NLS-1$
	EAU_ALIM("arrivee"), //$NON-NLS-1$
	EAU_EVAC("evac"), //$NON-NLS-1$
	NET("net"); //$NON-NLS-1$

	private final String key;

	LayerType(final String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

}
