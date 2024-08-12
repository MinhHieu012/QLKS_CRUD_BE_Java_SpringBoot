package vn.eledevo.vksbe.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.eledevo.vksbe.entity.RoomType;
import vn.eledevo.vksbe.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomTypeRepository extends BaseRepository<RoomType, Long> {
    Boolean existsByName(String name);

    @Query ("SELECT rt FROM RoomType rt " +
            "WHERE (:name IS NULL OR rt.name LIKE %:name%) " +
            "AND (:maxPeople IS NULL OR rt.maxPeople LIKE %:maxPeople%)" )
    List<RoomType> listRoomTypeSearchedAndPagingFromDB (
            @Param("name") String name,
            @Param("maxPeople") String maxPeople,
            Pageable pageable
    );
}
