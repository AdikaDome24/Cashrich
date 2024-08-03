package com.cashrich.repository;

import com.cashrich.entity.AccountUserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccountUserRepository extends JpaRepository<AccountUserEntity, Long> {

    Optional<AccountUserEntity> findById(Long userId);

    boolean existsByEmail(String email);
    boolean existsByMobile(String mobile);

    Optional<AccountUserEntity> findByEmailOrMobileAndAccountUserProfile_Username(String email, String mobile, @NonNull String username);

    boolean existsByAccountUserProfile_Username(String username);
}
