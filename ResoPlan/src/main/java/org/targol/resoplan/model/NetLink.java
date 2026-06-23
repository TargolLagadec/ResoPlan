package org.targol.resoplan.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("NET")
public class NetLink extends AbstractLink {

}
