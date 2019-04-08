package bot;

import bot.processing.UserSession;
import dbService.dao.DAOContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Bot extends TelegramLongPollingBot {
    private final String BOTNAME = "CaseInDocHelperBot";
    private final String BOTTOKEN = "854269089:AAF-GvqdGb46vUlQMWl7Z8aOZ5cc9S_8vtc";

    private Map<String, UserSession> userSessions;
    private DAOContext daoContext;

    private static Bot bot;
    private Bot() {
        userSessions = new ConcurrentHashMap<>();
        daoContext = new DAOContext();
    }

    static {
        bot = new Bot();
    }

    public static Bot getInstance() {
        return bot;
    }

    public UserSession getUserSession(String chatId) {
        return userSessions.getOrDefault(chatId, null);
    }

    public void sendMessage(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String chatId, String text, InlineKeyboardMarkup keyboard) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyboard);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String chatId, String text, ReplyKeyboardMarkup keyboard) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyboard);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        String chatId = null;
        if(update.hasCallbackQuery())
            chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        else if(update.hasMessage() && update.getMessage().getText() != null)
            chatId = String.valueOf(update.getMessage().getChatId());
        else
            return;
        if(userSessions.containsKey(chatId)) {
            userSessions.get(chatId).executeActiveProcess(update);
        }
        else {
            userSessions.put(chatId, new UserSession(daoContext, chatId));
        }
    }

//    public void onUpdateReceived(Update update) {
//        Message message = update.getMessage();
//        if (message != null && message.hasText()) {
//            switch (message.getText()) {
//                case "Создать":
//                    try {
//                        execute(sendInlineKeyBoardMessage(update.getMessage().getChatId()));
//                    } catch (TelegramApiException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                case "Мои документы":
//                    //sendMsg(message, "Список документов");
//                    try {
//                        sendDocUploadingAFile(update.getMessage().getChatId(), new File("pika.jpg"), "my file");
//                    } catch (TelegramApiException e) {
//                        e.printStackTrace();
//                    }
//
//                    break;
//                case "Помощь":
//                    sendMsgByChatId(String.valueOf(message.getChatId()), "*Привет, я бот для облегчения работы с документами.*\n" +
//                            "_Перечень моих команд:_\n" +
//                            "_Создать:_ здесь можно выбрать тип файла для работы\n" +
//                            "_Мои документы:_ вывод всех когда-либо отправленных документов и их текущего состояния\n");
//                    break;
//                default:
//
//            }
//        }
//        else if(update.hasCallbackQuery()) {
//            try {
//                execute(new SendMessage()
//                        .setText(update.getCallbackQuery().getData())
//                        .setChatId(update.getCallbackQuery().getMessage().getChatId()));
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("Создать"));
        keyboardFirstRow.add(new KeyboardButton("Мои документы"));
        keyboardFirstRow.add(new KeyboardButton("Помощь"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }



    public String getBotUsername() {
        return BOTNAME;
    }

    public String getBotToken() {
        return BOTTOKEN;
    }
}
