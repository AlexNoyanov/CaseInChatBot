package bot.processing;

import bot.Bot;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import dbService.dao.DAOContext;
import dbService.entity.Departments;
import dbService.entity.Document;
import dbService.entity.Employee;
import dbService.entity.PdfTemplate;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pdf.FieldAndRequest;
import pdf.PdfHandler;
import pdf.PdfHandlerImpl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class DocumentFillingProcess extends Process {
    private Document document;
    private PdfHandler pdfHandler;
    private List<FieldAndRequest> fields;
    private int curFieldIndex;
    private Set<String> filledFields;

    public DocumentFillingProcess(DAOContext daoContext, String chatId, String templateName) {
        this.daoContext = daoContext;
        this.chatId = chatId;
        document = new Document();
        document.setDocType(templateName);
        filledFields = new HashSet<>();
        curDepartmentIndex = 0;
        curEmployeeIndex = 0;
        curEditField = 0;
        employees = null;

        PdfTemplate template = daoContext.getPdfTemplateDAO().getByName(templateName);
        pdfHandler = new PdfHandlerImpl(template.getSerializedPdfTemplate());

        try {
            fields = (List<FieldAndRequest>)
                    new ObjectInputStream(new ByteArrayInputStream(template.getSerializedFields()))
                    .readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        autoComplete();
        executeCurState(null);
    }

    private void autoComplete() {
        Employee cur = daoContext.getEmployeeDAO().getByChatId(chatId);
        Field[] employeeFields = Employee.class.getDeclaredFields();
        for(Field field : employeeFields) {
            if(containsField(fields, field.getName())) {
                try {
                    field.setAccessible(true);
                    pdfHandler.setField(field.getName(), (String)field.get(cur));
                    field.setAccessible(false);
                }
                catch (IllegalAccessException e) {}
                filledFields.add(field.getName());
            }
        }
        if(containsField(fields, "date")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            pdfHandler.setField("date", dateFormat.format(new Date()));
            filledFields.add("date");
        }
    }

    private boolean containsField(List<FieldAndRequest> fields, String field) {
        for(FieldAndRequest far : fields) {
            if(far.getField().equals(field))
                return true;
        }
        return false;
    }

    private void sendDefaultRequest() {
        SendMessage message = new SendMessage();
        message.setText(fields.get(curFieldIndex).getRequest());
        message.setChatId(chatId);
        try {
            Bot.getInstance().execute(message);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendToDepartmentRequest() {
        SendMessage message = new SendMessage();
        message.setText(fields.get(curFieldIndex).getRequest());
        message.setChatId(chatId);
        message.setReplyMarkup(createMarkupForToDepartmentMessage());

        try {
            Bot.getInstance().execute(message);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendToPersonRequest() {
        SendMessage message = new SendMessage();
        message.setText(fields.get(curFieldIndex).getRequest());
        message.setChatId(chatId);
        message.setReplyMarkup(createMarkupForToPersonMessage());

        try {
            Bot.getInstance().execute(message);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private int curDepartmentIndex;

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

    private int curEmployeeIndex;
    private List<Employee> employees;

    private InlineKeyboardMarkup createMarkupForToPersonMessage() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        if(employees == null) {
            employees = daoContext
                        .getEmployeeDAO()
                        .getByDepartment(pdfHandler.getFieldValue("toDepartment"));
        }
        int max = curEmployeeIndex + 4;
        for(; curEmployeeIndex < employees.size() && curEmployeeIndex < max; curEmployeeIndex++) {
            Employee cur = employees.get(curEmployeeIndex);
            keyboard.add(createRow(createButton(cur.getPosition() + " " + cur.getFullName(),
                    cur.getPosition() + " " + cur.getFullName())));
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

    private void iterateCurFieldIndex() {
        if(curFieldIndex >= fields.size())
            return;
        while(curFieldIndex < fields.size() &&
                filledFields.contains(fields.get(curFieldIndex).getField()))
            curFieldIndex++;
    }

    private void handleCallback(CallbackQuery callback) {
        if(curState.equals("sendToDepartmentRequest")) {
            String data = callback.getData();
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
                edit.setMessageId(callback.getMessage().getMessageId());
                edit.setInlineMessageId(callback.getInlineMessageId());
                edit.setReplyMarkup(createMarkupForToDepartmentMessage());
                try {
                    Bot.getInstance().execute(edit);
                }
                catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else {
                pdfHandler.setField(fields.get(curFieldIndex).getField(), data);
                curFieldIndex++;
                document.setToDepartment(data);
                iterateCurFieldIndex();
            }
        }
        else if(curState.equals("sendToPersonRequest")) {
            String data = callback.getData();
            if(data.equals("Следующие") || data.equals("Предыдущие")) {
                if(data.equals("Предыдущие") && curEmployeeIndex < 4)
                    return;
                if(data.equals("Следующие") && curEmployeeIndex + 1 >= employees.size())
                    return;
                int newIndex = curEmployeeIndex - 4;
                if(data.equals("Предыдущие"))
                    curEmployeeIndex = Math.max(0, curEmployeeIndex - 8);
                if(curEmployeeIndex == newIndex)
                    return;
                EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
                edit.setChatId(chatId);
                edit.setMessageId(callback.getMessage().getMessageId());
                edit.setInlineMessageId(callback.getInlineMessageId());
                edit.setReplyMarkup(createMarkupForToDepartmentMessage());
                try {
                    Bot.getInstance().execute(edit);
                }
                catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else {
                String[] posAndFIO = data.split(" ");
                if(posAndFIO.length < 4)
                    return;
                if(pdfHandler.getFieldValue("toPosition") != null) {
                    pdfHandler.setField("toPosition", posAndFIO[0]);
                    filledFields.add("toPosition");
                }
                pdfHandler.setField(fields.get(curFieldIndex).getField(), posAndFIO[1] +
                        " " + posAndFIO[2] +
                        " " + posAndFIO[3]);
                curFieldIndex++;
                document.setToName(data);
                iterateCurFieldIndex();
            }
        }
    }

    private void handleMessage(Message message) {
        String msg = message.getText();
        if(curState.equals("sendToDepartmentRequest")) {
            if (!Departments.contains(msg)) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Такого отдела не существует :(");
                sendMessage.setChatId(chatId);
                try {
                    Bot.getInstance().execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                pdfHandler.setField(fields.get(curFieldIndex).getField(), msg);
                curFieldIndex++;
                document.setToDepartment(msg);
                iterateCurFieldIndex();
            }
        }
        else if(curState.equals("sendToPersonRequest")) {
            if(!containsInList(employees, msg)) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Такого сотрудника не существует :(");
                sendMessage.setChatId(chatId);
                try {
                    Bot.getInstance().execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                String[] posAndFIO = msg.split(" ");
                if(posAndFIO.length < 4)
                    return;
                if(pdfHandler.getFieldValue("toPosition") != null) {
                    pdfHandler.setField("toPosition", posAndFIO[0]);
                    filledFields.add("toPosition");
                }
                pdfHandler.setField(fields.get(curFieldIndex).getField(), posAndFIO[1] +
                        " " + posAndFIO[2] +
                        " " + posAndFIO[3]);
                curFieldIndex++;
                document.setToName(msg);
                iterateCurFieldIndex();
            }
        }
        else {
            pdfHandler.setField(fields.get(curFieldIndex).getField(), msg);
            curFieldIndex++;
            iterateCurFieldIndex();
        }

    }

    private boolean containsInList(List<Employee> list, String fullName) {
        for(Employee person : list) {
            if(person.getFullName().equals(fullName))
                return true;
        }
        return false;
    }

    private void showPreview() {
        byte[] doc = pdfHandler.getSerializedDocument();
  //      RandomFileNameGenerator random = new RandomFileNameGenerator();
        File pdf = new File(document.getDocType() + "№" + new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE) + ".pdf");
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfReader(new ByteArrayInputStream(doc)),
                                                        new PdfWriter(new FileOutputStream(pdf)));
            pdfDocument.close();
            PDDocument document = PDDocument.load(pdf);

            SendDocument sendDocument = new SendDocument();
            sendDocument.setDocument(pdf);
            sendDocument.setChatId(chatId);
//            PDFRenderer pdfRenderer = new PDFRenderer(document);
//            List<InputMedia> pictures = new ArrayList<>();
//            for(int page = 0; page < document.getNumberOfPages(); page++) {
//                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300);
//                String fileName = random.getRandomName() + ".jpg";
//                File file = new File(fileName);
//                ImageIO.write(bim, "JPEG", file);
//                pictures.add(new InputMediaPhoto().setMedia(file, file.getName()));
//            }
//            SendMediaGroup sendMediaGroup = new SendMediaGroup();
//            sendMediaGroup.setChatId(chatId);
//            sendMediaGroup.setMedia(pictures);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Отправить документ или редактировать?");

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            keyboard.add(createRow(createButton("Отправить", "Отправить"),
                                   createButton("Редактировать", "Редактировать")));
            inlineKeyboardMarkup.setKeyboard(keyboard);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);

            try {
                Bot.getInstance().execute(sendDocument);
                Bot.getInstance().execute(sendMessage);
            }
            catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void handleReview(Update update) {
        String data = update.hasCallbackQuery() ? update.getCallbackQuery().getData()
                                                : update.getMessage().getText();
        if(data.equals("Редактировать")) {
            curState = "editing";
            editFlag = false;
        }
        else if(data.equals("Отправить")) {
            curState = "Sending";
        }
    }

    private void sendEditing() {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите поле для изменения");
        message.setReplyMarkup(createMarkupForEditing());
        editFlag = true;
        try {
            Bot.getInstance().execute(message);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private FieldAndRequest fieldToEdit;

    private void handleEditing(Update update) {
        if(!update.hasCallbackQuery()) {
            return;
        }
        String data = update.getCallbackQuery().getData();
        if(data.equals("Предыдущие") || data.equals("Следующие")) {
            if(data.equals("Предыдущие") && curEditField < 4)
                return;
            if(data.equals("Следующие") && curEditField + 1 >= fields.size())
                return;
            int newIndex = curEditField - 4;
            if(data.equals("Предыдущие"))
                curEditField = Math.max(0, curEditField - 8);
            if(curEditField == newIndex)
                return;
            EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
            edit.setChatId(chatId);
            edit.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            edit.setInlineMessageId(update.getCallbackQuery().getInlineMessageId());
            edit.setReplyMarkup(createMarkupForEditing());
            try {
                Bot.getInstance().execute(edit);
            }
            catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        else {
            curEditField = 0;
            fieldToEdit = fields.get(Integer.parseInt(data));
            curState = "filling";
        }
    }

    private int curEditField;

    private InlineKeyboardMarkup createMarkupForEditing() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        int max = curEditField + 4;
        for(; curEditField < fields.size() && curEditField < max; curEditField++) {
            FieldAndRequest cur = fields.get(curEditField);
            keyboard.add(createRow(createButton(cur.getRussianField(), String.valueOf(curEditField))));
        }
        keyboard.add(createRow(createButton("Предыдущие", "Предыдущие"),
                createButton("Следующие", "Следующие")));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

    private void sendEditField() {
        if(fieldToEdit.getField().equals("toDepartment")) {
            sendToDepartmentRequest();
            curState = "sendToDepartmentRequest";
        }
        else if(fieldToEdit.getField().equals("toPerson")) {
            sendToPersonRequest();
            curState = "sendToPersonRequest";
        }
        else {
            SendMessage message = new SendMessage();
            message.setText(fieldToEdit.getRequest());
            message.setChatId(chatId);
            try {
                Bot.getInstance().execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleEditField(Update update) {
        String data = update.getMessage().getText();
        pdfHandler.setField(fieldToEdit.getField(), data);
        curState = "showPreview";
    }

    private void fillDocument() {
        document.setToName(pdfHandler.getFieldValue("toPerson"));
        document.setToDepartment(pdfHandler.getFieldValue("toDepartment"));
        document.setClosed(false);
        document.setFromDepartment(pdfHandler.getFieldValue("department"));
        document.setFromName(pdfHandler.getFieldValue("surName") + " " +
                             pdfHandler.getFieldValue("firstName") + " " +
                             pdfHandler.getFieldValue("middleName"));
        document.setFullName(document.getDocType() + "№" + new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE));
        document.setStatus("Рассматривается");
        document.setSerializedDocument(pdfHandler.getSerializedDocument());
    }

    private void sendDocument() {
        fillDocument();

        daoContext.getDocumentDAO().insert(document);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Документ отправлен");

        Employee recipient = findEmployee(employees, document.getToName());

        SendMessage sendDocMessage = new SendMessage();
        sendDocMessage.setChatId(recipient.getChatId());
        sendDocMessage.setText("Вам пришёл новый документ на обработку!");

        SendDocument sendDocument = new SendDocument();
        try {
            File pdfFile = new File(new RandomFileNameGenerator().getRandomName() + ".pdf");
            PdfDocument doc = new PdfDocument(new PdfReader(new ByteArrayInputStream(document.getSerializedDocument())),
                    new PdfWriter(new FileOutputStream(pdfFile)));
            doc.close();
            sendDocument.setChatId(recipient.getChatId());
            sendDocument.setDocument(pdfFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Bot.getInstance().execute(message);
            Bot.getInstance().execute(sendDocMessage);
            Bot.getInstance().execute(sendDocument);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private Employee findEmployee(List<Employee> list, String fullName) {
        for(Employee cur : list) {
            if(cur.getFullName().equals(fullName))
                return cur;
        }
        return null;
    }

    boolean editFlag = false;

    @Override
    public void executeCurState(Update update) {
        iterateCurFieldIndex();
        if(update != null) {
            if(curState.startsWith("send")) {
                if (update.hasCallbackQuery()) {
                    handleCallback(update.getCallbackQuery());
                } else {
                    handleMessage(update.getMessage());
                }
                if(curFieldIndex >= fields.size()) {
                    curState = "showPreview";
                }
            }
            else if(curState.equals("showPreview")) {
                handleReview(update);
            }
            else if(curState.equals("editing")) {
                handleEditing(update);
            }
            else if(curState.equals("filling")) {
                handleEditField(update);
            }
        }
        if(curFieldIndex < fields.size() && fields.get(curFieldIndex).getField().equals("toDepartment")) {
            sendToDepartmentRequest();
            curState = "sendToDepartmentRequest";
        }
        else if(curFieldIndex < fields.size() && fields.get(curFieldIndex).getField().equals("toPerson")) {
            sendToPersonRequest();
            curState = "sendToPersonRequest";
        }
        else if(curFieldIndex < fields.size()){
            sendDefaultRequest();
            curState = "sendDefaultRequest";
        }
        else if(curState.equals("showPreview")) {
            showPreview();
        }
        else if(!editFlag && curState.equals("editing")) {
            sendEditing();
        }
        else if(curState.equals("filling")) {
            sendEditField();
        }
        else if(curState.equals("Sending")) {
            sendDocument();

            Bot.getInstance()
                    .getUserSession(chatId)
                    .setNextProcess(new MainMenuProcess(daoContext, chatId));
        }
    }
}
