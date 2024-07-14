package artchain.farmpro.crop;

import java.util.List;

public record CropResponses(List<CropResponse> crops) {

    public static CropResponses from(List<Crop> crops) {
        List<CropResponse> responses = crops.stream()
                .map(CropResponse::of)
                .toList();

        return new CropResponses(responses);
    }
}
