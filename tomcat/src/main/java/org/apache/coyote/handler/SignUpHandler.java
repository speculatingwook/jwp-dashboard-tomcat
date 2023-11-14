package org.apache.coyote.handler;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.exception.badrequest.ExistUserException;
import nextstep.jwp.model.User;
import org.apache.coyote.query.QueryParams;
import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.response.HttpResponse;
import org.apache.coyote.response.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.Optional;

import static org.apache.coyote.response.ContentType.HTML;
import static org.apache.coyote.response.StatusCode.FOUND;

public class SignUpHandler {
    private static final Logger log = LoggerFactory.getLogger(SignUpHandler.class);

    private SignUpHandler() {
    }

    public static HttpResponse register(final HttpRequest request) throws URISyntaxException {
        final QueryParams queryParams = request.getQueryParams();

        final String account = queryParams.getValueFromKey("account");
        final String password = queryParams.getValueFromKey("password");
        final String email = queryParams.getValueFromKey("email");

        checkAlreadyExistUser(account);

        final User user = new User(account, password, email);

        InMemoryUserRepository.save(user);

        final String userInformation = user.toString();
        log.info("회원가입 성공! : {}", userInformation);

        return HttpResponse.of(FOUND, HTML, Location.from("/index.html"));
    }

    private static void checkAlreadyExistUser(String account) {
        final Optional<User> foundUser = InMemoryUserRepository.findByAccount(account);
        if (foundUser.isPresent()) {
            throw new ExistUserException();
        }
    }

}
