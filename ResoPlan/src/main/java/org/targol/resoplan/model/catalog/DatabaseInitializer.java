package org.targol.resoplan.model.catalog;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.catalog.enums.AlimConstraint;
import org.targol.resoplan.model.catalog.enums.NodeCategory;
import org.targol.resoplan.services.HookTypesService;
import org.targol.resoplan.services.NodeModelsService;

import com.opencsv.CSVReader;

@Component
public class DatabaseInitializer implements CommandLineRunner {

	private final static Map<String, LayerType> allHooks;
	static {
		allHooks = new HashMap<>(13);
		allHooks.put("per12", LayerType.WATER_ALIM); //$NON-NLS-1$
		allHooks.put("per16", LayerType.WATER_ALIM); //$NON-NLS-1$
		allHooks.put("per20", LayerType.WATER_ALIM); //$NON-NLS-1$
		allHooks.put("vis2027", LayerType.WATER_ALIM); //$NON-NLS-1$
		allHooks.put("vis1521", LayerType.WATER_ALIM); //$NON-NLS-1$
		allHooks.put("pvc32", LayerType.WATER_EVAC); //$NON-NLS-1$
		allHooks.put("pvc40", LayerType.WATER_EVAC); //$NON-NLS-1$
		allHooks.put("pvc100", LayerType.WATER_EVAC); //$NON-NLS-1$
		allHooks.put("rj45", LayerType.NET); //$NON-NLS-1$
		allHooks.put("prise", LayerType.ELEC); //$NON-NLS-1$
		allHooks.put("eclcommun", LayerType.ELEC); //$NON-NLS-1$
		allHooks.put("eclcommand", LayerType.ELEC); //$NON-NLS-1$
		allHooks.put("telrupt", LayerType.ELEC); //$NON-NLS-1$
		allHooks.put("vaetvien", LayerType.ELEC); //$NON-NLS-1$
		allHooks.put("metaElec", LayerType.ELEC); //$NON-NLS-1$
	}

	private final static Map<String, Double> hooksHeight;
	static {
		hooksHeight = new HashMap<>(13);
		hooksHeight.put("per12", 25.0d); //$NON-NLS-1$
		hooksHeight.put("per16", 25.0d); //$NON-NLS-1$
		hooksHeight.put("per20", 25.0d); //$NON-NLS-1$
		hooksHeight.put("vis2027", 25.0d); //$NON-NLS-1$
		hooksHeight.put("vis1521", 25.0d); //$NON-NLS-1$
		hooksHeight.put("pvc32", 25.0d); //$NON-NLS-1$
		hooksHeight.put("pvc40", 25.0d); //$NON-NLS-1$
		hooksHeight.put("pvc100", 25.0d); //$NON-NLS-1$
		hooksHeight.put("rj45", 30.0d); //$NON-NLS-1$
		hooksHeight.put("prise", 30.0d); //$NON-NLS-1$
		hooksHeight.put("eclcommun", 0.0d); //$NON-NLS-1$
		hooksHeight.put("eclcommand", 0.0d); //$NON-NLS-1$
		hooksHeight.put("telrupt", 90.0d); //$NON-NLS-1$
		hooksHeight.put("vaetvien", 90.0d); //$NON-NLS-1$
		hooksHeight.put("metaElec", 150.0d); //$NON-NLS-1$
	}

	private final ResourceLoader resourceLoader;
	private final HookTypesService typeSvc;
	private final NodeModelsService modelSvc;
	private final Map<String, HookType> hooksByNames;
	private final List<String> readErrors;

	public DatabaseInitializer(final ResourceLoader resourceLoader, final HookTypesService typeSvc,
			final NodeModelsService modelSvc) {
		this.resourceLoader = resourceLoader;
		this.typeSvc = typeSvc;
		this.modelSvc = modelSvc;
		this.hooksByNames = new HashMap<>();
		this.readErrors = new ArrayList<>();
	}

