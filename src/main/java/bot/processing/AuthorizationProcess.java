package bot.processing;

import bot.Bot;
import dbService.dao.DAOContext;
import dbService.entity.Employee;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class AuthorizationProcess extends Process {
    public AuthorizationProcess(DAOContext daoContext, String chatId) {
        this.daoContext = daoContext;
        this.chatId = chatId;
        //curState = "chooseLoginOrRegister";
        chooseLoginOrRegister();
    }

    @Override
    public void executeCurState(Update update) {
        if(curState.equals("getDecision")) {
            getDecision(update);
        }
    }

    private void chooseLoginOrRegister() {
        SendMessage message = new SendMessage();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button1 = new InlineKeyboardButton("Регистрация");
        button1.setCallbackData("Регистрация");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Вход");
        button2.setCallbackData("Вход");
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button1);
        row.add(button2);
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);
        inlineKeyboardMarkup.setKeyboard(keyboard);

        message.setChatId(chatId);
        message.setText("Добро пожаловать, пожалуйста, войдите или зарегистрируйтесь");
        message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            Bot.getInstance().execute(message);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }

        curState = "getDecision";
    }

    private void getDecision(Update update) {
        Employee user = daoContext.getEmployeeDAO().getByChatId(chatId);
        Process nextProcess = null;
        if(update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            if (data.equals("Регистрация")) {
                if(user != null) {
                    Bot.getInstance().sendMessage(chatId, "Вы уже зарегистрированы, пожалуйста войдите в систему");
                    nextProcess = new LoginProcess(daoContext, chatId);
                }
                else
                    nextProcess = new RegistrationProcess(daoContext, chatId);

            }
            else if(user == null) {
                Bot.getInstance().sendMessage(chatId, "Вы не зарегистрированы, пожалуйста зарегистрируйтесь");
                nextProcess = new RegistrationProcess(daoContext, chatId);
            }
            else {
                nextProcess = new LoginProcess(daoContext, chatId);
            }
        }
        Bot.getInstance()
                .getUserSession(chatId)
                .setNextProcess(nextProcess);
    }
}
