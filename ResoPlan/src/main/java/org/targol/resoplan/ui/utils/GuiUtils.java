package org.targol.resoplan.ui.utils;

import java.util.Objects;
import java.util.Optional;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.model.catalog.HookType;
import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.model.catalog.enums.NodeCategory;
import org.targol.resoplan.model.catalog.enums.NodeCross;
import org.targol.resoplan.services.HookTypesService;
import org.targol.resoplan.services.NodeModelsService;
import org.targol.resoplan.ui.components.CatalogButton;
import org.targol.resoplan.ui.components.CatalogButtonUpOrDown;
import org.targol.resoplan.ui.components.LayerLinkButton;
import org.targol.resoplan.ui.utils.events.LinkTracingEvent;
import org.targol.resoplan.ui.utils.events.NodePlacementEvent;
import org.targol.resoplan.utils.MiscUtils;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.beans.binding.BooleanBinding;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class GuiUtils {

	/*
	 * marge de 200px pour ne pas coller aux bords du ScrollPane
	 */
	public static final double NETWORK_PLANS_MARGIN = 0;

	/**
	 * Convertit une position IHM (local au Pane) en coordonnées absolues Terrain (BDD)
	 */
	public static Point2D ihmToAbsolute(final Floor floor, final double ioX, final double ioY) {
		final double absX = ioX - NETWORK_PLANS_MARGIN - floor.getShiftX();
		final double absY = ioY - NETWORK_PLANS_MARGIN - floor.getShiftY();
		return new Point2D(absX, absY);
	}

	/**
	 * Convertit une coordonnée absolue Terrain (BDD) en position de pixel IHM pour le rendu
	 */
	public static Point2D absoluteToIo(final Floor floor, final double absX, final double absY) {
		final double ioX = absX + NETWORK_PLANS_MARGIN + floor.getShiftX();
		final double ioY = absY + NETWORK_PLANS_MARGIN + floor.getShiftY();
		return new Point2D(ioX, ioY);
	}

	public static Bounds calculateUniversalProjectBounds(final Project project) {
		double maxWidth = 0;
		double maxHeight = 0;

		for (final Floor f : project.getFloors()) {
			final double footprintX = f.getImgWidth() * f.getZoomFactor() + Math.abs(f.getShiftX());
			final double footprintY = f.getImgHeight() * f.getZoomFactor() + Math.abs(f.getShiftY());
			if (footprintX > maxWidth) {
				maxWidth = footprintX;
			}
			if (footprintY > maxHeight) {
				maxHeight = footprintY;
			}
		}
		return new BoundingBox(0, 0, maxWidth + NETWORK_PLANS_MARGIN * 2, maxHeight + NETWORK_PLANS_MARGIN * 2);
	}

	public static Image getCatalogIcon(final String name, final Color replacmentColor) {
		final String path = "/images/catalog/".concat(name).concat(".png"); //$NON-NLS-1$ //$NON-NLS-2$
		Image icon = new Image(Objects.requireNonNull(GuiUtils.class.getResourceAsStream(path)));
		icon = GuiUtils.changeColorInImage(icon, Color.WHITE, replacmentColor);
		return icon;
	}

	public static Image colorizeImage(final Image originalImage, final Color targetColor) {
		if (originalImage == null) {
			return originalImage;
		}
		final int width = (int) originalImage.getWidth();
		final int height = (int) originalImage.getHeight();
		// if image not yet fully loaded, exit
		if (width <= 0 || height <= 0) {
			return originalImage;
		}
		// Colo black : no change in original image
		if (Color.BLACK.equals(targetColor)) {
			return originalImage;
		}
		final WritableImage tintedImage = new WritableImage(width, height);
		final PixelReader reader = originalImage.getPixelReader();
		final PixelWriter writer = tintedImage.getPixelWriter();

		// Changing black or dark grey pixels with pixels of chosen color
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				final Color pixelColor = reader.getColor(x, y);
				if (pixelColor.getBrightness() < 0.6 && pixelColor.getOpacity() > 0.1) {
					final Color newPixel = new Color(targetColor.getRed(), targetColor.getGreen(),
							targetColor.getBlue(), pixelColor.getOpacity());
					writer.setColor(x, y, newPixel);
				} else {
					writer.setColor(x, y, pixelColor);
				}
			}
		}
		return tintedImage;
	}

	public static Image changeColorInImage(final Image originalImage, final Color initialColor,
			final Color targetColor) {
		if (originalImage == null) {
			return originalImage;
		}
		final int width = (int) originalImage.getWidth();
		final int height = (int) originalImage.getHeight();
		// if image not yet fully loaded, exit
		if (width <= 0 || height <= 0) {
			return originalImage;
		}
		final WritableImage resultImage = new WritableImage(width, height);
		final PixelReader reader = originalImage.getPixelReader();
		final PixelWriter writer = resultImage.getPixelWriter();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				final Color pixelColor = reader.getColor(x, y);
				if (areEqualsColorsExceptTransparency(initialColor, pixelColor)) {
					final Color newColor = new Color(targetColor.getRed(), targetColor.getGreen(),
							targetColor.getBlue(), pixelColor.getOpacity());
					writer.setColor(x, y, newColor);
				} else {
					writer.setColor(x, y, pixelColor);
				}
			}
		}
		return resultImage;
	}

	private static boolean areEqualsColorsExceptTransparency(final Color col1, final Color col2) {
		return col1.getRed() == col2.getRed() && col1.getGreen() == col2.getGreen() && col1.getBlue() == col2.getBlue();
	}

	public static Image createImageViewFromInternalPng(final String imageInternalPath) {
		return new Image(GuiUtils.class.getResourceAsStream(imageInternalPath));
	}

	public static void errorAlert(final String msg) {
		genericAlert(Messages.getString("Generic.error.title"), msg, Alert.AlertType.ERROR); //$NON-NLS-1$
	}

	public static void infoAlert(final String msg) {
		genericAlert(Messages.getString("Generic.info.title"), msg, Alert.AlertType.INFORMATION); //$NON-NLS-1$
	}

	public static Optional<String> getTextFromInputDialog(final String title, final String msg,
			final String initialValue, final String regExp) {
		final TextInputDialog prompt;
		if (initialValue == null) {
			prompt = new TextInputDialog();
		} else {
			prompt = new TextInputDialog(initialValue);
		}
		prompt.setTitle(title);
		prompt.setContentText(msg);
//		ThemesManager.getInstance().setTheme(prompt.getDialogPane());
		if (regExp != null) {
			final TextField textField = prompt.getEditor();
			textField.setTextFormatter(new TextFormatter<String>(change -> {
				if (change.getControlNewText().matches(regExp)) {
					return change;
				}
				return null;
			}));
		}
		return prompt.showAndWait();
	}

	private static void genericAlert(final String title, final String contentText, final Alert.AlertType type) {
		final Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setContentText(contentText);
		alert.showAndWait();
	}

	private static BooleanBinding buildNewDisabledBinding(final LayerType layer, final NodeCross direction) {
		switch (direction) {
		case NONE:
			return BindingBuilder.createDefaultBuilderFor(layer).build();
		case GOES_DOWN:
			return BindingBuilder.createDefaultBuilderFor(layer).activeFloorAndProjectMatch((floor, project) -> {
				if (floor == null || project == null) {
					return true;
				}
				return floor.getNumber() == project.getLowestFloorNumber();
			}).build();
		case GOES_UP:
			return BindingBuilder.createDefaultBuilderFor(layer).activeFloorAndProjectMatch((floor, project) -> {
				if (floor == null || project == null) {
					return true;
				}
				return floor.getNumber() == project.getTopmostFloorNumber();
			}).build();
		default:
			return null;
		}
	}

	public static VBox buildCategorizedNodeModelsToolbar(final LayerType layer, final NodeCategory cat,
			final NodeModelsService modelsService, final ToggleGroup placementGroup) {
		final VBox ret = new VBox(5);
		ret.getStyleClass().add("toolgroup"); //$NON-NLS-1$
		ret.setAlignment(Pos.CENTER_LEFT);
		final Label catLab = new Label(cat.getLabel());
		ret.getChildren().add(catLab);
		final HBox buttons = new HBox(5);
		for (final NodeModel model : modelsService.getAllByCategory(cat)) {
			if (model.isCrossesFloor()) {
				final CatalogButtonUpOrDown btnUp = buildUpDownButton(layer, model, NodeCross.GOES_UP);
				btnUp.setToggleGroup(placementGroup); // Partagé pour qu'un seul outil soit actif à la fois
				btnUp.setUserData(model);
				buttons.getChildren().add(btnUp);
				final CatalogButtonUpOrDown btnDown = buildUpDownButton(layer, model, NodeCross.GOES_DOWN);
				btnDown.setToggleGroup(placementGroup); // Partagé pour qu'un seul outil soit actif à la fois
				btnDown.setUserData(model);
				buttons.getChildren().add(btnDown);
			} else {
				final CatalogButton btn = new CatalogButton(model, () -> NodePlacementEvent.of(layer,
						AppStateManager.getInstance().activeFloorProperty().get(), model));
				btn.disableProperty().bind(BindingBuilder.createDefaultBuilderFor(layer).build());
				btn.setToggleGroup(placementGroup); // Partagé pour qu'un seul outil soit actif à la fois
				btn.setUserData(model);
				buttons.getChildren().add(btn);
			}
		}
		ret.getChildren().add(buttons);
		return ret;
	}

	private static CatalogButtonUpOrDown buildUpDownButton(final LayerType layer, final NodeModel model,
			final NodeCross nodeCross) {
		final CatalogButtonUpOrDown ret = new CatalogButtonUpOrDown(model, nodeCross, () -> NodePlacementEvent.of(layer,
				AppStateManager.getInstance().activeFloorProperty().get(), model, nodeCross));
		ret.disableProperty().bind(buildNewDisabledBinding(layer, nodeCross));
		return ret;
	}

	public static VBox buildMiscNodeModelsToolbarFilteredByHookTypes(final LayerType layer,
			final NodeModelsService modelsService, final ToggleGroup placementGroup) {
		final VBox ret = new VBox(10);
		ret.getStyleClass().add("toolgroup"); //$NON-NLS-1$
		ret.setAlignment(Pos.CENTER_LEFT);
		final NodeCategory cat = NodeCategory.DIVERS;
		final Label catLab = new Label(cat.getLabel());
		ret.getChildren().add(catLab);
		final HBox buttons = new HBox(5);
		for (final NodeModel model : modelsService.getAllByCategory(cat)) {
			final Optional<NodeModel> optMod = modelsService.getByIdWithallowedHooks(model.getId());
			if (optMod.isEmpty()) {
				continue;
			}
			final NodeModel fullmodel = optMod.get();
			final HookTypesService hooksService = SpringContextHelper.getBean(HookTypesService.class);
			if (MiscUtils.containsAny(fullmodel.getAllowedHooks(), hooksService.getAllFromLayer(layer))) {
				if (model.isCrossesFloor()) {
					final CatalogButtonUpOrDown btnUp = buildUpDownButton(layer, fullmodel, NodeCross.GOES_UP);
					btnUp.setToggleGroup(placementGroup); // Partagé pour qu'un seul outil soit actif à la fois
					btnUp.setUserData(fullmodel);
					buttons.getChildren().add(btnUp);
					final CatalogButtonUpOrDown btnDown = buildUpDownButton(layer, fullmodel, NodeCross.GOES_DOWN);
					btnDown.setToggleGroup(placementGroup); // Partagé pour qu'un seul outil soit actif à la fois
					btnDown.setUserData(fullmodel);
					buttons.getChildren().add(btnDown);
				} else {

					final CatalogButton btn = new CatalogButton(fullmodel, () -> NodePlacementEvent.of(layer,
							AppStateManager.getInstance().activeFloorProperty().get(), fullmodel));
					btn.setToggleGroup(placementGroup); // Partagé pour qu'un seul outil soit actif à la fois
					btn.disableProperty().bind(BindingBuilder.createDefaultBuilderFor(layer).build());
					btn.setUserData(fullmodel);
					buttons.getChildren().add(btn);
				}
			}
		}
		ret.getChildren().add(buttons);
		return ret;
	}

	public static Node buildLinksToolbar(final LayerType layer, final ToggleGroup placementGroup) {
		final VBox ret = new VBox(10);
		ret.getStyleClass().add("toolgroup"); //$NON-NLS-1$
		ret.setAlignment(Pos.CENTER_LEFT);
		final Label catLab = new Label(Messages.getString("ToolBar.links.label")); //$NON-NLS-1$
		ret.getChildren().add(catLab);
		final HBox buttons = new HBox(5);
		final HookTypesService hooksService = SpringContextHelper.getBean(HookTypesService.class);
		for (final HookType hook : hooksService.getAllFromLayer(layer)) {
			final LayerLinkButton btn = new LayerLinkButton(hook,
					() -> LinkTracingEvent.of(layer, AppStateManager.getInstance().activeFloorProperty().get(), hook));
			btn.setToggleGroup(placementGroup); // Partagé pour qu'un seul outil soit actif à la fois
			btn.disableProperty().bind(buildNewDisabledBinding(layer, NodeCross.NONE));
			btn.setUserData(hook);
			buttons.getChildren().add(btn);
		}
		ret.getChildren().add(buttons);
		return ret;
	}

}
