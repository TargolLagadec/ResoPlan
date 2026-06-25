package org.targol.resoplan.model.catalog.enums;

import org.targol.resoplan.i18n.Messages;

public enum NodeCross {
	NONE,
	GOES_UP,
	GOES_DOWN;

	public String getLabel() {
		return Messages.getString("NodeCross." + this.toString()); //$NON-NLS-1$
	}

	public String getDescription() {
		return Messages.getString("NodeCross." + this.toString() + ".desc"); //$NON-NLS-1$ //$NON-NLS-2$
	}

}