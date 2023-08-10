package mikolaj.bogudal.pacman.business.service;

import lombok.extern.java.Log;
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
public class ImageService {

    private final Map<String, ImageIcon> images;

    public ImageService() {
        this.images = new HashMap<>();
    }

    public JLabel createImage(String name, int x, int y) {
        return createImage(name, x, y, 100, 100);
    }

    public JLabel createImage(String name, int x, int y, int w, int h) {
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
