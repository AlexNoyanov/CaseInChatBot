package bot;

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
import java.util.List;


public class Bot extends TelegramLongPollingBot {
    private final String BOTNAME = "CaseInDocHelperBot";
    private final String BOTTOKEN = "854269089:AAF-GvqdGb46vUlQMWl7Z8aOZ5cc9S_8vtc";

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        //sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "Создать":
                    try {
                        execute(sendInlineKeyBoardMessage(update.getMessage().getChatId()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "Мои документы":
                    //sendMsg(message, "Список документов");
                    try {
                        sendDocUploadingAFile(update.getMessage().getChatId(), new File("pika.jpg"), "my file");
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    break;
                case "Помощь":
                    sendMsg(message, "*Привет, я бот для облегчения работы с документами.*\n" +
                            "_Перечень моих команд:_\n" +
                            "_Создать:_ здесь можно выбрать тип файла для работы\n" +
                            "_Мои документы:_ вывод всех когда-либо отправленных документов и их текущего состояния\n");
                    break;
                default:

            }
        }
        else if(update.hasCallbackQuery()) {
            try {
                execute(new SendMessage()
                        .setText(update.getCallbackQuery().getData())
                        .setChatId(update.getCallbackQuery().getMessage().getChatId()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    /*case "1":
                    sendMsg(message, "Кнопка 1");
                case "2":
                    sendMsg(message, "Кнопка 2");*/

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

    public static SendMessage sendInlineKeyBoardMessage(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("1");
        inlineKeyboardButton1.setCallbackData("Button \"1\" has been pressed");
        inlineKeyboardButton2.setText("2");
        inlineKeyboardButton2.setCallbackData("Button \"2\" has been pressed");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setChatId(chatId).setText("Создать").setReplyMarkup(inlineKeyboardMarkup);
    }

    private void sendDocUploadingAFile(Long chatId, java.io.File save,String caption) throws TelegramApiException {
        SendDocument sendDocumentRequest = new SendDocument();
        sendDocumentRequest.setChatId(chatId);
        sendDocumentRequest.setDocument(save);
        sendDocumentRequest.setCaption(caption);
        execute(sendDocumentRequest);
    }



    public String getBotUsername() {
        return BOTNAME;
    }

    public String getBotToken() {
        return BOTTOKEN;
    }
}
