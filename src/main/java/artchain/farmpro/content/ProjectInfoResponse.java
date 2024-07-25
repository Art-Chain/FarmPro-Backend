package artchain.farmpro.content;

import artchain.farmpro.crop.CropRequest;
import io.swagger.v3.oas.annotations.Parameter;

public record ProjectInfoResponse(
		@Parameter(description = "농작물 이름", required = true)
		CropRequest cropCategoryName,
		String cropDetailName
) {
}
