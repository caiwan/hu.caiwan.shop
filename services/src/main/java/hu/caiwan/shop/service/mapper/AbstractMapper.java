package hu.caiwan.shop.service.mapper;

public interface AbstractMapper <D, E>{
	public D toDto(final E e) throws RuntimeException;
	public E fromDto(final D d) throws RuntimeException;
}
