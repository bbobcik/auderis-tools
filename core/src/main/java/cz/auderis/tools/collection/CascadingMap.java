package cz.auderis.tools.collection;

import java.util.Map;

public interface CascadingMap<K, V> extends Map<K, V> {

	Map<K, ? extends V> getParentMap();
	void setParentMap(Map<? extends K, ? extends V> parentMap);
	boolean hasParentMap();

	void clearCurrentMap();
	void clearParentMap();

	V removeFromCurrentMap(Object key);

}
