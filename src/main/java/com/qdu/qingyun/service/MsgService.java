package com.qdu.qingyun.service;

import com.qdu.qingyun.entity.PO.MsgPO;

import java.util.List;

/**
 * @ClassName MsgService
 * @Description 与消息相关的Service
 * @Author 23580
 * @Date 2021/6/7 20:20
 * @Version 1.0
 **/

public interface MsgService {

    // 获取没有被删除的所有消息
    List<MsgPO> getNotReadMsg(String ssNumber,int pageNum);
    List<MsgPO> getAllMsg(String ssNumber,int pageNum);

    // 获取
    int getUnReadMsgCount(String ssNumber);

    boolean setMsgRead(String ssNumber, int msgId);
    boolean setMsgDel(String ssNumber, int msgId);

    boolean sendMsg(MsgPO msgPO);
}
