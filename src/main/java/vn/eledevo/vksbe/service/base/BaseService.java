package vn.eledevo.vksbe.service.base;

import java.util.List;
import java.util.Set;

import vn.eledevo.vksbe.exception.ApiException;
import vn.eledevo.vksbe.exception.ValidationException;

public interface BaseService<Rq, Rp, ID> {
    Rp insert(Rq rq) throws ValidationException;

    Rp update(ID id, Rq rq) throws ValidationException, ApiException;

    Rp getById(ID id);

    List<Rp> getByIds(Set<ID> ids);

    Rp deleteById(ID id) throws ValidationException;

    List<Rp> deleteByIds(Set<ID> ids) throws ValidationException;

    Rp softDeleteById(ID id) throws ValidationException;
}
