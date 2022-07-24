package com.ywding1994.community.event;

import javax.annotation.Resource;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * 事件生产者
 */
@Component
public class EventProducer {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 对事件进行处理
     *
     * @param event 事件实体
     */
    public void fireEvent(Event event) {
        // 将事件发布到指定主题
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }

}
