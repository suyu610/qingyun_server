package com.qdu.qingyun.service.impl;

import com.qdu.qingyun.entity.Dialog.DialogPO;
import com.qdu.qingyun.mapper.DialogMapper;
import com.qdu.qingyun.service.DialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName DialogServiceImpl

 * @Date 2021/7/4 23:54
 * @Version 1.0
 **/
@Service("DialogService")
public class DialogServiceImpl implements DialogService {
    @Autowired
    DialogMapper dialogMapper;
    @Override
    public DialogPO getNewDialog() {
        return dialogMapper.getNewDialog();
    }
}
