package latihan.controller;

import latihan.entity.PriorityLabel;
import latihan.entity.StatusLabel;
import latihan.entity.Ticket;
import latihan.service.TicketService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TicketListController {

    private TicketService service = new TicketService();
    private List<Ticket> tickets;

    // --- Perubahan Tipe Data ---
    private List<StatusLabel> statusOptions;
    private List<PriorityLabel> priorityOptions;

    // Pilihan "All" sekarang direpresentasikan dengan nilai null
    private StatusLabel selectedStatus;
    private PriorityLabel selectedPriority;

    @Init
    public void init() {
        tickets = service.getAllTickets();

        // --- Mengisi opsi langsung dari Enum ---
        statusOptions = Arrays.asList(StatusLabel.values());
        priorityOptions = Arrays.asList(PriorityLabel.values());

        // Nilai awal filter adalah null (artinya "All")
        selectedStatus = null;
        selectedPriority = null;
    }

    public List<Ticket> getFilteredTickets() {
        return tickets.stream()
                .filter(t -> selectedStatus == null // Jika null, berarti "All", loloskan semua
                        || t.getStatus() == selectedStatus.getStatus()) // Bandingkan enum Status
                .filter(t -> selectedPriority == null // Jika null, berarti "All", loloskan semua
                        || t.getPriority() == selectedPriority.getPriority()) // Bandingkan enum Priority
                .collect(Collectors.toList());
    }

    // --- Getter dan Setter yang diperbarui ---
    public List<StatusLabel> getStatusOptions() {
        return statusOptions;
    }

    public List<PriorityLabel> getPriorityOptions() {
        return priorityOptions;
    }

    public StatusLabel getSelectedStatus() {
        return selectedStatus;
    }

    @NotifyChange("filteredTickets") // Tambahkan notifikasi saat setter dipanggil
    public void setSelectedStatus(StatusLabel selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

    public PriorityLabel getSelectedPriority() {
        return selectedPriority;
    }

    @NotifyChange("filteredTickets") // Tambahkan notifikasi saat setter dipanggil
    public void setSelectedPriority(PriorityLabel selectedPriority) {
        this.selectedPriority = selectedPriority;
    }


    @Command
    @NotifyChange({"filteredTickets", "selectedStatus", "selectedPriority"}) // Notifikasi semua yang berubah
    public void resetFilter() {
        selectedStatus = null;
        selectedPriority = null;
    }

    @Command
    public void viewTicket(@BindingParam("id") Long id) {
        Executions.sendRedirect("ticket_detail.zul?id=" + id);
    }
}