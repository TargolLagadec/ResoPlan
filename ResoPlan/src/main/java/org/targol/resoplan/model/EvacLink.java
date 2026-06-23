package org.targol.resoplan.model;

import org.targol.resoplan.model.enums.WaterEvacType;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@DiscriminatorValue("EAU_EVAC")
public class EvacLink extends AbstractLink {

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "evacType")
	private WaterEvacType evacType;

	public WaterEvacType getEvacType() {
		return this.evacType;
	}

	public void setEvacType(final WaterEvacType evacType) {
		this.evacType = evacType;
	}
}
