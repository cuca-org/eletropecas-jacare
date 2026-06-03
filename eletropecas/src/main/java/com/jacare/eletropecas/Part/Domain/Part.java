package com.jacare.eletropecas.Part.Domain;

import com.jacare.eletropecas.Manufacturer.Domain.Manufacturer;

public class Part {
    private Long id;
    private String description;
    private Integer quantityInStock;
    private Double supplierPrice;
    private Manufacturer manufacturer;

    public Part() {}

    public Part(Long id, String description, Integer quantityInStock, Double supplierPrice, Manufacturer manufacturer) {
        this.id = id;
        this.description = description;
        this.quantityInStock = quantityInStock;
        this.supplierPrice = supplierPrice;
        this.manufacturer = manufacturer;
    }

    // Método para apoiar a lógica de negócio de baixa no estoque
    public boolean hasAvailableStock(int amount) {
        return this.quantityInStock >= amount;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getQuantityInStock() { return quantityInStock; }
    public void setQuantityInStock(Integer quantityInStock) { this.quantityInStock = quantityInStock; }

    public Double getSupplierPrice() { return supplierPrice; }
    public void setSupplierPrice(Double supplierPrice) { this.supplierPrice = supplierPrice; }

    public Manufacturer getManufacturer() { return manufacturer; }
    public void setManufacturer(Manufacturer manufacturer) { this.manufacturer = manufacturer; }
}