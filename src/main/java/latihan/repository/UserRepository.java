package latihan.repository;

import latihan.dto.UserListDto;
import latihan.entity.Role;
import latihan.entity.Ticket;
import latihan.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private static UserRepository instance;
    private final List<User> users = new ArrayList<>();
    private Long sequence = 1L;

    public UserRepository(){
        users.add(new User(sequence++, "Varrel", Role.USER, LocalDateTime.now()));
        users.add(new User(sequence++, "Ikan", Role.AGENT, LocalDateTime.now()));
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public List<UserListDto> findAll() {
        return users.stream().map(e->{
            return new UserListDto(e.getId(), e.getName(), e.getRole());
        }).toList();
    }

    public Optional<User> findById(Long id) {
        return users.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public void save(User user) {
        if (user.getId() == null) {
            user.setId(sequence++);
            users.add(user);
        } else {
            users.removeIf(t -> t.getId().equals(user.getId()));
            users.add(user);
        }
        users.sort(Comparator.comparing(User::getId));
    }

    public void delete(Long id) {
        users.removeIf(t -> t.getId().equals(id));
    }
}
