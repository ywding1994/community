package com.ywding1994.community.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ywding1994.community.constant.HTTPStatusCodeConstant;
import com.ywding1994.community.constant.MessageConstant;
import com.ywding1994.community.entity.Message;
import com.ywding1994.community.entity.User;
import com.ywding1994.community.service.MessageService;
import com.ywding1994.community.service.UserService;
import com.ywding1994.community.util.CommunityUtil;
import com.ywding1994.community.util.HostHolder;
import com.ywding1994.community.vo.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(path = "/letter")
@Api(tags = "私信消息接口")
public class MessageController {

    @Resource
    private HostHolder hostHolder;

    @Resource
    private MessageService messageService;

    @Resource
    private UserService userService;

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    @ApiOperation(value = "显示私信列表", httpMethod = "GET")
    public String getLetterList(Model model, Page page) {
        User user = hostHolder.getUser();

        // 设置分页信息
        page.setLimit(5);
        page.setRows(messageService.findConversationCount(user.getId()));
        page.setPath("/letter/list");

        // 会话列表
        List<Message> conversations = messageService.findConversations(user.getId(), page.getCurrent(),
                page.getLimit());
        List<Map<String, Object>> conversationMaps = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(conversations)) {
            for (Message message : conversations) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
                map.put("unreadCount", messageService.findLetterUnreadCount(user.getId(), message.getConversationId()));
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.getById(targetId));
                conversationMaps.add(map);
            }
        }
        model.addAttribute("conversationMaps", conversationMaps);

        // 查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        return "/site/letter";
    }

    @RequestMapping(path = "/detail/{conversationId}", method = RequestMethod.GET)
    @ApiOperation(value = "显示会话详情", httpMethod = "GET")
    public String getLetterDetail(Model model, Page page,
            @PathVariable("conversationId") @ApiParam("会话id") String conversationId) {
        // 设置分页信息
        page.setLimit(5);
        page.setRows(messageService.findLetterCount(conversationId));
        page.setPath("/letter/detail/" + conversationId);

        // 私信列表
        List<Message> letters = messageService.findLetters(conversationId, page.getCurrent(), page.getLimit());
        List<Map<String, Object>> letterMaps = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(letters)) {
            for (Message message : letters) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.getById(message.getFromId()));
                letterMaps.add(map);
            }
        }
        model.addAttribute("letterMaps", letterMaps);

        // 会话目标
        model.addAttribute("target", getLetterTarget(conversationId));

        // 设置已读
        List<Integer> ids = getLetterIds(letters);
        if (CollectionUtils.isNotEmpty(ids)) {
            messageService.readMessages(ids);
        }
        return "/site/letter-detail";
    }

    /**
     * 获取指定会话的目标用户
     *
     * @param conversationId
     * @return 目标用户
     */
    private User getLetterTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        int targetId = hostHolder.getUser().getId().equals(Integer.parseInt(ids[0])) ? Integer.parseInt(ids[1])
                : Integer.parseInt(ids[0]);
        return userService.getById(targetId);
    }

    /**
     * 获取当前用户未读的私信消息id列表
     *
     * @param letters 私信消息
     * @return 当前用户未读的私信消息id列表
     */
    private List<Integer> getLetterIds(List<Message> letters) {
        List<Integer> ids = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(letters)) {
            for (Message message : letters) {
                if (hostHolder.getUser().getId().equals(message.getToId())
                        && message.getStatus().equals(MessageConstant.Status.UNREAD)) {
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }

    @RequestMapping(path = "/send", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "发送私信消息", httpMethod = "POST")
    public String sendLetter(@RequestParam @ApiParam("收件人") String toName,
            @RequestParam @ApiParam("私信消息内容") String content) {
        User target = userService.getOne(new LambdaQueryWrapper<>(User.class).eq(User::getUsername, toName));
        if (Objects.isNull(target)) {
            return CommunityUtil.getJSONString(HTTPStatusCodeConstant.INTERNAL_SERVER_ERROR, "目标用户不存在！");
        }

        Integer fromId = hostHolder.getUser().getId();
        Integer toId = target.getId();
        Message message = Message.builder().fromId(fromId).toId(toId)
                .conversationId(Integer.compare(fromId, toId) < 0 ? fromId + "_" + toId : toId + "_" + fromId)
                .content(content).build();
        messageService.addMessage(message);
        return CommunityUtil.getJSONString(HTTPStatusCodeConstant.OK, message.toString());
    }

}
