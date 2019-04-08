package bot.processing;

import bot.Bot;
import dbService.dao.DAOContext;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class MainMenuProcess extends Process{
    public MainMenuProcess(DAOContext daoContext, String chatId) {
        this.daoContext = daoContext;
        this.chatId = chatId;

        Bot.getInstance().sendMessage(chatId, "", createButtons());
    }

    public static ReplyKeyboardMarkup createButtons() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("Создать"));
        keyboardFirstRow.add(new KeyboardButton("Запросить"));
        keyboardFirstRow.add(new KeyboardButton("Мои документы"));
        keyboardFirstRow.add(new KeyboardButton("Помощь"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }

    @Override
    public void executeCurState(Update update) {
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
