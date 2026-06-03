package com.jacare.eletropecas.Part.Application;

import com.jacare.eletropecas.Part.Domain.Part;

import java.util.List;

public interface IPartService {
    // Operações CRUD
    Part createPart(Part part);
    List<Part> getAllParts();
    Part getPartById(Long id);
    Part updatePart(Long id, Part part);
    void deletePart(Long id);

    // Estoque
    void increaseStock(Long partId, int quantity);
    void decreaseStock(Long partId, int quantity);
    List<Part> getPartsBelowStock(int minimumQuantity);
}
