package dbService.entity;

import java.sql.Timestamp;
import java.util.Objects;

public class Request extends DataBaseObject {
    private String docType;
    private String fullName;
    private String fromEmployeeId;
    private String toEmployeeId;
    private String status;
    private boolean isClosed;
    private Timestamp updateDate;

    public Request(String docType, String fullName, String fromEmployeeId, String toEmployeeId, String status, boolean isClosed, Timestamp updateDate) {
        this.docType = docType;
        this.fullName = fullName;
        this.fromEmployeeId = fromEmployeeId;
        this.toEmployeeId = toEmployeeId;
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

    public String getFromEmployeeId() {
        return fromEmployeeId;
    }

    public void setFromEmployeeId(String fromEmployeeId) {
        this.fromEmployeeId = fromEmployeeId;
    }

    public String getToEmployeeId() {
        return toEmployeeId;
    }

    public void setToEmployeeId(String toEmployeeId) {
        this.toEmployeeId = toEmployeeId;
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
        if (!(o instanceof Request)) return false;
        Request request = (Request) o;
        return isClosed == request.isClosed &&
                Objects.equals(docType, request.docType) &&
                Objects.equals(fullName, request.fullName) &&
                Objects.equals(fromEmployeeId, request.fromEmployeeId) &&
                Objects.equals(toEmployeeId, request.toEmployeeId) &&
                Objects.equals(status, request.status) &&
                Objects.equals(updateDate, request.updateDate) &&
                request.id == id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, docType, fullName, fromEmployeeId, toEmployeeId, status, isClosed, updateDate);
    }
}
