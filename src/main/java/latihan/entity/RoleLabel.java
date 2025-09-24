package latihan.entity;

public enum RoleLabel {
    USER(Role.ROLE_USER, "User"),
    AGENT(Role.ROLE_AGENT, "Agent"),
    ADMIN(Role.ROLE_ADMIN, "Admin");

    private final Role role;
    private final String label;

    RoleLabel(Role role, String label) {
        this.role = role;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public Role getStatus() {
        return role;
    }

    public static RoleLabel fromStatus(Role role) {
        for (RoleLabel rl : values()) {
            if (rl.role == role) {
                return rl;
            }
        }
        return null; // Or throw, or return a default
    }
}
