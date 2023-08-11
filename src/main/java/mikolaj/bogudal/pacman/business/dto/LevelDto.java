package mikolaj.bogudal.pacman.business.dto;

import lombok.Builder;
import lombok.Data;

import javax.swing.*;
import java.util.Map;

@Builder
@Data
public class LevelDto {
    int rows=5;
    int cols=5;
    String[][] map;
    Map<String, ImageIcon> images;
    JLabel endScreen;
    JLabel[][] bricks;
    JLabel background;
    String assetsLocation;
}
