package sample.amqp.event;

import javax.persistence.*;

@Entity
@Table(name = "event_table")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "event_type")
public abstract class AbstractEvent {
    private String uuid;

    // for serialization
    public AbstractEvent() { }

    public AbstractEvent(String uuid) {
        this.uuid = uuid;
    }

    @Id
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
