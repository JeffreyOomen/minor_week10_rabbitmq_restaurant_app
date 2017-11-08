package com.infosupport.minor.rabbit_mq_restaurant_app.services;

import com.infosupport.minor.rabbit_mq_restaurant_app.dao.OrderRepository;
import com.infosupport.minor.rabbit_mq_restaurant_app.domain.Order;
import com.infosupport.minor.rabbit_mq_restaurant_app.domain.Order.Status;
import com.infosupport.minor.rabbit_mq_restaurant_app.rabbitmq.RabbitMqSender;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private RabbitMqSender rabbitMqSender;

  @Autowired
  public void setOrderRepository(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public void placeOrder(List<Integer> dishIds) {
    Order order = new Order();
    order.setDishIds(dishIds);
    order.setStatus(Status.REQUESTED);
    orderRepository.save(order);

    rabbitMqSender.send( order.getDishIds());
  }
}
