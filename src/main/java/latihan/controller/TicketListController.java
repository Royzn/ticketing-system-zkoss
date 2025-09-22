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
    private Button btnSearch, btnReset;

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
            service.createTicket("Keyboard broken", "Keys not working",
                    "Closed", "Low", "Admin3", "UserC");
        }

        loadTickets(service.getAllTickets());

        // 🔥 Real-time filter: saat user pilih status/priority
        statusFilter.addEventListener(Events.ON_CHANGE, e -> filterTickets());
        priorityFilter.addEventListener(Events.ON_CHANGE, e -> filterTickets());

        btnReset.addEventListener(Events.ON_CLICK, e -> resetFilter());
    }

    private void loadTickets(List<Ticket> tickets) {
        ticketList.getItems().clear();
        for (Ticket t : tickets) {
            ticketList.appendChild(buildListitem(t));
        }
    }

    private void filterTickets() {
        String status = statusFilter.getValue();
        String priority = priorityFilter.getValue();

        List<Ticket> tickets = service.getAllTickets()
                .stream()
                .filter(t -> {
                    boolean matchStatus = "All".equalsIgnoreCase(status) || status == null || status.isEmpty()
                            || t.getStatus().equalsIgnoreCase(status);
                    boolean matchPriority = "All".equalsIgnoreCase(priority) || priority == null || priority.isEmpty()
                            || t.getPriority().equalsIgnoreCase(priority);
                    return matchStatus && matchPriority;
                })
                .toList(); // Java 16+ (kalau Java 8, pakai collect(Collectors.toList()))

        loadTickets(tickets);
    }


    private void resetFilter() {
        statusFilter.setValue("All");
        priorityFilter.setValue("All");
        loadTickets(service.getAllTickets());
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
