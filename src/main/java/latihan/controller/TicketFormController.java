package latihan.controller;

import latihan.entity.Ticket;
import latihan.service.TicketService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

public class TicketFormController extends SelectorComposer<Component> {
    private TicketService service = new TicketService();
    private Ticket editingTicket; // tiket yang sedang diedit

    @Wire private Textbox titleBox;
    @Wire private Textbox descBox;
    @Wire private Combobox priorityBox;
    @Wire private Textbox assignedToBox;
    @Wire private Combobox statusBox;
    @Wire private Textbox requesterBox;
    @Wire private Button saveBtn;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        String idParam = Executions.getCurrent().getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            Long id = Long.parseLong(idParam);
            editingTicket = service.getTicket(id);

            if (editingTicket != null) {
                // Prefill form dengan data tiket
                titleBox.setValue(editingTicket.getTitle());
                descBox.setValue(editingTicket.getDescription());
                priorityBox.setValue(editingTicket.getPriority());
                assignedToBox.setValue(editingTicket.getAssignedTo());
                statusBox.setValue(editingTicket.getStatus());
                requesterBox.setValue(editingTicket.getRequester());
            }
        }

        // Action tombol save
        saveBtn.addEventListener("onClick", e -> {
            if (editingTicket == null) {
                // Mode Create
                service.createTicket(
                        titleBox.getValue(),
                        descBox.getValue(),
                        statusBox.getValue(),
                        priorityBox.getValue(),
                        assignedToBox.getValue(),
                        requesterBox.getValue()
                );
            } else {
                // Mode Update
                editingTicket.setTitle(titleBox.getValue());
                editingTicket.setDescription(descBox.getValue());
                editingTicket.setStatus(statusBox.getValue());
                editingTicket.setPriority(priorityBox.getValue());
                editingTicket.setAssignedTo(assignedToBox.getValue());
                editingTicket.setRequester(requesterBox.getValue());

                service.updateTicket(editingTicket); // 🔑 pastikan ada di service
            }

            Executions.sendRedirect("tickets.zul");
        });
    }
}
