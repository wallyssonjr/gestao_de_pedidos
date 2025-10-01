package br.com.gestaopedidos.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AmbienteService {

    @Value("${spring.profiles.active:dev}")
    private String profileAtivo;

    public String getProfileAtivo() {
        return this.profileAtivo;
    }

    public boolean isProfileDev() {
        return "dev".equalsIgnoreCase(this.profileAtivo);
    }

}
