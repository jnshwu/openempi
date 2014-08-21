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
package org.openhie.openempi.service;

import java.util.List;
import java.util.Set;

import org.springframework.security.userdetails.UsernameNotFoundException;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.AuthenticationException;
import org.openhie.openempi.dao.UserDao;
import org.openhie.openempi.model.Permission;
import org.openhie.openempi.model.Role;
import org.openhie.openempi.model.User;
import org.openhie.openempi.model.UserFile;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *  Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a> 
 */
public interface UserManager extends UniversalManager {

    /**
     * Convenience method for testing - allows you to mock the DAO and set it on an interface.
     * @param userDao the UserDao implementation to use
     */
    void setUserDao(UserDao userDao);

    /**
     * Retrieves a user by userId.  An exception is thrown if user not found
     *
     * @param userId the identifier for the user
     * @return User
     */
    User getUser(String userId);

    /**
     * Authentication method. Currently the only authentication method available is using
     * username/password pair credentials. In the future we need to add support for certificate
     * based authentication.
     */
    public User authenticate(String username, String password) throws AuthenticationException;
    
    /**
     * Authentication method using a session Key. This authentication method is used
     * by the EJB layer for the purpose of being able to maintain a conversation with a service
     * after successful authentication using another method.
     * 
     * @param sessionKey Obtained after successful authentication and used by EJB layer methods 
     * to have a "stateful-like" interaction regarding authentication with the stateless services
     * 
     * @return If the session key is recognized and the authentication attempt is successful, the
     * User object of the authenticated user is returned.
     * 
     * @throws AuthenticationException
     */
    public User authenticate(String sessionKey) throws AuthenticationException;
    
    /**
     * Creates a new session usually as a result of a successful authentication event. End users
     * can utilize the sessionId for subsequent requests.
     */
    String createSession(User user);
    
    /**
    /**
     * Finds a user by their username.
     * @param username the user's username used to login
     * @return User a populated user object
     * @throws org.springframework.security.userdetails.UsernameNotFoundException
     *         exception thrown when user not found
     */
    User getUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * Retrieves a list of users, filtering with parameters on a user object
     * @param user parameters to filter on
     * @return List
     */
    List<User> getUsers(User user);

    /**
     * Saves a user's information.
     *
     * @param user the user's information
     * @throws UserExistsException thrown when user already exists
     * @return user the updated user object
     */
    User saveUser(User user) throws UserExistsException;

    /**
     * Removes a user from the database by their userId
     *
     * @param userId the user's id
     */
    void removeUser(String userId);
    
    /**
     * Retrieves the list of files that are associated with a user.
     * @param user parameter to filter on
     * @return List
     */
    public List<UserFile> getUserFiles(User user);
    
    /**
     * Retrieves a specific file entry using the entry's primary key
     * @param userFileId identifier for user file entry
     * @return UserFile
     */
    public UserFile getUserFile(Integer userFileId);
    
    /**
     * Save a user file entry
     * 
     * @param userFile a populated user file entry
     * @return userFile the stored user file object with identifying information
     */
    public UserFile saveUserFile(UserFile userFile);
    
    /**
     * Removes a user entry from the database by its id
     *
     * @param userFile file entry
     */
    void removeUserFile(UserFile userFile);

    /**
     * Returns the list of roles that have been defined in the system.
     * 
     */
    public List<Role> getRoles();
    
    /**
     * Returns the list of permissions that are assigned to a user through
     * the set of roles associated with the user.
     * 
     * @param
     * @return The set of permissions that the user has.
     */
    public Set<Permission> getUserPermissions(User user);
    
    /**
     * Returns the role identified by the id provided by the caller.
     * 
     * @param roleId
     */
    public Role getRole(Long roleId);
    
    public Role getRoleByName(String roleName);
    
    public Role saveRole(Role role) throws ApplicationException;
    
    public void removeRole(String roleName) throws ApplicationException;
    
    /**
     * Returns the list of all known and supported permissions on the system.
     */
    public List<Permission> getPermissions();
}
