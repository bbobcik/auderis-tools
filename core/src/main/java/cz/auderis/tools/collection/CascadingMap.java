package cz.auderis.tools.collection;

import java.util.Map;

/**
 * The interface Cascading map.
 * @param <K>  the type parameter
 * @param <V>  the type parameter
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public interface CascadingMap<K, V> extends Map<K, V> {

	/**
	 * Gets parent map.
	 *
	 * @return the parent map
	 */
	Map<K, ? extends V> getParentMap();

	/**
	 * Sets parent map.
	 *
	 * @param parentMap the parent map
	 */
	void setParentMap(Map<? extends K, ? extends V> parentMap);

	/**
	 * Has parent map.
	 *
	 * @return the boolean
	 */
	boolean hasParentMap();

	/**
	 * Clear current map.
	 */
	void clearCurrentMap();

	/**
	 * Clear parent map.
	 */
	void clearParentMap();

	/**
	 * Remove from current map.
	 *
	 * @param key the key
	 * @return the v
	 */
	V removeFromCurrentMap(Object key);

}
