package nextstep.jwp.member.infrastructure.repository;

import nextstep.jwp.member.domain.UserRepository;
import nextstep.jwp.member.domain.model.User;
import nextstep.jwp.member.infrastructure.db.InMemoryUserRepository;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public User save(User user) {
        InMemoryUserRepository.save(user);
        return user;
    }

    @Override
    public User findByAccount(String account) {
        return InMemoryUserRepository.findByAccount(account).orElseThrow(IllegalStateException::new);
    }
}
