package org.targol.resoplan.model.problems;

import org.targol.resoplan.model.LayerType;

public class Problem {
	public enum Severity {
		INFO,
		WARNING,
		ERROR
	}

	private final Severity severity;
	private final String message;

	private final Integer projectId;
	private final Integer floorId; // null si lié uniquement au projet
	private final LayerType layerType; // null si lié au projet/étage global
	private final Integer nodeId; // null si lié au layer/étage global

	public static Problem noProjectError() {
		return new Problem(Severity.INFO, "Aucun projet ouvert", null, null, null, null);
	}

	public static Problem projectError(final Integer projectId, final String message) {
		return new Problem(Severity.ERROR, message, projectId, null, null, null);
	}

	public static Problem floorError(final Integer projectId, final Integer floorId, final String message) {
		return new Problem(Severity.ERROR, message, projectId, floorId, null, null);
	}

	public Problem(final Severity severity, final String message, final Integer projectId, final Integer floorId,
			final LayerType layerType, final Integer nodeId) {
		this.severity = severity;
		this.message = message;
		this.projectId = projectId;
		this.floorId = floorId;
		this.layerType = layerType;
		this.nodeId = nodeId;
	}

	public Severity getSeverity() {
		return this.severity;
	}

	public String getMessage() {
		return this.message;
	}

	public int getProjectId() {
		return this.projectId;
	}

	public Integer getFloorId() {
		return this.floorId;
	}

	public LayerType getLayerType() {
		return this.layerType;
	}

	public Integer getNodeId() {
		return this.nodeId;
	}
}