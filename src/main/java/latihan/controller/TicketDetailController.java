package latihan.controller;

import latihan.entity.PriorityLabel;
import latihan.entity.StatusLabel;
import latihan.entity.Ticket;
import latihan.service.TicketService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;

import java.time.format.DateTimeFormatter;

public class TicketDetailController {

    private TicketService service = new TicketService();
    private Ticket ticket; // Objek untuk menampung data tiket yang akan ditampilkan

    /**
     * Metode @Init dieksekusi saat ViewModel dibuat.
     * @QueryParam("id") secara otomatis mengambil parameter 'id' dari URL.
     */
    @Init
    public void init(@QueryParam("id") String idParam) {
        if (idParam != null && !idParam.isEmpty()) {
            Long id = Long.parseLong(idParam);
            ticket = service.getTicket(id); // Muat data tiket dari service
        }
    }

    /**
     * Perintah ini dipanggil saat tombol 'Edit' diklik.
     */
    @Command
    public void editTicket() {
        if (ticket != null) {
            Executions.sendRedirect("ticket_form.zul?id=" + ticket.getId());
        }
    }

    /**
     * Perintah ini dipanggil saat tombol 'Delete' diklik.
     * Menambahkan konfirmasi sebelum menghapus adalah praktik yang baik.
     */
    @Command
    public void deleteTicket() {
        if (ticket != null) {
            String message = "Are you sure you want to delete ticket #" + ticket.getId() + " (" + ticket.getTitle() + ")?";

            Messagebox.show(message, "Confirm Deletion", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    // Logika penghapusan jika pengguna menekan OK
                    // service.deleteTicket(ticket.getId()); // Asumsi Anda punya metode ini di service
                    Executions.sendRedirect("tickets.zul"); // Redirect ke halaman daftar
                }
            });
        }
    }

    /**
     * Perintah ini dipanggil saat tombol 'Change Status' diklik.
     * Logika untuk menampilkan popup/modal perubahan status bisa ditambahkan di sini.
     */
    @Command
    public void changeStatus() {
        // Placeholder: Tampilkan notifikasi sederhana
        Messagebox.show("Status change functionality will be implemented here.", "Info", Messagebox.OK, Messagebox.INFORMATION);
    }


    // --- Getters (Wajib untuk Data Binding di ZUL) ---

    public Ticket getTicket() {
        return ticket;
    }

    /**
     * Getter "computed" untuk mendapatkan label status yang user-friendly.
     * ZUL akan memanggil ini melalui @load(vm.statusLabel).
     */
    public String getStatusLabel() {
        if (ticket == null || ticket.getStatus() == null) {
            return "N/A";
        }
        StatusLabel sl = StatusLabel.fromStatus(ticket.getStatus());
        return sl != null ? sl.getLabel() : ticket.getStatus().name();
    }

    /**
     * Getter "computed" untuk mendapatkan label prioritas yang user-friendly.
     * ZUL akan memanggil ini melalui @load(vm.priorityLabel).
     */
    public String getPriorityLabel() {
        if (ticket == null || ticket.getPriority() == null) {
            return "N/A";
        }
        PriorityLabel pl = PriorityLabel.fromPriority(ticket.getPriority());
        return pl != null ? pl.getLabel() : ticket.getPriority().name();
    }

    public String getFormattedCreatedDate() {
        if (ticket == null || ticket.getCreatedDate() == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        return ticket.getCreatedDate().format(formatter);
    }
}