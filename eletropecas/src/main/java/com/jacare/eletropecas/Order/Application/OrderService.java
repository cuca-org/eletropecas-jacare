package com.jacare.eletropecas.Order.Application;

import com.jacare.eletropecas.Order.Domain.Order;
import com.jacare.eletropecas.Order.Domain.OrderStatus;
import com.jacare.eletropecas.Order.Persistence.OrderEntity;
import com.jacare.eletropecas.Order.Persistence.OrderMapper;
import com.jacare.eletropecas.Order.Persistence.OrderRepository;
import com.jacare.eletropecas.Part.Domain.Part;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        OrderEntity entity = OrderMapper.toEntity(order);
        OrderEntity saved = orderRepository.save(entity);

        return OrderMapper.toDomain(saved);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toDomain)
                .toList();
    }

    @Override
    public Order getOrderById(Long id) {
        OrderEntity found = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Ordem de serviço não encontrada: " + id));

        return OrderMapper.toDomain(found);
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, Order updatedOrder) {

        Order existing = getOrderById(id);

        existing.setApplianceDescription(
                updatedOrder.getApplianceDescription());

        existing.setApplianceBrand(
                updatedOrder.getApplianceBrand());

        existing.setSerialNumber(
                updatedOrder.getSerialNumber());

        existing.setDefeitoReported(
                updatedOrder.getDefeitoReported());

        existing.setClient(
                updatedOrder.getClient());

        existing.setLaborCost(
                updatedOrder.getLaborCost());

        existing.setBudgetDeadline(
                updatedOrder.getBudgetDeadline());

        existing.setEstimatedDelivery(
                updatedOrder.getEstimatedDelivery());

        existing.setRequiredParts(
                updatedOrder.getRequiredParts());

        OrderEntity saved = orderRepository.save(
                OrderMapper.toEntity(existing));

        return OrderMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {

        Order order = getOrderById(id);

        orderRepository.delete(
                OrderMapper.toEntity(order));
    }

    @Override
    @Transactional
    public Order approveBudget(Long orderId) {

        Order order = getOrderById(orderId);

        order.setStatus(OrderStatus.BUDGET_APPROVED);

        OrderEntity saved = orderRepository.save(
                OrderMapper.toEntity(order));

        return OrderMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public Order cancelOrder(Long orderId) {

        Order order = getOrderById(orderId);

        order.setStatus(OrderStatus.CANCELLED);

        OrderEntity saved = orderRepository.save(
                OrderMapper.toEntity(order));

        return OrderMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public Order startRepair(Long orderId) {

        Order order = getOrderById(orderId);

        order.setStatus(OrderStatus.IN_REPAIR);

        OrderEntity saved = orderRepository.save(
                OrderMapper.toEntity(order));

        return OrderMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public Order markAsReady(Long orderId) {

        Order order = getOrderById(orderId);

        order.setStatus(OrderStatus.READY);

        OrderEntity saved = orderRepository.save(
                OrderMapper.toEntity(order));

        return OrderMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public Order deliverOrder(Long orderId) {

        Order order = getOrderById(orderId);

        order.setStatus(OrderStatus.DELIVERED);

        OrderEntity saved = orderRepository.save(
                OrderMapper.toEntity(order));

        return OrderMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public Order addRequiredPart(Long orderId, Part part) {

        Order order = getOrderById(orderId);

        order.getRequiredParts().add(part);

        OrderEntity saved = orderRepository.save(
                OrderMapper.toEntity(order));

        return OrderMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public Order removeRequiredPart(Long orderId, Long partId) {

        Order order = getOrderById(orderId);

        order.getRequiredParts()
                .removeIf(part ->
                        part.getId().equals(partId));

        OrderEntity saved = orderRepository.save(
                OrderMapper.toEntity(order));

        return OrderMapper.toDomain(saved);
    }

    @Override
    public Double calculateMaterialCost(Long orderId) {

        Order order = getOrderById(orderId);

        return order.getRequiredParts()
                .stream()
                .mapToDouble(Part::getSupplierPrice)
                .sum();
    }

    @Override
    public Double calculateTotalCost(Long orderId) {

        Order order = getOrderById(orderId);

        return calculateMaterialCost(orderId)
                + order.getLaborCost();
    }

    @Override
    @Transactional
    public Order defineBudgetDeadline(
            Long orderId,
            LocalDate deadline) {

        Order order = getOrderById(orderId);

        order.setBudgetDeadline(deadline);

        OrderEntity saved = orderRepository.save(
                OrderMapper.toEntity(order));

        return OrderMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public Order defineEstimatedDelivery(
            Long orderId,
            LocalDate estimatedDelivery) {

        Order order = getOrderById(orderId);

        order.setEstimatedDelivery(
                estimatedDelivery);

        OrderEntity saved = orderRepository.save(
                OrderMapper.toEntity(order));

        return OrderMapper.toDomain(saved);
    }

    @Override
    public OrderStatus getOrderStatus(Long orderId) {

        return getOrderById(orderId)
                .getStatus();
    }
}