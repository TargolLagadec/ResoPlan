package org.targol.resoplan.model.problems;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.print.attribute.standard.Severity;

import org.springframework.stereotype.Component;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.ui.utils.events.ProblemsUpdatedEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;

import javafx.application.Platform;

@Component
public class ProblemsRegistry {

	private final ValidationService validationService;
	private final List<Problem> currentProblems = new CopyOnWriteArrayList<>();

	public ProblemsRegistry(final ValidationService validationService) {
		this.validationService = validationService;
		UiEventBus.register(null, ProblemsUpdatedEvent.CHECK_PROBLEMS, event -> {
			validateAndRefresh(event.getProject());
		});
	}

	public void validateAndRefresh(final Project project) {
		final List<Problem> freshProblems = this.validationService.validateProject(project);
		updateProblems(freshProblems);
	}

	public List<Problem> getProblems() {
		return Collections.unmodifiableList(this.currentProblems);
	}

	public void updateProblems(final Collection<Problem> newProblems) {
		this.currentProblems.clear();
		this.currentProblems.addAll(newProblems);
		Platform.runLater(() -> UiEventBus.send(ProblemsUpdatedEvent.fireRedraw()));
	}

	public boolean hasErrors() {
		return this.currentProblems.stream().anyMatch(p -> Severity.ERROR.equals(p.getSeverity()));
	}
}