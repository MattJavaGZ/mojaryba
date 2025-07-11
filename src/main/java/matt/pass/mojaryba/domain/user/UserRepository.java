package matt.pass.mojaryba.domain.user;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByNickIgnoreCase(String nick);
    List<User> findAllByActivIsTrue();
}
