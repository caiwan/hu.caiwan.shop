package hu.caiwan.shop.persist.dao.custom;

/**
 * Add some more unsupported functions to JPA repositories such as detach and merge. 
 * Has to be tied to JPA repository due to have the right persistence context.
 * @author caiwan
 *
 */

public interface CustomEntityManager <E> {
	public void merge(E e);
	public void detach(E e);
}
