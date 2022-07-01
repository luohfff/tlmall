package com.tuling;

import com.tuling.tulingmall.mapper.UmsMemberMapper;
import com.tuling.tulingmall.model.UmsMember;
import com.tuling.tulingmall.model.UmsMemberExample;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class MapperTest {
    @Resource
    private UmsMemberMapper memberMapper;

    @Test
    public void MemberTest(){
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo("roy");
        example.or(example.createCriteria().andPhoneEqualTo("123"));
        List<UmsMember> umsMembers = memberMapper.selectByExample(example);
        umsMembers.forEach(System.out::println);
    }
}
