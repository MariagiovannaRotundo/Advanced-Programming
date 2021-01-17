package esercizio2;

import java.lang.annotation.*;

//annotation for class

//TYPE : for the Class
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XMLable {
}

