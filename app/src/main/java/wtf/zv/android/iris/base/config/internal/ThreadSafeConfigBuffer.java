package wtf.zv.android.iris.base.config.internal;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

@Qualifier
@Retention(RUNTIME)
public @interface ThreadSafeConfigBuffer {}