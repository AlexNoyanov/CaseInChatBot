package pdf;

import java.io.File;

public interface PdfHandler {
    void setField(String fieldName, String value);
    String getFieldValue(String field);
    byte[] getSerializedDocument();
}
