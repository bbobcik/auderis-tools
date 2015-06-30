package cz.auderis.tools.collection.fixed;

import java.util.Set;

public class SingleItemSet<E> extends SingleItemCollection<E> implements Set<E> {
	private static final long serialVersionUID = 234L;

	public SingleItemSet() {
		super();
	}

	public SingleItemSet(E obj) {
		super(obj);
	}

	@Override
	protected Class<?> getEqualClass() {
		return Set.class;
	}

}
