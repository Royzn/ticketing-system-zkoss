package latihan.controller; // <-- DIUBAH DI SINI

import latihan.entity.Priority;
import latihan.entity.Status;
import latihan.entity.StatusLabel;
import latihan.entity.Ticket;
import latihan.service.TicketService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;

import java.util.Arrays;
import java.util.List;

public class TicketFormController {

    private TicketService service = new TicketService();
    private Ticket currentTicket;

    private List<Status> statusOptions;
    private List<Priority> priorityOptions;

    @Init
    public void init(@QueryParam("id") String idParam) {
        statusOptions = Arrays.asList(Status.values());
        priorityOptions = Arrays.asList(Priority.values());

        if (idParam != null && !idParam.isEmpty()) {
            Long id = Long.parseLong(idParam);
            currentTicket = service.getTicket(id);
        } else {
            currentTicket = new Ticket();
        }
    }

    public String getStatusLabelText(Status status) {
        if (status == null) return "";
        StatusLabel sl = StatusLabel.fromStatus(status);
        return sl != null ? sl.getLabel() : status.name();
    }

    @Command
    public void saveTicket() {
        if (currentTicket.getId() == null) {
            service.createTicket(
                    currentTicket.getTitle(),
                    currentTicket.getDescription(),
                    currentTicket.getStatus() != null ? currentTicket.getStatus().name() : null,
                    currentTicket.getPriority() != null ? currentTicket.getPriority().name() : null,
                    currentTicket.getAssignedTo(),
                    currentTicket.getRequester()
            );
        } else {
            service.updateTicket(
                    currentTicket,
                    currentTicket.getTitle(),
                    currentTicket.getDescription(),
                    currentTicket.getStatus() != null ? currentTicket.getStatus().name() : null,
                    currentTicket.getPriority() != null ? currentTicket.getPriority().name() : null,
                    currentTicket.getAssignedTo(),
                    currentTicket.getRequester()
            );
        }
        Executions.sendRedirect("tickets.zul");
    }

    // --- Getter dan Setter (Wajib untuk Data Binding) ---
    public Ticket getCurrentTicket() { return currentTicket; }
    public void setCurrentTicket(Ticket currentTicket) { this.currentTicket = currentTicket; }
    public List<Status> getStatusOptions() { return statusOptions; }
    public List<Priority> getPriorityOptions() { return priorityOptions; }
}