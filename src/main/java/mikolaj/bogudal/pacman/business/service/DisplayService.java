package mikolaj.bogudal.pacman.business.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log
@Service
public class DisplayService {

    private final JFrame frame;
    private final JLabel background;
    private final Map<String, ImageIcon> images;
    private JPanel panel;
    private JPanel bufferedPanel;

    public DisplayService() {
        this.frame = new JFrame("PacMan");
        images = new HashMap<>();
        background=createImage("background", 0,0,1000, 1000);
    }

    @PostConstruct
    void init() {
        this.frame.setSize(1000, 1000);
        this.frame.setLayout(null);
        this.frame.setVisible(true);
        panel = createJPanel();
    }

    @Scheduled(fixedDelay = 16)
    void display() {
        log.info("loop");

        bufferedPanel = createJPanel();
        bufferedPanel.add(createImage("player",0,0));
        bufferedPanel.add(background);
        bufferedPanel.repaint();
        bufferedPanel.revalidate();

        frame.remove(panel);
        panel = bufferedPanel;
        frame.add(panel);
        frame.repaint();
        frame.revalidate();

    }

    private JPanel createJPanel(){
        var out = new JPanel();
        out.setLayout(null);
        out.setVisible(true);
        out.setBounds(0, 0, 1000, 1000);
        out.setBackground(new Color(0, 0, 0));
        return out;
    }

    private JLabel createImage(String name, int x, int y) {
        return createImage(name, x, y,100, 100);
    }

    private JLabel createImage(String name, int x, int y, int w, int h) {
        JLabel out = null;
        try {
            ImageIcon image = null;
            if(images.containsKey(name)){
                image=images.get(name);
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
            out.setBounds(x,y,w,h);
        } catch (IOException e) {
            log.severe(e.getMessage());
            out = new JLabel();
        }
        return out;
    }

}
