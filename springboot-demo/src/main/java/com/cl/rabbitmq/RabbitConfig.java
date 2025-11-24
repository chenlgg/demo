package com.cl.rabbitmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 * 配置不同类型的交换机、队列和绑定关系
 *
 * @author: chenl
 * @since: 2025/9/3 21:50
 */
import org.springframework.amqp.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    // ==================== 基础队列定义 ====================

    /**
     * 订单处理队列
     */
    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable("order.queue").build();
    }

    // ==================== Direct Exchange配置 ====================

    /**
     * 订单直连交换机
     */
    @Bean
    public DirectExchange orderExchange() {
        return ExchangeBuilder.directExchange("order.exchange")
                .durable(true)
                .build();
    }

    /**
     * 订单队列绑定到订单交换机
     */
    @Bean
    public Binding bindingOrder(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue)
                .to(orderExchange)
                .with("order.create");
    }

    // ==================== Fanout Exchange配置 ====================

    /**
     * 广播交换机
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder.fanoutExchange("fanout.exchange")
                .durable(true)
                .build();
    }

    /**
     * Fanout队列A - 用于接收广播消息
     */
    @Bean
    public Queue fanoutQueueA() {
        return QueueBuilder.durable("fanout.queue.A").build();
    }

    /**
     * Fanout队列B - 用于接收广播消息
     */
    @Bean
    public Queue fanoutQueueB() {
        return QueueBuilder.durable("fanout.queue.B").build();
    }

    /**
     * Fanout队列A绑定到广播交换机
     */
    @Bean
    public Binding bindingFanoutA(Queue fanoutQueueA, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueueA).to(fanoutExchange);
    }

    /**
     * Fanout队列B绑定到广播交换机
     */
    @Bean
    public Binding bindingFanoutB(Queue fanoutQueueB, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueueB).to(fanoutExchange);
    }

    // ==================== Topic Exchange配置 ====================

    /**
     * 主题交换机
     */
    @Bean
    public TopicExchange topicExchange() {
        return ExchangeBuilder.topicExchange("topic.exchange")
                .durable(true)
                .build();
    }

    /**
     * Topic队列A - 匹配order.*模式
     */
    @Bean
    public Queue topicQueueA() {
        return QueueBuilder.durable("topic.queue.A").build();
    }

    /**
     * Topic队列B - 匹配order.#模式
     */
    @Bean
    public Queue topicQueueB() {
        return QueueBuilder.durable("topic.queue.B").build();
    }

    /**
     * Topic队列C - 精确匹配order.create
     */
    @Bean
    public Queue topicQueueC() {
        return QueueBuilder.durable("topic.queue.C").build();
    }

    /**
     * Topic队列A绑定到主题交换机 - 匹配order.*路由键
     */
    @Bean
    public Binding bindingTopicA(Queue topicQueueA, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueueA)
                .to(topicExchange)
                .with("order.*");
    }

    /**
     * Topic队列B绑定到主题交换机 - 匹配order.#路由键
     */
    @Bean
    public Binding bindingTopicB(Queue topicQueueB, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueueB)
                .to(topicExchange)
                .with("order.#");
    }

    /**
     * Topic队列C绑定到主题交换机 - 精确匹配order.create路由键
     */
    @Bean
    public Binding bindingTopicC(Queue topicQueueC, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueueC)
                .to(topicExchange)
                .with("order.create");
    }

    // ==================== Headers Exchange配置 ====================

    /**
     * 头部交换机
     */
    @Bean
    public HeadersExchange headersExchange() {
        return ExchangeBuilder.headersExchange("headers.exchange")
                .durable(true)
                .build();
    }

    /**
     * Headers队列A - 匹配type=order且priority=high的消息
     */
    @Bean
    public Queue headersQueueA() {
        return QueueBuilder.durable("headers.queue.A").build();
    }

    /**
     * Headers队列B - 匹配type=order的消息
     */
    @Bean
    public Queue headersQueueB() {
        return QueueBuilder.durable("headers.queue.B").build();
    }

    /**
     * Headers队列C - 匹配version=1.0的消息
     */
    @Bean
    public Queue headersQueueC() {
        return QueueBuilder.durable("headers.queue.C").build();
    }

    /**
     * Headers队列A绑定到头部交换机 - 全匹配模式
     */
    @Bean
    public Binding bindingHeadersA(Queue headersQueueA, HeadersExchange headersExchange) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "order");
        headers.put("priority", "high");
        return BindingBuilder.bind(headersQueueA)
                .to(headersExchange)
                .whereAll(headers).match();
    }

    /**
     * Headers队列B绑定到头部交换机 - 任一匹配模式
     */
    @Bean
    public Binding bindingHeadersB(Queue headersQueueB, HeadersExchange headersExchange) {
        return BindingBuilder.bind(headersQueueB)
                .to(headersExchange)
                .where("type").matches("order");
    }

    /**
     * Headers队列C绑定到头部交换机 - 版本匹配
     */
    @Bean
    public Binding bindingHeadersC(Queue headersQueueC, HeadersExchange headersExchange) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("version", "1.0");
        return BindingBuilder.bind(headersQueueC)
                .to(headersExchange)
                .whereAll(headers).match();
    }
}
