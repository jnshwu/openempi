/**
 *
 * Copyright (C) 2002-2012 "SYSNET International, Inc."
 * support@sysnetint.com [http://www.sysnetint.com]
 *
 * This file is part of OpenEMPI.
 *
 * OpenEMPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openhie.openempi.dao;

import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Address;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.Role;
import org.openhie.openempi.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

public class UserDaoTest extends BaseDaoTestCase {
    private UserDao dao = null;
    private RoleDao rdao = null;
    private Long userId;
    
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
        User user = new User("userTest");
        user.setPassword("password");
        user.setEnabled(true);
        user.setFirstName("Test");
        user.setLastName("Admin");
        Address address = new Address();
        address.setCity("Denver");
        address.setProvince("CO");
        address.setCountry("USA");
        address.setPostalCode("80210");
        user.setAddress(address);
        user.setEmail("testuser@appfuse.org");
        user.setWebsite("http://raibledesigns.com");
		user.setEnabled(true);
        user.setAccountExpired(false);
        user.setAccountLocked(false);
        user.setCredentialsExpired(false);
        
        Role role = rdao.getRoleByName(Constants.USER_ROLE);
        assertNotNull(role.getId());
        user.addRole(role);

        user = dao.saveUser(user);
        flush();
        userId = user.getId(); 
        
        assertNotNull(user.getId());
        user = dao.get(user.getId());
        assertEquals("password", user.getPassword());
	}
	
    public void setUserDao(UserDao dao) {
        this.dao = dao;
    }
    
    public void setRoleDao(RoleDao rdao) {
        this.rdao = rdao;
    }

    public void testGetUserInvalid() throws Exception {
        try {
            dao.get(1000L);
            fail("'badusername' found in database, failing test...");
        } catch (DataAccessException d) {
            assertTrue(d != null);
        }
    }
    
    public void testGetUser() throws Exception {
        User user = dao.get(userId);

        assertNotNull(user);
        assertEquals(1, user.getRoles().size());
        assertTrue(user.isEnabled());
    }

    public void testGetUserPassword() throws Exception {
        User user = dao.get(userId);
        assertEquals("password", user.getPassword());
    }

    public void testUpdateUser() throws Exception {
        User user = dao.get(userId);

        Address address = user.getAddress();
        address.setAddress("new address");

        dao.saveUser(user);
        flush();

        user = dao.get(userId);
        assertEquals(address, user.getAddress());
        assertEquals("new address", user.getAddress().getAddress());
    }

    public void testAddUserRole() throws Exception {
        User user = dao.get(userId);
        assertEquals(1, user.getRoles().size());

        Role role = rdao.getRoleByName(Constants.ADMIN_ROLE);
        user.addRole(role);
        user = dao.saveUser(user);
        flush();

        user = dao.get(userId);
        assertEquals(2, user.getRoles().size());

        //add the same role twice - should result in no additional role
        user.addRole(role);
        dao.saveUser(user);
        flush();

        user = dao.get(userId);
        assertEquals("more than 2 roles", 2, user.getRoles().size());

        user.getRoles().remove(role);
        dao.saveUser(user);
        flush();

        user = dao.get(userId);
        assertEquals(1, user.getRoles().size());
    }

    public void testUserExists() throws Exception {
        boolean b = dao.exists(userId);
        assertTrue(b);
    }
    
    public void testRemoveUser() throws Exception {
    	User user = dao.get(userId);
        assertEquals("password", user.getPassword());

        dao.remove(user.getId());
        flush();
        
        try {
            dao.get(user.getId());
            fail("getUser didn't throw DataAccessException");
        } catch (DataAccessException d) {
            assertNotNull(d);
        }    
    }
/*    
    public void testUserNotExists() throws Exception {
        boolean b = dao.exists(userId);
        assertFalse(b);
    }
*/
}
