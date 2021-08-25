package com.qdu.qingyun.entity.VO;

import com.qdu.qingyun.entity.PO.FilePO;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName DocUploadVO
 * @Description 上传文件的
 * @Author 23580
 * @Date 2021/6/9 23:19
 * @Version 1.0
 **/

@Data
public class DocUploadVO implements Serializable {
    int id;
    FilePO[] upload_fileUrl_List;
    String upload_doc_title;
    String upload_course_title;
    String upload_doc_introduce;
    // 课程分类的id
    int upload_categoryId;
    // 要转为数组
    String[] upload_tagId_list;
    int upload_doc_previewCount;
    int upload_doc_type;
    float upload_doc_price;
    String ssNumber;
    String openid;
    // 以逗号分割
    String upload_file_name;
    String upload_doc_file_dir;
}
