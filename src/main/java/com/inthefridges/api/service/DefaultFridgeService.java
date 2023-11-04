package com.inthefridges.api.service;

import com.inthefridges.api.dto.request.FridgeRequest;
import com.inthefridges.api.dto.response.FridgeResponse;
import com.inthefridges.api.entity.Fridge;
import com.inthefridges.api.entity.InFridgeFile;
import com.inthefridges.api.entity.Item;
import com.inthefridges.api.entity.Member;
import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;
import com.inthefridges.api.global.utils.FileUtil;
import com.inthefridges.api.repository.FileRepository;
import com.inthefridges.api.repository.FridgeRepository;
import com.inthefridges.api.repository.ItemRepository;
import com.inthefridges.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultFridgeService implements FridgeService {

    private final FridgeRepository repository;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<FridgeResponse> getList(Long memberId) {
        List<Fridge> fridges = repository.findFridgesByMemberId(memberId);
        return fridges
                .stream()
                .map(this::convertToFridgeResponse)
                .toList();
    }

    @Override
    public FridgeResponse get(Long memberId, Long id) {
        Member member = fetchMemberById(memberId);
        Fridge fridge = fetchFridgeById(id);
        validateMemberFridgeMatch(member, fridge);

        Fridge findFridge = fetchFridgeById(fridge.getId());

        return convertToFridgeResponse(findFridge);
    }

    @Override
    public FridgeResponse create(FridgeRequest fridgeRequest, Long memberId, Long fileId) {
        Fridge fridge = convertToFridge(fridgeRequest, null, memberId);
        repository.save(fridge);

        updatedFile(fridge, fileId);
        Fridge findfridge = fetchFridgeById(fridge.getId());

        return convertToFridgeResponse(findfridge);
    }

    @Override
    public FridgeResponse update(Long id, FridgeRequest fridgeRequest, Long memberId, Long fileId) {
        Fridge fridge = convertToFridge(fridgeRequest, id, memberId);

        Member member = fetchMemberById(fridge.getMemberId());
        Fridge fetchFridge = fetchFridgeById(id);
        validateMemberFridgeMatch(member, fetchFridge);

        updatedFile(fridge, fileId);
        fetchFridge.setName(fridge.getName());
        repository.update(fetchFridge);

        Fridge findFridge = fetchFridgeById(fetchFridge.getId());
        return convertToFridgeResponse(findFridge);
    }

    @Override
    public void delete(Long id, Long memberId) {
        Member member = fetchMemberById(memberId);
        Fridge fetchFridge = fetchFridgeById(id);
        validateMemberFridgeMatch(member, fetchFridge);
        repository.delete(id);
    }

    /**
     * fridgeId 로 fridge 찾기
     * @param id fridgeId
     * @return Fridge
     */
    private Fridge fetchFridgeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_FRIDGE));
    }

    /**
     * memberId 로 member 찾기
     * @param memberId principal's memberId
     * @return Member
     */
    private Member fetchMemberById(Long memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_MEMBER));
    }

    /**
     * principal memberId 와 접근하려는 냉장고의 등록자 memberId 가 같은지 비교
     * @param member principal's memberId
     * @param fridge 접근하려는 냉장고
     */
    private void validateMemberFridgeMatch(Member member, Fridge fridge) {
        if (!fridge.getMemberId().equals(member.getId()))
            throw new ServiceException(ExceptionCode.NOT_MATCH_MEMBER);
    }

    /**
     * 냉장고 이미지를 등록하는 메서드
     * @param fridge
     * @param fileId
     */
    private void updatedFile(Fridge fridge, Long fileId) {
        if(fileId == null)
            return;

        InFridgeFile file = fileRepository.findById(InFridgeFile.builder()
                        .id(fileId)
                        .build())
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_FILE));
        file.setFridgeId(fridge.getId());
        fileRepository.update(file);
    }

    /**
     * Fridge Entity -> FridgeResponse
     */
    private FridgeResponse convertToFridgeResponse(Fridge fridge){
        Item item = itemRepository.findByFridgeId(fridge.getId()).orElseGet(Item::new);
        InFridgeFile file = fileRepository.findByFridgeId(fridge.getId())
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_FILE));
        String imagePath = FileUtil.getFilePath(file);
        return new FridgeResponse(fridge.getId(), fridge.getName(), item.getExpirationAt(), imagePath);
    }

    /**
     * FridgeResponse -> Fridge Entity
     */
    private Fridge convertToFridge(FridgeRequest fridgeRequest, Long id, Long memberId){
        return Fridge.builder()
                .name(fridgeRequest.name())
                .memberId(memberId)
                .build();
    }

}
