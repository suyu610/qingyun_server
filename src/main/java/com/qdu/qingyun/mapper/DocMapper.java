package com.qdu.qingyun.mapper;

import com.qdu.qingyun.entity.VO.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;

@Mapper
public interface DocMapper {
    String getTitle(int docId);
    List<DocSearchResVO> searchByKeyword(String keyword);
    LinkedList<DocItemVO> getHotDoc();
    LinkedList<DocItemVO> getDocListByCourseName(String courseName);
    int insertDoc(DocUploadVO vo);

    int insertDocFileUrl(@Param("doc_id") int doc_id, @Param("urls") String[] urls);

    int insertDocPreviewImgUrl(@Param("doc_id") int doc_id, @Param("urls") List<String> urls);

    int insertSingleDocPreviewImgUrl(@Param("doc_id") int doc_id, @Param("url") String url);

    DocDetailItemVO getDocDetailById(int id);

    List<String> getDocFiles(int id);

    List<String> getDocPreviewImage(int id);

    List<DocRelatedItemVO> getRelatedDocByCategoryId(@Param("categoryId") String categoryId, @Param("docId") int id);

    // 获取某个学生发布的资料
    List<DocRelatedItemVO> getDocBySsNumber(@Param("ssNumber")String ssNumber,@Param("limit") int limit);

    // 获取我自己发布的资料
    List<MyUploadDocVO> getMyDoc(@Param("ssNumber")String ssNumber);

    String  getSingleImgByDocId(int docId);

    DocGeneratePreviewImageItemVO getDocGeneratePreviewImageItemById(int docId);

    List<String> getAllCourse();

    // 获取收藏的文档
    List<DocStarItemVO> getStarDoc(String ssNumber);

    // 取消收藏
    int unStar(@Param("ssNumber") String ssNumber,@Param("docId") int docId);

    int updateStarBySsNumberAndDocId(@Param("ssNumber") String ssNumber,@Param("docId") int docId);

    void insertStarBySsNumberAndDocId(@Param("ssNumber") String ssNumber,@Param("docId") int docId);

    Boolean isStar(@Param("ssNumber") String ssNumber,@Param("docId") int docId);

    Boolean togglePublished(@Param("ssNumber") String ssNumber,@Param("docId") int docId);

    // 根据学号，找到此人所有的docId
    List<Integer> getDocIdBySsNumber(String ssNumber);
}
