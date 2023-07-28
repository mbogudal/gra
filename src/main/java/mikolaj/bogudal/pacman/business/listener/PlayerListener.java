package mikolaj.bogudal.pacman.business.listener;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PlayerListener extends KeyAdapter {
    private final JLabel jLabel;
    private final int speed;

    public PlayerListener(JLabel jLabel, int speed) {
        this.jLabel = jLabel;
        this.speed = speed;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_D:
                jLabel.setLocation(jLabel.getX() + speed, jLabel.getY());
                break;
            case KeyEvent.VK_A:
                jLabel.setLocation(jLabel.getX() - speed, jLabel.getY());
                break;
            case KeyEvent.VK_W:
                jLabel.setLocation(jLabel.getX(), jLabel.getY() - speed);
                break;
            case KeyEvent.VK_S:
                jLabel.setLocation(jLabel.getX(), jLabel.getY() + speed);
                break;
        }
    }
}
