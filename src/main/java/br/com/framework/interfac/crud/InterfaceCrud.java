package br.com.framework.interfac.crud;
import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

import org.hibernate.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public interface InterfaceCrud<T> extends Serializable {
	
	
	void save(T obj) throws Exception;
	
	void persist(T obj) throws Exception;
	
	void saveOrUpdate(T obj) throws Exception;
	
	void update(T obj) throws Exception;
	
	void delete(T obj) throws Exception;
	
	T merge (T obj) throws Exception;
	
	List<T> findList(Class<T> objs) throws Exception;
		
	Object findById(Class<T> entidade, Long id) throws Exception;
	
	T findByPorId(Class<T> entidade, Long id) throws Exception;
	
	List<T> findListByQueryDinamica(String s) throws Exception;	
	
	//executar update com HQL
	void executeUpdateQueryDinamica(String s) throws Exception;
	
	//executar update com SQL
	void executeUpdateSQLDinamica(String s) throws Exception;
	
	// limpa a sessao do hibernate
	void clearSession() throws Exception;
	
	//retira um objeto da sessao do hibernate
	void evict (Object objs) throws Exception;
	
	Session getSession() throws Exception;
	
	List<?> getListSQLDinamica(String sql) throws Exception;
	
	// JDBC do spring
	JdbcTemplate getJdbcTemplate();	
	SimpleJdbcTemplate getSimpleJdbcTemplate();	
	SimpleJdbcInsert getSimpleJdbcInsert();
	SimpleJdbcTemplate getSimpleJdbcTemplateImp();
	
	Long totalRegistro(String table) throws Exception;
	
	Query obterQuery(String query) throws Exception;
	
	
	//carregamento dinamico com jsf e primefaces
	List<T> findListByQueryDinamica(String query, int iniciaNovoRegistro, int maximoResultado) throws Exception;
	
	
}
