package com.jacare.eletropecas.Order.Persistence;

import com.jacare.eletropecas.Order.Domain.Order;
import com.jacare.eletropecas.Part.Persistence.PartMapper;
import com.jacare.eletropecas.User.Persistence.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderEntity toEntity(Order order) {
        if (order == null) {
            return null;
        }

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(order.getId());
        orderEntity.setApplianceDescription(order.getApplianceDescription());
        orderEntity.setApplianceBrand(order.getApplianceBrand());
        orderEntity.setSerialNumber(order.getSerialNumber());
        orderEntity.setDefeitoReported(order.getDefeitoReported());
        orderEntity.setcustomer(UserMapper.toEntity(order.getClient()));
        orderEntity.setStatus(order.getStatus());
        orderEntity.setBudgetDeadline(order.getBudgetDeadline());
        orderEntity.setEstimatedDelivery(order.getEstimatedDelivery());
        orderEntity.setLaborCost(order.getLaborCost());

        // Evita compartilhar a mesma referência de lista mutável diretamente se necessário
        if (order.getRequiredParts() != null) {
            orderEntity.setRequiredParts(new ArrayList<>(order.getRequiredParts()
                    .stream()
                    .map(PartMapper::toEntity)
                    .toList()
            ));
        }

        return orderEntity;
    }

    public static Order toDomain(OrderEntity orderEntity) {
        if (orderEntity == null) {
            return null;
        }

        Order order = new Order();
        order.setId(orderEntity.getId());
        order.setApplianceDescription(orderEntity.getApplianceDescription());
        order.setApplianceBrand(orderEntity.getApplianceBrand());
        order.setSerialNumber(orderEntity.getSerialNumber());
        order.setDefeitoReported(orderEntity.getDefeitoReported());
        order.setClient(UserMapper.toDomain(orderEntity.getcustomer()));
        order.setStatus(orderEntity.getStatus());
        order.setBudgetDeadline(orderEntity.getBudgetDeadline());
        order.setEstimatedDelivery(orderEntity.getEstimatedDelivery());
        order.setLaborCost(orderEntity.getLaborCost());

        if (orderEntity.getRequiredParts() != null) {
            order.setRequiredParts(new ArrayList<>(orderEntity.getRequiredParts()
                    .stream()
                    .map(PartMapper::toDomain)
                    .toList()
            ));
        }

        return order;
    }
}
