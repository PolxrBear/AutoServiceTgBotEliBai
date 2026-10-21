public class Main {

    public static void main(String[] args) {
        try {
            BotInit.start();
        } catch (BotConfigException e) {
            System.err.println("❌ Ошибка конфигурации бота: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Критическая ошибка при запуске приложения: " + e.getMessage());
        }
    }
}
