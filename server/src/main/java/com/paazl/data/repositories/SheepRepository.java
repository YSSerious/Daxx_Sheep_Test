package com.paazl.data.repositories;

import com.paazl.data.Sheep;
import com.paazl.data.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SheepRepository extends JpaRepository<Sheep, Long> {
    List<Sheep> findAllByState(State state);
    List<Sheep> findAllByStateNot(State state);
}
