package com.qdu.qingyun.service;

import com.qdu.qingyun.entity.VO.MoneyRecordVO;
import com.qdu.qingyun.entity.VO.OrderAddReqVO;
import com.qdu.qingyun.entity.VO.OrderReqVO;
import com.qdu.qingyun.entity.VO.OrderSoldResVO;

import java.util.List;

public interface OrderService {
    boolean hasBought(String ssNumber,int docId);
    boolean addOrder(OrderAddReqVO vo);
    List<OrderReqVO> getAllOrders(String ssNumber);
    List<OrderSoldResVO> getAllSoldOrders(String ssNumber);
    MoneyRecordVO getMoneyRecord(String ssNumber);
}
