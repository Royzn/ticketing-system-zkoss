package latihan.dto;

public class TicketDto {
    private String title;
    private String description;
    private String status;
    private String priority;
    private String assignedTo;
    private String requester;

    // Getter & Setter
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public String getRequester() { return requester; }
    public void setRequester(String requester) { this.requester = requester; }
}
