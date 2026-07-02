package org.targol.resoplan.model;

import org.targol.resoplan.model.catalog.HookType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "HOOK")
public class Hook {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HOOK_TYPE_ID")
	private HookType hookType;

	@Column(name = "POS_X", nullable = false)
	private double posX;

	@Column(name = "POS_Y", nullable = false)
	private double posY;

	@Column(name = "POS_Z", nullable = false)
	private double posZ;

	@Column(name = "LINKED", nullable = false)
	private final boolean linked = false;

	/**
	 * DO NOT USE, FOR JPA ONLY
	 */
	public Hook() {
	}

	public Hook(final HookType hookType, final double posX, final double posY, final double posZ) {
		this.hookType = hookType;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public HookType getHookType() {
		return this.hookType;
	}

	public void setHookType(final HookType hookType) {
		this.hookType = hookType;
	}

	public double getPosX() {
		return this.posX;
	}

	public void setPosX(final double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return this.posY;
	}

	public void setPosY(final double posY) {
		this.posY = posY;
	}

	public double getPosZ() {
		return this.posZ;
	}

	public void setPosZ(final double posZ) {
		this.posZ = posZ;
	}

	public boolean isLinked() {
		return this.linked;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false; // Pas le même type -> pas égal
		}
		final Hook hook = (Hook) o;
		if (this.id != 0) {
			return this.id == hook.id;
		}
		return super.equals(o);
	}
}
