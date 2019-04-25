package vn.vano.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import vn.vano.cms.repository.CommonRepo;
import vn.vano.cms.repository.IBaseAction;

import javax.transaction.Transactional;
import java.util.List;

@Service("commonService")
public class CommonService implements IBaseAction{

    @Autowired
    CommonRepo commonRepo;

    @Modifying
    public boolean isExistTable(String tblName) {
        return commonRepo.isExistTable(tblName);
    }

    @Transactional
    @Modifying
    public void createTable(String tblName, String coreTableName) throws Exception {
        commonRepo.createTable(tblName, coreTableName);
    }

    @Transactional
    @Modifying
    @Override
    public <T> int create(String transId, T bean) {
        return commonRepo.create(transId, bean);
    }

    @Transactional
    @Modifying
    @Override
    public <T> int update(String transId, T bean, String whereClause, Object[] params, boolean withField, String... fields) {
        return commonRepo.update(transId, bean, whereClause, params, withField, fields);
    }

    @Transactional
    @Modifying
    @Override
    public <T> int update(String transId, T bean, String whereClause, Object[] params, boolean withField, boolean withNull, String... fields) {
        return commonRepo.update(transId, bean, whereClause, params, withField, withNull, fields);
    }

    @Transactional
    @Modifying
    @Override
    public <T> int delete(String transId, T bean, String whereClause, Object[] params) {
        return commonRepo.delete(transId, bean, whereClause, params);
    }

    @Override
    public <T> List<T> findById(String transId, T bean, Long id) {
        return commonRepo.findById(transId, bean, id);
    }

    @Override
    public <T> List<T> findAll(String transId, T bean) {
        return commonRepo.findAll(transId, bean);
    }

    @Override
    public <T> List<T> findByClause(String transId, T bean, String whereClause, Object[] params) {
        return commonRepo.findByClause(transId, bean, whereClause, params);
    }

    @Override
    public <T> List<T> findBySQL(String transId, String sql, Object[] params) throws Exception {
        return commonRepo.findBySQL(transId, sql, params);
    }
}
