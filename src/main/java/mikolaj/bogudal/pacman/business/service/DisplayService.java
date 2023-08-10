package mikolaj.bogudal.pacman.business.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;

@Log
@Service
public class DisplayService {

    private final JFrame frame;

    private final JPanel panel;
    private final GamePlayService gamePlayService;
    private final SystemService systemService;
    private final ImageService imageService;


    public DisplayService(GamePlayService gamePlayService, SystemService systemService, ImageService imageService) {
        this.gamePlayService = gamePlayService;
        this.systemService = systemService;
        this.imageService = imageService;
        this.frame = new JFrame("OneCaucas");
        panel = createJPanel();
    }

    @PostConstruct
    void init() {
        this.frame.setLayout(null);
        this.frame.setVisible(true);
        frame.addKeyListener(gamePlayService.getPlayerDto().getPlayerListener());
        frame.setFocusable(true);
        frame.setSize(systemService.getScreenW(), systemService.getScreenH());
        panel.add(gamePlayService.getLevelDto().getEndScreen());
        gamePlayService.getLevelDto().getEndScreen().setVisible(false);
        panel.add(gamePlayService.getPlayerDto().getPlayer());
        for (int i = 0; i < gamePlayService.getLevelDto().getRows(); i++) {
            for (int j = 0; j < gamePlayService.getLevelDto().getCols(); j++) {
                if ("0".equals(gamePlayService.getLevelDto().getMap()[i][j])) {
                    gamePlayService.getLevelDto().getBricks()[i][j] = imageService.createImage("bricks", j * 100, i * 100);
                    panel.add(gamePlayService.getLevelDto().getBricks()[i][j]);
                }
            }
        }
        panel.add(gamePlayService.getLevelDto().getBackground());
        panel.repaint();
        panel.revalidate();
        frame.add(panel);
        frame.repaint();
        frame.revalidate();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private JPanel createJPanel() {
        var out = new JPanel();
        out.setLayout(null);
        out.setVisible(true);
        out.setBounds(0, 0, systemService.getScreenW(), systemService.getScreenH());
        out.setBackground(new Color(0, 0, 0));
        return out;
    }





}
