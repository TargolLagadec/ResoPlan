package org.targol.resoplan.ui.utils;

import java.util.function.Predicate;

import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.Project;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ObjectProperty;

public class BindingBuilder {

	private final AppStateManager stateMgr = AppStateManager.getInstance();
	// De base, le bouton n'est pas désactivé
	private BooleanExpression expression = BooleanExpression
			.booleanExpression(Bindings.createBooleanBinding(() -> false));

	private BindingBuilder() {
	}

	public static BindingBuilder createDefaultBuilderFor(final LayerType type) {
		return BindingBuilder.disableWhen().stateIsNot(AppState.PROJECT_READY).noActiveFloor().networkLayerIsNot(type);
	}

	public static BindingBuilder disableWhen() {
		return new BindingBuilder();
	}

	/** Condition : L'état global de l'application est dans un des états fournis */
	public BindingBuilder stateIs(final AppState... states) {
		BooleanExpression cond = this.stateMgr.currentAppStateProperty().isNull();
		for (final AppState s : states) {
			cond = cond.or(this.stateMgr.currentAppStateProperty().isEqualTo(s));
		}
		this.expression = this.expression.or(cond);
		return this;
	}

	/** Condition : L'état global de l'application N'EST PAS celui fourni */
	public BindingBuilder stateIsNot(final AppState state) {
		this.expression = this.expression.or(this.stateMgr.currentAppStateProperty().isNotEqualTo(state));
		return this;
	}

	/** Condition : Aucun étage n'est actuellement sélectionné */
	public BindingBuilder noActiveFloor() {
		this.expression = this.expression.or(this.stateMgr.activeFloorProperty().isNull());
		return this;
	}

	/** Condition : Le calque réseau actif n'est pas celui attendu */
	public BindingBuilder networkLayerIsNot(final LayerType layer) {
		this.expression = this.expression.or(this.stateMgr.activeNetworkLayerProperty().isNotEqualTo(layer));
		return this;
	}

	/** Condition custom basée sur l'étage actif (ex: numéro d'étage) */
	public BindingBuilder activeFloorMatches(final Predicate<Floor> floorPredicate) {
		final BooleanBinding customBinding = Bindings.createBooleanBinding(() -> {
			final Floor f = this.stateMgr.activeFloorProperty().get();
			return f == null || floorPredicate.test(f);
		}, this.stateMgr.activeFloorProperty());

		this.expression = this.expression.or(customBinding);
		return this;
	}

	/** Condition custom dépendante à la fois de l'étage ET du projet courant */
	public BindingBuilder activeFloorAndProjectMatch(final BiPredicateForBinding floorProjectPredicate) {
		final ObjectProperty<Floor> floorProp = this.stateMgr.activeFloorProperty();
		final ObjectProperty<Project> projProp = this.stateMgr.currentProjectProperty();

		final BooleanBinding customBinding = Bindings.createBooleanBinding(() -> {
			final Floor f = floorProp.get();
			final Project p = projProp.get();
			return floorProjectPredicate.test(f, p);
		}, floorProp, projProp);

		this.expression = this.expression.or(customBinding);
		return this;
	}

	/** Termine le Builder et retourne le BooleanBinding final pour le bouton */
	public BooleanBinding build() {
		return (BooleanBinding) this.expression;
	}

	/**
	 * Interface fonctionnelle SAM pour la gestion du couple Floor/Project dans le
	 * binding
	 */
	@FunctionalInterface
	public interface BiPredicateForBinding {
		boolean test(Floor floor, Project project);
	}
}