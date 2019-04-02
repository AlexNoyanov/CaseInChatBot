package dbService.dao;

import dbService.entity.PdfTemplate;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class PdfTemplateDAOImpl_InsertAndDeleteTest {
    static PdfTemplateDAO dao;
    static PdfTemplate pdfTemplate;

    @BeforeClass
    public static void setUp() {
        dao = new PdfTemplateDAOImpl();
        pdfTemplate = new PdfTemplate("123", new byte[] {1, 2, 3}, new byte[] {1, 3, 3});
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
            statement.execute("delete from PdfTemplates");
        }
        catch (SQLException e) {
            fail();
        }
    }

    @Test
    public void insert() {
        dao.insert(pdfTemplate);
        PdfTemplate expected = dao.getById(pdfTemplate.getId());
        assertEquals(expected, pdfTemplate);
    }

    @Test
    public void deleteById() {
        dao.insert(pdfTemplate);
        dao.deleteById(pdfTemplate.getId());
        PdfTemplate expected = dao.getById(pdfTemplate.getId());
        assertNull(expected);
    }

    @Test
    public void deleteByName() {
        dao.insert(pdfTemplate);
        dao.deleteByName(pdfTemplate.getFullName());
        PdfTemplate expected = dao.getByName(pdfTemplate.getFullName());
        assertNull(expected);
    }
}