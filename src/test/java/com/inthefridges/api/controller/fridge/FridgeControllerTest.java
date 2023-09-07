package com.inthefridges.api.controller.fridge;

import com.inthefridges.api.service.FridgeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

/*
* @Controller, @RestController가 설정된 클래스들을 찾아 메모리에 생성,
* @Service, @Repository 객체들은 테스트 대상이 아니므로 생성되지 않음.*/
@WebMvcTest(FridgeController.class)
class FridgeControllerTest {

    /*
    * 서블릿 컨테이너를 모킹한 객체로 컨트롤러에 대한 테스트코드 작성 가능하며,
    * 웹 API 테스트 시 사용함.
    * Spring MVC의 시작점으로 이 클래스를 통해 GET,POST 등 다양한 API 테스트 가능*/
    @Autowired
    private MockMvc mockMve; // 컨트롤러에 대한 단위테스트

    @MockBean
    private FridgeService service;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @DisplayName("[API][GET] 냉장고 리스트 조회")
    @Test
    void getList() {
        // given
//        given(service.getList(anyLong()))
//                .willReturn()
        // when
        // then
    }

    @Test
    void get() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}