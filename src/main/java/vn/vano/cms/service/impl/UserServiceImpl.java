package vn.vano.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.vano.cms.repository.UserServiceRepo;
import vn.vano.cms.service.UserService;
import vn.yotel.admin.jpa.AuthUser;
import vn.yotel.commons.bo.impl.GenericBoImpl;

import java.util.List;

@Service(value = "userService")
public class UserServiceImpl extends GenericBoImpl<AuthUser, Integer> implements UserService {

    @Autowired
    UserServiceRepo userServiceRepo;

    @Override
    public Page<AuthUser> findByParam(String userName, String email, Pageable pageable) {
        return getDAO().findByParam(userName, email, pageable);
    }

    @Override
    public List<Integer> findRoleByUser(Integer userId) {
        return getDAO().findRoleByUser(userId);
    }

    @Override
    public UserServiceRepo getDAO() {
        return userServiceRepo;
    }
}
