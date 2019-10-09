import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.util.Stack;

public class TelegramIO extends TelegramLongPollingBot implements UserInterface {
    Stack<Update> StackOfRequests = new Stack<Update>();

    public boolean printMessage(String text) {
        Update currentUpdate = new Update();
        Message message = new Message();
        SendMessage messageForSending = new SendMessage();

        if (!StackOfRequests.isEmpty()) {
            currentUpdate = StackOfRequests.pop();
            message = currentUpdate.getMessage();
        }

        if (message.hasText()) {
            messageForSending.enableMarkdown(true);
            messageForSending.setChatId(message.getChatId().toString());
            messageForSending.setText(text);
        }
        if (messageForSending.getText() != null) {
            try {
                execute(messageForSending);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public String getInput() {
        Update currentUpdate = new Update();
        Message message = new Message();

        if (!StackOfRequests.isEmpty()) {
            currentUpdate = StackOfRequests.peek();
            message = currentUpdate.getMessage();
        }

        return message.hasText() ? message.getText() : null;
    }

    ;

    public boolean getClickOnButton(Button button) {
        return true;
    }

    ;

    public void onUpdateReceived(Update update) {
        StackOfRequests.push(update);
    }

    public String getBotUsername() {
        return "LingvoMastersBot";
    }

    public String getBotToken() {
        return "980620653:AAFCTuWdj69yeXio3u30MyNz8h1jC1_Zb8Q";
    }
}
