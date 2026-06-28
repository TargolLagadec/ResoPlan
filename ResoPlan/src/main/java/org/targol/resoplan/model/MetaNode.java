package org.targol.resoplan.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
@DiscriminatorValue("META")
public class MetaNode extends AbstractNode implements INodeContainer {

	@Enumerated(EnumType.STRING)
	private MetaNodeType metaType;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "META_NODE_ID", referencedColumnName = "ID")
	private List<Node> nodes;

	public MetaNode() {
		super();
		this.nodes = new ArrayList<>();
		this.posZ = -1;
		this.metaType = MetaNodeType.COLONNE;
	}

	// GETTERS AND SETTERS
	public MetaNodeType getMetaType() {
		return this.metaType;
	}

	public void setMetaType(final MetaNodeType metaType) {
		this.metaType = metaType;
	}

	public List<Node> getNodes() {
		return this.nodes;
	}

	public void setNodes(final List<Node> nodes) {
		this.nodes = nodes;
	}

	@Override
	public void addNode(final AbstractNode node) {
		this.nodes.add((Node) node);
	}

	@Override
	public void removeNode(final AbstractNode node) {
		this.nodes.remove(node);
	}
}
