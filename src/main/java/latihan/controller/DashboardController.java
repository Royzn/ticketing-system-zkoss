package latihan.controller;

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
        long openCount = tickets.stream().filter(t -> "Open".equalsIgnoreCase(t.getStatus())).count();
        long inProgressCount = tickets.stream().filter(t -> "In Progress".equalsIgnoreCase(t.getStatus())).count();
        long closedCount = tickets.stream().filter(t -> "Closed".equalsIgnoreCase(t.getStatus())).count();

        // hitung priority
        long lowCount = tickets.stream().filter(t -> "Low".equalsIgnoreCase(t.getPriority())).count();
        long mediumCount = tickets.stream().filter(t -> "Medium".equalsIgnoreCase(t.getPriority())).count();
        long highCount = tickets.stream().filter(t -> "High".equalsIgnoreCase(t.getPriority())).count();

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
                    row.appendChild(new Label(t.getStatus()));
                    row.appendChild(new Label(t.getPriority()));
                    recentRows.appendChild(row);
                });
        System.out.println("Tickets size: " + tickets.size());

    }
}
