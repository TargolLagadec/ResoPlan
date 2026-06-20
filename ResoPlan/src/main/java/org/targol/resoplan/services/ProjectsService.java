package org.targol.resoplan.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.repositories.ProjectsRepository;

@Service
@Transactional
public class ProjectsService {

	private final ProjectsRepository repo;
	private Project openedProject = null;

	public ProjectsService(final ProjectsRepository repo) {
		this.repo = repo;
	}

	public void setOpenedProject(final Project project) {
		if (project != null) {
			project.setLastOpened(LocalDateTime.now());
			updateProject(project);
		}
		this.openedProject = project;
	}

	public Project getOpenedProject() {
		return this.openedProject;
	}

	public Optional<Project> getProject(final int id) {
		return this.repo.findById(id);
	}

	public List<Project> getAllProjects() {
		return this.repo.findAll();
	}

	public List<Project> getLastTenProjects() {
		return this.repo.findTop10ByOrderByLastOpenedDesc();
	}

	public Project createProject(final String name, final int nbFloors) throws ServiceException {
		if (isProjectNameDuplicate(name)) {
			throw new ServiceException(Messages.getString("ProjectsService.CreateError.name.duplicate")); //$NON-NLS-1$
		}
		// On crée le projet et ses étages.
		final Project proj = new Project(name);
		for (int curFloorNum = 0; curFloorNum < nbFloors; curFloorNum++) {
			final Floor curFloor = new Floor(curFloorNum);
			proj.addFloor(curFloor);
		}
		proj.setLastOpened(LocalDateTime.now());
		return this.repo.save(proj);
	}

	public Project updateProject(final Project projectToUpdate) throws ServiceException {
		final int id = projectToUpdate.getId();
		if (id <= 0) {
			throw new ServiceException(Messages.getString("ProjectsService.UpdateError.NoId")); //$NON-NLS-1$
		}
		final Optional<Project> test = this.repo.findById(id);
		if (test.isEmpty()) {
			throw new ServiceException(Messages.getString("ProjectsService.UpdateError.DoesntExist")); //$NON-NLS-1$
		}
		return this.repo.save(projectToUpdate);
	}

	public void deleteProject(final int id) {
		this.repo.deleteById(id);
	}

	private boolean isProjectNameDuplicate(final String name) {
		final Optional<Project> resu = this.repo.findByName(name);
		return resu.isPresent();
	}

}