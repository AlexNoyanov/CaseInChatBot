package pdf;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

public class PdfHandlerImpl implements PdfHandler{
    private PdfDocument pdfDocument;
    private ByteArrayOutputStream outputStream;
    private PdfAcroForm acroForm;
    private Map<String, PdfFormField> fields;

    public PdfHandlerImpl(byte[] serializedDoc) throws IOException {
        outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedDoc);
        pdfDocument = new PdfDocument(new PdfReader(inputStream), new PdfWriter(outputStream));
        acroForm = PdfAcroForm.getAcroForm(pdfDocument, true);
        fields = acroForm.getFormFields();
    }

    @Override
    public void setField(String fieldName, String value) {
        fields.get(fieldName).setValue(value).setReadOnly(true);
    }

    @Override
    public byte[] getSerializedDocument() {
        acroForm.setNeedAppearances(true);
        pdfDocument.close();
        return outputStream.toByteArray();
    }
}
