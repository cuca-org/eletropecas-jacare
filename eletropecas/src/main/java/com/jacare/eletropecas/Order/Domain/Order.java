package com.jacare.eletropecas.Order.Domain;

import com.jacare.eletropecas.Part.Domain.Part;
import com.jacare.eletropecas.User.Domain.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id;

    // Dados do Eletrodoméstico
    private String applianceDescription;
    private String applianceBrand;
    private String serialNumber;
    private String defectReported;

    // Relacionamentos e Fluxo
    private User client;
    private OrderStatus status;

    // Prazos e Controle
    private LocalDate budgetDeadline;
    private LocalDate estimatedDelivery;

    // Financeiro
    private Double laborCost;
    
    // Peças necessárias para o conserto
    private List<Part> requiredParts = new ArrayList<>(); 

    public Order() {
        this.status = OrderStatus.BUDGET_PENDING;
    }

    public Order(Long id, String applianceDescription, String applianceBrand, String defectReported, User client) {
        this.id = id;
        this.applianceDescription = applianceDescription;
        this.applianceBrand = applianceBrand;
        this.defectReported = defectReported;
        this.client = client;
        this.status = OrderStatus.BUDGET_PENDING;
        this.laborCost = 0.0;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getApplianceDescription() { return applianceDescription; }
    public void setApplianceDescription(String applianceDescription) { this.applianceDescription = applianceDescription; }

    public String getApplianceBrand() { return applianceBrand; }
    public void setApplianceBrand(String applianceBrand) { this.applianceBrand = applianceBrand; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public String getDefeitoReported() { return defectReported; }
    public void setDefeitoReported(String defectReported) { this.defectReported = defectReported; }

    public User getClient() { return client; }
    public void setClient(User client) { this.client = client; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public LocalDate getBudgetDeadline() { return budgetDeadline; }
    public void setBudgetDeadline(LocalDate budgetDeadline) { this.budgetDeadline = budgetDeadline; }

    public LocalDate getEstimatedDelivery() { return estimatedDelivery; }
    public void setEstimatedDelivery(LocalDate estimatedDelivery) { this.estimatedDelivery = estimatedDelivery; }

    public Double getLaborCost() { return laborCost; }
    public void setLaborCost(Double laborCost) { this.laborCost = laborCost; }

    public List<Part> getRequiredParts() { return requiredParts; }
    public void setRequiredParts(List<Part> requiredParts) { this.requiredParts = requiredParts; }
}