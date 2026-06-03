package com.jacare.eletropecas.Part.Persistence;

import com.jacare.eletropecas.Part.Domain.Part;

public class PartMapper {
    public static Part toDomain(PartEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Part(
                entity.getId(),
                entity.getDescription(),
                entity.getQuantityInStock(),
                entity.getSupplierPrice(),
                entity.getManufacturer()
        );
    }

    public static PartEntity toEntity(Part part) {
        if (part == null) {
            return null;
        }
        return new PartEntity(
                part.getId(),
                part.getDescription(),
                part.getQuantityInStock(),
                part.getSupplierPrice(),
                part.getManufacturer()
        );
    }
}
