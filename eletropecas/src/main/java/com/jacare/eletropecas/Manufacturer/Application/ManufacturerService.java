package com.jacare.eletropecas.Manufacturer.Application;

import com.jacare.eletropecas.Manufacturer.Domain.Manufacturer;
import com.jacare.eletropecas.Manufacturer.Persistence.ManufacturerEntity;
import com.jacare.eletropecas.Manufacturer.Persistence.ManufacturerMapper;
import com.jacare.eletropecas.Manufacturer.Persistence.ManufacturerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerService implements IManufacturerService {

    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerService(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    @Transactional
    public Manufacturer createManufacturer(Manufacturer manufacturer) {
        ManufacturerEntity entity = ManufacturerMapper.toEntity(manufacturer);
        ManufacturerEntity saved = manufacturerRepository.save(entity);
        return ManufacturerMapper.toDomain(saved);
    }

    @Override
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll()
                .stream()
                .map(ManufacturerMapper::toDomain)
                .toList();
    }

    @Override
    public Manufacturer getManufacturerById(Long id) {
        ManufacturerEntity found = manufacturerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fabricante não encontrado com o ID: " + id));

        return ManufacturerMapper.toDomain(found);
    }

    @Override
    @Transactional
    public Manufacturer updateManufacturer(Long id, Manufacturer updatedManufacturer) {
        Manufacturer existingManufacturer = getManufacturerById(id);

        existingManufacturer.setName(updatedManufacturer.getName());
        existingManufacturer.setContactEmail(updatedManufacturer.getContactEmail());

        ManufacturerEntity entity = ManufacturerMapper.toEntity(existingManufacturer);
        ManufacturerEntity saved = manufacturerRepository.save(entity);

        return ManufacturerMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void deleteManufacturer(Long id) {
        Manufacturer manufacturer = getManufacturerById(id);
        ManufacturerEntity entity = ManufacturerMapper.toEntity(manufacturer);
        manufacturerRepository.delete(entity);
    }
}