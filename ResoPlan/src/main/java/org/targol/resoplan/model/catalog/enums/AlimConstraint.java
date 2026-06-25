package org.targol.resoplan.model.catalog.enums;

import org.targol.resoplan.i18n.Messages;

public enum AlimConstraint {
	COLD,
	HOT,
	BOTH,
	ANY,
	SAME,
	NONE;

	public String getLabel() {
		return Messages.getString("AlimConstraint." + this.toString()); //$NON-NLS-1$
	}

	public String getDescription() {
		return Messages.getString("AlimConstraint." + this.toString() + ".desc"); //$NON-NLS-1$ //$NON-NLS-2$
	}

}
