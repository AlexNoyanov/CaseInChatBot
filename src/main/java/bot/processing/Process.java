package bot.processing;

import dbService.dao.DAOContext;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public abstract class Process {
    protected String chatId;
    protected DAOContext daoContext;
    protected String curState;

    public abstract void executeCurState(Update update);
}
