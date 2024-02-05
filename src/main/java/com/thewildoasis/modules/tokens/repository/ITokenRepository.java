package com.thewildoasis.modules.tokens.repository;

import com.thewildoasis.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ITokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);

    @Query("select t from Token t where t.user.id = :userId and t.revoked = false and t.expired = false")
    Optional<Token> findByUserId(Integer userId);
}