package org.example.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class LessonController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    // Храним уроки в мапе
    private final Map<String, String> lessons = new ConcurrentHashMap<>();

    public LessonController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Создание урока и отправка сообщения в Kafka
    @PostMapping("/createLesson")
    public Optional<String> createLesson(@RequestBody LessonRequest lessonRequest) {
        // Генерация уникального ID для урока
        String lessonId = "lesson-" + System.currentTimeMillis();
        // Сохраняем название урока в мапе
        lessons.put(lessonId, lessonRequest.getTitle());

        // Формируем сообщение для отправки в Kafka
        String lessonMessage = String.format("{\"lessonId\": \"%s\", \"title\": \"%s\"}",
                lessonId, lessonRequest.getTitle());

        // Отправляем сообщение в Kafka (топик "lesson-created")
        kafkaTemplate.send("lesson-created", lessonId, lessonMessage);

        return Optional.of("Урок с ID " + lessonId + " успешно создан и отправлен в Kafka.");
    }

    // Получение всех уроков (опционально, для просмотра)
    @GetMapping("/getLessons")
    public Map<String, String> getLessons() {
        return lessons;
    }
}
