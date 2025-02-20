package com.security.securityImpl.security.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
@Data
@MappedSuperclass
public class Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time")
    private Date creationTime;

    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date")
    private Date lastModifiedDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;


}
