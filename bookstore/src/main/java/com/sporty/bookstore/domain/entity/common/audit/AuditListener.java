package com.sporty.bookstore.domain.entity.common.audit;

import com.sporty.bookstore.domain.entity.common.ModelStatus;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * Created by Tigran Melkonyan
 * Date: 4/16/25
 * Time: 8:24 PM
 */
public class AuditListener extends AuditingEntityListener {

    @PrePersist
    public void setCreatedOn(AuditableBaseEntity audit) {
        audit.setCreatedOn(Instant.now());
        audit.setStatus(ModelStatus.ACTIVE);
        setUpdatedOn(audit);
    }

    @PreUpdate
    public void setUpdatedOn(AuditableBaseEntity audit) {
        audit.setUpdatedOn(Instant.now());
        if (audit.getStatus().equals(ModelStatus.DELETED)) {
            audit.setDeletedOn(Instant.now());
        } else {
            audit.setDeletedOn(null);
        }
    }
}
