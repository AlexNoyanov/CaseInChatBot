package dbService.dao;

import dbService.entity.PdfTemplate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PdfTemplateDAOImpl implements PdfTemplateDAO{
    private Connection connection;

    public PdfTemplateDAOImpl() {
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

    @Override
    public PdfTemplate getById(int id) {
        PdfTemplate pdfTemplate = null;
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from PdfTemplates" +
                    " where ifPdfTemplate = " + id);
            if(rs.next()) {
                pdfTemplate = new PdfTemplate(rs.getString(2),
                        rs.getBytes(3),
                        rs.getBytes(4));
                pdfTemplate.setId(rs.getInt(1));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return pdfTemplate;
    }

    @Override
    public PdfTemplate getByName(String name) {
        PdfTemplate pdfTemplate = null;
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from PdfTemplates" +
                    " where name = " + "'" + name + "'");
            if(rs.next()) {
                pdfTemplate = new PdfTemplate(rs.getString(2),
                        rs.getBytes(3),
                        rs.getBytes(4));
                pdfTemplate.setId(rs.getInt(1));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return pdfTemplate;
    }

    @Override
    public List<PdfTemplate> getAll() {
        List<PdfTemplate> list = new ArrayList<>();
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from PdfTemplates");
            while(rs.next()) {
                PdfTemplate pdfTemplate = new PdfTemplate(rs.getString(2),
                        rs.getBytes(3),
                        rs.getBytes(4));
                pdfTemplate.setId(rs.getInt(1));
                list.add(pdfTemplate);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    @Override
    public void insert(PdfTemplate entity) {
        try(PreparedStatement statement = connection.prepareStatement("insert into PdfTemplates" +
                " (name, serializedPdfTemplate, serializedFields) values (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, entity.getName());
                statement.setBytes(2, entity.getSerializedPdfTemplate());
                statement.setBytes(3, entity.getSerializedFields());
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
    public void update(PdfTemplate entity) {
        try(PreparedStatement statement = connection.prepareStatement("update PdfTemplates set" +
                        " name = ?, serializedPdfTemplate = ?, serializedFields = ?" +
                        " where idPdfTemplate = ?")) {
            statement.setString(1, entity.getName());
            statement.setBytes(2, entity.getSerializedPdfTemplate());
            statement.setBytes(3, entity.getSerializedFields());
            statement.setInt(4, entity.getId());
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
            statement.execute("delete from PdfTemplates" +
                    " where idPdfTemplate = " + id);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteByName(String name) {
        try(Statement statement = connection.createStatement()) {
            statement.execute("delete from PdfTemplates" +
                    " where name = " + "'" + name + "'");
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
