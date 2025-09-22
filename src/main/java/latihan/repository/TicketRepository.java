package latihan.repository;

import latihan.entity.Ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketRepository {
    private static TicketRepository instance;
    private final List<Ticket> tickets = new ArrayList<>();
    private Long sequence = 1L;

    public TicketRepository(){
        tickets.add(new Ticket(sequence++,"Email not working", "Cannot send or receive emails", "Open", "High", "Admin1", "UserA", LocalDateTime.now()));
        tickets.add(new Ticket(sequence++, "VPN issue", "Cannot connect to VPN", "In Progress", "Medium", "Admin2", "UserB", LocalDateTime.now()));
    }

    public static TicketRepository getInstance() {
        if (instance == null) {
            instance = new TicketRepository();
        }
        return instance;
    }

    public List<Ticket> findAll() {
        return tickets;
    }

    public Optional<Ticket> findById(Long id) {
        return tickets.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public Ticket save(Ticket ticket) {
        if (ticket.getId() == null) {
            ticket.setId(sequence++);
            tickets.add(ticket);
            System.out.println(tickets);
        } else {
            tickets.removeIf(t -> t.getId().equals(ticket.getId()));
            tickets.add(ticket);
        }
        return ticket;
    }

    public void delete(Long id) {
        tickets.removeIf(t -> t.getId().equals(id));
    }
}
