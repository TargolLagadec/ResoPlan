package org.targol.resoplan.repositories;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.NodeHook;
import org.targol.resoplan.model.NodeModel;
import org.targol.resoplan.model.enums.ElecType;
import org.targol.resoplan.model.enums.WaterAlimType;
import org.targol.resoplan.model.enums.WaterEvacType;
import org.targol.resoplan.services.NodeHooksService;
import org.targol.resoplan.services.NodeModelsService;

@Component
public class DatabaseInitializer implements CommandLineRunner {
	private final NodeHooksService hookSvc;
	private final NodeModelsService modelSvc;

	public DatabaseInitializer(final NodeHooksService hookSvc, final NodeModelsService modelSvc) {
		this.hookSvc = hookSvc;
		this.modelSvc = modelSvc;
	}

	@Override
	public void run(final String... args) throws Exception {
		// Si les nodeHooks sont là, c'est que l'init est Ok (ils sont immuables)
		if (!this.hookSvc.getAll().isEmpty()) {
			return;
		}
		// Si les nodeHooks ne sont pas là, on les crée puis le "catalogue"
		// d'équipements de base
		final NodeHook nh_net = this.hookSvc.save(new NodeHook());
		final NodeHook nh_alim12 = this.hookSvc.save(new NodeHook(WaterAlimType.PER_12));
		final NodeHook nh_alim16 = this.hookSvc.save(new NodeHook(WaterAlimType.PER_16));
		final NodeHook nh_alim20 = this.hookSvc.save(new NodeHook(WaterAlimType.PER_20));
		final NodeHook nh_evac32 = this.hookSvc.save(new NodeHook(WaterEvacType.PVC_32));
		final NodeHook nh_evac40 = this.hookSvc.save(new NodeHook(WaterEvacType.PVC_40));
		final NodeHook nh_evac100 = this.hookSvc.save(new NodeHook(WaterEvacType.PVC_100));
		final NodeHook nh_elecPrise = this.hookSvc.save(new NodeHook(ElecType.PRISE_3X25));
		final NodeHook nh_elecLightCommon = this.hookSvc
				.save(new NodeHook(ElecType.ECLAIRAGE_COMMUN_NEUTRE_ET_TERRE_2X15));
		final NodeHook nh_elecLightCommand = this.hookSvc.save(new NodeHook(ElecType.ECLAIRAGE_COMMANDE_PHASE_1X15));
		final NodeHook nh_elecTelerupt = this.hookSvc.save(new NodeHook(ElecType.PILOTE_TELERUPTEUR_COULEUR_2X15));
		final NodeHook nh_elecVaEtVient = this.hookSvc.save(new NodeHook(ElecType.ENTRE_VAETVIENT_COULEUR_2X15));

		final NodeModel priseRj45 = new NodeModel(Messages.getString("Catalog.rj45.name")); //$NON-NLS-1$
		priseRj45.setDescription(Messages.getString("Catalog.rj45.desc")); //$NON-NLS-1$
		priseRj45.addAllowedHook(nh_net);
		this.modelSvc.save(priseRj45);
		final NodeModel grosDebit = new NodeModel(Messages.getString("Catalog.grosDebit.name")); //$NON-NLS-1$
		grosDebit.setDescription(Messages.getString("Catalog.grosDebit.desc")); //$NON-NLS-1$
		grosDebit.addAllowedHook(nh_evac40);
		grosDebit.addAllowedHook(nh_alim16);
		this.modelSvc.save(grosDebit);
		final NodeModel petitDebit = new NodeModel(Messages.getString("Catalog.petitDebit.name")); //$NON-NLS-1$
		petitDebit.setDescription(Messages.getString("Catalog.petitDebit.desc")); //$NON-NLS-1$
		petitDebit.addAllowedHook(nh_evac32);
		petitDebit.addAllowedHook(nh_alim12);
		this.modelSvc.save(petitDebit);
		final NodeModel chauffeEau = new NodeModel(Messages.getString("Catalog.chauffeEau.name")); //$NON-NLS-1$
		chauffeEau.setDescription(Messages.getString("Catalog.chauffeEau.desc")); //$NON-NLS-1$
		chauffeEau.addAllowedHook(nh_evac32);
		chauffeEau.addAllowedHook(nh_alim20);
		chauffeEau.addAllowedHook(nh_elecPrise);
		this.modelSvc.save(chauffeEau);
		final NodeModel wc = new NodeModel(Messages.getString("Catalog.wc.name")); //$NON-NLS-1$
		wc.setDescription(Messages.getString("Catalog.wc.desc")); //$NON-NLS-1$
		wc.addAllowedHook(nh_evac100);
		wc.addAllowedHook(nh_alim12);
		this.modelSvc.save(wc);
		final NodeModel spot = new NodeModel(Messages.getString("Catalog.spot.name")); //$NON-NLS-1$
		spot.setDescription(Messages.getString("Catalog.spot.desc")); //$NON-NLS-1$
		spot.addAllowedHook(nh_elecLightCommon);
		spot.addAllowedHook(nh_elecLightCommand);
		this.modelSvc.save(spot);
		final NodeModel inter = new NodeModel(Messages.getString("Catalog.inter.name")); //$NON-NLS-1$
		inter.setDescription(Messages.getString("Catalog.inter.desc")); //$NON-NLS-1$
		inter.addAllowedHook(nh_elecLightCommand);
		inter.addAllowedHook(nh_elecLightCommand);
		this.modelSvc.save(inter);
		final NodeModel vv = new NodeModel(Messages.getString("Catalog.vv.name")); //$NON-NLS-1$
		vv.setDescription(Messages.getString("Catalog.vv.desc")); //$NON-NLS-1$
		vv.addAllowedHook(nh_elecLightCommand);
		vv.addAllowedHook(nh_elecVaEtVient);
		this.modelSvc.save(vv);
		final NodeModel poussoir = new NodeModel(Messages.getString("Catalog.poussoir.name")); //$NON-NLS-1$
		poussoir.setDescription(Messages.getString("Catalog.poussoir.desc")); //$NON-NLS-1$
		poussoir.addAllowedHook(nh_elecTelerupt);
		this.modelSvc.save(poussoir);
		final NodeModel telerupt = new NodeModel(Messages.getString("Catalog.telerupt.name")); //$NON-NLS-1$
		telerupt.setDescription(Messages.getString("Catalog.telerupt.desc")); //$NON-NLS-1$
		telerupt.addAllowedHook(nh_elecLightCommand);
		telerupt.addAllowedHook(nh_elecTelerupt);
		this.modelSvc.save(telerupt);
	}
}