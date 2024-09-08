package org.lyl.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.lyl.common.annotation.JacksonSensitive;
import org.lyl.common.sensitive.SensitiveStrategy;

import java.util.Date;

@Data
@Accessors(chain = true)
public class UserInfo {


    private String userId;

    private String userNamePrefix;

    private String userName;

    private Date birthDate;

    private String birthDateTime;


    @JacksonSensitive(strategy = SensitiveStrategy.PHONE)
    private String phoneNo;

    @JacksonSensitive(strategy = SensitiveStrategy.ADDRESS)
    private String address;


    private Integer amount;




}
