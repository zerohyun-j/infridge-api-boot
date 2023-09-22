package com.inthefridges.api.service;

import com.inthefridges.api.dto.response.ItemResponse;
import com.inthefridges.api.entity.*;
import com.inthefridges.api.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DefaultItemServiceTest {

    @InjectMocks
    private DefaultItemService service;
    @Mock
    private ItemRepository repository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private FridgeRepository fridgeRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private StorageTypeRepository storageTypeRepository;

    @BeforeEach
    void setup(){
        // Member
        Member member = Member.builder()
                        .id(10L)
                        .build();
        given(memberRepository.findByMemberId(anyLong())).willReturn(Optional.ofNullable(member));

        // Fridge
        Fridge fridge = Fridge.builder()
                .id(7L)
                .name("김치냉장고")
                .memberId(10L)
                .build();
        given(fridgeRepository.findById(anyLong())).willReturn(Optional.ofNullable(fridge));

        // Category
        Category category = new Category(1, "해산물");
        given(categoryRepository.findById(anyInt())).willReturn(Optional.ofNullable(category));

        // storageType
        StorageType storageType = new StorageType(1, "냉장");
        given(storageTypeRepository.findById(anyInt())).willReturn(Optional.ofNullable(storageType));

        // item
        Item item = Item
                .builder()
                .id(7L)
                .name("계란")
                .memberId(10L)
                .fridgeId(7L)
                .categoryId(1)
                .storageTypeId(1)
                .quantity(30)
                .build();
        given(repository.save(any())).willReturn(Math.toIntExact(item.getId()));
        given(repository.findById(anyLong())).willReturn(Optional.ofNullable(item));
//        given(repository.findByIdAndFridgeId(anyLong(), anyLong())).willReturn(Optional.ofNullable(item));


        List<Item> items = Arrays.asList(
                Item.builder().id(1L).name("요거트").memberId(10L).fridgeId(7L).categoryId(1).storageTypeId(1).build(),
                Item.builder().id(3L).name("생수").memberId(10L).fridgeId(7L).categoryId(1).storageTypeId(1).build(),
                Item.builder().id(4L).name("불고기").memberId(10L).fridgeId(7L).categoryId(1).storageTypeId(1).build()
        );
        given(repository.findAllByFridgeIdAndStorageId(anyLong(), anyInt())).willReturn(items);
    }

    @Test
    @DisplayName("식품 등록 성공")
    void create() throws Exception {
        //given
        Item item = Item.builder()
                .id(7L)
                .fridgeId(7L)
                .name("계란")
                .memberId(10L)
                .categoryId(1)
                .storageTypeId(1)
                .quantity(30)
                .build();
        
        //when
        ItemResponse itemResponse = service.create(item);

        //then
        Assertions.assertThat(itemResponse.storageTypeId()).isEqualTo(item.getStorageTypeId());

    }
}