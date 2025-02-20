package mg.ITU.generique;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mg.ITU.DAO.reflexion.Reflexion;
import mg.ITU.annotation.Colonne;
import mg.ITU.annotation.PrimaryKey;
import mg.ITU.annotation.Table;

public class Generic_DAO implements Interf_generic {

    private Util_DB udb;
    private Reflexion reflexion;
    private Query requete;

    public Query getRequete() { return requete;}

/** constructeur   */
    public Generic_DAO () throws Exception {
        if (this.getReflexion() == null || this.getUdb() == null || this.getRequete() == null) {
            this.reflexion = null;
            this.udb = null; 
            this.requete = null;  
        }
        this.udb = Util_DB.get_instance();
        this.reflexion = new Reflexion();
        this.requete = new Query();
        
    }

/** GENERIQUE DAO  */

    @Override
    public void save(Object origine) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = this.udb.get_connection();
            
            ArrayList <Object> objects = get_allWith_CRITERIA(connection, origine, null, null);
            if (objects.size() > 0) {
                throw new Exception("EFA AO IO ["+origine.getClass().getSimpleName()+"] IO ...");
            } else {
                this.is_annotedCLASS(origine);
            
                ArrayList <Field> fs = this.get_attrsANNOTED(origine);
                ArrayList <Field> field_annoted = new ArrayList <Field> ();
                for (Field field : fs) {
                    if (!field.isAnnotationPresent(PrimaryKey.class)) {
                        field_annoted.add(field);
                    }
                }fs.removeAll(fs);
                if (field_annoted.size() > 0) {
                    // field ef tsy mis primary key tsony ... 
                    this.getRequete().modifRequete_content(origine, null, null);
                    statement = connection.prepareStatement(requete._INSERTION(origine, field_annoted, this.reflexion));
                    
                    System.out.println("REQUETE : "+this.requete.getRequete()); // REQUETE
                    statement.execute(); // EXECUTE ...
                    field_annoted.removeAll(field_annoted);
                } else throw new Exception("PROBLEME D'ANNOTATION pour les attributs...");                                 
            
            }
        
