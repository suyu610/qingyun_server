package com.qdu.qingyun.entity.Dialog;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName DialogPO

 * @Date 2021/7/4 23:54
 * @Version 1.0
 **/
@Data
public class DialogPO implements Serializable {
    String dialogId;
    String title;
    String content;
    Date createtime;
    String author;
}
