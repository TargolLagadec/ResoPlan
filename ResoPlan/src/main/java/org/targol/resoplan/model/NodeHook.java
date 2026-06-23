package org.targol.resoplan.model;

import org.targol.resoplan.model.enums.ElecType;
import org.targol.resoplan.model.enums.LayerType;
import org.targol.resoplan.model.enums.WaterAlimType;
import org.targol.resoplan.model.enums.WaterEvacType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "NodeHook")
public class NodeHook {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private int id;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "layer")
	private LayerType layer;

	@OneToOne
	@JoinColumn(name = "hookId")
	private Hook hook1;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "alimType")
	private WaterAlimType alimType;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "evacType")
	private WaterEvacType evacType;

	@Enumerated(EnumType.STRING)
	@Column(length = 40, name = "elecType")
	private ElecType elecType;

	/**
	 * Builds a net layer nodeHook
	 */
	public NodeHook() {
		this.layer = LayerType.NET;
	}

	/**
	 * Builds a water alimentation layer nodeHook of given type
	 *
	 * @param alimType water alimentation Type
	 */
	public NodeHook(final WaterAlimType alimType) {
		this.layer = LayerType.EAU_ALIM;
		this.alimType = alimType;
	}

	/**
	 * Builds a water evacuation layer nodeHook of given type
	 *
	 * @param evacType water evacuation type
	 */
	public NodeHook(final WaterEvacType evacType) {
		this.layer = LayerType.EAU_EVAC;
		this.evacType = evacType;
	}

	/**
	 * Builds an electrical layer nodeHook of given type
	 *
	 * @param elecType electrical type
	 */
	public NodeHook(final ElecType elecType) {
		this.layer = LayerType.ELEC;
		this.elecType = elecType;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public LayerType getLayer() {
		return this.layer;
	}

	public void setLayer(final LayerType layer) {
		this.layer = layer;
	}

	public WaterAlimType getAlimType() {
		return this.alimType;
	}

	public void setAlimType(final WaterAlimType alimType) {
		this.alimType = alimType;
	}

	public WaterEvacType getEvacType() {
		return this.evacType;
	}

	public void setEvacType(final WaterEvacType evacType) {
		this.evacType = evacType;
	}

	public ElecType getElecType() {
		return this.elecType;
	}

	public void setElecType(final ElecType elecType) {
		this.elecType = elecType;
	}
}
