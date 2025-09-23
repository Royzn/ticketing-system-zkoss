package latihan.controller;

import latihan.entity.StatusLabel;
import latihan.entity.Ticket;
import latihan.service.TicketService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

public class TicketDetailController extends SelectorComposer<Component> {
    private TicketService service = new TicketService();
    private Ticket ticket;

    @Wire private Label lblId;
    @Wire private Label lblTitle;
    @Wire private Label lblDescription;
    @Wire private Label lblStatus;
    @Wire private Label lblPriority;
    @Wire private Label lblAssignedTo;
    @Wire private Label lblRequester;
    @Wire private Label lblCreatedDate;
    @Wire private org.zkoss.zul.Button btnEdit;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        String idParam = Executions.getCurrent().getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            Long id = Long.parseLong(idParam);
            ticket = service.getTicket(id);

            if (ticket != null) {
                lblId.setValue(ticket.getId().toString());
                lblTitle.setValue(ticket.getTitle());
                lblDescription.setValue(ticket.getDescription());
                lblStatus.setValue(StatusLabel.fromStatus(ticket.getStatus()).getLabel());
                lblPriority.setValue(ticket.getPriority().toString());
                lblAssignedTo.setValue(ticket.getAssignedTo());
                lblRequester.setValue(ticket.getRequester());
                lblCreatedDate.setValue(ticket.getCreatedDate().toString());

                // 🔑 pasang event listener di tombol Edit
                btnEdit.addEventListener("onClick", e -> {
                    Executions.sendRedirect("ticket_form.zul?id=" + ticket.getId());
                });
            }
        }
    }
}
