package com.restapi.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
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
        value = {"createdAt", "updatedAt", "createdBy"},
        allowGetters = true
)
@Api(value="Model to audit entities, all timestamps are in SERVER'S LOCAL TIME")
public abstract class AuditModel<U> implements Serializable {

    @CreatedBy
    @ApiModelProperty(notes = "Creator of resource",readOnly = true)
    @Column(name = "CREATED_BY", nullable = false)
    @Type(type = "nstring")
    private U createdBy;


    @Column(name = "UPDATED_AT", nullable = false)
    @ApiModelProperty(notes = "Last time when offer was modified in instant type", readOnly = true)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    @ApiModelProperty(notes = "Creation time",readOnly = true)
    private LocalDateTime createdAt;

    public void setCreatedBy(U createdBy)
    {
        this.createdBy = createdBy;
    }

    public U getCreatedBy()
    {
        return this.createdBy;
    }

    public LocalDateTime getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
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
