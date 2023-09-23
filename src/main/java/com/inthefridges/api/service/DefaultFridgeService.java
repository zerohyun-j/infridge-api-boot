package com.inthefridges.api.service;

import com.inthefridges.api.dto.response.FridgeResponse;
import com.inthefridges.api.entity.Fridge;
import com.inthefridges.api.entity.InFridgeFile;
import com.inthefridges.api.entity.Member;
import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;
import com.inthefridges.api.repository.FileRepository;
import com.inthefridges.api.repository.FridgeRepository;
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
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<FridgeResponse> getList(Long memberId) {
        List<Fridge> fridges = repository.findByMemberId(memberId);
        return fridges
                .stream()
                .map(fridge -> convertToFridgeResponse(fridge))
                .toList();
    }

    @Override
    public FridgeResponse get(Long memberId, Long id) {
        Member member = fetchMemberById(memberId);
        Fridge fridge = fetchFridgeById(id);
        validateMemberFridgeMatch(member, fridge);
        return convertToFridgeResponse(fridge);
    }

    @Override
    public FridgeResponse create(Fridge fridge, Long fileId) {
        repository.save(fridge);

        // 냉장고 이미지 등록
        if(fileId != null) {
            InFridgeFile file = fileRepository.findById(InFridgeFile.builder()
                                                        .id(fileId)
                                                        .build())
                    .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_FILE));
            file.setFridgeId(fridge.getId());
            fileRepository.update(file);
        }

        Fridge fetchFridge = fetchFridgeById(fridge.getId());
        return convertToFridgeResponse(fetchFridge);
    }

    @Override
    public FridgeResponse update(Long id, Long memberId, Fridge fridge) {
        Member member = fetchMemberById(memberId);
        Fridge fetchFridge = fetchFridgeById(id);
        validateMemberFridgeMatch(member, fetchFridge);

        fetchFridge.setName(fridge.getName());
        repository.update(fetchFridge);
        Fridge updatedFridge = fetchFridgeById(id);
        return convertToFridgeResponse(updatedFridge);
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
    public Fridge fetchFridgeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_FRIDGE));
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
     * principal memberId 와 접근하려는 냉장고의 등록자 memberId 가 같은지 비교
     * @param member principal's memberId
     * @param fridge 접근하려는 냉장고
     */
    public void validateMemberFridgeMatch(Member member, Fridge fridge) {
        if (!fridge.getMemberId().equals(member.getId()))
            throw new ServiceException(ExceptionCode.NOT_MATCH_MEMBER);
    }

    /**
     * Fridge Entity -> FridgeResponse
     */
    private FridgeResponse convertToFridgeResponse(Fridge fridge){
        return new FridgeResponse(fridge.getId(), fridge.getName(), null);
    }



}
