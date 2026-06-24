package com.jenu.service.impl;


import com.jenu.client.UserClient;
import com.jenu.enums.PaymentGateway;
import com.jenu.enums.PaymentStatus;
import com.jenu.exception.PaymentException;
import com.jenu.payload.dto.UserDto;
import com.jenu.payload.request.PaymentInitiateRequest;
import com.jenu.payload.request.PaymentVerifyRequest;
import com.jenu.payload.response.PaymentDTO;
import com.jenu.payload.response.PaymentInitiateResponse;
import com.jenu.mapper.PaymentMapper;
import com.jenu.model.Payment;
import com.jenu.payload.response.PaymentLinkResponse;
import com.jenu.repository.PaymentRepository;
import com.jenu.service.PaymentService;
import com.jenu.service.gateway.StripeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
   // private final PaymentEventProducer paymentEventProducer;
    private final StripeService stripeService;
    private final UserClient userClient;

    @Override
    @Transactional
    public PaymentInitiateResponse initiatePayment(PaymentInitiateRequest request) throws PaymentException {
        try {
            log.info("Initiating payment for user: {} with gateway: {}",
                    request.getUserId(), request.getGateway());

            // Check if payment already exists for this booking
            paymentRepository.findByBookingId(request.getBookingId())
                    .ifPresent(existingPayment -> {
                        if (existingPayment.getStatus() == PaymentStatus.SUCCESS) {
                            throw new RuntimeException("Payment already completed for this booking");
                        }
                    });

            // Create payment entity
            Payment payment = Payment.builder()
                    .userId(request.getUserId())
                    .bookingId(request.getBookingId())
                    .amount(request.getAmount())
                    .provider(request.getGateway())
                    .status(PaymentStatus.PENDING)
                    .transactionId(generateTransactionId())
                    .build();

            payment = paymentRepository.save(payment);

            // Create response based on gateway
            PaymentInitiateResponse response = PaymentInitiateResponse.builder()
                    .paymentId(payment.getId())
                    .gateway(request.getGateway())
                    .transactionId(payment.getTransactionId())
                    .amount(request.getAmount())
                    .description(request.getDescription())
                    .success(true)
                    .message("Payment initiated successfully")
                    .build();

            if (request.getGateway() == PaymentGateway.STRIPE) {
                log.info("Fetching user details from user-service for user ID: {}", request.getUserId());
                
                UserDto user = userClient.getUserById(request.getUserId());
                
                if (user == null) {
                    log.error("User not found for ID: {}", request.getUserId());
                    throw new PaymentException("User not found for payment initiation");
                }
                
                log.info("Successfully retrieved user: {}", user.getId());
                PaymentLinkResponse paymentLinkResponse = stripeService.createPaymentLink(user, payment);
                response.setCheckoutUrl(paymentLinkResponse.getPayment_link_url());
                response.setStripePaymentLinkId(paymentLinkResponse.getPayment_link_id());
                log.info("Payment link created successfully for user: {}", user.getId());

            }

            log.info("Payment initiated successfully with ID: {}", payment.getId());
            return response;

        } catch (Exception e) {
            log.error("Error initiating payment: {}", e.getMessage(), e);
            throw new PaymentException("Failed to initiate payment: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PaymentDTO verifyPayment(PaymentVerifyRequest request) throws PaymentException {
        log.info("Verifying payment: {}", request);
        
        Payment payment = null;
        boolean isValid = false;
        
      if (request.getStripePaymentIntentId() != null) {
            // Handle Stripe verification
            isValid = stripeService.verifyPayment(request.getStripePaymentIntentId());
            
            // Get payment ID from metadata
            Map<String, Object> paymentIntentDetails = stripeService.retrievePaymentIntent(request.getStripePaymentIntentId());
            
            @SuppressWarnings("unchecked")
            Map<String, String> metadata = (Map<String, String>) paymentIntentDetails.get("metadata");
            Long paymentId = Long.parseLong(metadata.get("payment_id"));
            
            payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new PaymentException("Payment not found with ID: " + paymentId));
            
            if (isValid) {
                payment.setProviderPaymentId(request.getStripePaymentIntentId());
            }
        }
      else {
            throw new PaymentException("No valid payment verification ID provided");
        }

        if (isValid) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(LocalDateTime.now());

            // Save payment first
            payment = paymentRepository.save(payment);
            log.info("Payment verified successfully: {}", payment.getId());
            //paymentEventProducer.sendPaymentCompleted(payment);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Payment verification failed");
            log.error("Payment verification failed: {}", payment.getId());
            payment = paymentRepository.save(payment);

           // paymentEventProducer.sendPaymentFailed(payment);
        }

        return PaymentMapper.toDTO(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDTO> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .map(PaymentMapper::toDTO);
    }



    @Override
    @Transactional(readOnly = true)
    public Map<Long, PaymentDTO> getPaymentsByBookingIds(List<Long> bookingIds) {
        if (bookingIds == null || bookingIds.isEmpty()) return Map.of();
        return paymentRepository.findByBookingIdIn(bookingIds).stream()
                .collect(Collectors.toMap(Payment::getBookingId, PaymentMapper::toDTO));
    }

    private String generateTransactionId() {
        return "TXN_" + System.currentTimeMillis() + "_" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
