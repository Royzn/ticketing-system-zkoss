package latihan.repository;

import latihan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // Custom method to get users where role = 'AGENT'
    @Query("SELECT u FROM User u WHERE u.role = 'AGENT'")
    List<User> getAgents();
}
