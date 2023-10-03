package com.wu.wechat.service;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-12 15:37
 * @ Description：
 */
public interface MessageService {

    /**
     * 推送待审批人员
     * @param processId
     * @param userId
     * @param taskId
     */
    void pushPendingMessage(Long processId, Long userId, String taskId);

    /**
     * 审批后推送提交审批人员
     * @param processId
     * @param userId
     * @param status
     */
    void pushProcessedMessage(Long processId, Long userId, Integer status);
}
