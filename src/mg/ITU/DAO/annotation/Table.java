package mg.ITU.DAO.annotation;

import java.lang.annotation.*;

/**
 * annotatoin table
 * Nom_table
 */

@Inherited
@Documented
@Target (ElementType.TYPE)
@Retention (RetentionPolicy.RUNTIME)
public @interface Table {
    String value();
}