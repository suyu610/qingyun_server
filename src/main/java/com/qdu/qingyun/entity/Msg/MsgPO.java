package com.qdu.qingyun.entity.Msg;

import com.qdu.qingyun.constant.MsgType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName MsgPO

 * @Date 2021/6/18 23:18
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsgPO implements Serializable {
 /* 【 系统自动发送的 】
  *           1  用户购买后，通知 [购买者] ”收到订单，预计什么时候送达“ ReceiveOrderToBuyer
  *           2  用户购买后，通知 [管理员] “有人购买”  ReceiveOrderToAdmin
  *           3  用户购买后，通知 [文档拥有者] “有人购买你的文档” ReceiveOrderToOwner
  *
  * ****************************************************************
  * 【 管理员触发的 】
  *           4  管理员点击送货，通知 [购买者] ”正在送货中“ ExpressingToBuyer
  *          *5  管理员通过用户的商品评论，通知 [评论者] ”你的评论被通过“ CommentAcceptToBuyer
  *          *6  管理员拒绝用户的商品评论，通知 [评论者] ”你的评论被通过“ CommentRejectToBuyer
  *           7  管理员通过用户的商品评论，通知 [文档拥有者] ”你的文章有新的评论“ CommentAcceptToOwner
  *           8  管理员通过用户文档，通知 [文档拥有者] ”你的文档被通过“ DocAcceptToOwner
  *           9  管理员拒绝用户文档，通知 [文档拥有者] ”你的文档被拒绝“ DocRejectToOwner
  *           10  管理员设置精选用户文档，通知 [文档拥有者] ”你的文档被精选“ DocHotToOwner
  *
  * ****************************************************************
  * 【 用户触发的 】
  *          *11 用户提交新的评论，通知 [管理员] ”有新的评论需要审核“ CommentSubmitToAdmin
  *           12 用户提交新的文档，通知 [管理员] ”有新的文档需要审核“ DocSubmitToAdmin
  */


    public static MsgPO ReceiveOrderToBuyer(String ssNumber,String docName){
        MsgPO msgPO = new MsgPO();
        msgPO.setUserNumber(ssNumber);
        msgPO.setTitle("收到 《"+docName+"》 订单");
        msgPO.setContent("我们已收到你的订单，正在处理");
        msgPO.setAuthorSsNumber("官方");
        msgPO.setMsgType(MsgType.ReceiveOrderToBuyer.ordinal());
        return msgPO;
    }

    public static MsgPO ExpressingToBuyer(String ssNumber,String docName){
        MsgPO msgPO = new MsgPO();
        msgPO.setUserNumber(ssNumber);
        msgPO.setTitle("我们正在为您送货");
        msgPO.setContent("我们正在为您送《"+docName+"》，请您注意接听电话");
        msgPO.setAuthorSsNumber("官方");
        msgPO.setMsgType(MsgType.ExpressingToBuyer.ordinal());
        return msgPO;
    }

    int id;
    Date createTime;
    String userNumber;
    String title;
    String content;
    boolean isSend;
    boolean isRead;
    boolean DELETED;
    int msgType;
    String authorSsNumber;
}
