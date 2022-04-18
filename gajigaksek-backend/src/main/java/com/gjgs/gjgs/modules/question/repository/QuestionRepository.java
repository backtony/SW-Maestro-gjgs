package com.gjgs.gjgs.modules.question.repository;

import com.gjgs.gjgs.modules.question.entity.Question;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface QuestionRepository extends JpaRepository<Question, Long>,
                                                                        QuestionQueryRepository{

    @EntityGraph(attributePaths = "member")
    Optional<Question> findByMemberUsernameAndId(String username, Long id);

    boolean existsByMemberUsernameAndId(String username,Long id);
}
