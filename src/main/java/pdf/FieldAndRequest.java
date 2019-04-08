package pdf;

import java.io.Serializable;
import java.util.Objects;

public class FieldAndRequest implements Serializable {
    private String field;
    private String request;
    private String russianField;

    public FieldAndRequest(String field, String request, String russianField) {
        this.field = field;
        this.request = request;
        this.russianField = russianField;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRussianField() {
        return russianField;
    }

    public void setRussianField(String russianField) {
        this.russianField = russianField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldAndRequest that = (FieldAndRequest) o;
        return Objects.equals(field, that.field) &&
                Objects.equals(request, that.request) &&
                Objects.equals(russianField, that.russianField);
    }

    @Override
    public int hashCode() {

        return Objects.hash(field, request, russianField);
    }
}
