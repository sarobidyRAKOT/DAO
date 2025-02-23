package mg.ITU.DAO.generique;

import java.sql.Connection;
import java.util.ArrayList;

public interface Generic_DAO {

/** INTERFACE GENERIC_DAO ... */  
    public ArrayList<Object> get_allWith_CRITERIA (Object origine, String apresWhere, Pagination paging) throws Exception;
    public int save (Object origine) throws Exception;
    public ArrayList<Object> get_all (Object origine, String apresWhere, Pagination paging) throws Exception;
    public ArrayList<Object> get_allWith_criteriaINTERV(Object origine, String apresWhere, Pagination paging, Object[][] intervals, String[] col_names) throws Exception;

/** fonction variante avec un parametre Connection */
    public ArrayList<Object> get_allWith_CRITERIA (Connection conn, Object origine, String apresWhere, Pagination paging) throws Exception;  
    public int save (Connection conn, Object origine) throws Exception;
    public ArrayList<Object> get_all(Connection conn, Object origine, String apresWhere, Pagination paging) throws Exception;
    public ArrayList<Object> get_allWith_criteriaINTERV(Connection conn, Object origine, String apresWhere, Pagination paging, Object[][] intervals, String[] col_names) throws Exception;

}
