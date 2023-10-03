package com.wu.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.model.system.SysMenu;
import com.wu.vo.system.AssginMenuVo;
import com.wu.vo.system.RouterVo;

import java.util.List;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-07 17:50
 * @ Description：
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 菜单树形数据
     * @return
     */
    List<SysMenu> findNodes();

    //删除菜单
    public boolean removeById(Long id);

    /**
     * 根据角色获取授权权限数据
     * @return
     */
    List<SysMenu> findSysMenuByRoleId(Long roleId);

    /**
     * 保存角色权限
     * @param  assginMenuVo
     */
    void doAssign(AssginMenuVo assginMenuVo);

    /**
     * 获取用户菜单
     * @param userId
     * @return
     */
    List<RouterVo> findUserMenuList(Long userId);

    /**
     * 获取用户按钮权限
     * @param userId
     * @return
     */
    List<String> findUserPermsList(Long userId);

}
