package dao;

import dao.DaoImpl;

public class DaoFactory {
	
	private static DaoFactory instance = new DaoFactory();
	
	private DaoFactory() {		
	}
	
	public static DaoFactory getInstance() {
		return instance;
	}
	
	public Dao getDao() {
		return new DaoImpl();
	}
}
