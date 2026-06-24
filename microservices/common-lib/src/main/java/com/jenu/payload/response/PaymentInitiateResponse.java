package com.jenu.payload.response;

import com.jenu.enums.PaymentGateway;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInitiateResponse {

    private Long paymentId;
    private PaymentGateway gateway;
    private String transactionId;

    // Razorpay specific fields
    private String razorpayOrderId;
    
    // Stripe specific fields
    private String stripePaymentLinkId;
    private String stripePaymentIntentId;

    private Double amount;
    private String currency;
    private String description;

    // Frontend should redirect user to this URL for payment
    private String checkoutUrl;

    private String message;
    private Boolean success;
}
