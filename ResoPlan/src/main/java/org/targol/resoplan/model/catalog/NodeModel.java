package org.targol.resoplan.model.catalog;

import java.util.ArrayList;
import java.util.List;

import org.targol.resoplan.model.catalog.enums.AlimConstraint;
import org.targol.resoplan.model.catalog.enums.HookType;
import org.targol.resoplan.model.catalog.enums.NodeCategory;
import org.targol.resoplan.model.catalog.enums.NodeCross;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "NodeModel")
public class NodeModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(length = 20, name = "category")
	private NodeCategory category;

	@Enumerated(EnumType.STRING)
	@Column(length = 5, name = "alimConstraint")
	private AlimConstraint alimConstraint;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "nodeCross")
	private NodeCross nodeCross;

	@Column(name = "imgName")
	private String imgName;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ModelAllowedHooks", joinColumns = @JoinColumn(name = "ModelId"), inverseJoinColumns = @JoinColumn(name = "NodeHookId"))
	private List<HookType> allowedHooks;

	public NodeModel() {
		this.allowedHooks = new ArrayList<>();
	}

	public NodeModel(final String name) {
		this();
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public NodeCategory getCategory() {
		return this.category;
	}

	public void setCategory(final NodeCategory category) {
		this.category = category;
	}

	public AlimConstraint getAlimConstraint() {
		return this.alimConstraint;
	}

	public void setAlimConstraint(final AlimConstraint alimConstraint) {
		this.alimConstraint = alimConstraint;
	}

	public NodeCross getNodeCross() {
		return this.nodeCross;
	}

	public void setNodeCross(final NodeCross nodeCross) {
		this.nodeCross = nodeCross;
	}

	public String getImgName() {
		return this.imgName;
	}

	public void setImgName(final String imgName) {
		this.imgName = imgName;
	}

	public List<HookType> getAllowedHooks() {
		return this.allowedHooks;
	}

	public void setAllowedHooks(final List<HookType> allowedHooks) {
		this.allowedHooks = allowedHooks;
	}

	public void addAllowedHook(final HookType allowedHook) {
		this.allowedHooks.add(allowedHook);
	}

	public void removeAllowedHook(final HookType allowedHook) {
		this.allowedHooks.remove(allowedHook);
	}
}
