package latihan.entity;

public enum PriorityLabel {
    LOW(Priority.LOW, "Low", "badge badge-success"),
    MEDIUM(Priority.MEDIUM, "Medium", "badge badge-warning"),
    HIGH(Priority.HIGH, "High", "badge badge-danger");

    private final Priority priority;
    private final String label;
    private final String cssClass;

    PriorityLabel(Priority priority, String label, String cssClass) {
        this.priority = priority;
        this.label = label;
        this.cssClass = cssClass;
    }

    public String getLabel() {
        return label;
    }

    public String getCssClass() {
        return cssClass;
    }

    public Priority getPriority() {
        return priority;
    }

    public static PriorityLabel fromPriority(Priority priority) {
        for (PriorityLabel pl : values()) {
            if (pl.priority == priority) {
                return pl;
            }
        }
        return null;
    }
}
