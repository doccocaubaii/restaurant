package vn.softdreams.easypos.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.easypos.repository.PrintTemplateRepositoryCustom;

import javax.persistence.EntityManager;

public class PrintTemplateRepositoryImpl implements PrintTemplateRepositoryCustom {

    @Autowired
    private EntityManager entityManager;
}
