package com.wu.process.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.auth.service.SysUserService;
import com.wu.model.process.ProcessRecord;
import com.wu.model.system.SysUser;
import com.wu.process.mapper.ProcessRecordMapper;
import com.wu.process.service.ProcessRecordService;
import com.wu.security.custom.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-10 22:02
 * @ Description：
 */
@Service
public class ProcessRecordServiceImpl extends ServiceImpl<ProcessRecordMapper, ProcessRecord> implements ProcessRecordService {
    @Autowired
    private ProcessRecordMapper processRecordMapper;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public void record(Long processId, Integer status, String description) {

        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());

        ProcessRecord processRecord = new ProcessRecord();

        processRecord.setProcessId(processId);
        processRecord.setStatus(status);
        processRecord.setDescription(description);
        processRecord.setOperateUserId(sysUser.getId());
        processRecord.setOperateUser(sysUser.getName());
        processRecordMapper.insert(processRecord);

    }
}
