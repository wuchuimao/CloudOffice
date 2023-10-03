package com.wu.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wu.model.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-07 17:49
 * @ Description：
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    @Select("select distinct " +
            "m.id,m.parent_id,m.name,m.type,m.path,m.component,m.perms,m.icon,m.sort_value,m.status,m.create_time,m.update_time,m.is_deleted " +
            "from sys_menu m " +
            "inner join sys_role_menu rm on rm.menu_id = m.id " +
            "inner join sys_user_role ur on ur.role_id = rm.role_id " +
            "where ur.user_id = #{userId} " +
            "and m.status = 1 " +
            "and rm.is_deleted = 0 " +
            "and ur.is_deleted = 0 " +
            "and m.is_deleted = 0"
    )
    List<SysMenu> findListByUserId(Long userId);
}
