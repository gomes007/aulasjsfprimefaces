package br.com.project.filter;


import java.io.IOException;
import java.io.Serializable;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.filter.DelegatingFilterProxy;

import br.com.framework.hibernate.session.HibernateUtil;
import br.com.framework.utils.UtilFramework;
import br.com.project.listener.ContextLoaderListenerCaixakiUtils;
import br.com.project.model.classes.Entidade;
import jakarta.servlet.annotation.WebFilter;


@WebFilter(filterName="conexaoFilter")
public class FilterOpenSessionInView extends DelegatingFilterProxy implements Serializable{

	private static final long serialVersionUID = 1L;

	private static SessionFactory sf;
	
	//executa apenas uma vez qdo a aplicacao esta sendo iniciada
	@Override
	protected void initFilterBean() throws ServletException {
		
		sf = HibernateUtil.getSessionFactory();
		
	}
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

		//JDBC Spring (responsavel commit e rollback)
		BasicDataSource springDataSource = (BasicDataSource) ContextLoaderListenerCaixakiUtils.getBean("springDataSource");
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(springDataSource);
		TransactionStatus status = transactionManager.getTransaction(def);

		try {

			servletRequest.setCharacterEncoding("UTF-8");

			// captura usuario que faz a operacao
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletResponse response = (HttpServletResponse) servletResponse;
			HttpSession sessao = request.getSession();
			Entidade userLogadoSessao = (Entidade) sessao.getAttribute("userLogadoSessao");

			if (userLogadoSessao != null) {
				UtilFramework.getThreadLocal().set(userLogadoSessao.getEnt_codigo());
			}

			sf.getCurrentSession().beginTransaction();
			
			//antes de executar acao
			chain.doFilter(servletRequest, servletResponse);

			//apos executar acao(resposta)
			transactionManager.commit(status);

			if (sf.getCurrentSession().getTransaction().isActive()) {
				sf.getCurrentSession().flush();
				sf.getCurrentSession().getTransaction().commit();
			}

			if (sf.getCurrentSession().isOpen()) {
				sf.getCurrentSession().close();
			}

			servletResponse.setCharacterEncoding("UTF-8");
			servletResponse.setContentType("text/html; charset=UTF-8");

		} catch (Exception e) {

			transactionManager.rollback(status);

			e.printStackTrace();

			if (sf.getCurrentSession().getTransaction().isActive()) {
				sf.getCurrentSession().getTransaction().rollback();
			}
			if (sf.getCurrentSession().isOpen()) {
				sf.getCurrentSession().close();
			}
		} finally {
			if (sf.getCurrentSession().isOpen()) {
				if (sf.getCurrentSession().beginTransaction().isActive()) {
					sf.getCurrentSession().flush();
					sf.getCurrentSession().clear();
				}
				if (sf.getCurrentSession().isOpen()) {
					sf.getCurrentSession().close();
				}
			}

		}
	}
	

}
