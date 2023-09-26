package se.knzoon.leaguehelper.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByUsernameIn(Set<String> usernames);

    List<User> findAllByUsernameIsStartingWithOrderByUsername(String searchString);
}
