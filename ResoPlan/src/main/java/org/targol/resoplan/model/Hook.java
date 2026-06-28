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

	/**
	 * DO NOT USE, FOR JPA ONLY
	 */
	public Hook() {
	}

	public Hook(final HookType hookType, final double posX, final double posY) {
		this.hookType = hookType;
		this.posX = posX;
		this.posY = posY;
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
}
