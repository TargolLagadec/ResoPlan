package org.targol.resoplan.ui.utils;

import java.util.Objects;

import org.targol.resoplan.utils.PreferencesManager;

import javafx.scene.image.Image;

public class ThemesManager {

	private static ThemesManager instance;

	public static ThemesManager getInstance() {
		if (instance == null) {
			instance = new ThemesManager();
		}

		return instance;
	}

	private ThemesManager() {
	}

	public Image getIcon(final String name) {
		final String path = PreferencesManager.getInstance().getCurrentTheme().getImagesRelativePath().concat(name)
				.concat(".png"); //$NON-NLS-1$
		return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
	}

	public enum Theme {
		DARK("Dark", "/style/dark.css", "/images/dark/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		GREY("Grey", "/style/grey.css", "/images/grey/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		SUNNY("Sunny", "/style/sunny.css", "/images/sunny/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ICEBERG("Iceberg", "/style/iceberg.css", "/images/iceberg/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		private final String name;
		private final String cssRelativePath;
		private final String imagesRelativePath;

		private Theme(final String name, final String cssRelativePath, final String imagesRelativePath) {
			this.name = name;
			this.cssRelativePath = cssRelativePath;
			this.imagesRelativePath = imagesRelativePath;
		}

		public String getName() {
			return this.name;
		}

		public String getCssfilePath() {
			return this.cssRelativePath;
		}

		public String getImagesRelativePath() {
			return this.imagesRelativePath;
		}

	}
}