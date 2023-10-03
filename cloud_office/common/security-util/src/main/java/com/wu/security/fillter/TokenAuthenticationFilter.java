package com.wu.security.fillter;

import com.alibaba.fastjson.JSON;
import com.wu.common.config.exception.WuException;
import com.wu.common.jwt.JwtHelper;
import com.wu.common.result.Result;
import com.wu.common.result.ResultCodeEnum;
import com.wu.common.util.ResponseUtil;
import com.wu.security.custom.LoginUserInfoHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-08 20:11
 * @ Description：用来判断是否已经登入,是登入路径则到TokenLoginFilter校验，不是登入路径则判断是否有token，有,且可以去除userName则放行，无则拒绝访问
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        logger.info("uri:"+request.getRequestURI());
        //如果是登录接口，直接放行
        if("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_AUTH));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        System.out.println("..............................是否登入判断");
        // token置于header里
        String token = request.getHeader("token");
        logger.info("token:"+token);
        if (!StringUtils.isEmpty(token)) {
            String username = JwtHelper.getUsername(token);
            logger.info("useruame:"+username);
            if (!StringUtils.isEmpty(username)) {
                //通过ThreadLocal记录当前登录人信息
                LoginUserInfoHelper.setUserId(JwtHelper.getUserId(token));
                LoginUserInfoHelper.setUsername(username);

                //通过userName从redis中获取权限数据
                String authoritiesString = (String) redisTemplate.opsForValue().get(username);
                //把redis中的字符串权限数据转换为要求的集合类型List<SimpleGrantedAuthority>
                List<Map> mapList = JSON.parseArray(authoritiesString, Map.class);
                if(CollectionUtils.isEmpty(mapList)){
                    throw new WuException(ResultCodeEnum.LOGIN_AUTH);
                }
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for (Map map : mapList) {
//                    System.out.println(map.get("authority")+"..........................................");
                    authorities.add(new SimpleGrantedAuthority((String)map.get("authority")));
                }
                return new UsernamePasswordAuthenticationToken(username, null, authorities);
            }else {
                return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
            }
        }
        return null;
    }
}
