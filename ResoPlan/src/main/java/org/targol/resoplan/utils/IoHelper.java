package org.targol.resoplan.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class IoHelper {

	public static File getFloorsImagesDirectory() {
		// Récupère le dossier où l'application est lancée
		final String workingDir = System.getProperty("user.dir"); //$NON-NLS-1$
		final File targetDir = new File(workingDir + File.separator + "data" + File.separator + "images"); //$NON-NLS-1$ //$NON-NLS-2$

		// Crée les dossiers /data/images s'ils n'existent pas encore
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		return targetDir;
	}

	public static String generateFloorPlanImageName(final File sourceFile, final int projectId, final int floorId) {
		final String originalName = sourceFile.getName();
		String extension = ""; //$NON-NLS-1$
		final int i = originalName.lastIndexOf('.');
		if (i > 0) {
			extension = originalName.substring(i);
		}
		return String.format("Project%d_Floor%d%s", projectId, floorId, extension); //$NON-NLS-1$
	}

	public static File copyFile(final File origin, final String targetName) {
		final File targetDir = IoHelper.getFloorsImagesDirectory();
		final File targetFile = new File(targetDir, targetName);
		try {
			Files.copy(origin.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			return targetFile;
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
