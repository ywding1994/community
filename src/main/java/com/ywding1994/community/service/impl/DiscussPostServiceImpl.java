package com.ywding1994.community.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.ywding1994.community.constant.DiscussPostConstant;
import com.ywding1994.community.dao.DiscussPostMapper;
import com.ywding1994.community.entity.DiscussPost;
import com.ywding1994.community.service.DiscussPostService;
import com.ywding1994.community.util.SensitiveFilter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DiscussPostServiceImpl extends ServiceImpl<DiscussPostMapper, DiscussPost> implements DiscussPostService {

    @Resource
    private SensitiveFilter sensitiveFilter;

    @Value("${caffeine.posts.max-size}")
    private int maxSize;

    @Value("${caffeine.posts.expire-seconds}")
    private int expireSeconds;

    /**
     * 讨论帖列表缓存
     */
    private LoadingCache<String, List<DiscussPost>> postListCache;

    /**
     * 讨论帖总数缓存
     */
    private LoadingCache<Integer, Integer> postRowsCache;

    @PostConstruct
    public void init() {
        // 初始化讨论帖列表缓存
        postListCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<DiscussPost>>() {
                    @Override
                    public @Nullable List<DiscussPost> load(@NonNull String key) throws Exception {
                        if (StringUtils.isEmpty(key)) {
                            throw new IllegalArgumentException("参数错误！");
                        }

                        String[] params = key.split(":");
                        if (ArrayUtils.isEmpty(params) || params.length != 2) {
                            throw new IllegalArgumentException("参数错误！");
                        }
                        int current = Integer.valueOf(params[0]);
                        int limit = Integer.valueOf(params[1]);

                        log.info("load DiscussPost list from DB.");
                        return findDiscussPostsFromDB(0, current, limit, 1);
                    }
                });

        // 初始化讨论帖总数缓存
        postRowsCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Override
                    public @Nullable Integer load(Integer key) throws Exception {
                        log.info("load DiscussPostRows from DB.");
                        return findDiscussPostRowsFromDB(key);
                    }
                });
    }

    @Override
    public List<DiscussPost> findDiscussPosts(int userId, int current, int limit, int orderMode) {
        if (userId == 0 && orderMode == 1) {
            return postListCache.get(current + ":" + limit);
        }

        log.info("load DiscussPost list from DB.");
        return findDiscussPostsFromDB(userId, current, limit, orderMode);
    }

    /**
     * 从数据库中分页查询指定用户所发的讨论帖
     * <p>
     * 注：若userId = 0则分页查询所有用户所发的讨论帖，所有查询均不包括被拉黑的讨论帖。
     * </p>
     *
     * @param userId    用户id
     * @param current   当前页码，从1开始
     * @param limit     每页贴数上限
     * @param orderMode 排序模式，0为默认排列（按发帖类型、创建时间降序排列），1为按热度排列（按发帖类型、讨论帖分数、创建时间降序排列）
     * @return 指定用户所发的讨论帖
     */
    private List<DiscussPost> findDiscussPostsFromDB(int userId, int current, int limit, int orderMode) {
        Page<DiscussPost> page = new Page<>(current, limit, false);
        page = this.page(page,
                new LambdaQueryWrapper<>(DiscussPost.class)
                        .ne(DiscussPost::getStatus, DiscussPostConstant.Status.BLOCKED)
                        .eq(userId != 0, DiscussPost::getUserId, userId)
                        .orderByDesc(DiscussPost::getType)
                        .orderByDesc(orderMode == 1, DiscussPost::getScore)
                        .orderByDesc(DiscussPost::getCreateTime));
        return page.getRecords();
    }

    @Override
    public int findDiscussPostRows(int userId) {
        if (userId == 0) {
            return postRowsCache.get(userId);
        }

        log.info("load DiscussPostRows from DB.");
        return findDiscussPostRowsFromDB(userId);
    }

    /**
     * 从数据库中查询指定用户的发帖数量
     * <p>
     * 注：若userId = 0则查询所有用户的发帖数量，所有查询均不包括被拉黑的讨论帖。
     * </p>
     *
     * @param userId 用户id
     * @return 指定用户的发帖数量
     */
    private int findDiscussPostRowsFromDB(int userId) {
        return (int) this.count(new LambdaQueryWrapper<>(DiscussPost.class).select(DiscussPost::getId)
                .ne(DiscussPost::getStatus, DiscussPostConstant.Status.BLOCKED)
                .eq(userId != 0, DiscussPost::getUserId, userId));
    }

    @Override
    public boolean addDiscussPost(DiscussPost discussPost) {
        // 校验空值
        if (Objects.isNull(discussPost)) {
            throw new IllegalArgumentException("DiscussPost is null!");
        }

        // 转移HTMl标记
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));

        // 过滤敏感词
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));

        // 保存讨论帖
        return this.save(discussPost);
    }

    @Override
    public boolean updateCommentCount(int id, int commentCount) {
        DiscussPost discussPost = this.getById(id);
        if (Objects.isNull(discussPost)) {
            return false;
        }

        discussPost.setCommentCount(commentCount);
        return this.updateById(discussPost);
    }

    @Override
    public boolean updateType(int id, int type) {
        DiscussPost discussPost = this.getById(id);
        if (Objects.isNull(discussPost)) {
            return false;
        }

        discussPost.setType(type);
        return this.updateById(discussPost);
    }

    @Override
    public boolean updateStatus(int id, int status) {
        DiscussPost discussPost = this.getById(id);
        if (Objects.isNull(discussPost)) {
            return false;
        }

        discussPost.setStatus(status);
        return this.updateById(discussPost);
    }

    @Override
    public boolean updateScore(int id, double score) {
        DiscussPost discussPost = this.getById(id);
        if (Objects.isNull(discussPost)) {
            return false;
        }

        discussPost.setScore(score);
        return this.updateById(discussPost);
    }

}
