package org.example.kafka;

import org.example.kafka.entity.PurchaseOrder;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PurchaseOrderRepository {

    private final Map<Long, PurchaseOrder> database = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public PurchaseOrder save(PurchaseOrder purchaseOrder) {
        if (purchaseOrder.getId() == null) {
            purchaseOrder.setId(idGenerator.getAndIncrement());
        }
        database.put(purchaseOrder.getId(), purchaseOrder);
        return purchaseOrder;
    }

    public List<PurchaseOrder> findAll() {
        return new ArrayList<>(database.values());
    }
}
