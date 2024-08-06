package vn.eledevo.vksbe.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import vn.eledevo.vksbe.entity.Token;

public interface TokenRepository extends BaseRepository<Token, Long> {
    @Query(
            value =
                    """
			select t from Token t inner join User u\s
			on t.user.id = u.id\s
			where u.id = :id and (t.isExpired = false or t.isRevoked = false)\s
			""")
    List<Token> findAllValidTokenByUser(UUID id);

    Optional<Token> findByAccessToken(String token);
}