            connection.commit();   // COMMIT a la fin ...
        } catch (SQLException e0) {
            /** catch params sans connection */
            try {
                if (connection != null) connection.rollback();
                if (statement != null) statement.close();
            } catch (SQLException e1) {
                throw e1;
            }
            throw e0;
        } finally {
            try {
                if (connection != null) connection.close();
                if (statement != null) statement.close();
            } catch (SQLException e2) {
                throw e2;
            }
        }
    }

    @Override
    public void save(Connection connection, Object origine) throws Exception {

        PreparedStatement statement = null;
        try {
            ArrayList <Object> objects = get_allWith_CRITERIA(connection, origine, null, null);
            if (objects.size() > 0) {
                throw new Exception("EFA AO IO ["+origine.getClass().getSimpleName()+"] IO ...");
            } else {
                
                // this.is_annotedCLASS(origine);
            
                ArrayList <Field> fs = this.get_attrsANNOTED(origine);
                ArrayList <Field> field_annoted = new ArrayList <Field> ();
                for (Field field : fs) {
                    if (!field.isAnnotationPresent(PrimaryKey.class)) {
                        field_annoted.add(field);
                    }
                }fs.removeAll(fs);
                if (field_annoted.size() > 0) {
                    // field ef tsy mis primary key tsony ... 
                    this.getRequete().modifRequete_content(origine, null, null);
                    statement = connection.prepareStatement(requete._INSERTION(origine, field_annoted, this.reflexion));
                    
                    System.out.println("REQUETE : "+this.requete.getRequete());
                    statement.execute(); // EXECUTE ...
    
                    field_annoted.removeAll(field_annoted);
                } else throw new Exception("PROBLEME D'ANNOTATION pour les attributs...");                                 
            }

        } catch (SQLException e0) {
            /**catch parals avec connection  */
            try {
                if (connection != null) connection.rollback();
                if (statement != null) statement.close();
            } catch (SQLException e1) {
                throw e1;
            }
            throw e0;
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e2) {
                throw e2;
            }
        }
    
    }

    @Override
    public ArrayList<Object> get_all(Object origine, Integer offset, Integer nbr) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            this.is_annotedCLASS(origine);

            ArrayList <Field> fs = this.get_attrsANNOTED(origine);
            Field field_primaryKey = null;
            // maka colonne primary key ra misy
            for (Field field : fs) {
                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    field_primaryKey = field;
                    fs.removeAll(fs);
                    break;
                }
            }

            connection = this.udb.get_connection();
            this.getRequete().modifRequete_content(origine, offset, nbr);
            String table_name = origine.getClass().getAnnotation(Table.class).name();
            statement = connection.prepareStatement(requete._GET_ALL(table_name, field_primaryKey));
            
            System.out.println("REQUETE : "+this.requete.getRequete());
            ResultSet result = statement.executeQuery(); // EXECUTE ...

            return this.traitement_resultSet(origine, result);

        } catch (SQLException e0) {
            /** catch params sans connection */
            try {
                if (statement != null) statement.close();
            } catch (SQLException e1) {
                throw e1;
            }
            throw e0;
        } finally {
            try {
                if (connection != null) connection.close();
                if (statement != null) statement.close();
            } catch (SQLException e2) {
                throw e2;
            }
        }
    }

    @Override
    public ArrayList<Object> get_all(Connection connection, Object origine, Integer offset, Integer nbr)
            throws Exception {
        PreparedStatement statement = null;

        try {
            this.is_annotedCLASS(origine);

            ArrayList <Field> fs = this.get_attrsANNOTED(origine);
            Field field_primaryKey = null;
            // maka colonne primary key ra misy
            for (Field field : fs) {
                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    field_primaryKey = field;
                    fs.removeAll(fs);
                    break;
                }
            }

            this.getRequete().modifRequete_content(origine, offset, nbr);
            String table_name = origine.getClass().getAnnotation(Table.class).name();
            statement = connection.prepareStatement(requete._GET_ALL(table_name, field_primaryKey));
            
            System.out.println("REQUETE : "+this.requete.getRequete());
            ResultSet result = statement.executeQuery(); // EXECUTE ...

            return this.traitement_resultSet(origine, result);

        } catch (SQLException e0) {
            /**catch parals avec connection  */
            try {
                if (statement != null) statement.close();
            } catch (SQLException e1) {
                throw e1;
            }
            throw e0;
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e2) {
                throw e2;
            }
        } 
    }


    @Override
    public ArrayList<Object> get_allWith_criteriaINTERV(Object origine, Object[][] intervals, String[] col_names,
            Integer offset, Integer nbr) throws Exception {

        Connection connection = null;
        PreparedStatement statement = null;

        try {

            if (intervals != null && col_names != null
            && intervals.length > 0 && col_names.length > 0) {
                ArrayList <Field> attrs_annotedCOLONNE = this.get_attrsANNOTED(origine);
                
                isAllIntervals_Valid(origine, intervals, col_names, attrs_annotedCOLONNE); // thows s'il y a des erreurs ...
                this.is_annotedCLASS(origine); // throw pour les problemes d'annotation
                
                connection = this.udb.get_connection();
                this.getRequete().modifRequete_content(origine, offset, nbr);

                statement = connection.prepareStatement(requete._GET_ALLwithCriteriaIntervalle(intervals, col_names, attrs_annotedCOLONNE, this.reflexion));
                
                System.out.println("REQUETE : "+this.requete.getRequete());
                ResultSet result = statement.executeQuery(); // EXECUTE ...
                
                return this.traitement_resultSet(origine, result);
            }
            else return this.get_all(origine, offset, nbr);
            
        } catch (SQLException e0) {
            /** catch params sans connection */
            try {
                if (statement != null) statement.close();
            } catch (SQLException e1) {
                throw e1;
            }
            throw e0;
        } finally {
            try {
                if (connection != null) connection.close();
                if (statement != null) statement.close();
            } catch (SQLException e2) {
                throw e2;
            }
        }
    }


    @Override
    public ArrayList<Object> get_allWith_criteriaINTERV(Connection connection, Object origine, Object[][] intervals,
            String[] col_names, Integer offset, Integer nbr) throws Exception {
        
        PreparedStatement statement = null;

        try {

            if (intervals != null && col_names != null
            && intervals.length > 0 && col_names.length > 0) {
                ArrayList <Field> attrs_annotedCOLONNE = this.get_attrsANNOTED(origine);
                
                isAllIntervals_Valid(origine, intervals, col_names, attrs_annotedCOLONNE); // thows s'il y a des erreurs ...
                this.is_annotedCLASS(origine); // throw pour les problemes d'annotation
                
                this.getRequete().modifRequete_content(origine, offset, nbr);

                statement = connection.prepareStatement(requete._GET_ALLwithCriteriaIntervalle(intervals, col_names, attrs_annotedCOLONNE, this.reflexion));
                
                System.out.println("REQUETE : "+this.requete.getRequete());
                ResultSet result = statement.executeQuery(); // EXECUTE ...
                
                return this.traitement_resultSet(origine, result);
            }
            else return this.get_allWith_CRITERIA(connection, origine, offset, nbr);
            
        } catch (SQLException e0) {
            /**catch parals avec connection  */
            try {
                if (statement != null) statement.close();
            } catch (SQLException e1) {
                throw e1;
            }
            throw e0;
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e2) {
                throw e2;
            }
        } 
    }




    @Override
    public ArrayList<Object> get_allWith_CRITERIA(Object origine, Integer offset, Integer nbr) throws Exception {
        
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            this.is_annotedCLASS(origine);
            
            ArrayList <Field> fields_annoted = get_attrsANNOTED(origine);
            ArrayList <Object[]> list_cleValeur = fields_withValue(origine, fields_annoted);
            // maka colonne primary key ra misy
            Field field_primaryKey = null;
            for (Field field : fields_annoted) {
                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    field_primaryKey = field;
                    fields_annoted.removeAll(fields_annoted);
                    break;
                }
            }
            connection = this.udb.get_connection();
            this.getRequete().modifRequete_content(origine, offset, nbr);

            statement = connection.prepareStatement(requete._GET_ALLwithCRITERIA(origine, list_cleValeur, field_primaryKey));
            
            System.out.println("REQUETE : "+this.requete.getRequete());
            ResultSet result = statement.executeQuery(); // EXECUTE ...

            list_cleValeur.removeAll(list_cleValeur);
            return this.traitement_resultSet(origine, result);

        } catch (SQLException e0) {
            /** catch params sans connection */
            try {
                if (statement != null) statement.close();
            } catch (SQLException e1) {
                throw e1;
            }
            throw e0;
        } finally {
            try {
                if (connection != null) connection.close();
                if (statement != null) statement.close();
            } catch (SQLException e2) {
                throw e2;
            }
        }
        
    }



    @Override
    public ArrayList<Object> get_allWith_CRITERIA(Connection connection, Object origine, Integer offset, Integer nbr)
            throws Exception {

        PreparedStatement statement = null;

        try {
            this.is_annotedCLASS(origine);
            
            ArrayList <Field> fields_annoted = get_attrsANNOTED(origine);
            ArrayList <Object[]> list_cleValeur = fields_withValue(origine, fields_annoted);
            // maka colonne primary key ra misy
            Field field_primaryKey = null;
            for (Field field : fields_annoted) {
                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    field_primaryKey = field;
                    fields_annoted.removeAll(fields_annoted);
                    break;
                }
            }
            this.getRequete().modifRequete_content(origine, offset, nbr);

            statement = connection.prepareStatement(requete._GET_ALLwithCRITERIA(origine, list_cleValeur, field_primaryKey));
            
            System.out.println("REQUETE : "+this.requete.getRequete());
            ResultSet result = statement.executeQuery(); // EXECUTE ...

            list_cleValeur.removeAll(list_cleValeur);
            return this.traitement_resultSet(origine, result);
        } catch (SQLException e0) {
            /**catch parals avec connection  */
            try {
                if (statement != null) statement.close();
            } catch (SQLException e1) {
                throw e1;
            }
            throw e0;
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e2) {
                throw e2;
            }
        } 
        
    }
