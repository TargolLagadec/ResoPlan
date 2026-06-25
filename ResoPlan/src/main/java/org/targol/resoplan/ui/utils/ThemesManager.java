package org.targol.resoplan.ui.utils;

import java.util.Objects;

import org.targol.resoplan.utils.PreferencesManager;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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
		final String path = "/images/custom/".concat(name).concat(".png"); //$NON-NLS-1$ //$NON-NLS-2$
		Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
		final Color iconColor = PreferencesManager.getInstance().getCurrentTheme().getImagesMainColor();
		if (!Color.WHITE.equals(iconColor)) {
			icon = GuiUtils.changeColorInImage(icon, Color.WHITE, iconColor);
		}
		return icon;
	}

	public Image getCatalogIcon(final String name, final boolean themeDependant) {
		final String path = "/images/catalog/".concat(name).concat(".png"); //$NON-NLS-1$ //$NON-NLS-2$
		Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
		final Color iconColor = PreferencesManager.getInstance().getCurrentTheme().getImagesMainColor();
		if (themeDependant && !Color.WHITE.equals(iconColor)) {
			icon = GuiUtils.changeColorInImage(icon, Color.WHITE, iconColor);
		}
		return icon;
	}

	public enum Theme {
		DARK("Dark", "/style/dark.css", Color.WHITE), //$NON-NLS-1$ //$NON-NLS-2$
		GREY("Grey", "/style/grey.css", Color.WHITE), //$NON-NLS-1$ //$NON-NLS-2$
		SUNNY("Sunny", "/style/sunny.css", Color.SADDLEBROWN), //$NON-NLS-1$ //$NON-NLS-2$
		ICEBERG("Iceberg", "/style/iceberg.css", Color.MEDIUMBLUE); //$NON-NLS-1$ //$NON-NLS-2$

		private final String name;
		private final String cssRelativePath;
		private final Color imagesMainColor;

		private Theme(final String name, final String cssRelativePath, final Color imagesMainColor) {
			this.name = name;
			this.cssRelativePath = cssRelativePath;
			this.imagesMainColor = imagesMainColor;
		}

		public String getName() {
			return this.name;
		}

		public String getCssfilePath() {
			return this.cssRelativePath;
		}

		public Color getImagesMainColor() {
			return this.imagesMainColor;
		}

	}
}