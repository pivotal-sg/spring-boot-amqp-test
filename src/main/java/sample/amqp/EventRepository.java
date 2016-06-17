package sample.amqp;

import org.springframework.data.repository.Repository;

public interface EventRepository extends Repository<AbstractEvent, String> {
    AbstractEvent save(AbstractEvent event);
    AbstractEvent findOne(String uuid);
}
