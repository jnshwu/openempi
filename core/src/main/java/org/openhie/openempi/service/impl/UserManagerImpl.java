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
package org.openhie.openempi.service.impl;

import java.io.File;
import java.util.List;
import java.util.Set;

//import javax.jws.WebService;
import javax.persistence.EntityExistsException;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.AuthenticationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.dao.RoleDao;
import org.openhie.openempi.dao.UserDao;
import org.openhie.openempi.dao.UserSessionDao;
import org.openhie.openempi.model.Permission;
import org.openhie.openempi.model.Role;
import org.openhie.openempi.model.User;
import org.openhie.openempi.model.UserFile;
import org.openhie.openempi.model.UserSession;
import org.openhie.openempi.service.UserExistsException;
import org.openhie.openempi.service.UserManager;
import org.openhie.openempi.service.UserService;
import org.openhie.openempi.util.SessionGenerator;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.userdetails.UsernameNotFoundException;


/**
 * Implementation of UserManager interface.
 *
 * @author Odysseas Pentakalos
 */
//@WebService(serviceName = "UserService", endpointInterface = "org.openhie.openempi.service.UserService")
public class UserManagerImpl extends UniversalManagerImpl implements UserManager, UserService
{
    private UserDao userDao;
    private RoleDao roleDao;
    private UserSessionDao userSessionDao;
    private PasswordEncoder passwordEncoder;

    /**
     * Set the PasswordEncoder used to encrypt passwords.
     * @param passwordEncoder the PasswordEncoder implementation
     */
    @Required
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     */
    public User getUser(String userId) {
        return userDao.getUser(new Long(userId));
    }

    /**
     * {@inheritDoc}
     */
    public List<User> getUsers(User user) {
        return userDao.getUsers();
    }
    
    public String createSession(User user) {
    	String sessionKey = SessionGenerator.generateSessionId();
    	UserSession userSession = new UserSession(sessionKey, user, new java.util.Date());
    	userSessionDao.saveUserSession(userSession);
    	return sessionKey;
    }
    
    public User authenticate(String username, String password) throws AuthenticationException {
		log.debug("Authentication request for user with username: " + username);
		if (username == null || password == null) {
			log.warn("Authentication attempt failed due to missing credentials: username=" + username + "; password=" + password);
			throw new AuthenticationException("Authentication failed; no credentials were provided in the request.");
		}
				
//    	User user = (User) getUserByUsername(username);
//		if (user == null) {
//			log.warn("Authentication attempt with unknown username " + username);
//			throw new AuthenticationException("Authentication failed.");
//		}

		User user = null;
        try {
        	user = (User) getUserByUsername(username);
        } catch (UsernameNotFoundException e) {
        	log.warn("Authentication attempt with unknown username " + username);
			throw new AuthenticationException("Authentication failed.");     	
        }
        
        if ( user.isAccountLocked() ) {
			throw new AuthenticationException("Account locked.");   
        }

        if ( user.isCredentialsExpired() ) {
			throw new AuthenticationException("Credentials Expired.");   
        }
        
		String encrypted = passwordEncoder.encodePassword(password, null);
		log.debug("Encrypted password is " + encrypted + " as opposed to " + user.getPassword());
		if (!encrypted.equalsIgnoreCase(user.getPassword())) {
			log.warn("Authentication attempt failed due to incorrect password.");
			throw new AuthenticationException("Authentication failed.");
		}
		return user;
    }
    
    public User authenticate(String sessionKey) throws AuthenticationException {
    	log.debug("Authentication request for user with session id: " + sessionKey);
    	UserSession userSession = userSessionDao.findBySessionKey(sessionKey);
    	if (userSession == null) {
    		log.warn("Authentication attempt failed due to invalid session key: " + sessionKey);
    		throw new AuthenticationException("Invalid session key");
    	}
    	log.debug("Authentication attempt succeeded for user: " + userSession.getUser().getUsername() + " and session key " + sessionKey);
    	return userSession.getUser();
    }
    
    /** 
     * TODO Need to add support for a logout operation that removes the session from the system
     */
    public void logout(UserSession userSession) {
    	
    }
    
    public Set<Permission> getUserPermissions(User user) {
    	Set<Permission> permissions = new java.util.HashSet<Permission>();
    	if (user == null || user.getId() == null) {
    		return permissions;
    	}
    	user = getUser(user.getId().toString());
    	for (Role role : user.getRoles()) {
    		permissions.addAll(role.getPermissions());
    	}
    	return permissions;
    }
    
