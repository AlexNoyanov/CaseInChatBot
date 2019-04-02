package dbService.entity;

import java.util.Arrays;
import java.util.Objects;

public class PdfTemplate extends DataBaseObject{
    private String name;
    private byte[] serializedPdfTemplate;
    private byte[] serializedFields;

    public PdfTemplate(String name, byte[] serializedPdfTemplate, byte[] serializedFields) {
        this.name = name;
        this.serializedPdfTemplate = serializedPdfTemplate;
        this.serializedFields = serializedFields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return Objects.equals(name, that.name) &&
                Arrays.equals(serializedPdfTemplate, that.serializedPdfTemplate) &&
                Arrays.equals(serializedFields, that.serializedFields) &&
                id == that.id;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, id);
        result = 31 * result + Arrays.hashCode(serializedPdfTemplate);
        result = 31 * result + Arrays.hashCode(serializedFields);
        return result;
    }
}
