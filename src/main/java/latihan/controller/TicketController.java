package latihan.controller;

import latihan.entity.Ticket;
import latihan.service.TicketService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;

import java.util.List;

public class TicketController {
    private TicketService service = new TicketService();
    private List<Ticket> tickets;
    private Ticket selected;
    private Ticket newTicket = new Ticket(); // untuk form create

    @Init
    public void init() {
        System.out.println("running");
        tickets = service.getAllTickets();

        if (tickets.isEmpty()) {
            service.createTicket("Email not working", "Cannot send or receive emails",
                    "Open", "High", "Admin1", "UserA");
            service.createTicket("VPN issue", "Cannot connect to VPN",
                    "In Progress", "Medium", "Admin2", "UserB");
            tickets = service.getAllTickets();
        }
    }

    @Command @NotifyChange("tickets")
    public void deleteTicket(@BindingParam("id") Long id) {
        service.deleteTicket(id);
        tickets = service.getAllTickets();
    }

    @Command @NotifyChange({"tickets","newTicket"})
    public void saveTicket() {
        service.createTicket(
                newTicket.getTitle(),
                newTicket.getDescription(),
                newTicket.getStatus(),
                newTicket.getPriority(),
                newTicket.getAssignedTo(),
                newTicket.getRequester() != null ? newTicket.getRequester() : "Guest"
        );
        newTicket = new Ticket(); // reset form
        tickets = service.getAllTickets();

        // 🔑 Redirect otomatis ke tickets.zul setelah save
        Executions.sendRedirect("tickets.zul");
    }

    public List<Ticket> getTickets() { return tickets; }

    public Ticket getSelected() { return selected; }
    public void setSelected(Ticket selected) { this.selected = selected; }

    public Ticket getNewTicket() { return newTicket; }
    public void setNewTicket(Ticket newTicket) { this.newTicket = newTicket; }
}
