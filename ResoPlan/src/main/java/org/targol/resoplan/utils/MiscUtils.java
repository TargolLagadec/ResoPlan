package org.targol.resoplan.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MiscUtils {

	public static <T> Set<T> getCommonElements(Set<T> list1, Set<T> list2) {
		if (list1 == null || list2 == null) {
			return Set.of();
		}
		Set<T> set1 = new HashSet<>(list1);
		return list2.stream().filter(set1::contains).distinct().collect(Collectors.toSet());
	}

	public static <T> List<T> getCommonElements(List<T> list1, List<T> list2) {
		if (list1 == null || list2 == null) {
			return List.of();
		}
		Set<T> set1 = new HashSet<>(list1);
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
