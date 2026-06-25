package org.targol.resoplan.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "layerType")
	private LayerType layerType;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "LayerId", referencedColumnName = "id")
	private List<Link> links;

	public Layer() {
		this.links = new ArrayList<>();
	}

	public Layer(final LayerType type) {
		this();
		this.layerType = type;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public LayerType getLayerType() {
		return this.layerType;
	}

	public void setLayerType(final LayerType layerType) {
		this.layerType = layerType;
	}

	public List<Link> getLinks() {
		return this.links;
	}

	public void setLinks(final List<Link> links) {
		this.links = links;
	}

	public void addLink(final Link link) {
		this.links.add(link);
	}

	public void removeLink(final Link link) {
		this.links.remove(link);
	}
}
