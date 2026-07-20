package org.targol.resoplan.ui.utils.events;

import org.targol.resoplan.model.Project;

import javafx.event.EventType;

public class ProjectUpdatedEvent extends GenericActionEvent {

	private static final long serialVersionUID = -237107763542098637L;
	public static final EventType<ProjectUpdatedEvent> PROJECT_UPDATE = new EventType<>(GenericActionEvent.ANY,
			"PROJECT_UPDATE"); //$NON-NLS-1$
	public static final EventType<ProjectUpdatedEvent> PROJECT_CREATE = new EventType<>(GenericActionEvent.ANY,
			"PROJECT_CREATE"); //$NON-NLS-1$

	public static ProjectUpdatedEvent fireUpdate(final Project project) {
		return new ProjectUpdatedEvent(PROJECT_UPDATE, project);
	}

	public static ProjectUpdatedEvent fireCreate() {
		return new ProjectUpdatedEvent(PROJECT_CREATE, null);
	}

	private final Project project;

	private ProjectUpdatedEvent(final EventType<ProjectUpdatedEvent> event, final Project project) {
		super(event);
		this.project = project;
	}

	public Project getProject() {
		return this.project;
	}

}