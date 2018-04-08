package com.eastrobot.converter.service.impl;

import com.eastrobot.converter.service.YouTuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YouTuServiceImplTest {

    @Autowired
    private YouTuService youTuService;

    @Test
    public void ocr() {

        try {
            youTuService.ocr("C:\\Users\\User\\Desktop\\11.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}