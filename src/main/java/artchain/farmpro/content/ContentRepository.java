package artchain.farmpro.content;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
	List<Content> findTop3ByOrderByIdAsc();
}
