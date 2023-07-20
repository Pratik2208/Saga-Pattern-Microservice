package com.pratik.saga.PaymentService.command.api.events;

import com.pratik.saga.CommonService.events.PaymentProcessedEvent;
import com.pratik.saga.PaymentService.command.api.data.Payment;
import com.pratik.saga.PaymentService.command.api.data.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PaymentEventHandler {

    @Autowired
    private PaymentRepository paymentRepository;

    @EventHandler
    public void on(PaymentProcessedEvent event){
        Payment payment = Payment
                .builder()
                .orderId(event.getOrderId())
                .paymentStatus("COMPLETED")
                .paymentId(event.getPaymentId())
                .timeStamp(new Date())
                .build();

        paymentRepository.save(payment);
    }
}
