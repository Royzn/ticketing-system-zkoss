package latihan.controller;

import latihan.entity.Role;
import latihan.entity.User;
import latihan.service.UserService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UserFormController extends SelectorComposer<Component> {
    @Wire
    private Textbox nameBox;
    @Wire
    private Combobox roleBox;
    @Wire
    private Button saveBtn;
    @Wire
    private Label errorMsg;
    @Wire
    private Button updateBtn;
    @Wire
    private Button cancelBtn;

    private Long id;
    private User user;

    private final UserService service = new UserService();

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
        }

        if (saveBtn != null) {
            saveBtn.addEventListener(Events.ON_CLICK, e -> addUser());
        }

        if(updateBtn !=null){
            updateBtn.addEventListener(Events.ON_CLICK, e -> updateUser());
        }

        if(cancelBtn != null){
            cancelBtn.addEventListener(Events.ON_CLICK, e -> {
                String targetUrl = "user_detail.zul?id=" + id;
                Executions.sendRedirect(targetUrl);
            });
        }
    }

    public void getUserData(){
        User u = service.getUserById(id);
        this.user = u;

        if(u != null){
            nameBox.setValue(u.getName());
            for (Comboitem item : roleBox.getItems()) {
                if (u.getRole().toString().equals(item.getLabel())) {
                    roleBox.setSelectedItem(item);
                    break;
                }
            }
        }
    }

    private void addUser() {
        if (nameBox.getValue().isEmpty()) {
            errorMsg.setValue("Name is required.");
            Clients.scrollIntoView(nameBox); // optional: scroll to field
            return;
        }

        if (roleBox.getValue().isEmpty()) {
            errorMsg.setValue("Role is required.");
            Clients.scrollIntoView(roleBox);
            return;
        }

        service.createUser(
                nameBox.getValue().trim(),
                roleBox.getValue()
        );

        Executions.sendRedirect("users.zul");
    }

    private void updateUser(){
        if (nameBox.getValue().isEmpty()) {
            errorMsg.setValue("Name is required.");
            Clients.scrollIntoView(nameBox); // optional: scroll to field
            return;
        }

        if (roleBox.getValue().isEmpty()) {
            errorMsg.setValue("Role is required.");
            Clients.scrollIntoView(roleBox);
            return;
        }

        service.updateUser(user, nameBox.getValue(), roleBox.getValue());

        Executions.sendRedirect("users.zul");
    }
}
