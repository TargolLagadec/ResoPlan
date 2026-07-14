package org.targol.resoplan.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.ui.utils.AppState;

public class MiscUtils {

	public static AppState getAppState(final Project proj) {
		if (proj == null) {
			return AppState.NO_PROJECT;
		}
		if (proj.getPlansScale() <= 0) {
			return AppState.PROJECT_INCOMPLETE;
		}
		for (final Floor floor : proj.getFloors()) {
			if (floor.getImgPath() == null) {
				return AppState.PROJECT_INCOMPLETE;
			}
		}
		return AppState.PROJECT_READY;
	}

	/**
	 * Ramène les éléments communs de deux {@link Set}.
	 *
	 * @param <T>   Type des éléments des Sets.
	 * @param list1 premier Set
	 * @param list2 second Set
	 * @return les éléments communs
	 */
	public static <T> Set<T> getCommonElements(final Set<T> list1, final Set<T> list2) {
		if (list1 == null || list2 == null) {
			return Set.of();
		}
		final Set<T> set1 = new HashSet<>(list1);
		return list2.stream().filter(set1::contains).distinct().collect(Collectors.toSet());
	}

	/**
	 * Ramène les éléments communs de deux {@link List}.
	 *
	 * @param <T>   Type des éléments des Lists.
	 * @param list1 première List
	 * @param list2 seconde List
	 * @return les éléments communs
	 */
	public static <T> List<T> getCommonElements(final List<T> list1, final List<T> list2) {
		if (list1 == null || list2 == null) {
			return List.of();
		}
		final Set<T> set1 = new HashSet<>(list1);
		return list2.stream().filter(set1::contains).distinct().collect(Collectors.toList());
	}

	/**
	 * Returns true if given lists have at least a common element
	 *
	 * @param <T>   Type contained in the lists
	 * @param list1 first list
	 * @param list2 second list
	 * @return true if given lists have at least a common element
	 */
	public static <T> boolean containsAny(final List<T> list1, final List<T> list2) {
		final Set<T> set1 = new HashSet<>(list1);
		return list2.stream().anyMatch(set1::contains);
	}

	/**
	 * Returns true if given sets have at least a common element
	 *
	 * @param <T>   Type contained in the sets
	 * @param list1 first set
	 * @param list2 second set
	 * @return true if given sets have at least a common element
	 */
	public static <T> boolean containsAny(final Set<T> list1, final Set<T> list2) {
		final Set<T> set1 = new HashSet<>(list1);
		return list2.stream().anyMatch(set1::contains);
	}
}
