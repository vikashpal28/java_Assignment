package com.example.Assignment.entity.repository;

import com.example.Assignment.entity.Bot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotRepository extends JpaRepository<Bot , Long> {
}
