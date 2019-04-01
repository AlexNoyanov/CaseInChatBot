package pdf;

import java.sql.Timestamp;

public class Document {
    private String type;
    private String name;
    private byte[] serializedDocument;
    private String fromName;
    private String fromDepartment;
    private String toName;
    private String toDepartment;
    private String status;
    private Timestamp date;
}
