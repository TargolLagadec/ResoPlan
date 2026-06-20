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
	private int shiftX;

	@Column(name = "shiftY")
	private int shiftY;

	@Column(name = "zoomFactor")
	private float zoomFactor;

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

	public int getShiftX() {
		return this.shiftX;
	}

	public void setShiftX(final int shiftX) {
		this.shiftX = shiftX;
	}

	public int getShiftY() {
		return this.shiftY;
	}

	public void setShiftY(final int shiftY) {
		this.shiftY = shiftY;
	}

	public float getZoomFactor() {
		return this.zoomFactor;
	}

	public void setZoomFactor(final float zoomFactor) {
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
