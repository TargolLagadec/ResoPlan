package org.targol.resoplan.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.targol.resoplan.model.catalog.NodeModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Node")
public class Node {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "modelId")
	private NodeModel model;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "NodeId", referencedColumnName = "id")
	private List<Hook> hooks;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "Node_Layers", joinColumns = @JoinColumn(name = "node_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "layer_type")
	private final Set<LayerType> activeLayers = new HashSet<>();

	@Column(name = "posX", nullable = false)
	private int posX;

	@Column(name = "posY", nullable = false)
	private int posY;

	public Node() {
		this.hooks = new ArrayList<>();
	}

	public Node(final NodeModel model) {
		this();
		this.model = model;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public NodeModel getModel() {
		return this.model;
	}

	public void setModel(final NodeModel model) {
		this.model = model;
	}

	public List<Hook> getHooks() {
		return this.hooks;
	}

	public void setHooks(final List<Hook> hooks) {
		this.hooks = hooks;
	}

	public int getPosX() {
		return this.posX;
	}

	public void setPosX(final int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return this.posY;
	}

	public void setPosY(final int posY) {
		this.posY = posY;
	}
}
