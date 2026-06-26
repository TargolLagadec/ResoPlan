package org.targol.resoplan.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MiscUtils {

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
}
