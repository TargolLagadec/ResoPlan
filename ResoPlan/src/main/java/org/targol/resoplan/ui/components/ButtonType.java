package org.targol.resoplan.ui.components;

public enum ButtonType {
	ADD("add"),
	EDIT("edit"),
	DELETE("delete"),
	BROWSE("browse");

	private final String key;

	ButtonType(final String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

}
