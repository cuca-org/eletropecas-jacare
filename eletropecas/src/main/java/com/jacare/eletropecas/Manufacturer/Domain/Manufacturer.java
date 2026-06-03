package com.jacare.eletropecas.Manufacturer.Domain;

public class Manufacturer {
    private Long id;
    private String name;
    private String contactEmail;

    public Manufacturer() {}

    public Manufacturer(Long id, String name, String contactEmail) {
        this.id = id;
        this.name = name;
        this.contactEmail = contactEmail;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
}