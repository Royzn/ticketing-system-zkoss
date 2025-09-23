package latihan.viewmodel;

import latihan.dto.UserListDto;
import latihan.entity.RoleLabel;
import latihan.entity.User;
import latihan.service.UserService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;

import java.util.List;

public class UserListViewModel {

    private final UserService userService = new UserService();

    private ListModelList<UserListDto> users;

    @Init
    public void init() {
        loadUsers();
    }

    private void loadUsers() {
        List<UserListDto> userList = userService.getAllUsers();
        users = new ListModelList<>(userList);
    }

    // Getter for data binding to the Listbox model
    public ListModelList<UserListDto> getUsers() {
        return users;
    }

    // Command to navigate to user detail page
    @Command
    @NotifyChange("users")
    public void viewUser(@BindingParam("user") UserListDto user) {
        Executions.sendRedirect("user_detail.zul?id=" + user.getId());
    }

    // Helper method to get Role label for display
    public String getRoleLabel(UserListDto user) {
        return RoleLabel.fromStatus(user.getRole()).getLabel();
    }
}
