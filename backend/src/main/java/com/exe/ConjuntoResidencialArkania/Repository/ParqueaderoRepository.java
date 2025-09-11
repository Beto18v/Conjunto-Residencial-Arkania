package com.exe.ConjuntoResidencialArkania.Repository;

import com.exe.ConjuntoResidencialArkania.Entity.ParqueaderoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParqueaderoRepository extends JpaRepository<ParqueaderoEntity, Long> {
}
