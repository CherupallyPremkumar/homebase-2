package com.homebase.ecom.promo.rule;

import com.homebase.ecom.promo.model.ActionType;
import com.homebase.ecom.shared.Money;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public final class Action implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID actionId;
    private final ActionType type;
    private final Map<String, Object> parameters;

    public Action(UUID actionId, ActionType type, Map<String, Object> parameters) {
        this.actionId = actionId;
        this.type = type;
        this.parameters = parameters;
    }

    public UUID getActionId() {
        return actionId;
    }

    public ActionType getType() {
        return type;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public static ActionBuilder builder() {
        return new ActionBuilder();
    }

    public static class ActionBuilder {
        private UUID actionId;
        private ActionType type;
        private Map<String, Object> parameters;

        public ActionBuilder actionId(UUID actionId) {
            this.actionId = actionId;
            return this;
        }

        public ActionBuilder type(ActionType type) {
            this.type = type;
            return this;
        }

        public ActionBuilder parameters(Map<String, Object> parameters) {
            this.parameters = parameters;
            return this;
        }

        public Action build() {
            return new Action(actionId, type, parameters);
        }
    }
}
