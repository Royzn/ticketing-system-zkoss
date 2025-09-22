package latihan.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

public class ConfirmModalController extends SelectorComposer<Component> {
    @Wire
    private Window confirmWin;

    @Wire
    private Button btnYes, btnNo;

    @Wire
    private Label confirmMsg;

    private Runnable onConfirm;

    public void setConfirmMessage(String message) {
        confirmMsg.setValue(message);
    }

    public void setOnConfirm(Runnable onConfirm) {
        this.onConfirm = onConfirm;
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        // Store this controller instance as an attribute on the root window
        comp.setAttribute("controller", this);

        btnYes.addEventListener("onClick", event -> {
            if (onConfirm != null) {
                onConfirm.run();
            }
            confirmWin.detach();
        });

        btnNo.addEventListener("onClick", event -> {
            confirmWin.detach();
        });
    }
}
