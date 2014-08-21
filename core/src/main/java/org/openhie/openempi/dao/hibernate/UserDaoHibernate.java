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

import javax.persistence.Table;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.openhie.openempi.dao.UserDao;
import org.openhie.openempi.model.Permission;
import org.openhie.openempi.model.Role;
import org.openhie.openempi.model.User;
import org.openhie.openempi.model.UserFile;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve User objects.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *   Modified by <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 *   Extended to implement Acegi UserDetailsService interface by David Carter david@carter.net
 *   Modified by <a href="mailto:bwnoll@gmail.com">Bryan Noll</a> to work with 
 *   the new BaseDaoHibernate implementation that uses generics.
*/
public class UserDaoHibernate extends GenericDaoHibernate<User, Long> implements UserDao, UserDetailsService {

    /**
     * Constructor that sets the entity to User.class.
     */
    public UserDaoHibernate() {
        super(User.class);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        return getHibernateTemplate().find("from User u order by upper(u.username)");
    }
        
    public User getUser(final Long userId) {
    	return (User) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				User user = (User) session.load(User.class, userId);
		    	if (user == null) {
		    		return user;
		    	}
		    	Hibernate.initialize(user.getRoles());
		    	return user;
			}
    	});
    }
    
    /**
     * {@inheritDoc}
     */
    public User saveUser(User user) {
        log.debug("user's id: " + user.getId());
        getHibernateTemplate().saveOrUpdate(user);
        // necessary to throw a DataIntegrityViolation and catch it in UserManager
        getHibernateTemplate().flush();
        return user;
    }

    /**
     * Overridden simply to call the saveUser method. This is happening 
     * because saveUser flushes the session and saveObject of BaseDaoHibernate 
     * does not.
     *
     * @param user the user to save
     * @return the modified user (with a primary key set if they're new)
     */
    @Override
    public User save(User user) {
        return this.saveUser(user);
    }

    /** 
     * {@inheritDoc}
    */
    @SuppressWarnings("unchecked")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetails> users = getHibernateTemplate().find("from User where username=?", username);
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("user '" + username + "' not found...");
        } else {
        	User user = (User) users.get(0);
        	Hibernate.initialize(user.getRoles());
        	UserDetails userDetails = (UserDetails) getUser(user.getId());
        	for (GrantedAuthority authority : userDetails.getAuthorities()) {
        		log.trace("User " + username + " has authority: " + authority.getAuthority());
        	}
            return userDetails; 
        }
    }

    /** 
     * {@inheritDoc}
    */
    public String getUserPassword(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(SessionFactoryUtils.getDataSource(getSessionFactory()));
        Table table = AnnotationUtils.findAnnotation(User.class, Table.class);
        return (String) jdbcTemplate.queryForObject("select password from " + table.name() + " where username=?",
        		new Object[] {username}, String.class);
    }

    @SuppressWarnings("unchecked")
	public List<UserFile> getUserFiles(User user) {
    	if (user == null || user.getId() == null) {
    		return new java.util.ArrayList<UserFile>();
    	}
		List<UserFile> userFiles = getHibernateTemplate().find("from UserFile userFile where userFile.owner.id = ? ", user.getId());
		return userFiles;
	}

    public UserFile getUserFile(Integer userFileId) {
    	if (userFileId == null) {
    		return null;
    	}
    	return (UserFile) getHibernateTemplate().get(UserFile.class, userFileId);
    }
    
	public UserFile saveUserFile(UserFile userFile) {
		getHibernateTemplate().merge(userFile);
        getHibernateTemplate().flush();
		return userFile;
	}
	
	public void removeUserFile(UserFile userFile) {
		if (userFile == null || userFile.getUserFileId() == null) {
			return;
		}
		UserFile theFile = (UserFile) getHibernateTemplate().get(UserFile.class, userFile.getUserFileId());
		getHibernateTemplate().delete(theFile);
		getHibernateTemplate().flush();
	}
}
