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

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final EntityManager em;

    @Transactional(readOnly = true)
    public List<SubscribeDto> subscribeList(Long principalId, Long userId) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT u.id, u.user_name, u.profile_image_url, ");
        sb.append("if((SELECT 1 FROM subscribe WHERE from_user_id = ? AND to_user_id = u.id), 1, 0) subscribeState, ");
        sb.append("if((?=u.id), 1, 0) equal_user_state ");
        sb.append("FROM user u INNER JOIN subscribe s ");
        sb.append("ON u.id = s.to_user_id ");
        sb.append("WHERE s.from_user_id = ?");

        Query query = em.createNativeQuery(sb.toString())
                .setParameter(1, principalId)
                .setParameter(2, principalId)
                .setParameter(3, userId);

        List<Object[]> results = query.getResultList();
        List<SubscribeDto> subscribeDtos = mapResultsToDto(results);
        return subscribeDtos;
    }

    private List<SubscribeDto> mapResultsToDto(List<Object[]> results) {
        for (Object[] result : results) {
            System.out.println(result);
        }
        return null;
    }

    @Transactional
    public void subscribe(Long fromUserId, Long toUserId) {
        try {
            subscribeRepository.subscribe(fromUserId, toUserId);
        } catch (Exception e) {
            throw new CustomException("이미 구독을 하였습니다.");
        }

    }

    @Transactional
    public void unSubscribe(Long fromUserId, Long toUserId) {
        subscribeRepository.unSubscribe(fromUserId, toUserId);
    }
}
