package org.example.kafka;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ReminderService {

    private final KafkaProducer kafkaProducer;

    private final List<String> reminders = List.of(
            "Выпить воды",
            "Растяжка 7 минут"
    );

    public ReminderService(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public String generateAndSendReminder() {
        String reminder = reminders.get(new Random().nextInt(reminders.size()));
        kafkaProducer.sendReminder(reminder);
        return reminder;
    }
}
