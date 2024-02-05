package com.thewildoasis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    // @JsonIgnore
    @JsonProperty("created_at")
    @CreatedDate
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @CreatedBy
    @Column(name = "createdBy", updatable = false)
    private String createdBy;

    @JsonIgnore
    @LastModifiedDate
    @Column(name = "updatedAt", insertable = false)
    private LocalDateTime updatedAt;

    @JsonIgnore
    @LastModifiedBy
    @Column(name = "updatedBy", insertable = false)
    private String updatedBy;
}
