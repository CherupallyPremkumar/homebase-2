package com.homebase.ecom.batch;

import com.stripe.exception.StripeException;
import com.stripe.model.BalanceTransaction;
import com.stripe.model.BalanceTransactionCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeSettlementDownloader {

    private static final Logger log = LoggerFactory.getLogger(StripeSettlementDownloader.class);

    /**
     * Connects to Stripe and fetches the latest Balance Transactions to generate
     * a settlement CSV file for the batch reconciliation job.
     */
    public String downloadLatestSettlementCsv() {
        log.info("Downloading latest Stripe settlement report via API...");
        String filePath = "stripe-settlement-latest.csv";

        try {
            Path path = Paths.get(filePath);
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                // Write CSV header
                writer.write("chargeId,amount,fee,net,currency,status\n");

                Map<String, Object> params = new HashMap<>();
                params.put("limit", 100); // Fetch latest 100 for batch reconciliation demo
                params.put("type", "charge"); // Only reconcile charges for now

                BalanceTransactionCollection transactions = BalanceTransaction.list(params);

                for (BalanceTransaction txn : transactions.getData()) {
                    String chargeId = txn.getSource();
                    if (chargeId == null || !chargeId.startsWith("ch_")) {
                        continue; // Skip if not a charge
                    }

                    // Stripe stores amounts in smallest currency unit (e.g., paise for INR)
                    BigDecimal amount = BigDecimal.valueOf(txn.getAmount()).divide(BigDecimal.valueOf(100));
                    BigDecimal fee = BigDecimal.valueOf(txn.getFee()).divide(BigDecimal.valueOf(100));
                    BigDecimal net = BigDecimal.valueOf(txn.getNet()).divide(BigDecimal.valueOf(100));
                    String currency = txn.getCurrency().toUpperCase();
                    String status = "succeeded"; // Balance transactions for charges usually imply success

                    writer.write(String.format("%s,%.2f,%.2f,%.2f,%s,%s\n",
                            chargeId, amount, fee, net, currency, status));
                }
            }

            log.info("Actual Stripe settlement CSV saved to {}", path.toAbsolutePath());
            return filePath;

        } catch (IOException | StripeException e) {
            log.error("Failed to download/create settlement CSV from Stripe API", e);
            throw new RuntimeException("Failed to download settlement CSV", e);
        }
    }
}
