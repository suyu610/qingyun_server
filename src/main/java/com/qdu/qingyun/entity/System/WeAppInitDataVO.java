package com.qdu.qingyun.entity.System;

import com.qdu.qingyun.entity.Doc.DocItemVO;
import com.qdu.qingyun.entity.User.UserInitDataBO;
import com.qdu.qingyun.entity.Ad.AdPO;
import com.qdu.qingyun.entity.Msg.MsgPO;
import com.qdu.qingyun.entity.Doc.CategoryItemVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/*
 * @Author uuorb
 * @Description 当用户打开app时，返回一些初始信息
 * @Date 2021/6/7 14:17
 * @Param
 * @return
 *  token, 热门文档, 已购清单, 余额, 消息, 上传数, 购买数, 分类，顶部banner图，[青空优选相关的]
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeAppInitDataVO implements Serializable {
    String token;
    UserInitDataBO userInitDataBO;

    CategoryItemVO category;

    // 展示在首页的热门文档
    List<DocItemVO> hotDoc;
    // 消息列表
    List<MsgPO> msgList;

    // 顶部轮播图
    List<AdPO> adList;
}
