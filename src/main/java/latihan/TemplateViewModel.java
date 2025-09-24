package latihan;

import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;

public class TemplateViewModel {

    private boolean isDashboard = false;
    private boolean isTickets = false;
    private boolean isForm = false;
    private boolean isDetail = false;
    private boolean isUsers = false;

    @Init
    public void init() {
        String path = Executions.getCurrent().getDesktop().getRequestPath().toLowerCase();

        if (path.contains("dashboard")) {
            isDashboard = true;
        } else if (path.contains("tickets") && !path.contains("form")) {
            isTickets = true;
        } else if (path.contains("ticket_form")) {
            isForm = true;
        } else if (path.contains("ticket_detail")) {
            isDetail = true;
        } else if (path.contains("users")) {
            isUsers = true;
        }
    }

    // Each getter returns "menu-link" or "menu-link active"
    public String getDashboardClass() {
        return isDashboard ? "menu-link active" : "menu-link";
    }

    public String getTicketsClass() {
        return isTickets ? "menu-link active" : "menu-link";
    }

    public String getFormClass() {
        return isForm ? "menu-link active" : "menu-link";
    }

    public String getDetailClass() {
        return isDetail ? "menu-link active" : "menu-link";
    }

    public String getUsersClass() {
        return isUsers ? "menu-link active" : "menu-link";
    }

    // Tambahan: Command untuk logout
    @Command
    public void logout() {
        // hapus session user
        Executions.getCurrent().getSession().invalidate();

        // redirect ke halaman login
        Executions.sendRedirect("login.zul");
    }

    public boolean isAdmin() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

}
