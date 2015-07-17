/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.webbudget.domain.service;

import br.com.webbudget.domain.security.Grant;
import br.com.webbudget.domain.security.Group;
import br.com.webbudget.domain.security.GroupMembership;
import br.com.webbudget.domain.security.Role;
import br.com.webbudget.domain.security.User;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.picketlink.idm.IdentityManagementException;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.picketlink.idm.query.RelationshipQuery;

/**
 * Servico responsavel por todo o gerenciamento do modelo de seguranca
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 06/10/2013
 */
@ApplicationScoped
public class AccountService {

    @Inject
    private IdentityManager identityManager;
    @Inject
    private RelationshipManager relationshipManager;

    /**
     * 
     * @param user 
     */
    @Transactional
    public void save(User user) {

        // pegamos o grupo e setamos o user no membership dele
        final GroupMembership groupMembership = user.getGroupMembership();
        
        // pegamos a senha antes de salvar o usuario
        final String unsecurePassword = user.getPassword();
        
        // salvamos
        this.identityManager.add(user);

        // atualizamos o usuario com a senha
        this.identityManager.updateCredential(user, new Password(unsecurePassword));
        
        // concedemos ao usuario o grant para o grupo que ele escolheu
        this.relationshipManager.add(groupMembership);
    }
    
    /**
     * 
     * @param user 
     */
    @Transactional
    public void update(User user) {

        // pegamos o grupo
        final GroupMembership groupMembership = user.getGroupMembership();
        
        // pegamos a senha antes de salvar o usuario
        final String unsecurePassword = user.getPassword();
        
        // salvamos
        this.identityManager.update(user);
        
        // atualizamos o usuario com a senha
        if (unsecurePassword != null && !unsecurePassword.isEmpty()) {
            this.identityManager.updateCredential(user, new Password(unsecurePassword));
        }
        
        // concedemos ao usuario o grant para o grupo que ele escolheu
        this.relationshipManager.update(groupMembership);
    }
    
    /**
     * 
     * @param user 
     */
    @Transactional
    public void delete(User user) {
        
        // removemos os relacioanamentos
        for (GroupMembership membership : this.listMembershipsByUser(user)) {
            this.relationshipManager.remove(membership);
        }
        
        // removemos o usuario do contexto de seguranca
        this.identityManager.remove(user);
    }

    /**
     *
     * @param username
     * @return
     */
    public User findUserByUsername(String username) {

        final IdentityQueryBuilder queryBuilder = this.identityManager.getQueryBuilder();

        final List<User> users = queryBuilder.createIdentityQuery(User.class)
                .where(queryBuilder.equal(User.USER_NAME, username)).getResultList();

        if (users.isEmpty()) {
            return null;
        } else if (users.size() == 1) {
            return users.get(0);
        } else {
            throw new IdentityManagementException("account.error.duplicated-usernames");
        }
    }

    /**
     *
     * @param userId
     * @return
     */
    public User findUserById(String userId) {

        final IdentityQueryBuilder queryBuilder = this.identityManager.getQueryBuilder();

        final List<User> users = queryBuilder.createIdentityQuery(User.class)
                .where(queryBuilder.equal(User.ID, userId)).getResultList();

        if (users.isEmpty()) {
            return null;
        } else if (users.size() == 1) {
            final User user = users.get(0);
            user.setGroupMembership(this.listMembershipsByUser(user).get(0));
            return user;            
        } else {
            throw new IdentityManagementException("account.error.duplicated-usernames");
        }
    }

    /**
     *
     * @param authorization
     * @return
     */
    public Role findRoleByName(String authorization) {

        final IdentityQueryBuilder queryBuilder = this.identityManager.getQueryBuilder();

        final List<Role> roles = queryBuilder.createIdentityQuery(Role.class)
                .where(queryBuilder.equal(Role.AUTHORIZATION, authorization)).getResultList();

        if (roles.isEmpty()) {
            return null;
        } else if (roles.size() == 1) {
            return roles.get(0);
        } else {
            throw new IdentityManagementException("account.error.duplicated-roles");
        }
    }

    /**
     *
     * @param groupName
     * @return
     */
    public Group findGroupByName(String groupName) {

        final IdentityQueryBuilder queryBuilder = this.identityManager.getQueryBuilder();

        final List<Group> groups = queryBuilder.createIdentityQuery(Group.class)
                .where(queryBuilder.equal(Group.NAME, groupName)).getResultList();

        if (groups.isEmpty()) {
            return null;
        } else if (groups.size() == 1) {
            return groups.get(0);
        } else {
            throw new IdentityManagementException("account.error.duplicated-groups");
        }
    }
    
    /**
     * 
     * @param user
     * @return 
     */
    public List<GroupMembership> listMembershipsByUser(User user) {

        final RelationshipQuery<GroupMembership> query
                = this.relationshipManager.createRelationshipQuery(GroupMembership.class);

        query.setParameter(GroupMembership.MEMBER, user);
        
        return query.getResultList();
    }

