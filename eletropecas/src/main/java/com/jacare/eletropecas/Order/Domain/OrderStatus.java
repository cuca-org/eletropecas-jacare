package com.jacare.eletropecas.Order.Domain;

public enum OrderStatus {
    // Aguardando Orçamento
    BUDGET_PENDING,

    // Orçamento Autorizado pelo Cliente
    BUDGET_APPROVED,

    // Em manutenção (distribuído ao técnico)
    IN_REPAIR,

    // Pronto para retirada
    READY,

    // Entregue ao cliente / Finalizado
    DELIVERED,

    // Recusado/Cancelado
    CANCELLED
}