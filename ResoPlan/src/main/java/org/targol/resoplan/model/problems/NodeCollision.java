package org.targol.resoplan.model.problems;

import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.Node;

public record NodeCollision(Severity severity, Node other, LayerType layer) {

}
