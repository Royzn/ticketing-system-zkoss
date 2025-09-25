package latihan.service;

import latihan.dto.UserListDto;
import latihan.entity.RoleEntity;
import latihan.entity.User;
import latihan.exception.UsernameAlreadyExistsException;
import latihan.repository.RoleRepository;
import latihan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    @Autowired
    RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserListDto> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(user -> new UserListDto(user.getId(), user.getName(), user.getRole(), user.getUsername()))
                .collect(Collectors.toList());
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<UserListDto> getAgent() {
        return repository.getAgents()
                .stream()
                .map(user -> new UserListDto(user.getId(), user.getName(), user.getRole(), user.getUsername()))
                .collect(Collectors.toList());
    }

    public void createUser(String name, Long roleId, String username, String password) {
        if (repository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("Username is already taken");
        }

        RoleEntity role = roleRepository.findById(roleId).orElse(null);
        if(role == null) throw new UsernameAlreadyExistsException("Role Not Found");

        User u = new User(null, name, role, LocalDateTime.now(), username, passwordEncoder.encode(password));
        repository.save(u);
    }

    public void updateUser(User user, String name, Long roleId, String username, String password, String oldUsername) {
        if (repository.existsByUsername(username) && !oldUsername.equals(username)) {
            throw new UsernameAlreadyExistsException("Username is already taken");
        }

        RoleEntity role = roleRepository.findById(roleId).orElse(null);
        if(role == null) throw new UsernameAlreadyExistsException("Role Not Found");

        user.setName(name);
        user.setRole(role);
        user.setUpdatedDate(LocalDateTime.now());
        if(password!=null && !password.isEmpty()){
            user.setPasswordHash(passwordEncoder.encode(password));
        }
        user.setUsername(username);
        repository.save(user);
    }

    public void deleteUser(User u) {
        repository.delete(u);
    }

    public List<RoleEntity> getAllRoles(){
        return roleRepository.findAll();
    }
}
