package artchain.farmpro.crop;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropRepository extends JpaRepository<Crop, Long> {

    List<Crop> searchCropsByNameContaining(String keyword);
}
