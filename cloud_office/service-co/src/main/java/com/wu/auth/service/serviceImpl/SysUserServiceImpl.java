package com.wu.auth.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.auth.mapper.SysRoleMapper;
import com.wu.auth.mapper.SysUserMapper;
import com.wu.auth.mapper.SysUserRoleMapper;
import com.wu.auth.service.SysMenuService;
import com.wu.auth.service.SysUserService;
import com.wu.model.system.SysRole;
import com.wu.model.system.SysUser;
import com.wu.model.system.SysUserRole;
import com.wu.security.custom.LoginUserInfoHelper;
import com.wu.vo.system.AssginRoleVo;
import com.wu.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-07 12:32
 * @ Description：
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 根据用户获取角色数据
     * @param userId
     * @return
     */

    @Override
    public Map<String, Object> findRoleByUserId(Long userId) {
        //查询所有的角色
        List<SysRole> SysRoles = sysRoleMapper.selectList(null);

        //查询用户拥有的角色id
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(wrapper);
        List<Long> sysUserRoleIds = sysUserRoles.stream().map(c -> c.getRoleId()).collect(Collectors.toList());

        //对角色进行分类
        List<SysRole> assginRoleList = new ArrayList<>();
        for (SysRole role : SysRoles) {
            //已分配
            if(sysUserRoleIds.contains(role.getId())) {
                assginRoleList.add(role);
            }
        }

        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assginRoleList", assginRoleList);
        roleMap.put("allRolesList", SysRoles);
        return roleMap;
    }

    /**
     * 重新为用户分配角色
     * @param assginRoleVo
     */
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        //根据userid先删除user对应得所有角色
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,assginRoleVo.getUserId());
        sysUserRoleMapper.delete(wrapper);
        //再重新分配角色
        for (Long roleId : assginRoleVo.getRoleIdList()) {
            if(StringUtils.isEmpty(roleId)) continue;
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(assginRoleVo.getUserId());
            userRole.setRoleId(roleId);
            sysUserRoleMapper.insert(userRole);
        }
    }


    //根据用户id更新用户状态
    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser sysUser = baseMapper.selectById(id);
        if(status.intValue() == 1) {
            sysUser.setStatus(status);
        } else {
            sysUser.setStatus(0);
        }
        baseMapper.updateById(sysUser);
    }

    @Override
    public SysUser getByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = baseMapper.selectOne(wrapper);
        return sysUser;
    }

    /**
     * 根据用户名获取用户登录信息
     * @param username
     * @return
     */
    public Map<String, Object> getUserInfo(String username){
        Map<String, Object> result = new HashMap<>();
        SysUser sysUser = this.getByUsername(username);

        //根据用户id获取菜单权限值
        List<RouterVo> routerVoList = sysMenuService.findUserMenuList(sysUser.getId());
        //根据用户id获取用户按钮权限
        List<String> permsList = sysMenuService.findUserPermsList(sysUser.getId());

        result.put("name", sysUser.getName());
        result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        result.put("roles",  new HashSet<>());
        result.put("buttons", permsList);
        result.put("routers", routerVoList);
        return result;
    }

    //根据id获取当前用户信息，并封装为map
    @Override
    public Map<String, Object> getCurrentUser() {
        SysUser sysUser = baseMapper.selectById(LoginUserInfoHelper.getUserId());
        //SysDept sysDept = sysDeptService.getById(sysUser.getDeptId());
        //SysPost sysPost = sysPostService.getById(sysUser.getPostId());
        Map<String, Object> map = new HashMap<>();
        map.put("name", sysUser.getName());
        map.put("phone", sysUser.getPhone());
        //map.put("deptName", sysDept.getName());
        //map.put("postName", sysPost.getName());
        return map;
    }
}
