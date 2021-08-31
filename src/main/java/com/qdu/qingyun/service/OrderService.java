package com.qdu.qingyun.service;

import com.qdu.qingyun.entity.Order.MoneyRecordVO;
import com.qdu.qingyun.entity.Order.OrderAddReqVO;
import com.qdu.qingyun.entity.Order.OrderReqVO;
import com.qdu.qingyun.entity.Order.OrderSoldResVO;

import java.util.List;

public interface OrderService {
    boolean hasBought(String ssNumber,int docId);
    boolean addOrder(OrderAddReqVO vo);
    List<OrderReqVO> getAllOrders(String ssNumber);
    List<OrderSoldResVO> getAllSoldOrders(String ssNumber);
    MoneyRecordVO getMoneyRecord(String ssNumber);
}
