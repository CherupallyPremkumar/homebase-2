package com.homebase.ecom.settlement.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.dto.CalculateSettlementSettlementPayload;
import com.homebase.ecom.shared.Money;
import com.homebase.ecom.shared.CurrencyResolver;
import org.chenile.cconfig.sdk.CconfigClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

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

        @Autowired
        private CconfigClient cconfigClient;

        @Autowired
        private CurrencyResolver currencyResolver;

        @Override
        public void transitionTo(Settlement settlement,
                        CalculateSettlementSettlementPayload payload,
                        State startState, String eventId,
                        State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

                // --- Fetch Dynamic Rules (Commission & Currency) ---
                BigDecimal commissionRate = new BigDecimal("0.10"); // Default fallback
                String currency = currencyResolver.resolve().code();

                try {
                        // Fetch Commission Rate from onboarding module
                        java.util.Map<String, Object> map = cconfigClient.value("on-boarding", null);
                        if (map != null) {
                                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                                com.fasterxml.jackson.databind.JsonNode node = mapper.valueToTree(map);
                                com.fasterxml.jackson.databind.JsonNode commNode = node
                                                .at("/rules/finances/commissionDefault");
                                if (!commNode.isMissingNode() && commNode.isNumber()) {
                                        commissionRate = BigDecimal.valueOf(commNode.asDouble())
                                                        .divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
                                }
                        }
                } catch (Exception e) {
                        // Log or handle error, using default commissionRate
                }

                // 1. Fetch completed order items for this supplier for the given month/year
                java.util.List<com.homebase.ecom.settlement.dto.SettlementOrderItemDTO> deliveredItems = orderClient
                                .getDeliveredOrderItemsForSupplier(
                                                settlement.getSupplierId(), settlement.getPeriodMonth(),
                                                settlement.getPeriodYear());

                java.math.BigDecimal totalSales = java.math.BigDecimal.ZERO;
                java.math.BigDecimal totalCommission = java.math.BigDecimal.ZERO;

                // 2. Aggregate line items
                for (com.homebase.ecom.settlement.dto.SettlementOrderItemDTO item : deliveredItems) {
                        com.homebase.ecom.settlement.model.SettlementLineItem lineItem = new com.homebase.ecom.settlement.model.SettlementLineItem();
                        lineItem.setSettlement(settlement);
                        lineItem.setOrderId(item.orderId);
                        lineItem.setOrderItemId(item.orderItemId);

                        com.homebase.ecom.shared.Money salesMoney = new com.homebase.ecom.shared.Money(
                                        item.itemSalesAmount, currency);
                        lineItem.setItemSalesAmount(salesMoney);

                        // Calculate commission for this item (scale 2 for currency precision)
                        java.math.BigDecimal itemCommission = item.itemSalesAmount.multiply(commissionRate)
                                        .setScale(2, java.math.RoundingMode.HALF_UP);
                        com.homebase.ecom.shared.Money commMoney = new com.homebase.ecom.shared.Money(itemCommission,
                                        currency);
                        lineItem.setItemCommissionAmount(commMoney);

                        settlement.getLineItems().add(lineItem);

                        totalSales = totalSales.add(item.itemSalesAmount);
                        totalCommission = totalCommission.add(itemCommission);
                }

                // 3. Finalize settlement aggregate (all amounts scaled to 2 decimal places)
                totalSales = totalSales.setScale(2, java.math.RoundingMode.HALF_UP);
                totalCommission = totalCommission.setScale(2, java.math.RoundingMode.HALF_UP);
                java.math.BigDecimal netPayout = totalSales.subtract(totalCommission).setScale(2, java.math.RoundingMode.HALF_UP);

                settlement.setTotalSalesAmount(new com.homebase.ecom.shared.Money(totalSales, currency));
                settlement.setCommissionAmount(new com.homebase.ecom.shared.Money(totalCommission, currency));
                settlement.setNetPayoutAmount(new com.homebase.ecom.shared.Money(netPayout, currency));

                settlement.getTransientMap().previousPayload = payload;
        }

}
