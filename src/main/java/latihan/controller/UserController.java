package latihan.controller;

import latihan.dto.UserListDto;
import latihan.entity.RoleLabel;
import latihan.entity.Ticket;
import latihan.entity.User;
import latihan.service.UserService;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import java.util.List;

public class UserController extends SelectorComposer<Component> {
    private final UserService userService = new UserService();
    @Wire
    private Listbox userList;

    @Override
    public void doAfterCompose(Component comp) throws Exception{
        super.doAfterCompose(comp);
        loadUsers();
    }

    private void loadUsers() {
        if (userList == null) return;

        userList.getItems().clear();
        List<UserListDto> users = userService.getAllUsers();

        for (UserListDto u : users) {
            Listitem item = new Listitem();
            item.appendChild(new Listcell(String.valueOf(u.getId())));
            item.appendChild(new Listcell(u.getName()));
            item.appendChild(new Listcell(RoleLabel.fromStatus(u.getRole()).getLabel()));

            Button viewBtn = new Button("View");
            viewBtn.setStyle("background:#3498db; color:white; border-radius:6px;");
            viewBtn.addEventListener(Events.ON_CLICK, ev ->
                    Executions.sendRedirect("user_detail.zul?id=" + u.getId())
            );

            Listcell actionCell = new Listcell();
            actionCell.appendChild(viewBtn);
            item.appendChild(actionCell);

            userList.appendChild(item);
        }
    }


}
