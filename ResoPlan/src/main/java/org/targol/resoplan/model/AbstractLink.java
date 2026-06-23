package org.targol.resoplan.model;

import org.targol.resoplan.model.enums.LinkDepth;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Link")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "layer", discriminatorType = DiscriminatorType.STRING, length = 10)
public abstract class AbstractLink {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	protected int id;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "depth")
	protected LinkDepth depth;

	@OneToOne
	@JoinColumn(name = "hook1Id")
	protected Hook hook1;

	@OneToOne
	@JoinColumn(name = "hook2Id")
	protected Hook hook2;

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public LinkDepth getDepth() {
		return this.depth;
	}

	public void setDepth(final LinkDepth depth) {
		this.depth = depth;
	}

	public Hook getHook1() {
		return this.hook1;
	}

	public void setHook1(final Hook hook1) {
		this.hook1 = hook1;
	}

	public Hook getHook2() {
		return this.hook2;
	}

	public void setHook2(final Hook hook2) {
		this.hook2 = hook2;
	}
}
