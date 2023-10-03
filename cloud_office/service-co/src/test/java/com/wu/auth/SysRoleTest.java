package com.wu.auth;

import com.wu.auth.mapper.SysRoleMapper;
import com.wu.auth.service.SysRoleService;
import com.wu.model.system.SysRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-05 14:56
 * @ Description：junit4: @SpringBootTest, RunWith(SpringRunner.class), org.junit.Test
 *                junit5: @SpringBootTest, org.junit.jupiter.api.Test
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SysRoleTest {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleService sysRoleService;

    @Test
    public void testSelectList(){
        System.out.println("..............SelectAll method test............");
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        sysRoles.forEach(System.out::println);

    }

    @Test
    public void testSelectAll(){
        System.out.println("..............SelectAll method test............");
        List<SysRole> roles = sysRoleService.list();
        roles.forEach(System.out::println);
    }
}
