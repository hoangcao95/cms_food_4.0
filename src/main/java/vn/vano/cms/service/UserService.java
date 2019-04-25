package vn.vano.cms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.yotel.admin.jpa.AuthUser;
import vn.yotel.commons.bo.GenericBo;

import java.util.List;

public interface UserService extends GenericBo<AuthUser, Integer> {

    Page<AuthUser> findByParam(String userName, String email, Pageable pageable);
    List<Integer> findRoleByUser(Integer userId);
}
