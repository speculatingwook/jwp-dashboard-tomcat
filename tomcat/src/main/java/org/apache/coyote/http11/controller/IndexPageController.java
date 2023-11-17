package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.annotation.Controller;
import org.apache.coyote.http11.annotation.RequestMapping;

@Controller
public class IndexPageController {

	@RequestMapping("/")
	public String showIndexPage() {
		return "hello world !!";
	}

}
