package com.cashrich.repository;

import com.cashrich.entity.CoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepository extends JpaRepository<CoinEntity, Long> {
}
