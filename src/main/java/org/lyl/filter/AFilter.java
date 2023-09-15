package org.lyl.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(0)
@Component
public class AFilter implements IFilter{

    @Override
    public void filter() {
        System.out.println("AFilter");
    }
}
