package dbService.dao;

public class DAOContext implements AutoCloseable {
    private DocumentDAO documentDAO;
    private RequestDAO requestDAO;
    private PdfTemplateDAO pdfTemplateDAO;
    private EmployeeDAO employeeDAO;

    public DAOContext() {
        documentDAO = new DocumentDAOImpl();
        requestDAO = new RequestDAOImpl();
        pdfTemplateDAO = new PdfTemplateDAOImpl();
        employeeDAO = new EmployeeDAOImpl();
    }

    @Override
    public void close() throws Exception {
        documentDAO.close();
        requestDAO.close();
        pdfTemplateDAO.close();
        employeeDAO.close();
    }

    public DocumentDAO getDocumentDAO() {
        return documentDAO;
    }

    public RequestDAO getRequestDAO() {
        return requestDAO;
    }

    public PdfTemplateDAO getPdfTemplateDAO() {
        return pdfTemplateDAO;
    }

    public EmployeeDAO getEmployeeDAO() {
        return employeeDAO;
    }
}
