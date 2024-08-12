package vn.eledevo.vksbe.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.eledevo.vksbe.entity.User;

@Repository
public interface UserRepository extends BaseRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByIdentificationNumber(String identificationNumber);

    @Query ("SELECT u FROM User u " +
            "WHERE (:username IS NULL OR u.username LIKE %:username%) " +
            "AND (:phone IS NULL OR u.phone LIKE %:phone%) " +
            "AND (:identificationNumber IS NULL OR u.identificationNumber LIKE %:identificationNumber%) ")
    List<User> searchUsers (
            @Param("username") String username,
            @Param("phone") String phone,
            @Param("identificationNumber") String identificationNumber
    );

    @Query ("SELECT u FROM User u " +
            "WHERE (:username IS NULL OR u.username LIKE %:username%) " +
            "AND (:phone IS NULL OR u.phone LIKE %:phone%) " +
            "AND (:identificationNumber IS NULL OR u.identificationNumber LIKE %:identificationNumber%) ")
    List<User> listUserSearchedAndPagingFromDB (
            @Param("username") String username,
            @Param("phone") String phone,
            @Param("identificationNumber") String identificationNumber,
            Pageable pageable
    );
}
