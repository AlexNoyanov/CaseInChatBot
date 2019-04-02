package dbService.dao;

import dbService.entity.Document;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAOImpl implements DocumentDAO {
    private Connection connection;

    public DocumentDAOImpl() {
        connection = ConnectionProvider.getConnection();
    }

    @Override
    public void close() throws Exception {
        try {
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<Document> getDocuments(String byField, Object byValue) {
        List<Document> list = new ArrayList<>();
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from Documents" +
                    " where " + byField +" = " + "'" + byValue + "'");
            while(rs.next()) {
                Document document = new Document(rs.getString(2),
                        rs.getString(3),
                        rs.getBytes(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getBoolean(10),
                        rs.getTimestamp(11));
                document.setId(rs.getInt(1));
                list.add(document);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    @Override
    public Document getById(int id) {
        Document document = null;
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from Documents" +
                    " where idDocument = " + id);
            if(rs.next()) {
                document = new Document(rs.getString(2),
                        rs.getString(3),
                        rs.getBytes(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getBoolean(10),
                        rs.getTimestamp(11));
                document.setId(rs.getInt(1));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return document;
    }

    @Override
    public Document getByName(String fullName) {
        return getDocuments("fullName", fullName).get(0);
    }

    @Override
    public List<Document> getAllByType(String docType) {
        return getDocuments("docType", docType);
    }

    @Override
    public List<Document> getAllByDeparture(String fromDepartment) {
        return getDocuments("fromDepartment", fromDepartment);
    }

    @Override
    public List<Document> getAllByArrival(String toDepartment) {
        return getDocuments("toDepartment", toDepartment);
    }

    @Override
    public List<Document> getAllBySender(String fromName) {
        return getDocuments("fromName", fromName);
    }

    @Override
    public List<Document> getAllByRecipient(String toName) {
        return getDocuments("toName", toName);
    }

    @Override
    public List<Document> getAllByStatus(boolean isClosed) {
        return getDocuments("isClosed", isClosed);
    }

    @Override
    public List<Document> getAllAfterDate(Timestamp updateDate) {
        List<Document> list = new ArrayList<>();
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from Documents" +
                    " where updateDate > " + "'" + updateDate + "'");
            while(rs.next()) {
                Document document = new Document(rs.getString(2),
                        rs.getString(3),
                        rs.getBytes(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getBoolean(10),
                        rs.getTimestamp(11));
                document.setId(rs.getInt(1));
                list.add(document);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    @Override
    public List<Document> getAll() {
        List<Document> list = new ArrayList<>();
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from Documents");
            while(rs.next()) {
                Document document = new Document(rs.getString(2),
                        rs.getString(3),
                        rs.getBytes(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getBoolean(10),
                        rs.getTimestamp(11));
                document.setId(rs.getInt(1));
                list.add(document);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    @Override
    public void insert(Document entity) {
        try(PreparedStatement statement = connection.prepareStatement("insert into Documents" +
                        " (docType, fullName, serializedDocument, fromName, fromDepartment, toName," +
                        " toDepartment, status, isClosed, updateDate) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getDocType());
            statement.setString(2, entity.getFullName());
            statement.setBytes(3, entity.getSerializedDocument());
            statement.setString(4, entity.getFromName());
            statement.setString(5, entity.getFromDepartment());
            statement.setString(6, entity.getToName());
            statement.setString(7, entity.getToDepartment());
            statement.setString(8, entity.getStatus());
            statement.setBoolean(9, entity.isClosed());
            statement.setTimestamp(10, entity.getUpdateDate());
            statement.execute();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if(generatedKeys.next())
                entity.setId(generatedKeys.getInt(1));
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(Document entity) {
        try(PreparedStatement statement = connection.prepareStatement("update Documents set" +
                        " docType = ?, fullName = ?, serializedDocument = ?, fromName = ?, fromDepartment = ?," +
                        " toName = ?, toDepartment = ?, status = ?, isClosed = ?, updateDate = ? where idDocument = ?",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getDocType());
            statement.setString(2, entity.getFullName());
            statement.setBytes(3, entity.getSerializedDocument());
            statement.setString(4, entity.getFromName());
            statement.setString(5, entity.getFromDepartment());
            statement.setString(6, entity.getToName());
            statement.setString(7, entity.getToDepartment());
            statement.setString(8, entity.getStatus());
            statement.setBoolean(9, entity.isClosed());
            statement.setTimestamp(10, entity.getUpdateDate());
            statement.setInt(11, entity.getId());
            statement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        try(Statement statement = connection.createStatement()) {
            statement.execute("delete from Documents" +
                    " where idDocument = " + id);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
