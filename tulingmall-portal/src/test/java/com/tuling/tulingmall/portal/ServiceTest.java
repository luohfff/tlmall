package com.tuling.tulingmall.portal;

import com.tuling.tulingmall.mapper.SmsHomeBrandMapper;
import com.tuling.tulingmall.mapper.UmsMemberMapper;
import com.tuling.tulingmall.model.SmsHomeBrand;
import com.tuling.tulingmall.model.SmsHomeBrandExample;
import com.tuling.tulingmall.model.UmsMember;
import com.tuling.tulingmall.model.UmsMemberExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author roy
 * @desc
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ServiceTest {

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Resource
    private SmsHomeBrandMapper smsHomeBrandMapper;

    @Test
    public void memberTest(){
        List<UmsMember> umsMembers = umsMemberMapper.selectByExample(new UmsMemberExample());
        umsMembers.forEach(System.out::println);
    }

    @Test
    public void smshomebrandTest(){
        smsHomeBrandMapper.selectByExample(new SmsHomeBrandExample()).forEach(System.out::println);
    }
}
