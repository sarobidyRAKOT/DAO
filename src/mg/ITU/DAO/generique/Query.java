package mg.ITU.DAO.generique;

import java.util.ArrayList;

public class Query {



/** construteur REQUETE */
    public Query () { /*consructeur par defaut */}





/** format REQUETE  
 * @throws Exception */

    public static String INSERTION (String table_name, ArrayList <Object[]> attrValues) throws Exception {
        
        String requete;        
        requete = "INSERT INTO "+table_name+"\r\n   (";
        
        if (attrValues != null && attrValues.size() > 0) {
            
            for (Object[] objects : attrValues) requete += (String) objects[0]+", ";
            requete += ")\r\nVALUES \r\n(";
            for (Object[] objects : attrValues) {
                String value = Query.get_valueColumn(objects[1]);
                requete += value+", ";
            } 
        } else throw new Exception("PAS DE VALEUR POUR L'INSERTION");
        requete += ")";
        requete = requete.replace(", )", ")");

        return requete;
    }


    public static String GET_ALL (String table_name, Pagination paging) {
        String requete = "SELECT * FROM "+table_name+"\r\n???";
        if (paging != null) {
            requete += pagination_query (paging);
        }
        return requete;
    }

    public static String GET_ALLwithCRITERIA (String table_name, ArrayList <Object[]> attrValues, Pagination paging) {

        String requete;
        // String table_name = mere.getClass().getAnnotation(Table.class).value();
        requete = "SELECT * FROM "+table_name;
        if (attrValues != null && attrValues.size() > 0) {
            requete += "\r\nWHERE ";
            for (int i = 0; i < attrValues.size(); i++) {
                String value = Query.get_valueColumn(attrValues.get(i)[1]);
                requete += (String) attrValues.get(i)[0] +" = "+value + " AND ";
            }   
            requete += "...";
            requete = requete.replace("AND ...", "\r\n???");
        } else requete += "\r\n???";

        if (paging != null) {
            requete += pagination_query (paging);
        }

        return requete;
    }


public static String GET_ALLwithCRITERIAIntervalle(
        String table_name, 
        ArrayList<Object[]> attrValues, 
        Object[][] intervals, 
        String[] colonnes_name, 
        Pagination paging) {

    String requete = Query.GET_ALLwithCRITERIA(table_name, attrValues, paging);

    if (colonnes_name != null && intervals != null && colonnes_name.length == intervals.length) {

        // Ajout du bloc WHERE ou AND selon la présence existante
        if (!requete.contains("WHERE")) {
            requete = requete.replace("???", "WHERE \r\n???");
        } else {
            requete = requete.replace("???", "AND \r\n???");
        }

        // Construction des critères d'intervalle
        StringBuilder conditions = new StringBuilder();
        for (int i = 0; i < intervals.length; i++) {
            Object val1 = intervals[i][0];
            Object val2 = intervals[i][1];
            String column = colonnes_name[i];

            if (val1 != null && val2 == null) conditions.append(Query.get_valueColumn(val1)).append(" <= ").append(column);
            else if (val1 == null && val2 != null) conditions.append(column).append(" <= ").append(Query.get_valueColumn(val2));
            else if (val1 != null && val2 != null) {
                conditions.append(column).append(" BETWEEN ").append(Query.get_valueColumn(val1)).append(" AND ").append(Query.get_valueColumn(val2));
            } else continue;
            conditions.append(" AND ");
        }

        // Suppression du dernier " AND " s'il existe
        int len = conditions.length();
        if (len >= 5) {
            conditions.setLength(len - 5);
        }

        // Insertion des conditions dans la requête
        requete = requete.replace("\r\n???", conditions.toString() + "***");

        // Nettoyage final
        if (requete.contains("WHERE ***")) {
            requete = requete.replace("\r\nWHERE ***", "");
        } else {
            requete = requete.replace("***", "");
        }
    }

    return requete;
}


/** function PRIVATE */

    private static String pagination_query (Pagination paging) {
        String req = "";
        // if (attr_primaryKey != null) {
        //     String colonnePK = attr_primaryKey.getAnnotation(Colonne.class).value();
        //     req = " ORDER BY "+colonnePK;
        // }
        req += "\r\nLIMIT "+paging.LIMIT+ " OFFSET "+paging.OFFSET;
        return req;
    }


    private static String get_valueColumn (Object object) {
        String req = null;
        if (object instanceof Integer || object instanceof Double || object instanceof Long ||
        object instanceof Boolean || object instanceof Float || object instanceof Byte) {
            req = object.toString();
        } else {
            req = "'"+object.toString()+"'";
        }
        return req;
    }

}
