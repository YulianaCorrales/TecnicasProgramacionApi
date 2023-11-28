package apifestivos.apifestivos.entidades.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FestivoDto {

    private String festivo;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    public FestivoDto() {
    }

    public FestivoDto(String festivo, LocalDate fecha) {
        this.festivo = festivo;
        this.fecha = fecha;
    }

    public String getFestivo() {
        return festivo;
    }

    public void setFestivo(String festivo) {
        this.festivo = festivo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}