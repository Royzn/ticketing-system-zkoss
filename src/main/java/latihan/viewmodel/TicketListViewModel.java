package latihan.viewmodel;

import latihan.entity.*;
import latihan.service.TicketService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TicketListViewModel {

    private TicketService service = new TicketService();
    private List<Ticket> tickets;

    // --- Perubahan Tipe Data ---
    private List<Option> statusOptions;
    private List<Option> priorityOptions;

    // Pilihan "All" sekarang direpresentasikan dengan nilai null
    private Option selectedStatus;
    private Option selectedPriority;

    @Init
    public void init() {
        tickets = service.getAllTickets();

        // --- Mengisi opsi langsung dari Enum ---
        loadStatusOptions();
        loadPriorityOptions();

        // Nilai awal filter adalah null (artinya "All")
        selectedStatus = statusOptions.get(0);
        selectedPriority = priorityOptions.get(0);
    }

    private void loadStatusOptions() {
        statusOptions = new ArrayList<>();
        statusOptions.add(new TicketListViewModel.Option("All", "All"));
        for (Status s : Status.values()) {
            StatusLabel statusLabel = StatusLabel.fromStatus(s);
            statusOptions.add(new TicketListViewModel.Option(s.name(), statusLabel.getLabel()));
        }
    }

    private void loadPriorityOptions() {
        priorityOptions = new ArrayList<>();
        priorityOptions.add(new TicketListViewModel.Option("All", "All"));
        for (Priority p : Priority.values()) {
            PriorityLabel priorityLabel = PriorityLabel.fromPriority(p);
            priorityOptions.add(new TicketListViewModel.Option(p.name(), priorityLabel.getLabel()));
        }
    }

    public static class Option {
        private final String value;
        private final String label;

        public Option(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }

    public List<Ticket> getFilteredTickets() {
        return tickets.stream()
                .filter(t -> selectedStatus.getValue().equals("All") // Jika null, berarti "All", loloskan semua
                        || t.getStatus().equals(selectedStatus.getValue()) ) // Bandingkan enum Status
                .filter(t -> selectedPriority.getValue().equals("All") // Jika null, berarti "All", loloskan semua
                        || t.getPriority().equals(selectedPriority.getValue())) // Bandingkan enum Priority
                .collect(Collectors.toList());
    }

    // --- Getter dan Setter yang diperbarui ---
    public List<Option> getStatusOptions() {
        return statusOptions;
    }

    public List<Option> getPriorityOptions() {
        return priorityOptions;
    }

    public Option getSelectedStatus() {
        return selectedStatus;
    }

    @NotifyChange("filteredTickets") // Tambahkan notifikasi saat setter dipanggil
    public void setSelectedStatus(Option selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

    public Option getSelectedPriority() {
        return selectedPriority;
    }

    @NotifyChange("filteredTickets") // Tambahkan notifikasi saat setter dipanggil
    public void setSelectedPriority(Option selectedPriority) {
        this.selectedPriority = selectedPriority;
    }


    @Command
    @NotifyChange({"filteredTickets", "selectedStatus", "selectedPriority"}) // Notifikasi semua yang berubah
    public void resetFilter() {
        selectedStatus = statusOptions.get(0);
        selectedPriority = priorityOptions.get(0);
    }

    @Command
    public void viewTicket(@BindingParam("id") Long id) {
        Executions.sendRedirect("ticket_detail.zul?id=" + id);
    }

    public String getStatusLabel(String status) {
        return StatusLabel.fromStatus(Status.valueOf(status)).getLabel();
    }

    public String getPriorityLabel(String priority) {
        return PriorityLabel.fromPriority(Priority.valueOf(priority)).getLabel();
    }

    public String getAssignedToName(User assignedTo) {
        if (assignedTo == null) return "";
        return assignedTo.getName();  // adjust type casting if needed
    }

}