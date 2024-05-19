package com.butlergram.service;

import com.butlergram.dto.SubscribeDto;
import com.butlergram.exception.CustomException;
import com.butlergram.repository.SubscribeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final EntityManager em;

    //프로필페이지 구독정보 리스트
    @Transactional(readOnly = true)
    public List<SubscribeDto> subscribeList(Long principalId, Long userId) {
        
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT u.user_id, u.username, u.profile_image_url, ");
        sb.append("IF((SELECT 1 FROM subscribe WHERE from_user_id = ? AND to_user_id = u.user_id), 1, 0) subscribeState, ");
        sb.append("IF((? = u.user_id), 1, 0) equalUserState ");
        sb.append("FROM user u INNER JOIN subscribe s ");
        sb.append("ON u.user_id = s.to_user_id ");
        sb.append("WHERE s.from_user_id = ?");

        Query query = em.createNativeQuery(sb.toString())
                .setParameter(1, principalId)
                .setParameter(2, principalId)
                .setParameter(3, userId);

        List<Object[]> results = query.getResultList();
        
        return mapResultsToDto(results);
    }

    private List<SubscribeDto> mapResultsToDto(List<Object[]> results) {
        
        List<SubscribeDto> subscribeDtos = new ArrayList<>();
        
        for (Object[] result : results) {
            Long id = ((Number) result[0]).longValue();
            String userName = (String) result[1];
            String profileImageUrl = (String) result[2];
            boolean subscribeState = ((Number) result[3]).intValue() == 1;
            boolean equalUserState = ((Number) result[4]).intValue() == 1;

            SubscribeDto subscribeDto = new SubscribeDto(id, userName, profileImageUrl, subscribeState, equalUserState);
            subscribeDtos.add(subscribeDto);
        }
        
        return subscribeDtos;
    }

    //구독하기
    @Transactional
    public void subscribe(Long fromUserId, Long toUserId) {

        try {
            subscribeRepository.subscribe(fromUserId, toUserId);
        } catch (Exception e) {
            throw new CustomException("이미 구독을 하였습니다.");
        }
    }

    //구독취소
    @Transactional
    public void unSubscribe(Long fromUserId, Long toUserId) {
        subscribeRepository.unSubscribe(fromUserId, toUserId);
    }
}
