package server.database;

import commons.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InstructionRepository extends JpaRepository<Instruction, Integer> {

}
