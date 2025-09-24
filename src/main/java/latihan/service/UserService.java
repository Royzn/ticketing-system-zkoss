package latihan.service;

import latihan.dto.UserListDto;
import latihan.entity.Role;
import latihan.entity.User;
import latihan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public List<UserListDto> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(user -> new UserListDto(user.getId(), user.getName(), Role.valueOf(user.getRole())))
                .collect(Collectors.toList());
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<UserListDto> getAgent() {
        return repository.getAgents()
                .stream()
                .map(user -> new UserListDto(user.getId(), user.getName(), Role.valueOf(user.getRole())))
                .collect(Collectors.toList());
    }

    public void createUser(String name, String role) {
        User u = new User(null, name, role.toUpperCase(), LocalDateTime.now());
        repository.save(u);
    }

    public void updateUser(User user, String name, String role) {
        user.setName(name);
        user.setRole(role.toUpperCase());
        user.setUpdatedDate(LocalDateTime.now());
        repository.save(user);
    }

    public void deleteUser(User u) {
        repository.delete(u);
    }
}
