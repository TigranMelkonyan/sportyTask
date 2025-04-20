package com.sporty.iam.domain.entity.common.audit;


import com.sporty.iam.domain.entity.common.base.AbstractDomainEntity;
import com.sporty.iam.domain.entity.common.base.ModelStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

/**
 * Created by Tigran Melkonyan
 * Date: 4/16/25
 * Time: 8:16 PM
 */
@EntityListeners(AuditListener.class)
@Getter
@Setter
@MappedSuperclass
public class AuditableBaseEntity extends AbstractDomainEntity {

    @CreatedDate
    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @LastModifiedDate
    @Column(name = "updated_on")
    private Instant updatedOn;

    @Column(name = "deleted_on")
    private Instant deletedOn;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ModelStatus status = ModelStatus.ACTIVE;

}
