package dbService.dao;

import dbService.entity.Request;

import java.sql.Timestamp;
import java.util.List;

public interface RequestDAO extends DAO<Request> {
    Request getByFullName(String fullName);
    List<Request> getByDocType(String docType);
    List<Request> getBySender(String sender);
    List<Request> getByRecipient(String recipient);
    List<Request> getByStatus(boolean isClosed);
    List<Request> getByAfterDate(Timestamp updateDate);
}
