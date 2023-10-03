package com.wu.wechat.service.serviceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.model.wechat.Menu;
import com.wu.vo.wechat.MenuVo;
import com.wu.wechat.mapper.MenuMapper;
import com.wu.wechat.service.MenuService;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-11 15:24
 * @ Description：
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private WxMpService wxMpService;

    //获取微信菜单
    @Override
    public List<MenuVo> findMenuInfo() {
        List<MenuVo> list = new ArrayList<>();
        //查询所有微信菜单
        List<Menu> menuList = baseMapper.selectList(null);

        //将查询的微信菜单Menu转换为树形的MenuVo
        for (Menu oneMenu: menuList) {
            if (oneMenu.getParentId() == 0){
                MenuVo oneMenuVo = new MenuVo();
                BeanUtils.copyProperties(oneMenu, oneMenuVo);
                List<MenuVo> childrenList = findChildrenList(oneMenu, menuList);
                oneMenuVo.setChildren(childrenList);
                list.add(oneMenuVo);
            }

        }
        return list;
    }

    //递归查找子菜单
    private List<MenuVo> findChildrenList(Menu menu, List<Menu> list) {
        ArrayList<MenuVo> childrenList = new ArrayList<>();
        for (Menu menu1: list){
            if (menu1.getParentId().longValue() == menu.getId().longValue()){
                MenuVo menuVo = new MenuVo();
                BeanUtils.copyProperties(menu1, menuVo);
                childrenList.add(menuVo);
                menuVo.setChildren(findChildrenList(menu1, list));
            }

        }
        return childrenList;
    }
    //推送菜单
    @Override
    public void syncMenu() {
        //菜单数据查询，封装为微信要求格式
        List<MenuVo> menuVoList = this.findMenuInfo();
        //菜单(微信约定好的格式)
        JSONArray buttonList = new JSONArray();
        for(MenuVo oneMenuVo : menuVoList) {
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());
            if(CollectionUtils.isEmpty(oneMenuVo.getChildren())) {
                one.put("type", oneMenuVo.getType());
                // http://cloudofficefront.v5.idcfengye.com域名是为了微信端可以通过域名访问员工端界面。
                one.put("url", "http://cloudofficefront.v5.idcfengye.com/#"+oneMenuVo.getUrl());
            } else {
                JSONArray subButton = new JSONArray();
                for(MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                    JSONObject view = new JSONObject();
                    view.put("type", twoMenuVo.getType());
                    if(twoMenuVo.getType().equals("view")) {
                        view.put("name", twoMenuVo.getName());
                        //H5页面地址
                        view.put("url", "http://cloudofficefront.v5.idcfengye.com/#"+twoMenuVo.getUrl());
                    } else {
                        view.put("name", twoMenuVo.getName());
                        view.put("key", twoMenuVo.getMeunKey());
                    }
                    subButton.add(view);
                }
                one.put("sub_button", subButton);
            }
            buttonList.add(one);
        }
        //菜单
        JSONObject button = new JSONObject();
        button.put("button", buttonList);
        try {
            //封装好的菜单进行推送
            wxMpService.getMenuService().menuCreate(button.toJSONString());
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Override
    public void removeMenu() {
        wxMpService.getMenuService().menuDelete();
    }
}
