package hu.caiwan.shop.service;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.LinkedList;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.caiwan.shop.service.mapper.AbstractMapper;

// @formatter:off
public abstract class GenericCrudService<D, E, PK extends Serializable>
		implements AbstractCrudService<D, PK> {
	// @formatter:on

	protected abstract JpaRepository<E, PK> getRepository();
	protected abstract AbstractMapper<D, E> getMapper();

	@Override
	public Collection<D> getAll() {
		Collection<E> all = getRepository().findAll();
		Collection<D> res = new LinkedList<>();
		for (E e : all) {
			res.add(getMapper().toDto(e));
		}
		return res;
	}

	@Override
	public D getByID(PK pk) {
		if (!getRepository().exists(pk)) {
			// TODO: Define own exception
			throw new RuntimeException(MessageFormat.format("Element {0} does not exists wih key {1}", "@@@", pk));
		}
		E e = getRepository().findOne(pk);
		return getMapper().toDto(e);
	}

	@Override
	public void add(D dto) {
		E e = getMapper().fromDto(dto);
		if (e == null)
			throw new NullPointerException();
		getRepository().save(e);
	}

	@Override
	public void update(PK pk, D dto) {
		if (!getRepository().exists(pk)) {
			// TODO: Define own exception
			throw new RuntimeException(MessageFormat.format("Element {0} does not exists wih key {1}", "@@@", pk));
		}
		E e = getMapper().fromDto(dto);
		getRepository().save(e);
	}

	@Override
	public void delete(PK pk) {
		getRepository().delete(pk);
	}

}
