package mikolaj.bogudal.pacman.business.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@Jacksonized
public class LevelJsonDto {
    Long id;
    String name;
    String assetsLocation;
}
