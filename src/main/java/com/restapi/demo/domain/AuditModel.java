package com.restapi.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import io.swagger.models.auth.In;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-basic
 *
 * Helper class to reuse some fields across DTO objects
 */


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt", "createdBy"},
        allowGetters = true
)
@Api(value="Model to audit entities, all timestamps are in SERVER'S LOCAL TIME")
public abstract class AuditModel<U> implements Serializable {

    /**
     * As of Spring Boot 2.0 the "java.time.ZonedDateTime" class is not
     * supported for JPA auditing (AuditAware)
     *
     * Better if we use Instant, since at specifies precise point in
     * time independent of time zone.
     *
     * References:
     *
     *   (1) https://jira.spring.io/browse/DATAJPA-1242
     *   (2) spring-projects/spring-boot#10743
     *   (3) https://jira.spring.io/browse/DATACMNS-1243
     */

    @CreatedBy
    @ApiModelProperty(notes = "Creator of resource",readOnly = true)
    @Column(name = "CREATED_BY", nullable = false)
    @Type(type = "nstring")
    private U createdBy;


    @Column(name = "UPDATED_AT", nullable = false)
    @ApiModelProperty(notes = "Last time when offer was modified in instant type", readOnly = true)
    @LastModifiedDate
    private Instant updatedAt;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    @ApiModelProperty(notes = "Creation time",readOnly = true)
    private Instant createdAt;

    public void setCreatedBy(U createdBy)
    {
        this.createdBy = createdBy;
    }

    public U getCreatedBy()
    {
        return this.createdBy;
    }

    public Instant getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public Instant getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt)
    {
        this.createdAt = createdAt;
    }

    @Override
    public String toString()
    {
        return "AuditModel{" +
                "createdBy=" + createdBy +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
