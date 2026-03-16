package com.homebase.ecom.inventory.service.scheduler;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.dto.ReleaseReservedStockInventoryPayload;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.InventoryItemEntity;
import com.homebase.ecom.inventory.infrastructure.persistence.mapper.InventoryItemMapper;
import com.homebase.ecom.inventory.service.validator.InventoryItemPolicyValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Scheduled job that releases stale inventory reservations via STM.
 */
@Component
public class InventoryReservationExpiryScheduler {

    private static final Logger log = LoggerFactory.getLogger(InventoryReservationExpiryScheduler.class);

    @Autowired
    private InventoryItemPolicyValidator policyValidator;

    @Autowired
    @Qualifier("_inventoryStateEntityService_")
    private StateEntityServiceImpl<InventoryItem> inventoryStateEntityService;

    @Autowired
    private InventoryItemMapper inventoryItemMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Scheduled(fixedDelay = 5 * 60 * 1000)
    @Transactional
    public void releaseExpiredReservations() {
        int ttlMinutes = policyValidator.getReservationTtlMinutes();
        Instant cutoff = Instant.now().minus(ttlMinutes, ChronoUnit.MINUTES);

        log.debug("Running reservation expiry check. TTL={} minutes, cutoff={}", ttlMinutes, cutoff);

        TypedQuery<InventoryItemEntity> query = entityManager.createQuery(
                "SELECT DISTINCT i FROM InventoryItemEntity i JOIN i.activeReservations r " +
                "WHERE r.status = 'RESERVED' AND r.createdAt < :cutoff",
                InventoryItemEntity.class);
        query.setParameter("cutoff", cutoff);

        List<InventoryItemEntity> entities = query.getResultList();
        if (entities.isEmpty()) {
            return;
        }

        log.info("Found {} inventory items with potentially expired reservations", entities.size());

        for (InventoryItemEntity entity : entities) {
            InventoryItem model = inventoryItemMapper.toModel(entity);
            // Release each expired reservation through STM
            model.getExpiredOrderIds(cutoff).forEach(orderId -> {
                ReleaseReservedStockInventoryPayload payload = new ReleaseReservedStockInventoryPayload();
                payload.setOrderId(orderId);
                inventoryStateEntityService.processById(entity.getId(), "releaseReservedStock", payload);
            });
        }
    }
}
