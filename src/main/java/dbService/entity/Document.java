package dbService.entity;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

public class Document extends DataBaseObject{
    private String type;
    private String name;
    private byte[] serializedDocument;
    private String fromName;
    private String fromDepartment;
    private String toName;
    private String toDepartment;
    private String status;
    private boolean isClosed;
    private Timestamp date;

    public Document(String type, String name, byte[] serializedDocument, String fromName, String fromDepartment, String toName, String toDepartment, String status, boolean isClosed, Timestamp date) {
        this.type = type;
        this.name = name;
        this.serializedDocument = serializedDocument;
        this.fromName = fromName;
        this.fromDepartment = fromDepartment;
        this.toName = toName;
        this.toDepartment = toDepartment;
        this.status = status;
        this.isClosed = isClosed;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getSerializedDocument() {
        return serializedDocument;
    }

    public void setSerializedDocument(byte[] serializedDocument) {
        this.serializedDocument = serializedDocument;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromDepartment() {
        return fromDepartment;
    }

    public void setFromDepartment(String fromDepartment) {
        this.fromDepartment = fromDepartment;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToDepartment() {
        return toDepartment;
    }

    public void setToDepartment(String toDepartment) {
        this.toDepartment = toDepartment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document)) return false;
        Document document = (Document) o;
        return isClosed == document.isClosed &&
                Objects.equals(type, document.type) &&
                Objects.equals(name, document.name) &&
                Arrays.equals(serializedDocument, document.serializedDocument) &&
                Objects.equals(fromName, document.fromName) &&
                Objects.equals(fromDepartment, document.fromDepartment) &&
                Objects.equals(toName, document.toName) &&
                Objects.equals(toDepartment, document.toDepartment) &&
                Objects.equals(status, document.status) &&
                Objects.equals(date, document.date) &&
                id == document.id;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, type, name, fromName, fromDepartment, toName, toDepartment, status, isClosed, date);
        result = 31 * result + Arrays.hashCode(serializedDocument);
        return result;
    }
}
