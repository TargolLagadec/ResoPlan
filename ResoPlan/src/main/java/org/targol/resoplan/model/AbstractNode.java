package org.targol.resoplan.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "NODE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "NODE_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractNode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	protected int id;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "NODE_LAYERS", joinColumns = @JoinColumn(name = "NODE_ID"))
	@Enumerated(EnumType.STRING)
	@Column(name = "LAYER_TYPE")
	protected Set<LayerType> activeLayers = new HashSet<>();

	@Column(name = "POS_X", nullable = false)
	protected double posX;

	@Column(name = "POS_Y", nullable = false)
	protected double posY;

	@Column(name = "POS_Z", nullable = false)
	protected double posZ;

	// GETTERS AND SETTERS
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setActiveLayers(Set<LayerType> activeLayers) {
		this.activeLayers = activeLayers;
	}

	public Set<LayerType> getActiveLayers() {
		return this.activeLayers;
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
}
