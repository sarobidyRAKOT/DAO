package mg.ITU.generique;

import java.lang.reflect.Field;
import java.util.ArrayList;

import mg.ITU.DAO.reflexion.Reflexion;
import mg.ITU.annotation.Colonne;
import mg.ITU.annotation.Table;

public class Query {

    protected Object origine;
    private String requete;


    // Paginatoin 
    protected int OFFSET;
    protected int NBR;
    protected boolean PAGINATION = false;

/** construteur REQUETE */
    // public Requete (Object object, Integer offset, Integer nbr) throws Exception {
    //     if (object != null) {
    //         if (offset != null && nbr != null && nbr > 0 && offset >= 0) {
    //             this.PAGINATION = true;
    //             OFFSET = offset;
    //             NBR = nbr;
    //         }
    //         this.setOrigine(object);
    //     } else throw new Exception("OBJECT NULL");
    // }

    public Query () { /*consructeur par defaut */}

    public void modifRequete_content (Object object, Integer offset, Integer nbr) throws Exception {
        this.requete = null;
        if (object != null) {
            if (offset != null && nbr != null && nbr > 0 && offset >= 0) {
                this.PAGINATION = true;
                this.OFFSET = offset;
                this.NBR = nbr;
            }
            this.setOrigine(object);
        } else throw new Exception("OBJECT NULL");
    }




/** format REQUETE  
 * @throws Exception */

    public String _INSERTION (Object object, ArrayList <Field> fields_annoted, Reflexion reflexion) throws Exception {
        String nom_table = object.getClass().getAnnotation(Table.class).name();
        this.requete = "INSERT INTO "+nom_table+" (";
        
        for (int i = 0; i < fields_annoted.size() - 1; i++) {
            Colonne colonne = fields_annoted.get(i).getAnnotation(Colonne.class);
            this.requete += colonne.nom()+", ";
        }
        Colonne colonne = fields_annoted.get(fields_annoted.size()-1).getAnnotation(Colonne.class);
        this.requete += colonne.nom();
        this.requete += ") VALUES (";
        for (int i = 0; i < fields_annoted.size() - 1; i++) {
            Object obj = reflexion.execute_METHODE(object, "get"+fields_annoted.get(i).getName(), null);
            this.requete += traite_object(obj)+", ";
        }
        Object obj = reflexion.execute_METHODE(object, "get"+fields_annoted.get(fields_annoted.size()-1).getName(), null);
        this.requete += traite_object(obj)+")";

        return this.getRequete();
    }


    public String _GET_ALL (String nom_table, Field field_primaryKey) {
        this.requete = "SELECT * FROM "+nom_table;
        if (enabled_pagination()) {
            this.requete += format_pagination(field_primaryKey);
        }
        return this.getRequete();
    }

    public String _GET_ALLwithCRITERIA (Object object, ArrayList <Object[]> attr_withValue, Field field_primaryKey) {

        String table_name = origine.getClass().getAnnotation(Table.class).name();
        this.requete = "SELECT * FROM "+table_name;
        if (attr_withValue.size() > 0) {
            this.requete += " WHERE ";
            for (int i = 0; i < attr_withValue.size()-1; i++) {
                String nom_colonne =  (String) attr_withValue.get(i)[0];
                String value = traite_object(attr_withValue.get(i)[1]);
                this.requete += nom_colonne +" = "+value + " AND ";
            }   
            String nom_colonne =  (String) attr_withValue.get(attr_withValue.size()-1)[0];
            String value = traite_object(attr_withValue.get(attr_withValue.size()-1)[1]);
            this.requete += nom_colonne +" = "+value;
        }

        if (enabled_pagination()) {
            this.requete += format_pagination(field_primaryKey);
        }

        return this.getRequete();
    }


    public String _GET_ALLwithCriteriaIntervalle (Object[][] intervals, String[] nom_attrs, ArrayList <Field> attrs_annoteedColonne, Reflexion reflexion) {

        String table_name = origine.getClass().getAnnotation(Table.class).name();
        this.requete = "SELECT * FROM "+table_name+" WHERE ";

        Colonne colonne = null;
        for (int i = 0; i < intervals.length-1; i++) {
            
            // get annotation de la colonne pour chaque nom attrs 
            colonne = (reflexion.get_fieldClasse_byName(attrs_annoteedColonne, nom_attrs[i])).getAnnotation(Colonne.class);

            if (intervals[i][0] != null && intervals[i][1] == null) {
                this.requete += colonne.nom()+" > "+this.traite_object(intervals[i][0]);
            } else if (intervals[i][0] == null && intervals[i][1] != null) {
                this.requete += colonne.nom()+" < "+this.traite_object(intervals[i][1]);
            } else {
                // tsy maintsy samy ts null daoly ...
                this.requete += colonne.nom()+" > "+this.traite_object(intervals[i][0])+" and ";
                this.requete += colonne.nom()+" < "+this.traite_object(intervals[i][1]);
            }
            this.requete += " AND ";
        }

        colonne = (reflexion.get_fieldClasse_byName(attrs_annoteedColonne, nom_attrs[intervals.length-1])).getAnnotation(Colonne.class);

        if (intervals[intervals.length-1][0] != null && intervals[intervals.length-1][1] == null) {
            this.requete += colonne.nom()+" > "+this.traite_object(intervals[intervals.length-1][0]);
        } else if (intervals[intervals.length-1][0] == null && intervals[intervals.length-1][1] != null) {
            this.requete += colonne.nom()+" < "+this.traite_object(intervals[intervals.length-1][1]);
        } else {
            // tsy maintsy samy ts null daoly ...
            this.requete += colonne.nom()+" > "+this.traite_object(intervals[intervals.length-1][0])+" and ";
            this.requete += colonne.nom()+" < "+this.traite_object(intervals[intervals.length-1][1]);
        }

        if (enabled_pagination()) format_pagination(null);
        
        return this.getRequete();
    }

/** function PRIVATE */

    private String format_pagination (Field attr_primaryKey) {
        String req = "";
        if (attr_primaryKey != null) {
            String colonnePK = attr_primaryKey.getAnnotation(Colonne.class).nom();
            req = " ORDER BY "+colonnePK;
        }
        req += " LIMIT "+this.NBR+ " OFFSET "+this.OFFSET;
        return req;
    }
    private String traite_object (Object object) {
        String req = null;
        if (object instanceof Integer || object instanceof Double || object instanceof Long ||
        object instanceof Boolean || object instanceof Float || object instanceof Byte) {
            req = object.toString();
        } else {
            req = "'"+object.toString()+"'";
        }
        return req;
    }

/** PUBLIC FONCTION  */

    public boolean enabled_pagination () {
        if (PAGINATION) {
            boolean enable = false;
            if (this.getNBR() != null && this.getOFFSET() != null) {
                enable = true;
            }
            return enable;
        } else {
            return false;
        }
    }
    

    private void setOrigine (Object object) { this.origine = object; }
    public String getRequete() { return requete; }
    
    public Integer getNBR() {
        if (this.NBR <= 0) {
            return null;
        } else {
            return this.NBR;
        }
    }
    public Integer getOFFSET() {
        if (OFFSET < 0) {
            return null;
        } else {
            return this.OFFSET;
        }
    }

}
