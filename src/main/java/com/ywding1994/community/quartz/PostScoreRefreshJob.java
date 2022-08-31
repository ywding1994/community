package com.ywding1994.community.quartz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.ywding1994.community.constant.CommunityConstant;
import com.ywding1994.community.constant.DiscussPostConstant;
import com.ywding1994.community.entity.DiscussPost;
import com.ywding1994.community.service.DiscussPostService;
import com.ywding1994.community.service.ElasticsearchService;
import com.ywding1994.community.service.LikeService;
import com.ywding1994.community.util.RedisKeyUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostScoreRefreshJob implements Job {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private LikeService likeService;

    @Resource
    private ElasticsearchService elasticsearchService;

    /**
     * 牛客纪元
     */
    private static final Date EPOCH;

    // 静态代码块：仅在类加载时执行，且仅执行一次。
    static {
        try {
            EPOCH = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-08-01 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("初始化牛客纪元失败！", e);
        }
    }

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        String redisKey = RedisKeyUtil.getPostScoreKey();
        BoundSetOperations<String, Object> operations = redisTemplate.boundSetOps(redisKey);

        if (operations.size() == 0) {
            log.info("[任务取消] 没有需要刷新的讨论帖！");
            return;
        }

        log.info("[任务开始] 正在刷新讨论帖分数...待刷新的讨论帖总数为：" + operations.size());
        while (operations.size() > 0) {
            this.refresh((Integer) operations.pop());
        }
        log.info("[任务结束] 讨论帖分数刷新完毕！");
    }

    private void refresh(int postId) {
        DiscussPost discussPost = discussPostService.getById(postId);
        if (Objects.isNull(discussPost)) {
            log.error("该讨论帖不存在：id = " + postId);
            return;
        }

        // 是否为精华
        boolean wonderful = discussPost.getStatus() == DiscussPostConstant.Status.HIGHLIGHTED;
        // 评论数量
        int commentCount = discussPost.getCommentCount();
        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(CommunityConstant.ENTITY_TYPE_POST, postId);

        // 计算权重(w = 精华分 + 评论数 * 10 + 点赞数 * 2)
        double w = (wonderful ? 75 : 0) + commentCount * 10 + likeCount * 2;
        // 计算分数(score = log(w) + 距离天数(= 发布时间 - 牛客纪元))
        double score = Math.log10(Math.max(w, 1))
                + (discussPost.getCreateTime().getTime() - EPOCH.getTime()) / (1000 * 3600 * 24);
        // 更新讨论帖分数
        discussPostService.updateScore(postId, score);
        // 同步搜索数据
        elasticsearchService.saveDiscussPost(discussPostService.getById(postId));
    }

}
