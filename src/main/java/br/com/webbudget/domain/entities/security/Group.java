package br.com.webbudget.domain.entities.security;

import br.com.webbudget.domain.entities.PersistentEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.metamodel.SingularAttribute;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 26/12/2017
 */
@Entity
@Table(name = "groups")
@ToString(exclude = {"parent", "grants"})
@EqualsAndHashCode(callSuper = true, exclude = {"parent"})
public class Group extends PersistentEntity {

    @Getter
    @Setter
    @NotNull(message = "{group.name}")
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Getter
    @Setter
    @Column(name = "blocked", nullable = false)
    private boolean blocked;
    
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_parent")
    private Group parent;
    
    @OneToMany(mappedBy = "group", fetch = EAGER, cascade = {REMOVE})
    private final List<Grant> grants;
    
    /**
     * 
     */
    public Group() { 
        this.blocked = false;
        this.grants = Collections.emptyList();
    }

    /**
     * 
     * @param name 
     */
    public Group(String name) {
        this();
        this.name = name;
    }
    
    /**
     * 
     * @param name
     * @param parent 
     */
    public Group(String name, Group parent) {
        this();
        this.name = name;
        this.parent = parent;
    }

    /**
     *
     * @param exampleData
     * @return
     */
    public static Group asExample(String exampleData) {
        if (exampleData == null) {
            exampleData = "";
        }
        return new Group(exampleData);
    }

    /**
     *
     * @return
     */
    public static SingularAttribute<Group, ?>[] filterProperties() {
        return new SingularAttribute[]{Group_.name};
    }
    
    /**
     * 
     * @param grant 
     */
    public void addRole(Grant grant) {
        this.grants.add(grant);
    }
    
    /**
     * 
     * @param grants 
     */
    public void addRoles(List<Grant> grants) {
        this.grants.addAll(grants);
    }

    /**
     * @return os grants do grupo e seu superior
     */
    public List<Grant> getGrants() {
        
        final List<Grant> groupGrants = new ArrayList<>(this.grants);
        
        if (this.parent != null) {
            groupGrants.addAll(this.parent.getGrants());
        }
        
        return Collections.unmodifiableList(groupGrants);
    }

    /**
     * @return as permissoes deste grupo e seu superior
     */
    public Set<String> getPermissions() {
        return this.getGrants().stream()
                    .map(Grant::getAuthorization)
                    .map(Authorization::getFullPermission)
                    .collect(Collectors.toSet());
    }
}
