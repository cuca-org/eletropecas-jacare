package com.jacare.eletropecas.Part.Api.Dto;

import com.jacare.eletropecas.Manufacturer.Api.Dto.ManufacturerResponse;
import com.jacare.eletropecas.Part.Domain.Part;

public record PartResponse(
        Long id,
        String description,
        Integer quantityInStock,
        Double supplierPrice,
        ManufacturerResponse manufacturer
) {
    public static PartResponse toResponse(Part part) {
        if (part == null) return null;
        return new PartResponse(
                part.getId(),
                part.getDescription(),
                part.getQuantityInStock(),
                part.getSupplierPrice(),
                ManufacturerResponse.toResponse(part.getManufacturer())
        );
    }
}