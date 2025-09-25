package latihan.viewmodel;

import latihan.entity.*;
import latihan.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.spring.SpringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketListViewModel {

    private TicketService service;
    private List<Ticket> tickets;

    // --- Perubahan Tipe Data ---
    private List<Option> statusOptions;
    private List<Option> priorityOptions;

    // Pilihan "All" sekarang direpresentasikan dengan nilai null
    private Option selectedStatus;
    private Option selectedPriority;

    @Init
    public void init() {
        service = (TicketService) SpringUtil.getBean("ticketService");

        tickets = service.getAllTickets();

        loadStatusOptions();
        loadPriorityOptions();

        selectedStatus = statusOptions.get(0);
        selectedPriority = priorityOptions.get(0);
    }

    private void loadStatusOptions() {
        statusOptions = new ArrayList<>();
        statusOptions.add(new TicketListViewModel.Option(0L, "All"));
        List<StatusEntity> statusEntityList = service.getAllStatus();
        for (StatusEntity s : statusEntityList) {
            statusOptions.add(new TicketListViewModel.Option(s.getId(), s.getLabel()));
        }
    }

    private void loadPriorityOptions() {
        priorityOptions = new ArrayList<>();
        priorityOptions.add(new TicketListViewModel.Option(0L, "All"));
        List<PriorityEntity> priorityEntityList = service.getAllPriority();
        for (PriorityEntity p : priorityEntityList) {
            priorityOptions.add(new TicketListViewModel.Option(p.getId(), p.getLabel()));
        }
    }

    public static class Option {
        private final Long value;
        private final String label;

        public Option(Long value, String label) {
            this.value = value;
            this.label = label;
        }

        public Long getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }

    public List<Ticket> getFilteredTickets() {
        return tickets.stream()
                .filter(t -> selectedStatus.getValue().equals(0L)
                        || t.getStatus().getId().equals(selectedStatus.getValue()))
                .filter(t -> selectedPriority.getValue().equals(0L)
                        || t.getPriority().getId().equals(selectedPriority.getValue()))
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

}