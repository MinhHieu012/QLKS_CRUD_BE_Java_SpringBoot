package vn.eledevo.vksbe.service.base;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.*;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.exception.ApiException;
import vn.eledevo.vksbe.exception.ValidationException;
import vn.eledevo.vksbe.mapper.BaseMapper;
import vn.eledevo.vksbe.repository.BaseRepository;

@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class BaseServiceImpl<Rq, Rp, T, ID> implements BaseService<Rq, Rp, ID> {
    BaseMapper<Rq, Rp, T> mapper;

    BaseRepository<T, ID> repository;

    protected BaseServiceImpl(BaseMapper<Rq, Rp, T> mapper, BaseRepository<T, ID> repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    protected Map<String, String> toInsertErrors(Rq rq) {
        return new HashMap<>();
    }

    protected Map<String, String> toUpdateErrors(ID id, Rq rq) {
        return new HashMap<>();
    }

    protected Map<String, String> toDeleteErrors(ID id) {
        return new HashMap<>();
    }

    protected Map<String, String> toDeleteErrors(Set<ID> ids) {
        return new HashMap<>();
    }

    @Override
    public Rp insert(Rq rq) throws ValidationException {
        Map<String, String> errors = toInsertErrors(rq);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        T t = mapper.toEntity(rq);
        T tResult = repository.save(t);
        return mapper.toResponse(tResult);
    }

    @Override
    public Rp update(ID id, Rq rq) throws ValidationException, ApiException {
        T t = repository
                .findById(id)
                .orElseThrow(() -> new ApiException(NOT_FOUND.value(), NOT_FOUND.getReasonPhrase()));
        Map<String, String> errors = toUpdateErrors(id, rq);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        T tUpdate = mapper.toEntityUpdate(rq, t);
        T tResult = repository.save(tUpdate);
        return mapper.toResponse(tResult);
    }

    @Override
    public Rp getById(ID id) {
        Optional<T> tOptional = repository.findById(id);
        return mapper.toResponse(tOptional.orElse(null));
    }

    @Override
    public List<Rp> getByIds(Set<ID> ids) {
        List<T> tList = repository.findAllById(ids);
        return mapper.toListResponse(tList);
    }

    @Override
    public Rp deleteById(ID id) throws ValidationException {
        Map<String, String> errors = toDeleteErrors(id);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        repository.deleteById(id);
        return null;
    }

    @Override
    public List<Rp> deleteByIds(Set<ID> ids) throws ValidationException {
        Map<String, String> errors = toDeleteErrors(ids);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        repository.deleteAllById(ids);
        return null;
    }

    //    @Override
    //    public Rp softDeleteById(ID id) throws ValidationException {
    //        Map<String, String> errors = toDeleteErrors(id);
    //        if (!errors.isEmpty()) {
    //            throw new ValidationException(errors);
    //        }
    //        repository.softDeleteById(id);
    //        return null;
    //    }
}
