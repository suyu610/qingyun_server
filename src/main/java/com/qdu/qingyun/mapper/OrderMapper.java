package com.qdu.qingyun.mapper;

import com.qdu.qingyun.entity.Order.OrderAddReqVO;
import com.qdu.qingyun.entity.Order.OrderReqVO;
import com.qdu.qingyun.entity.Order.OrderSoldResVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    boolean hasBought(String ssNumber,int docId);
    int addOrder(OrderAddReqVO vo);
    List<OrderReqVO> getAllOrders(String ssNumber);
    List<OrderSoldResVO> getAllSoldOrders(@Param("docIdList")List<Integer> docIdList);
}
