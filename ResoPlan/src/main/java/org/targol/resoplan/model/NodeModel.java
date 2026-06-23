package org.targol.resoplan.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ModelAllowedHooks", joinColumns = @JoinColumn(name = "ModelId"), inverseJoinColumns = @JoinColumn(name = "NodeHookId"))
	private List<NodeHook> allowedHooks;

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

	public List<NodeHook> getAllowedHooks() {
		return this.allowedHooks;
	}

	public void setAllowedHooks(final List<NodeHook> allowedHooks) {
		this.allowedHooks = allowedHooks;
	}

	public void addAllowedHook(final NodeHook allowedHook) {
		this.allowedHooks.add(allowedHook);
	}

	public void removeAllowedHook(final NodeHook allowedHook) {
		this.allowedHooks.remove(allowedHook);
	}
}
