package latihan.viewmodel;

import latihan.dto.UserListDto;
import latihan.entity.RoleLabel;
import latihan.entity.User;
import latihan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;

import java.util.List;

@Component
public class UserListViewModel {

    private UserService userService;

    private ListModelList<UserListDto> users;

    @Init
    public void init() {
        userService = (UserService) SpringUtil.getBean("userService");
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
