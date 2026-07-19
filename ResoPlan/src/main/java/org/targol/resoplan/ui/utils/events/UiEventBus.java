package org.targol.resoplan.ui.utils.events;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Node;

public class UiEventBus {

	private static final Group EVENT_HUB = new Group();

	/**
	 * Permet de s'abonner en tant que receveur d'évènement métier. Cette méthode
	 * gère le désabonnement pour gérer les fuites mémoires.
	 *
	 * @param <T>          Type d'évèment auquel le receveur s'abonne
	 * @param handlerOwner Le composant graphique qui s'abonne.
	 * @param event        l'évènement auquel on s'abonne
	 * @param handler      la méthode appelée quand l'évènement arrive.
	 */
	public static <T extends GenericActionEvent> void register(final Node handlerOwner, final EventType<T> event,
			final EventHandler<? super T> handler) {

		// Si le composant est déjà dans une scène active au démarrage, on l'abonne
		if (handlerOwner == null || handlerOwner.getScene() != null) {
			EVENT_HUB.addEventHandler(event, handler);
		}

		if (handlerOwner != null) {
			handlerOwner.sceneProperty().addListener((obs, oldScene, newScene) -> {
				if (newScene == null) {
					unregister(event, handler);
				} else {
					unregister(event, handler);
					EVENT_HUB.addEventHandler(event, handler);
				}
			});
		}
	}

	/**
	 * Permet de se desabonner en tant que receveur d'évènement métier
	 *
	 * @param <T>     Type d'évèment auquel le receveur s'abonne
	 * @param handler la méthode appelée quand l'évènement arrive.
	 */
	public static <T extends GenericActionEvent> void unregister(final EventType<T> type,
			final EventHandler<? super T> handler) {
		EVENT_HUB.removeEventHandler(type, handler);
	}

	/**
	 * Recoit un évènement pour le propager à tous les abonnés.
	 *
	 * @param event l'évènement à dispatcher
	 */
	public static void send(final GenericActionEvent event) {
		EVENT_HUB.fireEvent(event);
	}
}
