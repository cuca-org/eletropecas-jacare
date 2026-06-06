package com.jacare.eletropecas.Manufacturer.Application;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jacare.eletropecas.Manufacturer.Domain.Manufacturer;
import com.jacare.eletropecas.Manufacturer.Persistence.ManufacturerEntity;
import com.jacare.eletropecas.Manufacturer.Persistence.ManufacturerMapper;
import com.jacare.eletropecas.Manufacturer.Persistence.ManufacturerRepository;

@ExtendWith(MockitoExtension.class)
class ManufacturerServiceTest {

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private ManufacturerService manufacturerService;

    @Test
    @DisplayName("Deve criar um fabricante com sucesso")
    void shouldCreateManufacturerSuccessfully() {
        Manufacturer manufacturer = new Manufacturer(
                1L,
                "Bosch",
                "contato@bosch.com"
        );

        when(manufacturerRepository.save(any(ManufacturerEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Manufacturer result = manufacturerService.createManufacturer(manufacturer);

        assertAll("Fabricante criado",
                () -> assertNotNull(result),
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Bosch", result.getName()),
                () -> assertEquals("contato@bosch.com", result.getContactEmail())
        );

        verify(manufacturerRepository, times(1)).save(any(ManufacturerEntity.class));
    }

    @Test
    @DisplayName("Deve retornar todos os fabricantes")
    void shouldReturnAllManufacturers() {
        Manufacturer manufacturer1 = new Manufacturer(
                1L,
                "Bosch",
                "contato@bosch.com"
        );

        Manufacturer manufacturer2 = new Manufacturer(
                2L,
                "LG",
                "suporte@lg.com"
        );

        when(manufacturerRepository.findAll()).thenReturn(List.of(
                ManufacturerMapper.toEntity(manufacturer1),
                ManufacturerMapper.toEntity(manufacturer2)
        ));

        List<Manufacturer> result = manufacturerService.getAllManufacturers();

        assertAll("Lista de fabricantes",
                () -> assertEquals(2, result.size()),
                () -> assertEquals("Bosch", result.get(0).getName()),
                () -> assertEquals("LG", result.get(1).getName())
        );

        verify(manufacturerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar fabricante pelo ID")
    void shouldReturnManufacturerById() {
        Manufacturer manufacturer = new Manufacturer(
                1L,
                "Bosch",
                "contato@bosch.com"
        );

        when(manufacturerRepository.findById(1L))
                .thenReturn(Optional.of(ManufacturerMapper.toEntity(manufacturer)));

        Manufacturer result = manufacturerService.getManufacturerById(1L);

        assertAll("Fabricante encontrado",
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Bosch", result.getName()),
                () -> assertEquals("contato@bosch.com", result.getContactEmail())
        );

        verify(manufacturerRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando fabricante não existir")
    void shouldThrowExceptionWhenManufacturerNotFound() {
        when(manufacturerRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> manufacturerService.getManufacturerById(1L)
        );

        assertEquals("Fabricante não encontrado com o ID: 1", exception.getMessage());

        verify(manufacturerRepository, times(1)).findById(1L);
        verify(manufacturerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar um fabricante com sucesso")
    void shouldUpdateManufacturerSuccessfully() {
        Manufacturer existingManufacturer = new Manufacturer(
                1L,
                "Bosch",
                "contato@bosch.com"
        );

        Manufacturer updatedManufacturer = new Manufacturer(
                1L,
                "Electrolux",
                "suporte@electrolux.com"
        );

        when(manufacturerRepository.findById(1L))
                .thenReturn(Optional.of(ManufacturerMapper.toEntity(existingManufacturer)));

        when(manufacturerRepository.save(any(ManufacturerEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Manufacturer result = manufacturerService.updateManufacturer(1L, updatedManufacturer);

        assertAll("Fabricante atualizado",
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Electrolux", result.getName()),
                () -> assertEquals("suporte@electrolux.com", result.getContactEmail())
        );

        verify(manufacturerRepository, times(1)).findById(1L);
        verify(manufacturerRepository, times(1)).save(any(ManufacturerEntity.class));
    }

    @Test
    @DisplayName("Deve excluir um fabricante existente")
    void shouldDeleteManufacturerSuccessfully() {
        Manufacturer manufacturer = new Manufacturer(
                1L,
                "Bosch",
                "contato@bosch.com"
        );

        when(manufacturerRepository.findById(1L))
                .thenReturn(Optional.of(ManufacturerMapper.toEntity(manufacturer)));

        manufacturerService.deleteManufacturer(1L);

        verify(manufacturerRepository, times(1)).findById(1L);
        verify(manufacturerRepository, times(1)).delete(any(ManufacturerEntity.class));
    }
}