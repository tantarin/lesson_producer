package org.example.kafka.service;

import org.example.kafka.PurchaseOrderRepository;
import org.example.kafka.dto.OrderRequestDTO;
import org.example.kafka.dto.OrderResponseDTO;
import org.example.kafka.entity.PurchaseOrder;
import org.example.kafka.enums.OrderStatus;
import org.example.kafka.eventhandlers.OrderEventPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    // product price map
    private static final Map<Integer, Integer> PRODUCT_PRICE =  Map.of(
            1, 100,
            2, 200,
            3, 300
    );

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private OrderEventPublisherService eventPublisherService;

    public PurchaseOrder createOrder(OrderRequestDTO orderRequestDTO){
        PurchaseOrder purchaseOrder = this.purchaseOrderRepository.save(this.dtoToEntity(orderRequestDTO));
        this.eventPublisherService.raiseOrderCreatedEvent(purchaseOrder);
        return purchaseOrder;
    }

    public List<OrderResponseDTO> getAll() {
        return this.purchaseOrderRepository.findAll()
                                .stream()
                                .map(this::entityToDto)
                                .collect(Collectors.toList());
    }

    private PurchaseOrder dtoToEntity(final OrderRequestDTO dto){
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(PRODUCT_PRICE.get(purchaseOrder.getProductId()));
        return purchaseOrder;
    }

    private OrderResponseDTO entityToDto(final PurchaseOrder purchaseOrder){
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(purchaseOrder.getId());
        dto.setProductId(purchaseOrder.getProductId());
        dto.setUserId(purchaseOrder.getUserId());
        dto.setStatus(purchaseOrder.getStatus());
        dto.setPrice(purchaseOrder.getPrice());
        return dto;
    }

}
