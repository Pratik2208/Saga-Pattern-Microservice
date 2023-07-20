package com.pratik.saga.ShipmentService.command.api.events;

import com.pratik.saga.CommonService.events.OrderShippedEvent;
import com.pratik.saga.ShipmentService.command.api.data.Shipment;
import com.pratik.saga.ShipmentService.command.api.data.ShipmentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipmentEventHandler {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @EventHandler
    public void on(OrderShippedEvent event){
        Shipment shipment
                = new Shipment();
        BeanUtils.copyProperties(event,shipment);
        shipmentRepository.save(shipment);
    }
}