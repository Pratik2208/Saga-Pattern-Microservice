package com.pratik.saga.OrderService.commnad.api.saga;

import com.pratik.saga.CommonService.commands.CompleteOrderCommand;
import com.pratik.saga.CommonService.commands.ShipOrderCommand;
import com.pratik.saga.CommonService.commands.ValidatePaymentCommand;
import com.pratik.saga.CommonService.events.OrderCompletedEvent;
import com.pratik.saga.CommonService.events.OrderShippedEvent;
import com.pratik.saga.CommonService.events.PaymentProcessedEvent;
import com.pratik.saga.CommonService.model.User;
import com.pratik.saga.CommonService.queries.GetUserPaymentDetailsQuery;
import com.pratik.saga.OrderService.commnad.api.events.OrderCreatedEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
@Slf4j
@NoArgsConstructor
public class OrderProcessingSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderCreatedEvent event){
        // Saga also should handle this event & it should start the saga
        log.info("Order Created Event in Saga for Order Id : " + event.getOrderId());

        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery
                = new GetUserPaymentDetailsQuery(event.getUserId());

        // Now we will send this query and query handler in the User projection will handle that query and will give
        // the user

        User user = null;

        // there may be chance that we are not getting details from user service so keep this into try
        try{
            user = queryGateway.query(getUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        }catch (Exception e){
            log.error(e.getMessage());

            // If there is an error then start compensating the transaction
        }

        // Now this should trigger some other command like in this case it's validate payment command
        ValidatePaymentCommand validatePaymentCommand = ValidatePaymentCommand.builder()
                .cardDetail(user.getCardDetail())
                .paymentId(UUID.randomUUID().toString())
                .orderId(event.getOrderId())
                .build();

        commandGateway.sendAndWait(validatePaymentCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent event){

        try{
            ShipOrderCommand shipOrderCommand = ShipOrderCommand
                    .builder()
                    .shipmentId(UUID.randomUUID().toString())
                    .orderId(event.getOrderId())
                    .build();
            commandGateway.send(shipOrderCommand);
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent event){
        CompleteOrderCommand completeOrderCommand =
                CompleteOrderCommand.builder()
                        .orderId(event.getOrderId())
                        .orderStatus("APPROVED")
                        .build();

        commandGateway.send(completeOrderCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handle(OrderCompletedEvent event){
        log.info("Order Completed Event in Saga for Order Id : " + event.getOrderId());
    }
}
