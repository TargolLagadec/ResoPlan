package org.targol.resoplan.model.enums;

import org.targol.resoplan.i18n.Messages;

public enum WaterAlimType {
	PER_12("per12"), //$NON-NLS-1$
	PER_16("per16"), //$NON-NLS-1$
	PER_20("per20"); //$NON-NLS-1$

	private final String libKey;

	private WaterAlimType(final String libKey) {
		this.libKey = libKey;
	}

	public String getShortLib() {
		return Messages.getString("WaterAlimType." + this.libKey + ".short"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String getLongLib() {
		return Messages.getString("WaterAlimType." + this.libKey + ".long"); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
