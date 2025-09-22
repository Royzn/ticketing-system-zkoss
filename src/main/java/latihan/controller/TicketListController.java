package latihan.controller;

import latihan.entity.Ticket;
import latihan.service.TicketService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.util.List;

public class TicketListController extends SelectorComposer<Component> {

    @Wire
    private Listbox ticketList;
    @Wire
    private Combobox statusFilter, priorityFilter;
    @Wire
    private Button btnSearch;

    private final TicketService service = new TicketService();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        // dummy data kalau kosong
        if (service.getAllTickets().isEmpty()) {
            service.createTicket("Email not working", "Cannot send/receive emails",
                    "Open", "High", "Admin1", "UserA");
            service.createTicket("VPN issue", "Cannot connect to VPN",
                    "In Progress", "Medium", "Admin2", "UserB");
        }

        loadTickets();

        btnSearch.addEventListener(Events.ON_CLICK, e -> filterTickets());
    }

    private void loadTickets() {
        ticketList.getItems().clear();
        for (Ticket t : service.getAllTickets()) {
            ticketList.appendChild(buildListitem(t));
        }
    }

    private void filterTickets() {
        String status = statusFilter.getValue();
        String priority = priorityFilter.getValue();

        ticketList.getItems().clear();
        for (Ticket t : service.getAllTickets()) {
            if (!"All".equalsIgnoreCase(status) && !t.getStatus().equalsIgnoreCase(status)) continue;
            if (!"All".equalsIgnoreCase(priority) && !t.getPriority().equalsIgnoreCase(priority)) continue;
            ticketList.appendChild(buildListitem(t));
        }
    }

    private Listitem buildListitem(Ticket t) {
        Listitem item = new Listitem();
        item.appendChild(new Listcell(String.valueOf(t.getId())));
        item.appendChild(new Listcell(t.getTitle()));
        item.appendChild(new Listcell(t.getStatus()));
        item.appendChild(new Listcell(t.getPriority()));
        item.appendChild(new Listcell(t.getAssignedTo()));

        Button btnView = new Button("View");
        btnView.setStyle("background:#3498db; color:white; border-radius:6px;");
        btnView.addEventListener(Events.ON_CLICK, e ->
                Executions.sendRedirect("ticket_detail.zul?id=" + t.getId())
        );

        Listcell actionCell = new Listcell();
        actionCell.appendChild(btnView);
        item.appendChild(actionCell);

        return item;
    }
}
