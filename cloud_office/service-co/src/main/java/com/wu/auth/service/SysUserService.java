package com.wu.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.model.system.SysUser;
import com.wu.vo.system.AssginRoleVo;

import java.util.Map;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-07 12:30
 * @ Description：
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 根据用户获取角色数据
     * @param userId
     * @return
     */
    Map<String, Object> findRoleByUserId(Long userId);

    /**
     * 分配角色
     * @param assginRoleVo
     */
    void doAssign(AssginRoleVo assginRoleVo);

    //更新用户状态
    void updateStatus(Long id, Integer status);
    //根据用户名获取用户
    SysUser getByUsername(String username);
    /**
     * 根据用户名获取用户登录信息
     * @param username
     * @return
     */
    Map<String, Object> getUserInfo(String username);

    Map<String, Object> getCurrentUser();


}
