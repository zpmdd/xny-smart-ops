package com.xny.system;

import com.xny.system.yingshi.utils.YsUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class YsTest {
    @Test
    public void test(){
        String token = YsUtils.getToken();
        System.out.println(token);

        String playUrl = YsUtils.getPlayUrl();
        System.out.println(playUrl);
    }
}
