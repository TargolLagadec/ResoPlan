package org.targol.resoplan.model;

import org.targol.resoplan.model.catalog.HookType;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Link")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "layer", discriminatorType = DiscriminatorType.STRING, length = 10)
public class Link {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	protected int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hookTypeId")
	private HookType hookType;

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

	public HookType getHookType() {
		return this.hookType;
	}

	public void setHookType(final HookType hookType) {
		this.hookType = hookType;
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
