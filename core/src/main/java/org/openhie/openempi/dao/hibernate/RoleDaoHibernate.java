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
package org.openhie.openempi.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.dao.RoleDao;
import org.openhie.openempi.model.Permission;
import org.openhie.openempi.model.Role;
import org.openhie.openempi.model.User;
import org.springframework.orm.hibernate3.HibernateCallback;


/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve Role objects.
 *
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a> 
 */
public class RoleDaoHibernate extends GenericDaoHibernate<Role, Long> implements RoleDao {

    /**
     * Constructor to create a Generics-based version using Role as the entity
     */
    public RoleDaoHibernate() {
        super(Role.class);
    }

    /**
     * {@inheritDoc}
     */
    public Role getRole(final Long roleId) {
    	return (Role) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Role role = (Role) session.load(Role.class, roleId);
		        if (role == null) {
		        	return role;
		        }
		        Hibernate.initialize(role.getPermissions());
		        return role;
			}    		
    	});
    }
    
    /**
     * {@inheritDoc}
     */
    public Role saveRole(Role role) {
        log.debug("Saving the role: " + role);
        getHibernateTemplate().saveOrUpdate(role);
        getHibernateTemplate().flush();
        return role;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Role> getRoles() {
        return getHibernateTemplate().find("from Role r order by upper(r.name)");
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Permission> getPermissions() {
        return getHibernateTemplate().find("from Permission p order by p.name");
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public Role getRoleByName(final String roleName) {
    	return (Role) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List<Role> roles = (List<Role>) session.createQuery("from Role r where r.name = :roleName").setString("roleName", roleName).list();
		        if (roles == null || roles.size() == 0) {
		        	return null;
		        }
		        Role role = roles.get(0);
		        Hibernate.initialize(role.getPermissions());
		        return role;
			}    		
    	});
    }

    /**
     * {@inheritDoc}
     * @throws ApplicationException 
     */
    public void removeRole(final String rolename) throws ApplicationException {
        Object role = getRoleByName(rolename);
        final String nameOfRole = rolename;
        if (role != null) {
        	@SuppressWarnings("unchecked")
			List<User> users = (List<User>) getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					return session.createCriteria(User.class)
						.createAlias("roles", "role")
						.add(Restrictions.eq("role.name", nameOfRole))
						.list();
				}
        	});
        	if (users != null && users.size() > 0) {
        		throw new ApplicationException("Cannot delete role " + rolename + " because it is currently in use by one or more users.");
        	}
            getHibernateTemplate().delete(role);
            log.debug("Removed role " + role);
        }
    }
}
