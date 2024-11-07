package com.ssafy.runit.domain.record.service;

import com.ssafy.runit.domain.pace.dto.response.PaceResponse;
import com.ssafy.runit.domain.pace.entity.Pace;
import com.ssafy.runit.domain.pace.repository.PaceRepository;
import com.ssafy.runit.domain.record.dto.request.RecordSaveRequest;
import com.ssafy.runit.domain.record.dto.response.RecordGetListResponse;
import com.ssafy.runit.domain.record.dto.response.RecordGetResponse;
import com.ssafy.runit.domain.record.entity.Record;
import com.ssafy.runit.domain.record.repository.RecordRepository;
import com.ssafy.runit.exception.code.AuthErrorCode;
import com.ssafy.runit.exception.code.TrackErrorCode;
import com.ssafy.runit.util.S3UploadUtil;
import com.ssafy.runit.domain.track.entity.Track;
import com.ssafy.runit.domain.track.repository.TrackRepository;
import com.ssafy.runit.domain.user.entity.User;
import com.ssafy.runit.domain.user.repository.UserRepository;
import com.ssafy.runit.exception.CustomException;
import com.ssafy.runit.exception.code.RecordErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final TrackRepository trackRepository;
    private final PaceRepository paceRepository;
    private final UserRepository userRepository;
    private final S3UploadUtil s3UploadUtil;

    @Override
    @Transactional
    public void saveRunningRecord(UserDetails userDetails, RecordSaveRequest request, MultipartFile file) {
        if (file == null){
            throw new CustomException(TrackErrorCode.NOT_FOUND_TRACK_IMG);
        }

        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(AuthErrorCode.UNREGISTERED_USER_ERROR)
        );

        Record record = request.mapper(findUser);

        recordRepository.save(record);

        try{
            String url = s3UploadUtil.saveFile(file);
            Record afRecord = request.toEntity(record, url);

            trackRepository.save(afRecord.getTrack());

            List<Pace> paceList = afRecord.getPaceList();

            paceRepository.saveAll(paceList);
        }catch (Exception e){
            log.debug("여기 걸리나");
            throw new CustomException(TrackErrorCode.NOT_FOUND_TRACK_IMG);
        }
    }

    @Override
    public RecordGetResponse getRecord(UserDetails userDetails, Long recordId) {
        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();

        Record record = recordRepository.findByUserIdAndRecordId(findUser.getId(), recordId).orElseThrow(
                () -> new CustomException(RecordErrorCode.NOT_FOUND_RECORD_DATA)
        );

        List<PaceResponse> paceResponseList = paceRepository.findByRecordId(recordId)
                .stream()
                .map(item -> PaceResponse.isEntity(item.getBpm(), item.getPace()))
                .collect(Collectors.toList());


        return RecordGetResponse.fromEntity(record, paceResponseList);
    }

    @Override
    public List<RecordGetListResponse> getRecordList(UserDetails userDetails) {
        User findUser = userRepository.findByUserNumber(userDetails.getUsername()).orElseThrow();

        List<RecordGetListResponse> recordGetListResponses = recordRepository.findByUserId(findUser.getId())
                .stream()
                .map(item -> {
                    Track track = trackRepository.findTrackImageUrlByRecordId(item.getId()).orElseThrow();
                    return RecordGetListResponse.fromEntity(item, findUser.getUserName(), track.getTrackImageUrl());

                })
                .collect(Collectors.toList());


        return recordGetListResponses;
    }
}
