package com.jenu.service.gateway;

import com.jenu.exception.PaymentException;
import com.jenu.payload.dto.UserDto;
import com.jenu.payload.response.PaymentLinkResponse;
import com.jenu.model.Payment;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;



    @Value("${stripe.callback.base-url}")
    private String callbackBaseUrl;

    /**
     * Initialize Stripe API key
     */
    private void initializeStripe() {
        if (stripeApiKey != null && !stripeApiKey.isEmpty()) {
            Stripe.apiKey = stripeApiKey;
        }
    }

    /**
     * Create a payment link using Stripe Checkout
     * This creates a hosted checkout page URL
     */
    public PaymentLinkResponse createPaymentLink(
            UserDto user,
            Payment payment) throws PaymentException {

        if (!isConfigured()) {
            throw new PaymentException("Stripe not configured. Please setup API key");
        }

        try {
            initializeStripe();

            // Convert amount to cents (1 USD = 100 cents)
            BigDecimal amount = BigDecimal.valueOf(payment.getAmount());
            Long amountInCents = amount.multiply(new BigDecimal("100")).longValue();

            // Create success and cancel URLs
            String successUrl = callbackBaseUrl + "/booking-success/" + payment.getBookingId() + "?session_id={CHECKOUT_SESSION_ID}";
            String cancelUrl = callbackBaseUrl + "/payment-cancelled/" + payment.getId();

            // Create line items for the payment
            SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("usd")
                    .setUnitAmount(amountInCents)
                    .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Airline Booking - Transaction: " + payment.getTransactionId())
                                    .setDescription("Booking ID: " + payment.getBookingId())
                                    .build()
                    )
                    .build();

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setPriceData(priceData)
                    .setQuantity(1L)
                    .build();

            // Build customer information
            SessionCreateParams.CustomerCreation customerCreation = SessionCreateParams.CustomerCreation.ALWAYS;

            // Create metadata for tracking
            Map<String, String> metadata = new HashMap<>();
            metadata.put("user_id", String.valueOf(user.getId()));
            metadata.put("payment_id", String.valueOf(payment.getId()));
            metadata.put("booking_id", String.valueOf(payment.getBookingId()));
            metadata.put("transaction_id", payment.getTransactionId());

            // Create payment link parameters
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount(amountInCents)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Airline Booking - Transaction: " + payment.getTransactionId())
                                                                    .setDescription("Booking ID: " + payment.getBookingId())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .putAllMetadata(metadata)
                    .putAllMetadata(metadata)
                    .setCustomerEmail(user.getEmail())
                    .setPhoneNumberCollection(
                            SessionCreateParams.PhoneNumberCollection.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);

            PaymentLinkResponse response = new PaymentLinkResponse();
            response.setPayment_link_url(session.getUrl());
            response.setPayment_link_id(session.getId());


            return response;

        } catch (StripeException e) {
            log.error("Failed to create Stripe payment link: {}", e.getMessage());
            throw new PaymentException("Failed to create Stripe payment link: " + e.getMessage());
        }
    }

    /**
     * Create a Payment Intent for more control over the payment process
     */
    public Map<String, Object> createPaymentIntent(
            UserDto user,
            Payment payment) throws PaymentException {

        if (!isConfigured()) {
            throw new PaymentException("Stripe not configured. Please setup API key");
        }

        try {
            initializeStripe();

            // Convert amount to cents
            BigDecimal amount = BigDecimal.valueOf(payment.getAmount());
            Long amountInCents = amount.multiply(new BigDecimal("100")).longValue();

            // Create metadata
            Map<String, String> metadata = new HashMap<>();
            metadata.put("user_id", String.valueOf(user.getId()));
            metadata.put("payment_id", String.valueOf(payment.getId()));
            metadata.put("booking_id", String.valueOf(payment.getBookingId()));
            metadata.put("transaction_id", payment.getTransactionId());

            // Create payment intent
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("usd")
                    .setDescription("Airline Booking - " + payment.getTransactionId())
                    .setReceiptEmail(user.getEmail())
                    .putAllMetadata(metadata)
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            Map<String, Object> response = new HashMap<>();
            response.put("id", paymentIntent.getId());
            response.put("client_secret", paymentIntent.getClientSecret());
            response.put("amount", paymentIntent.getAmount());
            response.put("currency", paymentIntent.getCurrency());
            response.put("status", paymentIntent.getStatus());

            log.info("Stripe payment intent created with ID: {}", paymentIntent.getId());
            return response;

        } catch (StripeException e) {
            log.error("Failed to create Stripe payment intent: {}", e.getMessage());
            throw new PaymentException("Failed to create Stripe payment intent: " + e.getMessage());
        }
    }

    /**
     * Retrieve payment intent details
     */
    public Map<String, Object> retrievePaymentIntent(String paymentIntentId) throws PaymentException {
        if (!isConfigured()) {
            throw new PaymentException("Stripe not configured. Please setup API key");
        }

        try {
            initializeStripe();

            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            Map<String, Object> response = new HashMap<>();
            response.put("id", paymentIntent.getId());
            response.put("status", paymentIntent.getStatus());
            response.put("amount", paymentIntent.getAmount());
            response.put("currency", paymentIntent.getCurrency());
            response.put("latest_charge", paymentIntent.getLatestCharge());
            response.put("metadata", paymentIntent.getMetadata());
            response.put("created", paymentIntent.getCreated());

            log.info("Retrieved Stripe payment intent: {}", paymentIntentId);
            return response;

        } catch (StripeException e) {
            log.error("Failed to retrieve Stripe payment intent: {}", e.getMessage());
            throw new PaymentException("Failed to retrieve payment intent: " + e.getMessage());
        }
    }

    public Map<String, Object> retrieveCheckoutSession(String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);

        Map<String, Object> result = new HashMap<>();
        result.put("payment_intent", session.getPaymentIntent());
        result.put("payment_status", session.getPaymentStatus());

        return result;
    }

    /**
     * Verify payment by checking if it was successfully charged
     */
    public boolean verifyPayment(String paymentIntentId) throws PaymentException {
        if (!isConfigured()) {
            throw new PaymentException("Stripe not configured. Please setup API key");
        }

        try {
            initializeStripe();

            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            // Check if payment succeeded
            boolean isSuccessful = paymentIntent.getStatus().equals("succeeded");

            log.info("Payment verification for {}: {}", paymentIntentId, isSuccessful);
            return isSuccessful;

        } catch (StripeException e) {
            log.error("Failed to verify Stripe payment: {}", e.getMessage());
            throw new PaymentException("Failed to verify payment: " + e.getMessage());
        }
    }

    /**
     * Refund a payment
     */
    public Map<String, Object> refundPayment(String paymentIntentId, Long amountInCents) throws PaymentException {
        if (!isConfigured()) {
            throw new PaymentException("Stripe not configured. Please setup API key");
        }

        try {
            initializeStripe();

            RefundCreateParams.Builder refundBuilder = RefundCreateParams.builder()
                    .setPaymentIntent(paymentIntentId);

            if (amountInCents != null) {
                refundBuilder.setAmount(amountInCents);
            }

            Refund refund = Refund.create(refundBuilder.build());

            Map<String, Object> response = new HashMap<>();
            response.put("refund_id", refund.getId());
            response.put("amount", refund.getAmount());
            response.put("status", refund.getStatus());
            response.put("payment_intent", refund.getPaymentIntent());

            log.info("Refund processed for payment intent {}: {}", paymentIntentId, refund.getId());
            return response;

        } catch (StripeException e) {
            log.error("Failed to refund Stripe payment: {}", e.getMessage());
            throw new PaymentException("Failed to refund payment: " + e.getMessage());
        }
    }
    /**
     * Check if Stripe is properly configured
     */
    public boolean isConfigured() {
        return stripeApiKey != null && !stripeApiKey.isEmpty();
    }
}
