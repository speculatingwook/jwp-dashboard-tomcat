package nextstep.jwp.member.application;


import nextstep.jwp.member.domain.UserRepository;
import nextstep.jwp.member.domain.model.User;
import nextstep.jwp.member.infrastructure.repository.UserRepositoryImpl;

public class MemberService {
    private final UserRepository userRepository;

    public MemberService() {
        this.userRepository = new UserRepositoryImpl();
    }

    public String getMemberLoginInfo(String account, String password) {
        User user = userRepository.findByAccount(account);
        if(user.checkPassword(password)){
            return user.toString();
        }
        return "WrongPassWord";
    }

    public String saveMember(String account, String email,String password) {
        User user = new User(account,email,password);
        user = userRepository.save(user);
        return user.toString();
    }
}
