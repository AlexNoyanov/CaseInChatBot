package bot.processing;

import bot.Bot;
import dbService.dao.DAOContext;
import dbService.entity.Employee;
import dbService.entity.PdfTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocumentCreationProcess extends Process {
    List<PdfTemplate> templates;
    private int curTemplateIndex;

    public DocumentCreationProcess(DAOContext daoContext, String chatId) {
        this.daoContext = daoContext;
        this.chatId = chatId;
        templates = daoContext.getPdfTemplateDAO().getAll();
        curTemplateIndex = 0;

        sendTemplateList();
    }

    public void sendTemplateList() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Выберите документ для заполнения");
        sendMessage.setReplyMarkup(createMarkupForTemplateList());

        try {
            Bot.getInstance().execute(sendMessage);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String handleCallback(Update update) {
        if(!update.hasCallbackQuery())
            return null;
        String data = update.getCallbackQuery().getData();
        if(data.equals("Следующие") || data.equals("Предыдущие")) {
            if(data.equals("Предыдущие") && curTemplateIndex < 4)
                return null;
            if(data.equals("Следующие") && curTemplateIndex + 1 >= templates.size())
                return null;
            int newIndex = curTemplateIndex - 4;
            if(data.equals("Предыдущие"))
                curTemplateIndex = Math.max(0, curTemplateIndex - 8);
            if(curTemplateIndex == newIndex)
                return null;
            EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
            edit.setChatId(chatId);
            edit.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            edit.setInlineMessageId(update.getCallbackQuery().getInlineMessageId());
            edit.setReplyMarkup(createMarkupForTemplateList());
            try {
                Bot.getInstance().execute(edit);
            }
            catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return null;
        }
        else {
            return data;
        }
    }

    private InlineKeyboardMarkup createMarkupForTemplateList() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        int max = curTemplateIndex + 4;
        for(; curTemplateIndex < templates.size() && curTemplateIndex < max; curTemplateIndex++) {
            PdfTemplate cur = templates.get(curTemplateIndex);
            keyboard.add(createRow(createButton(cur.getFullName(), cur.getFullName())));
        }
        keyboard.add(createRow(createButton("Предыдущие", "Предыдущие"),
                createButton("Следующие", "Следующие")));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardButton createButton(String message, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton(message);
        button.setCallbackData(callbackData);
        return button;
    }

    private List<InlineKeyboardButton> createRow(InlineKeyboardButton... buttons) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        Collections.addAll(row, buttons);
        return row;
    }

    @Override
    public void executeCurState(Update update) {
        String res = handleCallback(update);
        if(res != null) {
            Bot.getInstance()
                    .getUserSession(chatId)
                    .setNextProcess(new DocumentFillingProcess(daoContext, chatId, res));
        }
    }
}
