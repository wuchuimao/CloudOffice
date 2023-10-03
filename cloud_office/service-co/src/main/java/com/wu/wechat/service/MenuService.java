package com.wu.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.model.wechat.Menu;
import com.wu.vo.wechat.MenuVo;

import java.util.List;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-11 15:23
 * @ Description：
 */
public interface MenuService extends IService<Menu> {
    List<MenuVo> findMenuInfo();

    public void syncMenu();

    void removeMenu();
}
