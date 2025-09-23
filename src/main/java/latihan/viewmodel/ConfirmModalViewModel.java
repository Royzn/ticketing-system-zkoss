package latihan.viewmodel;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

public class ConfirmModalViewModel {

    private String confirmMsg;
    private Runnable onConfirm;

    @Wire
    private Window confirmWin;

    // Initialize with arguments passed from createComponents
    @Init
    public void init(@ExecutionArgParam("confirmMessage") String message,
                     @ExecutionArgParam("onConfirm") Runnable onConfirm) {
        this.confirmMsg = message;
        this.onConfirm = onConfirm;
    }

    // Getter for label binding
    public String getConfirmMsg() {
        return confirmMsg;
    }

    // Command for "Yes" button
    @Command
    public void yes(@ContextParam(ContextType.COMPONENT) Component comp) {
        if (onConfirm != null) {
            onConfirm.run();
        }
        // Close the modal window
        Window window = (Window) comp.getSpaceOwner();
        window.detach();
    }

    // Command for "No" button
    @Command
    public void no(@ContextParam(ContextType.COMPONENT) Component comp) {
        Window window = (Window) comp.getSpaceOwner();
        window.detach();
    }
}
