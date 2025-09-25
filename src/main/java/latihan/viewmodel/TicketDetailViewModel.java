package latihan.viewmodel;

import latihan.entity.*;
import latihan.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Messagebox;

import java.time.format.DateTimeFormatter;

@Component
public class TicketDetailViewModel {

    private TicketService service;
    private Ticket ticket;

    @Init
    public void init(@QueryParam("id") String idParam) {
        service = (TicketService) SpringUtil.getBean("ticketService");
        if (idParam != null && !idParam.isEmpty()) {
            Long id = Long.parseLong(idParam);
            ticket = service.getTicket(id);
        }
    }

    /**
     * Command ini dipanggil saat tombol "Change Status" diklik.
     * Ini akan menampilkan popup konfirmasi.
     */
    @Command
    public void changeStatus() {
        StatusEntity nextStatus = service.getNextStatus(ticket.getStatus());
        if (nextStatus == null) {
            return;
        }

        String currentStatusLabel = ticket.getStatus().getLabel();
        String nextStatusLabel = nextStatus.getLabel();
        String message = "Are you sure you want to change status from '" + currentStatusLabel + "' to '" + nextStatusLabel + "'?";

        Messagebox.show(message, "Confirm Status Change", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, event -> {
            if (Messagebox.ON_OK.equals(event.getName())) {
                this.ticket = service.updateTicketStatus(ticket.getId(), nextStatus);

                // Beri tahu ZK untuk me-refresh semua properti di halaman
                BindUtils.postNotifyChange(null, null, this, "*");
            }
        });
    }

    // Command untuk edit dan delete tetap sama
    @Command
    public void editTicket() {
        if (ticket != null) {
            Executions.sendRedirect("ticket_form.zul?id=" + ticket.getId());
        }
    }

    @Command
    public void deleteTicket() {
        if (ticket != null) {
            String message = "Are you sure you want to delete ticket #" + ticket.getId() + " (" + ticket.getTitle() + ")?";
            Messagebox.show(message, "Confirm Deletion", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                     service.deleteTicket(ticket);
                    Executions.sendRedirect("tickets.zul");
                }
            });
        }
    }

    // --- GETTERS (Wajib untuk Data Binding di ZUL) ---

    public Ticket getTicket() {
        return ticket;
    }

    /**
     * Getter ini akan mengontrol apakah tombol "Change Status" bisa diklik atau tidak.
     * @return true jika status adalah CLOSED, false jika sebaliknya.
     */
    public boolean isChangeStatusDisabled() {
        return ticket == null || ticket.getStatus().getLabel().equals("CLOSED");
    }


    public String getFormattedCreatedDate() {
        if (ticket == null || ticket.getCreatedDate() == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        return ticket.getCreatedDate().format(formatter);
    }


    /**
     * Getter "computed property" untuk memformat tanggal pembaruan tiket.
     * Akan menampilkan "N/A" jika tiket belum pernah diperbarui.
     */
    public String getFormattedUpdatedDate() {
        if (ticket == null || ticket.getUpdatedDate() == null) {
            return "N/A"; // Tampilkan ini jika belum pernah ada update
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        return ticket.getUpdatedDate().format(formatter);
    }

    public String getAssignedToName(User assignedTo) {
        if (assignedTo == null) return "";
        return assignedTo.getName();  // adjust type casting if needed
    }
}