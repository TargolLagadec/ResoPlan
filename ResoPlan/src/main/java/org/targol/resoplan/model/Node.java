package org.targol.resoplan.model;

import java.util.ArrayList;
import java.util.List;

import org.targol.resoplan.model.catalog.HookType;
import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.model.catalog.enums.NodeCross;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
@DiscriminatorValue("SINGLE")
public class Node extends AbstractNode {

	@ManyToOne
	@JoinColumn(name = "MODEL_ID")
	private NodeModel model;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "NODE_ID", referencedColumnName = "ID")
	private List<Hook> hooks;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "NODE_CROSS")
	private NodeCross nodeCross = NodeCross.NONE;

	/**
	 * Dans le cas ou le Node est traversant un plancher ou un plafond, node associé
	 * à l'autre étage.
	 */
	@OneToOne
	@JoinColumn(name = "LINKED_NODE_ID")
	private Node linkedNode;

	/**
	 * DO NOT USE, FOR JPA ONLY
	 */
	public Node() {
		super();
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
}
