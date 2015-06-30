package cz.auderis.tools.collection.fixed;

import java.util.Collection;
import java.util.Set;

public class BlackHoleSet<E> extends BlackHoleCollection<E> implements Set<E> {
	private static final long serialVersionUID = 123L;

	public BlackHoleSet() {
		super();
	}

	@Override
	protected Class<? extends Collection> getRequiredClass() {
		return Set.class;
	}

}
