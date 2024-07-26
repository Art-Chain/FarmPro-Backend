package artchain.farmpro.crop;

import artchain.farmpro.crop.dto.CropResponses;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class CropService {

	private CropRepository cropRepository;

	public CropResponses findCrop(String keyword) {
		List<Crop> crops = cropRepository.searchCropsByNameContaining(keyword);
		return CropResponses.from(crops);
	}

	public CropResponses findAll() {
		List<Crop> crops = cropRepository.findAll();
		return CropResponses.from(crops);
	}
}
