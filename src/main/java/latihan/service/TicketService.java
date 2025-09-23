package latihan.service;

import latihan.entity.Priority;
import latihan.entity.Status;
import latihan.entity.Ticket;
import latihan.entity.User;
import latihan.repository.TicketRepository;
import latihan.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

public class TicketService {
    private final TicketRepository repository = TicketRepository.getInstance();
    private final UserRepository userRepository = UserRepository.getInstance();

    public List<Ticket> getAllTickets() {
        return repository.findAll();
    }

    public Ticket getTicket(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Ticket createTicket(String title, String description, String status,
                               String priority, Long assignedTo, String requester) {
        User u = userRepository.findById(assignedTo).orElse(null);
        if(u!= null){
            Ticket ticket = new Ticket(null, title, description, Status.valueOf(status), Priority.valueOf(priority), u, requester, LocalDateTime.now());
            return repository.save(ticket);
        }
        return null;
    }

    public Ticket updateTicket(Ticket ticket, String title, String description, String status,
                               String priority, Long assignedTo, String requester) {

        ticket.setUpdatedDate(LocalDateTime.now());
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setStatus(Status.valueOf(status));
        ticket.setPriority(Priority.valueOf(priority));

        User u = userRepository.findById(assignedTo).orElse(null);
        if(u!= null) ticket.setAssignedTo(u);
        ticket.setRequester(requester);
        return repository.save(ticket);
    }

    public Ticket updateTicketStatus(Long ticketId, Status newStatus) {
        Ticket ticketToUpdate = getTicket(ticketId);
        if (ticketToUpdate != null) {
            ticketToUpdate.setStatus(newStatus);
            ticketToUpdate.setUpdatedDate(LocalDateTime.now());
            repository.save(ticketToUpdate);
        }
        return ticketToUpdate; // Mengembalikan objek yang sudah diupdate
    }

    public void deleteTicket(Long id) {
        repository.delete(id);
    }


}
