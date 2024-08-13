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
        List<Room> roomListFromDB = repository.findAll();
        List<RoomResponse> roomListUserReceived =
                roomListFromDB.stream().map(mapper::toResponse).collect(Collectors.toList());
        return roomListUserReceived;
    }

    @Override
    public RoomResponse addRoom(RoomRequest roomRequest) throws ValidationException {
        if (repository.existsByRoomNumber(roomRequest.getRoomNumber())) {
            throw new ValidationException("Số phòng này", "đã tồn tại");
        }

        // Khởi tạo đối tượng User
        User userUUID = new User();

        // Lưu UserId (UUID) vào biến userUUID của đối tượng User
        userUUID.setId(SecurityUtils.getUserId());

        /**
         * Vì bên entity Room đang lưu (room_type_id) là 1 đối tượng RoomType -> Ko lưu đc trực tiếp id ở rq xuống
         * -> Mà phải lưu đối tượng -> Phải dùng findById -> Tìm đối tượng RoomType có id giống với id ở rq xuống
         * -> Và gán vào biến roomTypeID -> Biến roomTypeID chứa đối tượng RoomType đúng với id ở rq xuống
         * -> Gán giá trị đối tượng đó vào đối tượng RoomType ở Room (đang là khóa ngoại FK)
         * => Mới lưu đc
         *
         * => Cùng KDL mới lưu đc (Ban đầu số != đối tượng) -> Ko lưu đc
         */
        Optional<RoomType> roomTypeOptional = roomTypeRepository.findById(roomRequest.getRoomTypeId());
        RoomType roomTypeID = roomTypeOptional.get();

        // Chuyển rq -> entity
        Room room = mapper.toEntity(roomRequest);

        // Set (lưu) UUID vào đối tượng User ở Room entity
        room.setUser(userUUID);

        // Lưu giá trị đối tượng đã lấy đc đúng theo id ở rq gửi xuống vào -> RoomType ở Room (đang là khóa ngoại FK)
        room.setRoomType(roomTypeID);

        // Set trạng thái phòng mặc định là "ACTIVE"
        room.setStatus(RoomStatus.ACTIVE);

        // Lưu data xuống db
        Room roomAddData = repository.save(room);

        // Chuyển entity -> rp
        return mapper.toResponse(roomAddData);
    }

    @Override
    public RoomResponse updateRoom(Integer id, RoomRequest roomRequest) throws ValidationException {
        Room room = repository.findById(id).orElseThrow(() -> new ValidationException("Room", "Room not found!"));

        if (repository.existsByRoomNumber(roomRequest.getRoomNumber())) {
            throw new ValidationException("Số phòng này", "đã tồn tại");
        }

        User userUUID = new User();
        userUUID.setId(SecurityUtils.getUserId());

        Optional<RoomType> roomTypeOptional = roomTypeRepository.findById(roomRequest.getRoomTypeId());
        RoomType roomTypeID = roomTypeOptional.get();

        room.setUser(userUUID);
        room.setRoomType(roomTypeID);
        room.setStatus(RoomStatus.valueOf(roomRequest.getStatus()));

        room.setName(roomRequest.getName());
        room.setFloor(roomRequest.getFloor());
        room.setRoomNumber(roomRequest.getRoomNumber());
        room.setDescription(roomRequest.getDescription());
        room.setPrice(roomRequest.getPrice());

        // Lưu data xuống db
        Room roomUpdateData = repository.save(room);

        return mapper.toResponse(roomUpdateData);
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
            String status) {
        Pageable roomPageable =
                PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.valueOf(orderBy.toUpperCase()), orderedColumn));

        List<Room> roomList =
                repository.listRoomSearchedAndPagingFromDB(name, roomNumber, floor, roomTypeId, status, roomPageable);

        List<RoomResponse> listSortAndPagingAndSearch =
                roomList.stream().map(mapper::toResponse).toList();
        return listSortAndPagingAndSearch;
    }
}
