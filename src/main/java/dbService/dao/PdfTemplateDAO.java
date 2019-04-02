package dbService.dao;

import dbService.entity.PdfTemplate;

public interface PdfTemplateDAO extends DAO<PdfTemplate> {
    PdfTemplate getByName(String name);
    public void deleteByName(String name);
}
