package com.jacare.eletropecas.Manufacturer.Application;

import com.jacare.eletropecas.Manufacturer.Domain.Manufacturer;

import java.util.List;

public interface IManufacturerService {
    Manufacturer createManufacturer(Manufacturer manufacturer);
    List<Manufacturer> getAllManufacturers();
    Manufacturer getManufacturerById(Long id);
    Manufacturer updateManufacturer(Long id, Manufacturer manufacturer);
    void deleteManufacturer(Long id);
}
