package dbService.entity;

import java.util.Arrays;
import java.util.Objects;

public class PdfTemplate extends DataBaseObject{
    private String fullName;
    private byte[] serializedPdfTemplate;
    private byte[] serializedFields;

    public PdfTemplate() {}

    public PdfTemplate(String fullName, byte[] serializedPdfTemplate, byte[] serializedFields) {
        this.fullName = fullName;
        this.serializedPdfTemplate = serializedPdfTemplate;
        this.serializedFields = serializedFields;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public byte[] getSerializedPdfTemplate() {
        return serializedPdfTemplate;
    }

    public void setSerializedPdfTemplate(byte[] serializedPdfTemplate) {
        this.serializedPdfTemplate = serializedPdfTemplate;
    }

    public byte[] getSerializedFields() {
        return serializedFields;
    }

    public void setSerializedFields(byte[] serializedFields) {
        this.serializedFields = serializedFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PdfTemplate)) return false;
        PdfTemplate that = (PdfTemplate) o;
        return Objects.equals(fullName, that.fullName) &&
                Arrays.equals(serializedPdfTemplate, that.serializedPdfTemplate) &&
                Arrays.equals(serializedFields, that.serializedFields) &&
                id == that.id;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(fullName, id);
        result = 31 * result + Arrays.hashCode(serializedPdfTemplate);
        result = 31 * result + Arrays.hashCode(serializedFields);
        return result;
    }
}
