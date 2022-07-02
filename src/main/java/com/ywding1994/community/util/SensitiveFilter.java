package com.ywding1994.community.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 敏感词过滤器
 */
@Component
@Slf4j
public class SensitiveFilter {

    /**
     * 敏感词文件名
     * <p>
     * 该文件保存在"/src/main/resources"路径下
     * </p>
     */
    private static final String FILENAME = "sensitive_words.txt";

    /**
     * 替换符
     */
    private static final String REPLACEMENT = "***";

    /**
     * 初始化的前缀树根节点
     */
    private TrieNode rootNode = new TrieNode();

    /**
     * 初始化
     * <p>
     * 当容器实例化Bean后自动调用该方法，意味着：该Bean将在服务器启动时被初始化。
     * </p>
     */
    @PostConstruct
    public void init() {
        try (
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(FILENAME);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));) {
            // 读取敏感词并将其添加到前缀树
            String keyword;
            while (((keyword = bufferedReader.readLine())) != null) {
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            log.error("Load sensitive words file failed: " + e.getMessage());
        }
    }

    /**
     * 将敏感词添加到前缀树
     *
     * @param keyword 敏感词
     */
    private void addKeyword(String keyword) {
        TrieNode p = this.rootNode;

        for (int i = 0; i < keyword.length(); ++i) {
            char c = keyword.charAt(i);
            TrieNode subNode = p.getSubNode(c);

            // 初始化子结点
            if (Objects.isNull(subNode)) {
                subNode = new TrieNode();
                p.addSubNode(c, subNode);
            }

            // p指向子结点
            p = subNode;

            // 设置关键词结束标识
            if (i == keyword.length() - 1) {
                p.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     *
     * @param text 待过滤文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        // 指向各个TrieNode结点
        TrieNode p = rootNode;

        // text的双指针，l指向本轮开始检查的位置，r指向当前遍历到的位置
        int l = 0;
        int r = 0;

        // 过滤后的文本
        StringBuffer stringBuffer = new StringBuffer();

        while (r < text.length()) {
            char c = text.charAt(r);

            // 检查时跳过符号
            if (isSymbol(c)) {
                // 若p指向根结点，说明本轮检查的起始位置为一个字符，无需检查即可知字符c必然在结果集中
                if (p == rootNode) {
                    stringBuffer.append(c);
                    l++;
                }
                r++;
                continue;
            }

            // 检查下级结点
            p = p.getSubNode(c);
            if (Objects.isNull(p)) {
                // l开头不会是敏感词，将l加入结果集并从l + 1处开始下一轮检查
                stringBuffer.append(text.charAt(l));
                r = ++l;
                p = rootNode;
            } else if (p.isKeywordEnd()) {
                // [l, r]处为敏感词，将以替换为预设字符，并从r + 1处开始下一轮检查
                stringBuffer.append(REPLACEMENT);
                l = ++r;
                p = rootNode;
            } else {
                // 本轮检查未完成，继续检查下一个字符
                r++;
            }
        }

        // 将最后一批字符加入结果集，该批字符如存在则一定不为敏感词
        stringBuffer.append(text.substring(l));
        return stringBuffer.toString();
    }

    /**
     * 判断是否为符号
     *
     * @param c 待判断的字符
     * @return 判断结果
     */
    private boolean isSymbol(char c) {
        // 东亚文字范围：[0x2E80, 0x9FFF]
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    /**
     * 自定义的前缀树类
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class TrieNode {

        /**
         * 关键词结束标识
         */
        private boolean isKeywordEnd = false;

        /**
         * 子结点集合
         * <p>
         * key为下级字符，value为下级结点。
         * </p>
         */
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        /**
         * 添加子结点
         *
         * @param c    作为key的字符
         * @param node 待添加的子结点
         */
        public void addSubNode(char c, TrieNode node) {
            subNodes.put(c, node);
        }

        /**
         * 获取子结点
         *
         * @param c 作为key的字符
         * @return 对应的子结点
         */
        public TrieNode getSubNode(char c) {
            return subNodes.get(c);
        }

    }

}
