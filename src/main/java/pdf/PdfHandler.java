package pdf;

public interface PdfHandler {
    void setField(String fieldName, String value);
    byte[] getSerializedDocument();

}
