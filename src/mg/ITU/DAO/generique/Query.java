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


    public static String GET_ALLwithCRITERIAIntervalle (String table_name, ArrayList <Object[]> attrValues, Object[][] intervals, String[] colonnes_name, Pagination paging) {

        // String table_name = origine.getClass().getAnnotation(Table.class).value();
        String requete = Query.GET_ALLwithCRITERIA(table_name, attrValues, paging);

        if (colonnes_name != null && intervals != null) {
            if (!requete.contains("WHERE")) requete = requete.replace("???", "WHERE \r\n???"); 
            else requete = requete.replace("???", "AND \r\n???");
            
            StringBuilder sb = new StringBuilder(requete);
            int index = requete.indexOf("\r\n???");
            sb.insert(index, "rakoto");

            String req = "";
            int i = 0;
            for (Object[] v : intervals) {
                if (v[0] != null && v[1] == null) req += Query.get_valueColumn(v[0])+" <= "+colonnes_name[i];
                else if (v[0] == null && v[1] != null)  req += colonnes_name[i]+" <= "+Query.get_valueColumn(v[1]);
                else req += colonnes_name[i]+" BETWEEN "+Query.get_valueColumn(v[0])+" AND "+Query.get_valueColumn(v[1]);
                req += " AND ";
                ++ i;
            }
            req += ",";
            req = req.replace(" AND ,", "");
            requete = sb.toString();
            requete = requete.replace("rakoto", req);
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
