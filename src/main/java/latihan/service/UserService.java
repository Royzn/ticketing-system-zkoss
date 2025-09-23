package latihan.service;

import latihan.dto.UserListDto;
import latihan.entity.Role;
import latihan.entity.User;
import latihan.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

public class UserService {
    private final UserRepository repository = UserRepository.getInstance();

    public List<UserListDto> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<UserListDto> getAgent(){
        return repository.getAgent();
    }

    public void createUser(String name, String role) {
        User u = new User(null, name, role.toUpperCase(), LocalDateTime.now());
        repository.save(u);
    }

    public void updateUser(User user, String name, String role) {
        user.setRole(role);
        user.setName(name);
        user.setUpdatedDate(LocalDateTime.now());
        repository.save(user);
    }

    public void deleteUser(Long id) {
        repository.delete(id);
    }
}
