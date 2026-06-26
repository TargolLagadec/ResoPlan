package org.targol.resoplan.model.catalog;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.LayerType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "HookType")
public class HookType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private int id;

	@Column(name = "hookKey", nullable = false)
	private String hookKey;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "layer")
	private LayerType layer;

	public HookType(final LayerType layer, final String hookKey) {
		this.hookKey = hookKey;
		this.layer = layer;
	}

	/**
	 * Do not use : FOR JPA ONLY
	 */
	public HookType() {
	}

	public String getLabel() {
		return Messages.getString(this.layer.getKey() + "." + this.hookKey + ".short"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String getDescription() {
		return Messages.getString(this.layer.getKey() + "." + this.hookKey + ".long"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getHookKey() {
		return this.hookKey;
	}

	public void setHookKey(final String hookKey) {
		this.hookKey = hookKey;
	}

	public LayerType getLayer() {
		return this.layer;
	}

	public void setLayer(final LayerType layer) {
		this.layer = layer;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false; // Pas le même type -> pas égal
		}
		final HookType hookType = (HookType) o;
		return this.id == hookType.id;
	}

	@Override
	public int hashCode() {
		// En JPA, la règle d'or pour le hashCode est d'utiliser une valeur constante
		// ou basée sur l'ID si l'entité est persistée, pour éviter que l'objet ne
		// change
		// de place dans un HashSet si son ID est généré après la sauvegarde.
		return getClass().hashCode();
	}
}
