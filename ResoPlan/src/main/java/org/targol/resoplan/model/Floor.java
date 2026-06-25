package org.targol.resoplan.model;

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
@Table(name = "Floor")
public class Floor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "number", nullable = false)
	private int number;

	@Column(name = "imgPath")
	private String imgPath;

	@Column(name = "shiftX")
	private double shiftX;

	@Column(name = "shiftY")
	private double shiftY;

	@Column(name = "zoomFactor")
	private double zoomFactor = 1.0d;

	@Column(name = "vitual")
	private boolean virtual;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "FloorId", referencedColumnName = "id")
	private List<Layer> layers;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "FloorId", referencedColumnName = "id")
	private final List<Node> nodes;

	public Floor() {
		this.layers = new ArrayList<>();
		this.nodes = new ArrayList<>();
	}

	public Floor(final int number) {
		this();
		this.number = number;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(final int number) {
		this.number = number;
	}

	public String getImgPath() {
		return this.imgPath;
	}

	public void setImgPath(final String imgPath) {
		this.imgPath = imgPath;
	}

	public double getShiftX() {
		return this.shiftX;
	}

	public void setShiftX(final double shiftX) {
		this.shiftX = shiftX;
	}

	public double getShiftY() {
		return this.shiftY;
	}

	public void setShiftY(final double shiftY) {
		this.shiftY = shiftY;
	}

	public double getZoomFactor() {
		return this.zoomFactor;
	}

	public void setZoomFactor(final double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	public boolean isVirtual() {
		return this.virtual;
	}

	public void setVirtual(final boolean virtual) {
		this.virtual = virtual;
	}

	public List<Layer> getLayers() {
		return this.layers;
	}

	public void setLayers(final List<Layer> layers) {
		this.layers = layers;
	}

	public void addLayer(final Layer r) {
		this.layers.add(r);
	}

	public void removeLayer(final Layer r) {
		this.layers.remove(r);
	}
}
