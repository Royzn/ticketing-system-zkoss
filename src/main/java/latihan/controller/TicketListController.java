package latihan.controller;

import latihan.entity.*;
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

        loadTickets(service.getAllTickets());

        if(statusFilter != null){
            Comboitem i = new Comboitem();
            i.setLabel("All");
            i.setValue("All");
            i.setParent(statusFilter);
            for (Status s : Status.values()) {
                StatusLabel statusLabel = StatusLabel.fromStatus(s);
                if (statusLabel != null) {
                    Comboitem item = new Comboitem();
                    item.setLabel(statusLabel.getLabel());
                    item.setValue(s.name()); // the enum value
                    item.setParent(statusFilter);
                }
            }
        }

        if(priorityFilter != null){
            Comboitem i = new Comboitem();
            i.setLabel("All");
            i.setValue("All");
            i.setParent(priorityFilter);
            for (Priority p : Priority.values()) {
                Comboitem item = new Comboitem();
                item.setLabel(p.name());
                item.setValue(p);
                item.setParent(priorityFilter);
            }
        }

        // 🔥 Real-time filter: saat user pilih status/priority
        if(statusFilter!= null) statusFilter.addEventListener(Events.ON_CHANGE, e -> filterTickets());
        if(priorityFilter!=null) priorityFilter.addEventListener(Events.ON_CHANGE, e -> filterTickets());

        btnReset.addEventListener(Events.ON_CLICK, e -> resetFilter());
    }

    private void loadTickets(List<Ticket> tickets) {
        ticketList.getItems().clear();
        for (Ticket t : tickets) {
            ticketList.appendChild(buildListitem(t));
        }
    }

    private void filterTickets() {
        //buat ambil value
        Comboitem selectedStatusFilter = statusFilter.getSelectedItem();
        String status;
        if (selectedStatusFilter != null) {
            status = (String) selectedStatusFilter.getValue();
        } else {
            status = "";
        }

        String priority = priorityFilter.getValue();

        List<Ticket> tickets = service.getAllTickets()
                .stream()
                .filter(t -> {
                    boolean matchStatus = "All".equalsIgnoreCase(status) || status == null || status.isEmpty()
                            || t.getStatus().toString().equals(status);
                    boolean matchPriority = "All".equalsIgnoreCase(priority) || priority == null || priority.isEmpty()
                            || t.getPriority().toString().equals(priority);
                    return matchStatus && matchPriority;
                })
                .toList();

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
        item.appendChild(new Listcell(StatusLabel.fromStatus(t.getStatus()).getLabel()));
        item.appendChild(new Listcell(t.getPriority().toString()));
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
