package com.thehecklers.planefinder;

import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
// 2. repository 수정 작업 -> CrudRepository =>> ReactiveCrudRepository
public interface PlaneRepository extends ReactiveCrudRepository<Aircraft, Long> {
}
