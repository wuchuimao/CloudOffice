package com.wu.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.model.process.ProcessRecord;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-10 21:57
 * @ Description：
 */
public interface ProcessRecordService extends IService<ProcessRecord> {

    void record(Long processId, Integer status, String description);
}
