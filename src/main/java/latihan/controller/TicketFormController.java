package latihan.controller;

import latihan.entity.Priority;
import latihan.entity.Status;
import latihan.entity.StatusLabel;
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
                titleBox.setValue(editingTicket.getTitle());
                descBox.setValue(editingTicket.getDescription());
                priorityBox.setValue(editingTicket.getPriority().toString());
                assignedToBox.setValue(editingTicket.getAssignedTo());
                statusBox.setValue(editingTicket.getStatus().toString());
                requesterBox.setValue(editingTicket.getRequester());
            }
        }

        if(statusBox !=null) {
            for (Status s : Status.values()) {
                StatusLabel statusLabel = StatusLabel.fromStatus(s);
                if (statusLabel != null) {
                    Comboitem item = new Comboitem();
                    item.setLabel(statusLabel.getLabel());
                    item.setValue(s.name()); // the enum value
                    item.setParent(statusBox);
                }
            }
        }

        if(priorityBox!=null){
            for (Priority p : Priority.values()) {
                Comboitem item = new Comboitem();
                item.setLabel(p.name());
                item.setValue(p);
                item.setParent(priorityBox);
            }
        }

        // Action tombol save
        saveBtn.addEventListener("onClick", e -> {
            updateTicket();
        });
    }

    public void updateTicket(){
        Comboitem selectedStatus = statusBox.getSelectedItem();
        String status;
        if (selectedStatus != null) {
            status = (String) selectedStatus.getValue();
        } else {
            status = "";
        }

        if (editingTicket == null) {
            service.createTicket(
                    titleBox.getValue(),
                    descBox.getValue(),
                    status,
                    priorityBox.getValue(),
                    assignedToBox.getValue(),
                    requesterBox.getValue()
            );
        } else {
            service.updateTicket(editingTicket, titleBox.getValue(), descBox.getValue(), statusBox.getValue(), priorityBox.getValue(), assignedToBox.getValue(), requesterBox.getValue()); // 🔑 pastikan ada di service
        }

        Executions.sendRedirect("tickets.zul");
    }
}
