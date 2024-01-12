package org.woen.team18742.Modules.Manager;

import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Tools.ToolTelemetry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Module {

}
