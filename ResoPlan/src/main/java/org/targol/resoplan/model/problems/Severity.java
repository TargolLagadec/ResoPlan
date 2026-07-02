package org.targol.resoplan.model.problems;

public enum Severity {
	INFO("info"), //$NON-NLS-1$
	WARNING("warning"), //$NON-NLS-1$
	ERROR("error"); //$NON-NLS-1$

	private final String key;

	Severity(final String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

}