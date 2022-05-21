package com.zugazagoitia.knag.vault;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecretRepository extends JpaRepository<Secret, Long> {
    List<Secret> findByUserId(String userId);
}
