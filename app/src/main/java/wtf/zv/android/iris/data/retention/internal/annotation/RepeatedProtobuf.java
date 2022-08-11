package wtf.zv.android.iris.data.retention.internal.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Qualifier
@Retention(RUNTIME)
@Target(ElementType.TYPE_PARAMETER)
public @interface RepeatedProtobuf {

}
