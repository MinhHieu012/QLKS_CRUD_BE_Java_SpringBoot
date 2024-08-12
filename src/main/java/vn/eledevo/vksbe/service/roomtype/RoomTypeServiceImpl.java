package vn.eledevo.vksbe.service.roomtype;

import static vn.eledevo.vksbe.constant.ResponseMessage.ROOM_TYPE_EXISTED;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.dto.request.roomtype.RoomTypeRequest;
import vn.eledevo.vksbe.dto.response.roomtype.RoomTypeResponse;
import vn.eledevo.vksbe.entity.RoomType;
import vn.eledevo.vksbe.entity.User;
import vn.eledevo.vksbe.exception.ValidationException;
import vn.eledevo.vksbe.mapper.RoomTypeMapper;
import vn.eledevo.vksbe.repository.RoomTypeRepository;
import vn.eledevo.vksbe.utils.SecurityUtils;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {

    RoomTypeRepository repository;

    RoomTypeMapper mapper;

    @Override
    public List<RoomTypeResponse> getAllRoomType() {
        List<RoomType> roomTypeListFromDB = repository.findAll();
        List<RoomTypeResponse> roomTypeList =
                roomTypeListFromDB.stream().map(mapper::toResponse).toList();
        return roomTypeList;
    }

    @Override
    public RoomTypeResponse addRoomType(RoomTypeRequest roomTypeRequest) throws ValidationException {

        RoomType roomType = mapper.toEntity(roomTypeRequest);

        // Khởi tạo đối tượng User lấy ra UUID
        User user = new User();
        user.setId(SecurityUtils.getUserId());

        if (repository.existsByName(roomTypeRequest.getName())) {
            throw new ValidationException("Tên phòng", ROOM_TYPE_EXISTED);
        }

        roomType.setName(roomTypeRequest.getName());
        roomType.setMaxPeople(roomTypeRequest.getMaxPeople());
        roomType.setDescription(roomTypeRequest.getDescription());
        roomType.setUser(user);

        RoomType roomTypeAddData = repository.save(roomType);

        return mapper.toResponse(roomTypeAddData);
    }

    @Override
    public RoomTypeResponse updateRoomType(Long id, RoomTypeRequest roomTypeRequest) throws ValidationException {

        RoomType roomType =
                repository.findById(id).orElseThrow(() -> new ValidationException("Room type", "Room type not found!"));

        // Khởi tạo đối tượng User lấy ra UUID
        User user = new User();
        user.setId(SecurityUtils.getUserId());

        if (repository.existsByName(roomTypeRequest.getName())) {
            throw new ValidationException("Tên phòng", ROOM_TYPE_EXISTED);
        }

        roomType.setName(roomTypeRequest.getName());
        roomType.setMaxPeople(roomTypeRequest.getMaxPeople());
        roomType.setDescription(roomTypeRequest.getDescription());
        roomType.setUser(user);

        RoomType roomTypeUpdateResult = repository.save(roomType);

        return mapper.toResponse(roomTypeUpdateResult);
    }

    @Override
    public RoomTypeResponse deleteRoomType(Long id) throws ValidationException {
        RoomType roomType =
                repository.findById(id).orElseThrow(() -> new ValidationException("Room type", "Room type not found!"));
        repository.deleteById(id);
        return null;
    }

    @Override
    public List<RoomTypeResponse> filterRoomType(
            String orderBy, int page, int limit, String orderedColumn, String name, String maxPeople) {
        Pageable roomTypePageable =
                PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.valueOf(orderBy.toUpperCase()), orderedColumn));

        List<RoomType> roomTypeList = repository.listRoomTypeSearchedAndPagingFromDB(name, maxPeople, roomTypePageable);

        List<RoomTypeResponse> roomTypeResponseList =
                roomTypeList.stream().map(mapper::toResponse).collect(Collectors.toList());
        return roomTypeResponseList;
    }
}
