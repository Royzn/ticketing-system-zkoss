package latihan.service;

import latihan.entity.Role;
import latihan.entity.User;
import latihan.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

public class UserService {
    private final UserRepository repository = UserRepository.getInstance();

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public User createUser(String name, String role) {
        User u = new User(null, name, Role.valueOf(role.toUpperCase()), LocalDateTime.now());
        return repository.save(u);
    }

    public User updateTicket(User user) {
        return repository.save(user);
    }

    public void deleteTicket(Long id) {
        repository.delete(id);
    }
}
