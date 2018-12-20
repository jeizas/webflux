package com.jeizas.controller.webflux;

import com.jeizas.ApplicationLauncher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @Author jeizas
 * @Date 2018 8/19/18 5:24 PM
 */
@RunWith(SpringRunner.class)
//@WebAppConfiguration
@SpringBootTest(classes={ApplicationLauncher.class})
public class TestControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void test() throws Exception {
        long startTiem = System.currentTimeMillis();
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/test")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        System.out.println("耗时：" + (System.currentTimeMillis() - startTiem));
    }

}
