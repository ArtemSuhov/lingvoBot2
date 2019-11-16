public class BotMessage {
    String textOfMessage;
    String chatId;

    BotMessage(String text, String id){
        this.textOfMessage = text;
        this.chatId = id;
    }
}
