package latihan.service;

import latihan.entity.Ticket;
import latihan.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.List;

public class TicketService {
    private final TicketRepository repository = new TicketRepository();

    public List<Ticket> getAllTickets() {
        return repository.findAll();
    }

    public Ticket getTicket(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Ticket createTicket(String title, String description, String status,
                               String priority, String assignedTo, String requester) {
        Ticket ticket = new Ticket(null, title, description, status, priority, assignedTo, requester, LocalDateTime.now());
        return repository.save(ticket);
    }

    public Ticket updateTicket(Ticket ticket) {
        return repository.save(ticket);
    }

    public void deleteTicket(Long id) {
        repository.delete(id);
    }
}
