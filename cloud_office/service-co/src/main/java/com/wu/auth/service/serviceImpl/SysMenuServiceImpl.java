package com.wu.auth.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.auth.mapper.SysMenuMapper;
import com.wu.auth.mapper.SysRoleMenuMapper;
import com.wu.auth.service.SysMenuService;
import com.wu.auth.service.helper.MenuHelper;
import com.wu.common.config.exception.WuException;
import com.wu.model.system.SysMenu;
import com.wu.model.system.SysRoleMenu;
import com.wu.vo.system.AssginMenuVo;
import com.wu.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-07 17:51
 * @ Description：
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> findNodes() {
        //全部权限列表
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        if (CollectionUtils.isEmpty(sysMenuList)) return null;

        //构建树形数据
        List<SysMenu> result = MenuHelper.buildTree(sysMenuList);
        return result;
    }

    @Override
    public boolean removeById(Long id) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, id);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new WuException(201,"多级菜单不能删除");
        }
        baseMapper.deleteById(id);
        return false;
    }
    /**
     * 根据角色获取授权权限数据
     * @return
     */
    @Override
    public List<SysMenu> findSysMenuByRoleId(Long roleId) {
        //全部菜单列表
        LambdaQueryWrapper<SysMenu> sysMenuWrapper = new LambdaQueryWrapper<>();
        sysMenuWrapper.eq(SysMenu::getStatus, 1);
        List<SysMenu> sysMenuList = baseMapper.selectList(sysMenuWrapper);

        //根据角色id获取对应菜单id
        LambdaQueryWrapper<SysRoleMenu> sysRoleMemuWrapper = new LambdaQueryWrapper<>();
        sysRoleMemuWrapper.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(sysRoleMemuWrapper);
        List<Long> sysRoleMenuIdList = sysRoleMenus.stream().map(c -> c.getMenuId()).collect(Collectors.toList());

        //根据角色对应的菜单id列表将所有菜单中对应的菜单进行选中
        for (SysMenu menu : sysMenuList) {
           if (sysRoleMenuIdList.contains(menu.getId())){
               menu.setSelect(true);
           }else{
               menu.setSelect(false);
           }
        }
        List<SysMenu> resultSysMenuList = MenuHelper.buildTree(sysMenuList);
        return resultSysMenuList;
    }

    /**
     * 保存角色权限
     * @param  assginMenuVo
     */
    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        //根据roleId先删除role对应得所有菜单
        LambdaQueryWrapper<SysRoleMenu> warpper = new LambdaQueryWrapper<>();
        warpper.eq(SysRoleMenu::getRoleId, assginMenuVo.getRoleId());
        sysRoleMenuMapper.delete(warpper);
        //再重新给角色分配菜单
        for (Long menuId : assginMenuVo.getMenuIdList()) {
            if (StringUtils.isEmpty(menuId)) continue;
            SysRoleMenu rolePermission = new SysRoleMenu();
            rolePermission.setRoleId(assginMenuVo.getRoleId());
            rolePermission.setMenuId(menuId);
            sysRoleMenuMapper.insert(rolePermission);
        }
    }

    /**
     * 根据用户id获取用户菜单
     * @param userId
     * @return
     */
    @Override
    public List<RouterVo> findUserMenuList(Long userId) {

        List<SysMenu> sysMenuList = null;

        //超级管理员admin账号id为：1
        if (userId.longValue() == 1) {
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus, 1).orderByAsc(SysMenu::getSortValue);
            sysMenuList = baseMapper.selectList(wrapper);
        } else {
            sysMenuList = baseMapper.findListByUserId(userId);
        }
        //构建树形数据
        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(sysMenuList);

        List<RouterVo> routerVoList = MenuHelper.buildMenus(sysMenuTreeList);
        return routerVoList;
    }

    /**
     * 获取用户按钮权限
     * @param userId
     * @return
     */
    public List<String> findUserPermsList(Long userId){
        //超级管理员admin账号id为：1
        List<SysMenu> sysMenuList = null;
        if (userId.longValue() == 1) {
            sysMenuList = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1));
        } else {
            sysMenuList = baseMapper.findListByUserId(userId);
        }
        List<String> permsList = sysMenuList.stream()
                .filter(item -> item.getType() == 2)
                .map(item -> item.getPerms())
                .collect(Collectors.toList());
        return permsList;
    }

}
