package artchain.farmpro.content.dto;

import artchain.farmpro.crop.dto.CropRequest;
import io.swagger.v3.oas.annotations.Parameter;

public record ProjectInfoRequest(
		@Parameter(description = "농작물 이름", required = true)
		CropRequest cropCategoryName,
		String cropDetailName,
		String growMethod,
		@Parameter(description = "농장 특징", required = true)
		String plantDescription,
		String cropPrice,
		String plantContactInfo) {
}
