package mg.ITU.DAO.generique;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import mg.ITU.DAO.reflexion.*;
import mg.ITU.DAO.annotation.*;
import mg.ITU.DAO.principale.*;

public class Generic implements Generic_DAO {

    private static Util_DB udb;


/** constructeur   */
    public Generic () throws Exception {
        // if (this.getReflexion() == null || this.getUdb() == null || this.getRequete() == null) {
        //     this.reflexion = null;
        //     this.udb = null; 
        //     this.requete = null;  
        // }
        if (Generic.udb == null) {
            Generic.udb = Util_DB.get_Instance();        
        }
    }

// /** GENERIQUE DAO  */


    @Override
    public ArrayList<Object> get_allWith_CRITERIA(Object origine, String apresWhere, Pagination paging) throws Exception {
        
        Connection conn = null;
        ArrayList <Object> list = new ArrayList <> ();
        // ArrayList <Field> f_primaryKeys = new ArrayList <> ();
        PreparedStatement statement = null;
        ResultSet rSet = null;

        
        is_classeValid(origine);
        ArrayList <Field> fields_annoted = get_attrsANNOTED(origine);
        ArrayList <Object[]> listscle_valeur = fieldsValue(origine, fields_annoted);
        
        // maka COLONNE primary key ra misy
        // for (Field field : fields_annoted) {
        //     if (field.isAnnotationPresent(PrimaryKey.class)) f_primaryKeys.add(field);
        // }

        try {
            conn = Generic.udb.connect();
            String table_name = origine.getClass().getAnnotation(Table.class).value();

            String requete = Query.GET_ALLwithCRITERIA(table_name, listscle_valeur, paging);

            if (apresWhere != null && !apresWhere.isEmpty()) {
                requete = requete.replace("???", apresWhere);
            } else requete = requete.replace("???", "");

            System.out.println(requete);
            statement = conn.prepareStatement(requete);
            
            rSet = statement.executeQuery(); // EXECUTE ...
            list = Generic.traitement_resultSet(origine, rSet, fields_annoted);

        } catch (SQLException e0) {
            e0.printStackTrace();
            // throw e0;
        } catch (Throwable e) {
            // Probleme d'invocation
            throw (Exception) e;
        } finally {

            fields_annoted.removeAll(fields_annoted);
            listscle_valeur.removeAll(listscle_valeur);
            try {
                if (conn != null) conn.close();
                if (statement != null) statement.close();
                if (rSet != null) rSet.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        return list;

        
    }


    @Override
    public ArrayList<Object> get_allWith_CRITERIA(Connection conn, Object origine, String apresWhere, Pagination paging) throws Exception {
        
        
        ArrayList <Object> list = new ArrayList <> ();
        // ArrayList <Field> f_primaryKeys = new ArrayList <> ();
        PreparedStatement statement = null;
        ResultSet rSet = null;

        
        is_classeValid(origine); // VERIFICATION originie SI VALIDE
        ArrayList <Field> fields_annoted = Generic.get_attrsANNOTED(origine);
        ArrayList <Object[]> listscle_valeur = Generic.fieldsValue(origine, fields_annoted);
        
        // maka COLONNE primary key ra misy
        // for (Field field : fields_annoted) {
        //     if (field.isAnnotationPresent(PrimaryKey.class)) f_primaryKeys.add(field);
        // }

        try {
            String table_name = origine.getClass().getAnnotation(Table.class).value();
            String requete = Query.GET_ALLwithCRITERIA(table_name, listscle_valeur, paging);

            if (apresWhere != null && !apresWhere.isEmpty()) {
                requete = requete.replace("???", apresWhere);
            } else requete = requete.replace("???", "");

            System.out.println(requete);
            statement = conn.prepareStatement(requete);
            
            rSet = statement.executeQuery(); // EXECUTE ...
            list = Generic.traitement_resultSet(origine, rSet, fields_annoted);

        } catch (SQLException e0) {
            e0.printStackTrace();
            // throw e0;
        } catch (Throwable e) {
            // Probleme d'invocation
            throw (Exception) e;
        } finally {

            fields_annoted.removeAll(fields_annoted);
            listscle_valeur.removeAll(listscle_valeur);
            try {
                // if (conn != null) conn.close();
                if (statement != null) statement.close();
                if (rSet != null) rSet.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        return list;
        
    }



    @Override
    public int save (Object origine) throws Exception {

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rSet = null;


        is_classeValid(origine); // VERIFICATION originie SI VALIDE
        ArrayList <Field> fields_annoted = Generic.get_attrsANNOTED(origine);
        ArrayList <Object[]> listscle_valeur = Generic.fieldsValue(origine, fields_annoted);
        

        try {
            conn = Generic.udb.connect();
            conn.setAutoCommit(false);

            String table_name = origine.getClass().getAnnotation(Table.class).value();
            String requete = Query.INSERTION (table_name, listscle_valeur);

            System.out.println(requete);
            statement = conn.prepareStatement(requete, PreparedStatement.RETURN_GENERATED_KEYS);
            int nbr_saved = statement.executeUpdate(); // EXECUTE UPDATE ...
            
            rSet = statement.getGeneratedKeys();
            Generic.set_ID(origine, rSet, fields_annoted);

            conn.commit();

            return nbr_saved;
        } catch (SQLException e0) {
            e0.printStackTrace();
            // throw e0;
        } catch (Throwable e) {
            // Probleme d'invocation
            throw (Exception) e;
        } finally {

            fields_annoted.removeAll(fields_annoted);
            listscle_valeur.removeAll(listscle_valeur);
            try {
                if (conn != null) conn.close();
                if (statement != null) statement.close();
                if (rSet != null) rSet.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }

        return 0;
    }

    @Override
    public int save (Connection conn, Object origine) throws Exception {

        PreparedStatement statement = null;
        ResultSet rSet = null;


        is_classeValid(origine); // VERIFICATION originie SI VALIDE
        ArrayList <Field> fields_annoted = Generic.get_attrsANNOTED(origine);
        ArrayList <Object[]> listscle_valeur = Generic.fieldsValue(origine, fields_annoted);
        

        try {

            String table_name = origine.getClass().getAnnotation(Table.class).value();
            String requete = Query.INSERTION (table_name, listscle_valeur);

            System.out.println(requete);
            statement = conn.prepareStatement(requete, PreparedStatement.RETURN_GENERATED_KEYS);
            int nbr_saved = statement.executeUpdate(); // EXECUTE UPDATE ...
            
            rSet = statement.getGeneratedKeys();
            Generic.set_ID(origine, rSet, fields_annoted);

            return nbr_saved;
        } catch (SQLException e0) {
            e0.printStackTrace();
            // throw e0;
        } catch (Throwable e) {
            // Probleme d'invocation
            throw (Exception) e;
        } finally {

            fields_annoted.removeAll(fields_annoted);
            listscle_valeur.removeAll(listscle_valeur);
            try {
                // if (conn != null) conn.close();
                if (statement != null) statement.close();
                if (rSet != null) rSet.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public ArrayList<Object> get_all (Object origine, String apresWhere, Pagination paging) throws Exception {
        
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rSet = null;
        ArrayList <Object> list = new ArrayList <> ();


        Generic.is_classeValid (origine); // VERIFIE SI LA CLASSE ORIGINE EST BIEN ANNOTER
        ArrayList <Field> fields_annoted = Generic.get_attrsANNOTED(origine);

        try {

            conn = Generic.udb.connect();
            
            String table_name = origine.getClass().getAnnotation(Table.class).value();
            String requete = Query.GET_ALL(table_name, paging);

            if (apresWhere != null && !apresWhere.isEmpty()) {
                requete = requete.replace("???", apresWhere);
            } else requete = requete.replace("\r\n???", "");

            System.out.println(requete);
            statement = conn.prepareStatement(requete);
            
            rSet = statement.executeQuery(); // EXECUTE ...
            list = Generic.traitement_resultSet(origine, rSet, fields_annoted);

        } catch (SQLException e0) {
            e0.printStackTrace();
            // throw e0;
        } catch (Throwable e) {
            // Probleme d'invocation
            throw (Exception) e;
        } finally {

            fields_annoted.removeAll(fields_annoted);
            try {
                if (conn != null) conn.close();
                if (statement != null) statement.close();
                if (rSet != null) rSet.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }

        return list;
    }

    @Override
    public ArrayList<Object> get_all(Connection conn, Object origine, String apresWhere, Pagination paging) throws Exception {

        PreparedStatement statement = null;
        ResultSet rSet = null;
        ArrayList <Object> list = new ArrayList <> ();


        Generic.is_classeValid (origine); // VERIFIE SI LA CLASSE ORIGINE EST BIEN ANNOTER
        ArrayList <Field> fields_annoted = Generic.get_attrsANNOTED(origine);

        try {
            
            String table_name = origine.getClass().getAnnotation(Table.class).value();
            String requete = Query.GET_ALL(table_name, paging);

            if (apresWhere != null && !apresWhere.isEmpty()) {
                requete = requete.replace("???", apresWhere);
            } else requete = requete.replace("\r\n???", "");

            System.out.println(requete);
            statement = conn.prepareStatement(requete);
            
            rSet = statement.executeQuery(); // EXECUTE ...
            list = Generic.traitement_resultSet(origine, rSet, fields_annoted);

        } catch (SQLException e0) {
            e0.printStackTrace();
            // throw e0;
        } catch (Throwable e) {
            // Probleme d'invocation
            throw (Exception) e;
        } finally {

            fields_annoted.removeAll(fields_annoted);
            try {
                // if (conn != null) conn.close();
                if (statement != null) statement.close();
                if (rSet != null) rSet.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }

        return list;
    }


    @Override
    public ArrayList<Object> get_allWith_criteriaINTERV (Object origine, String apresWhere, Pagination paging, Object[][] intervals, String[] col_names) throws Exception {
       
       
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rSet = null;
        ArrayList <Object> list = new ArrayList <> ();


        is_classeValid(origine); // VERIFICATION originie SI VALIDE
        Generic.validIntervalle(intervals, col_names); // INTERVALLE VALID
        ArrayList <Field> fields_annoted = Generic.get_attrsANNOTED(origine);
        ArrayList <Object[]> listscle_valeur = Generic.fieldsValue(origine, fields_annoted);
        
        try {
            conn = Generic.udb.connect();
            
            String table_name = origine.getClass().getAnnotation(Table.class).value();
            String requete = Query.GET_ALLwithCRITERIAIntervalle(table_name, listscle_valeur, intervals, col_names, paging);
            
            if (apresWhere != null && !apresWhere.isEmpty()) {
                requete = requete.replace("???", apresWhere);
            } else requete = requete.replace("\r\n???", "");
            
            
            System.out.println(requete);
            statement = conn.prepareStatement(requete);
            
            rSet = statement.executeQuery(); // EXECUTE ...
            list = Generic.traitement_resultSet(origine, rSet, fields_annoted);
            

        } catch (SQLException e0) {
            e0.printStackTrace();
            // throw e0;
        } catch (Throwable e) {
            // Probleme d'invocation
            throw (Exception) e;
        } finally {

            listscle_valeur.removeAll(listscle_valeur);
            fields_annoted.removeAll(fields_annoted);
            try {
                if (conn != null) conn.close();
                if (statement != null) statement.close();
                if (rSet != null) rSet.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        return list;
    }


    @Override
    public ArrayList<Object> get_allWith_criteriaINTERV(Connection conn, Object origine, String apresWhere, Pagination paging, 
            Object[][] intervals, String[] col_names) throws Exception {

        PreparedStatement statement = null;
        ResultSet rSet = null;
        ArrayList <Object> list = new ArrayList <> ();


        is_classeValid(origine); // VERIFICATION originie SI VALIDE
        Generic.validIntervalle(intervals, col_names); // INTERVALLE VALID
        ArrayList <Field> fields_annoted = Generic.get_attrsANNOTED(origine);
        ArrayList <Object[]> listscle_valeur = Generic.fieldsValue(origine, fields_annoted);
        
        try {
            
            String table_name = origine.getClass().getAnnotation(Table.class).value();
            String requete = Query.GET_ALLwithCRITERIAIntervalle(table_name, listscle_valeur, intervals, col_names, paging);
            
            if (apresWhere != null && !apresWhere.isEmpty()) {
                requete = requete.replace("???", apresWhere);
            } else requete = requete.replace("\r\n???", "");
            
            
            System.out.println(requete);
            statement = conn.prepareStatement(requete);
            
            rSet = statement.executeQuery(); // EXECUTE ...
            list = Generic.traitement_resultSet(origine, rSet, fields_annoted);
            

        } catch (SQLException e0) {
            e0.printStackTrace();
            // throw e0;
        } catch (Throwable e) {
            // Probleme d'invocation
            throw (Exception) e;
        } finally {

            listscle_valeur.removeAll(listscle_valeur);
            fields_annoted.removeAll(fields_annoted);
            try {
                if (statement != null) statement.close();
                if (rSet != null) rSet.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        return list;
    }





    private static void validIntervalle (Object[][] intervals, String[] colonnes_name) throws Exception {

        if (intervals != null && colonnes_name != null) {
            if (intervals.length == colonnes_name.length) {
                for (Object[] inter : intervals) {
                    if (inter.length != 2) throw new Exception("INCOMPATIBLE INTERVALLE, 2 OBJETS SEULEMENT");  
                }
            } else throw new Exception("LE NOMBRE D'INTERVAL NE CONVIENT PAS AU NOMBRE DE COLONNE");
        } 

        // return true;
    }

    @SuppressWarnings("unlikely-arg-type")
    private static void set_ID (Object mere, ResultSet resultSet, ArrayList <Field> annoted_field) throws Throwable {
        // ArrayList <Object> list_object = new ArrayList <> ();

        ArrayList <Field> fields_PK  = new ArrayList<>();
        for (Field field : annoted_field) {
            if (field.isAnnotationPresent(PrimaryKey.class)) fields_PK.add(field);
        }

        try {
            while (resultSet != null && resultSet.next()) {
                // APPEL DE SETTER ...
                Generic.setter(mere, fields_PK, resultSet);
            }
        } catch (SQLException e ) {
            e.printStackTrace();
        } catch (InvocationTargetException | InstantiationException e) {
            throw e.getCause();
        } finally{
            fields_PK.remove(fields_PK);
        }

    }


    private static ArrayList <Object> traitement_resultSet (Object mere, ResultSet resultSet, ArrayList <Field> annoted_field) throws Throwable {
        ArrayList <Object> list_object = new ArrayList <> ();

        try {
            while (resultSet != null && resultSet.next()) {
                Constructor<?> constructor = mere.getClass().getConstructor();
                Object object = constructor.newInstance();
                Generic.setter(object, annoted_field, resultSet); // APPEL SETTER ..
                list_object.add(object);
            }

        } catch (SQLException e ) {
            e.printStackTrace();
        } catch (InvocationTargetException | InstantiationException e) {
            
            throw e.getCause();
        }

        return list_object;
    }

    private static void setter (Object mere, ArrayList <Field> fields, ResultSet resultSet) throws SQLException, ReflectiveOperationException {
        int i = 0;
        Object[] params = new Object[fields.size()];
        for (Field field : fields) {
            params[i] = resultSet.getString(field.getAnnotation(Colonne.class).value()); 
            ++ i;
        }

        for (int j = 0; j < params.length; j ++) {
            String attr = fields.get(j).getName().substring(0, 1).toUpperCase() + fields.get(j).getName().substring(1);

            // APPEL SETTERS de type `String`
            Reflexion.executeMethod_NR(mere, "set"+attr, new Object[] { params[j] }, new Class<?>[] { String.class });
        }
    }

    private static ArrayList <Object[]> fieldsValue (Object mere, ArrayList <Field> fields_annoted) throws ReflectiveOperationException {
        
        // GET FIELDS ANNOTER ...
        ArrayList <Object[]> fields = new ArrayList <> ();

        for (Field field : fields_annoted) {
            // RENDRE MAJUSCLE AU PREMIER LETTRE ... 
            String attr = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);

            Object value = Reflexion.executeMethod_WR (mere, "get"+attr, null);
            if (value != null) {
                Colonne colonne = field.getAnnotation(Colonne.class);
                Object[] cle_valeur = new Object[] {
                    colonne.value(), value
                };
                fields.add(cle_valeur);
            }
        
        }

        return fields;
    }

    private static ArrayList <Field> get_attrsANNOTED (Object mere) {
        /**
         * GET ATTRIBUT ANNOTER DANS LA CLASSE MERE ...
         */
        Field[] fields = mere.getClass().getDeclaredFields();
        ArrayList <Field> fields_annoted = new ArrayList <Field> ();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Colonne.class)) {
                fields_annoted.add(field);
            }
        }

        return fields_annoted;
    }

    private static void is_classeValid (Object mere) throws ExecutionException {


        /**
         * 1. VERIFICATION SI LA CLASSE EST ANNOTE @Table
         * 2. LA VALEUR DE L'ANNOTATION N'EST PAS VIDE ...
         * 3. POUR CHAQUE ATTRIBUT ANNOTER, On verifie si la valeur de l'annotation n'EST PAS VIDE 
         */
        int nombreAttr_Annoter = 0;
        String err = "PROBLEME D'ANNOTATION, ??? ET LA VALEUR DE L'ANNOTAION NE DOIS PAS ETRE VIDE";
        
        if (mere.getClass().isAnnotationPresent(Table.class)
            && !mere.getClass().getAnnotation(Table.class).value().trim().isEmpty()) {
            Field[] fields = mere.getClass().getDeclaredFields();
            for (Field field : fields) {
                // REF MISY ATTRIBUT ANNOTER DE EF METY
                if (field.isAnnotationPresent(Colonne.class) && field.getAnnotation(Colonne.class).value().trim().isEmpty()) {
                    throw new ExecutionException(err.replace("??? ET", "ATTR "+field.toString()), null);
                } else ++ nombreAttr_Annoter;
            }

            if (nombreAttr_Annoter <= 0) throw new ExecutionException("PROBLEME D'ANNOTAION, AUCUN ATTR ANNOTER `@Colonne`", null);
        } else {
            throw new ExecutionException(err.replace("???", "LA CLASSE "+mere.getClass().getName()+" DOIT ETRE ANNOTEE `@Table`"), null);
        }
    }


/** GETTERS */
    // public Reflexion getReflexion() { return reflexion; }
    public Util_DB getUdb() { return udb; }




}
