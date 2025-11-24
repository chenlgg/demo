// TestController.java
package com.cl.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: chenl
 * @since: 2025/9/3 22:11
 * @description:
 */
@RestController
public class TestController {

    @Autowired
    private OrderSender orderSender;

    @GetMapping("/send")
    public String send() {
        for (int i = 0; i < 100000; i++) {
            orderSender.send(i + "");
        }
        //orderSender.send("1001");
        return "消息已发送";
    }

    // ==================== Fanout测试接口 ====================

    /**
     * 发送Fanout广播消息
     * @param message 消息内容
     * @return 返回结果
     */
    @GetMapping("/fanout/send")
    public String sendFanoutMessage(@RequestParam(defaultValue = "测试广播消息") String message) {
        orderSender.sendFanoutMessage(message);
        return "Fanout消息已发送: " + message;
    }

    /**
     * 发送订单相关的Fanout广播消息
     * @param orderId 订单ID
     * @return 返回结果
     */
    @GetMapping("/fanout/sendOrder")
    public String sendOrderFanoutMessage(@RequestParam String orderId) {
        orderSender.sendOrderFanoutMessage(orderId);
        return "订单广播消息已发送: " + orderId;
    }

    /**
     * 批量发送Fanout广播消息
     * @param count 发送数量
     * @param prefix 消息前缀
     * @return 返回结果
     */
    @GetMapping("/fanout/sendBatch")
    public String sendFanoutBatch(@RequestParam(defaultValue = "10") int count,
                                  @RequestParam(defaultValue = "批量广播消息") String prefix) {
        for (int i = 1; i <= count; i++) {
            String message = prefix + " - " + i;
            orderSender.sendFanoutMessage(message);
        }
        return "已批量发送 " + count + " 条Fanout消息";
    }

    // ==================== Topic测试接口 ====================

    /**
     * 发送Topic消息
     * @param routingKey 路由键
     * @param message 消息内容
     * @return 返回结果
     */
    @GetMapping("/topic/send")
    public String sendTopicMessage(@RequestParam String routingKey,
                                   @RequestParam(defaultValue = "测试Topic消息") String message) {
        orderSender.sendTopicMessage(routingKey, message);
        return "Topic消息已发送 - 路由键: " + routingKey + ", 消息: " + message;
    }

    /**
     * 发送订单创建Topic消息
     * @param orderId 订单ID
     * @return 返回结果
     */
    @GetMapping("/topic/sendCreate")
    public String sendOrderCreateTopicMessage(@RequestParam String orderId) {
        orderSender.sendOrderCreateTopicMessage(orderId);
        return "订单创建Topic消息已发送: " + orderId;
    }

    /**
     * 发送订单更新Topic消息
     * @param orderId 订单ID
     * @return 返回结果
     */
    @GetMapping("/topic/sendUpdate")
    public String sendOrderUpdateTopicMessage(@RequestParam String orderId) {
        orderSender.sendOrderUpdateTopicMessage(orderId);
        return "订单更新Topic消息已发送: " + orderId;
    }

    /**
     * 发送订单删除Topic消息
     * @param orderId 订单ID
     * @return 返回结果
     */
    @GetMapping("/topic/sendDelete")
    public String sendOrderDeleteTopicMessage(@RequestParam String orderId) {
        orderSender.sendOrderDeleteTopicMessage(orderId);
        return "订单删除Topic消息已发送: " + orderId;
    }

    /**
     * 发送详细订单操作Topic消息
     * @param operation 操作类型
     * @param orderId 订单ID
     * @param detail 详细信息
     * @return 返回结果
     */
    @GetMapping("/topic/sendDetail")
    public String sendOrderDetailTopicMessage(@RequestParam String operation,
                                              @RequestParam String orderId,
                                              @RequestParam(defaultValue = "无详细信息") String detail) {
        orderSender.sendOrderDetailTopicMessage(operation, orderId, detail);
        return String.format("订单%s操作Topic消息已发送: %s, 详情: %s", operation, orderId, detail);
    }

    /**
     * 批量发送不同类型Topic消息
     * @param count 每种类型发送数量
     * @return 返回结果
     */
    @GetMapping("/topic/sendBatch")
    public String sendTopicBatch(@RequestParam(defaultValue = "3") int count) {
        for (int i = 1; i <= count; i++) {
            // 发送创建订单消息
            orderSender.sendOrderCreateTopicMessage("CREATE_" + i);

            // 发送更新订单消息
            orderSender.sendOrderUpdateTopicMessage("UPDATE_" + i);

            // 发送删除订单消息
            orderSender.sendOrderDeleteTopicMessage("DELETE_" + i);

            // 发送详细操作消息
            orderSender.sendOrderDetailTopicMessage("create", "DETAIL_" + i, "详细信息" + i);
        }
        return "已批量发送 " + (count * 4) + " 条Topic消息";
    }

    // ==================== Headers测试接口 ====================

    /**
     * 发送Headers消息
     * @param message 消息内容
     * @param type 类型
     * @param priority 优先级
     * @param version 版本
     * @return 返回结果
     */
    @GetMapping("/headers/send")
    public String sendHeadersMessage(@RequestParam(defaultValue = "测试Headers消息") String message,
                                     @RequestParam(required = false) String type,
                                     @RequestParam(required = false) String priority,
                                     @RequestParam(required = false) String version) {
        Map<String, Object> headers = new HashMap<>();
        if (type != null) headers.put("type", type);
        if (priority != null) headers.put("priority", priority);
        if (version != null) headers.put("version", version);

        orderSender.sendHeadersMessage(message, headers);
        return "Headers消息已发送: " + message + ", Headers: " + headers;
    }

    /**
     * 发送订单Headers消息
     * @param orderId 订单ID
     * @param priority 优先级
     * @return 返回结果
     */
    @GetMapping("/headers/sendOrder")
    public String sendOrderHeadersMessage(@RequestParam String orderId,
                                          @RequestParam(defaultValue = "normal") String priority) {
        orderSender.sendOrderHeadersMessage(orderId, priority);
        return "订单Headers消息已发送: " + orderId + ", 优先级: " + priority;
    }

    /**
     * 发送高优先级订单Headers消息
     * @param orderId 订单ID
     * @return 返回结果
     */
    @GetMapping("/headers/sendHighPriorityOrder")
    public String sendHighPriorityOrderHeadersMessage(@RequestParam String orderId) {
        orderSender.sendHighPriorityOrderHeadersMessage(orderId);
        return "高优先级订单Headers消息已发送: " + orderId;
    }

    /**
     * 发送普通优先级订单Headers消息
     * @param orderId 订单ID
     * @return 返回结果
     */
    @GetMapping("/headers/sendNormalPriorityOrder")
    public String sendNormalPriorityOrderHeadersMessage(@RequestParam String orderId) {
        orderSender.sendNormalPriorityOrderHeadersMessage(orderId);
        return "普通优先级订单Headers消息已发送: " + orderId;
    }

    /**
     * 批量发送Headers消息
     * @param count 发送数量
     * @return 返回结果
     */
    @GetMapping("/headers/sendBatch")
    public String sendHeadersBatch(@RequestParam(defaultValue = "3") int count) {
        for (int i = 1; i <= count; i++) {
            // 发送高优先级订单消息
            orderSender.sendHighPriorityOrderHeadersMessage("HIGH_" + i);

            // 发送普通优先级订单消息
            orderSender.sendNormalPriorityOrderHeadersMessage("NORMAL_" + i);
        }
        return "已批量发送 " + (count * 2) + " 条Headers消息";
    }
}
