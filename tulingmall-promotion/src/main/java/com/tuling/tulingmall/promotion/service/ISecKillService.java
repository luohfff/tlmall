package com.tuling.tulingmall.promotion.service;


import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;

/*秒杀相关服务*/
public interface ISecKillService {

    /*在本地生成静态化的页面*/
    List<String> makeStaticHtml(long secKillId) throws TemplateException, IOException;

    /*将静态化页面上传至服务器*/
    void deployHtml();

}
