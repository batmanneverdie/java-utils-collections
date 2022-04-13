package com.yintech.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>redis 工具类<p/>
 *
 * @author laipan
 * @date 2021/11/10,10:34
 * @since v0.1
 */
@Slf4j
@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> String <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * string 类型，为指定 key set 值
     *
     * @param key   键
     * @param value 值
     * @return 执行结果
     */
    public boolean set(String key, String value) {
        boolean succeed = false;
        try {
            stringRedisTemplate.opsForValue().set(key, value);
            succeed = true;
        } catch (Exception e) {
            log.error("# redis set key 出错，key={}，value={}。错误信息：{}", key, value, e);
        }
        return succeed;
    }

    /**
     * string 类型，为指定 key set 值并设置过期时间
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间
     * @param unit   时间单位
     * @return 执行结果
     */
    public boolean set(String key, String value, long expire, TimeUnit unit) {
        boolean succeed = false;
        try {
            stringRedisTemplate.opsForValue().set(key, value, expire, unit);
            succeed = true;
        } catch (Exception e) {
            log.error("# redis set 带有过期时间的 key 出错，key={}，value={}，expire time={}，单位：{}。错误信息：{}", key, value, expire, unit, e);
        }
        return succeed;
    }

    /**
     * string 类型，为指定 key set 值并设置过期时间
     *
     * @param key 键
     * @return 值，键不存在时返回 null
     */
    public String get(String key) {
        String res = null;
        try {
            res = stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("# 获取 redis string key 出错，key={}，错误信息：{}", key, e);
        }
        return res;
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Lists <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 在 list 的头部插入数据，如果 key 不存在，则先创建此 key
     *
     * @param key   键
     * @param value 值
     * @return 插入元素后的 list 长度
     */
    public long leftPush(String key, String value) {
        long len = -1L;
        try {
            len = stringRedisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            log.error("# redis 推入元素到 list 出错，key={}，value={}，", key, value, e);
        }
        return len;
    }

    /**
     * 拉取指定位置的 list 元素
     *
     * @param key   键
     * @param start 起始位置，0 表示第一个元素
     * @param end   结束位置，-1 表示最后一个元素，-2 表示倒数第二个元素......
     * @return 元素集合
     */
    public List<String> range(String key, long start, long end) {
        List<String> list = new ArrayList<>();
        try {
            list = stringRedisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("# redis 获取 list 出错，key={}，start={}，end={}", key, start, end, e);
        }
        return list;
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> hash <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 往 redis hash 中写入元素
     *
     * @param key   redis hash key
     * @param map   <field,value>
     * @return  写入状态
     */
    public boolean hmset(String key, Map<Object, Object> map) {
        boolean succeed = false;
        try {
            stringRedisTemplate.opsForHash().putAll(key, map);
            succeed = true;
        } catch (Exception e) {
            log.error("# redis hash set 出错，key={}，value={}。错误信息：{}", key, map, e);
        }
        return succeed;
    }


    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> commons <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 删除 redis key
     *
     * @param key key
     * @return 删除状态
     */
    public boolean delete(String key) {
        boolean succeed = false;
        try {
            if (stringRedisTemplate.hasKey(key)) {
                succeed = stringRedisTemplate.delete(key);
            } else {
                succeed = true;
            }
        } catch (Exception e) {
            log.error("# redis 删除 key 出错，key={}", key, e);
        }
        return succeed;
    }
}
