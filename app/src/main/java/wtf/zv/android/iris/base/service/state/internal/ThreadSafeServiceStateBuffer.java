package wtf.zv.android.iris.base.service.state.internal;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

@Qualifier
@Retention(RUNTIME)
public @interface ThreadSafeServiceStateBuffer {}