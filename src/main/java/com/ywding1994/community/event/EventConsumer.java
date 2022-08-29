package com.ywding1994.community.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections4.MapUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.ywding1994.community.constant.CommunityConstant;
import com.ywding1994.community.entity.DiscussPost;
import com.ywding1994.community.entity.Message;
import com.ywding1994.community.service.DiscussPostService;
import com.ywding1994.community.service.ElasticsearchService;
import com.ywding1994.community.service.MessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * 事件消费者
 */
@Component
@Slf4j
public class EventConsumer {

    @Resource
    private MessageService messageService;

    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private ElasticsearchService elasticsearchService;

    /**
     * 消费评论和点赞事件
     *
     * @param record 消费者记录
     */
    @KafkaListener(topics = { CommunityConstant.TOPIC_COMMENT, CommunityConstant.TOPIC_LIKE,
            CommunityConstant.TOPIC_FOLLOW })
    public void handleCommentMessage(ConsumerRecord<String, Object> record) {
        if (Objects.isNull(record) || Objects.isNull(record.value())) {
            log.error("消息的内容为空！");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (Objects.isNull(event)) {
            log.error("消息格式错误！");
            return;
        }

        // 发送站内通知
        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());
        if (MapUtils.isNotEmpty(event.getData())) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }

        Message message = Message.builder().fromId(CommunityConstant.SYSTEM_USER_ID).toId(event.getEntityUserId())
                .conversationId(event.getTopic()).content(JSONObject.toJSONString(content)).build();
        messageService.addMessage(message);
    }

    /**
     * 消费发帖事件
     *
     * @param record 消费者记录
     */
    @KafkaListener(topics = { CommunityConstant.TOPIC_PUBLISH })
    public void handlePublishMessage(ConsumerRecord<String, Object> record) {
        if (Objects.isNull(record) || Objects.isNull(record.value())) {
            log.error("消息的内容为空！");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (Objects.isNull(event)) {
            log.error("消息格式错误！");
            return;
        }

        DiscussPost discussPost = discussPostService.getById(event.getEntityId());
        elasticsearchService.saveDiscussPost(discussPost);
    }

    /**
     * 消费删帖事件
     *
     * @param record 消费者记录
     */
    @KafkaListener(topics = { CommunityConstant.TOPIC_DELETE })
    public void handleDeleteMessage(ConsumerRecord<String, Object> record) {
        if (Objects.isNull(record) || Objects.isNull(record.value())) {
            log.error("消息的内容为空！");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (Objects.isNull(event)) {
            log.error("消息格式错误！");
            return;
        }

        elasticsearchService.deleteDiscussPost(event.getEntityId());
    }

}
