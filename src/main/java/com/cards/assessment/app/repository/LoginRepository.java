package com.cards.assessment.app.repository;

import com.cards.assessment.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface LoginRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}
