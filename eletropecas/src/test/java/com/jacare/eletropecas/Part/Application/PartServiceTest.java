package com.jacare.eletropecas.Part.Application;

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
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jacare.eletropecas.Manufacturer.Domain.Manufacturer;
import com.jacare.eletropecas.Part.Domain.Part;
import com.jacare.eletropecas.Part.Persistence.PartEntity;
import com.jacare.eletropecas.Part.Persistence.PartMapper;
import com.jacare.eletropecas.Part.Persistence.PartRepository;

@ExtendWith(MockitoExtension.class)
class PartServiceTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private PartService partService;

    private Manufacturer createManufacturer() {
        return new Manufacturer(1L, "Bosch", "contato@bosch.com");
    }

    private Part createPart() {
        return new Part(
                1L,
                "Filtro de Óleo",
                10,
                25.0,
                createManufacturer()
        );
    }

    @Test
    @DisplayName("Deve criar uma peça com sucesso")
    void shouldCreatePartSuccessfully() {
        Part part = createPart();

        when(partRepository.save(any(PartEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Part result = partService.createPart(part);

        assertAll("Peça criada",
                () -> assertNotNull(result),
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Filtro de Óleo", result.getDescription()),
                () -> assertEquals(10, result.getQuantityInStock()),
                () -> assertEquals(25.0, result.getSupplierPrice())
        );

        verify(partRepository, times(1)).save(any(PartEntity.class));
    }

    @Test
    @DisplayName("Deve retornar todas as peças cadastradas")
    void shouldReturnAllParts() {
        Part part1 = createPart();
        Part part2 = new Part(
                2L,
                "Pastilha de Freio",
                20,
                45.0,
                createManufacturer()
        );

        when(partRepository.findAll()).thenReturn(List.of(
                PartMapper.toEntity(part1),
                PartMapper.toEntity(part2)
        ));

        List<Part> result = partService.getAllParts();

        assertAll("Lista de peças",
                () -> assertEquals(2, result.size()),
                () -> assertEquals("Filtro de Óleo", result.get(0).getDescription()),
                () -> assertEquals("Pastilha de Freio", result.get(1).getDescription())
        );

        verify(partRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma peça pelo ID")
    void shouldReturnPartById() {
        Part part = createPart();

        when(partRepository.findById(1L))
                .thenReturn(Optional.of(PartMapper.toEntity(part)));

        Part result = partService.getPartById(1L);

        assertAll("Peça encontrada",
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Filtro de Óleo", result.getDescription()),
                () -> assertEquals(10, result.getQuantityInStock())
        );

        verify(partRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a peça não existir")
    void shouldThrowExceptionWhenPartNotFound() {
        when(partRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> partService.getPartById(1L)
        );

        assertEquals("Peça não encontrada com o ID: 1", exception.getMessage());

        verify(partRepository, times(1)).findById(1L);
        verify(partRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar uma peça com sucesso")
    void shouldUpdatePartSuccessfully() {
        Part existingPart = createPart();

        Part updatedPart = new Part(
                1L,
                "Filtro Premium",
                30,
                40.0,
                createManufacturer()
        );

        when(partRepository.findById(1L))
                .thenReturn(Optional.of(PartMapper.toEntity(existingPart)));

        when(partRepository.save(any(PartEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Part result = partService.updatePart(1L, updatedPart);

        assertAll("Peça atualizada",
                () -> assertEquals("Filtro Premium", result.getDescription()),
                () -> assertEquals(30, result.getQuantityInStock()),
                () -> assertEquals(40.0, result.getSupplierPrice())
        );

        verify(partRepository, times(1)).findById(1L);
        verify(partRepository, times(1)).save(any(PartEntity.class));
    }

    @Test
    @DisplayName("Deve excluir uma peça existente")
    void shouldDeletePartSuccessfully() {
        Part part = createPart();

        when(partRepository.findById(1L))
                .thenReturn(Optional.of(PartMapper.toEntity(part)));

        partService.deletePart(1L);

        verify(partRepository, times(1)).findById(1L);
        verify(partRepository, times(1)).delete(any(PartEntity.class));
    }

    @Test
    @DisplayName("Deve aumentar o estoque da peça")
    void shouldIncreaseStock() {
        Part part = createPart();

        when(partRepository.findById(1L))
                .thenReturn(Optional.of(PartMapper.toEntity(part)));

        when(partRepository.save(any(PartEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        partService.increaseStock(1L, 5);

        verify(partRepository, times(1)).findById(1L);
        verify(partRepository, times(1)).save(any(PartEntity.class));
    }

    @Test
    @DisplayName("Não deve permitir aumento de estoque com quantidade inválida")
    void shouldThrowExceptionWhenIncreasingStockWithInvalidQuantity() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> partService.increaseStock(1L, 0)
        );

        assertEquals("A quantidade deve ser maior que zero", exception.getMessage());

        verify(partRepository, never()).findById(anyLong());
        verify(partRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve diminuir o estoque da peça")
    void shouldDecreaseStockSuccessfully() {
        Part part = createPart();

        when(partRepository.findById(1L))
                .thenReturn(Optional.of(PartMapper.toEntity(part)));

        when(partRepository.save(any(PartEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        partService.decreaseStock(1L, 5);

        verify(partRepository, times(1)).findById(1L);
        verify(partRepository, times(1)).save(any(PartEntity.class));
    }

    @Test
    @DisplayName("Não deve permitir baixa de estoque com quantidade inválida")
    void shouldThrowExceptionWhenDecreaseStockWithInvalidQuantity() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> partService.decreaseStock(1L, -1)
        );

        assertEquals("A quantidade deve ser maior que zero", exception.getMessage());

        verify(partRepository, never()).findById(anyLong());
        verify(partRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando não houver estoque suficiente")
    void shouldThrowExceptionWhenStockIsInsufficient() {
        Part part = new Part(
                1L,
                "Filtro de Óleo",
                2,
                25.0,
                createManufacturer()
        );

        when(partRepository.findById(1L))
                .thenReturn(Optional.of(PartMapper.toEntity(part)));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> partService.decreaseStock(1L, 5)
        );

        assertEquals("Estoque insuficiente para a peça 1", exception.getMessage());

        verify(partRepository, times(1)).findById(1L);
        verify(partRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar apenas peças com estoque abaixo do mínimo")
    void shouldReturnPartsBelowStock() {
        Part lowStockPart = new Part(
                1L,
                "Parafuso",
                3,
                2.0,
                createManufacturer()
        );

        Part okStockPart = new Part(
                2L,
                "Filtro de Óleo",
                10,
                25.0,
                createManufacturer()
        );

        when(partRepository.findAll()).thenReturn(List.of(
                PartMapper.toEntity(lowStockPart),
                PartMapper.toEntity(okStockPart)
        ));

        List<Part> result = partService.getPartsBelowStock(5);

        assertAll("Peças abaixo do estoque mínimo",
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Parafuso", result.get(0).getDescription())
        );

        verify(partRepository, times(1)).findAll();
    }
}