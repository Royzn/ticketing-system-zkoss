package latihan.viewmodel;

import latihan.entity.Role;
import latihan.entity.RoleLabel;
import latihan.entity.User;
import latihan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.SpringUtil;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserFormViewModel {

    private UserService service;

    private Long id;
    private User user;

    private String name;
    private RoleOption selectedRole;
    private String username;
    private String password;
    private String errorMsg;

    private List<RoleOption> roleOptions;

    // Class to hold role options for dropdown
    public static class RoleOption {
        private final String value;
        private final String label;

        public RoleOption(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }

    @Init
    public void init() {
        service = (UserService) SpringUtil.getBean("userService");

        loadRoleOptions();

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

    private void loadRoleOptions() {
        roleOptions = new ArrayList<>();
        for (Role r : Role.values()) {
            RoleLabel roleLabel = RoleLabel.fromStatus(r);
            roleOptions.add(new RoleOption(r.name(), roleLabel.getLabel()));
        }
    }

    private void loadUserData() {
        this.user = service.getUserById(id);
        if (user != null) {
            this.name = user.getName();
            for (RoleOption option : roleOptions) {
                if (option.getValue().equals(user.getRole())) {
                    this.selectedRole = option;
                    break;
                }
            }
        }
    }

    @Command
    // === VALIDASI ===
    private boolean isInvalidForm() {
        if (name == null || name.trim().isEmpty()) {
            errorMsg = "Name is required.";
            Clients.showNotification(errorMsg, "error", null, "top_center", 3000);
            return true;
        }

        if (selectedRole == null || selectedRole.getValue() == null) {
            errorMsg = "Role is required.";
            Clients.showNotification(errorMsg, "error", null, "top_center", 3000);
            return true;
        }

        if (username == null || username.trim().isEmpty()) {
            errorMsg = "Username is required.";
            Clients.showNotification(errorMsg, "error", null, "top_center", 3000);
            return true;
        }

        if (password == null || password.trim().isEmpty()) {
            errorMsg = "Role is required.";
            Clients.showNotification(errorMsg, "error", null, "top_center", 3000);
            return true;
        }

        errorMsg = null;
        return false;
    }

    @Command
    @NotifyChange("errorMsg")
    public void save() {
        if (isInvalidForm()) return;

        String roleValue = selectedRole.getValue();
        service.createUser(name.trim(), roleValue, username, password);

        Executions.sendRedirect("users.zul");
    }

    @Command
    @NotifyChange("errorMsg")
    public void update() {
        if (isInvalidForm()) return;

        String roleValue = selectedRole.getValue();
        service.updateUser(user, name.trim(), roleValue);

        Executions.sendRedirect("users.zul");
    }

    @Command
    public void cancel() {
        if (id != null) {
            Executions.sendRedirect("user_detail.zul?id=" + id);
        } else {
            Executions.sendRedirect("users.zul");
        }
    }

    // === Getters and Setters for ZUL Binding ===

    public String getName() {
        return name;
    }

    @NotifyChange("name")
    public void setName(String name) {
        this.name = name;
    }

    public RoleOption getSelectedRole() {
        return selectedRole;
    }

    @NotifyChange("selectedRole")
    public void setSelectedRole(RoleOption selectedRole) {
        this.selectedRole = selectedRole;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public List<RoleOption> getRoleOptions() {
        return roleOptions;
    }

    public boolean isNew() {
        return id == null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
