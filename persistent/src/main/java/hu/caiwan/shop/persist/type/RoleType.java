package hu.caiwan.shop.persist.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RoleType {
	// @formatter:off
	ROLE_USER(PrivilegeType.PRIVILEGE_READ), 
	ROLE_ADMIN(PrivilegeType.PRIVILEGE_READ, PrivilegeType.PRIVILEGE_WRITE),
	ROLE_TEST(PrivilegeType.PRIVILEGE_READ, PrivilegeType.PRIVILEGE_TEST);
	// @formatter:on
	
	private List<PrivilegeType> privileges;

	private RoleType(PrivilegeType... privileges) {
		this.privileges = new ArrayList<PrivilegeType>(Arrays.asList(privileges));
	}

	public List<PrivilegeType> getPrivileges() {
		return privileges;
	}

}
