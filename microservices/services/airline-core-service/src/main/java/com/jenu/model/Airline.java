package com.jenu.model;


import com.jenu.embeddable.Support;
import com.jenu.enums.AirlineStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "airlines")
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String iataCode;

    @Column(nullable = false,unique = true)
    private String icaoCode;

    @Column(nullable = false)
    private String name;

    private String alias;

    @Column(nullable = false)
    private String country;

    private String logoUrl;

    private String website;

    @Enumerated(EnumType.STRING)
    private AirlineStatus status=AirlineStatus.ACTIVE;

    private String alliance;

    @Column(name = "headquarters_city_id")
    private Long headquartersCityId;

    @Embedded
    private Support support;

    @Column(nullable = false,unique = true)
    private Long ownerId;

    private Long updatedById;

    @CreatedDate
    @Column(updatable = false,nullable = false)
    private Instant createdAt;


    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;


}
