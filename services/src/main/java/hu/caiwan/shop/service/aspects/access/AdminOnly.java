package hu.caiwan.shop.service.aspects.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Endpoint method security with aspectJ
 * Mark an endpoint with so to make accessible by administrator only  
 * @author caiwan
 */
@Target(ElementType.METHOD)
public @interface AdminOnly {

}
