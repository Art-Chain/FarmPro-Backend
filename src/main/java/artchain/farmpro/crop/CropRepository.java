package artchain.farmpro.crop;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropRepository extends JpaRepository<Crop, Long> {

    Optional<Crop> searchByNameIs(String name);
    List<Crop> searchCropsByNameContaining(String keyword);
}