    /**
     *
     * @param user
     * @return
     */
    public List<Group> listUserGroups(User user) {

        final RelationshipQuery<GroupMembership> query
                = this.relationshipManager.createRelationshipQuery(GroupMembership.class);

        query.setParameter(GroupMembership.MEMBER, user);

        final List<Group> groups = new ArrayList<>();

        query.getResultList().stream().forEach((membership) -> {
            groups.add(membership.getGroup());
        });

        return groups;
    }

    /**
     *
     * @param enabled
     * @return
     */
    public List<User> listUsers(Boolean enabled) {

        final IdentityQueryBuilder queryBuilder = this.identityManager.getQueryBuilder();

        final IdentityQuery<User> query = queryBuilder.createIdentityQuery(User.class);

        if (enabled != null) {
            query.where(queryBuilder.equal(User.ENABLED, enabled));
        }

        return query.getResultList();
    }
    
    /**
     * 
     * @param enabled
     * @return 
     */
    public List<Group> listGroups(Boolean enabled) {

        final IdentityQueryBuilder queryBuilder = this.identityManager.getQueryBuilder();

        final IdentityQuery<Group> query = queryBuilder.createIdentityQuery(Group.class);

        if (enabled != null) {
            query.where(queryBuilder.equal(User.ENABLED, enabled));
        }

        return query.getResultList();
    }

    /**
     *
     * @param user
     * @return
     */
    public List<Group> listUserGroupsAndGrants(User user) {

        final RelationshipQuery<GroupMembership> query
                = this.relationshipManager.createRelationshipQuery(GroupMembership.class);

        query.setParameter(GroupMembership.MEMBER, user);

        final List<Group> groups = new ArrayList<>();

        query.getResultList().stream().forEach((membership) -> {
            groups.add(membership.getGroup());
        });

        // preenchemos os grants do grupo
        groups.stream().forEach((group) -> {
            group.setGrants(this.listGrantsByGroup(group));
        });

        return groups;
    }

    /**
     *
     * @param group
     * @return
     */
    public List<Grant> listGrantsByGroup(Group group) {

        final RelationshipQuery<Grant> query = this.relationshipManager
                .createRelationshipQuery(Grant.class);

        query.setParameter(Grant.ASSIGNEE, group);

        return query.getResultList();
    }

    /**
     *
     * @param member
     * @param group
     * @return
     */
    public boolean isMember(User member, Group group) {

        final RelationshipQuery<GroupMembership> query
                = this.relationshipManager.createRelationshipQuery(GroupMembership.class);

        query.setParameter(GroupMembership.MEMBER, member);

        final List<GroupMembership> memberships = query.getResultList();

        if (memberships.stream().anyMatch((membership)
                -> (membership.getGroup().getId().equals(group.getId())))) {
            return true;
        }

        return false;
    }

    /**
     *
     * @param group
     * @param account
     */
    public void addToGroup(Group group, Account account) {
        this.relationshipManager.add(new GroupMembership(group, account));
    }

    /**
     *
     * @param group
     * @param account
     */
    public void removeFromGroup(Group group, Account account) {

        final RelationshipQuery<GroupMembership> query
                = this.relationshipManager.createRelationshipQuery(GroupMembership.class);

        query.setParameter(GroupMembership.GROUP, group);
        query.setParameter(GroupMembership.MEMBER, account);

        for (GroupMembership membership : query.getResultList()) {
            this.relationshipManager.remove(membership);
        }
    }

    /**
     *
     * @param user
     * @param role
     * @return
     */
    public boolean userHasRole(User user, Role role) {

        final List<Group> groups = this.listUserGroups(user);

        boolean hasRole = false;

        for (Group group : groups) {
            if (this.groupHasRole(group, role)) {
                hasRole = true;
                break;
            }
        }

        return hasRole;
    }

    /**
     *
     * @param group
     * @param role
     * @return
     */
    public boolean groupHasRole(Group group, Role role) {

        final RelationshipQuery<Grant> query = this.relationshipManager.createRelationshipQuery(Grant.class);

        query.setParameter(Grant.ASSIGNEE, group);
        query.setParameter(Grant.ROLE, role);

        if (query.getResultList().stream().anyMatch((grant)
                -> (grant.getAssignee().getId().equals(group.getId())))) {
            return true;
        }

        return false;
    }

    /**
     *
     * @param role
     * @param group
     */
    public void grantToGroup(Role role, Group group) {
        this.relationshipManager.add(new Grant(role, group));
    }

    /**
     *
     * @param role
     * @param group
     */
    public void revokeGroupGrant(Role role, Group group) {

        final RelationshipQuery<Grant> query
                = this.relationshipManager.createRelationshipQuery(Grant.class);

        query.setParameter(Grant.ASSIGNEE, group);
        query.setParameter(Grant.ROLE, role);

        for (Grant grant : query.getResultList()) {
            this.relationshipManager.remove(grant);
        }
    }
}
