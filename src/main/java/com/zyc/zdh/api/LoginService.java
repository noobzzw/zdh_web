package com.zyc.zdh.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.TaskLogsMapper;
import com.zyc.zdh.entity.EtlEcharts;
import com.zyc.zdh.shiro.MyAuthenticationToken;
import com.zyc.zdh.shiro.MyRealm;
import com.zyc.zdh.shiro.SessionDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.zyc.zdh.entity.ResultInfo;
import com.zyc.zdh.entity.User;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: LoginService
 *
 * @author zyc-admin
 * @date 2018年2月5日
 * @Description: cloud.api包下的服务 不需要通过shiro验证拦截，需要自定义的token验证
 */
@Controller("loginService")
@RequestMapping("api")
@Slf4j
public class LoginService {
    @Autowired
    SessionDao sessionDao;
    @Autowired
    MyRealm myRealm;
    @Resource
    TaskLogsMapper taskLogsMapper;

    @RequestMapping("login")
    @ResponseBody
    @CrossOrigin
    public ResultInfo login(@RequestParam String username,
                            @RequestParam String password) {
        //在自己登录的rest里面写，比如UserRest里面的login方法中，user为传递过来的参数
        Subject currentUser = SecurityUtils.getSubject();
        final User user = new User(username, password);
        MyAuthenticationToken token = new MyAuthenticationToken(user.getUsername(), user.getPassword(), false, "", "", "");
        // 开始进入shiro的认证流程
        currentUser.login(token);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(ResultInfo.Code.Success.getValue());
        resultInfo.setMessage("完成认证");
        resultInfo.setData(currentUser.getSession());
        resultInfo.setToken(currentUser.getSession().getId().toString());
        // 设置session的过期时间
        sessionDao.getRedisUtil()
                .getRedisTemplate()
                .expire(sessionDao.getCacheKey(currentUser.getSession().getId().toString()) , 7 * 24, TimeUnit.HOURS);
        return resultInfo;
    }

    @RequestMapping("test")
    @ResponseBody
    public ResultInfo test(String token) {

        ResultInfo resultInfo = new ResultInfo();

        resultInfo.setCode(ResultInfo.Code.IllegalToken.getValue());
        resultInfo.setMessage("请先完成认证");
        resultInfo.setData("");
        if (valid(token)) {

            try{
                Session se=sessionDao.readSession(token);
                Object obj = se.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                SimplePrincipalCollection coll = (SimplePrincipalCollection) obj;
                User user=(User) coll.getPrimaryPrincipal();

                if(user !=null) {
                    resultInfo.setCode(ResultInfo.Code.Success.getValue());
                    resultInfo.setMessage("以完成认证");
                    resultInfo.setData(token);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return resultInfo;
    }

    @RequestMapping("logout")
    @ResponseBody
    public ResultInfo logout(String token) {

        ResultInfo resultInfo = new ResultInfo();
        if (valid(token)) {

            Session se=sessionDao.readSession(token);

            Object obj = se.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            SimplePrincipalCollection coll = (SimplePrincipalCollection) obj;
            User user=(User) coll.getPrimaryPrincipal();

            if(user !=null){
                Cache<Object,AuthenticationInfo> cache=myRealm.getAuthenticationCache();
                if (cache!=null && user !=null){
                    cache.remove(user.getUsername());
                }
            }
            SecurityUtils.getSubject().logout();
            sessionDao.getActiveSessionsCache().remove(token);

            resultInfo.setCode(ResultInfo.Code.Success.getValue());
            resultInfo.setMessage("退出成功");
            resultInfo.setData(token);
        } else {
            resultInfo.setCode(ResultInfo.Code.IllegalToken.getValue());
            resultInfo.setMessage("请先完成认证");
            resultInfo.setData("");
        }
        return resultInfo;
    }

    @RequestMapping("report")
    @ResponseBody
    public String report(User user) {

       List<EtlEcharts> a= taskLogsMapper.slectByOwner("1");

       return JSON.toJSONString(a);
    }

    /**
     * 验证token 是否有效
     * @param token
     * @return
     */
    private boolean valid(String token) {
        try {
            sessionDao.readSession(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/user/info",method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo getUserInfo(@RequestParam String token) {
        // 根据token获取用户信息
        Session se = sessionDao.readSession(token);
        Object obj = se.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        SimplePrincipalCollection simplePrincipalCollection = (SimplePrincipalCollection) obj;
        User user = (User) simplePrincipalCollection.getPrimaryPrincipal();
        log.info("For this token ,get user : {}",user);
        // 拼接返回信息
        final ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(ResultInfo.Code.Success.getValue());
        resultInfo.setMessage("成功获取user info");
        // todo 暂时设置权限全为admin
        final JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("admin");
        result.put("roles",jsonArray.toJSONString());
        result.put("avatar",user.getImageUrl());
        resultInfo.setData(result);
        return resultInfo;
    }

}
