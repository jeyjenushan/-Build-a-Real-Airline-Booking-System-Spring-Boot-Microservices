package com.jenu.paymentservice.service;


import com.jenu.exception.PaymentException;
import com.jenu.payload.request.PaymentInitiateRequest;
import com.jenu.payload.request.PaymentVerifyRequest;
import com.jenu.payload.response.PaymentDTO;
import com.jenu.payload.response.PaymentInitiateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PaymentService {

    PaymentInitiateResponse initiatePayment(PaymentInitiateRequest request) throws PaymentException;

    PaymentDTO verifyPayment(PaymentVerifyRequest request) throws PaymentException;


    Page<PaymentDTO> getAllPayments(Pageable pageable);



    Map<Long, PaymentDTO> getPaymentsByBookingIds(List<Long> bookingIds);
}
