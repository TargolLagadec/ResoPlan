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
@Table(name = "Layer")
public class Layer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name", nullable = false)
	private String name;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "LayerId", referencedColumnName = "id")
	private List<AbstractLink> links;

	public Layer() {
		this("FakeProject");
	}

	public Layer(final String name) {
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

	public List<AbstractLink> getLinks() {
		return this.links;
	}

	public void setLinks(final List<AbstractLink> links) {
		this.links = links;
	}

	public void addLink(final AbstractLink link) {
		this.links.add(link);
	}

	public void removeLink(final AbstractLink link) {
		this.links.remove(link);
	}
}
