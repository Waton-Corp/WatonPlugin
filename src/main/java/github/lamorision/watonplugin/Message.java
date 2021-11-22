package github.lamorision.watonplugin;

public class Message {
    String sender, content;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String format(String format) {
        return format
                .replace("%sender%", sender)
                .replace("%content%", content);
    }
}
