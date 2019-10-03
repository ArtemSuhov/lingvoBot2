import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Translater {

    public void printRussianTranslation(String text) {
        var apiKey = "trnsl.1.1.20190918T173040Z.b3cf9c009a52bafd.a789edafc22db606c8ecabd860d0f8207a86c8d5";
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);

        StringBuilder request = HTTPRequests.getRequest("https://translate.yandex.net/api/v1.5/tr.json/translate?key="
                + apiKey
                + "&text="
                + encodedText
                + "&lang=en-ru", "GET");

        System.out.println(parseTheTranslate(request.toString()));
    }

    private static String parseTheTranslate(String request) {
        var lastPart = request.split(",")[2].split(":")[1];
        return lastPart.substring(2, lastPart.length() - 4);
    }

}
