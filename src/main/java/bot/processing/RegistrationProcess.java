package bot.processing;

import bot.Bot;
import dbService.dao.DAOContext;
import dbService.entity.Departments;
import dbService.entity.Employee;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegistrationProcess extends Process {
    private Employee user;
    private int curDepartmentIndex;

    public RegistrationProcess(DAOContext daoContext, String chatId) {
        this.daoContext = daoContext;
        this.chatId = chatId;
        curDepartmentIndex = 0;
        user = new Employee();
        user.setChatId(chatId);

        Bot.getInstance().sendMessage(chatId, "Введите Ваше ФИО");
        curState = "FIO";
    }

    private void handleFIO(Update update) {
        String[] fio = update.getMessage().getText().split(" ");
        if(fio.length < 3) {
            Bot.getInstance().sendMessage(chatId, "ФИО должно состоять из 3 слов");
        }
        else {
            user.setSurName(fio[0]);
            user.setFirstName(fio[1]);
            user.setMiddleName(fio[2]);
            curState = "department";
            Bot.getInstance().sendMessage(chatId,
                    "Введите название вашего отдела",
                    createMarkupForToDepartmentMessage());
        }
    }

    private void handleDepartment(Update update) {
        if(!update.hasCallbackQuery()) {
            return;
        }
        String data = update.getCallbackQuery().getData();
        if(data.equals("Следующие") || data.equals("Предыдущие")) {
            if(data.equals("Предыдущие") && curDepartmentIndex < 4)
                return;
            if(data.equals("Следующие") && curDepartmentIndex + 1 >= Departments.size())
                return;
            int newIndex = curDepartmentIndex - 4;
            if(data.equals("Предыдущие"))
                curDepartmentIndex = Math.max(0, curDepartmentIndex - 8);
            if(curDepartmentIndex == newIndex)
                return;
            EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
            edit.setChatId(chatId);
            edit.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            edit.setInlineMessageId(update.getCallbackQuery().getInlineMessageId());
            edit.setReplyMarkup(createMarkupForToDepartmentMessage());
            try {
                Bot.getInstance().execute(edit);
            }
            catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        else {
            user.setDepartment(data);
            curState = "position";
            Bot.getInstance().sendMessage(chatId, "Введите вашу должность");
        }
    }

    private void handlePosition(Update update) {
        String data = update.getMessage().getText();
        user.setPosition(data);
        curState = "login";
        Bot.getInstance().sendMessage(chatId, "Придумайте логин");
    }

    private void handleLogin(Update update) {
        String data = update.getMessage().getText();
        if(daoContext.getEmployeeDAO().getByLogin(data) != null) {
            Bot.getInstance().sendMessage(chatId, "К сожалению, этот логин занят :( Придумайте другой.");
        }
        else {
            user.setLogin(data);
            curState = "pass";
            Bot.getInstance().sendMessage(chatId, "Придумайте надёжный пароль(8+ символов)");
        }
    }

    private void handlePass(Update update) {
        String data = update.getMessage().getText();
        if(data == null || data.length() < 8) {
            Bot.getInstance().sendMessage(chatId, "Пароль слишком короткий, придумайте что-то ещё");
        }
        else {
            user.setPass(data);
            Bot.getInstance().sendMessage(chatId,
                    "Регистрация прошла успешно, можете приступить к работе",
                        MainMenuProcess.createButtons());
            daoContext.getEmployeeDAO().insert(user);

            Bot.getInstance().getUserSession(chatId).setLoggedIn(true);
            Bot.getInstance()
                    .getUserSession(chatId)
                    .setNextProcess(new MainMenuProcess(daoContext, chatId));
        }
    }

    private InlineKeyboardMarkup createMarkupForToDepartmentMessage() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        int max = curDepartmentIndex + 4;
        for(; curDepartmentIndex < Departments.size() && curDepartmentIndex < max; curDepartmentIndex++) {
            keyboard.add(createRow(createButton(Departments.get(curDepartmentIndex),
                    Departments.get(curDepartmentIndex))));
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
        if(curState.equals("FIO")) {
            handleFIO(update);
        }
        else if(curState.equals("department")) {
            handleDepartment(update);
        }
        else if(curState.equals("position")) {
            handlePosition(update);
        }
        else if(curState.equals("login")) {
            handleLogin(update);
        }
        else if(curState.equals("pass")) {
            handlePass(update);
        }
    }
}
