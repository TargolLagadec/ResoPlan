package org.targol.resoplan.model;

import org.targol.resoplan.model.enums.ElecType;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@DiscriminatorValue("ELEC")
public class ElecLink extends AbstractLink {

	@Enumerated(EnumType.STRING)
	@Column(length = 40, name = "elecType")
	private ElecType elecType;

	public ElecType getElecType() {
		return this.elecType;
	}

	public void setElecType(final ElecType elecType) {
		this.elecType = elecType;
	}
}
