package org.targol.resoplan.services;

import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.NodeHook;
import org.targol.resoplan.model.enums.ElecType;
import org.targol.resoplan.model.enums.LayerType;
import org.targol.resoplan.model.enums.WaterAlimType;
import org.targol.resoplan.model.enums.WaterEvacType;
import org.targol.resoplan.repositories.NodeHooksRepository;

@Service
@Transactional
public class NodeHooksService {

	private final NodeHooksRepository repo;

	public NodeHooksService(final NodeHooksRepository repo) {
		this.repo = repo;
	}

	public NodeHook save(final NodeHook hook) throws ServiceException {
		switch (hook.getLayer()) {
		case LayerType.ELEC:
			if (hook.getElecType() == null) {
				throw new ServiceException(
						Messages.getString("NodeHooksService.inconsistent.other", LayerType.ELEC, ElecType.class)); //$NON-NLS-1$
			}
			break;
		case LayerType.EAU_ALIM:
			if (hook.getAlimType() == null) {
				throw new ServiceException(Messages.getString("NodeHooksService.inconsistent.other", LayerType.EAU_ALIM, //$NON-NLS-1$
						WaterAlimType.class));
			}
			break;
		case LayerType.EAU_EVAC:
			if (hook.getEvacType() == null) {
				throw new ServiceException(Messages.getString("NodeHooksService.inconsistent.other", LayerType.EAU_EVAC, //$NON-NLS-1$
						WaterEvacType.class));
			}
			break;
		case LayerType.NET:
			if (hook.getEvacType() != null || hook.getAlimType() != null || hook.getElecType() != null) {
				throw new ServiceException(Messages.getString("NodeHooksService.inconsistent.net", LayerType.NET)); //$NON-NLS-1$
			}
			break;
		}
		return this.repo.save(hook);
	}

	public List<NodeHook> getAll() {
		return this.repo.findAll();
	}

	public void deleteNodeHook(final int id) {
		this.repo.deleteById(id);
	}

}