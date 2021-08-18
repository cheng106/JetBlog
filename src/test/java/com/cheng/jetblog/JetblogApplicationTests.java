package com.cheng.jetblog;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JetblogApplicationTests {

    @Test
    void contextLoads() {
        // a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3
        // a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3
        String s = DigestUtils.sha256Hex("123");

        // 19711b9a35735e677b499a567bb17e5c
        // 19711b9a35735e677b499a567bb17e5c
        s = DigestUtils.md5Hex(s);
        System.out.println("s = " + s);
    }

}
