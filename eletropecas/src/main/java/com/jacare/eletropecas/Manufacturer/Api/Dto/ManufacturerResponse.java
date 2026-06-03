package com.jacare.eletropecas.Manufacturer.Api.Dto;

import com.jacare.eletropecas.Manufacturer.Domain.Manufacturer;

public record ManufacturerResponse(
        Long id,
        String name,
        String contactEmail
) {
    public static ManufacturerResponse toResponse(Manufacturer manufacturer) {
        if (manufacturer == null) return null;
        return new ManufacturerResponse(
                manufacturer.getId(),
                manufacturer.getName(),
                manufacturer.getContactEmail()
        );
    }
}