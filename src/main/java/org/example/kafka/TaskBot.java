package org.example.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TaskBot extends TelegramLongPollingBot {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public TaskBot(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        if (messageText.equals("/start")) {
            sendMessage(chatId, "Привет! Я помогу тебе с твоими задачами.");
            // Здесь бот отправляет информацию в Kafka
            kafkaTemplate.send("task-topic", "User " + chatId + " started task.");
        } else if (messageText.equals("/complete")) {
            sendMessage(chatId, "Задача завершена!");
            // Отправка данных в Kafka
            kafkaTemplate.send("task-topic", "User " + chatId + " completed task.");
        }
    }

    private void sendMessage(Long chatId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "YourBotUsername";
    }

    @Override
    public String getBotToken() {
        return "YOUR_BOT_TOKEN";
    }
}

