public class BotRunException extends Exception {
    public BotRunException(String message) {
        super(message);
    }

    public BotRunException(String message, Throwable cause) {
        super(message, cause);
    }
}