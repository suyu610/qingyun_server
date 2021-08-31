package com.qdu.qingyun.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.qdu.qingyun.entity.Doc.DocItemVO;
import com.qdu.qingyun.entity.Doc.DocRelatedItemVO;
import com.qdu.qingyun.entity.System.WeAppInitDataVO;
import com.qdu.qingyun.entity.User.*;
import com.qdu.qingyun.entity.Ad.AdPO;
import com.qdu.qingyun.entity.Msg.MsgPO;
import com.qdu.qingyun.mapper.*;
import com.qdu.qingyun.service.CategoryService;
import com.qdu.qingyun.service.OrderService;
import com.qdu.qingyun.service.ShowDocService;
import com.qdu.qingyun.service.UserService;
import com.qdu.qingyun.util.IPUtil;
import com.qdu.qingyun.util.JwtTokenUtil;
import com.qdu.qingyun.util.LogUtil;
import com.qdu.qingyun.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    DocMapper docMapper;

    @Autowired
    TagMapper tagMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    OrderService orderService;

    @Autowired
    ShowDocService docService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    @Cacheable(value = "school_stu", key = "methodName+#name+#ssNumber")
    public boolean nameAndNumberMatched(String name, String ssNumber) {
        int count = userMapper.nameAndNumberMatched(name, ssNumber);
        return count > 0;
    }

    @Override
    public boolean hasRegister(String ssNumber) {
        int count = userMapper.hasRegister(ssNumber);
        return count > 0;
    }

    @Override
    public boolean pwdCorrect(LoginUserReqVO vo) {
        String db_pwd = userMapper.getPassword(vo.getSsNumber());
        // 匹配成功
        return StringUtils.equals(db_pwd, vo.getPassword());
    }

    @Override
    public boolean uploadLastLoginTime(String token) {
        try {
            String ssNumber = (String) JwtTokenUtil.getClaimsFromToken(token).get("ssNumber");
            return userMapper.uploadLastLoginTime(ssNumber);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean initUser(LoginUserReqVO vo) {
        // 说明创建成功
        return userMapper.initUser(vo) > 0;
    }

    // 签发token
    // 把openid，name 放到claim里
    // 用ssNumber做subject
    @Override
    public String generateToken(String ssNumber, String openid) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("openid", openid);
        claims.put("ssNumber", ssNumber);
        return JwtTokenUtil.getAccessToken(ssNumber, claims);
    }

    @Override
    public int underLockMinute(HttpServletRequest request) {
        int minute = (int) redisUtil.getExpire("IPTryCount" + IPUtil.getIpAddr(request)) / 60;
        int tryCount = redisUtil.get("IPTryCount" + IPUtil.getIpAddr(request)) == null ? 3 : (int) redisUtil.get("IPTryCount" + IPUtil.getIpAddr(request));
        if (tryCount > 0) {
            return 0;
        }

        return minute;
    }

    @Override
    public int getRemainTryCount(HttpServletRequest request) {
        int tryCount = redisUtil.get("IPTryCount" + IPUtil.getIpAddr(request)) == null ? 3 : (int) redisUtil.get("IPTryCount" + IPUtil.getIpAddr(request));
        // 设置重试次数，30分钟
        redisUtil.set("IPTryCount" + IPUtil.getIpAddr(request), --tryCount, 60 * 30);
        return tryCount;
    }

    // 到了这一步，都存在token了
    // 这里由于是登陆时候需要获得的数据，所以返回全部的数据

    @Override
    public WeAppInitDataVO getData(LoginUserReqVO vo) throws Exception {
        String ssNumber;
        String openid;
        if (vo.getToken() != null) {
            ssNumber = (String) JwtTokenUtil.getClaimsFromToken(vo.getToken()).get("ssNumber");
            openid = (String) JwtTokenUtil.getClaimsFromToken(vo.getToken()).get("openid");
        } else {
            throw new Exception("非法登录");
        }
        if (vo.getNeedCategory() == 1) {
            return this.getAllData(ssNumber, openid, 1);
        } else {
            return this.getAllData(ssNumber, openid, 0);

        }
    }

    private WeAppInitDataVO getAllData(String ssNumber, String openid, int needCategory) {

        UserInitDataBO userInitDataBO = userMapper.getInitUserData(ssNumber);

        userInitDataBO.setIsAdmin(adminMapper.isAdmin(ssNumber));

        userInitDataBO.setRemainMoney(orderService.getMoneyRecord(ssNumber).getRemainMoney());

        userInitDataBO.setSsNumber(ssNumber);
        userInitDataBO.setBoughtDocList(orderService.getAllOrders(ssNumber));

        ///// 公开的
        List<DocItemVO> hotDoc = docService.getHotDoc(0);

        // 顶部轮播图
        LinkedList<AdPO> adList = null;

        // 消息列表
        LinkedList<MsgPO> msgList = null;

        WeAppInitDataVO weAppInitDataVO = new WeAppInitDataVO();
        weAppInitDataVO.setUserInitDataBO(userInitDataBO);
        weAppInitDataVO.setToken(this.generateToken(ssNumber, openid));
        weAppInitDataVO.setHotDoc(hotDoc);
        if (needCategory == 1) {
            weAppInitDataVO.setCategory(categoryService.getAllCategory());
        }
        return weAppInitDataVO;
    }


    @Override
    public WeAppInitDataVO getData(GetDataReqVO vo) {
        WeAppInitDataVO weAppInitDataVO = new WeAppInitDataVO();
        // 获取全部数据
        if (vo.getFields().contains("all")) {
            return this.getAllData(vo.getSsNumber(), vo.getOpenid(), 1);
        }
        String[] fields = vo.getFields().split(",");
        for (String field : fields) {
            switch (field) {
                case "msg":
                    weAppInitDataVO.setMsgList(null);
                    break;
                case "hotDoc":
                    weAppInitDataVO.setHotDoc(docService.getHotDoc(0));
            }
        }
        return weAppInitDataVO;
    }

    @Override
    public UserProfileResVO getProfileByNumber(String ssNumber) {
        UserProfileResVO userProfileResVO = userMapper.getProfileByNumber(ssNumber);
        List<DocRelatedItemVO> docRelatedItemList = docMapper.getDocBySsNumber(ssNumber, 99999);
        for (DocRelatedItemVO item :
                docRelatedItemList) {
            item.setPreviewImageUrl(docMapper.getSingleImgByDocId(item.getId()));
        }
        userProfileResVO.setDocList(docRelatedItemList);
        LogUtil.GetLog().info(docRelatedItemList);
        userProfileResVO.setTagList(tagMapper.getTagsBySsNumber(ssNumber));
        return userProfileResVO;
    }

    @Override
    public boolean insertTag(UserTagVO userTagVO) {
        return tagMapper.insertTag(userTagVO) >= 1;
    }

    @Override
    public boolean modifyProfile(UserProfileReqVO vo) {
        return userMapper.modifyProfile(vo);
    }

    @Override
    public UserInitDataBO getSelfProfileByNumber(String ssNumber) {
        UserInitDataBO bo = userMapper.getInitUserData(ssNumber);
        return bo;
    }


}
