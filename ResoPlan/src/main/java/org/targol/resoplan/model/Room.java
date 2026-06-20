package org.targol.resoplan.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Room")
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name", nullable = false)
	private String name;

//	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinColumn(name = "ProjectId", referencedColumnName = "id")
//	private List<UmlEnumeration> enums;
//
//	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinColumn(name = "ProjectId", referencedColumnName = "id")
//	private List<UmlClass> classes;
//
//	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinColumn(name = "ProjectId", referencedColumnName = "id")
//	private List<UmlAbstractClass> abstracts;
//
//	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinColumn(name = "ProjectId", referencedColumnName = "id")
//	private List<UmlInterface> interfaces;

	public Room() {
		this("FakeProject");
	}

	public Room(final String name) {
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

}
