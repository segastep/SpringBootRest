package com.restapi.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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
        value = { "createdAt", "updatedAt"},
        allowGetters = true
)
public abstract class AuditModel implements Serializable {

    /**
     * Temporal(TemporalType.TIMESTAMP) annotation
     * Causes Hibernate annotation exception, because of the ZonedDateTime not being a valid Timestamp,
     * However, in 5.2 ZonedDateTime is persisted as timestamp by default, quite strange...
     */

    @Column(name = "UPDATED_AT", nullable = false)
    @ApiModelProperty(notes = "Last time when offer was modified")
    @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @LastModifiedDate
    private ZonedDateTime updatedAt;

    @NotNull
    @CreatedDate
    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    @ApiModelProperty(notes = "Time of creation of offer")
    @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime createdAt;

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public AuditModel setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public AuditModel setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Override
    public String toString() {
        return "AuditModel{" +
                "updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
