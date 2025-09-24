package latihan.service;

import latihan.entity.Status;
import latihan.entity.Ticket;
import latihan.entity.User;
import latihan.repository.TicketRepository;
import latihan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    UserRepository userRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicket(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public Ticket createTicket(String title, String description, String priority, Long assignedTo, String requester) {
        User user = userRepository.findById(assignedTo).orElse(null);
        if (user == null) {
            return null;
        }

        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setPriority(priority); // assumed string value like "HIGH"
        ticket.setStatus(Status.OPEN.name()); // set default status
        ticket.setAssignedTo(user);
        ticket.setRequester(requester);
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setUpdatedDate(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(Ticket ticket, String title, String description, String status,
                               String priority, Long assignedTo, String requester) {

        User user = userRepository.findById(assignedTo).orElse(null);
        if (user == null) {
            return null;
        }

        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setStatus(status); // already validated as Status string
        ticket.setPriority(priority);
        ticket.setAssignedTo(user);
        ticket.setRequester(requester);
        ticket.setUpdatedDate(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicketStatus(Long ticketId, Status newStatus) {
        Ticket ticket = getTicket(ticketId);
        if (ticket == null) {
            return null;
        }

        ticket.setStatus(newStatus.name());
        ticket.setUpdatedDate(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Ticket ticket) {
        ticketRepository.delete(ticket);
    }
}
