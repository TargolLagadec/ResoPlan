package org.targol.resoplan.ui.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.services.ProjectsService;
import org.targol.resoplan.ui.panels.FloorsAdjustmentPanel;
import org.targol.resoplan.ui.panels.floornetwork.FloorsNetworksSplitPane;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

@SpringBootTest(classes = org.targol.resoplan.SpringApplication.class)
public class UiEventBusMemoryTest {

	public static final String ETAPE_INIT = "État initial"; //$NON-NLS-1$
	public static final String ETAPE_FIN = "État final"; //$NON-NLS-1$

	private static Map<String, Double> results = new HashMap<>();
	@Autowired
	private ProjectsService service;

	@BeforeAll
	public static void initJavaFX() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		Platform.startup(latch::countDown);
		latch.await();
	}

	@Test
	public void testMemoryLeaks() throws InterruptedException {
		final Project proj = this.service.openProjectWithFloorsAndNodes(this.service.getAllProjects().get(0)).get();
		this.service.setOpenedProject(proj);
		final CountDownLatch simulationLatch = new CountDownLatch(1);
		final List<WeakReference<Object>> weakRefs = new ArrayList<>();
		Platform.runLater(() -> {
			final StackPane root = new StackPane();
			final Scene scene = new Scene(root);
			lancerTestAntiFuite(proj, root, weakRefs, simulationLatch);
		});
		simulationLatch.await();
		forceGarbageCollector(ETAPE_FIN);
		final double memInit = results.get(ETAPE_INIT);
		final double memFin = results.get(ETAPE_FIN);
		final double diff = memFin - memInit;
		assertEquals(memInit, memFin, String.format("FUITE MEMOIRE DE %.2f Mo", diff));
	}

	private void lancerTestAntiFuite(final Project proj, final Pane conteneurParent,
			final List<WeakReference<Object>> weakRefs, final CountDownLatch latch) {
		System.out.println("=== DÉBUT DE LA SIMULATION ===");
		forceGarbageCollector(ETAPE_INIT);
		final int nbRepetitions = 30;
		final Timeline timeline = new Timeline();
		final double intervalleMs = 10;
		for (int i = 0; i < nbRepetitions; i++) {
			final int index = i;
			final KeyFrame frame = new KeyFrame(Duration.millis(i * intervalleMs), event -> {
				for (Node child : conteneurParent.getChildren()) {
					child = null;
				}
				conteneurParent.getChildren().clear();
				if (index % 2 == 0) {
					final FloorsAdjustmentPanel p1 = new FloorsAdjustmentPanel(proj);
					weakRefs.add(new WeakReference<>(p1));
					conteneurParent.getChildren().add(p1);
				} else {
					final FloorsNetworksSplitPane p2 = new FloorsNetworksSplitPane(proj);
					weakRefs.add(new WeakReference<>(p2));
					conteneurParent.getChildren().add(p2);
				}
			});
			timeline.getKeyFrames().add(frame);
		}

		timeline.setOnFinished(event -> {
			conteneurParent.getChildren().clear();
			System.out.println("Simulation terminée. Attente de la libération des ressources...");
			final PauseTransition pause = new PauseTransition(Duration.seconds(1));
			pause.setOnFinished(e -> {
				latch.countDown();
			});
			pause.play();
		});

		timeline.play();
	}

	public static void forceGarbageCollector(final String etape) {
		for (int i = 0; i < 5; i++) {
			System.gc();
			try {
				Thread.sleep(500);
			} catch (final InterruptedException ignored) {
			}
		}
		final Runtime runtime = Runtime.getRuntime();
		final long memoireUtiliseeOctets = runtime.totalMemory() - runtime.freeMemory();
		final double memoireMo = memoireUtiliseeOctets / (1024.0 * 1024.0);
		System.out.printf("[%s] Mémoire utilisée : %.2f Mo%n", etape, memoireMo);
		results.put(etape, memoireMo);
	}
}