package dbService.dao;

import dbService.entity.Request;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDAOImpl implements RequestDAO{
    private Connection connection;

    public RequestDAOImpl() {
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

    private List<Request> getEmployees(String selectQuery) {
        List<Request> list = new ArrayList<>();
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(selectQuery);
            while(rs.next()) {
                Request request = new Request(rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getBoolean(7),
                        rs.getTimestamp(8));
                request.setId(rs.getInt(1));
                list.add(request);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    @Override
    public Request getById(int id) {
        List<Request> list = getEmployees("select * from Requests where fullName = " + id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Request getByFullName(String fullName) {
        List<Request> list = getEmployees("select * from Requests where fullName = "
                + "'" + fullName + "'");
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Request> getByDocType(String docType) {
        return getEmployees("select * from Requests where docType = "
                + "'" + docType + "'");
    }

    @Override
    public List<Request> getBySender(String sender) {
        return getEmployees("select * from Requests where fromEmployeeId = "
                + "'" + sender + "'");
    }

    @Override
    public List<Request> getByRecipient(String recipient) {
        return getEmployees("select * from Requests where fromEmployeeId = "
                + "'" + recipient + "'");
    }

    @Override
    public List<Request> getByStatus(boolean isClosed) {
        return getEmployees("select * from Requests where isClosed = "
                + "'" + isClosed + "'");
    }

    @Override
    public List<Request> getByAfterDate(Timestamp updateDate) {
        return getEmployees("select * from Requests where updateDate > "
                + "'" + updateDate + "'");
    }

    @Override
    public List<Request> getAll() {
        return getEmployees("select * from Requests");
    }


    @Override
    public void insert(Request entity) {
        try(PreparedStatement statement = connection.prepareStatement("insert into Requests" +
                        " (docType, fullName, fromEmployeeId, toEmployeeId, status, isClosed," +
                        " updateDate) values (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getDocType());
            statement.setString(2, entity.getFullName());
            statement.setString(3, entity.getFromEmployeeId());
            statement.setString(4, entity.getToEmployeeId());
            statement.setString(5, entity.getStatus());
            statement.setBoolean(6, entity.isClosed());
            statement.setTimestamp(7, entity.getUpdateDate());
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
    public void update(Request entity) {
        try(PreparedStatement statement = connection.prepareStatement("update Requests set" +
                        " docType = ?, fullName = ?, fromEmployeeId = ?, toEmployeeId = ?, status = ?, isClosed = ?," +
                        " updateDate = ? where idRequest = ?")) {
            statement.setString(1, entity.getDocType());
            statement.setString(2, entity.getFullName());
            statement.setString(3, entity.getFromEmployeeId());
            statement.setString(4, entity.getToEmployeeId());
            statement.setString(5, entity.getStatus());
            statement.setBoolean(6, entity.isClosed());
            statement.setTimestamp(7, entity.getUpdateDate());
            statement.setInt(8, entity.getId());
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
            statement.execute("delete from Requests" +
                    " where idRequest = " + id);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
