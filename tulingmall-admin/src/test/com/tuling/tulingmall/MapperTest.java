package com.tuling.tulingmall;

import com.tuling.tulingmall.mapper.UmsAdminMapper;
import com.tuling.tulingmall.model.UmsAdmin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author roy
 * @desc
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MapperTest {

    @Autowired
    private UmsAdminMapper adminMapper;

    @Test
    public void getUser(){
        List<UmsAdmin> umsAdmins = adminMapper.selectList(null);
        umsAdmins.forEach(System.out::println);
    }
}
