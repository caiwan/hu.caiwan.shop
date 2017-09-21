package hu.caiwan.shop.service;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.stereotype.Service;

@Service
public interface AbstractCrudService <DTO, PK extends Serializable>{
	public Collection<DTO> getAll();
	public DTO getByID(PK pk);
	public void add(DTO dto);
	public void update(PK pk, DTO dto);
	public void delete(PK pk);
}
