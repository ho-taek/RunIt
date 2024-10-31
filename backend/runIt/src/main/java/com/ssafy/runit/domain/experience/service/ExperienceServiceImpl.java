package com.ssafy.runit.domain.experience.service;


import com.ssafy.runit.domain.experience.dto.request.ExperienceSaveRequest;
import com.ssafy.runit.domain.experience.dto.response.ExperienceGetListResponse;
import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.domain.experience.repository.ExperienceRepository;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.AuthErrorCode;
import com.ssafy.runit.util.DateUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;


    @Override
    @Transactional
    public void experienceSave(User user, ExperienceSaveRequest request) {
//      기존의 user id 받아서 처리하는 부분
        Experience exp = request.Mapper(user, LocalDateTime.now());
        experienceRepository.save(exp);
    }

    public Long experienceChangedSum(Long id) {
        LocalDate lastMonday = DateUtils.getLastMonday();
        return experienceRepository.experienceChangedSum(id, lastMonday.atStartOfDay());
    }

    @Override
    public List<ExperienceGetListResponse> experienceList(Long userId) {
        List<ExperienceGetListResponse> expList = experienceRepository.findByUser_Id(userId);
        return expList;
    }
}
