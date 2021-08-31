package com.qdu.qingyun.service;

import com.qdu.qingyun.entity.Doc.DocSearchResVO;

import java.util.List;

public interface SearchService {
    List<DocSearchResVO> search(String keyword);
}