/** end  GENERIQUE DAO  */


/** private fonction   */

    private boolean isAllIntervals_Valid (Object origine, Object[][] intervalle, String[] nom_attr, ArrayList <Field> fields_annoted) throws Exception {

        try {
            String error = "";
            if (intervalle.length == nom_attr.length) {
                /// -> LES DEUS INTERVALLES SONT DE MEME TAILLE
                is_intervalle_memeTYPE(intervalle); // throws Exception 
                int i = 0;
                for (Object[] objects : intervalle) {

                    Class <?> interv_type = intervalle_difNULL(objects).getClass();
                    Class <?> object_type = reflexion.get_fieldClasse_byName(fields_annoted, nom_attr[i]).getType();
                    if (!interv_type.isAssignableFrom(object_type)) {
                        error += "L'INTERVALLE indice ["+i+"] A L'ATTR SPECIFIEE\n";
                    }

                    ++ i;
                }
            } else {
                error += "LES 2 INTERVALLES NE SONT PAS DE MEME TAILLE\n";
                error += "intervalle.length = "+intervalle.length+" et \n";
                error += "nom_attr.length = "+nom_attr.length+"  \n\n";
            }

            if (!error.equals("")) {
                throw new Exception(error);
            }
        } catch (Exception e) {
            // Auto-generated catch block
            throw e;
        }

        return false;
    }

    private Object intervalle_difNULL (Object[] intervalle) {
        Object interv = null;
        for (Object object : intervalle) {
            if (object != null) {
                interv = object;
                break;
            }
        }
        return interv;
    }
    private void is_intervalle_memeTYPE (Object[][] intervalle) throws Exception {

        int indice = 0;
        String error = "";
        for (Object[] objects : intervalle) {
            if (objects.length == 2) {
                // VERIFIE SI L'INTERVALLE EST DETAILLE 2 DIM ...
                if (objects[0] != null && objects[1] != null) {
                    // POUR L'INTERVALLE A 2 COTES ...
                    Class<?> [] classe_type = reflexion.get_paramsTYPE(objects);
                    if (! classe_type[0].isAssignableFrom(classe_type[1])) error += "L'INTERVALLE indice ["+indice+"] N'EST PAS DE MEME TYPE\n";
                }
                else if (objects[0] == null && objects[1] == null) error += "L'INTERVALLE indice ["+indice+"] NE PEUT PAS ETRE L'UNE EST L'AUTRE\n";
            } else {
                error += "NBR INTERVALLE indice ["+indice+"] != 2\n";
            }
            ++ indice;
        }

        if (!error.equals("")) {
            // VERIFIE SI L'ERREUR EXIST ...
            throw new Exception(error);
        } else error = null;
    }

    private ArrayList <Object[]> fields_withValue (Object object, ArrayList <Field> fields_annoted) throws Exception {
        
        // GET FIELDS ANNOTER ...
        ArrayList <Object[]> fields = new ArrayList <> ();

        for (Field field : fields_annoted) {

            String attr = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            Object value = reflexion.execute_METHODE(object, "get"+attr, null);
            if (value != null) {
                Colonne colonne = field.getAnnotation(Colonne.class);
                Object[] cle_valeur = new Object[2];
                cle_valeur[0] = colonne.nom(); // clé
                cle_valeur[1] = value; // valeur
                fields.add(cle_valeur);
            }
        }

        return fields;
    }


    private ArrayList <Object> traitement_resultSet (Object origine, ResultSet resultSet) throws Exception {
        ArrayList <Object> list_object = new ArrayList <> ();

        ArrayList <Field> annoted_field = this.get_attrsANNOTED(origine);
        try {
            while (resultSet.next()) {
                int i = 0;
                Object[] params = new Object[annoted_field.size()];
                for (Field field : annoted_field) {
                    Colonne colonne = field.getAnnotation(Colonne.class);
                    params[i] = resultSet.getObject(colonne.nom());
                
                    ++ i;
                }
                Class<?> [] type_params = this.getReflexion().get_paramsTYPE(params);
                Constructor<?> constructor = origine.getClass().getConstructor(type_params);
                Object object = constructor.newInstance(params);
                list_object.add(object);
            }

            return list_object;
        } catch (SQLException | NoSuchMethodException | SecurityException e) {
            throw e;
        }

    }

    private ArrayList <Field> get_attrsANNOTED (Object object) {
        // Attribut annoter colonne ...
        Field[] fields = object.getClass().getDeclaredFields();
        ArrayList <Field> fields_annoted = new ArrayList <Field> ();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Colonne.class)) {
                fields_annoted.add(field);
            }
        }
        return fields_annoted;
    }


    private void is_annotedCLASS (Object object) throws Exception {
        boolean annoted = false;
        if (object.getClass().isAnnotationPresent(Table.class)) {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Colonne.class)
                && !field.isAnnotationPresent(PrimaryKey.class)) {
                    annoted = true;
                    break;
                    // EFA MISY ATTRIBUT ANNOTER AO
                }
            }
        }
        // oni verifie maintenant si la classe est annoter ou pas
        // sinon mandefa EXCEPTION 
        if (!annoted) {
            throw new Exception("PROBLEME D'ANNOTATION...\nANNOTER BIEN VOTRE CLASSE");
        } 
    }

/** GETTERS */
    public Reflexion getReflexion() { return reflexion; }
    public Util_DB getUdb() { return udb; }



}
