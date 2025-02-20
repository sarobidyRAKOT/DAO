package mg.ITU.DAO.annotation;

import java.lang.annotation.*;

@Target (ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented

public @interface Colonne {
    String value ();
}
