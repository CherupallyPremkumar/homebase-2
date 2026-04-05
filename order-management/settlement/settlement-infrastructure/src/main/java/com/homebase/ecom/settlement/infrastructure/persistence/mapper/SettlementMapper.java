package com.homebase.ecom.settlement.infrastructure.persistence.mapper;

import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.model.SettlementActivityLog;
import com.homebase.ecom.settlement.model.SettlementAdjustment;
import com.homebase.ecom.settlement.infrastructure.persistence.entity.SettlementActivityLogEntity;
import com.homebase.ecom.settlement.infrastructure.persistence.entity.SettlementAdjustmentEntity;
import com.homebase.ecom.settlement.infrastructure.persistence.entity.SettlementEntity;
import com.homebase.ecom.shared.Money;

import java.util.stream.Collectors;

public class SettlementMapper {

    public SettlementEntity toEntity(Settlement model) {
        if (model == null) return null;
        SettlementEntity entity = new SettlementEntity();
        mapModelToEntity(model, entity);
        return entity;
    }

    public Settlement toModel(SettlementEntity entity) {
        if (entity == null) return null;
        Settlement model = new Settlement();

        // Base entity fields
        model.setId(entity.getId());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setCreatedBy(entity.getCreatedBy());
        model.setLastModifiedBy(entity.getLastModifiedBy());
        model.setVersion(entity.getVersion());

        // STM fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());

        // Business fields
        model.setDescription(entity.getDescription());
        model.setSupplierId(entity.getSupplierId());
        model.setOrderId(entity.getOrderId());
        model.setOrderAmount(entity.getOrderAmount());
        // Nullable money fields stored as Long (paise) in entity
        String currency = entity.getOrderAmount() != null ? entity.getOrderAmount().getCurrency() : null;
        model.setCurrency(currency);
        model.setCommissionAmount(longToMoney(entity.getCommissionAmountPaise(), currency));
        model.setPlatformFee(longToMoney(entity.getPlatformFeePaise(), currency));
        model.setNetAmount(longToMoney(entity.getNetAmountPaise(), currency));
        model.setSettlementPeriodStart(entity.getSettlementPeriodStart());
        model.setSettlementPeriodEnd(entity.getSettlementPeriodEnd());
        model.setDisbursementReference(entity.getDisbursementReference());
        model.setTenant(entity.tenant);

        // Changeset 004 fields
        model.setPaymentId(entity.getPaymentId());
        model.setSettlementNumber(entity.getSettlementNumber());
        model.setTaxAmount(longToMoney(entity.getTaxAmountPaise(), currency));
        model.setAdjustmentAmount(longToMoney(entity.getAdjustmentAmountPaise(), currency));
        model.setBankAccountId(entity.getBankAccountId());
        model.setDisbursementDate(entity.getDisbursementDate());
        model.setDisbursementMethod(entity.getDisbursementMethod());

        // Child collections
        if (entity.getAdjustments() != null) {
            model.setAdjustments(entity.getAdjustments().stream()
                    .map(this::toAdjustmentModel)
                    .collect(Collectors.toList()));
        }

        if (entity.getActivities() != null) {
            entity.getActivities().forEach(activityLogEntity -> {
                SettlementActivityLog activityLog = toActivityLogModel(activityLogEntity);
                model.addActivity(activityLog.activityName, activityLog.activityComment);
            });
        }

        return model;
    }

    /**
     * Merges updated JPA entity fields onto an existing JPA entity (for update scenarios).
     */
    public void mergeEntity(SettlementEntity existing, SettlementEntity updated) {
        if (updated == null || existing == null) return;

        existing.setDescription(updated.getDescription());
        existing.setSupplierId(updated.getSupplierId());
        existing.setOrderId(updated.getOrderId());
        existing.setOrderAmount(updated.getOrderAmount());
        existing.setCommissionAmountPaise(updated.getCommissionAmountPaise());
        existing.setPlatformFeePaise(updated.getPlatformFeePaise());
        existing.setNetAmountPaise(updated.getNetAmountPaise());
        existing.setSettlementPeriodStart(updated.getSettlementPeriodStart());
        existing.setSettlementPeriodEnd(updated.getSettlementPeriodEnd());
        existing.setDisbursementReference(updated.getDisbursementReference());
        existing.tenant = updated.tenant;

        existing.setPaymentId(updated.getPaymentId());
        existing.setSettlementNumber(updated.getSettlementNumber());
        existing.setTaxAmountPaise(updated.getTaxAmountPaise());
        existing.setAdjustmentAmountPaise(updated.getAdjustmentAmountPaise());
        existing.setBankAccountId(updated.getBankAccountId());
        existing.setDisbursementDate(updated.getDisbursementDate());
        existing.setDisbursementMethod(updated.getDisbursementMethod());

        existing.setCurrentState(updated.getCurrentState());
        existing.setStateEntryTime(updated.getStateEntryTime());

        existing.getAdjustments().clear();
        if (updated.getAdjustments() != null) {
            existing.getAdjustments().addAll(updated.getAdjustments());
        }

        existing.getActivities().clear();
        if (updated.getActivities() != null) {
            existing.getActivities().addAll(updated.getActivities());
        }
    }

