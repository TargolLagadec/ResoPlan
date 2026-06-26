package org.targol.resoplan.ui.panels.floornetwork.layers.evac;

import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.catalog.HookType;
import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.ui.utils.AppStateManager;
import org.targol.resoplan.ui.utils.GuiUtils;
import org.targol.resoplan.ui.utils.events.LinkTracingEvent;
import org.targol.resoplan.ui.utils.events.NodePlacementEvent;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class EvacuationsCanvas extends Canvas {

	private final GraphicsContext gc;
	private final Color drawingColor = Color.BROWN;
	private final Color backroundColor = Color.GREY;
	private boolean isDrawingTube = false;
	private double startX, startY;

	private NodeModel currentNodeModel;
	private HookType currentHookType;
	private final Floor floor;

	public EvacuationsCanvas(final Floor floor, final double width, final double height) {
		super(width, height);
		this.floor = floor;
		this.gc = this.getGraphicsContext2D();
		this.gc.setFill(this.drawingColor);
		this.gc.setStroke(this.drawingColor);
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleCanvasClick);
		addEventHandler(NodePlacementEvent.WATER_EVAC, evt -> {
			final Floor activeFloor = AppStateManager.getInstance().activeFloorProperty().get();
			if (activeFloor != null && activeFloor.equals(this.floor)) {
				final NodeModel newTool = evt.getModel();
				if (newTool != null) {
					System.err.println(
							"Dans le listener EvacuationsCanvas, l'outil actuel est set à " + newTool.getName());
					this.setCurrentNodeModel(newTool);
				}
			}
			evt.consume();
		});

		addEventHandler(LinkTracingEvent.WATER_EVAC, evt -> {
			final Floor activeFloor = AppStateManager.getInstance().activeFloorProperty().get();
			if (activeFloor != null && activeFloor.equals(this.floor)) {
				final HookType newTool = evt.getHook();
				if (newTool != null) {
					System.err.println(
							"Dans le listener EvacuationsCanvas, l'outil actuel est set à " + newTool.getHookKey());
					this.setCurrentHookType(newTool);
				}
			}
			evt.consume();
		});
	}

	private void handleCanvasClick(final MouseEvent event) {
		final double x = event.getX();
		final double y = event.getY();
		if (this.currentHookType == null && this.currentNodeModel == null) {
			return;
		}
		if (this.currentNodeModel != null) {
			createNode(x, y);
			this.isDrawingTube = false;

		} else if (this.currentHookType != null) {
			if (!this.isDrawingTube) {
				this.startX = x;
				this.startY = y;
				this.isDrawingTube = true;
				this.gc.setFill(Color.BLUE);
				this.gc.fillOval(x - 2, y - 2, 4, 4);
			} else {
				dessinerTuyau(this.startX, this.startY, x, y);
				this.isDrawingTube = false;
			}
		}
		event.consume();
	}

	private void createNode(final double x, final double y) {
		this.gc.setFill(this.backroundColor);
		this.gc.fillRoundRect(x - 12, y - 12, 24, 24, 10, 10);
		final Image img = GuiUtils.getCatalogIcon(this.currentNodeModel.getImgName(), this.drawingColor);
		this.gc.drawImage(img, x - 12, y - 12, 24, 24);
	}

	private void dessinerTuyau(final double x1, final double y1, final double x2, final double y2) {
		this.gc.setStroke(Color.BROWN);
		// TODO Modifier ça
		final double tubeDiam = 100;
		this.gc.setLineWidth(tubeDiam / 10);
		this.gc.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
		this.gc.strokeLine(x1, y1, x2, y2);
	}

	public void setCurrentNodeModel(final NodeModel tool) {
		this.currentNodeModel = tool;
		this.isDrawingTube = false;
	}

	public void setCurrentHookType(final HookType currentHookType) {
		this.currentHookType = currentHookType;
		this.isDrawingTube = false;
	}

}
