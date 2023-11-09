package org.apache.coyote.http11;

public class ControllerMapper {
    public Object mappingController(String url) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String domain = url.replace("/", "").split(".")[0];

        String controllerClassName = url.substring(0, 1).toUpperCase() + domain.substring(1) + "Controller";
        Class<?> controllerClass = Class.forName("nextstep.jwp.controller." + controllerClassName);
        return controllerClass.newInstance();
    }
}
