package latihan.repository;

import latihan.entity.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    List<Ticket> findAll();
    Optional<Ticket> findById(Long id);
    Ticket save(Ticket ticket);
    void delete(Long id);
}
