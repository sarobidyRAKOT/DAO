package mg.ITU.DAO.generique;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import mg.ITU.DAO.annotation.Table;
import mg.ITU.DAO.principale.IntervalCondition;

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

    public static String GET_ALLwithCRITERIA (String table_name, ArrayList <Object[]> attrValues, String apresWhere, Pagination paging) {

        String requete = "SELECT\n"+
        "   *\n"+
        "FROM "+table_name;
        if (attrValues != null && attrValues.size() > 0) {
            requete += "\nWHERE "+attrValues.stream().map(objs -> objs[0].toString()+" = "+Query.get_valueColumn(objs[1])).collect(Collectors.joining("\nAND "));
        }

        if (apresWhere != null && !apresWhere.isEmpty()) {
            requete += "\n"+apresWhere;
        }

        if (paging != null) {
            requete += "\n"+paging.toString();
        }

        return requete;
    }


    public static <T> String GET_ALLwithCRITERIAIntervalle (String table_name, ArrayList<Object[]> attrValues, IntervalCondition <?>[] iConditions, String apresWhere, Pagination paging) {

        String requete = Query.GET_ALLwithCRITERIA(table_name, attrValues, null, null);


        if (iConditions != null && iConditions.length > 0) {
            if (requete.contains("WHERE")) {
                requete += "\nAND ";
            } else requete += "WHERE ";
            requete += Arrays.stream(iConditions).map(iC -> iC.getQuery()).collect(Collectors.joining("\nAND "));
        }

        if (apresWhere != null && !apresWhere.isEmpty()) {
            requete += "\n"+apresWhere;
        }
        if (paging != null) {
            requete += "\n"+paging.toString();
        }

        return requete;
    }

    public static String UPDATE (Object origine, ArrayList <Object[]> attrValues, Object[] primaryKeyCle_valeur) throws Exception {

        String requete;       
        
        String table_name = origine.getClass().getAnnotation(Table.class).value();

        requete = "UPDATE "+table_name+"\n" +
                  "SET\n";
        
        if (attrValues != null && attrValues.size() > 0) {
            
            requete += attrValues.stream().map(object -> "     "+object[0]
                                .toString()+" = "+Query.get_valueColumn(object[1]))
                                .collect(Collectors.joining(",\n"));
            requete += "\nWHERE "+primaryKeyCle_valeur[0] +" = "+Query.get_valueColumn(primaryKeyCle_valeur[1])+";"; 
            
        } else throw new Exception("PAS DE VALEUR POUR UPDATE");

        return requete;
    }

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


    public static void printQuery (String table_name, String requete) {
        System.out.println("\nRequete FROM ["+table_name+"] ******:\n"+requete+"\nFin ******");
    }
}
