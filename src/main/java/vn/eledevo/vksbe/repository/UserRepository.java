package vn.eledevo.vksbe.repository;

import java.util.Optional;
import java.util.UUID;

import vn.eledevo.vksbe.entity.User;

public interface UserRepository extends BaseRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByIdentificationNumber(String identificationNumber);
}
