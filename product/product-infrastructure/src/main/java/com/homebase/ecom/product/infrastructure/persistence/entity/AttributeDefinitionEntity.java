package com.homebase.ecom.product.infrastructure.persistence.entity;

import com.homebase.ecom.product.domain.model.AttributeDefinition.InputType;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attribute_definitions")
public class AttributeDefinitionEntity {
    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "input_type")
    private InputType inputType;

    private boolean filterable;
    private boolean searchable;
    private boolean required;

    @Column(name = "display_order")
    private int displayOrder;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "attribute_id")
    private List<AttributeOptionEntity> options = new ArrayList<>();

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public InputType getInputType() { return inputType; }
    public void setInputType(InputType inputType) { this.inputType = inputType; }

    public boolean isFilterable() { return filterable; }
    public void setFilterable(boolean filterable) { this.filterable = filterable; }

    public boolean isSearchable() { return searchable; }
    public void setSearchable(boolean searchable) { this.searchable = searchable; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }

    public List<AttributeOptionEntity> getOptions() { return options; }
    public void setOptions(List<AttributeOptionEntity> options) { this.options = options; }
}
