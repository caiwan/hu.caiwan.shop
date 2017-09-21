package hu.caiwan.shop.controller;

import java.io.Serializable;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hu.caiwan.shop.service.AbstractCrudService;

public abstract class AbstractCrudController<D, PK extends Serializable> {

	protected abstract AbstractCrudService<D, PK> getService();

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	Collection<D> getAll() {
		return getService().getAll();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	void add(@RequestBody @Valid D dto) {
		getService().add(dto);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseBody
	D get(@PathVariable PK id) {
		return getService().getByID(id);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	@ResponseBody
	void update(@PathVariable PK id, @RequestBody @Valid D dto) {
		getService().update(id, dto);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ResponseBody
	void delete(@PathVariable PK id) {
		getService().delete(id);
	}
}
