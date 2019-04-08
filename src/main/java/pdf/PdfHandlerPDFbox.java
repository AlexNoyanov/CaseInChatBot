package pdf;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class PdfHandlerPDFbox implements PdfHandler{
    private byte[] serializedDoc;
    private PDDocument pdfDocument;
    private ByteArrayOutputStream outputStream;
    private PDAcroForm acroForm;
    String defaultAppearanceString;
   // private Map<String, PdfFormField> fields;

    public PdfHandlerPDFbox(byte[] serializedDoc) {
        initialize(serializedDoc);
    }

    private void initialize(byte[] serializedDoc) {
        this.serializedDoc = serializedDoc;
        outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedDoc);
        try {
            pdfDocument = PDDocument.load(serializedDoc);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

        PDFont font = null;
        try {
            font = PDType0Font.load(pdfDocument,
                    new FileInputStream("C:/Windows/Fonts/calibri.ttf"), false);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        PDResources res = acroForm.getDefaultResources(); // could be null, if so, then create it with the setter
        if(res == null) {
            res = new PDResources();
            acroForm.setDefaultResources(res);
        }
        String fontName = res.add(font).getName();
        defaultAppearanceString = "/" + fontName + " 0 Tf 0 g"; // adjust to replace existing font name
        acroForm.setDefaultAppearance(defaultAppearanceString);
    }

    @Override
    public void setField(String fieldName, String value) {
        try {
            PDField field = acroForm.getField(fieldName);
            field.setValue(value);
            field.setReadOnly(true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFieldValue(String field) {
        return acroForm.getField(field).getValueAsString();
    }

    @Override
    public byte[] getSerializedDocument() {
        acroForm.setNeedAppearances(true);
        try {
            pdfDocument.save(outputStream);
            pdfDocument.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        serializedDoc = outputStream.toByteArray();
        initialize(serializedDoc);
        return serializedDoc;
    }
}