	@Override
	public void run(final String... args) throws Exception {
		// Si les HookTypes sont là, c'est que l'init est Ok (ils sont immuables)
		if (!this.typeSvc.getAll().isEmpty()) {
			return;
		}
		// Si les HookTypes ne sont pas là, on les crée
		createAllHooksAndFillMap();
		// puis on remplit le catalog à partir du csv fourni dans les ressources
		readCatalog();
		if (this.readErrors.size() > 0) {
			System.err.println("Erreur(s) à la lecture du fichier catalogue :");
			for (final String error : this.readErrors) {
				System.err.println("\t".concat(error));
			}
		}
	}

	private void createAllHooksAndFillMap() {
		for (final String key : allHooks.keySet()) {
			HookType ht = new HookType(allHooks.get(key), key, hooksHeight.get(key));
			ht = this.typeSvc.save(ht);
			this.hooksByNames.put(key, ht);
		}
	}

	private void readCatalog() {
		final Resource resource = this.resourceLoader.getResource("classpath:init/catalog.csv"); //$NON-NLS-1$
		if (!resource.exists()) {
			this.readErrors.add("Le fichier \"init/catalog.csv\" n'existe pas dans les ressources du projet."); //$NON-NLS-1$
			return;
		}
		try {
			final Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			final CSVReader csvReader = new CSVReader(reader);
			// On saute la première ligne (entêtes)
			String[] nextLine = csvReader.readNext();
			int numLine = 2;
			while ((nextLine = csvReader.readNext()) != null) {
				final String name = nextLine[0];
				final String description = nextLine[1];
				final String hooksString = nextLine[2];
				final String category = nextLine[3];
				final String constr = nextLine[4];
				final String nodeCross = nextLine[5];
				final String imgName = nextLine[6];
				// CHECK HookType
				final List<HookType> hooks = convertCsvHookKeysToHookTypes(hooksString, numLine);
				if (hooks.isEmpty()) {
					this.readErrors.add("Ligne " + numLine + " => les hooks définis dans la ligne \"" + nextLine //$NON-NLS-1$ //$NON-NLS-2$
							+ "\" ne sont pas corrects."); //$NON-NLS-1$
					continue;
				}
				// CHECK NodeCategory
				NodeCategory cat;
				try {
					cat = NodeCategory.valueOf(category);
				} catch (final Exception e) {
					this.readErrors.add("Ligne " + numLine + " => Impossible de déterminer la catégorie du matériel."); //$NON-NLS-1$ //$NON-NLS-2$
					continue;
				}
				// CHECK AlimConstraint
				AlimConstraint constraint;
				try {
					constraint = AlimConstraint.valueOf(constr);
				} catch (final Exception e) {
					this.readErrors.add("Ligne " + numLine //$NON-NLS-1$
							+ " => Impossible de déterminer la contrainte d'alimentation du matériel."); //$NON-NLS-1$
					continue;
				}
				// CHECK NodeCross
				final boolean cross = nodeCross == null || !nodeCross.equals("YES") ? false : true; //$NON-NLS-1$
				final NodeModel nodeModel = new NodeModel(name);
				nodeModel.setDescription(description);
				nodeModel.setCategory(cat);
				nodeModel.setAlimConstraint(constraint);
				nodeModel.setAllowedHooks(hooks);
				nodeModel.setCrossesFloor(cross);
				nodeModel.setImgName(imgName);
				this.modelSvc.save(nodeModel);
				numLine++;
			}
		} catch (final Exception e) {
			this.readErrors.add("Impossible de lire le fichier \"init/catalog.csv\" dans les ressources du projet."); //$NON-NLS-1$
		}
	}

	private List<HookType> convertCsvHookKeysToHookTypes(final String hooksString, final int numLine) {
		final List<HookType> ret = new ArrayList<>();
		if (hooksString == null || hooksString.trim().isEmpty()) {
			return ret;
		}
		final String[] hookKeys = hooksString.split(" "); //$NON-NLS-1$
		for (final String key : hookKeys) {
			final HookType hook = this.hooksByNames.get(key.trim());
			if (hook != null) {
				ret.add(hook);
			} else {
				this.readErrors.add("Ligne " + numLine + "=> HookType introuvable pour la clé : " + key); //$NON-NLS-1$
			}
		}
		return ret;
	}
}