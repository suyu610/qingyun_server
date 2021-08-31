package com.qdu.qingyun.constant;

/**
 * @ClassName MsgType

 * @Date 2021/6/19 0:38
 * @Version 1.0
 **/
public enum MsgType {
/******************************************************************
 * 【 系统自动发送的 】
 *           1  用户购买后，通知 [购买者] ”收到订单，预计什么时候送达“
 *           2  用户购买后，通知 [管理员] “有人购买”
 *           3  用户购买后，通知 [文档拥有者] “有人购买你的文档”
 *
 * ****************************************************************
 * 【 管理员触发的 】
 *           4  管理员点击送货，通知 [购买者] ”正在送货中“
 *          *5  管理员通过用户的商品评论，通知 [评论者] ”你的评论被通过“
 *          *6  管理员拒绝用户的商品评论，通知 [评论者] ”你的评论被通过“
 *           7  管理员通过用户的商品评论，通知 [文档拥有者] ”你的文章有新的评论“
 *           8  管理员通过用户文档，通知 [文档拥有者] ”你的文档被通过“
 *           9  管理员拒绝用户文档，通知 [文档拥有者] ”你的文档被拒绝“
 *           10  管理员设置精选用户文档，通知 [文档拥有者] ”你的文档被精选“
 *
 * ****************************************************************
 * 【 用户触发的 】
 *          *11 用户提交新的评论，通知 [管理员] ”有新的评论需要审核“
 *           12 用户提交新的文档，通知 [管理员] ”有新的文档需要审核“
 *
 */

//  系统自动发送的
    ReceiveOrderToBuyer,
    ReceiveOrderToAdmin,
    ReceiveOrderToOwner,
//  管理员触发的
    ExpressingToBuyer,
    CommentAcceptToBuyer,
    CommentRejectToBuyer,
    CommentAcceptToOwner,
    DocAcceptToOwner,
    DocRejectToOwner,
    DocHotToOwner,
//  用户触发的
    CommentSubmitToAdmin,
    DocSubmitToAdmin
}
