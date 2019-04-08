package dbService;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import dbService.dao.PdfTemplateDAO;
import dbService.dao.PdfTemplateDAOImpl;
import dbService.entity.PdfTemplate;
import org.apache.pdfbox.pdmodel.PDDocument;
import pdf.FieldAndRequest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InsertPdfTemplates {
    public static void main(String[] args) throws IOException {
        PdfTemplateDAO dao = new PdfTemplateDAOImpl();

//        String fileName = "Заявление на оплачиваемый отпуск";
//        PdfTemplate pdf = new PdfTemplate();
//        pdf.setFullName(fileName);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PdfDocument pdfDocument = new PdfDocument(new PdfReader(fileName + ".pdf"),
//                                  new PdfWriter(baos));
//        pdfDocument.close();
//        pdf.setSerializedPdfTemplate(baos.toByteArray());
//
//        List<FieldAndRequest> fields = new ArrayList<>();
//
//        fields.add(new FieldAndRequest("surName", "Введите вашу Фамилию", "Ваша фамилия"));
//        fields.add(new FieldAndRequest("firstName", "Введите ваше имя", "Ваше имя"));
//        fields.add(new FieldAndRequest("middleName", "Введите ваше отчество", "Ваше отчество"));
//        fields.add(new FieldAndRequest("department", "Введите название вашего отдела", "Ваш отдел"));
//        fields.add(new FieldAndRequest("position", "Введите вашу должность", "Ваша должность"));
//        fields.add(new FieldAndRequest("toDepartment", "Введите название отдела получателя", "Отдел получателя"));
//        fields.add(new FieldAndRequest("toPerson", "Введите ФИО получателя", "ФИО получателя"));
//        fields.add(new FieldAndRequest("toPosition", "Введите должность получателя", "Должность получателя"));
//        fields.add(new FieldAndRequest("numOfDays", "Введите длительность отпуска", "Длительность отпуска"));
//        fields.add(new FieldAndRequest("start", "Введите дату начала отпуска", "Дата начала отпуска"));
//        fields.add(new FieldAndRequest("end", "Введите дату конца отпуска", "Дата конца отпуска"));
//        fields.add(new FieldAndRequest("date", "Введите текущую дату", "Текущая дата"));
//
//        ByteArrayOutputStream fieldBAOS = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(fieldBAOS);
//        oos.writeObject(fields);
//        pdf.setSerializedFields(fieldBAOS.toByteArray());
//
//        dao.insert(pdf);



//        String fileName = "Заявление на оплачиваемый отпуск";
//        PdfTemplate pdf = new PdfTemplate();
//        pdf.setFullName(fileName);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PDDocument pdfDocument = PDDocument.load(new File(fileName + ".pdf"));
//        pdfDocument.save(baos);
//        pdfDocument.close();
//        pdf.setSerializedPdfTemplate(baos.toByteArray());
//
//        List<FieldAndRequest> fields = new ArrayList<>();
//
//        fields.add(new FieldAndRequest("surName", "Введите вашу Фамилию", "Ваша фамилия"));
//        fields.add(new FieldAndRequest("firstName", "Введите ваше имя", "Ваше имя"));
//        fields.add(new FieldAndRequest("middleName", "Введите ваше отчество", "Ваше отчество"));
//        fields.add(new FieldAndRequest("department", "Введите название вашего отдела", "Ваш отдел"));
//        fields.add(new FieldAndRequest("position", "Введите вашу должность", "Ваша должность"));
//        fields.add(new FieldAndRequest("toDepartment", "Введите название отдела получателя", "Отдел получателя"));
//        fields.add(new FieldAndRequest("toPerson", "Введите ФИО получателя", "ФИО получателя"));
//        fields.add(new FieldAndRequest("toPosition", "Введите должность получателя", "Должность получателя"));
//        fields.add(new FieldAndRequest("numOfDays", "Введите длительность отпуска", "Длительность отпуска"));
//        fields.add(new FieldAndRequest("start", "Введите дату начала отпуска", "Дата начала отпуска"));
//        fields.add(new FieldAndRequest("end", "Введите дату конца отпуска", "Дата конца отпуска"));
//        fields.add(new FieldAndRequest("date", "Введите текущую дату", "Текущая дата"));
//
//        ByteArrayOutputStream fieldBAOS = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(fieldBAOS);
//        oos.writeObject(fields);
//        pdf.setSerializedFields(fieldBAOS.toByteArray());
//
//        dao.insert(pdf);





        String fileName = "Справка о месте работы";
        PdfTemplate pdf = new PdfTemplate();
        pdf.setFullName(fileName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(fileName + ".pdf"),
                new PdfWriter(baos));
        pdfDocument.close();
        pdf.setSerializedPdfTemplate(baos.toByteArray());

        List<FieldAndRequest> fields = new ArrayList<>();

        fields.add(new FieldAndRequest("surName", "Введите вашу Фамилию", "Ваша фамилия"));
        fields.add(new FieldAndRequest("firstName", "Введите ваше имя", "Ваше имя"));
        fields.add(new FieldAndRequest("middleName", "Введите ваше отчество", "Ваше отчество"));
        fields.add(new FieldAndRequest("department", "Введите название вашего отдела", "Ваш отдел"));
        fields.add(new FieldAndRequest("position", "Введите вашу должность", "Ваша должность"));
        fields.add(new FieldAndRequest("date", "Введите текущую дату", "Текущая дата"));

        ByteArrayOutputStream fieldBAOS = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(fieldBAOS);
        oos.writeObject(fields);
        pdf.setSerializedFields(fieldBAOS.toByteArray());

        dao.insert(pdf);
    }
}
