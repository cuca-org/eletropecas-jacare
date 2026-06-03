package com.jacare.eletropecas.Order.Persistence;

import com.jacare.eletropecas.Order.Domain.OrderStatus;
import com.jacare.eletropecas.Part.Domain.Part;
import com.jacare.eletropecas.User.Domain.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String applianceDescription;
    private String applianceBrand;
    private String serialNumber;
    private String defectReported;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDate budgetDeadline;
    private LocalDate estimatedDelivery;
    private Double laborCost;

    @ManyToMany
    @JoinTable(
            name = "order_parts",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "part_id")
    )
    private List<Part> requiredParts = new ArrayList<>();

    public OrderEntity() {
        this.status = OrderStatus.BUDGET_PENDING;
    }

    public OrderEntity(Long id, String applianceDescription, String applianceBrand, String defectReported, User customer) {
        this.id = id;
        this.applianceDescription = applianceDescription;
        this.applianceBrand = applianceBrand;
        this.defectReported = defectReported;
        this.customer = customer;
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

    public User getcustomer() { return customer; }
    public void setcustomer(User customer) { this.customer = customer; }

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
