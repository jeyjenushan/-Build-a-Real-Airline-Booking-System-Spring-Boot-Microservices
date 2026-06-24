package com.jenu.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVerifyRequest {

    // Razorpay specific fields
    private String razorpayPaymentId;

    // Stripe specific fields
    private String stripePaymentIntentId;
}
