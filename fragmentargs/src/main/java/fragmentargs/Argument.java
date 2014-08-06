package fragmentargs;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by patrick on 8/3/14.
 */
@Retention(CLASS)
@Target(FIELD)
public @interface Argument {
    String value();

}
