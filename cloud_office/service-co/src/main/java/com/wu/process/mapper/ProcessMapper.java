package com.wu.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.vo.process.ProcessQueryVo;
import com.wu.vo.process.ProcessVo;
import com.wu.model.process.Process;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-10 15:15
 * @ Description：
 */
@Mapper
public interface ProcessMapper extends BaseMapper<Process> {

    IPage<ProcessVo> selectPage(Page<ProcessVo> page, @Param("vo") ProcessQueryVo processQueryVo);
}
