package latihan.controller;

import latihan.entity.User;
import latihan.service.UserService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
public class UserDetailController extends GenericForwardComposer<Component> {
    private final UserService userService = new UserService();
    private Long id;

    @Wire
    private Label userId;
    @Wire
    private Label name;
    @Wire
    private Label role;
    @Wire
    private Label createdAt;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        String idParam = Executions.getCurrent().getParameter("id");

        if (idParam != null) {
            try {
                this.id = Long.parseLong(idParam);
                getUserData();
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format: " + idParam);
            }
        } else {
            System.out.println("ID parameter is missing.");
        }
    }

    public void getUserData(){
        User u = userService.getUserById(id);

        if(u != null){
            userId.setValue(u.getId().toString());
            name.setValue(u.getName());
            role.setValue(u.getRole().toString());
            createdAt.setValue(u.getCreatedDate().toString());
        }
    }
}

