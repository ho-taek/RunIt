package com.ssafy.runit.domain.record.dto.response;

import com.ssafy.runit.domain.record.entity.Record;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RecordGetResponse(
    long id,
    double distance,
    Integer bpm,
    LocalDateTime startTime,
    LocalDateTime endTime,
    String name,
    String imageUrl
) {
    public static RecordGetResponse fromEntity(Record record, String name, String imageUrl){
        return RecordGetResponse.builder()
                .id(record.getId())
                .name(name)
                .imageUrl(imageUrl)
                .distance(record.getDistance())
                .bpm(record.getBpm())
                .startTime(record.getStartTime())
                .endTime(record.getEndTime())
                .build();
    }
}
