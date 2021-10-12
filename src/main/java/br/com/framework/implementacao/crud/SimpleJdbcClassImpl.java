package br.com.framework.implementacao.crud;


import java.io.Serializable;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implemetar Serializable para SimpleJdbcCall
 * @author Paulo
 *
 */
@Component
@Transactional
public class SimpleJdbcClassImpl extends SimpleJdbcCall implements Serializable {

	private static final long serialVersionUID = 1L;

	public SimpleJdbcClassImpl(DataSource dataSource) {
		super(dataSource);
		
	}

}
