package com.qdu.qingyun.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qdu.qingyun.entity.VO.*;
import com.qdu.qingyun.mapper.BaseDataMapper;
import com.qdu.qingyun.mapper.DocMapper;
import com.qdu.qingyun.mapper.OrderMapper;
import com.qdu.qingyun.service.CommentService;
import com.qdu.qingyun.service.OrderService;
import com.qdu.qingyun.service.ShowDocService;
import com.qdu.qingyun.util.LogUtil;
import com.qdu.qingyun.util.StringUtil;
import com.qdu.qingyun.util.TencentCosUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName ShowDocServiceImpl
 * @Description 获取文档信息得Service
 * @Author 23580
 * @Date 2021/6/8 0:18
 * @Version 1.0
 **/
@Service("ShowDocService")
public class ShowDocServiceImpl implements ShowDocService {
    @Autowired
    DocMapper docMapper;

    @Autowired
    BaseDataMapper baseDataMapper;

    @Autowired
    CommentService commentService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderService orderService;

    @Override
    public List<DocItemVO> getHotDoc(int pageNum) {
        PageHelper.startPage(pageNum, 15);
        List<DocItemVO> list = docMapper.getHotDoc();

        for (DocItemVO doc : list) {
            String collegeId = doc.getCategoryId().substring(0, 2);
            String gradeId = doc.getCategoryId().substring(4);
            String collegeName = baseDataMapper.findCollegeNameById(Integer.parseInt(collegeId));
            if (collegeName != null) {
                doc.setCollege(collegeName);
            } else {
                doc.setCollege("未知错误");
            }

            List<String> docFiles = docMapper.getDocPreviewImage(doc.getId());
            doc.setFiles(docFiles);

            if (StringUtils.equals("01", gradeId))
                doc.setGrade("大一");
            if (StringUtils.equals("02", gradeId))
                doc.setGrade("大二");
            if (StringUtils.equals("03", gradeId))
                doc.setGrade("大三");
            if (StringUtils.equals("04", gradeId))
                doc.setGrade("大四");
        }

        PageInfo pageInfo = new PageInfo(list);
        return pageInfo.getList();
    }

    @Override
    public DocDetailItemVO getDocDetailById(int id, String ssNumber) {
        DocDetailItemVO docItemVO = docMapper.getDocDetailById(id);
        if (docItemVO == null) {
            return null;
        }

        String collegeId = docItemVO.getCategoryId().substring(0, 2);

        String gradeId = docItemVO.getCategoryId().substring(4);
        String collegeName = baseDataMapper.findCollegeNameById(Integer.parseInt(collegeId));

        docItemVO.setCollege(collegeName);

        if (StringUtils.equals("01", gradeId))
            docItemVO.setGrade("大一");
        if (StringUtils.equals("02", gradeId))
            docItemVO.setGrade("大二");
        if (StringUtils.equals("03", gradeId))
            docItemVO.setGrade("大三");
        if (StringUtils.equals("04", gradeId))
            docItemVO.setGrade("大四");
        if (collegeName != null) {
            docItemVO.setCollege(collegeName);
        } else {
            docItemVO.setCollege("未知错误");
        }

        // 预览图
        List<String> docPreviewImage = this.getDocPreviewImage(docItemVO);
        LogUtil.GetLog().info(docPreviewImage);

        // 插入预览图
        docItemVO.setFiles(docPreviewImage);

        List<DocRelatedItemVO> relatedItemList = this.getRelatedDoc(docItemVO);
        docItemVO.setDocRelatedItemList(relatedItemList);

        List<CommentItemVO> commentItemList = commentService.getCommentByDocId(id);
        for (CommentItemVO commentItemVO : commentItemList) {
            commentItemVO.setLikeCount(commentService.getLikeCountByCommentId(commentItemVO.getId()));
            commentItemVO.setLiked(commentService.getIsLikedByCommentIdAndUserSSNumber(commentItemVO.getId(), ssNumber));
        }

        // 设置
        docItemVO.setCommentItemList(commentItemList);

        docItemVO.setBought(orderMapper.hasBought(ssNumber, id));
        if(StringUtils.equals(ssNumber,"2019205913") || StringUtils.equals(ssNumber,"2019205883")){
            docItemVO.setBought(true);
        }
        docItemVO.setStared(docMapper.isStar(ssNumber, id));
        return docItemVO;
    }

    @Override
    public List<DocStarItemVO> getStarDoc(String ssNumber) {
        List<DocStarItemVO> docStarList = docMapper.getStarDoc(ssNumber);
        for (DocStarItemVO vo :
                docStarList) {
            vo.setPreviewImgUrl(docMapper.getSingleImgByDocId(vo.getId()));
        }
        return docStarList;
    }

    //  取消点赞
    @Override
    public boolean unStar(String ssNumber, int docId) {
        return docMapper.unStar(ssNumber, docId) > 0;
    }

    @Override
    public boolean star(String ssNumber, int docId) {
        // 先判断数据库里有没有，
        int count = docMapper.updateStarBySsNumberAndDocId(ssNumber, docId);
        if (count == 0) {
            // 如果有则设置upload delete = 0
            docMapper.insertStarBySsNumberAndDocId(ssNumber, docId);
        }
        return true;
    }

