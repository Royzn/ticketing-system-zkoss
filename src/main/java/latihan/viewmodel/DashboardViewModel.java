package latihan.viewmodel;

import latihan.entity.Priority;
import latihan.entity.PriorityLabel;
import latihan.entity.Status;
import latihan.entity.StatusLabel;
import latihan.entity.Ticket;
import latihan.service.TicketService;
import org.springframework.stereotype.Component;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zkplus.spring.SpringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DashboardViewModel {

    private TicketService service;

    // Properti untuk menyimpan hasil perhitungan
    private long openCount;
    private long inProgressCount;
    private long closedCount;
    private long lowCount;
    private long mediumCount;
    private long highCount;

    // Properti untuk menyimpan daftar tiket terbaru
    private List<Ticket> recentTickets;

    @Init
    public void init() {
        service = (TicketService) SpringUtil.getBean("ticketService");
        loadDashboardData();
    }

    private void loadDashboardData() {
        List<Ticket> tickets = service.getAllTickets();
        if (tickets == null || tickets.isEmpty()) {
            recentTickets = Collections.emptyList();
            return;
        }

        // Hitung jumlah tiket berdasarkan Status
        openCount = tickets.stream().filter(t -> Objects.equals(t.getStatus(), Status.OPEN.toString())).count();
        inProgressCount = tickets.stream().filter(t -> Objects.equals(t.getStatus(), Status.IN_PROGRESS.toString())).count();
        closedCount = tickets.stream().filter(t -> Objects.equals(t.getStatus(), Status.CLOSED.toString())).count();

        // Hitung jumlah tiket berdasarkan Prioritas
        lowCount = tickets.stream().filter(t -> Objects.equals(t.getPriority(), Priority.LOW.toString())).count();
        mediumCount = tickets.stream().filter(t -> Objects.equals(t.getPriority(), Priority.MEDIUM.toString())).count();
        highCount = tickets.stream().filter(t -> Objects.equals(t.getPriority(), Priority.HIGH.toString())).count();

        // Ambil 5 tiket terbaru
        // Ambil 5 tiket terbaru berdasarkan ID (besar → kecil)
        recentTickets = tickets.stream()
                .sorted((t1, t2) -> Long.compare(t2.getId(), t1.getId()))
                .limit(5)
                .collect(Collectors.toList());

    }

    // --- Getters untuk Label ---
    public String getOpenCountLabel() { return "Open: " + openCount; }
    public String getInProgressCountLabel() { return "In Progress: " + inProgressCount; }
    public String getClosedCountLabel() { return "Closed: " + closedCount; }
    public String getLowCountLabel() { return "Low: " + lowCount; }
    public String getMediumCountLabel() { return "Medium: " + mediumCount; }
    public String getHighCountLabel() { return "High: " + highCount; }

    // --- Getter untuk daftar tiket terbaru ---
    public List<Ticket> getRecentTickets() {
        return recentTickets;
    }

    // --- Helper Methods untuk label bagus ---
    public String getStatusLabelText(Status status) {
        if (status == null) return "N/A";
        StatusLabel sl = StatusLabel.fromStatus(status);
        return sl != null ? sl.getLabel() : status.name();
    }

    public String getPriorityLabelText(Priority priority) {
        if (priority == null) return "N/A";
        PriorityLabel pl = PriorityLabel.fromPriority(priority);
        return pl != null ? pl.getLabel() : priority.name();
    }

    // --- Getter untuk Chart.js ---
    public long getOpenCount() { return openCount; }
    public long getInProgressCount() { return inProgressCount; }
    public long getClosedCount() { return closedCount; }

    public long getLowCount() { return lowCount; }
    public long getMediumCount() { return mediumCount; }
    public long getHighCount() { return highCount; }
}