    /**
     * {@inheritDoc}
     */
    public User saveUser(User user) throws UserExistsException {

        if (user.getVersion() == null) {
            // if new user, lowercase userId
            user.setUsername(user.getUsername().toLowerCase());
        }
        
        // Get and prepare password management-related artifacts
        boolean passwordChanged = false;
        if (passwordEncoder != null) {
            // Check whether we have to encrypt (or re-encrypt) the password
            if (user.getVersion() == null) {
                // New user, always encrypt
                passwordChanged = true;
            } else {
                // Existing user, check password in DB
                String currentPassword = userDao.getUserPassword(user.getUsername());
                if (currentPassword == null) {
                    passwordChanged = true;
                } else {
                    if (!currentPassword.equals(user.getPassword())) {
                        passwordChanged = true;
                    }
                }
            }

            // If password was changed (or new user), encrypt it
            if (passwordChanged) {
                user.setPassword(passwordEncoder.encodePassword(user.getPassword(), null));
            }
        } else {
            log.warn("PasswordEncoder not set, skipping password encryption...");
        }
        
        try {
            return userDao.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        } catch (EntityExistsException e) { // needed for JPA
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeUser(String userId) {
        log.debug("removing user: " + userId);
        userDao.remove(new Long(userId));
    }

    /**
     * {@inheritDoc}
     * @param username the login name of the human
     * @return User the populated user object
     * @throws UsernameNotFoundException thrown when username not found
     */
    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return (User) userDao.loadUserByUsername(username);
    }

    public UserFile getUserFile(Integer userFileId) {
    	log.debug("Loading user file entry with identifier: " + userFileId);
    	if (userFileId == null) {
    		return null;
    	}
    	return userDao.getUserFile(userFileId);
    }
    
	public List<UserFile> getUserFiles(User user) {
		log.debug("Loading user file entries for user: " + user);
		return userDao.getUserFiles(user);
	}

	public void removeUserFile(UserFile userFile) {
		log.debug("Deleting a user file entry: " + userFile);
		if (userFile.getUserFileId() == null) {
			return;
		}
		UserFile userFileEntry = userDao.getUserFile(userFile.getUserFileId());
		if (userFileEntry == null) {
			return;
		}
		File file = new File(userFileEntry.getFilename());
		boolean deleteOutcome = file.delete();
		log.debug("Deleting physical upload file stored at: " + file.getAbsolutePath() + " returned " + deleteOutcome);
		userDao.removeUserFile(userFile);
	}

	public UserFile saveUserFile(UserFile userFile) {
		userFile.setDateCreated( new java.util.Date());
		userFile.setOwner(Context.getUserContext().getUser());
		return userDao.saveUserFile(userFile);
	}
	
    /**
     * {@inheritDoc}
     */
    public List<Role> getRoles() {
        return roleDao.getRoles();
    }	

    /**
     * {@inheritDoc}
     */
    public Role getRole(Long roleId) {
    	return roleDao.getRole(roleId);
    }
    
    /**
     * {@inheritDoc}
     */
    public Role getRoleByName(String roleName) {
    	return roleDao.getRoleByName(roleName);
    }
    
    public Role saveRole(Role role) throws ApplicationException {
        try {
            return roleDao.saveRole(role);
        } catch (DataIntegrityViolationException e) {
            log.warn(e.getMessage(), e);
            throw new ApplicationException("Role '" + role.getName() + "' already exists!");
        } catch (EntityExistsException e) { // needed for JPA
            log.warn(e.getMessage(), e);
            throw new ApplicationException("Role '" + role.getName() + "' already exists!");
        }
    }
    
    public void removeRole(String roleName) throws ApplicationException {
        log.debug("removing role: " + roleName);
        roleDao.removeRole(roleName);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Permission> getPermissions() {
    	return roleDao.getPermissions();
    }
    
	public UserSessionDao getUserSessionDao() {
		return userSessionDao;
	}

	public void setUserSessionDao(UserSessionDao userSessionDao) {
		this.userSessionDao = userSessionDao;
	}
	
    /**
     * Set the Dao for communication with the data layer.
     * @param dao the UserDao that communicates with the database
     */
    @Required
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    
    @Required
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }
}
