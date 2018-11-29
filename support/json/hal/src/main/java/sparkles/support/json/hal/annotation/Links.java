package sparkles.support.json.hal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a {@link Link} instance or collection for inclusion in the _links of the resource.
 */
@Target({ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Links {

    /**
     * Relation name - if not set the property name will be used.
     * @return name of the relation represented by the link.
     */
    String value() default "";

    /**
     * CURIE prefix - if not set then no CURIE will be associated.
     * @return name of the CURIE intended to be used with this link.
     */
    String curie() default "";
}
