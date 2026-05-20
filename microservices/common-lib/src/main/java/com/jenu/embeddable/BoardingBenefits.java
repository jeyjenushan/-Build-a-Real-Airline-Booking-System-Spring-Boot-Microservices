package com.jenu.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardingBenefits {

    @Column(name = "priority_boarding",nullable = false)
    @Builder.Default
    private Boolean priorityBoarding=false;

    @Column(name = "priority_checking",nullable = false)
    @Builder.Default
    private Boolean priorityCheckin=false;

    @Column(name = "fast_track_security",nullable = false)
    @Builder.Default
    private Boolean fastTrackSecurity=false;

}
