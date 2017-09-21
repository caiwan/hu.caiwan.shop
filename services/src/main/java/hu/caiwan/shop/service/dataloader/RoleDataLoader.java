package hu.caiwan.shop.service.dataloader;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import hu.caiwan.shop.persist.dao.PrivilegeRepository;
import hu.caiwan.shop.persist.dao.RoleRepository;
import hu.caiwan.shop.persist.model.Privilege;
import hu.caiwan.shop.persist.model.Role;
import hu.caiwan.shop.persist.type.PrivilegeType;
import hu.caiwan.shop.persist.type.RoleType;

@Component
public class RoleDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private boolean alreadySetup = false;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (alreadySetup)
			return;

		for (RoleType roleElem : RoleType.values()) {
			List<Privilege> privileges = new LinkedList<Privilege>();
			for (PrivilegeType privilegeElem : roleElem.getPrivileges()) {
				privileges.add(createPrivilegeIfNotFound(privilegeElem.name()));
			}
			/* final Role role = */ createRoleIfNotFound(roleElem.name(), privileges);
		}

		alreadySetup = true;

	}

	@Transactional
	private final Privilege createPrivilegeIfNotFound(final String name) {
		Privilege privilege = privilegeRepository.findByName(name);
		if (privilege == null) {
			privilege = new Privilege(name);
			privilegeRepository.save(privilege);
		}
		return privilege;
	}

	@Transactional
	private final Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(name);
			role.setPrivileges(privileges);
			roleRepository.save(role);
		}
		return role;
	}

}
