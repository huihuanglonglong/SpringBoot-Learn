package org.lyl;

import org.lyl.config.BeanLifeConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/**
 * TODO
 * 1、https://mp.weixin.qq.com/s/5WXp6X63vviOW98dCb691A?poc_token=HFODtGWjdSjRJe5L0gQVjkeWBFb5XA7qoIRwHxVV
 * 2、https://mp.weixin.qq.com/s/mMzIvFvuUq18FreTCWOrLQ
 * 3、https://mp.weixin.qq.com/s?__biz=MzUxOTc4NjEyMw==&mid=2247572329&idx=2&sn=4d9d3215d8589d17c2d5f52d8a9f6fed&chksm=f9f7c08dce80499b8dbee4f5777ddafc50f6f6b9f96dfc21abdeba90a9e68b85d1a1f350ef43&scene=132&exptype=timeline_recommend_article_extendread_samebiz#wechat_redirect
 * 4、https://mp.weixin.qq.com/s/ncsJIMtCTyx4qQO23jTaZA
 * 5、https://mp.weixin.qq.com/s/XDfXtcerFnVtpofF2vLPAA
 * 6、https://mp.weixin.qq.com/s/6lQMW6L6w9rGNaggk_rfLQ
 * 7、https://mp.weixin.qq.com/s/RTAebla4O1zzY657M5ziGA
 * 8、https://mbd.baidu.com/newspage/data/videolanding?nid=sv_15587064880285880078&sourceFrom=share
 * 9、https://mp.weixin.qq.com/s/bZjip5KldMaXuwFvt8JYeg
 * 10、https://mp.weixin.qq.com/s/izDvvl_rSSUxvP47jTUUQQ
 * 11、对线面试官：https://mp.weixin.qq.com/mp/appmsgalbum?__biz=MzU4NzA3MTc5Mg==&action=getalbum&album_id=1657204970858872832&scene=126&sessionid=-40628353#wechat_redirect
 * 
 *
 */
@EnableScheduling
@EnableAsync
@ServletComponentScan
@EnableWebMvc
@SpringBootApplication
@EnableTransactionManagement // 开启事务注解扫描
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class LearnProjectMainClass {

    private static Logger logger = LoggerFactory.getLogger(LearnProjectMainClass.class);

    public static void main(String[] args) {
        System.setProperty("config.property.pass", "AES_sourceKey#2024");
        ConfigurableApplicationContext run = SpringApplication.run(LearnProjectMainClass.class, args);
        BeanLifeConfig bean = run.getBean(BeanLifeConfig.class);
        logger.info("BeanLifeConfig = {}", bean);
    }


    /**
     * Spring学习
     * 1、BeanFactoryPostProcessor 和 BeanPostProcessor 之间的区别？
     *   https://blog.csdn.net/ruangongtaotao/article/details/135911902
     *   https://blog.csdn.net/weixin_53287520/article/details/139484810
     *
     *
     *
     *
     */


}
