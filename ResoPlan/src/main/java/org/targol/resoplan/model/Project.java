package org.targol.resoplan.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Project")
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "lastOpened")
	private LocalDateTime lastOpened;

	/**
	 * Échelle des plans en pixels par metre
	 */
	@Column(name = "plansScale")
	private int plansScale;

	/**
	 * Marge en % lors du calcul des débits. Ce pourcentage est ajouté aux quantités
	 * calculées.
	 */
	@Column(name = "consumptionMargin")
	private int consumptionMargin;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ProjectId", referencedColumnName = "id")
	private List<Floor> floors;

	public Project() {
		this.floors = new ArrayList<>();
	}

	public Project(final String name) {
		this();
		this.name = name;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public LocalDateTime getLastOpened() {
		return this.lastOpened;
	}

	public void setLastOpened(final LocalDateTime lastOpened) {
		this.lastOpened = lastOpened;
	}

	public int getPlansScale() {
		return this.plansScale;
	}

	public void setPlansScale(final int plansScale) {
		this.plansScale = plansScale;
	}

	public int getConsumptionMargin() {
		return this.consumptionMargin;
	}

	public void setConsumptionMargin(final int consumptionMargin) {
		this.consumptionMargin = consumptionMargin;
	}

	public List<Floor> getFloors() {
		return this.floors;
	}

	public void setFloors(final List<Floor> floors) {
		this.floors = floors;
	}

	public void addFloor(final Floor f) {
		this.floors.add(f);
	}

	public void removeFloor(final Floor f) {
		this.floors.remove(f);
	}

	@Override
	public String toString() {
		return "Projet [" + this.name + "] Nb. d'étages : " + this.floors.size(); //$NON-NLS-1$//$NON-NLS-2$
	}
}
