package apifestivos.apifestivos.interfaces;

import java.time.LocalDate;
import java.util.List;

import apifestivos.apifestivos.entidades.dtos.FestivoDto;

public interface IFestivoServicio {

    boolean esFestivo(LocalDate fecha);

    List<FestivoDto> obtenerFestivos(int año);

    boolean esFechaValida(String strFecha);
}