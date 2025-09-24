package latihan.repository;

import latihan.dto.UserListDto;
import latihan.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();
    List<User> getAgents();
    Optional<User> findById(Long id);
    void save(User user);
    void delete(Long id);
}
