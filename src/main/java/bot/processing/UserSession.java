package bot.processing;

import bot.Bot;
import dbService.dao.DAOContext;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UserSession {
    private String chatId;
    private DAOContext daoContext;
    private Process curProcess;
    private Process nextProcess;
    private boolean loggedIn;

    public UserSession(DAOContext daoContext, String chatId) {
        this.daoContext = daoContext;
        this.chatId = chatId;
        loggedIn = false;
        nextProcess = null;
        curProcess = new AuthorizationProcess(daoContext, chatId);
    }

    public void setNextProcess(Process nextProcess) {
        this.nextProcess = nextProcess;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void executeActiveProcess(Update update) {
        mainMenuProcessing(update);
        if(nextProcess != null) {
            curProcess = nextProcess;
            nextProcess = null;
        }
        if(curProcess.getClass().equals(MainMenuProcess.class))
            return;
        curProcess.executeCurState(update);

    }

    public void mainMenuProcessing(Update update) {
        if(!loggedIn)
            return;
        String data = null;
        if(update.hasMessage() && update.getMessage().getText() != null) {
            data = update.getMessage().getText();
        }
        else
            return;
        if(data.equals("Создать")) {
            Bot.getInstance()
                    .getUserSession(chatId)
                    .setNextProcess(new DocumentCreationProcess(daoContext, chatId));
        }
        else if(data.equals("Запросить")) {
            Bot.getInstance().sendMessage(chatId, "Я пока не умею запрашивать документы :(\n" +
                    "Подождите следующего релиза");
        }
        else if(data.equals("Мои документы")) {
            Bot.getInstance().sendMessage(chatId, "Я пока не умею показывать документы :(\n" +
                    "Подождите следующего релиза");
        }
        else if(data.equals("Помощь")) {
            Bot.getInstance().sendMessage(chatId, "*Привет, я бот для облегчения работы с документами.*\n" +
                    "_Перечень моих команд:_\n" +
                    "_Создать:_ здесь можно заполнить новый документ\n" +
                    "_Запросить:_ здесь можно запросить документ у другого сотрудника\n" +
                    "_Мои документы:_ вывод всех когда-либо отправленных документов, запросов и их текущего состояния\n");
        }
    }
}
