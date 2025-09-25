package latihan.entity;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private StatusEntity status;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    private PriorityEntity priority;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id", foreignKey = @ForeignKey(name = "fk_ticket_user", foreignKeyDefinition = "FOREIGN KEY (assigned_to_id) REFERENCES users(id) ON DELETE CASCADE"))
    private User assignedTo;

    @Column(name = "requester")
    private String requester;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime deletedDate;

    public Ticket(Long id, String title, String description, StatusEntity status,
                  PriorityEntity priority, User assignedTo, String requester, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignedTo = assignedTo;
        this.requester = requester;
        this.createdDate = createdDate;
    }

    public Ticket() {

    }

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public StatusEntity getStatus() { return status; }
    public void setStatus(StatusEntity status) { this.status = status; }

    public PriorityEntity getPriority() { return priority; }
    public void setPriority(PriorityEntity priority) { this.priority = priority; }

    public User getAssignedTo() { return assignedTo; }
    public void setAssignedTo(User assignedTo) { this.assignedTo = assignedTo; }

    public String getRequester() { return requester; }
    public void setRequester(String requester) { this.requester = requester; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public LocalDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }
}
