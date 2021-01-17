package esercizio2;

import java.lang.annotation.*;

//annotation for fields

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XMLfield {
	String type();
	//optional 
	String name() default "";
}

