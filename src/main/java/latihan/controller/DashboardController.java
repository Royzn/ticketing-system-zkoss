package latihan.controller;

import latihan.entity.StatusLabel;
import latihan.entity.Ticket;
import latihan.service.TicketService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.util.List;

public class DashboardController extends SelectorComposer<Component> {

    @Wire
    private Label lblOpen, lblInProgress, lblClosed;
    @Wire
    private Label lblLow, lblMedium, lblHigh;
    @Wire
    private Rows recentRows;

    private final TicketService service = new TicketService();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        updateDashboard();
    }

    private void updateDashboard() {
        List<Ticket> tickets = service.getAllTickets();

        // hitung status
        long openCount = tickets.stream().filter(t -> "OPEN".equalsIgnoreCase(t.getStatus().toString())).count();
        long inProgressCount = tickets.stream().filter(t -> "IN_PROGRESS".equalsIgnoreCase(t.getStatus().toString())).count();
        long closedCount = tickets.stream().filter(t -> "CLOSED".equalsIgnoreCase(t.getStatus().toString())).count();

        // hitung priority
        long lowCount = tickets.stream().filter(t -> "LOW".equalsIgnoreCase(t.getPriority().toString())).count();
        long mediumCount = tickets.stream().filter(t -> "MEDIUM".equalsIgnoreCase(t.getPriority().toString())).count();
        long highCount = tickets.stream().filter(t -> "HIGH".equalsIgnoreCase(t.getPriority().toString())).count();

        // update label
        lblOpen.setValue("Open: " + openCount);
        lblInProgress.setValue("In Progress: " + inProgressCount);
        lblClosed.setValue("Closed: " + closedCount);

        lblLow.setValue("Low: " + lowCount);
        lblMedium.setValue("Medium: " + mediumCount);
        lblHigh.setValue("High: " + highCount);

        // isi tabel recent tickets (3 terakhir)
        recentRows.getChildren().clear(); // clear old rows
        tickets.stream()
                .sorted((a, b) -> Long.compare(b.getId(), a.getId())) // urutkan desc by id
                .limit(3)
                .forEach(t -> {
                    Row row = new Row();
                    row.appendChild(new Label(String.valueOf(t.getId())));
                    row.appendChild(new Label(t.getTitle()));
                    row.appendChild(new Label(StatusLabel.fromStatus(t.getStatus()).getLabel()));
                    row.appendChild(new Label(t.getPriority().toString()));
                    recentRows.appendChild(row);
                });
        System.out.println("Tickets size: " + tickets.size());

    }
}
