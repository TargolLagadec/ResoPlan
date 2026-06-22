package org.targol.resoplan.ui.utils;

public enum AppState {
	NO_PROJECT, // Pas de projet ouvert
	PROJECT_WITHOUT_IMAGES, // Projet ouvert, mais config plans incomplète
	PROJECT_READY // Projet 100% prêt pour le dessin
}