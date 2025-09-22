package latihan.controller;

import latihan.entity.User;
import latihan.service.UserService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserDetailController extends SelectorComposer<Component> {
    private final UserService userService = new UserService();
    private Long id;

    @Wire
    private Label userId, name, role, createdAt;
    @Wire
    private Button editBtn, deleteBtn;


    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Selectors.wireComponents(comp, this, false);

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

        if(editBtn != null){
            editBtn.addEventListener(Events.ON_CLICK, e -> {
                String targetUrl = "update_user.zul?id=" + id;
                Executions.sendRedirect(targetUrl);
            });
        }

        if (deleteBtn != null) {
            deleteBtn.addEventListener(Events.ON_CLICK, e -> {
                // Load confirmation modal dynamically
                Window confirmWin = (Window) Executions.createComponents(
                        "/confirm_win.zul", null, null);

                // Get the modal's controller and configure it
                ConfirmModalController modalCtrl = (ConfirmModalController) confirmWin.getAttribute("controller");
                if(modalCtrl != null){
                    modalCtrl.setConfirmMessage("Are you sure you want to delete user \"" + name.getValue() + "\"?");
                    modalCtrl.setOnConfirm(() -> {
                        userService.deleteUser(id);
                        Executions.sendRedirect("users.zul");
                    });
                }
                confirmWin.doModal();
            });
        }

    }

    public void getUserData(){
        User u = userService.getUserById(id);

        if(u != null){
            userId.setValue(u.getId().toString());
            name.setValue(u.getName());
            role.setValue(u.getRole().toString());
            LocalDateTime createdDate = u.getCreatedDate(); // e.g., LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd MMM yyyy, HH:mm", Locale.getDefault());

            String formatted = createdDate.format(formatter);
            createdAt.setValue(formatted);
        }
    }
}

