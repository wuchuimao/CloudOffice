package com.wu.auth.controller;

import com.wu.auth.service.SysUserService;
import com.wu.common.config.exception.WuException;
import com.wu.common.jwt.JwtHelper;
import com.wu.common.result.Result;
import com.wu.common.util.MD5;
import com.wu.model.system.SysUser;
import com.wu.vo.system.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-06 10:22
 * @ Description：
 */
@Api(tags = "后台登入管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录
     * @return
     */
    @ApiOperation(value = "登入")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        System.out.println("..........................放行到IndexController");

        SysUser sysUser = sysUserService.getByUsername(loginVo.getUsername());
        if (sysUser == null)
            throw new WuException(201,"用户不存在");
        if (!sysUser.getPassword().equals(MD5.encrypt(loginVo.getPassword())))
            throw new WuException(201, "密码错误");
        if (sysUser.getStatus() == 0)
            throw new WuException(201, "用户被禁用，请联系管理员");
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", JwtHelper.createToken(sysUser.getId(), sysUser.getUsername()));
        return Result.ok(map);
    }
    /**
     * 获取用户信息
     * @return
     */
    @ApiOperation(value = "获取用户信息")
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        System.out.println("............................IndexController info");
        String username = JwtHelper.getUsername(request.getHeader("token"));
        Map<String, Object> map = sysUserService.getUserInfo(username);
        return Result.ok(map);
    }
    /**
     * 退出
     * @return
     */
    @PostMapping("logout")
    public Result logout(){
        System.out.println("............................IndexController out");
        return Result.ok();
    }
}
