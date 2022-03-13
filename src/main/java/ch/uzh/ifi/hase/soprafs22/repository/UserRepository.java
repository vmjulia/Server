package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    /**
     * was forced to make optional by the compiler meaning that it can be empty
     */
    Optional<User> findById(Long id);
}
