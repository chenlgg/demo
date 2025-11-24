// OrderSender.java
package com.cl.rabbitmq;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: chenl
 * @since: 2025/9/3 21:52
 * @description:
 */
@Component
public class OrderSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String orderId) {
        String message = "创建订单: " + orderId;
        rabbitTemplate.convertAndSend("order.exchange", "order.ecrate", message);
        System.out.println("已发送: " + message);
    }

    // 添加Fanout模式发送方法
    public void sendFanoutMessage(String message) {
        // 使用Fanout交换机发送消息，routing key会被忽略
        rabbitTemplate.convertAndSend("fanout.exchange", "", message);
        System.out.println("Fanout消息已发送: " + message);
    }

    // 重载方法，可以发送订单相关的Fanout消息
    public void sendOrderFanoutMessage(String orderId) {
        String message = "订单广播消息: " + orderId;
        sendFanoutMessage(message);
    }

    // ==================== Topic模式发送方法 ====================

    /**
     * 发送Topic消息
     * @param routingKey 路由键
     * @param message 消息内容
     */
    public void sendTopicMessage(String routingKey, String message) {
        rabbitTemplate.convertAndSend("topic.exchange", routingKey, message);
        System.out.println("Topic消息已发送 - 路由键: " + routingKey + ", 消息: " + message);
    }

    /**
     * 发送订单创建相关的Topic消息
     * @param orderId 订单ID
     */
    public void sendOrderCreateTopicMessage(String orderId) {
        String message = "创建订单: " + orderId;
        sendTopicMessage("order.create", message);
    }

    /**
     * 发送订单更新相关的Topic消息
     * @param orderId 订单ID
     */
    public void sendOrderUpdateTopicMessage(String orderId) {
        String message = "更新订单: " + orderId;
        sendTopicMessage("order.update", message);
    }

    /**
     * 发送订单删除相关的Topic消息
     * @param orderId 订单ID
     */
    public void sendOrderDeleteTopicMessage(String orderId) {
        String message = "删除订单: " + orderId;
        sendTopicMessage("order.delete", message);
    }

    /**
     * 发送详细订单操作的Topic消息
     * @param operation 操作类型
     * @param orderId 订单ID
     * @param detail 详细信息
     */
    public void sendOrderDetailTopicMessage(String operation, String orderId, String detail) {
        String message = String.format("订单%s操作: %s, 详情: %s", operation, orderId, detail);
        sendTopicMessage("order." + operation + ".detail", message);
    }

    // ==================== Headers模式发送方法 ====================

    /**
     * 发送Headers消息
     * @param message 消息内容
     * @param headers 消息头
     */
    public void sendHeadersMessage(String message, Map<String, Object> headers) {
        MessagePostProcessor messagePostProcessor = msg -> {
            headers.forEach((key, value) -> msg.getMessageProperties().setHeader(key, value));
            return msg;
        };
        rabbitTemplate.convertAndSend("headers.exchange", "", message, messagePostProcessor);
        System.out.println("Headers消息已发送: " + message + ", Headers: " + headers);
    }

    /**
     * 发送订单相关的Headers消息
     * @param orderId 订单ID
     * @param priority 优先级
     */
    public void sendOrderHeadersMessage(String orderId, String priority) {
        String message = "处理订单: " + orderId;
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "order");
        headers.put("priority", priority);
        headers.put("version", "1.0");
        sendHeadersMessage(message, headers);
    }

    /**
     * 发送高优先级订单Headers消息
     * @param orderId 订单ID
     */
    public void sendHighPriorityOrderHeadersMessage(String orderId) {
        sendOrderHeadersMessage(orderId, "high");
    }

    /**
     * 发送普通优先级订单Headers消息
     * @param orderId 订单ID
     */
    public void sendNormalPriorityOrderHeadersMessage(String orderId) {
        sendOrderHeadersMessage(orderId, "normal");
    }
}
