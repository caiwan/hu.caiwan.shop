package hu.caiwan.shop.service.mapper;

import org.springframework.stereotype.Component;

import hu.caiwan.shop.persist.model.Address;
import hu.caiwan.shop.service.dto.AddressDto;

@Component
public class AddressMapper extends CustomMapper<AddressDto, Address> {

	public AddressMapper() {
		super(AddressDto.class, Address.class);
	}

	@Override
	public AddressDto toDto(Address e) {
		AddressDto d1 = new AddressDto();
		defaultEntity2Dto(e, d1);

		return d1;
	}

	@Override
	public Address fromDto(AddressDto d) {
		Address e = new Address();
		defaultDto2Entity(d, e);
		return e;
	}

}
