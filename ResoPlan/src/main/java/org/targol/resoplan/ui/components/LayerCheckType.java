package org.targol.resoplan.ui.components;

public enum LayerCheckType {
	ELEC("elec"), //$NON-NLS-1$
	EAU_ALIM("arrivee"), //$NON-NLS-1$
	EAU_EVAC("evac"), //$NON-NLS-1$
	NET("net"); //$NON-NLS-1$

	private final String key;

	LayerCheckType(final String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

}
