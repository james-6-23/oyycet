package com.cet.practice.config;

import com.cet.practice.common.enums.UserRole;
import com.cet.practice.entity.CetUser;
import com.cet.practice.service.CetUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final CetUserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_DEFAULT_PASSWORD:admin123}")
    private String defaultPassword;

    @Override
    public void run(ApplicationArguments args) {
        CetUser existing = userService.getByUsername("admin");
        if (existing != null) {
            log.info("Admin user already exists, skipping initialization");
            return;
        }

        CetUser admin = new CetUser();
        admin.setUsername("admin");
        admin.setPasswordHash(passwordEncoder.encode(defaultPassword));
        admin.setNickname("管理员");
        admin.setRole(UserRole.ADMIN.getValue());
        admin.setStatus(1);
        userService.save(admin);
        log.info("Default admin user created successfully (username: admin)");
    }
}
