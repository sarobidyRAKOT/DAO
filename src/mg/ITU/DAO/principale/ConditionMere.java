package mg.ITU.DAO.principale;

public class ConditionMere <T> {

    public String valueToString (T value) {
        return value.toString();
    }


    public String getFormatInQuery (T value) {
        String string = null;
        if ( // ICI C'EST POUR LES TYPES PRIMITIFS ***
            value instanceof Integer   || 
            value instanceof Double    || 
            value instanceof Long      ||
            value instanceof Boolean   || 
            value instanceof Float     || 
            // value instanceof Short     ||
            // value instanceof Character ||
            value instanceof Byte    
        ) {
            string = this.valueToString(value);
        } else {
            string = "'"+this.valueToString(value)+"'";
        }
        return string;
    }

}
