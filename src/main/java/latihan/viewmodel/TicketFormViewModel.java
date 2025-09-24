package latihan.viewmodel; // <-- DIUBAH DI SINI

import latihan.dto.UserListDto;
import latihan.entity.*;
import latihan.service.TicketService;
import latihan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.SpringUtil;

import java.util.Arrays;
import java.util.List;

@Component
public class TicketFormViewModel {


    private TicketService service;

    private UserService uservice;
    private Ticket currentTicket;

    private List<Priority> priorityOptions;
    private List<UserListDto> agentOptions;
    private UserListDto selectedAgent;
    private Priority selectedPriority;
    private Long id;
    private String errorMessage;

    @Init
    public void init(@QueryParam("id") String idParam) {
        service = (TicketService) SpringUtil.getBean("ticketService");
        uservice = (UserService) SpringUtil.getBean("userService");

        priorityOptions = Arrays.asList(Priority.values());
        loadAgentList();
        if (idParam != null && !idParam.isEmpty()) {
            this.id = Long.parseLong(idParam);
            currentTicket = service.getTicket(this.id);
            if(currentTicket != null){
                for(UserListDto a: agentOptions){
                    if(a.getId().equals(currentTicket.getAssignedTo().getId())){
                        this.selectedAgent = a;
                    }
                }
                for(Priority p: priorityOptions){
                    if(p.name().equals(currentTicket.getPriority())){
                        this.selectedPriority = p;
                    }
                }
            }
        } else {
            currentTicket = new Ticket();
        }
    }

    // --- VALIDASI ---
    private boolean validateForm() {
        if (currentTicket.getTitle() == null || currentTicket.getTitle().trim().isEmpty()) {
            errorMessage = "Title wajib diisi.";
            return false;
        }
        if (currentTicket.getDescription() == null || currentTicket.getDescription().trim().isEmpty()) {
            errorMessage = "Description wajib diisi.";
            return false;
        }
        if (selectedPriority == null) {
            errorMessage = "Priority wajib dipilih.";
            return false;
        }
        if (selectedAgent == null) {
            errorMessage = "Agent wajib dipilih.";
            return false;
        }
        return true;
    }

    public String getStatusLabelText(Status status) {
        if (status == null) return "";
        StatusLabel sl = StatusLabel.fromStatus(status);
        return sl != null ? sl.getLabel() : status.name();
    }

    public String getPriorityLabelText(Priority priority){
        if(priority == null) return "";
        PriorityLabel pl = PriorityLabel.fromPriority(priority);
        return pl != null ? pl.getLabel() : priority.name();
    }

    public void loadAgentList(){
        agentOptions = uservice.getAgent();
    }

    @Command
    public void saveTicket() {
        if (!validateForm()) {
            // Bisa tampilkan notifikasi popup ke user
            Clients.showNotification(errorMessage, "error", null, "top_center", 3000);
            return;
        }
        if (currentTicket.getId() == null) {
            service.createTicket(
                    currentTicket.getTitle(),
                    currentTicket.getDescription(),
                    this.selectedPriority != null ? this.selectedPriority.toString() : null,
                    selectedAgent.getId(),
                    currentTicket.getRequester()
            );
        } else {
            service.updateTicket(
                    currentTicket,
                    currentTicket.getTitle(),
                    currentTicket.getDescription(),
                    currentTicket.getStatus() != null ? currentTicket.getStatus(): null,
                    this.selectedPriority != null ? this.selectedPriority.toString() : null,
                    selectedAgent.getId(),
                    currentTicket.getRequester()
            );
        }
        Executions.sendRedirect("tickets.zul");
    }

    // --- Getter dan Setter (Wajib untuk Data Binding) ---
    public Ticket getCurrentTicket() { return currentTicket; }
    public void setCurrentTicket(Ticket currentTicket) { this.currentTicket = currentTicket; }
    public List<Priority> getPriorityOptions() { return priorityOptions; }
    public List<UserListDto> getAgentOptions() {
        return agentOptions;
    }
    public UserListDto getSelectedAgent(){ return selectedAgent; }
    public void setSelectedAgent(UserListDto selectedAgent) {
        this.selectedAgent = selectedAgent;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Priority getSelectedPriority() {return selectedPriority;}
    public void setSelectedPriority(Priority priority){
        this.selectedPriority = priority;
    }
    public Long getId(){return id;}
}