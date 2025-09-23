package latihan.viewmodel;

import latihan.entity.RoleLabel;
import latihan.entity.User;
import latihan.service.UserService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;

import java.util.List;

public class UserListViewModel {

    private final UserService userService = new UserService();

    private ListModelList<User> users;

    @Init
    public void init() {
        loadUsers();
    }

    private void loadUsers() {
        List<User> userList = userService.getAllUsers();
        users = new ListModelList<>(userList);
    }

    // Getter for data binding to the Listbox model
    public ListModelList<User> getUsers() {
        return users;
    }

    // Command to navigate to user detail page
    @Command
    @NotifyChange("users")
    public void viewUser(@BindingParam("user") User user) {
        Executions.sendRedirect("user_detail.zul?id=" + user.getId());
    }

    // Helper method to get Role label for display
    public String getRoleLabel(User user) {
        return RoleLabel.fromStatus(user.getRole()).getLabel();
    }
}
