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
@Table(name = "HOOK_TYPE")
public class HookType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "HOOK_KEY", nullable = false)
	private String hookKey;

	@Column(name = "DEFAULT_HEIGHT", nullable = false)
	private double defaultHeight;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "LAYER")
	private LayerType layer;

	public HookType(final LayerType layer, final String hookKey, final double defaultHeight) {
		this.hookKey = hookKey;
		this.layer = layer;
		this.defaultHeight = defaultHeight;
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

	public double getDefaultHeight() {
		return this.defaultHeight;
	}

	public void setDefaultHeight(final double defaultHeight) {
		this.defaultHeight = defaultHeight;
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
		if (this.id != 0) {
			return this.id == hookType.id;
		}
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		// En JPA, la règle d'or pour le hashCode est d'utiliser une valeur constante
		// ou basée sur l'ID si l'entité est persistée, pour éviter que l'objet ne
		// change
		// de place dans un HashSet si son ID est généré après la sauvegarde.
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return getLabel();
	}
}
