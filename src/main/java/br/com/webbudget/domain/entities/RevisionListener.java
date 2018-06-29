package br.com.webbudget.domain.entities;

import org.apache.shiro.SecurityUtils;

/**
 * The listener to add more info to the revision of the audited entities
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/06/2018
 */
public class RevisionListener implements org.hibernate.envers.RevisionListener {

    /**
     * {@inheritDoc }
     *
     * @param revisionEntity
     */
    @Override
    public void newRevision(Object revisionEntity) {
        final Revision revision = (Revision) revisionEntity;
        revision.setCreatedBy(this.getLoggedUser());
    }

    /**
     * Get the username of the logged user
     *
     * @return the username of the logged user
     */
    private String getLoggedUser() {
        try {
            return String.valueOf(SecurityUtils.getSubject().getPrincipal());
        } catch (Exception ex) {
            return "unknow";
        }
    }
}