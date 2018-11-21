package pl.cba.gibcode.userComponent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.cba.gibcode.userComponent.model.User;
import pl.cba.gibcode.userComponent.model.UserDetails;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

	Optional<UserDetails> findByUser(User user);
}
