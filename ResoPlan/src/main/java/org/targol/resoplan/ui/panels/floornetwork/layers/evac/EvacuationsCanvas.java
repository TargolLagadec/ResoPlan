package org.targol.resoplan.ui.panels.floornetwork.layers.evac;

import org.targol.resoplan.model.Floor;
import org.targol.resoplan.ui.utils.AppStateManager;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class EvacuationsCanvas extends Canvas {

	private boolean isDrawingTube = false;
	private double startX, startY;

	private EvacMode currentMode;
	private final Floor floor;

	public EvacuationsCanvas(final Floor floor, final double width, final double height) {
		super(width, height);
		this.floor = floor;
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleCanvasClick);
		AppStateManager.getInstance().currentEvacModeProperty().addListener((obs, oldMode, newMode) -> {
        Floor activeFloor = AppStateManager.getInstance().activeFloorProperty().get();
        if (activeFloor != null && activeFloor.equals(this.floor)) {
			System.err.println("Dans le listener EvacuationsCanvas, l'EvacMode est set à " + newMode);
			if (newMode != null) {
				this.setCurrentMode(newMode);
			}}
		});
	}

	private void handleCanvasClick(final MouseEvent event) {
		final double x = event.getX();
		final double y = event.getY();
		final GraphicsContext gc = this.getGraphicsContext2D();
		if (this.currentMode == null) {
			return;
		}
		switch (this.currentMode) {
		case DESCENT_32:
		case DESCENT_40:
		case DESCENT_100:
			dessinerDescente(gc, x, y, this.currentMode.getDiam());
			this.isDrawingTube = false;
			AppStateManager.getInstance().setCurrentEvacMode(null);
			break;

		case RISE_32:
		case RISE_40:
		case RISE_100:
			dessinerMontee(gc, x, y, this.currentMode.getDiam());
			this.isDrawingTube = false;
			AppStateManager.getInstance().setCurrentEvacMode(null);
			break;

		case TUBE_32:
		case TUBE_40:
		case TUBE_100:
			if (!this.isDrawingTube) {
				this.startX = x;
				this.startY = y;
				this.isDrawingTube = true;
				gc.setFill(Color.BLUE);
				gc.fillOval(x - 2, y - 2, 4, 4);
			} else {
				dessinerTuyau(gc, this.startX, this.startY, x, y, this.currentMode.getDiam());
				this.isDrawingTube = false;
				AppStateManager.getInstance().setCurrentEvacMode(null);
			}
			break;

		case DELETE:
			// Note pour l'effacement : Le Canvas est un tableau de pixels "bête".
			// Pour effacer un élément précis (DELETE), il faudra stocker tes tuyaux dans
			// une liste d'objets métier,
			// vider le canvas avec gc.clearRect() et tout redessiner sauf l'élément
			// supprimé.
			AppStateManager.getInstance().setCurrentEvacMode(null);
			break;
		case HOME_OUT:
			AppStateManager.getInstance().setCurrentEvacMode(null);
			break;
		default:
			break;
		}
		event.consume();
	}

	private void dessinerDescente(final GraphicsContext gc, final double x, final double y, final double tubeDiam) {
		gc.setStroke(Color.BROWN); // Couleur conventionnelle pour les eaux usées
		gc.setLineWidth(2);
		gc.strokeOval(x - 8, y - 8, 16, 16);
		gc.setFill(Color.BROWN);
		gc.fillOval(x - 4, y - 4, 8, 8); // Rond plein au centre = ça descend
		gc.fillText(Double.toString(tubeDiam / 10), x + 8, y + 8);
	}

	private void dessinerMontee(final GraphicsContext gc, final double x, final double y, final double tubeDiam) {
		gc.setStroke(Color.BROWN);
		gc.setLineWidth(2);
		gc.strokeOval(x - 8, y - 8, 16, 16);
		gc.strokeLine(x - 6, y - 6, x + 6, y + 6);
		gc.strokeLine(x + 6, y - 6, x - 6, y + 6);
		gc.setFill(Color.BROWN);
		gc.fillText(Double.toString(tubeDiam / 10), x + 8, y + 8);
	}

	private void dessinerTuyau(final GraphicsContext gc, final double x1, final double y1, final double x2,
			final double y2, final double tubeDiam) {
		gc.setStroke(Color.BROWN);
		gc.setLineWidth(tubeDiam / 10);
		gc.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
		gc.strokeLine(x1, y1, x2, y2);
	}

	public void setCurrentMode(final EvacMode mode) {
		this.currentMode = mode;
		this.isDrawingTube = false;
	}
}
