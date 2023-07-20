package com.pratik.saga.OrderService.commnad.api.controller;

import com.pratik.saga.OrderService.commnad.api.command.CreateOrderCommand;
import com.pratik.saga.OrderService.commnad.api.model.OrderRestModel;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    public String createOrder(@RequestBody OrderRestModel orderRestModel){
        String orderId = UUID.randomUUID().toString();

        CreateOrderCommand createOrderCommand =
                CreateOrderCommand.builder()
                        .orderId(orderId)
                        .addressId(orderRestModel.getAddressId())
                        .productId(orderRestModel.getProductId())
                        .orderStatus("CREATED")
                        .quantity(orderRestModel.getQuantity())
                        .build();

        commandGateway.sendAndWait(createOrderCommand);
        return "Order Created";
    }
}
