package vn.vano.cms.repository;

import java.util.List;

public interface IBaseAction {
    <T> int create(String transId, T bean);
    <T> int update(String transId, T bean, String whereClause, Object[] params, boolean withField, String... fields);
    <T> int update(String transId, T bean, String whereClause, Object[] params, boolean withField, boolean withNull, String... fields);
    <T> int delete(String transId, T bean, String whereClause, Object[] params);
    <T> List<T> findById(String transId, T bean, Long id);
    <T> List<T> findAll(String transId, T bean);
    <T> List<T> findByClause(String transId, T bean, String whereClause, Object[] params);
    <T> List<T> findBySQL(String transId, String sql, Object[] params) throws Exception;
}
