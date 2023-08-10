package mikolaj.bogudal.pacman.business.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.java.Log;
import mikolaj.bogudal.pacman.business.dto.LevelDto;
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
    @Getter
    private final JLabel[][] bricks;
    private final JPanel panel;
    @Getter
    private final Point playerPoint;
    @Getter
    private final PlayerListener playerListener;
    @Getter
    private final JButton btnExcellent;
    @Getter
    private final JButton btnMeh;
    @Getter
    private final int screenW, screenH;
    @Getter
    private final LevelDto levelDto;

    public DisplayService() {
        this.frame = new JFrame("OneCaucas");
        screenW = 500;
        screenH = 500;

        images = new HashMap<>();
        background = createImage("background", 0, 0, screenW, screenH);
        player = createImage("player", 0, 0);
        panel = createJPanel();

        levelDto = LevelDto
                .builder()
                .cols(5)
                .rows(5)
                .map(new String[5][5])
                .images(new HashMap<>())
                .build();
        bricks = new JLabel[levelDto.getRows()][levelDto.getCols()];
        playerPoint = new Point(0, 0);
        playerListener = new PlayerListener(playerPoint, levelDto.getMap());
        endScreen = createImage("endScreen", 0, 0, screenW, screenH);
        btnExcellent = new JButton("Excellent");
        btnMeh = new JButton("Meh");

    }

    @PostConstruct
    void init() {
        initMap();
        this.frame.setLayout(null);
        this.frame.setVisible(true);
        frame.addKeyListener(playerListener);
        frame.setFocusable(true);
        frame.setSize(screenW, screenH);
        panel.add(btnExcellent);
        btnExcellent.setVisible(false);
        btnExcellent.setSize(100, 100);
        btnExcellent.setLocation(0, 0);
        panel.add(btnMeh);
        btnMeh.setVisible(false);
        btnMeh.setSize(100, 100);
        btnMeh.setLocation(100, 0);
        panel.add(endScreen);
        endScreen.setVisible(false);
        panel.add(player);
        for (int i = 0; i < levelDto.getRows(); i++) {
            for (int j = 0; j < levelDto.getCols(); j++) {
                if ("0".equals(levelDto.getMap()[i][j])) {
                    bricks[i][j] = createImage("bricks", j * 100, i * 100);
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void initMap() {
        for (int i = 0; i < levelDto.getRows(); i++) {
            for (int j = 0; j < levelDto.getCols(); j++) {
                levelDto.getMap()[i][j] = "0";
            }
        }
    }

    private JPanel createJPanel() {
        var out = new JPanel();
        out.setLayout(null);
        out.setVisible(true);
        out.setBounds(0, 0, screenW, screenH);
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
