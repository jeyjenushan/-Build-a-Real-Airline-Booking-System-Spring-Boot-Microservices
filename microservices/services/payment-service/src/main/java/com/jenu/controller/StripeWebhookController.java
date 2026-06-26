package com.jenu.controller;


import com.jenu.enums.PaymentStatus;
import com.jenu.exception.PaymentException;
import com.jenu.model.Payment;
import com.jenu.repository.PaymentRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments/webhook")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {

    private final PaymentRepository paymentRepository;
    //private final PaymentEventProducer paymentEventProducer;

    @Value("${stripe.webhook.secret:}")
    private String stripeWebhookSecret;

    /**
     * Handle Stripe webhook events
     * Stripe will send webhook events to this endpoint when payment status changes
     */
    @PostMapping("/stripe")
    public ResponseEntity<?> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String stripeSignature) {

        if (stripeWebhookSecret == null || stripeWebhookSecret.isEmpty()) {
            log.warn("Stripe webhook secret not configured");
            return ResponseEntity.ok().build();
        }

        try {
            // Verify the webhook signature
            Event event = Webhook.constructEvent(payload, stripeSignature, stripeWebhookSecret);

            log.info("Received Stripe webhook event: {}", event.getType());

            // Handle different event types
            switch (event.getType()) {
                case "payment_intent.succeeded":
                    handlePaymentIntentSucceeded(event);
                    break;

                case "payment_intent.payment_failed":
                    handlePaymentIntentFailed(event);
                    break;

                case "payment_intent.canceled":
                    handlePaymentIntentCanceled(event);
                    break;

                case "charge.refunded":
                    handleChargeRefunded(event);
                    break;

                case "payment_link.created":
                    log.info("Payment link created: {}", event.getData());
                    break;

                default:
                    log.debug("Unhandled Stripe event type: {}", event.getType());
            }

            return ResponseEntity.ok().build();

        } catch (SignatureVerificationException e) {
            log.error("Invalid Stripe webhook signature: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (Exception e) {
            log.error("Error processing Stripe webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Handle payment_intent.succeeded event
     */
    private void handlePaymentIntentSucceeded(Event event) throws PaymentException {
        EventDataObjectDeserializer dataDeserializer = event.getDataObjectDeserializer();
        PaymentIntent paymentIntent = (PaymentIntent) dataDeserializer.getObject()
                .orElseThrow(() -> new PaymentException("Failed to deserialize payment intent"));

        log.info("Payment intent succeeded: {}", paymentIntent.getId());

        // Get payment ID from metadata
        Map<String, String> metadata = paymentIntent.getMetadata();
        if (metadata == null || !metadata.containsKey("payment_id")) {
            log.error("Payment ID not found in Stripe metadata");
            return;
        }

        Long paymentId = Long.parseLong(metadata.get("payment_id"));

        // Update payment status
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(LocalDateTime.now());
            payment.setProviderPaymentId(paymentIntent.getId());

            paymentRepository.save(payment);
            log.info("Payment {} marked as SUCCESS", paymentId);

            // Send payment completed event
           // paymentEventProducer.sendPaymentCompleted(payment);
        } else {
            log.error("Payment not found with ID: {}", paymentId);
        }
    }

    /**
     * Handle payment_intent.payment_failed event
     */
    private void handlePaymentIntentFailed(Event event) throws PaymentException {
        EventDataObjectDeserializer dataDeserializer = event.getDataObjectDeserializer();
        PaymentIntent paymentIntent = (PaymentIntent) dataDeserializer.getObject()
                .orElseThrow(() -> new PaymentException("Failed to deserialize payment intent"));

        log.warn("Payment intent failed: {}", paymentIntent.getId());

        // Get payment ID from metadata
        Map<String, String> metadata = paymentIntent.getMetadata();
        if (metadata == null || !metadata.containsKey("payment_id")) {
            log.error("Payment ID not found in Stripe metadata");
            return;
        }

        Long paymentId = Long.parseLong(metadata.get("payment_id"));

        // Update payment status
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason(paymentIntent.getLastPaymentError() != null ?
                    paymentIntent.getLastPaymentError().getMessage() :
                    "Payment failed");

            paymentRepository.save(payment);
            log.info("Payment {} marked as FAILED", paymentId);

            // Send payment failed event
           // paymentEventProducer.sendPaymentFailed(payment);
        } else {
            log.error("Payment not found with ID: {}", paymentId);
        }
    }

    /**
     * Handle payment_intent.canceled event
     */
    private void handlePaymentIntentCanceled(Event event) throws PaymentException {
        EventDataObjectDeserializer dataDeserializer = event.getDataObjectDeserializer();
        PaymentIntent paymentIntent = (PaymentIntent) dataDeserializer.getObject()
                .orElseThrow(() -> new PaymentException("Failed to deserialize payment intent"));

        log.info("Payment intent cancelled: {}", paymentIntent.getId());

        // Get payment ID from metadata
        Map<String, String> metadata = paymentIntent.getMetadata();
        if (metadata == null || !metadata.containsKey("payment_id")) {
            log.error("Payment ID not found in Stripe metadata");
            return;
        }

        Long paymentId = Long.parseLong(metadata.get("payment_id"));

        // Update payment status
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Payment cancelled by user");

            paymentRepository.save(payment);
            log.info("Payment {} marked as FAILED (cancelled)", paymentId);

            // Send payment failed event
            //paymentEventProducer.sendPaymentFailed(payment);
        } else {
            log.error("Payment not found with ID: {}", paymentId);
        }
    }

    /**
     * Handle charge.refunded event
     */
    private void handleChargeRefunded(Event event) throws PaymentException {
        EventDataObjectDeserializer dataDeserializer = event.getDataObjectDeserializer();
        Charge charge = (Charge) dataDeserializer.getObject()
                .orElseThrow(() -> new PaymentException("Failed to deserialize charge"));

        log.info("Charge refunded: {} with refund ID: {}", charge.getId(), charge.getRefunded());

        // Get payment by provider payment ID
        // This would need to search by provider payment ID
        // For now, we just log it
        log.info("Refund processed for charge: {}", charge.getId());
    }
}
