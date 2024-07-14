package artchain.farmpro;

import static org.assertj.core.api.Assertions.assertThat;

import artchain.farmpro.crop.Crop;
import artchain.farmpro.crop.CropRepository;
import artchain.farmpro.crop.CropResponses;
import artchain.farmpro.crop.CropService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@Transactional
class CropServiceTest {

    @Autowired
    private CropService cropService;
    @Autowired
    private CropRepository cropRepository;

    @Test
    void 특정_문자열이_포함된_작물을_검색할_수_있다() {
        // given
        String keyword = "토";
        cropRepository.save(new Crop("토란"));
        cropRepository.save(new Crop("토마토"));

        // when
        CropResponses cropResponses = cropService.findCrop(keyword);

        // then
        assertThat(cropResponses.crops()).hasSize(2);
    }

    @Test
    void 특정_문자열이_포함되지_않은_작물은_검색하지_않는다() {
        // given
        String keyword = "사";
        cropRepository.save(new Crop("토란"));
        cropRepository.save(new Crop("토마토"));

        // when
        CropResponses cropResponses = cropService.findCrop(keyword);

        // then
        assertThat(cropResponses.crops()).hasSize(0);
    }
}
