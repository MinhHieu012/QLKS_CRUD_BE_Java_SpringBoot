package vn.eledevo.vksbe.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.mapstruct.*;

import vn.eledevo.vksbe.dto.response.PageResponse;

public abstract class BaseMapper<Rq, Rp, T> {
    public abstract T toEntity(Rq rq);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract T toEntityUpdate(Rq rq, @MappingTarget T t);

    public abstract Rp toResponse(T t);

    public abstract List<Rp> toListResponse(List<T> tList);

    public PageResponse<Rp> toPageResponse(List<T> tList, int total) {
        List<Rp> rpList = toListResponse(tList);
        return new PageResponse<>(total, rpList);
    }

    Long mapLocalDateTimeToEpochTimestamp(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toEpochSecond(ZoneOffset.UTC) : null;
    }

    LocalDateTime mapEpochTimestampToLocalDateTime(Long epochTimestamp) {
        return epochTimestamp != null
                ? LocalDateTime.ofInstant(Instant.ofEpochSecond(epochTimestamp), ZoneOffset.UTC)
                : null;
    }
}
