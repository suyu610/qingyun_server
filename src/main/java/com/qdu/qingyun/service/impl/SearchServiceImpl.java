package com.qdu.qingyun.service.impl;

import com.qdu.qingyun.entity.Doc.DocSearchResVO;
import com.qdu.qingyun.mapper.DocMapper;
import com.qdu.qingyun.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName SearchServiceImpl

 * @Date 2021/6/20 9:46
 * @Version 1.0
 **/
@Service("SearchService")
public class SearchServiceImpl implements SearchService {
    @Autowired
    DocMapper docMapper;

    @Override
    public List<DocSearchResVO> search(String keyword) {
        return docMapper.searchByKeyword(keyword);
    }
}
