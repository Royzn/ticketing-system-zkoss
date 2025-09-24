package latihan.viewmodel;

import latihan.entity.Role;
import latihan.entity.RoleLabel;
import latihan.entity.User;
import latihan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Window;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class UserDetailViewModel {

    private UserService userService;

    private Long id;
    private User user;

    private String userId;
    private String name;
    private String role;
    private String createdAt;

    @Init
    @NotifyChange({"userId", "name", "role", "createdAt"})
    public void init() {
        userService = (UserService) SpringUtil.getBean("userService");

        String idParam = Executions.getCurrent().getParameter("id");
        if (idParam != null) {
            try {
                this.id = Long.parseLong(idParam);
                loadUserData();
            } catch (NumberFormatException e) {
                System.out.println("Invalid id parameter: " + idParam);
            }
        } else {
            System.out.println("No id parameter provided");
        }
    }

    private void loadUserData() {
        if (id == null) return;

        this.user = userService.getUserById(id);
        System.out.println(this.user);
        if (user != null) {
            this.userId = user.getId().toString();
            this.name = user.getName();
            this.role = RoleLabel.fromStatus(Role.valueOf(user.getRole())).getLabel();

            LocalDateTime createdDate = user.getCreatedDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", Locale.getDefault());
            this.createdAt = createdDate.format(formatter);
        }
    }

    @Command
    public void edit() {
        Executions.sendRedirect("update_user.zul?id=" + id);
    }

    @Command
    public void delete() {
        final Map<String, Object> args = new HashMap<>();
        args.put("confirmMessage", "Are you sure you want to delete user \"" + name + "\"?");
        args.put("onConfirm", new Runnable() {
            @Override
            public void run() {
                userService.deleteUser(user);
                Executions.sendRedirect("users.zul");
            }
        });

        Window confirmWin = (Window) Executions.createComponents("/confirm_win.zul", null, args);
        confirmWin.doModal();
    }


    // === Getters for Data Binding ===
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser(){return user;}
}
