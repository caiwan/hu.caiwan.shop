package hu.caiwan.shop.service;

import java.util.Collection;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import hu.caiwan.shop.persist.dao.AddressRepository;
import hu.caiwan.shop.persist.model.Address;
import hu.caiwan.shop.persist.model.User;
import hu.caiwan.shop.service.dto.AddressDto;
import hu.caiwan.shop.service.mapper.AbstractMapper;
import hu.caiwan.shop.service.mapper.AddressMapper;

@Service
public class AddressService extends GenericCrudService<AddressDto, Address, Long> {

	@Autowired
	private UserService userService;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private AddressMapper addressMapper;

	@Override
	protected JpaRepository<Address, Long> getRepository() {
		return addressRepository;
	}

	@Override
	protected AbstractMapper<AddressDto, Address> getMapper() {
		return addressMapper;
	}

	// Cutom crud

	@Override
	public Collection<AddressDto> getAll() {
		User user = userService.getCurrentUser();
		Collection<Address> addrs = addressRepository.findByOwner(user);
		Collection<AddressDto> addrDto = new LinkedList<>();
		for (Address addr : addrs) {
			addrDto.add(addressMapper.toDto(addr));
		}
		return addrDto;
	}

	@Override
	public AddressDto getByID(Long pk) {
		if (!isOwner(pk)){
			throw new RuntimeException("Permission denied");
		}
		return super.getByID(pk);
	}

	@Override
	public void add(AddressDto dto) {
		Address address = addressMapper.fromDto(dto);
		address.setOwner(userService.getCurrentUser());
		addressRepository.save(address);
	}

	@Override
	public void update(Long pk, AddressDto dto) {
		if (!isOwner(pk)){
			throw new RuntimeException("Permission denied");
		}
		super.update(pk, dto);
	}

	@Override
	public void delete(Long pk) {

		// TODO Auto-generated method stub
		super.delete(pk);
	}
	
	// ... 

	private boolean isOwner(Long pk) {
		User user = userService.getCurrentUser();
		Address address = addressRepository.findOne(pk);
		if (address.getOwner() != user)
			return false;
		return true;
	}

}
