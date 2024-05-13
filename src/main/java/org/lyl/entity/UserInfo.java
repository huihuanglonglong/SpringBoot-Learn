package org.lyl.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.lyl.common.annotation.JacksonSensitive;
import org.lyl.common.sensitive.SensitiveStrategy;

@Data
@Accessors(chain = true)
public class UserInfo {

    @JacksonSensitive(strategy = SensitiveStrategy.PHONE)
    private String phoneNo;

    @JacksonSensitive(strategy = SensitiveStrategy.ADDRESS)
    private String address;


}
