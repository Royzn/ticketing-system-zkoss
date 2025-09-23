package latihan.entity;

public enum StatusLabel {
    OPEN(Status.OPEN, "Open", "badge badge-success"),
    IN_PROGRESS(Status.IN_PROGRESS, "In Progress", "badge badge-secondary"),
    CLOSED(Status.CLOSED, "Closed", "badge badge-danger");

    private final Status status;
    private final String label;
    private final String cssClass;

    StatusLabel(Status status, String label, String cssClass) {
        this.status = status;
        this.label = label;
        this.cssClass = cssClass;
    }

    public String getLabel() {
        return label;
    }

    public String getCssClass() {
        return cssClass;
    }

    public Status getStatus() {
        return status;
    }

    public static StatusLabel fromStatus(Status status) {
        for (StatusLabel sl : values()) {
            if (sl.status == status) {
                return sl;
            }
        }
        return null; // Or throw, or return a default
    }
}
