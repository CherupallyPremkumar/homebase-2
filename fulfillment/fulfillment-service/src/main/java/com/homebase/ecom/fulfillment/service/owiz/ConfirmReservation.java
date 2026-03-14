package com.homebase.ecom.fulfillment.service.owiz;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * OWIZ command that confirms the inventory reservation was successful
 * and stores the reservation IDs on the saga.
 */
@Component("confirmReservation")
public class ConfirmReservation implements Command<ChenileExchange> {

    private static final Logger log = LoggerFactory.getLogger(ConfirmReservation.class);

    @Override
    public void execute(ChenileExchange exchange) throws Exception {
        FulfillmentSaga saga = (FulfillmentSaga) exchange.getBody();
        log.info("Confirming reservation for order {}", saga.getOrderId());

        @SuppressWarnings("unchecked")
        java.util.List<String> reservationIds =
                (java.util.List<String>) saga.getTransientMap().get("reservationIds");

        if (reservationIds == null || reservationIds.isEmpty()) {
            String msg = "No reservation IDs found after stock deduction for order " + saga.getOrderId();
            saga.setErrorMessage(msg);
            throw new RuntimeException(msg);
        }

        Boolean stockValidated = (Boolean) saga.getTransientMap().get("stockValidated");
        if (!Boolean.TRUE.equals(stockValidated)) {
            String msg = "Stock validation was not completed for order " + saga.getOrderId();
            saga.setErrorMessage(msg);
            throw new RuntimeException(msg);
        }

        saga.getTransientMap().put("reservationConfirmed", true);
        log.info("Reservation confirmed for order {} with {} reservations",
                saga.getOrderId(), reservationIds.size());
    }
}
