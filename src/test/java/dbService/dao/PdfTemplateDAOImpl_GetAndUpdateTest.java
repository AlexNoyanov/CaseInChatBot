package dbService.dao;

import dbService.entity.PdfTemplate;
import org.junit.*;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PdfTemplateDAOImpl_GetAndUpdateTest {
    static PdfTemplateDAO dao;
    static PdfTemplate pdfTemplate;
    static PdfTemplate pdfTemplate2;
    static PdfTemplate pdfTemplate3;

    @BeforeClass
    public static void setUp() {
        dao = new PdfTemplateDAOImpl();
        pdfTemplate = new PdfTemplate("123", new byte[] {1, 2, 3}, new byte[] {1, 3, 3});
        pdfTemplate2 = new PdfTemplate("ergere", new byte[] {2, 3, 5, 6, 6}, new byte[] {4, 22, 111});
        pdfTemplate3 = new PdfTemplate("er34t343", new byte[] {1, 6, 0}, new byte[] {1, 66});
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
    public void addToDB() {
        dao.insert(pdfTemplate);
        dao.insert(pdfTemplate2);
        dao.insert(pdfTemplate3);
    }

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
    public void getById() {
        PdfTemplate got = dao.getById(pdfTemplate.getId());
        assertEquals(got, pdfTemplate);
    }

    @Test
    public void getByName() {
        PdfTemplate got = dao.getByName(pdfTemplate2.getFullName());
        assertEquals(got, pdfTemplate2);
    }

    @Test
    public void getAll() {
        List<PdfTemplate> expected = new ArrayList<>();
        expected.add(pdfTemplate);
        expected.add(pdfTemplate2);
        expected.add(pdfTemplate3);
        List<PdfTemplate> got = dao.getAll();
        assertEquals(got, expected);
    }

    @Test
    public void update() {
        pdfTemplate.setFullName("322");
        dao.update(pdfTemplate);
        PdfTemplate got = dao.getById(pdfTemplate.getId());
        assertEquals(pdfTemplate.getFullName(), got.getFullName());
        pdfTemplate.setFullName("123");
    }
}