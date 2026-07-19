package org.targol.resoplan.ui.utils;

import java.util.Objects;

import org.targol.resoplan.utils.PreferencesHelper;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ThemesHelper {

	public static Image getIcon(final String name) {
		final String path = "/images/custom/".concat(name).concat(".png"); //$NON-NLS-1$ //$NON-NLS-2$
		Image icon = new Image(Objects.requireNonNull(ThemesHelper.class.getResourceAsStream(path)));
		final Color iconColor = PreferencesHelper.getCurrentTheme().getImagesMainColor();
		if (!Color.WHITE.equals(iconColor)) {
			icon = GuiUtils.changeColorInImage(icon, Color.WHITE, iconColor);
		}
		return icon;
	}

	public static Image getCatalogIcon(final String name, final boolean themeDependant) {
		final String path = "/images/catalog/".concat(name).concat(".png"); //$NON-NLS-1$ //$NON-NLS-2$
		Image icon = new Image(Objects.requireNonNull(ThemesHelper.class.getResourceAsStream(path)));
		final Color iconColor = PreferencesHelper.getCurrentTheme().getImagesMainColor();
		if (themeDependant && !Color.WHITE.equals(iconColor)) {
			icon = GuiUtils.changeColorInImage(icon, Color.WHITE, iconColor);
		}
		return icon;
	}

	public enum Theme {
		// ATTENTION : CHANGER LES VALEURS DES COULEURS ICI EN CAS DE CHANGEMENT DANS
		// LES CSS
		DARK("Dark", "/style/dark.css", Color.WHITE, Color.web("#181818")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		GREY("Grey", "/style/grey.css", Color.WHITE, Color.web("#373e43")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		SUNNY("Sunny", "/style/sunny.css", Color.SADDLEBROWN, Color.web("#e8e6d3")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ICEBERG("Iceberg", "/style/iceberg.css", Color.MEDIUMBLUE, Color.web("#9dabbf")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		private final String name;
		private final String cssRelativePath;
		private final Color imagesMainColor;
		private final Color BackColor;

		private Theme(final String name, final String cssRelativePath, final Color imagesMainColor,
				final Color BackColor) {
			this.name = name;
			this.cssRelativePath = cssRelativePath;
			this.imagesMainColor = imagesMainColor;
			this.BackColor = BackColor;
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

		public Color getBackColor() {
			return this.BackColor;
		}

	}
}