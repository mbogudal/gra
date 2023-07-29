package mikolaj.bogudal.pacman.business.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.java.Log;
import mikolaj.bogudal.pacman.business.listener.PlayerListener;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log
@Service
public class DisplayService {

    private final JFrame frame;
    private final JLabel background;
    @Getter
    private final JLabel endScreen;
    @Getter
    private final JLabel player;
    private final Map<String, ImageIcon> images;
    private final String[][] map;
    @Getter
    private final JLabel[][] bricks;
    private final JPanel panel;
    @Getter
    private final Point windowPoint;
    @Getter
    private final Point playerPoint;
    @Getter
    private final PlayerListener playerListener;

    public DisplayService() {
        this.frame = new JFrame("PacMan");
        images = new HashMap<>();
        background = createImage("background", 0, 0, 1000, 1000);
        player = createImage("player", 0, 0);
        panel = createJPanel();
        map = new String[10][10];
        bricks = new JLabel[10][10];
        windowPoint=new Point(5,5);
        playerPoint=new Point(0,0);
        playerListener = new PlayerListener(playerPoint, 100, map);
        endScreen =createImage("endScreen", 0, 0, 1000, 1000);
    }

    @PostConstruct
    void init() {
        initMap();
        this.frame.setSize(1000, 1000);
        this.frame.setLayout(null);
        this.frame.setVisible(true);
        frame.addKeyListener(playerListener);
        frame.setFocusable(true);
        panel.add(endScreen);
        endScreen.setVisible(false);
        panel.add(player);
        for (int i = 0; i < 10; i++){
            for (int j = 0; j <10; j++){
                if("0".equals(map[i][j])){
                    bricks[i][j]=createImage("bricks",j*100, i*100);
                    panel.add(bricks[i][j]);
                }
            }
        }
        panel.add(background);
        panel.repaint();
        panel.revalidate();
        frame.add(panel);
        frame.repaint();
        frame.revalidate();

    }

    private void initMap(){
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                map[i][j]="0";
            }
        }
    }

    private JPanel createJPanel() {
        var out = new JPanel();
        out.setLayout(null);
        out.setVisible(true);
        out.setBounds(0, 0, 1000, 1000);
        out.setBackground(new Color(0, 0, 0));
        return out;
    }

    private JLabel createImage(String name, int x, int y) {
        return createImage(name, x, y, 100, 100);
    }

    private JLabel createImage(String name, int x, int y, int w, int h) {
        JLabel out = null;
        try {
            ImageIcon image = null;
            if (images.containsKey(name)) {
                image = images.get(name);
            } else {
                image = new ImageIcon(
                        ImageIO.read(
                                        ResourceUtils.getFile("classpath:assets/" + name + ".png"))
                                .getScaledInstance(w, h, Image.SCALE_SMOOTH)
                );
                images.put(name, image);
            }
            out = new JLabel(
                    image
            );
            out.setBounds(x, y, w, h);
        } catch (IOException e) {
            log.severe(e.getMessage());
            out = new JLabel();
        }
        return out;
    }

}
