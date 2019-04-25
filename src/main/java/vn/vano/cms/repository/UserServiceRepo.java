package vn.vano.cms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.yotel.admin.jpa.AuthUser;

import java.util.List;

public interface UserServiceRepo extends JpaRepository<AuthUser, Integer> {

    @Query(value = "SELECT u FROM AuthUser u " +
            "        WHERE (:p_username IS NULL OR u.userName LIKE CONCAT('%', :p_username, '%')) " +
            "          AND (:p_email IS NULL OR u.email LIKE CONCAT('%', :p_email, '%')) ", nativeQuery = false)
    Page<AuthUser> findByParam(@Param("p_username") String userName,
                               @Param("p_email") String email,
                               Pageable pageable);

    @Query(value = "SELECT role_id FROM auth_user_role WHERE user_id = :p_userid", nativeQuery = true)
    List<Integer> findRoleByUser(@Param("p_userid") Integer userId);
}
