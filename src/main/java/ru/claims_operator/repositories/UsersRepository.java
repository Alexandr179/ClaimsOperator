package ru.claims_operator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.claims_operator.models.User;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {

    @Query("select user FROM User user JOIN user.tokens token WHERE token = ?1")
    Optional<User> findByToken(String token);
}
