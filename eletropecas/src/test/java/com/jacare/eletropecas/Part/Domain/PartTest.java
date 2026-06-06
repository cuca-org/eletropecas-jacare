package com.jacare.eletropecas.Part.Domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jacare.eletropecas.Manufacturer.Domain.Manufacturer;

class PartTest {

    @Test
    @DisplayName("Deve criar uma peça com dados válidos")
    void shouldCreatePartWithValidData() {
        Manufacturer manufacturer = new Manufacturer();

        Part part = new Part(
                1L,
                "Filtro de Óleo",
                50,
                25.90,
                manufacturer
        );

        assertAll("Propriedades da peça",
                () -> assertEquals(1L, part.getId()),
                () -> assertEquals("Filtro de Óleo", part.getDescription()),
                () -> assertEquals(50, part.getQuantityInStock()),
                () -> assertEquals(25.90, part.getSupplierPrice()),
                () -> assertEquals(manufacturer, part.getManufacturer())
        );
    }

    @Test
    @DisplayName("Deve retornar verdadeiro quando houver estoque suficiente")
    void shouldReturnTrueWhenStockIsAvailable() {
        Part part = new Part(
                1L,
                "Filtro de Óleo",
                50,
                25.90,
                null
        );

        assertTrue(part.hasAvailableStock(10));
    }

    @Test
    @DisplayName("Deve retornar verdadeiro quando quantidade solicitada for igual ao estoque")
    void shouldReturnTrueWhenRequestedAmountEqualsStock() {
        Part part = new Part(
                1L,
                "Filtro de Óleo",
                50,
                25.90,
                null
        );

        assertTrue(part.hasAvailableStock(50));
    }

    @Test
    @DisplayName("Deve retornar falso quando não houver estoque suficiente")
    void shouldReturnFalseWhenStockIsInsufficient() {
        Part part = new Part(
                1L,
                "Filtro de Óleo",
                50,
                25.90,
                null
        );

        assertFalse(part.hasAvailableStock(51));
    }

    @Test
    @DisplayName("Deve permitir alterar a quantidade em estoque")
    void shouldUpdateQuantityInStock() {
        Part part = new Part();

        part.setQuantityInStock(100);

        assertEquals(100, part.getQuantityInStock());
    }
}