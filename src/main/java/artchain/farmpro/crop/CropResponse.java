package artchain.farmpro.crop;

public record CropResponse(Long id, String name) {

    public static CropResponse of(Crop crop) {
        return new CropResponse(crop.getId(), crop.getName());
    }
}
