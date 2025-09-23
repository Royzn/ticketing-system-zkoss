package latihan.controller;

import latihan.entity.StatusLabel;
import latihan.entity.Ticket;
import latihan.service.TicketService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.util.List;

public class TicketController extends SelectorComposer<Component> {

    @Wire
    private Listbox ticketList;
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

        loadTickets();

        if (saveBtn != null) {
            saveBtn.addEventListener(Events.ON_CLICK, e -> saveTicket());
        }
    }

    private void loadTickets() {
        if (ticketList == null) return;

        ticketList.getItems().clear();
        List<Ticket> tickets = service.getAllTickets();

        for (Ticket t : tickets) {
            Listitem item = new Listitem();
            item.appendChild(new Listcell(String.valueOf(t.getId())));
            item.appendChild(new Listcell(t.getTitle()));
            item.appendChild(new Listcell(StatusLabel.fromStatus(t.getStatus()).getLabel()));
            item.appendChild(new Listcell(t.getPriority().toString()));
            item.appendChild(new Listcell(t.getAssignedTo()));

            Button viewBtn = new Button("View");
            viewBtn.setStyle("background:#3498db; color:white; border-radius:6px;");
            viewBtn.addEventListener(Events.ON_CLICK, ev ->
                    Executions.sendRedirect("ticket_detail.zul?id=" + t.getId())
            );

            Listcell actionCell = new Listcell();
            actionCell.appendChild(viewBtn);
            item.appendChild(actionCell);

            ticketList.appendChild(item);
        }
    }

    private void saveTicket() {
        String title = titleBox.getValue();
        String desc = descBox.getValue();
        String status = statusBox.getValue();
        String priority = priorityBox.getValue();
        String assignedTo = assignedToBox.getValue();
        String requester = requesterBox.getValue();

        service.createTicket(title, desc, status, priority, assignedTo,
                (requester == null || requester.isEmpty()) ? "Guest" : requester);

        Executions.sendRedirect("tickets.zul");
    }
}
