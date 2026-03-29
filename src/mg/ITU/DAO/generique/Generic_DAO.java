package mg.ITU.DAO.generique;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import mg.ITU.DAO.principale.IntervalCondition;

public interface Generic_DAO {

/** INTERFACE GENERIC_DAO ... */  
    public ArrayList<Object> get_allWith_CRITERIA (Object origine, String apresWhere, Pagination paging) throws Exception;

    /**
     * 
     * @param origine
     * @return
     * @throws Exception
     * le save ne retourn pas l'objet mais le nombre ajouter et 
     * modifie le parametre Id de l'origine
     */
    public int save (Object origine) throws Exception;
    public ArrayList<Object> get_all (Object origine, String apresWhere, Pagination paging) throws Exception;
    public ArrayList<Object> get_allWith_criteriaINTERV(Connection con, Object origine, IntervalCondition <?> [] iConditions, String apresWhere, Pagination paging) throws Exception;

/** fonction variante avec un parametre Connection */
    public ArrayList<Object> get_allWith_CRITERIA (Connection conn, Object origine, String apresWhere, Pagination paging) throws Exception;  
    public int save (Connection conn, Object origine) throws Exception;
    public ArrayList<Object> get_all(Connection conn, Object origine, String apresWhere, Pagination paging) throws Exception;
    // public ArrayList<Object> get_allWith_criteriaINTERV(Object origine, String apresWhere, Pagination paging, Object[][] intervals, String[] col_names) throws Exception;

/** 
 * FONCTION EXECUTE REQUETE
 * Regle à suivre:
 *  1. REQUETE UPDATE:
 *      Ex:
 *        
 * 
*/
    // public String ExecuteRequete (Connection conn, Map<String, String> parametres, String requete) throws Exception;

/** FONCTION À REQUETE */
    public int updateObjectById (Connection conn, Object origine) throws Exception;
    /**
     * @return String en format JSON
     * @throws Exception
     * 
     * Format requete: -> valeur basique du requete
     *      SELECT
     *          ????
     *      FROM table_name
     *      WHERE
     *      JOIN ...
     */
    public String get (Connection conn, Map<String, String> colAleas, String requete) throws Exception;
}
