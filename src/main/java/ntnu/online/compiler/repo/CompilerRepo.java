package ntnu.online.compiler.repo;

import ntnu.online.compiler.model.CompilerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompilerRepo extends JpaRepository<CompilerModel, Long> {
}
