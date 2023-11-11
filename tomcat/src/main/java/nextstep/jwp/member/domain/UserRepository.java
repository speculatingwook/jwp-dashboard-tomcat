package nextstep.jwp.member.domain;

import nextstep.jwp.member.domain.model.User;

public interface UserRepository {
    User save(User user);
    User findByAccount(String account);
}
