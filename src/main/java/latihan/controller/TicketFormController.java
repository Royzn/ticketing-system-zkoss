package latihan.controller;

import latihan.service.TicketService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

public class TicketFormController extends SelectorComposer<Component> {

    @Wire
    private Textbox titleBox, descBox, assignedToBox, requesterBox;
    @Wire
    private Combobox statusBox, priorityBox;
    @Wire
    private Button saveBtn;

    private final TicketService service = new TicketService();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        saveBtn.addEventListener(Events.ON_CLICK, e -> saveTicket());
    }

    private void saveTicket() {
        service.createTicket(
                titleBox.getValue(),
                descBox.getValue(),
                statusBox.getValue(),
                priorityBox.getValue(),
                assignedToBox.getValue(),
                requesterBox.getValue().isEmpty() ? "Guest" : requesterBox.getValue()
        );

        Executions.sendRedirect("tickets.zul");
    }
}
