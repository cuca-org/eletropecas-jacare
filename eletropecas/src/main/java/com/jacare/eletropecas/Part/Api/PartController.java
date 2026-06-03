package com.jacare.eletropecas.Part.Api;

import com.jacare.eletropecas.Part.Api.Dto.PartResponse;
import com.jacare.eletropecas.Part.Application.PartService;
import com.jacare.eletropecas.Part.Domain.Part;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    @PostMapping
    public ResponseEntity<PartResponse> create(@RequestBody Part part) {
        Part created = partService.createPart(part);
        return ResponseEntity.status(HttpStatus.CREATED).body(PartResponse.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<PartResponse>> getAll() {
        List<PartResponse> responseList = partService.getAllParts().stream()
                .map(PartResponse::toResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartResponse> getById(@PathVariable Long id) {
        Part part = partService.getPartById(id);
        return ResponseEntity.ok(PartResponse.toResponse(part));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartResponse> update(@PathVariable Long id, @RequestBody Part part) {
        Part updated = partService.updatePart(id, part);
        return ResponseEntity.ok(PartResponse.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        partService.deletePart(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock/increase")
    public ResponseEntity<Void> increaseStock(@PathVariable Long id, @RequestParam int quantity) {
        partService.increaseStock(id, quantity);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/stock/decrease")
    public ResponseEntity<Void> decreaseStock(@PathVariable Long id, @RequestParam int quantity) {
        partService.decreaseStock(id, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/below-stock")
    public ResponseEntity<List<PartResponse>> getBelowStock(@RequestParam int minimumQuantity) {
        List<PartResponse> responseList = partService.getPartsBelowStock(minimumQuantity).stream()
                .map(PartResponse::toResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }
}