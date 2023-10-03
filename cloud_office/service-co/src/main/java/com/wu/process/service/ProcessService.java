package com.wu.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.vo.process.ApprovalVo;
import com.wu.vo.process.ProcessFormVo;
import com.wu.model.process.Process;
import com.wu.vo.process.ProcessQueryVo;
import com.wu.vo.process.ProcessVo;

import java.util.Map;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-10 15:17
 * @ Description：
 */
public interface ProcessService extends IService<Process> {

    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);

    void deployByZip(String deployPath);

    public void startUp(ProcessFormVo processFormVo);

    IPage<ProcessVo> findPending(Page<Process> pageParam);

    Map<String, Object> show(Long id);

    void approve(ApprovalVo approvalVo);

    IPage<ProcessVo> findProcessed(Page<Process> pageParam);

    IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam);



}
