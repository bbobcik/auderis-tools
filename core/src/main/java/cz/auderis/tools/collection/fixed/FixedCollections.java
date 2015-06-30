package cz.auderis.tools.collection.fixed;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class FixedCollections {

	public static <T> Collection<T> blackHoleCollection() {
		return new BlackHoleCollection<T>();
	}

	public static <T> Set<T> blackHoleSet() {
		return new BlackHoleSet<T>();
	}

	public static <T> List<T> blackHoleList() {
		return new BlackHoleList<T>();
	}

	public static <K,V> Map<K,V> blackHoleMap() {
		return new BlackHoleMap<K, V>();
	}

	public static <T> Collection<T> singleItemCollection() {
		return new SingleItemCollection<T>();
	}

	public static <T> Collection<T> singleItemCollection(T item) {
		return new SingleItemCollection<T>(item);
	}

	public static <T> Set<T> singleItemSet() {
		return new SingleItemSet<T>();
	}

	public static <T> Set<T> singleItemSet(T item) {
		return new SingleItemSet<T>(item);
	}

	public static <T> List<T> singleItemList() {
		return new SingleItemList<T>();
	}

	public static <T> List<T> singleItemList(T item) {
		return new SingleItemList<T>(item);
	}

	public static <K,V> Map<K,V> singleItemMap() {
		return new SingleItemMap<K, V>();
	}

	public static <K,V> Map<K,V> singleItemMap(K key, V value) {
		return new SingleItemMap<K, V>(key, value);
	}

	private FixedCollections() {
		throw new AssertionError();
	}

}
