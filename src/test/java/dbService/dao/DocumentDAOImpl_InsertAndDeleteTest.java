package dbService.dao;

import dbService.entity.Document;
import dbService.entity.PdfTemplate;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.*;

public class DocumentDAOImpl_InsertAndDeleteTest {
    static DocumentDAO dao;
    static Document document;

    @BeforeClass
    public static void setUp() {
        dao = new DocumentDAOImpl();
        document = new Document("type", "fullname", new byte[] {1, 2, 3},
                "fromName", "fromDepartment", "toName",
                "toDepartment", "status", false, new Timestamp(new Date().getTime()));
    }

    @AfterClass
    public static void tearDown() {
        try {
            dao.close();
        }
        catch (Exception e) {
            fail();
        }
    }

    @Before
    @After
    public void clearDB() {
        try(Connection connection = ConnectionProvider.getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("delete from Documents");
        }
        catch (SQLException e) {
            fail();
        }
    }

    @Test
    public void insert() {
        dao.insert(document);
        Document expected = dao.getById(document.getId());
        assertEquals(expected, document);
    }

    @Test
    public void deleteById() {
        dao.insert(document);
        dao.deleteById(document.getId());
        Document expected = dao.getById(document.getId());
        assertNull(expected);
    }
}