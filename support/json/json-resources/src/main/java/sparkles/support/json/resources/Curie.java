package sparkles.support.json.resources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation specifying a CURIE for use with list. As defined by W3C in
 * <a href="https://www.w3.org/TR/2010/NOTE-curie-20101216/">CURIE Syntax 1.0</a>. Note that in
 * the context of HAL the only substitution done to the template URI of a curie is the
 * <code>{rel}</code> place holder.
 */
@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Curie {

    /**
     * CURIE href template e.g. "http://docs.my.site/{rel}"
     * @return href a reference to the elaborated documentation for a given resource
     */
    String href() default "";

    /**
     * CURIE name used to reference the CURIE in {@link Link} annotations
     * e.g. "mysite"
     * @return the name of the curie
     */
    String prefix() default "";

}
