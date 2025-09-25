package latihan.viewmodel;

import latihan.dto.CountDto;
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

        CountDto data = service.getDashboardStats();

        openCount = data.getOpenCount();
        inProgressCount = data.getInProgressCount();
        closedCount = data.getClosedCount();
        lowCount = data.getLowCount();
        mediumCount = data.getMediumCount();
        highCount = data.getHighCount();

        recentTickets = service.getFiveMostRecentTickets();
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


    // --- Getter untuk Chart.js ---
    public long getOpenCount() { return openCount; }
    public long getInProgressCount() { return inProgressCount; }
    public long getClosedCount() { return closedCount; }

    public long getLowCount() { return lowCount; }
    public long getMediumCount() { return mediumCount; }
    public long getHighCount() { return highCount; }
}
