package com.pratik.saga.CommonService.commands;

import com.pratik.saga.CommonService.model.CardDetail;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class ValidatePaymentCommand {
    @TargetAggregateIdentifier
    private String paymentId;
    private String orderId;
    private CardDetail cardDetail;
}
