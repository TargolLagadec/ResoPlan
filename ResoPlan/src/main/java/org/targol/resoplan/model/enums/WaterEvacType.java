package org.targol.resoplan.model.enums;

import org.targol.resoplan.i18n.Messages;

public enum WaterEvacType {
	PVC_32("pvc32"), //$NON-NLS-1$
	PVC_40("pvc40"), //$NON-NLS-1$
	PVC_100("pvc100"); //$NON-NLS-1$

	private final String libKey;

	private WaterEvacType(final String libKey) {
		this.libKey = libKey;
	}

	public String getShortLib() {
		return Messages.getString("WaterEvacType." + this.libKey + ".short"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String getLongLib() {
		return Messages.getString("WaterEvacType." + this.libKey + ".long"); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
