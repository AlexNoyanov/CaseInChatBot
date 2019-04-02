package dbService.dao;

import dbService.entity.PdfTemplate;

public interface PdfTemplateDAO extends DAO<PdfTemplate> {
    PdfTemplate getByName(String fullName);
    public void deleteByName(String fullName);
}
