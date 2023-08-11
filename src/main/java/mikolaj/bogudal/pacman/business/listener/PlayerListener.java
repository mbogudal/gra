package mikolaj.bogudal.pacman.business.listener;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PlayerListener extends KeyAdapter {
    private final Point point;
    private final String[][] map;
    @Setter
    private boolean released;
    @Getter
    @Setter
    private boolean gameOver;


    public PlayerListener(Point point, String[][] map) {
        this.point = point;
        this.map = map;
        released=true;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(!released)
            return;

        switch (key) {
            case KeyEvent.VK_D:
                if (map[0].length > point.x+1) {
                    point.x += 1;
                }
                break;
            case KeyEvent.VK_A:
                if (0 < point.x) {
                    point.x -= 1;
                }
                break;
            case KeyEvent.VK_W:
                if (0 < point.y) {
                    point.y -= 1;
                }
                break;
            case KeyEvent.VK_S:
                if (map.length > point.y +1) {
                    point.y += 1;
                }
                break;
            case KeyEvent.VK_SPACE:
                gameOver=false;
                break;
        }
    }
}
