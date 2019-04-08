package dbService.entity;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

public class Document extends DataBaseObject{
    private String docType;
    private String fullName;
    private byte[] serializedDocument;
    private String fromName;
    private String fromDepartment;
    private String toName;
    private String toDepartment;
    private String status;
    private boolean isClosed;
    private Timestamp updateDate;

    public Document() {}

    public Document(String docType, String fullName, byte[] serializedDocument, String fromName, String fromDepartment, String toName, String toDepartment, String status, boolean isClosed, Timestamp updateDate) {
        this.docType = docType;
        this.fullName = fullName;
        this.serializedDocument = serializedDocument;
        this.fromName = fromName;
        this.fromDepartment = fromDepartment;
        this.toName = toName;
        this.toDepartment = toDepartment;
        this.status = status;
        this.isClosed = isClosed;
        this.updateDate = updateDate;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document)) return false;
        Document document = (Document) o;
        return isClosed == document.isClosed &&
                Objects.equals(docType, document.docType) &&
                Objects.equals(fullName, document.fullName) &&
                Arrays.equals(serializedDocument, document.serializedDocument) &&
                Objects.equals(fromName, document.fromName) &&
                Objects.equals(fromDepartment, document.fromDepartment) &&
                Objects.equals(toName, document.toName) &&
                Objects.equals(toDepartment, document.toDepartment) &&
                Objects.equals(status, document.status) &&
                Objects.equals(updateDate, document.updateDate) &&
                id == document.id;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, docType, fullName, fromName, fromDepartment, toName, toDepartment, status, isClosed, updateDate);
        result = 31 * result + Arrays.hashCode(serializedDocument);
        return result;
    }
}