    // --- Private mapping helpers ---

    private void mapModelToEntity(Settlement model, SettlementEntity entity) {
        entity.setId(model.getId());
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setLastModifiedBy(model.getLastModifiedBy());
        if (model.getVersion() != null) {
            entity.setVersion(model.getVersion());
        }

        entity.setCurrentState(model.getCurrentState());
        entity.setStateEntryTime(model.getStateEntryTime());

        entity.setDescription(model.getDescription());
        entity.setSupplierId(model.getSupplierId());
        entity.setOrderId(model.getOrderId());
        entity.setOrderAmount(model.getOrderAmount());
        // Nullable money fields: extract paise value or null
        entity.setCommissionAmountPaise(moneyToPaise(model.getCommissionAmount()));
        entity.setPlatformFeePaise(moneyToPaise(model.getPlatformFee()));
        entity.setNetAmountPaise(moneyToPaise(model.getNetAmount()));
        entity.setSettlementPeriodStart(model.getSettlementPeriodStart());
        entity.setSettlementPeriodEnd(model.getSettlementPeriodEnd());
        entity.setDisbursementReference(model.getDisbursementReference());
        entity.tenant = model.getTenant();

        entity.setPaymentId(model.getPaymentId());
        entity.setSettlementNumber(model.getSettlementNumber());
        entity.setTaxAmountPaise(moneyToPaise(model.getTaxAmount()));
        entity.setAdjustmentAmountPaise(moneyToPaise(model.getAdjustmentAmount()));
        entity.setBankAccountId(model.getBankAccountId());
        entity.setDisbursementDate(model.getDisbursementDate());
        entity.setDisbursementMethod(model.getDisbursementMethod());

        if (model.getAdjustments() != null) {
            entity.setAdjustments(model.getAdjustments().stream()
                    .map(this::toAdjustmentEntity)
                    .collect(Collectors.toList()));
        }

        if (model.obtainActivities() != null) {
            entity.setActivities(model.obtainActivities().stream()
                    .map(activityLog -> toActivityLogEntity((SettlementActivityLog) activityLog))
                    .collect(Collectors.toList()));
        }
    }

    /** Converts Money to Long paise value, null-safe. */
    private Long moneyToPaise(Money money) {
        return money != null ? money.getAmount() : null;
    }

    /** Converts Long paise value to Money, null-safe. */
    private Money longToMoney(Long paise, String currency) {
        if (paise == null) return null;
        return Money.of(paise, currency != null ? currency : "INR");
    }

    private SettlementAdjustmentEntity toAdjustmentEntity(SettlementAdjustment model) {
        if (model == null) return null;
        SettlementAdjustmentEntity entity = new SettlementAdjustmentEntity();
        entity.setId(model.getId());
        entity.setAmount(model.getAmount());
        entity.setReason(model.getReason());
        entity.setAdjustedBy(model.getAdjustedBy());
        entity.setAdjustedAt(model.getAdjustedAt());
        return entity;
    }

    private SettlementAdjustment toAdjustmentModel(SettlementAdjustmentEntity entity) {
        if (entity == null) return null;
        SettlementAdjustment model = new SettlementAdjustment();
        model.setId(entity.getId());
        model.setAmount(entity.getAmount());
        model.setReason(entity.getReason());
        model.setAdjustedBy(entity.getAdjustedBy());
        model.setAdjustedAt(entity.getAdjustedAt());
        return model;
    }

    private SettlementActivityLogEntity toActivityLogEntity(SettlementActivityLog model) {
        if (model == null) return null;
        SettlementActivityLogEntity entity = new SettlementActivityLogEntity();
        entity.setActivityName(model.activityName);
        entity.setActivitySuccess(model.activitySuccess);
        entity.setActivityComment(model.activityComment);
        return entity;
    }

    private SettlementActivityLog toActivityLogModel(SettlementActivityLogEntity entity) {
        if (entity == null) return null;
        SettlementActivityLog model = new SettlementActivityLog();
        model.activityName = entity.getActivityName();
        model.activitySuccess = entity.isActivitySuccess();
        model.activityComment = entity.getActivityComment();
        return model;
    }
}
