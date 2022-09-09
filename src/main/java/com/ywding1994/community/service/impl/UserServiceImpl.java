package com.ywding1994.community.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywding1994.community.constant.CommunityConstant;
import com.ywding1994.community.constant.LoginTicketConstant;
import com.ywding1994.community.constant.UserConstant;
import com.ywding1994.community.dao.UserMapper;
import com.ywding1994.community.entity.LoginTicket;
import com.ywding1994.community.entity.User;
import com.ywding1994.community.service.UserService;
import com.ywding1994.community.util.CommunityUtil;
import com.ywding1994.community.util.MailCilent;
import com.ywding1994.community.util.RedisKeyUtil;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private MailCilent mailCilent;

    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 缓存数据过期时间（秒）
     */
    private static final int EXPIRED_SECONDS = 3600;

    @Override
    public User getUserById(int userId) {
        User user = getCache(userId);
        if (Objects.isNull(user)) {
            user = initCache(userId);
        }
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<>(User.class).eq(User::getUsername, username));
    }

    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 校验空值
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("user参数不能为空！");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空！");
            return map;
        }

        // 校验账号
        if (Objects.nonNull(this.getUserByUsername(user.getUsername()))) {
            map.put("usernameMsg", "该账号已存在！");
            return map;
        }

        // 校验邮箱
        if (!user.getEmail().matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+")) {
            map.put("emailMsg", "邮箱地址不合法！");
            return map;
        }
        if (Objects.nonNull(this.getOne(new LambdaQueryWrapper<>(User.class).eq(User::getEmail, user.getEmail())))) {
            map.put("emailMsg", "该邮箱已被注册！");
            return map;
        }

        // 注册账号
        String username = user.getUsername();
        String password = user.getPassword();
        String salt = CommunityUtil.generateUUID().substring(0, 5);
        String email = user.getEmail();
        String activationCode = CommunityUtil.generateUUID();
        String headerUrl = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user = User.builder().username(username).password(CommunityUtil.md5(password + salt)).salt(salt).email(email)
                .type(UserConstant.Type.USER).status(UserConstant.Status.UNACTIVATED).activationCode(activationCode)
                .headerUrl(headerUrl).build();
        this.save(user);

        // 发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        // mailCilent.sendMail(user.getEmail(), "激活账号", content);
        mailCilent.sendMailToMyself("激活账号", content);

        return map;
    }

    @Override
    public int activation(int userId, String activationCode) {
        User user = this.getUserById(userId);
        if (user.getStatus() == UserConstant.Status.ACTIVATED) {
            return CommunityConstant.ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(activationCode)) {
            clearCache(userId);
            user.setStatus(UserConstant.Status.ACTIVATED);
            this.updateById(user);
            return CommunityConstant.ACTIVATION_SUCCESS;
        } else {
            return CommunityConstant.ACTIVATION_FAILURE;
        }
    }

    @Override
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 校验空值
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空！");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }

        // 校验账号
        User user = this.getUserByUsername(username);
        if (Objects.isNull(user)) {
            map.put("usernameMsg", "该账号不存在！");
            return map;
        }

        // 校验账号状态
        if (user.getStatus() == UserConstant.Status.UNACTIVATED) {
            map.put("usernameMsg", "该账号未激活！");
            return map;
        }

        // 校验密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确！");
            return map;
        }

        // 生成用户的登陆凭证
        LoginTicket loginTicket = LoginTicket.builder()
                .userId(user.getId())
                .ticket(CommunityUtil.generateUUID())
                .status(LoginTicketConstant.Status.VALID)
                .expired(new Date(System.currentTimeMillis() + expiredSeconds * 1000))
                .build();
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);
        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    @Override
    public void logout(String ticket) {
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(LoginTicketConstant.Status.INVALID);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    @Override
    public boolean updatePassword(int userId, String password) {
        User user = this.getById(userId);
        if (Objects.isNull(user)) {
            return false;
        }

        clearCache(userId);
        user.setPassword(password);
        return this.updateById(user);
    }

    @Override
    public boolean updateHeader(int userId, String headerUrl) {
        User user = this.getById(userId);
        if (Objects.isNull(user)) {
            return false;
        }

        clearCache(userId);
        user.setHeaderUrl(headerUrl);
        return this.updateById(user);
    }

    @Override
    public LoginTicket findLoginTicket(String ticket) {
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 从缓存中获取指定用户信息
     *
     * @param userId 用户id
     * @return 查询到的用户实体
     */
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 当未查询到指定用户信息时将该用户实体在缓存中初始化
     *
     * @param userId 用户id
     * @return 指定用户实体
     */
    private User initCache(int userId) {
        User user = this.getById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, EXPIRED_SECONDS, TimeUnit.SECONDS);
        return user;
    }

    /**
     * 当指定用户信息变更时清除对应的缓存数据
     *
     * @param userId 用户id
     */
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

}
