package com.restapi.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;


/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-basic
 *
 * Helper class to reuse some fields across DTO objects
 */


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = { "createdAt", "updatedAt", "createdBy"},
        allowGetters = true
)
public abstract class AuditModel<U> implements Serializable {

    /**
     * Reason why using Instant, took quite awhile to figure it out, quite confusing since for normal
     * entities ZonedDateTime is supported
     * https://github.com/communicode/communikey-backend/commit/3ccb8ef9d978ac8ff5a0619d7de3855e98540e25
     */

    @CreatedBy
    @Column(name = "CREATED_BY", nullable = false)
    @Type(type = "nstring")
    private U createdBy;


    @Column(name = "UPDATED_AT", nullable = false)
    @ApiModelProperty(notes = "Last time when offer was modified in instant type")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ",timezone = "UTC")
    @LastModifiedDate
    private Instant updatedAt;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    @ApiModelProperty(notes = "Time of creation of offer in instant type")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ",timezone = "UTC")
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
