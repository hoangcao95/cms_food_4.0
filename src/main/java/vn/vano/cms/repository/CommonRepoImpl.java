package vn.vano.cms.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.vano.cms.bean.QueryBean;
import vn.yotel.admin.jpa.SysParam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class CommonRepoImpl extends BaseRepo implements CommonRepo {
    private final Logger LOG = LoggerFactory.getLogger(CommonRepoImpl.class);

    @PersistenceContext
    EntityManager em;

    public boolean isExistTable(String tblName) {
        try {
            Query query = em.createNativeQuery("SELECT 1 FROM " + tblName);
            List lst = query.getResultList();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void createTable(String tblName, String coreTableName) throws Exception {
        try {
            String sql = "CREATE TABLE " + tblName + " LIKE " + coreTableName;
            Query query = em.createNativeQuery(sql);
            query.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public <T> int create(String transId, T bean) {
        int count = -1;
        try {
            QueryBean queryBean = getInsertQuery(transId, bean, false);
            count = executeUpdate(transId, queryBean.getSql(), queryBean.getListValue());
        } catch (Exception e) {
            LOG.error("", e);
        }

        return count;
    }

    @Override
    public <T> int update(String transId, T bean, String whereClause, Object[] params, boolean withField, String... fields) {
        int count = -1;
        try {
            QueryBean queryBean = getUpdateQuery(transId, bean, whereClause, params, withField, fields);
            count = executeUpdate(transId, queryBean.getSql(), queryBean.getListValue());
        } catch (Exception e) {
            LOG.error("", e);
        }

        return count;
    }

    @Override
    public <T> int update(String transId, T bean, String whereClause, Object[] params, boolean withField, boolean withNull, String... fields) {
        int count = -1;
        try {
            QueryBean queryBean = getUpdateQuery(transId, bean, whereClause, params, withField, withNull, fields);
            count = executeUpdate(transId, queryBean.getSql(), queryBean.getListValue());
        } catch (Exception e) {
            LOG.error("", e);
        }

        return count;
    }

    @Override
    public <T> int delete(String transId, T bean, String whereClause, Object[] params) {
        int count = -1;
        try {
            QueryBean queryBean = getDeleteQuery(transId, bean, whereClause, params);
            count = executeUpdate(transId, queryBean.getSql(), queryBean.getListValue());
        } catch (Exception ex) {
            LOG.error(transId + "::" + ex.getMessage(), ex);
        }

        return count;
    }

    @Override
    public <T> List<T> findById(String transId, T bean, Long id) {
        try {
            QueryBean queryBean = getRecordById(transId, bean, id);
            List<T> lst = executeQuery(transId, queryBean.getSql(), queryBean.getListValue());
            return lst;
        } catch (Exception ex) {
            LOG.error(transId + "::" + ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public <T> List<T> findAll(String transId, T bean) {
        try {
            QueryBean queryBean = getRecordAll(transId, bean);
            List<T> lst = executeQuery(transId, queryBean.getSql(), queryBean.getListValue());
            return lst;
        } catch (Exception ex) {
            LOG.error(transId + "::" + ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public <T> List<T> findByClause(String transId, T bean, String whereClause, Object[] params) {
        try {
            QueryBean queryBean = getRecordByParam(transId, bean, whereClause, params);
            List<T> lst = executeQuery(transId, queryBean.getSql(), queryBean.getListValue());
            return lst;
        } catch (Exception ex) {
            LOG.error(transId + "::" + ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public <T> List<T> findBySQL(String transId, String sql, Object[] params) throws Exception {
        try {
            QueryBean queryBean = getRecordBySql(transId, sql, params);
            List<T> lst = executeQuery(transId, queryBean.getSql(), queryBean.getListValue());
            return lst;
        } catch (Exception ex) {
            LOG.error(transId + "::" + ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public <T> int executeUpdate(String transId, String sql, List<Object> params) throws Exception {
        long time1 = System.currentTimeMillis();
        int result = -1;
        try {
            Query query = em.createNativeQuery(sql);
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i+1, params.get(i));
            }
            result = query.executeUpdate();
            return result;
        } catch (Exception ex) {
            throw ex;
        } finally {
            LOG.info(transId + "::EXECUTE::TIME=" + (System.currentTimeMillis() - time1) + "|UPDATED=" + result);
        }
    }

    @Override
    public <T> List<T> executeQuery(String transId, String sql, List<Object> params) throws Exception {
        long time1 = System.currentTimeMillis();
        List lstResult = null;
        try {
            Query query = em.createNativeQuery(sql);
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i+1, params.get(i));
            }
            lstResult = query.getResultList();
            return lstResult;
        } catch (Exception ex) {
            throw ex;
        } finally {
            LOG.info(transId + "::EXECUTE::TIME=" + (System.currentTimeMillis() - time1) + "|RESULT=" + (lstResult==null ? 0 : lstResult.size()));
        }
    }

    //<editor-fold desc="FUNCTION JPA" defaultstate="collapsed">
    @Override
    public <S extends SysParam> S save(S entity) {
        return null;
    }

    @Override
    public SysParam findOne(Long aLong) {
        return null;
    }

    @Override
    public boolean exists(Long aLong) {
        return false;
    }

    @Override
    public List<SysParam> findAll() {
        return null;
    }

    @Override
    public List<SysParam> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<SysParam> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<SysParam> findAll(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void delete(SysParam entity) {

    }

    @Override
    public void delete(Iterable<? extends SysParam> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void flush() {

    }

    @Override
    public void deleteInBatch(Iterable<SysParam> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public SysParam getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends SysParam> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends SysParam> List<S> save(Iterable<S> entities) {
        return null;
    }
    //</editor-fold>
}
