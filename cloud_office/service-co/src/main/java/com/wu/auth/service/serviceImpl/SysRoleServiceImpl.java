package com.wu.auth.service.serviceImpl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.auth.mapper.SysRoleMapper;
import com.wu.auth.service.SysRoleService;
import com.wu.model.system.SysRole;
import org.springframework.stereotype.Service;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-05 16:07
 * @ Description：
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
