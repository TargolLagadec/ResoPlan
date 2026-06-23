package org.targol.resoplan.model;

import org.targol.resoplan.model.enums.WaterAlimType;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@DiscriminatorValue("EAU_ALIM")
public class AlimLink extends AbstractLink {

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "alimType")
	private WaterAlimType alimType;

	public WaterAlimType getAlimType() {
		return this.alimType;
	}

	public void setAlimType(final WaterAlimType alimType) {
		this.alimType = alimType;
	}

}
