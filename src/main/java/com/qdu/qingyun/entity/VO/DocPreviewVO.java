package com.qdu.qingyun.entity.VO;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName DocPreviewVO
 * @Description 文檔在綫預覽時
 * @Author 23580
 * @Date 2021/6/18 4:01
 * @Version 1.0
 **/
@Data
public class DocPreviewVO implements Serializable {
    String title;
    String introduce;

    int totalPage;
    List<PreviewFileInfoVO> pageList;

    boolean canDownload;
    String downloadUrl;
}
