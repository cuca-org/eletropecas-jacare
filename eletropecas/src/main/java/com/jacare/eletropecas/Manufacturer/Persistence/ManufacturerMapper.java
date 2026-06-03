package com.jacare.eletropecas.Manufacturer.Persistence;

import com.jacare.eletropecas.Manufacturer.Domain.Manufacturer;

public class ManufacturerMapper {
    public static Manufacturer toDomain(ManufacturerEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Manufacturer(
                entity.getId(),
                entity.getName(),
                entity.getContactEmail()
        );
    }

    public static ManufacturerEntity toEntity(Manufacturer manufacturer) {
        if (manufacturer == null) {
            return null;
        }
        return new ManufacturerEntity(
                manufacturer.getId(),
                manufacturer.getName(),
                manufacturer.getContactEmail()
        );
    }
}
