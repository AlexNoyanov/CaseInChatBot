package bot.processing;

import bot.Bot;
import dbService.dao.DAOContext;
import dbService.entity.Employee;
import org.telegram.telegrambots.meta.api.objects.Update;

public class LoginProcess extends Process {
    private Employee user;
    private String login;
    private String pass;

    public LoginProcess(DAOContext daoContext, String chatId) {
        this.daoContext = daoContext;
        this.chatId = chatId;
        curState = "";
        user = daoContext.getEmployeeDAO().getByChatId(chatId);

        curState = "login";
        Bot.getInstance().sendMessage(chatId, "Введите ваш логин");
    }

    private void handleLogin(Update update) {
        login = update.getMessage().getText();
        curState = "pass";
        Bot.getInstance().sendMessage(chatId, "Введите ваш пароль");
    }

    private void handlePass(Update update) {
        pass = update.getMessage().getText();
        if(user.getLogin().equals(login) && user.getPass().equals(pass)) {
            Bot.getInstance().sendMessage(chatId,
                    "Добро пожаловать в систему, вы можете приступить к работе",
                    MainMenuProcess.createButtons());
            Bot.getInstance().getUserSession(chatId).setLoggedIn(true);
            Bot.getInstance()
                    .getUserSession(chatId)
                    .setNextProcess(new MainMenuProcess(daoContext, chatId));
        }
        else {
            curState = "login";
            Bot.getInstance().sendMessage(chatId, "Неверный логин или пароль\nВведите ваш логин");
        }
    }



    @Override
    public void executeCurState(Update update) {
        if(curState.equals("login")) {
            handleLogin(update);
        }
        else if(curState.equals("pass")) {
            handlePass(update);
        }
    }
}
