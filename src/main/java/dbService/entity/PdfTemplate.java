package dbService.entity;

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
}
