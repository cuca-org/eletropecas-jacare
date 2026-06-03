package com.jacare.eletropecas.Order.Api;

import com.jacare.eletropecas.Order.Api.Dto.OrderResponse;
import com.jacare.eletropecas.Order.Application.OrderService;
import com.jacare.eletropecas.Order.Domain.Order;
import com.jacare.eletropecas.Order.Domain.OrderStatus;
import com.jacare.eletropecas.Part.Domain.Part;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // CRUD

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody Order order) {
        Order created = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll() {
        List<OrderResponse> responseList = orderService.getAllOrders().stream()
                .map(OrderResponse::toResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(OrderResponse.toResponse(order));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable Long id, @RequestBody Order order) {
        Order updated = orderService.updateOrder(id, order);
        return ResponseEntity.ok(OrderResponse.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    // Fluxo de status do pedido

    @PatchMapping("/{id}/approve-budget")
    public ResponseEntity<OrderResponse> approveBudget(@PathVariable Long id) {
        Order order = orderService.approveBudget(id);
        return ResponseEntity.ok(OrderResponse.toResponse(order));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
        Order order = orderService.cancelOrder(id);
        return ResponseEntity.ok(OrderResponse.toResponse(order));
    }

    @PatchMapping("/{id}/start-repair")
    public ResponseEntity<OrderResponse> startRepair(@PathVariable Long id) {
        Order order = orderService.startRepair(id);
        return ResponseEntity.ok(OrderResponse.toResponse(order));
    }

    @PatchMapping("/{id}/mark-as-ready")
    public ResponseEntity<OrderResponse> markAsReady(@PathVariable Long id) {
        Order order = orderService.markAsReady(id);
        return ResponseEntity.ok(OrderResponse.toResponse(order));
    }

    @PatchMapping("/{id}/deliver")
    public ResponseEntity<OrderResponse> deliverOrder(@PathVariable Long id) {
        Order order = orderService.deliverOrder(id);
        return ResponseEntity.ok(OrderResponse.toResponse(order));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<OrderStatus> getStatus(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderStatus(id));
    }

    // Gerenciamento de peças do pedido

    @PostMapping("/{id}/parts")
    public ResponseEntity<OrderResponse> addPart(@PathVariable Long id, @RequestBody Part part) {
        Order order = orderService.addRequiredPart(id, part);
        return ResponseEntity.ok(OrderResponse.toResponse(order));
    }

    @DeleteMapping("/{id}/parts/{partId}")
    public ResponseEntity<OrderResponse> removePart(@PathVariable Long id, @PathVariable Long partId) {
        Order order = orderService.removeRequiredPart(id, partId);
        return ResponseEntity.ok(OrderResponse.toResponse(order));
    }

    // Cálculos e prazos

    @GetMapping("/{id}/material-cost")
    public ResponseEntity<Double> getMaterialCost(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.calculateMaterialCost(id));
    }

    @GetMapping("/{id}/total-cost")
    public ResponseEntity<Double> getTotalCost(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.calculateTotalCost(id));
    }

    @PatchMapping("/{id}/budget-deadline")
    public ResponseEntity<OrderResponse> defineBudgetDeadline(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline) {
        Order order = orderService.defineBudgetDeadline(id, deadline);
        return ResponseEntity.ok(OrderResponse.toResponse(order));
    }

    @PatchMapping("/{id}/estimated-delivery")
    public ResponseEntity<OrderResponse> defineEstimatedDelivery(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate estimatedDelivery) {
        Order order = orderService.defineEstimatedDelivery(id, estimatedDelivery);
        return ResponseEntity.ok(OrderResponse.toResponse(order));
    }
}