package mikolaj.bogudal.pacman.business.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class SystemService {
    @Getter
    private final int screenW, screenH;

    public SystemService() {
        this.screenW = 500;
        this.screenH = 500;
    }
}
