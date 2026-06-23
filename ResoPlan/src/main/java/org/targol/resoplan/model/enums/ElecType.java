package org.targol.resoplan.model.enums;

import org.targol.resoplan.i18n.Messages;

public enum ElecType {
	PRISE_3X25("prise"), //$NON-NLS-1$
	ECLAIRAGE_COMMUN_NEUTRE_ET_TERRE_2X15("eclcommun"), //$NON-NLS-1$
	ECLAIRAGE_COMMANDE_PHASE_1X15("eclcommand"), //$NON-NLS-1$
	PILOTE_TELERUPTEUR_COULEUR_2X15("telrupt"), //$NON-NLS-1$
	ENTRE_VAETVIENT_COULEUR_2X15("vaetvien"); //$NON-NLS-1$

	private final String libKey;

	private ElecType(final String libKey) {
		this.libKey = libKey;
	}

	public String getShortLib() {
		return Messages.getString("ElecType." + this.libKey + ".short"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String getLongLib() {
		return Messages.getString("ElecType." + this.libKey + ".long"); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
