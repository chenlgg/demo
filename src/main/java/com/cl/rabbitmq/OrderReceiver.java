// OrderReceiver.java
package com.cl.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author: chenl
 * @since: 2025/9/3 21:53
 * @description:
 */
@Component
public class OrderReceiver {

    @RabbitListener(queues = "order.queue")
    public void receive(String message, Message msg, Channel channel) {
        try {
            System.out.println("[接收]1 " + message);

            // 模拟业务处理
            //Thread.sleep(100);

            // 手动确认（来自 application-pro.properties 的配置）
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                // 拒绝并重新入队
                channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @RabbitListener(queues = "order.queue")
    public void receive2(String message, Message msg, Channel channel) {
        try {
            System.out.println("[接收]2 " + message);

            // 模拟业务处理
            //Thread.sleep(100);

            // 手动确认（来自 application-pro.properties 的配置）
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                // 拒绝并重新入队
                channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // ==================== Fanout接收实例 ====================

    // 接收Fanout队列A的消息
    @RabbitListener(queues = "fanout.queue.A")
    public void receiveFanoutA(String message, Message msg, Channel channel) {
        try {
            System.out.println("[Fanout接收者A] " + message);

            // 手动确认
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                // 拒绝并重新入队
                channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // 接收Fanout队列B的消息
    @RabbitListener(queues = "fanout.queue.B")
    public void receiveFanoutB(String message, Message msg, Channel channel) {
        try {
            System.out.println("[Fanout接收者B] " + message);

            // 手动确认
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                // 拒绝并重新入队
                channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // Fanout队列A的第二个消费者（演示多个消费者监听同一队列）
    @RabbitListener(queues = "fanout.queue.A")
    public void receiveFanoutA2(String message, Message msg, Channel channel) {
        try {
            System.out.println("[Fanout接收者A2] " + message);

            // 手动确认
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                // 拒绝并重新入队
                channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // ==================== Topic接收实例 ====================

    // 接收Topic队列A的消息 (匹配 order.*)
    @RabbitListener(queues = "topic.queue.A")
    public void receiveTopicA(String message, Message msg, Channel channel) {
        try {
            System.out.println("[Topic接收者A] 接收到消息: " + message +
                             ", 路由键: " + msg.getMessageProperties().getReceivedRoutingKey());

            // 手动确认
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                // 拒绝并重新入队
                channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // 接收Topic队列B的消息 (匹配 order.#)
    @RabbitListener(queues = "topic.queue.B")
    public void receiveTopicB(String message, Message msg, Channel channel) {
        try {
            System.out.println("[Topic接收者B] 接收到消息: " + message +
                             ", 路由键: " + msg.getMessageProperties().getReceivedRoutingKey());

            // 手动确认
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                // 拒绝并重新入队
                channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // 接收Topic队列C的消息 (只匹配 order.create)
    @RabbitListener(queues = "topic.queue.C")
    public void receiveTopicC(String message, Message msg, Channel channel) {
        try {
            System.out.println("[Topic接收者C] 接收到消息: " + message +
                             ", 路由键: " + msg.getMessageProperties().getReceivedRoutingKey());

            // 手动确认
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                // 拒绝并重新入队
                channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // Topic队列A的第二个消费者（演示多个消费者监听同一队列）
    @RabbitListener(queues = "topic.queue.A")
    public void receiveTopicA2(String message, Message msg, Channel channel) {
        try {
            System.out.println("[Topic接收者A2] 接收到消息: " + message +
                             ", 路由键: " + msg.getMessageProperties().getReceivedRoutingKey());

            // 手动确认
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                // 拒绝并重新入队
                channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // ==================== Headers接收实例 ====================

    // 接收Headers队列A的消息 (匹配type=order且priority=high)
    @RabbitListener(queues = "headers.queue.A")
    public void receiveHeadersA(String message, Message msg, Channel channel) {
        try {
            Map<String, Object> headers = msg.getMessageProperties().getHeaders();
            System.out.println("[Headers接收者A] 接收到消息: " + message +
                             ", Headers: " + headers);

            // 手动确认
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                // 拒绝并重新入队
                channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // 接收Headers队列B的消息 (匹配type=order)
    @RabbitListener(queues = "headers.queue.B")
    public void receiveHeadersB(String message, Message msg, Channel channel) {
        try {
            Map<String, Object> headers = msg.getMessageProperties().getHeaders();
            System.out.println("[Headers接收者B] 接收到消息: " + message +
                             ", Headers: " + headers);

            // 手动确认
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                // 拒绝并重新入队
                channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // 接收Headers队列C的消息 (匹配version=1.0)
    @RabbitListener(queues = "headers.queue.C")
    public void receiveHeadersC(String message, Message msg, Channel channel) {
        try {
            Map<String, Object> headers = msg.getMessageProperties().getHeaders();
            System.out.println("[Headers接收者C] 接收到消息: " + message +
                             ", Headers: " + headers);

            // 手动确认
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                // 拒绝并重新入队
                channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
