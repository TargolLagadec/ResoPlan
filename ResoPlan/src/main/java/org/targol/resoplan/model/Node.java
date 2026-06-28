package org.targol.resoplan.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.targol.resoplan.model.catalog.HookType;
import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.model.catalog.enums.NodeCross;

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
import jakarta.persistence.OneToOne;
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
	@CollectionTable(name = "Node_Layers", joinColumns = @JoinColumn(name = "nodeId"))
	@Enumerated(EnumType.STRING)
	@Column(name = "layerType")
	private final Set<LayerType> activeLayers = new HashSet<>();

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "nodeCross")
	private NodeCross nodeCross = NodeCross.NONE;

	/**
	 * Dans le cas ou le Node est traversant un plancher ou un plafond, node associé
	 * à l'autre étage.
	 */
	@OneToOne
	@JoinColumn(name = "LinkedNodeId")
	private Node linkedNode;

	@Column(name = "posX", nullable = false)
	private double posX;

	@Column(name = "posY", nullable = false)
	private double posY;

	/**
	 * DO NOT USE, FOR JPA ONLY
	 */
	public Node() {
		this.hooks = new ArrayList<>();
	}

	public Node(final NodeModel model) {
		this();
		this.model = model;
		buildUponModel();
	}

	private void buildUponModel() {
		if (this.model == null) {
			return;
		}
		for (final HookType modelHook : this.model.getAllowedHooks()) {
			final Hook hook = new Hook(modelHook, this.posX, this.posY);
			this.activeLayers.add(modelHook.getLayer());
			addHook(hook);
		}
	}

	// GETTERS AND SETTERS
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
		buildUponModel();
	}

	public List<Hook> getHooks() {
		return this.hooks;
	}

	public void setHooks(final List<Hook> hooks) {
		this.hooks = hooks;
	}

	public Set<LayerType> getActiveLayers() {
		return this.activeLayers;
	}

	public void addHook(final Hook hook) {
		this.hooks.add(hook);
	}

	public void removeHook(final Hook hook) {
		this.hooks.remove(hook);
	}

	public NodeCross getNodeCross() {
		return this.nodeCross;
	}

	public void setNodeCross(final NodeCross nodeCross) {
		this.nodeCross = nodeCross;
	}

	public Node getLinkedNode() {
		return this.linkedNode;
	}

	public void setLinkedNode(final Node linkedNode) {
		this.linkedNode = linkedNode;
	}

	public double getPosX() {
		return this.posX;
	}

	public void setPosX(final double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return this.posY;
	}

	public void setPosY(final double posY) {
		this.posY = posY;
	}
}
