package dbService.dao;

import dbService.entity.Document;
import dbService.entity.PdfTemplate;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentDAOImpl_GetAndUpdateTest {
    static DocumentDAO dao;
    static Document document;
    static Document document2;
    static Document document3;

    @BeforeClass
    public static void setUp() {
        dao = new DocumentDAOImpl();
        document = new Document("type", "fullname", new byte[] {1, 2, 3},
                "fromName", "fromDewgepartment", "toName",
                "toDepartment", "status", false, new Timestamp(new Date().getTime()));

        document2 = new Document("ty23pe", "fu123llname", new byte[] {3,6,2, 2, 3},
                "fromName", "fromDepartment", "toNsdfdsfame",
                "toDepartment", "sta4tus", true, new Timestamp(1));

        document3 = new Document("type", "fulln42ame", new byte[] {11, 22, 3},
                "fromN2ame", "fromDepartment", "toName",
                "toDepartdsment", "stat23423us", true, new Timestamp(3));
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
        dao.insert(document);
        dao.insert(document2);
        dao.insert(document3);
    }

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
    public void getById() {
        Document got = dao.getById(document.getId());
        assertEquals(got, document);
    }

    @Test
    public void getByName() {
        Document got = dao.getByName(document.getFullName());
        assertEquals(got, document);
    }

    @Test
    public void getAllByType() {
        List<Document> expected = new ArrayList<>();
        expected.add(document);
        expected.add(document3);
        List<Document> got = dao.getAllByType("type");
        assertEquals(got, expected);
    }

    @Test
    public void getAllByDeparture() {
        List<Document> expected = new ArrayList<>();
        expected.add(document2);
        expected.add(document3);
        List<Document> got = dao.getAllByDeparture("fromDepartment");
        assertEquals(got, expected);
    }

    @Test
    public void getAllByArrival() {
        List<Document> expected = new ArrayList<>();
        expected.add(document);
        expected.add(document2);
        List<Document> got = dao.getAllByArrival("toDepartment");
        assertEquals(got, expected);
    }

    @Test
    public void getAllBySender() {
        List<Document> expected = new ArrayList<>();
        expected.add(document);
        expected.add(document2);
        List<Document> got = dao.getAllBySender("fromName");
        assertEquals(got, expected);
    }

    @Test
    public void getAllByRecipient() {
        List<Document> expected = new ArrayList<>();
        expected.add(document);
        expected.add(document3);
        List<Document> got = dao.getAllByRecipient("toName");
        assertEquals(got, expected);
    }

    @Test
    public void getAllByStatus() {
        List<Document> expected = new ArrayList<>();
        expected.add(document2);
        expected.add(document3);
        List<Document> got = dao.getAllByStatus(true);
        assertEquals(got, expected);
    }

    @Test
    public void getAllAfterDate() {
        List<Document> expected = new ArrayList<>();
        expected.add(document);
        List<Document> got = dao.getAllAfterDate(new Timestamp(1000));
        assertEquals(got, expected);
    }

    @Test
    public void getAll() {
        List<Document> expected = new ArrayList<>();
        expected.add(document);
        expected.add(document2);
        expected.add(document3);
        List<Document> got = dao.getAll();
        assertEquals(got, expected);
    }

    @Test
    public void update() {
        document.setFullName("322");
        dao.update(document);
        Document got = dao.getById(document.getId());
        assertEquals(document.getFullName(), got.getFullName());
        document.setFullName("fullname");
    }
}