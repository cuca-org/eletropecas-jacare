package com.jacare.eletropecas.Order.Application;

import com.jacare.eletropecas.Order.Domain.Order;
import com.jacare.eletropecas.Order.Domain.OrderStatus;
import com.jacare.eletropecas.Part.Domain.Part;

import java.time.LocalDate;
import java.util.List;

public interface IOrderService {
    // Operações CRUD
    Order createOrder(Order order);
    List<Order> getAllOrders();
    Order getOrderById(Long id);
    Order updateOrder(Long id, Order order);
    void deleteOrder(Long id);

    // Fluxo de orçamento
    Order approveBudget(Long orderId);
    Order cancelOrder(Long orderId);

    // Fluxo da manutenção
    Order startRepair(Long orderId);
    Order markAsReady(Long orderId);
    Order deliverOrder(Long orderId);

    // Controle de peças
    Order addRequiredPart(Long orderId, Part part);
    Order removeRequiredPart(Long orderId, Long partId);

    // Custos
    Double calculateMaterialCost(Long orderId);
    Double calculateTotalCost(Long orderId);

    // Prazos
    Order defineBudgetDeadline(Long orderId, LocalDate deadline);
    Order defineEstimatedDelivery(Long orderId, LocalDate estimatedDelivery);

    // Consulta de andamento
    OrderStatus getOrderStatus(Long orderId);
}
