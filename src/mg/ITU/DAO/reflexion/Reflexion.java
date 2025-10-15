package mg.ITU.DAO.reflexion;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Reflexion {
    
    public static String get_nomClasse (Object object) {
        /** maka anaran le objet en parametre... */
        return object.getClass().getSimpleName();
    }

    
    public static Class <?>[] get_paramsTYPE (Object[] params) {
        /** maka type[] object  */
        Class <?>[] type_params = null;
        if (params != null && params.length > 0) {
            type_params = new Class <?>[params.length];
            int i = 0;
            for (Object param : params) {
                type_params[i] = param.getClass();
                ++ i; //  incrementer i
            }
        }
        
        return type_params;
    }

    
    public static Field get_fieldClasse_byName (ArrayList <Field> fields, String attr_name) {
        
        Field attr = null;
        for (Field field : fields) {
            if (field.getName().equals(attr_name)) {
                attr = field;
                break;
            }
        }
        return attr;
    }


    
    public static void executeMethod_NR (Object object, String methode_name, Object[] params, Class <?>[] type_params) throws ReflectiveOperationException {
        /** EXECUTE methode SANS RETOUR */
        
        Class <?> clazz = object.getClass();
        
        try {
            Method method = Reflexion.get_method(clazz, methode_name, type_params);
            method.invoke(object, params); // appeller la methode
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException | NoSuchMethodException e) {
            // Exception d'origine de l'objet mere
            throw e;
        }
    }

    public static void executeMethod_NR (Object object, String methode_name, Object[] params) throws ReflectiveOperationException {
        /** EXECUTE methode SANS RETOUR */
        
        Class <?> clazz = object.getClass();
        Class <?>[] type_params = Reflexion.get_paramsTYPE(params);

        try {
            Method method = Reflexion.get_method(clazz, methode_name, type_params);
            method.invoke(object, params); // appeller la methode
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException | NoSuchMethodException e) {
            // Exception d'origine de l'objet mere
            throw e;
        }
    }


    public static Object executeMethod_WR (Object object, String methode_name, Object[] params) throws ReflectiveOperationException  {
        Class <?> clazz = object.getClass();
        Class <?>[] type_params = Reflexion.get_paramsTYPE(params);

        try {
            Method method = Reflexion.get_method(clazz, methode_name, type_params);
            return method.invoke(object, params);
            // appeller la methode
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException | NoSuchMethodException e) {
            // Exception d'origine de l'objet mere
            throw e;
        }
        return null;
    }

    public static Object executeMethod_WR (Object object, String methode_name, Object[] params, Class<?>[] type_params) throws ReflectiveOperationException  {
        Class <?> clazz = object.getClass();

        try {
            Method method = Reflexion.get_method(clazz, methode_name, type_params);
            return method.invoke(object, params);
            // appeller la methode
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException | NoSuchMethodException e) {
            // Exception d'origine de l'objet mere
            throw e;
        }
        return null;
    }

    private static Method get_method (Class <?> mere, String nom_methode, Class <?>[] type_params) throws NoSuchMethodException, SecurityException {
       
        if (type_params == null || 
        (type_params != null && type_params.length == 0)) {
            return mere.getDeclaredMethod(nom_methode);
        } else {
            return mere.getDeclaredMethod(nom_methode, type_params);
        }
    }




}
