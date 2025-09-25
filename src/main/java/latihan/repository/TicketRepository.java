package latihan.repository;

import latihan.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    long countByStatus_Name(String statusName);
    long countByPriority_Name(String priorityName);

    List<Ticket> findTop5ByOrderByCreatedDateDesc();
}
