package org.apache.coyote.handler;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.exception.notfound.UserNotFoundException;
import nextstep.jwp.model.User;
import org.apache.coyote.processor.QueryProcessor;
import org.apache.coyote.request.QueryParams;
import org.apache.coyote.response.ContentType;
import org.apache.coyote.response.HttpResponse;
import org.apache.coyote.response.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

import static org.apache.coyote.response.ContentType.*;
import static org.apache.coyote.response.StatusCode.*;

public class LoginHandler {


    private static final Logger log = LoggerFactory.getLogger(LoginHandler.class);

    private LoginHandler() {
    }

    public static HttpResponse login(final String requestBody) throws URISyntaxException {
        final QueryParams queryParams = QueryParams.from(requestBody);

        final String account = queryParams.getValueFromKey("account");
        final String password = queryParams.getValueFromKey("password");

        final User user = InMemoryUserRepository.findByAccount(account)
                .orElseThrow(UserNotFoundException::new);


        if (!user.checkPassword(password)) {
            return HttpResponse.of(FOUND, HTML, "/401.html");
        }

        final String userInformation = user.toString();
        log.info(userInformation);
        return HttpResponse.of(FOUND, HTML, "/401.html");
    }
}
