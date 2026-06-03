package com.jacare.eletropecas.Manufacturer.Api;

import com.jacare.eletropecas.Manufacturer.Api.Dto.ManufacturerResponse;
import com.jacare.eletropecas.Manufacturer.Application.ManufacturerService;
import com.jacare.eletropecas.Manufacturer.Domain.Manufacturer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturers")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @PostMapping
    public ResponseEntity<ManufacturerResponse> create(@RequestBody Manufacturer manufacturer) {
        Manufacturer created = manufacturerService.createManufacturer(manufacturer);
        return ResponseEntity.status(HttpStatus.CREATED).body(ManufacturerResponse.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<ManufacturerResponse>> getAll() {
        List<ManufacturerResponse> responseList = manufacturerService.getAllManufacturers().stream()
                .map(ManufacturerResponse::toResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerResponse> getById(@PathVariable Long id) {
        Manufacturer manufacturer = manufacturerService.getManufacturerById(id);
        return ResponseEntity.ok(ManufacturerResponse.toResponse(manufacturer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManufacturerResponse> update(@PathVariable Long id, @RequestBody Manufacturer manufacturer) {
        Manufacturer updated = manufacturerService.updateManufacturer(id, manufacturer);
        return ResponseEntity.ok(ManufacturerResponse.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        manufacturerService.deleteManufacturer(id);
        return ResponseEntity.noContent().build();
    }
}