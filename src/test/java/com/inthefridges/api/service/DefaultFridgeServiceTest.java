package com.inthefridges.api.service;

import com.inthefridges.api.dto.request.FridgeRequest;
import com.inthefridges.api.dto.response.FridgeResponse;
import com.inthefridges.api.entity.Fridge;
import com.inthefridges.api.entity.Member;
import com.inthefridges.api.repository.FridgeRepository;
import com.inthefridges.api.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DefaultFridgeServiceTest {

    @InjectMocks
    private DefaultFridgeService service;

    @Mock
    private FridgeRepository repository;

    @Mock
    private MemberRepository memberRepository;

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

    // memberRepository의 findByMemberId mocking 후 재테스트 해야함
    @Test
    @DisplayName("냉장고 등록 성공")
    void create() {
        // given
        FridgeRequest fridgeRequest = new FridgeRequest("토리냉장고");
        Fridge fridge = createFridgeEntity(fridgeRequest);
        Member member = Member.builder()
                        .id(1L)
                        .build();

        given(memberRepository.findByMemberId(anyLong())).willReturn(Optional.ofNullable(member));
        given(repository.create(any())).willReturn(1);
        given(repository.findById(anyLong()+1)).willReturn(Optional.ofNullable(fridge));

        // when
        FridgeResponse fridgeResponse = service.create(1L, fridgeRequest);

        // then
        assertThat(fridgeResponse.name()).isEqualTo(fridgeRequest.name());
    }

    @DisplayName("냉장고 수정 성공")
    @Test
    void update() {
        // given
        Fridge fridge = Fridge.builder()
                        .id(1L)
                        .memberId(10L)
                        .name("토리냉장고")
                        .build();
        Fridge fetchFridge = Fridge.builder()
                        .id(1L)
                        .name(fridge.getName())
                        .memberId(10L)
                        .build();
        Member member = Member.builder()
                .id(10L)
                .build();

        given(memberRepository.findByMemberId(anyLong())).willReturn(Optional.ofNullable(member));
        given(repository.findById(anyLong()+1)).willReturn(Optional.ofNullable(fetchFridge));
        given(repository.update(any())).willReturn(1);

        // when
        FridgeResponse fridgeResponse = service.update(1L, 10L, fridge);

        // then
        assertThat(fridgeResponse.name()).isEqualTo(fridge.getName());
        assertThat(fridgeResponse.id()).isEqualTo(fridge.getId());
    }

    @Test
    @DisplayName("냉장고 삭제 성공")
    void delete() {
        // given
        Member member = Member.builder()
                .id(10L)
                .build();
        Fridge fridge = Fridge.builder()
                        .id(1L)
                        .memberId(10L)
                        .build();

        given(memberRepository.findByMemberId(anyLong())).willReturn(Optional.ofNullable(member));
        given(repository.findById(anyLong()+1)).willReturn(Optional.ofNullable(fridge));

        // when
        service.delete(1L, 10L);

        // then
    }

    private Fridge createFridgeEntity(FridgeRequest fridgeRequest) {
        return Fridge.builder()
                .id(1L)
                .name(fridgeRequest.name())
                .memberId(1L)
                .build();
    }
}