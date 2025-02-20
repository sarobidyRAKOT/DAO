package mg.ITU.generique;

import java.sql.Connection;
import java.util.ArrayList;

public interface Interf_generic {

/** INTERFACE GENERIC_DAO ... */  
    public void save(Object origine) throws Exception;
    public ArrayList<Object> get_all(Object origine, Integer offset, Integer nbr) throws Exception;
    public ArrayList<Object> get_allWith_criteriaINTERV(Object origine, Object[][] intervals, String[] col_names, Integer offset, Integer nbr) throws Exception;
    public ArrayList<Object> get_allWith_CRITERIA(Object origine, Integer offset, Integer nbr) throws Exception;

/** fonction variante avec un parametre Connection */
    public void save(Connection connection, Object origine) throws Exception;
    public ArrayList<Object> get_all(Connection connection, Object origine, Integer offset, Integer nbr) throws Exception;
    public ArrayList<Object> get_allWith_criteriaINTERV(Connection connection, Object origine, Object[][] intervals, String[] col_names, Integer offset, Integer nbr) throws Exception;
    public ArrayList<Object> get_allWith_CRITERIA(Connection connection, Object origine, Integer offset, Integer nbr) throws Exception;  

}
