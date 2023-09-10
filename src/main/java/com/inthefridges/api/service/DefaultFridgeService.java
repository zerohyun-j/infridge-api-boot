package com.inthefridges.api.service;

import com.inthefridges.api.dto.request.FridgeRequest;
import com.inthefridges.api.dto.response.FridgeResponse;
import com.inthefridges.api.entity.Fridge;
import com.inthefridges.api.entity.Member;
import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;
import com.inthefridges.api.repository.FridgeRepository;
import com.inthefridges.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultFridgeService implements FridgeService {

    private final FridgeRepository repository;
    private final MemberRepository memberRepository;

    @Override
    public List<FridgeResponse> getList(Long memberId) {
        List<Fridge> fridges = repository.findByMemberId(memberId);
        return fridges
                .stream()
                .map(fridge -> new FridgeResponse(fridge.getId(), fridge.getName(), null, null))
                .toList();
    }

    @Override
    public FridgeResponse create(Long memberId, FridgeRequest fridgeRequest) {
        Member member = fetchMemberById(memberId);
        Fridge fridge = Fridge.builder()
                .name(fridgeRequest.name())
                .memberId(member.getId())
                .build();
        repository.create(fridge);

        return get(fridge.getId());
    }

    @Override
    public FridgeResponse get(Long id) {
        Fridge fridge = repository.findById(id)
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_FRIDGE));
        return new FridgeResponse(fridge.getId(), fridge.getName(), null, null);
    }

    @Override
    public FridgeResponse update(Long id, Long memberId, Fridge fridge) {
        Member member = fetchMemberById(memberId);
        Fridge fetchFridge = fetchFridgeById(id);
        validateMemberFridgeMatch(member, fetchFridge);

        fetchFridge.setName(fridge.getName());
        repository.update(fetchFridge);

        return get(fetchFridge.getId());
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
     * principal memberId 와 수정하려는 냉장고의 작성자 memberId 가 같은지 비교
     * @param member principal's memberId
     * @param fridge 수정하려는 냉장고
     */
    private void validateMemberFridgeMatch(Member member, Fridge fridge) {
        if (!fridge.getMemberId().equals(member.getId()))
            throw new ServiceException(ExceptionCode.NOT_MATCH_MEMBER);
    }
}
