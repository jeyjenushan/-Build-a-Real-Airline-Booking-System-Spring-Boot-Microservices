package com.jenu.payload.response;

import com.jenu.enums.SeatType;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse {
    private Long id;
    private String seatNumber;
    private Integer seatRow;
    private Character columnLetter;
    private SeatType seatType;

    private Boolean isAvailable;
    private Boolean isBlocked;

    private Boolean isActive;

    private Double basePrice;
    private Double premiumSupercharge;
    private Double totalPrice;

    private Boolean hasExtraLegroom;

    private Boolean hasPowerOutlet;
    private Boolean hasTvScreen;

    private Boolean hasExtraWidth;

    private Integer seatPitch;
    private Integer seatWidth;
    private Integer reclineAngle;

    private Long seatMapId;
    private String seatMapName;
    private Long cabinClassId;
    private String cabinClassName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private Boolean isBookable;
    private String fullPosition;
}
