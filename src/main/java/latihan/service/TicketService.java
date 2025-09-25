package latihan.service;

import latihan.dto.CountDto;
import latihan.entity.*;
import latihan.exception.UsernameAlreadyExistsException;
import latihan.repository.PriorityRepository;
import latihan.repository.StatusRepository;
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

    @Autowired
    PriorityRepository priorityRepository;

    @Autowired
    StatusRepository statusRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicket(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public Ticket createTicket(String title, String description, Long priorityId, Long assignedTo, String requester) {
        User user = userRepository.findById(assignedTo).orElse(null);
        if (user == null) {
            return null;
        }

        PriorityEntity priority = priorityRepository.findById(priorityId).orElse(null);
        if(priority == null) throw new UsernameAlreadyExistsException("Priority Invalid");

        StatusEntity status = statusRepository.findByName("OPEN").orElse(null);
        if(status == null) throw new UsernameAlreadyExistsException("Status Invalid");

        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setPriority(priority); // assumed string value like "HIGH"
        ticket.setStatus(status); // set default status
        ticket.setAssignedTo(user);
        ticket.setRequester(requester);
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setUpdatedDate(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(Ticket ticket, String title, String description, Long statusId,
                               Long priorityId, Long assignedTo, String requester) {

        User user = userRepository.findById(assignedTo).orElse(null);
        if (user == null) {
            return null;
        }

        PriorityEntity priority = priorityRepository.findById(priorityId).orElse(null);
        if(priority == null) throw new UsernameAlreadyExistsException("Priority Invalid");

        StatusEntity status = statusRepository.findById(statusId).orElse(null);
        if(status == null) throw new UsernameAlreadyExistsException("Status Invalid");

        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setStatus(status);
        ticket.setPriority(priority);
        ticket.setAssignedTo(user);
        ticket.setRequester(requester);
        ticket.setUpdatedDate(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicketStatus(Long ticketId, StatusEntity newStatus) {
        Ticket ticket = getTicket(ticketId);
        if (ticket == null) {
            return null;
        }

        ticket.setStatus(newStatus);
        ticket.setUpdatedDate(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Ticket ticket) {
        ticketRepository.delete(ticket);
    }

    public List<PriorityEntity> getAllPriority(){
        return priorityRepository.findAll();
    }

    public List<StatusEntity> getAllStatus(){
        return statusRepository.findAll();
    }

    public StatusEntity getNextStatus(StatusEntity currentStatus) {
        if (currentStatus == null || currentStatus.getName() == null) {
            return null;
        }

        String nextStatusName = null;

        switch (currentStatus.getName()) {
            case "OPEN":
                nextStatusName = "IN_PROGRESS";
                break;
            case "IN_PROGRESS":
                nextStatusName = "CLOSED";
                break;
            default:
                return null; // No further status
        }

        return statusRepository.findByName(nextStatusName).orElse(null);
    }


    public CountDto getDashboardStats(){
        CountDto vm = new CountDto();

        vm.setOpenCount(ticketRepository.countByStatus_Name("OPEN"));
        vm.setInProgressCount(ticketRepository.countByStatus_Name("IN_PROGRESS"));
        vm.setClosedCount(ticketRepository.countByStatus_Name("CLOSED"));

        vm.setLowCount(ticketRepository.countByPriority_Name("LOW"));
        vm.setMediumCount(ticketRepository.countByPriority_Name("MEDIUM"));
        vm.setHighCount(ticketRepository.countByPriority_Name("HIGH"));

        return vm;
    }

    public List<Ticket> getFiveMostRecentTickets() {
        return ticketRepository.findTop5ByOrderByCreatedDateDesc();
    }
}
