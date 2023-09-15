package org.lyl.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class BFilter implements IFilter{
    @Override
    public void filter() {
        System.out.println("BFilter");
    }
}
