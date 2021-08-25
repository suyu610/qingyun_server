package com.qdu.qingyun.service.impl;

import com.qdu.qingyun.entity.PO.MsgPO;
import com.qdu.qingyun.mapper.MsgMapper;
import com.qdu.qingyun.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName MsgService
 * @Description TODO..
 * @Author 23580
 * @Date 2021/6/7 20:20
 * @Version 1.0
 **/
@Service("MsgService")
public class MsgServiceImpl implements MsgService {

    @Autowired
    MsgMapper msgMapper;

    @Override
    public List<MsgPO> getNotReadMsg(String ssNumber, int pageNum) {
        return msgMapper.getMsg(ssNumber,0);
    }

    @Override
    public List<MsgPO> getAllMsg(String ssNumber, int pageNum) {
        return msgMapper.getMsg(ssNumber,1);
    }

    @Override
    public int getUnReadMsgCount(String ssNumber){
        return msgMapper.getUnReadMsgCount(ssNumber);
    }

    @Override
    public boolean setMsgRead(String ssNumber,int msgId){
        return msgMapper.setMsgRead(ssNumber,msgId)>0;
    }

    @Override
    public boolean setMsgDel(String ssNumber, int msgId) {
        return msgMapper.setMsgDel(ssNumber,msgId)>0;
    }


    @Override
    public boolean sendMsg(MsgPO msgPO) {
        return msgMapper.insertMsg(msgPO)>0;
    }
}
