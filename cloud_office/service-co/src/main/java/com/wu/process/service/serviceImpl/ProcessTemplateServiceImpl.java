package com.wu.process.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.common.config.exception.WuException;
import com.wu.model.process.ProcessTemplate;
import com.wu.model.process.ProcessType;
import com.wu.process.mapper.ProcessTemplateMapper;
import com.wu.process.service.ProcessService;
import com.wu.process.service.ProcessTemplateService;
import com.wu.process.service.ProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-10 10:20
 * @ Description：
 */
@Service
public class ProcessTemplateServiceImpl  extends ServiceImpl<ProcessTemplateMapper, ProcessTemplate> implements ProcessTemplateService {

    @Autowired
    private ProcessTemplateMapper processTemplateMapper;

    @Autowired
    private ProcessTypeService processTypeService;

    @Autowired
    private ProcessService processService;

    @Override
    public IPage<ProcessTemplate> selectPage(Page<ProcessTemplate> pageParam) {
        //查询所有排序审批模板列表，再获取审批类型id列表
        //查询所有排序审批模板列表
        LambdaQueryWrapper<ProcessTemplate> queryWrapper = new LambdaQueryWrapper<ProcessTemplate>();
        queryWrapper.orderByDesc(ProcessTemplate::getId);
        IPage<ProcessTemplate> page = processTemplateMapper.selectPage(pageParam, queryWrapper);
        List<ProcessTemplate> processTemplateList = page.getRecords();
        //再获取审批类型id列表
        List<Long> processTypeIdList = processTemplateList
                .stream()
                .map(processTemplate -> processTemplate.getProcessTypeId())
                .collect(Collectors.toList());

        //通过审批类型id获取审批类型name
        if(!CollectionUtils.isEmpty(processTypeIdList)) {
            Map<Long, ProcessType> processTypeIdToProcessTypeMap = processTypeService.list(
                    new LambdaQueryWrapper<ProcessType>()
                            .in(ProcessType::getId, processTypeIdList))
                            .stream()
                            .collect(Collectors.toMap(ProcessType::getId, ProcessType -> ProcessType));
            //将审批类型name赋给ProcessTemplate中的processTypeName属性
            for(ProcessTemplate processTemplate : processTemplateList) {
                ProcessType processType = processTypeIdToProcessTypeMap.get(processTemplate.getProcessTypeId());
                if(null == processType) continue;
                processTemplate.setProcessTypeName(processType.getName());
            }
        }
        return page;
    }

    @Override
    public void publish(Long id) {
        ProcessTemplate processTemplate = this.getById(id);


        if(!StringUtils.isEmpty(processTemplate.getProcessDefinitionPath())) {
            processService.deployByZip(processTemplate.getProcessDefinitionPath());
        }

        processTemplate.setStatus(1);
        processTemplateMapper.updateById(processTemplate);
    }

}
