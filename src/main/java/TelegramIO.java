import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TelegramIO extends TelegramLongPollingBot implements UserInterface {
    BlockingQueue<Update> queueOfRequests = new LinkedBlockingQueue<Update>();
    Message currentMessage = new Message();

    public boolean printMessage(String text) {
        Update currentUpdate = new Update();
        SendMessage messageForSending = new SendMessage();

        if (currentMessage.hasText()) {
            messageForSending.enableMarkdown(true);
            messageForSending.setChatId(currentMessage.getChatId().toString());
            messageForSending.setText(text);
            currentMessage = new Message();
        }
        if (messageForSending.getText() != null) {
            try {
                setButtons(messageForSending);
                execute(messageForSending);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public String getInput() {
        Update currentUpdate = new Update();

        try {
            currentUpdate = queueOfRequests.poll(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (currentUpdate != null && currentUpdate.hasMessage()) {
            currentMessage = currentUpdate.getMessage();
        }

        return currentMessage.hasText() ? currentMessage.getText() : new String();
    }

    ;

    private void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/authors"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    public void onUpdateReceived(Update update) {
        queueOfRequests.offer(update);
    }

    public String getBotUsername() {
        return "LingvoMastersBot";
    }

    public String getBotToken() {
        return "980620653:AAFCTuWdj69yeXio3u30MyNz8h1jC1_Zb8Q";
    }
}
