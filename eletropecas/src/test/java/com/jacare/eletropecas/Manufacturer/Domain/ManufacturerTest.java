package com.jacare.eletropecas.Manufacturer.Domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ManufacturerTest {

    @Test
    @DisplayName("Deve criar um fabricante com dados válidos")
    void shouldCreateManufacturerWithValidData() {

        Manufacturer manufacturer = new Manufacturer(
                1L,
                "Bosch",
                "contato@bosch.com"
        );

        assertAll("Propriedades do fabricante",
                () -> assertEquals(1L, manufacturer.getId()),
                () -> assertEquals("Bosch", manufacturer.getName()),
                () -> assertEquals("contato@bosch.com", manufacturer.getContactEmail())
        );
    }

    @Test
    @DisplayName("Deve alterar o nome do fabricante")
    void shouldUpdateManufacturerName() {

        Manufacturer manufacturer = new Manufacturer(
                1L,
                "Bosch",
                "contato@bosch.com"
        );

        manufacturer.setName("Electrolux");

        assertEquals(
                "Electrolux",
                manufacturer.getName()
        );
    }

    @Test
    @DisplayName("Deve alterar o email de contato")
    void shouldUpdateManufacturerEmail() {

        Manufacturer manufacturer = new Manufacturer(
                1L,
                "Bosch",
                "contato@bosch.com"
        );

        manufacturer.setContactEmail(
                "suporte@electrolux.com"
        );

        assertEquals(
                "suporte@electrolux.com",
                manufacturer.getContactEmail()
        );
    }

    @Test
    @DisplayName("Deve alterar o ID do fabricante")
    void shouldUpdateManufacturerId() {

        Manufacturer manufacturer = new Manufacturer(
                1L,
                "Bosch",
                "contato@bosch.com"
        );

        manufacturer.setId(2L);

        assertEquals(
                2L,
                manufacturer.getId()
        );
    }

    @Test
    @DisplayName("Deve permitir valores nulos")
    void shouldAllowNullValues() {

        Manufacturer manufacturer = new Manufacturer(
                null,
                null,
                null
        );

        assertAll(
                () -> assertNull(manufacturer.getId()),
                () -> assertNull(manufacturer.getName()),
                () -> assertNull(manufacturer.getContactEmail())
        );
    }
}