package com.homebase.ecom.batch;

import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class RazorpaySettlementDownloader {

    private static final Logger log = LoggerFactory.getLogger(RazorpaySettlementDownloader.class);

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public String downloadLatestSettlementCsv() throws org.json.JSONException {
        log.info("Downloading latest Razorpay payments via API...");
        String filePath = "razorpay-settlement-latest.csv";

        try {
            RazorpayClient client = new RazorpayClient(keyId, keySecret);
            Path path = Paths.get(filePath);

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                // Write CSV header
                writer.write("paymentId,amount,fee,net,currency,status\n");

                JSONObject params = new JSONObject();
                params.put("count", 100);

                List<Payment> payments = client.payments.fetchAll(params);

                for (Payment payment : payments) {
                    String paymentId = payment.get("id");

                    // Razorpay stores amounts in smallest currency unit (paise)
                    long amountPaise = payment.get("amount");
                    long feePaise = payment.get("fee") != null ? (Integer) payment.get("fee") : 0;
                    long taxPaise = payment.get("tax") != null ? (Integer) payment.get("tax") : 0;

                    BigDecimal amount = BigDecimal.valueOf(amountPaise).divide(BigDecimal.valueOf(100));
                    BigDecimal totalFee = BigDecimal.valueOf(feePaise + taxPaise).divide(BigDecimal.valueOf(100));
                    BigDecimal net = amount.subtract(totalFee);

                    String currency = payment.get("currency");
                    String status = payment.get("status");

                    writer.write(String.format("%s,%.2f,%.2f,%.2f,%s,%s\n",
                            paymentId, amount, totalFee, net, currency, status));
                }
            }

            log.info("Actual Razorpay settlement CSV saved to {}", path.toAbsolutePath());
            return filePath;

        } catch (IOException | RazorpayException e) {
            log.error("Failed to download settlement data from Razorpay", e);
            throw new RuntimeException("Failed to download Razorpay data", e);
        }
    }
}
