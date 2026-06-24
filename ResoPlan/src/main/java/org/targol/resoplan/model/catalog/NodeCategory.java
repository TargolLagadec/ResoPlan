package org.targol.resoplan.model.catalog;

import org.targol.resoplan.i18n.Messages;

public enum NodeCategory {
	TABLEAU,
	MURALES,
	PLAFOND,
	DIVERS,
	JONCTIONS_PVC_32,
	JONCTIONS_PVC_40,
	JONCTIONS_PVC_100,
	JONCTIONS_PVC,
	JONCTIONS_PER_12,
	JONCTIONS_PER_16,
	JONCTIONS_PER_20,
	JONCTIONS_PER;

	public String getLabel() {
		return Messages.getString(toString());
	}
}
