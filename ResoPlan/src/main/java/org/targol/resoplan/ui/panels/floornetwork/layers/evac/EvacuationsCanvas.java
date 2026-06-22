package org.targol.resoplan.ui.panels.floornetwork.layers.evac;

import org.targol.resoplan.model.Floor;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class EvacuationsCanvas extends Canvas {

	private boolean isDrawingTube = false;
	private double startX, startY;

	private EvacMode currentMode = EvacMode.TUBE;
	private final Floor floor;

	public EvacuationsCanvas(final Floor floor, final double width, final double height) {
		super(width, height);
		this.floor = floor;
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleCanvasClick);
	}

	private void handleCanvasClick(final MouseEvent event) {
		final double x = event.getX();
		final double y = event.getY();
		final GraphicsContext gc = this.getGraphicsContext2D();

		switch (this.currentMode) {
		case DESCENT:
			dessinerDescente(gc, x, y);
			this.isDrawingTube = false; // Reset au cas où
			break;

		case RISE:
			dessinerMontee(gc, x, y);
			this.isDrawingTube = false;
			break;

		case TUBE:
			if (!this.isDrawingTube) {
				// Premier clic : on mémorise le point de départ
				this.startX = x;
				this.startY = y;
				this.isDrawingTube = true;
				// Optionnel : dessiner un petit point temporaire de départ
				gc.setFill(Color.BLUE);
				gc.fillOval(x - 2, y - 2, 4, 4);
			} else {
				// Second clic : on trace la ligne du tuyau
				dessinerTuyau(gc, this.startX, this.startY, x, y);
				this.isDrawingTube = false; // Le tuyau est posé
			}
			break;

		case DELETE:
			// Note pour l'effacement : Le Canvas est un tableau de pixels "bête".
			// Pour effacer un élément précis (DELETE), il faudra stocker tes tuyaux dans
			// une liste d'objets métier,
			// vider le canvas avec gc.clearRect() et tout redessiner sauf l'élément
			// supprimé.
			break;
		}
	}

	// --- FONCTIONS DE DESSIN GÉOMÉTRIQUE (Exigence 2) ---

	private void dessinerDescente(final GraphicsContext gc, final double x, final double y) {
		gc.setStroke(Color.BROWN); // Couleur conventionnelle pour les eaux usées
		gc.setLineWidth(2);

		// Exemple de symbole Descente : Un cercle barré d'une croix ou avec un rond
		// plein
		gc.strokeOval(x - 8, y - 8, 16, 16);
		gc.setFill(Color.BROWN);
		gc.fillOval(x - 4, y - 4, 8, 8); // Rond plein au centre = ça descend
	}

	private void dessinerMontee(final GraphicsContext gc, final double x, final double y) {
		gc.setStroke(Color.BROWN);
		gc.setLineWidth(2);

		// Exemple de symbole Montée : Un cercle vide avec une croix à l'intérieur
		gc.strokeOval(x - 8, y - 8, 16, 16);
		gc.strokeLine(x - 6, y - 6, x + 6, y + 6);
		gc.strokeLine(x + 6, y - 6, x - 6, y + 6);
	}

	private void dessinerTuyau(final GraphicsContext gc, final double x1, final double y1, final double x2,
			final double y2) {
		gc.setStroke(Color.BROWN);
		gc.setLineWidth(4); // Un tuyau horizontal est dessiné plus épais
		gc.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);

		// Trace la ligne droite entre le premier et le second clic
		gc.strokeLine(x1, y1, x2, y2);
	}

	// Setter pour changer le mode depuis ton contrôleur principal ou une barre
	// d'outils
	public void setCurrentMode(final EvacMode mode) {
		this.currentMode = mode;
		this.isDrawingTube = false; // Sécurité
	}
}
