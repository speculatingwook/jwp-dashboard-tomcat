package nextstep.jwp.controller;

import nextstep.jwp.Response.ResponseBody;
import nextstep.jwp.Response.ResponseHeader;
import nextstep.jwp.service.LoginService;
import nextstep.jwp.util.ResourceFinder;

public class RegisterController {
    private LoginService loginService = new LoginService();
    private ResourceFinder resourceFinder = new ResourceFinder();
    private ResponseHeader responseHeader = new ResponseHeader();
    private ResponseBody responseBody = new ResponseBody();
}
