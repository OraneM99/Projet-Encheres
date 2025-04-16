package com.eni.eBIDou.retrait;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetraitRepository extends JpaRepository<Retrait, Long> {

}
