package com.example.DB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MoveRepository extends JpaRepository<Move, Long> {

    @Query("SELECT m.moveData FROM Move m WHERE m.save.saveName = :saveName AND m.moveNumber = :moveNumber")
    String getMoveData(@Param("saveName") String saveName, @Param("moveNumber") Integer moveNumber);
}
