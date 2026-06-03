package com.jacare.eletropecas.Order.Api.Dto;

import com.jacare.eletropecas.Order.Domain.Order;
import com.jacare.eletropecas.Order.Domain.OrderStatus;
import com.jacare.eletropecas.Part.Api.Dto.PartResponse;
import com.jacare.eletropecas.User.Api.Dto.UserResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record OrderResponse(
        Long id,
        String applianceDescription,
        String applianceBrand,
        String serialNumber,
        String defectReported,
        UserResponse client,
        OrderStatus status,
        LocalDate budgetDeadline,
        LocalDate estimatedDelivery,
        Double laborCost,
        List<PartResponse> requiredParts
) {
    public static OrderResponse toResponse(Order order) {
        if (order == null) return null;

        List<PartResponse> partsResponse = order.getRequiredParts() != null
                ? order.getRequiredParts().stream().map(PartResponse::toResponse).toList()
                : new ArrayList<>();

        return new OrderResponse(
                order.getId(),
                order.getApplianceDescription(),
                order.getApplianceBrand(),
                order.getSerialNumber(),
                order.getDefeitoReported(),
                UserResponse.toResponse(order.getClient()),
                order.getStatus(),
                order.getBudgetDeadline(),
                order.getEstimatedDelivery(),
                order.getLaborCost(),
                partsResponse
        );
    }
}