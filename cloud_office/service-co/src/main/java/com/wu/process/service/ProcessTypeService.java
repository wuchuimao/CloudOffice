package com.wu.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.model.process.ProcessType;

import java.util.List;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-09 19:38
 * @ Description：
 */
public interface ProcessTypeService extends IService<ProcessType> {
    List<ProcessType> findProcessType();

}
