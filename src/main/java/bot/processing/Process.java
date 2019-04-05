package bot.processing;

import dbService.dao.DAOContext;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public abstract class Process {
    private DAOContext daoContext;
    private int curState;
    private List<String> states;

    public abstract void executeCurProcess(Update update);
}
