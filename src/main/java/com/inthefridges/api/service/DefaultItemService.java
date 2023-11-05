package com.inthefridges.api.service;

import com.inthefridges.api.dto.request.CreateItemRequest;
import com.inthefridges.api.dto.request.UpdateItemRequest;
import com.inthefridges.api.dto.response.ItemResponse;
import com.inthefridges.api.entity.Category;
import com.inthefridges.api.entity.Item;
import com.inthefridges.api.entity.Member;
import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;
import com.inthefridges.api.mapper.ItemMapper;
import com.inthefridges.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultItemService implements ItemService{
    private final ItemRepository repository;
    private final MemberRepository memberRepository;
    private final FridgeRepository fridgeRepository;
    private final CategoryRepository categoryRepository;
    private final StorageTypeRepository storageTypeRepository;

    @Override
    public ItemResponse create(CreateItemRequest createItemRequest, Long memberId, Long fridgeId) {

        Item item = ItemMapper.toItem(createItemRequest, memberId, fridgeId);
        memberRepository.findByMemberId(item.getMemberId())
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_MEMBER));

        fridgeRepository.findById(item.getFridgeId())
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_FRIDGE));

        Category category = categoryRepository.findById(item.getCategoryId())
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_CATEGORY));

        storageTypeRepository.findById(item.getStorageId())
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_STORAGE_TYPE));

        repository.save(item);

        Item findItem = repository.findById(item.getId())
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_ITEM));

        return ItemMapper.toItemResponse(findItem, category);
    }

    @Override
    public ItemResponse get(Long memberId, Long fridgeId, Long id) {
        Member member = fetchMemberById(memberId);
        Item item = fetchItemById(id);
        validateMemberFridgeMatch(member, item);

        Item findItem = repository.findByIdAndFridgeId(id, fridgeId)
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_ITEM));
        Category category = categoryRepository.findById(item.getCategoryId())
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_CATEGORY));

        return ItemMapper.toItemResponse(findItem, category);
    }

    @Override
    public List<ItemResponse> getList(Long memberId, Long fridgeId, int storageId) {

        List<Item> items = repository.findItemsByFridgeIdAndStorageId(fridgeId, storageId);

        return items.stream()
                .map(findItem -> {
                    Category category = categoryRepository.findById(findItem.getCategoryId())
                            .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_CATEGORY));
                    return ItemMapper.toItemResponse(findItem, category);
                })
                .toList();
    }

    @Override
    public ItemResponse update(UpdateItemRequest updateItemRequest, Long id, Long memberId, Long fridgeId) {

        Item item = ItemMapper.toItem(updateItemRequest, memberId, fridgeId);

        Member member = fetchMemberById(item.getMemberId());
        Item fetchItem = fetchItemById(item.getId());
        validateMemberFridgeMatch(member, fetchItem);

        fetchItem.setName(item.getName());
        fetchItem.setCategoryId(item.getCategoryId());
        fetchItem.setQuantity(item.getQuantity());
        fetchItem.setExpirationAt(item.getExpirationAt());

        repository.update(fetchItem);
        Item findItem = repository.findById(fetchItem.getId())
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_ITEM));
        Category category = categoryRepository.findById(findItem.getCategoryId())
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_CATEGORY));

        return ItemMapper.toItemResponse(fetchItem, category);
    }

    @Override
    public void delete(Long memberId, Long fridgeId, Long id) {
        Member member = fetchMemberById(memberId);
        Item fetchItem = fetchItemById(id);
        validateMemberFridgeMatch(member, fetchItem);

        repository.delete(id, fridgeId);
    }

    /**
     * itemId 로 item 찾기
     * @param id itemId
     * @return Item
     */
    public Item fetchItemById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_ITEM));
    }

    /**
     * memberId 로 member 찾기
     * @param memberId principal's memberId
     * @return Member
     */
    public Member fetchMemberById(Long memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_MEMBER));
    }

    /**
     * principal memberId 와 식품 등록록 memberId 가 같은지 비교
     * @param member principal's memberId
     * @param item 식품
     */
    public void validateMemberFridgeMatch(Member member, Item item) {
        if (!item.getMemberId().equals(member.getId()))
            throw new ServiceException(ExceptionCode.NOT_MATCH_MEMBER);
    }
}
