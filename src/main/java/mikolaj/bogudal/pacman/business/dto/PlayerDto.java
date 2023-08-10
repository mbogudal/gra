package mikolaj.bogudal.pacman.business.dto;

import lombok.Builder;
import lombok.Data;
import mikolaj.bogudal.pacman.business.listener.PlayerListener;

import javax.swing.*;
import java.awt.*;

@Builder
@Data
public class PlayerDto {
    Point playerPoint;
    JLabel player;
    PlayerListener playerListener;
}
