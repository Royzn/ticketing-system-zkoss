package latihan.viewmodel;

import latihan.entity.RoleLabel;
import latihan.entity.User;
import latihan.service.UserService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UserDetailViewModel {

    private final UserService userService = new UserService();

    private Long id;
    private User user;

    private String userId;
    private String name;
    private String role;
    private String createdAt;

    @Init
    @NotifyChange({"userId", "name", "role", "createdAt"})
    public void init() {
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
            this.role = RoleLabel.fromStatus(user.getRole()).getLabel();

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
        var args = new java.util.HashMap<String, Object>();
        args.put("confirmMessage", "Are you sure you want to delete user \"" + name + "\"?");
        args.put("onConfirm", (Runnable) () -> {
            userService.deleteUser(id);
            Executions.sendRedirect("users.zul");
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
}
