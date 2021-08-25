package com.qdu.qingyun.service.impl;

import com.qdu.qingyun.entity.PO.MsgPO;
import com.qdu.qingyun.entity.VO.MoneyRecordVO;
import com.qdu.qingyun.entity.VO.OrderAddReqVO;
import com.qdu.qingyun.entity.VO.OrderReqVO;
import com.qdu.qingyun.entity.VO.OrderSoldResVO;
import com.qdu.qingyun.mapper.DocMapper;
import com.qdu.qingyun.mapper.OrderMapper;
import com.qdu.qingyun.service.MsgService;
import com.qdu.qingyun.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName OrderServiceImpl
 * @Description TODO..
 * @Author 23580
 * @Date 2021/6/16 19:38
 * @Version 1.0
 **/
@Service("OrderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    DocMapper docMapper;
    @Autowired
    MsgService msgService;

    @Override
    public boolean hasBought(String ssNumber, int docId) {
        return orderMapper.hasBought(ssNumber, docId);
    }

    @Override
    public boolean addOrder(OrderAddReqVO vo) {
        // 做参数检查
        if (vo.getBuyerSsNumber() == null) {
            return false;
        }

        if (vo.getCreateTime() == null) {
            return false;
        }

        Long createTime = Long.parseLong(vo.getCreateTime()); //先用Long接收传过来的参数
        vo.setCreateTimeDateFormat(new Date(createTime));
        String docName = docMapper.getTitle(vo.getDocId());
        msgService.sendMsg(MsgPO.ReceiveOrderToBuyer(vo.getBuyerSsNumber(), docName));

        // todo: 这里要做一下订单确认
        return orderMapper.addOrder(vo) >= 1;
    }

    @Override
    public List<OrderReqVO> getAllOrders(String ssNumber) {
        List<OrderReqVO> orderReqVOList = orderMapper.getAllOrders(ssNumber);
        for (OrderReqVO vo : orderReqVOList
        ) {
            vo.setPreviewImgUrl(docMapper.getSingleImgByDocId(Integer.valueOf(vo.getDocId())));

        }
        return orderReqVOList;
    }

    @Override
    public List<OrderSoldResVO> getAllSoldOrders(String ssNumber) {
        // 先根据学号，找到此人所有的docId
        List<Integer> docIdList = new LinkedList<>();
        docIdList = docMapper.getDocIdBySsNumber(ssNumber);
        if (docIdList.size() != 0) {
            // 然后根据idList,找到所有订单
            List<OrderSoldResVO> orderSoldResList = orderMapper.getAllSoldOrders(docIdList);
            return orderSoldResList;
        }
        return new LinkedList<>();
    }

    @Override
    public MoneyRecordVO getMoneyRecord(String ssNumber) {
        MoneyRecordVO moneyRecordVO = new MoneyRecordVO();
        List<OrderSoldResVO> orderSoldResList = this.getAllSoldOrders(ssNumber);
        List<OrderReqVO> orderResList = this.getAllOrders(ssNumber);
        // 等于出售记录 - 提现记录
        float remainMoney = 0;
        for (OrderSoldResVO vo : orderSoldResList
        ) {
            remainMoney += vo.getPrice() * 100;

        }

        moneyRecordVO.setOrderReqVOList(orderResList);
        moneyRecordVO.setOrderSoldResVOList(orderSoldResList);
        moneyRecordVO.setWithDrawVOList(null);
        moneyRecordVO.setRemainMoney(remainMoney);

        return moneyRecordVO;
    }
}
