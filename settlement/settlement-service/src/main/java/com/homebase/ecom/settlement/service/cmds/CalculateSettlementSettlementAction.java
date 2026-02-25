package com.homebase.ecom.settlement.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.dto.CalculateSettlementSettlementPayload;
import com.homebase.ecom.shared.model.Money;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * Contains customized logic for the transition. Common logic resides at
 * {@link DefaultSTMTransitionAction}
 * <p>
 * Use this class if you want to augment the common logic for this specific
 * transition
 * </p>
 * <p>
 * Use a customized payload if required instead of MinimalPayload
 * </p>
 */
public class CalculateSettlementSettlementAction extends AbstractSTMTransitionAction<Settlement,

        CalculateSettlementSettlementPayload> {

    @Autowired
    private com.homebase.ecom.settlement.service.client.InternalOrderClient orderClient;

    // Platform commission rate, e.g., 10%
    private static final java.math.BigDecimal COMMISSION_RATE = new java.math.BigDecimal("0.10");

    @Override
    public void transitionTo(Settlement settlement,
            CalculateSettlementSettlementPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // 1. Fetch completed order items for this supplier for the given month/year
        java.util.List<com.homebase.ecom.settlement.dto.SettlementOrderItemDTO> deliveredItems = orderClient
                .getDeliveredOrderItemsForSupplier(
                        settlement.getSupplierId(), settlement.getPeriodMonth(), settlement.getPeriodYear());

        java.math.BigDecimal totalSales = java.math.BigDecimal.ZERO;
        java.math.BigDecimal totalCommission = java.math.BigDecimal.ZERO;

        // 2. Aggregate line items
        for (com.homebase.ecom.settlement.dto.SettlementOrderItemDTO item : deliveredItems) {
            com.homebase.ecom.settlement.model.SettlementLineItem lineItem = new com.homebase.ecom.settlement.model.SettlementLineItem();
            lineItem.setSettlement(settlement);
            lineItem.setOrderId(item.orderId);
            lineItem.setOrderItemId(item.orderItemId);

            com.homebase.ecom.shared.model.Money salesMoney = new com.homebase.ecom.shared.model.Money();
            salesMoney.setAmount(item.itemSalesAmount);
            salesMoney.setCurrency("INR");
            lineItem.setItemSalesAmount(salesMoney);

            // Calculate commission for this item
            java.math.BigDecimal itemCommission = item.itemSalesAmount.multiply(COMMISSION_RATE);
            com.homebase.ecom.shared.model.Money commMoney = new com.homebase.ecom.shared.model.Money();
            commMoney.setAmount(itemCommission);
            commMoney.setCurrency("INR");
            lineItem.setItemCommissionAmount(commMoney);

            settlement.getLineItems().add(lineItem);

            totalSales = totalSales.add(item.itemSalesAmount);
            totalCommission = totalCommission.add(itemCommission);
        }

        // 3. Set aggregated amounts on the Settlement root entity
        com.homebase.ecom.shared.model.Money totalM = new com.homebase.ecom.shared.model.Money();
        totalM.setAmount(totalSales);
        totalM.setCurrency("INR");
        settlement.setTotalSalesAmount(totalM);

        com.homebase.ecom.shared.model.Money commM = new com.homebase.ecom.shared.model.Money();
        commM.setAmount(totalCommission);
        commM.setCurrency("INR");
        settlement.setCommissionAmount(commM);

        com.homebase.ecom.shared.model.Money netM = new com.homebase.ecom.shared.model.Money();
        netM.setAmount(totalSales.subtract(totalCommission));
        netM.setCurrency("INR");
        settlement.setNetPayoutAmount(netM);

        settlement.transientMap.previousPayload = payload;
    }

}
