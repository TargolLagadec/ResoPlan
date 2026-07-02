package org.targol.resoplan.model;

import java.util.LinkedHashSet;
import java.util.Set;

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
@Table(name = "FLOOR")
public class Floor implements INodeContainer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "NUMBER", nullable = false)
	private int number;

	@Column(name = "IMAGE_PATH")
	private String imgPath;

	@Column(name = "IAMGE_WIDTH")
	private double imgWidth;

	@Column(name = "IMAGE_HEIGHT")
	private double imgHeight;

	@Column(name = "SHIFT_X")
	private double shiftX;

	@Column(name = "SHIFT_Y")
	private double shiftY;

	@Column(name = "ZOOM_FACTOR")
	private double zoomFactor = 1.0d;

	@Column(name = "VIRTUAL")
	private boolean virtual;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "FLOOR_ID", referencedColumnName = "ID")
	private Set<Layer> layers;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "FLOOR_ID", referencedColumnName = "ID")
	private final Set<AbstractNode> nodes;

	public Floor() {
		this.layers = new LinkedHashSet<>();
		this.nodes = new LinkedHashSet<>();
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

	public double getImgWidth() {
		return this.imgWidth;
	}

	public void setImgWidth(final double imgWidth) {
		this.imgWidth = imgWidth;
	}

	public double getImgHeight() {
		return this.imgHeight;
	}

	public void setImgHeight(final double imgHeight) {
		this.imgHeight = imgHeight;
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

	public Set<Layer> getLayers() {
		return this.layers;
	}

	public void setLayers(final Set<Layer> layers) {
		this.layers = layers;
	}

	public void addLayer(final Layer r) {
		this.layers.add(r);
	}

	public void removeLayer(final Layer r) {
		this.layers.remove(r);
	}

	public Set<AbstractNode> getNodes() {
		return this.nodes;
	}

	@Override
	public void addNode(final AbstractNode node) {
		this.nodes.add(node);
	}

	@Override
	public void removeNode(final AbstractNode node) {
		this.nodes.remove(node);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false; // Pas le même type -> pas égal
		}
		final Floor floor = (Floor) o;
		if (this.id != 0) {
			return this.id == floor.id;
		}
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
