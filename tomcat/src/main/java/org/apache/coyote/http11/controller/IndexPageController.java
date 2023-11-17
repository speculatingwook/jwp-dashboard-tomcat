package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.annotation.Controller;
import org.apache.coyote.http11.annotation.GetMapping;

@Controller
public class IndexPageController {

	@GetMapping("/")
	public String showIndexPage() {
		return "hello world !!";
	}

}
