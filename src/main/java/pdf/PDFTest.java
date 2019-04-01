package pdf;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import dao.ConnectionProvider;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PDFTest {
    public static void main(String[] args) throws IOException {
        PdfReader reader = new PdfReader("OtpuskTemplate.pdf");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument doc = new PdfDocument(reader, writer);

        PdfAcroForm form = PdfAcroForm.getAcroForm(doc, true);

        Map<String, PdfFormField> fields = form.getFormFields();

        fields.get("Должность Руководителя").setValue("Руководитель отдела закупок").setReadOnly(true);
        fields.get("Имя Руководителя").setValue("Иванов А.С.").setReadOnly(true);
        fields.get("Имя Заявителя").setValue("Сидоров Н.Д.").setReadOnly(true);
        fields.get("Должность Заявителя").setValue("Стажёр отдела закупок").setReadOnly(true);
        fields.get("Время Отпуска").setValue("28").setReadOnly(true);
        fields.get("Начало Отпуска").setValue("31.03.2019").setReadOnly(true);
        fields.get("Конец Отпуска").setValue("27.04.2019").setReadOnly(true);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date curDate = new Date();
        fields.get("Дата").setValue(dateFormat.format(curDate)).setReadOnly(true);

        form.setNeedAppearances(true);

        doc.close();

        byte[] serializedDoc = baos.toByteArray();

        Map<String, String> map = new HashMap<>();
        map.put("Должность Руководителя", String.class.getName());
        map.put("Конец Отпуска", Date.class.getName());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(map);

        byte[] serializedMap = byteArrayOutputStream.toByteArray();


        try {
            Connection connection = ConnectionProvider.getConnection();
            PreparedStatement insertStatement = connection.prepareStatement("insert into pdf_documents"
                    + " (name, pdf_template, fields_mapping) values (?, ?, ?)");
            insertStatement.setString(1, "NewOrder");
            insertStatement.setBytes(2, serializedDoc);
            insertStatement.setBytes(3, serializedMap);
            insertStatement.execute();
            insertStatement.close();



            Statement selectStatement = connection.createStatement();
            ResultSet rs = selectStatement.executeQuery("select * from pdf_documents");
            while(rs.next()){
                String name = rs.getString(2);
                byte[] selectedDoc = rs.getBytes(3);
                byte[] selectedMap = rs.getBytes(4);
                PdfReader pdfReader = new PdfReader(new ByteArrayInputStream(selectedDoc));
                PdfWriter pdfWriter = new PdfWriter("order.pdf");

                PdfDocument pdfDocument = new PdfDocument(pdfReader, pdfWriter);

                PdfAcroForm pdfAcroForm = PdfAcroForm.getAcroForm(pdfDocument, true);
                Map<String, PdfFormField> fieldsAcroForm = pdfAcroForm.getFormFields();
                for(String key : fieldsAcroForm.keySet())
                    System.out.println(key);
                fieldsAcroForm.get("Имя Руководителя").setValue("Николаев А.С.").setReadOnly(true);

                pdfAcroForm.setNeedAppearances(true);

                pdfDocument.close();
            }




            selectStatement.close();
            connection.close();
        }
        catch (SQLException e){
            System.out.println("SQL Exception");
        }























    }
}
