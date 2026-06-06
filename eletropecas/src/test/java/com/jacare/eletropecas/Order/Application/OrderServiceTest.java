package com.jacare.eletropecas.Order.Application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jacare.eletropecas.Manufacturer.Domain.Manufacturer;
import com.jacare.eletropecas.Order.Domain.Order;
import com.jacare.eletropecas.Order.Domain.OrderStatus;
import com.jacare.eletropecas.Order.Persistence.OrderEntity;
import com.jacare.eletropecas.Order.Persistence.OrderMapper;
import com.jacare.eletropecas.Order.Persistence.OrderRepository;
import com.jacare.eletropecas.Part.Domain.Part;
import com.jacare.eletropecas.User.Domain.User;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("Deve criar uma ordem de serviço")
    void shouldCreateOrderSuccessfully() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order order = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user);

        when(orderRepository.save(any(OrderEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.createOrder(order);

        assertAll("Ordem criada",
                () -> assertNotNull(result),
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Geladeira", result.getApplianceDescription()),
                () -> assertEquals("Brastemp", result.getApplianceBrand()),
                () -> assertEquals("Não está gelando", result.getDefeitoReported()),
                () -> assertEquals("andre@email.com", result.getClient().getEmail()),
                () -> assertEquals(OrderStatus.BUDGET_PENDING, result.getStatus()),
                () -> assertEquals(0.0, result.getLaborCost())
        );

        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    @DisplayName("Deve retornar todas as ordens cadastradas")
    void shouldReturnAllOrders() {
        User user1 = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order order1 = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user1);

        User user2 = new User(2L, "Maria Souza", "maria@email.com", "987.654.321-00", "hash2");
        Order order2 = new Order(2L, "Máquina de lavar", "Samsung", "Não centrifuga", user2);

        when(orderRepository.findAll()).thenReturn(List.of(
                OrderMapper.toEntity(order1),
                OrderMapper.toEntity(order2)
        ));

        List<Order> result = orderService.getAllOrders();

        assertAll("Lista de ordens",
                () -> assertEquals(2, result.size()),
                () -> assertEquals("Geladeira", result.get(0).getApplianceDescription()),
                () -> assertEquals("Máquina de lavar", result.get(1).getApplianceDescription())
        );

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma ordem pelo ID")
    void shouldReturnOrderById() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order order = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(OrderMapper.toEntity(order)));

        Order result = orderService.getOrderById(1L);

        assertAll("Ordem encontrada",
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Geladeira", result.getApplianceDescription()),
                () -> assertEquals("Brastemp", result.getApplianceBrand()),
                () -> assertEquals("Não está gelando", result.getDefeitoReported())
        );

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a ordem não existir")
    void shouldThrowExceptionWhenOrderNotFound() {
        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderService.getOrderById(1L)
        );

        assertEquals("Ordem de serviço não encontrada: 1", exception.getMessage());

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar uma ordem com sucesso")
    void shouldUpdateOrderSuccessfully() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order existingOrder = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user);

        User updatedUser = new User(2L, "Maria Souza", "maria@email.com", "987.654.321-00", "hash2");
        Order updatedOrder = new Order(1L, "Micro-ondas", "LG", "Não esquenta", updatedUser);
        updatedOrder.setSerialNumber("SN-123");
        updatedOrder.setLaborCost(120.0);
        updatedOrder.setBudgetDeadline(LocalDate.now().plusDays(5));
        updatedOrder.setEstimatedDelivery(LocalDate.now().plusDays(10));
        updatedOrder.setRequiredParts(List.of(
                new Part(
                        1L,
                        "Resistência",
                        10,
                        35.0,
                        new Manufacturer(1L, "Bosch", "contato@bosch.com")
                )
        ));

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(OrderMapper.toEntity(existingOrder)));

        when(orderRepository.save(any(OrderEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.updateOrder(1L, updatedOrder);

        assertAll("Ordem atualizada",
                () -> assertEquals("Micro-ondas", result.getApplianceDescription()),
                () -> assertEquals("LG", result.getApplianceBrand()),
                () -> assertEquals("SN-123", result.getSerialNumber()),
                () -> assertEquals("Não esquenta", result.getDefeitoReported()),
                () -> assertEquals(120.0, result.getLaborCost()),
                () -> assertEquals(updatedOrder.getBudgetDeadline(), result.getBudgetDeadline()),
                () -> assertEquals(updatedOrder.getEstimatedDelivery(), result.getEstimatedDelivery()),
                () -> assertEquals(1, result.getRequiredParts().size())
        );

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    @DisplayName("Deve excluir uma ordem existente")
    void shouldDeleteOrderSuccessfully() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order order = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(OrderMapper.toEntity(order)));

        orderService.deleteOrder(1L);

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).delete(any(OrderEntity.class));
    }

    @Test
    @DisplayName("Deve aprovar orçamento")
    void shouldApproveBudget() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order order = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(OrderMapper.toEntity(order)));

        when(orderRepository.save(any(OrderEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.approveBudget(1L);

        assertEquals(OrderStatus.BUDGET_APPROVED, result.getStatus());
    }

    @Test
    @DisplayName("Deve cancelar ordem")
    void shouldCancelOrder() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order order = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(OrderMapper.toEntity(order)));

        when(orderRepository.save(any(OrderEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.cancelOrder(1L);

        assertEquals(OrderStatus.CANCELLED, result.getStatus());
    }

    @Test
    @DisplayName("Deve iniciar reparo")
    void shouldStartRepair() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order order = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(OrderMapper.toEntity(order)));

        when(orderRepository.save(any(OrderEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.startRepair(1L);

        assertEquals(OrderStatus.IN_REPAIR, result.getStatus());
    }

    @Test
    @DisplayName("Deve marcar a ordem como pronta")
    void shouldMarkOrderAsReady() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order order = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(OrderMapper.toEntity(order)));

        when(orderRepository.save(any(OrderEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.markAsReady(1L);

        assertEquals(OrderStatus.READY, result.getStatus());
    }

    @Test
    @DisplayName("Deve marcar a ordem como entregue")
    void shouldDeliverOrder() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order order = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(OrderMapper.toEntity(order)));

        when(orderRepository.save(any(OrderEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.deliverOrder(1L);

        assertEquals(OrderStatus.DELIVERED, result.getStatus());
    }

    @Test
    @DisplayName("Deve adicionar peça necessária à ordem")
    void shouldAddRequiredPart() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order order = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user);
        Part part = new Part(
                1L,
                "Compressor",
                10,
                150.0,
                new Manufacturer(1L, "Bosch", "contato@bosch.com")
        );

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(OrderMapper.toEntity(order)));

        when(orderRepository.save(any(OrderEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.addRequiredPart(1L, part);

        assertAll("Peças necessárias",
                () -> assertEquals(1, result.getRequiredParts().size()),
                () -> assertEquals("Compressor", result.getRequiredParts().get(0).getDescription())
        );
    }

    @Test
    @DisplayName("Deve remover peça necessária da ordem")
    void shouldRemoveRequiredPart() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order order = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user);
        order.getRequiredParts().add(
                new Part(
                        1L,
                        "Compressor",
                        10,
                        150.0,
                        new Manufacturer(1L, "Bosch", "contato@bosch.com")
                )
        );

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(OrderMapper.toEntity(order)));

        when(orderRepository.save(any(OrderEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.removeRequiredPart(1L, 1L);

        assertTrue(result.getRequiredParts().isEmpty());
    }

    @Test
    @DisplayName("Deve calcular o custo dos materiais")
    void shouldCalculateMaterialCost() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order order = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user);

        order.getRequiredParts().add(
                new Part(1L, "Peça A", 1, 100.0, new Manufacturer(1L, "Bosch", "contato@bosch.com"))
        );
        order.getRequiredParts().add(
                new Part(2L, "Peça B", 1, 50.0, new Manufacturer(2L, "LG", "contato@lg.com"))
        );

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(OrderMapper.toEntity(order)));

        Double result = orderService.calculateMaterialCost(1L);

        assertEquals(150.0, result);
    }

    @Test
    @DisplayName("Deve calcular o custo total da ordem")
    void shouldCalculateTotalCost() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order order = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user);

        order.setLaborCost(200.0);
        order.getRequiredParts().add(
                new Part(1L, "Peça A", 1, 100.0, new Manufacturer(1L, "Bosch", "contato@bosch.com"))
        );
        order.getRequiredParts().add(
                new Part(2L, "Peça B", 1, 50.0, new Manufacturer(2L, "LG", "contato@lg.com"))
        );

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(OrderMapper.toEntity(order)));

        Double result = orderService.calculateTotalCost(1L);

        assertEquals(350.0, result);
    }

    @Test
    @DisplayName("Deve definir o prazo do orçamento")
    void shouldDefineBudgetDeadline() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order order = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user);
        LocalDate deadline = LocalDate.now().plusDays(5);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(OrderMapper.toEntity(order)));

        when(orderRepository.save(any(OrderEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.defineBudgetDeadline(1L, deadline);

        assertEquals(deadline, result.getBudgetDeadline());
    }

    @Test
    @DisplayName("Deve definir a data estimada de entrega")
    void shouldDefineEstimatedDelivery() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order order = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user);
        LocalDate estimatedDelivery = LocalDate.now().plusDays(10);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(OrderMapper.toEntity(order)));

        when(orderRepository.save(any(OrderEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.defineEstimatedDelivery(1L, estimatedDelivery);

        assertEquals(estimatedDelivery, result.getEstimatedDelivery());
    }

    @Test
    @DisplayName("Deve retornar o status da ordem")
    void shouldReturnOrderStatus() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");
        Order order = new Order(1L, "Geladeira", "Brastemp", "Não está gelando", user);
        order.setStatus(OrderStatus.READY);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(OrderMapper.toEntity(order)));

        OrderStatus result = orderService.getOrderStatus(1L);

        assertEquals(OrderStatus.READY, result);
    }
}