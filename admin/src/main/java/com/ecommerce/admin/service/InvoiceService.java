package com.ecommerce.admin.service;

import com.ecommerce.shared.domain.Customer;
import com.ecommerce.shared.domain.Order;
import com.ecommerce.shared.domain.OrderItem;
import com.ecommerce.customer.repository.CustomerRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

/**
 * GST-compliant invoice PDF generator for Indian e-commerce.
 * Uses OpenPDF (free LGPL library).
 */
@Service
public class InvoiceService {

    private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    @Value("${invoice.seller.name:HomeMade E-Commerce Pvt. Ltd.}")
    private String sellerName;

    @Value("${invoice.seller.gstin:NOT_CONFIGURED}")
    private String sellerGstin;

    @Value("${invoice.seller.address:India}")
    private String sellerAddress;

    @Value("${invoice.seller.pan:NOT_CONFIGURED}")
    private String sellerPan;

    @Value("${invoice.seller.state:Karnataka}")
    private String sellerState;

    @Value("${invoice.seller.state-code:29}")
    private String sellerStateCode;

    private final CustomerRepository customerRepository;

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(30, 30, 80));
    private static final Font HEADER_FONT = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
    private static final Font LABEL_FONT = new Font(Font.HELVETICA, 9, Font.BOLD, Color.DARK_GRAY);
    private static final Font VALUE_FONT = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);
    private static final Font SMALL_FONT = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.GRAY);
    private static final Font TOTAL_FONT = new Font(Font.HELVETICA, 11, Font.BOLD, new Color(30, 30, 80));

    public InvoiceService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void generateInvoice(Order order, OutputStream outputStream) {
        try {
            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Title
            Paragraph title = new Paragraph("TAX INVOICE", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Invoice metadata
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String invoiceNo = "INV-" + order.getId().substring(0, 8).toUpperCase();

            PdfPTable metaTable = new PdfPTable(2);
            metaTable.setWidthPercentage(100);
            addMetaRow(metaTable, "Invoice No:", invoiceNo);
            addMetaRow(metaTable, "Invoice Date:", order.getCreatedAt().format(fmt));
            addMetaRow(metaTable, "Order ID:", order.getId());
            addMetaRow(metaTable, "Place of Supply:", sellerState + " (" + sellerStateCode + ")");
            document.add(metaTable);
            document.add(new Paragraph(" "));

            // Seller & Buyer info
            PdfPTable partyTable = new PdfPTable(2);
            partyTable.setWidthPercentage(100);
            partyTable.setWidths(new float[] { 1, 1 });

            // Seller
            PdfPCell sellerCell = new PdfPCell();
            sellerCell.setPadding(8);
            sellerCell.setBorderColor(Color.LIGHT_GRAY);
            sellerCell.addElement(new Paragraph("Seller Details", LABEL_FONT));
            sellerCell.addElement(new Paragraph(sellerName, VALUE_FONT));
            sellerCell.addElement(new Paragraph("GSTIN: " + sellerGstin, VALUE_FONT));
            sellerCell.addElement(new Paragraph("PAN: " + sellerPan, VALUE_FONT));
            sellerCell.addElement(new Paragraph(sellerAddress, SMALL_FONT));
            partyTable.addCell(sellerCell);

            // Buyer
            PdfPCell buyerCell = new PdfPCell();
            buyerCell.setPadding(8);
            buyerCell.setBorderColor(Color.LIGHT_GRAY);
            buyerCell.addElement(new Paragraph("Buyer Details", LABEL_FONT));

            Customer customer = customerRepository.findById(order.getCustomerId()).orElse(null);
            if (customer != null) {
                String buyerName = (customer.getFirstName() != null ? customer.getFirstName() : "")
                        + " " + (customer.getLastName() != null ? customer.getLastName() : "");
                buyerCell.addElement(new Paragraph(buyerName.trim(), VALUE_FONT));
                buyerCell.addElement(new Paragraph("Email: " + customer.getEmail(), VALUE_FONT));
            } else {
                buyerCell.addElement(new Paragraph("Customer ID: " + order.getCustomerId(), VALUE_FONT));
            }
            partyTable.addCell(buyerCell);
            document.add(partyTable);
            document.add(new Paragraph(" "));

            // Items table
            PdfPTable itemsTable = new PdfPTable(5);
            itemsTable.setWidthPercentage(100);
            itemsTable.setWidths(new float[] { 3, 1, 1.5f, 1.5f, 1.5f });

            // Header
            Color headerBg = new Color(30, 30, 80);
            addHeaderCell(itemsTable, "Item", headerBg);
            addHeaderCell(itemsTable, "Qty", headerBg);
            addHeaderCell(itemsTable, "Unit Price (₹)", headerBg);
            addHeaderCell(itemsTable, "GST (₹)", headerBg);
            addHeaderCell(itemsTable, "Total (₹)", headerBg);

            BigDecimal totalTaxableValue = BigDecimal.ZERO;

            if (order.getItems() != null) {
                for (OrderItem item : order.getItems()) {
                    BigDecimal itemTotal = item.getTotalPrice();
                    // Approximate GST breakdown per item (proportional)
                    BigDecimal itemGst = order.getTotalAmount().compareTo(BigDecimal.ZERO) > 0
                            ? order.getTaxAmount().multiply(itemTotal).divide(
                                    order.getTotalAmount().subtract(order.getTaxAmount())
                                            .subtract(order.getShippingAmount()),
                                    2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;

                    addValueCell(itemsTable, item.getProductId());
                    addValueCell(itemsTable, String.valueOf(item.getQuantity()));
                    addValueCell(itemsTable, formatInr(item.getUnitPrice()));
                    addValueCell(itemsTable, formatInr(itemGst));
                    addValueCell(itemsTable, formatInr(itemTotal));

                    totalTaxableValue = totalTaxableValue.add(itemTotal);
                }
            }

            document.add(itemsTable);
            document.add(new Paragraph(" "));

            // Tax breakdown
            BigDecimal cgst = order.getTaxAmount().divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
            BigDecimal sgst = order.getTaxAmount().subtract(cgst);

            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(50);
            summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            addSummaryRow(summaryTable, "Taxable Value:", formatInr(totalTaxableValue));
            addSummaryRow(summaryTable, "CGST:", formatInr(cgst));
            addSummaryRow(summaryTable, "SGST:", formatInr(sgst));
            addSummaryRow(summaryTable, "Shipping:", formatInr(order.getShippingAmount()));

            // Total row
            PdfPCell totalLabel = new PdfPCell(new Phrase("TOTAL:", TOTAL_FONT));
            totalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalLabel.setPadding(6);
            totalLabel.setBorderColor(new Color(30, 30, 80));
            totalLabel.setBorderWidthTop(2);
            summaryTable.addCell(totalLabel);

            PdfPCell totalValue = new PdfPCell(new Phrase("₹ " + formatInr(order.getTotalAmount()), TOTAL_FONT));
            totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalValue.setPadding(6);
            totalValue.setBorderColor(new Color(30, 30, 80));
            totalValue.setBorderWidthTop(2);
            summaryTable.addCell(totalValue);

            document.add(summaryTable);
            document.add(new Paragraph(" "));

            // Footer
            Paragraph footer = new Paragraph(
                    "This is a computer-generated invoice and does not require a physical signature.",
                    SMALL_FONT);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            log.info("Invoice generated for order: {}", order.getId());
        } catch (Exception e) {
            log.error("Failed to generate invoice for order {}: {}", order.getId(), e.getMessage());
            throw new RuntimeException("Invoice generation failed", e);
        }
    }

    private void addMetaRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, LABEL_FONT));
        labelCell.setBorder(0);
        labelCell.setPadding(3);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, VALUE_FONT));
        valueCell.setBorder(0);
        valueCell.setPadding(3);
        table.addCell(valueCell);
    }

    private void addHeaderCell(PdfPTable table, String text, Color bg) {
        PdfPCell cell = new PdfPCell(new Phrase(text, HEADER_FONT));
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6);
        table.addCell(cell);
    }

    private void addValueCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, VALUE_FONT));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        cell.setBorderColor(Color.LIGHT_GRAY);
        table.addCell(cell);
    }

    private void addSummaryRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, LABEL_FONT));
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        labelCell.setBorder(0);
        labelCell.setPadding(4);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase("₹ " + value, VALUE_FONT));
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setBorder(0);
        valueCell.setPadding(4);
        table.addCell(valueCell);
    }

    private String formatInr(BigDecimal amount) {
        return amount != null ? amount.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00";
    }
}
