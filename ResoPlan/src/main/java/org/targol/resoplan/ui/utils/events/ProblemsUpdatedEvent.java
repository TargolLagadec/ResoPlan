package org.targol.resoplan.ui.utils.events;

import org.targol.resoplan.model.Project;

import javafx.event.EventType;

public class ProblemsUpdatedEvent extends GenericActionEvent {

	private static final long serialVersionUID = -237107763542098637L;
	public static final EventType<ProblemsUpdatedEvent> CHECK_PROBLEMS = new EventType<>(GenericActionEvent.ANY,
			"CHECK_PROBLEMS"); //$NON-NLS-1$
	public static final EventType<ProblemsUpdatedEvent> DISPLAY_PROBLEMS = new EventType<>(GenericActionEvent.ANY,
			"DISPLAY_PROBLEMS"); //$NON-NLS-1$

	public static ProblemsUpdatedEvent fireCheck(final Project project) {
		return new ProblemsUpdatedEvent(CHECK_PROBLEMS, project);
	}

	public static ProblemsUpdatedEvent fireRedraw() {
		return new ProblemsUpdatedEvent(DISPLAY_PROBLEMS, null);
	}

	private final Project project;

	private ProblemsUpdatedEvent(final EventType<ProblemsUpdatedEvent> event, final Project project) {
		super(event);
		this.project = project;
	}

	public Project getProject() {
		return this.project;
	}

}