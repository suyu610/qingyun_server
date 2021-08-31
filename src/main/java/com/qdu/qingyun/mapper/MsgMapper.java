package com.qdu.qingyun.mapper;

import com.qdu.qingyun.entity.Msg.MsgPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MsgMapper {
    List<MsgPO> getMsg(@Param("ssNumber") String ssNumber,@Param("flag") int flag);
    int getUnReadMsgCount(String ssNumber);
    int setMsgRead(String ssNumber,int msgId);
    int setMsgDel(String ssNumber,int msgId);
    int insertMsg(MsgPO po);
}
