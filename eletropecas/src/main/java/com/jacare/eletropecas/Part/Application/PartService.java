package com.jacare.eletropecas.Part.Application;

import com.jacare.eletropecas.Part.Domain.Part;
import com.jacare.eletropecas.Part.Persistence.PartEntity;
import com.jacare.eletropecas.Part.Persistence.PartMapper;
import com.jacare.eletropecas.Part.Persistence.PartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartService implements IPartService {
    private final PartRepository partRepository;

    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Override
    @Transactional
    public Part createPart(Part part) {
        PartEntity entity = PartMapper.toEntity(part);
        PartEntity saved = partRepository.save(entity);

        return PartMapper.toDomain(saved);
    }

    @Override
    public List<Part> getAllParts() {
        return partRepository.findAll()
                .stream()
                .map(PartMapper::toDomain)
                .toList();
    }

    @Override
    public Part getPartById(Long id) {
        PartEntity found = partRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Peça não encontrada com o ID: " + id));

        return PartMapper.toDomain(found);
    }

    @Override
    @Transactional
    public Part updatePart(Long id, Part updatedPart) {
        Part existingPart = getPartById(id);

        existingPart.setDescription(updatedPart.getDescription());
        existingPart.setQuantityInStock(updatedPart.getQuantityInStock());
        existingPart.setSupplierPrice(updatedPart.getSupplierPrice());
        existingPart.setManufacturer(updatedPart.getManufacturer());

        PartEntity entity = PartMapper.toEntity(existingPart);
        PartEntity saved = partRepository.save(entity);

        return PartMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void deletePart(Long id) {
        Part part = getPartById(id);

        PartEntity entity = PartMapper.toEntity(part);

        partRepository.delete(entity);
    }

    @Override
    @Transactional
    public void increaseStock(Long partId, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero");
        }

        Part part = getPartById(partId);

        part.setQuantityInStock(
                part.getQuantityInStock() + quantity
        );

        partRepository.save(
                PartMapper.toEntity(part)
        );
    }

    @Override
    @Transactional
    public void decreaseStock(Long partId, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero");
        }

        Part part = getPartById(partId);

        if (!part.hasAvailableStock(quantity)) {
            throw new IllegalStateException(
                    "Estoque insuficiente para a peça " + partId);
        }

        part.setQuantityInStock(
                part.getQuantityInStock() - quantity
        );

        partRepository.save(
                PartMapper.toEntity(part)
        );
    }

    @Override
    public List<Part> getPartsBelowStock(int minimumQuantity) {
        return partRepository.findAll()
                .stream()
                .map(PartMapper::toDomain)
                .filter(part ->
                        part.getQuantityInStock() < minimumQuantity)
                .toList();
    }
}
