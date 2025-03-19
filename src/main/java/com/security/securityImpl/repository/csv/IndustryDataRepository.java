package com.security.securityImpl.repository.csv;

import com.security.securityImpl.entity.csv.IndustryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndustryDataRepository extends JpaRepository<IndustryData, Long> {
}