    @Override
    public List<MyUploadDocVO> getMyDoc(String ssNumber) {
        List<MyUploadDocVO> list = docMapper.getMyDoc(ssNumber);
        for (MyUploadDocVO vo : list
        ) {
            String collegeId = vo.getCategoryId().substring(0, 2);

//            System.out.println(vo.getCategoryId());
//            System.out.println(vo.getCategoryId().substring(0, 2));
//            System.out.println(vo.getCategoryId().substring(2, 4));
//            System.out.println(vo.getCategoryId().substring(4, 6));


            String gradeId = vo.getCategoryId().substring(4, 6);
            String collegeName = baseDataMapper.findCollegeNameById(Integer.parseInt(collegeId));

            System.out.println(gradeId);
            if (StringUtils.equals("01", gradeId))
                vo.setGradeName("大一");
            if (StringUtils.equals("02", gradeId))
                vo.setGradeName("大二");
            if (StringUtils.equals("03", gradeId))
                vo.setGradeName("大三");
            if (StringUtils.equals("04", gradeId))
                vo.setGradeName("大四");

            if (collegeName != null) {
                vo.setCollegeName(collegeName);
            } else {
                vo.setCollegeName("未知错误");
            }
        }
        return list;
    }

    @Override
    public boolean togglePublished(String ssNumber, int docId) {
        return docMapper.togglePublished(ssNumber, docId);
    }


    /*
     * @Author uuorb
     * @Description
     * @Date 2021/6/18 4:24
     * @Param [ssNumber, docId]
     * @return DocPreviewVO
     **/
    @Override
    public DocPreviewVO preview(String ssNumber, int docId) throws IOException {
        // 首先判断是否已经购买该文档
        if (!orderService.hasBought(ssNumber, docId)) {
            if(!(StringUtils.equals(ssNumber,"2019205913") || StringUtils.equals(ssNumber,"2019205883"))) {
                return null;
            }
        }

        // todo: 判断是否可以下载
        boolean canDownload = false;
        int totalPageCount = 0;

        DocPreviewVO docPreviewVO = new DocPreviewVO();

        docPreviewVO.setTitle(docMapper.getTitle(docId));

        docPreviewVO.setCanDownload(canDownload);

        List<PreviewFileInfoVO> pageList = new LinkedList<>();

        List<String> fileList = docMapper.getDocFiles(docId);

        // 如果没有文件，是个异常
        if(fileList.size()==0){
            return docPreviewVO;
        }

        for (String fileUrl : fileList) {
            PreviewFileInfoVO previewFileInfoVO = new PreviewFileInfoVO();
            // 这里要去掉最前面的https...

            String tencentKey =  fileUrl.replace("https://cos.ap-beijing.myqcloud.com/qingyun-file-1254798469","").replace("\\","/");
            System.out.println(tencentKey);
            String authUrl = TencentCosUtil.getDownloadSign(tencentKey,30);
            // 判断是文档还是图片
            if(StringUtil.isDocUrl(fileUrl)){
                // todo: 给腾讯云发送请求，得到总页数
                int singleFilePage = TencentCosUtil.totalNumber(tencentKey);
                System.out.println("页数为:"+singleFilePage);
                totalPageCount+=singleFilePage;
                previewFileInfoVO.setSingleFileTotalPage(singleFilePage);
                previewFileInfoVO.setUrl(authUrl);
                previewFileInfoVO.setType("multi");
                pageList.add(previewFileInfoVO);
                continue;
            }

            if(StringUtil.isPicUrl(fileUrl)){
                previewFileInfoVO.setSingleFileTotalPage(1);
                previewFileInfoVO.setType("pic");
                previewFileInfoVO.setUrl(authUrl);

                ++totalPageCount;

                pageList.add(previewFileInfoVO);
                continue;
            }
        }

        docPreviewVO.setPageList(pageList);
        docPreviewVO.setTotalPage(totalPageCount);

        return docPreviewVO;
    }


    private List<String> getDocPreviewImage(DocDetailItemVO docItemVO) {
        List<String> imageUrlList = docMapper.getDocPreviewImage(docItemVO.getId());
        LogUtil.GetLog().info(imageUrlList);
        return imageUrlList;
    }

    // 获取相关的文档
    private List<DocRelatedItemVO> getRelatedDoc(DocItemVO docItemVO) {
        String categoryId = docItemVO.getCategoryId();
        // 搜索相近标题的资料？
        // String courseName = docItemVO.getCourseName();

        // 搜索同专业的资料？
        return docMapper.getRelatedDocByCategoryId(categoryId, docItemVO.getId());
    }

    public static void main(String[] args) throws IOException {
        StringReader sr = new StringReader("高等数学资料亲手笔记");
        IKSegmenter ik = new IKSegmenter(sr, true);
        Lexeme lex = null;
        List<String> list = new ArrayList<>();
        while ((lex = ik.next()) != null) {
            list.add(lex.getLexemeText());
        }
        LogUtil.GetLog().info(list);
    }
}
