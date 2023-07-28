package mikolaj.bogudal.pacman.business.listener;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PlayerListener extends KeyAdapter {
    private final JLabel jLabel;
    private final int speed;
    private final String[][] map;
    private int row;
    private int col;


    public PlayerListener(JLabel jLabel, int speed, String[][] map) {
        this.jLabel = jLabel;
        this.speed = speed;
        this.map = map;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_D:
                if(map[0].length>col+1) {
                    jLabel.setLocation(jLabel.getX() + speed, jLabel.getY());
                    col+=1;
                }
                break;
            case KeyEvent.VK_A:
                if(0<col) {
                    jLabel.setLocation(jLabel.getX() - speed, jLabel.getY());
                    col-=1;
                }
                break;
            case KeyEvent.VK_W:
                if(0<row) {
                    jLabel.setLocation(jLabel.getX(), jLabel.getY() - speed);
                    row-=1;
                }
                break;
            case KeyEvent.VK_S:
                if(map.length>row+1) {
                    jLabel.setLocation(jLabel.getX(), jLabel.getY() + speed);
                    row+=1;
                }
                break;
        }
    }
}
