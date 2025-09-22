package latihan.controller;

import latihan.service.UserService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;

public class UserFormController extends SelectorComposer<Component> {
    @Wire
    private Textbox nameBox;
    @Wire
    private Combobox roleBox;
    @Wire
    private Button saveBtn;

    private final UserService service = new UserService();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        if (saveBtn != null) {
            saveBtn.addEventListener(Events.ON_CLICK, e -> addUser());
        }
    }

    private void addUser() {
        service.createUser(
                nameBox.getValue(),
                roleBox.getValue()
        );

        Executions.sendRedirect("users.zul");
    }
}
