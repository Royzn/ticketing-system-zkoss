package latihan.viewmodel;

import latihan.entity.Role;
import latihan.entity.RoleLabel;
import latihan.entity.User;
import latihan.service.UserService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;

import java.util.ArrayList;
import java.util.List;

public class UserFormViewModel {

    private final UserService service = new UserService();

    private Long id;
    private User user;

    private String name;
    private RoleOption selectedRole;  // Role enum name as string, e.g. "ADMIN"
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
    public void init(@ExecutionArgParam("id") Long userId) {
        this.id = userId;
        loadRoleOptions();

        if (id != null) {
            loadUserData();
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
                if (option.getValue().equals(user.getRole().name())) {
                    this.selectedRole = option;
                    break;
                }
            }
        }
    }

    @Command
    @NotifyChange("errorMsg")
    public void save() {
        if (isInvalidForm()) return;
        String roleValue = selectedRole != null ? selectedRole.getValue() : null;

        service.createUser(name.trim(), roleValue);
        Executions.sendRedirect("users.zul");
    }

    @Command
    @NotifyChange("errorMsg")
    public void update() {
        if (isInvalidForm()) return;
        String roleValue = selectedRole != null ? selectedRole.getValue() : null;

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

    private boolean isInvalidForm() {
        if (name == null || name.trim().isEmpty()) {
            errorMsg = "Name is required.";
            return true;
        }

        if (selectedRole == null || selectedRole.getValue() == null) {
            errorMsg = "Role is required.";
            return true;
        }

        errorMsg = null;
        return false;
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
        System.out.println(selectedRole);
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
}
