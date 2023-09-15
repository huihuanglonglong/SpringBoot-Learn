package org.lyl.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServiceImpl {

    @Autowired
    List<IFilter> filterList;

    @Transactional
    void test(int i){
        filterList.forEach(IFilter::filter);
    }
}
