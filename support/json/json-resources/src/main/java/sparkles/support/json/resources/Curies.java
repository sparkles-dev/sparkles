package sparkles.support.json.resources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation specifying an array of curies to be used in defining link relations.
 *
 *
 * @link https://stackoverflow.com/questions/28154998/can-anyone-provide-a-good-explanation-of-curies-and-how-to-use-them
 */
@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Curies {

    /**
     * Annotation grouping a list of {@link Curies} for convenience/readability
     * @return an array of curies
     */
    Curie[] value() default {};

}
