package com.wu.auth.service.serviceImpl;

import com.wu.auth.service.SysMenuService;
import com.wu.auth.service.SysUserService;
import com.wu.model.system.SysUser;
import com.wu.security.custom.CustomUser;
import com.wu.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-08 19:30
 * @ Description：
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUsername(username);
        if(null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if(sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("账号已停用");
        }
        //根据userId查询用户权限数据
        List<String> userPermsList = sysMenuService.findUserPermsList(sysUser.getId());
        //创建list集合，封装最终的权限数据
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //查询list集合遍历
        for (String perm : userPermsList) {
            authorities.add(new SimpleGrantedAuthority(perm.trim()));
        }
        return new CustomUser(sysUser, authorities);
    }
}
