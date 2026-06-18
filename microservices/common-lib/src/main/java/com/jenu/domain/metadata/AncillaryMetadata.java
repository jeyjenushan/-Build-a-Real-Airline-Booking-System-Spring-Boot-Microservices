package com.jenu.domain.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Unified metadata container for all ancillary types.
 * Only the relevant metadata object will be populated based on ancillary type.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AncillaryMetadata {

    // For BAGGAGE type ancillaries
    private BaggageMetadata baggage;

    // For TRAVEL_PROTECTION type ancillaries
    private String protectionSummary;

    // For SPECIAL_SERVICE type ancillaries
    private String specialServiceDetails;

    // For UPGRADE type ancillaries
    private String upgradeDetails;
}
