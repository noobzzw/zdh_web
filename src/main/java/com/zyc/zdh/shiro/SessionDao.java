package com.zyc.zdh.shiro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;

@Slf4j
public class SessionDao extends EnterpriseCacheSessionDAO {
    public String getCacheKey(String token) {
        return cacheKey + token;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    private String cacheKey = "shiro:cache:shiro-activeSessionCache1:";

    private RedisUtil redisUtil;

    public RedisUtil getRedisUtil() {
        return redisUtil;
    }

    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    // 创建session，保存到数据库
    @Override
    protected Serializable doCreate(Session session) {
        super.doCreate(session);
        log.info("Do create session :{}",session.getId());
        getCacheManager().getCache("shiro-activeSessionCache1").put(session.getId().toString(), session);
        return session.getId();
    }

    // 获取session
    @Override
    protected Session doReadSession(Serializable sessionId) {
        return getActiveSessionsCache().get(sessionId);
    }

    // 更新session的最后一次访问时间
    @Override
    protected void doUpdate(Session session) {
        log.info("Do update session : {}",session.getId());
        if (getActiveSessionsCache().get(session.getId().toString()) != null) {
            getActiveSessionsCache().put(session.getId().toString(), session);
        }
    }

    // 删除session
    @Override
    protected void doDelete(Session session) {
        log.info("Do delete session : {}",session.getId());
        getActiveSessionsCache().remove(session.getId().toString());
    }
}
