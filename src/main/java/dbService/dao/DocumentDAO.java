package dbService.dao;

import dbService.entity.Document;

import java.sql.Timestamp;
import java.util.List;

public interface DocumentDAO extends DAO<Document> {
    Document getByName(String name);
    List<Document> getAllByType(String type);
    List<Document> getAllByDeparture(String fromDepartment);
    List<Document> getAllByArrival(String toDepartment);
    List<Document> getAllBySender(String fromName);
    List<Document> getAllByRecipient(String toName);
    List<Document> getAllByStatus(boolean isClosed);
    List<Document> getAllAfterDate(Timestamp date);
}
