package org.targol.resoplan.model;

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
	private double zoomFactor;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ProjectId", referencedColumnName = "id")
	private List<Room> rooms;

	public Floor() {
		this(0);
	}

	public Floor(final int number) {
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

	public List<Room> getRooms() {
		return this.rooms;
	}

	public void setRooms(final List<Room> rooms) {
		this.rooms = rooms;
	}

	public void addRoom(final Room r) {
		this.rooms.add(r);
	}

	public void removeRoom(final Room r) {
		this.rooms.remove(r);
	}

}
