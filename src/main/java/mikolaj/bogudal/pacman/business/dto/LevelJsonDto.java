package mikolaj.bogudal.pacman.business.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LevelJsonDto {
    Long id;
    String name;
    String assetsLocation;
}
