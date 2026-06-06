package com.jacare.eletropecas.Order.Domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jacare.eletropecas.User.Domain.User;

class OrderTest {

    @Test
    @DisplayName("Deve criar uma ordem com dados válidos")
    void shouldCreateOrderWithValidData() {

        User client = new User(
                1L,
                "André Silva",
                "andre@email.com",
                "123.456.789-00",
                "hash"
        );

        Order order = new Order(
                1L,
                "Geladeira",
                "Brastemp",
                "Não está gelando",
                client
        );

        assertAll("Propriedades da Ordem",
                () -> assertEquals(1L, order.getId()),
                () -> assertEquals("Geladeira", order.getApplianceDescription()),
                () -> assertEquals("Brastemp", order.getApplianceBrand()),
                () -> assertEquals("Não está gelando", order.getDefeitoReported()),
                () -> assertEquals(client, order.getClient())
        );
    }

    @Test
    @DisplayName("Deve iniciar com status BUDGET_PENDING")
    void shouldStartWithBudgetPendingStatus() {

        User client = new User(
                1L,
                "André Silva",
                "andre@email.com",
                "123.456.789-00",
                "hash"
        );

        Order order = new Order(
                1L,
                "Geladeira",
                "Brastemp",
                "Não está gelando",
                client
        );

        assertEquals(
                OrderStatus.BUDGET_PENDING,
                order.getStatus()
        );
    }

    @Test
    @DisplayName("Deve iniciar com custo de mão de obra zerado")
    void shouldStartWithZeroLaborCost() {

        User client = new User(
                1L,
                "André Silva",
                "andre@email.com",
                "123.456.789-00",
                "hash"
        );

        Order order = new Order(
                1L,
                "Geladeira",
                "Brastemp",
                "Não está gelando",
                client
        );

        assertEquals(
                0.0,
                order.getLaborCost()
        );
    }

    @Test
    @DisplayName("Deve criar ordem usando construtor padrão")
    void shouldCreateOrderUsingDefaultConstructor() {

        Order order = new Order();

        assertEquals(
                OrderStatus.BUDGET_PENDING,
                order.getStatus()
        );
    }

    @Test
    @DisplayName("Deve iniciar com lista de peças vazia")
    void shouldStartWithEmptyRequiredPartsList() {

        Order order = new Order();

        assertTrue(
                order.getRequiredParts().isEmpty()
        );
    }
}