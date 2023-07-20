package com.pratik.saga.PaymentService.command.api.aggregate;

import com.pratik.saga.CommonService.commands.ValidatePaymentCommand;
import com.pratik.saga.CommonService.events.PaymentProcessedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Slf4j
public class PaymentAggregate {
    @AggregateIdentifier
    private String paymentId;
    private String orderId;
    private String paymentStatus;

    public PaymentAggregate() {
    }

    @CommandHandler
    public PaymentAggregate(ValidatePaymentCommand validatePaymentCommand){
        // publish event so some event handler can listen that event
        log.info("Executing validate payment command for" + "Order id and Payment id :" +
                validatePaymentCommand.getOrderId(),validatePaymentCommand.getPaymentId()
                );

        PaymentProcessedEvent paymentProcessedEvent
                = new PaymentProcessedEvent(validatePaymentCommand.getOrderId()
                , validatePaymentCommand.getOrderId());

        AggregateLifecycle.apply(paymentProcessedEvent);
        log.info("Applied event");
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent event){
        this.paymentId = event.getPaymentId();
        this.orderId = event.getOrderId();
    }
}
