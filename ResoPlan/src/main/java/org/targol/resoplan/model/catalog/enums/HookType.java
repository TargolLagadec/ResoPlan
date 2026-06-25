package org.targol.resoplan.model.catalog.enums;

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

	@Column(name = "libKey")
	private String libKey;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "layer")
	private LayerType layer;

	public HookType(final LayerType layer, final String libKey) {
		this.libKey = libKey;
		this.layer = layer;
	}

	/**
	 * Do not use : FOR JPA ONLY
	 */
	public HookType() {
	}

	public String getShortLib() {
		return Messages.getString(this.layer.getKey() + this.libKey + ".short"); //$NON-NLS-1$
	}

	public String getLongLib() {
		return Messages.getString(this.layer.getKey() + this.libKey + ".long"); //$NON-NLS-1$
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getLibKey() {
		return this.libKey;
	}

	public void setLibKey(final String libKey) {
		this.libKey = libKey;
	}

	public LayerType getLayer() {
		return this.layer;
	}

	public void setLayer(final LayerType layer) {
		this.layer = layer;
	}

}
