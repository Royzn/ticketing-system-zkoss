package latihan.controller;

import latihan.entity.Priority;
import latihan.entity.PriorityLabel;
import latihan.entity.Status;
import latihan.entity.StatusLabel;
import latihan.entity.Ticket;
import latihan.service.TicketService;
import org.zkoss.bind.annotation.Init;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DashboardController {

    private final TicketService service = new TicketService();

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
        // Panggil metode untuk memuat dan memproses semua data
        loadDashboardData();
    }

    private void loadDashboardData() {
        List<Ticket> tickets = service.getAllTickets();
        if (tickets == null || tickets.isEmpty()) {
            recentTickets = Collections.emptyList();
            return;
        }

        // 1. Hitung jumlah tiket berdasarkan Status
        openCount = tickets.stream().filter(t -> Objects.equals(t.getStatus(), Status.OPEN.toString())).count();
        inProgressCount = tickets.stream().filter(t -> Objects.equals(t.getStatus(), Status.IN_PROGRESS.toString())).count();
        closedCount = tickets.stream().filter(t -> Objects.equals(t.getStatus(), Status.CLOSED.toString())).count();

        // 2. Hitung jumlah tiket berdasarkan Prioritas
        lowCount = tickets.stream().filter(t -> Objects.equals(t.getPriority(), Priority.LOW.toString())).count();
        mediumCount = tickets.stream().filter(t -> Objects.equals(t.getPriority(), Priority.MEDIUM.toString())).count();
        highCount = tickets.stream().filter(t -> Objects.equals(t.getPriority(), Priority.HIGH.toString())).count();

        // 3. Ambil 5 tiket terbaru untuk ditampilkan di grid
        recentTickets = tickets.stream()
                .sorted((t1, t2) -> t2.getCreatedDate().compareTo(t1.getCreatedDate())) // Urutkan berdasarkan tanggal terbaru
                .limit(5) // Ambil 5 tiket teratas
                .collect(Collectors.toList());
    }

    // --- Getters untuk Label Teks (Computed Properties) ---
    // Cara ini lebih bersih daripada menggabungkan String di file ZUL.

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

    // --- Helper Methods untuk mendapatkan label yang bagus (digunakan di ZUL) ---
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
}