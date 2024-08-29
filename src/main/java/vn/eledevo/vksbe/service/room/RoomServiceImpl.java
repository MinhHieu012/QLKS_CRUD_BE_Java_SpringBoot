package vn.eledevo.vksbe.service.room;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.RoomStatus;
import vn.eledevo.vksbe.dto.request.room.RoomRequest;
import vn.eledevo.vksbe.dto.response.room.RoomResponse;
import vn.eledevo.vksbe.dto.response.roomtype.RoomTypeResponseDTO;
import vn.eledevo.vksbe.entity.Room;
import vn.eledevo.vksbe.entity.RoomType;
import vn.eledevo.vksbe.entity.User;
import vn.eledevo.vksbe.exception.ValidationException;
import vn.eledevo.vksbe.mapper.RoomMapper;
import vn.eledevo.vksbe.repository.RoomRepository;
import vn.eledevo.vksbe.repository.RoomTypeRepository;
import vn.eledevo.vksbe.utils.SecurityUtils;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    RoomRepository repository;

    RoomTypeRepository roomTypeRepository;

    RoomMapper mapper;

    @Override
    public List<RoomResponse> getAllRoom() {
        List<Room> roomListFromDB = repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt", "updatedAt"));
        List<RoomResponse> roomListUserReceived =
                roomListFromDB.stream().map(mapper::toResponse).collect(Collectors.toList());
        return roomListUserReceived;
    }

    @Override
    public RoomResponse addRoom(RoomRequest roomRequest) throws ValidationException {
        if (repository.existsByRoomNumber(String.valueOf(roomRequest.getRoomNumber()))) {
            throw new ValidationException("roomNumber", "Số phòng này đã tồn tại!");
        }

        if (repository.existsByRoomTypeId(roomRequest.getRoomTypeId())) {
            throw new ValidationException("roomTypeExist", "Loại phòng này đã được sử dụng!");
        }

        User userUUID = new User();
        userUUID.setId(SecurityUtils.getUserId());

        Optional<RoomType> roomTypeFromDB = roomTypeRepository.findById(roomRequest.getRoomTypeId());
        if (roomTypeFromDB.isEmpty()) {
            throw new ValidationException("roomTypeExist", "Loại phòng bạn chọn không tồn tại!");
        }
        RoomType roomtype = roomTypeFromDB.get();

        Room room = mapper.toEntity(roomRequest);
        room.setRoomType(roomtype);
        room.setUser(userUUID);
        room.setStatus(RoomStatus.ACTIVE);

        Room roomAddData = repository.save(room);

        RoomResponse roomResponse = mapper.toResponse(roomAddData);

        return roomResponse;
    }

    @Override
    public RoomResponse updateRoom(Integer id, RoomRequest roomRequest) throws ValidationException {
        Room room = repository.findById(id).orElseThrow(() -> new ValidationException("Error", "Room not found!"));

        if (repository.existsByRoomNumber(String.valueOf(roomRequest.getRoomNumber()))) {
            throw new ValidationException("Lỗi", "Số phòng này đã tồn tại");
        }

        User userUUID = new User();
        userUUID.setId(SecurityUtils.getUserId());

        Optional<RoomType> roomTypeFromDB = roomTypeRepository.findById(roomRequest.getRoomTypeId());
        if (roomTypeFromDB.isEmpty()) {
            throw new ValidationException("Lỗi", "Loại phòng bạn chọn không tồn tại");
        }
        RoomType roomtype = roomTypeFromDB.get();

        room.setUser(userUUID);
        room.setRoomType(roomtype);
        room.setName(roomRequest.getName());
        room.setRoomNumber(String.valueOf(roomRequest.getRoomNumber()));
        room.setFloor(String.valueOf(roomRequest.getFloor()));
        room.setDescription(roomRequest.getDescription());
        room.setPrice(String.valueOf(roomRequest.getPrice()));
        room.setStatus(RoomStatus.valueOf(roomRequest.getStatus()));

        Room roomUpdateData = repository.save(room);

        RoomResponse roomResponse = mapper.toResponse(roomUpdateData);

        return roomResponse;
    }

    @Override
    public RoomResponse updateRoomStatus(Integer id, String status) throws ValidationException {
        Room room = repository.findById(id).orElseThrow(() -> new ValidationException("Room", "Room not found!"));

        User userUUID = new User();
        userUUID.setId(SecurityUtils.getUserId());
        room.setUser(userUUID);

        room.setStatus(RoomStatus.valueOf(status));

        Room roomStatusData = repository.save(room);

        return mapper.toResponse(roomStatusData);
    }

    @Override
    public List<RoomResponse> sortAndPagingAndSearch(
            String orderBy,
            int page,
            int limit,
            String orderedColumn,
            String name,
            String roomNumber,
            String floor,
            Long roomTypeId,
            String status)
            throws ValidationException {
        Pageable roomPageable =
                PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.valueOf(orderBy.toUpperCase()), orderedColumn));

        List<Room> roomList =
                repository.listRoomSearchedAndPagingFromDB(name, roomNumber, floor, roomTypeId, status, roomPageable);

        if (roomList.isEmpty()) {
            throw new ValidationException("Trống", "Không tìm thấy phòng tương ứng!");
        }

        List<RoomResponse> listSortAndPagingAndSearch = roomList.stream()
                .map(room -> {
                    RoomTypeResponseDTO roomTypeResponseDTO = new RoomTypeResponseDTO(
                            room.getRoomType().getId(),
                            room.getRoomType().getName(),
                            room.getRoomType().getMaxPeople(),
                            room.getRoomType().getDescription());
                    return RoomResponse.builder()
                            .id(room.getId())
                            .name(room.getName())
                            .roomNumber(room.getRoomNumber())
                            .floor(room.getFloor())
                            .roomType(roomTypeResponseDTO)
                            .price(room.getPrice())
                            .status(room.getStatus())
                            .build();
                })
                .collect(Collectors.toList());

        return listSortAndPagingAndSearch;
    }
}
