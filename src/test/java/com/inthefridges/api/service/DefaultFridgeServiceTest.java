package com.inthefridges.api.service;

import com.inthefridges.api.dto.response.FridgeResponse;
import com.inthefridges.api.entity.Fridge;
import com.inthefridges.api.repository.FridgeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DefaultFridgeServiceTest {

    @InjectMocks
    private DefaultFridgeService service;

    @Mock
    private FridgeRepository repository;


    @Test
    @DisplayName("회원 아이디로 냉장고 리스트 조회 성공")
    void getList() {
        // given
        List<Fridge> list = Arrays.asList(
                Fridge.builder().id(1L).name("토리냉장고").memberId(10L).build(),
                Fridge.builder().id(3L).name("냉장고").memberId(10L).build(),
                Fridge.builder().id(4L).name("김치냉장고").memberId(10L).build()
        );

        given(repository.findByMemberId(anyLong())).willReturn(list);

        // when
        List<FridgeResponse> result = service.getList(10L);

        // then
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).extracting("id").containsExactly(1L, 3L, 4L);
        assertThat(result).extracting("name").containsExactly("토리냉장고", "냉장고", "김치냉장고");
    }

    @Test
    @DisplayName("냉장고 아이디로 냉장고 조회 성공")
    void get() {
        // given
        Fridge fridge = Fridge.builder().name("냉장고").memberId(10L).build();
        given(repository.findById(anyLong())).willReturn(Optional.ofNullable(fridge));

        // when
        FridgeResponse fridgeResponse = service.get(10L);

        // then
        assertThat(fridgeResponse.name()).isEqualTo(fridge.getName());
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