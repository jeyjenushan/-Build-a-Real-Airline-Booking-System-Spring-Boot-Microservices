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
public class FlexibilityBenefits {

    private Boolean freeDateChange=false;

    @Column(name = "partial_refund",nullable = false)
    @Builder.Default
    private Boolean partialRefund=false;

    @Column(name = "full_refund",nullable = false)
    @Builder.Default
    private Boolean fullRefund=false;

}
