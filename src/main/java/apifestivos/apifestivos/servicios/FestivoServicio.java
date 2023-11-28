package apifestivos.apifestivos.servicios;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import apifestivos.apifestivos.entidades.Festivo;
import apifestivos.apifestivos.entidades.dtos.FestivoDto;
import apifestivos.apifestivos.interfaces.IFestivoServicio;
import apifestivos.apifestivos.repositorios.IFestivoRepositorio;

@Service
public class FestivoServicio implements IFestivoServicio {

    @Autowired
    IFestivoRepositorio repositorio;

    private LocalDate obtenerDomingoPascua(int año) {
        int mes, dia, A, B, C, D, E, M, N;
        M = 0;
        N = 0;
        if (año >= 1583 && año <= 1699) {
            M = 22;
            N = 2;
        } else if (año >= 1700 && año <= 1799) {
            M = 23;
            N = 3;
        } else if (año >= 1800 && año <= 1899) {
            M = 23;
            N = 4;
        } else if (año >= 1900 && año <= 2099) {
            M = 24;
            N = 5;
        } else if (año >= 2100 && año <= 2199) {
            M = 24;
            N = 6;
        } else if (año >= 2200 && año <= 2299) {
            M = 25;
            N = 0;
        }

        A = año % 19;
        B = año % 4;
        C = año % 7;
        D = ((19 * A) + M) % 30;
        E = ((2 * B) + (4 * C) + (6 * D) + N) % 7;

        // Decidir entre los 2 casos
        if (D + E < 10) {
            dia = D + E + 22;
            mes = 3; // Marzo
        } else {
            dia = D + E - 9;
            mes = 4; // Abril
        }

        // Excepciones especiales
        if (dia == 26 && mes == 4)
            dia = 19;
        if (dia == 25 && mes == 4 && D == 28 && E == 6 && A > 10)
            dia = 18;
        return LocalDate.of(año, mes, dia);
    }

    private LocalDate agregarDias(LocalDate fecha, int dias) {
        return fecha.plusDays(dias);
    }

    // se cambió la forma de cálcular la fecha, y cambié Date por LocalDate debido a que me salía "Deprecated"
   private LocalDate siguienteLunes(LocalDate fecha) {
    if (fecha.getDayOfWeek().getValue() > DayOfWeek.MONDAY.getValue()) {
        int diasHastaLunes = 8 - fecha.getDayOfWeek().getValue();
        fecha = fecha.plusDays(diasHastaLunes);
    } else if (fecha.getDayOfWeek().getValue() < DayOfWeek.MONDAY.getValue()) {
        fecha = fecha.plusDays(1);
    }
    return fecha;
}

    private List<Festivo> calcularFestivos(List<Festivo> festivos, int año) {
        if (festivos != null) {
            LocalDate pascua = obtenerDomingoPascua(año);
            int i = 0;
            for (final Festivo festivo : festivos) {
                switch (festivo.getTipo().getId()) {
                    case 1:
                        festivo.setFecha(LocalDate.of(año, festivo.getMes(), festivo.getDia()));
                        break;
                    case 2:
                        festivo.setFecha(siguienteLunes(LocalDate.of(año, festivo.getMes(), festivo.getDia())));
                        break;
                    case 3:
                        festivo.setFecha(agregarDias(pascua, festivo.getDiasPascua()));
                        break;
                    case 4:
                        festivo.setFecha(siguienteLunes(agregarDias(pascua, festivo.getDiasPascua())));
                        break;
                }
                festivos.set(i, festivo);
                i++;
            }
        }
        return festivos;
    }

    @Override
    public List<FestivoDto> obtenerFestivos(int año) {
        List<Festivo> festivos = repositorio.findAll();
        festivos = calcularFestivos(festivos, año);
        List<FestivoDto> fechas = new ArrayList<>();
        for (final Festivo festivo : festivos) {
            fechas.add(new FestivoDto(festivo.getNombre(), festivo.getFecha()));
        }
        return fechas;
    }

    private boolean fechasIguales(LocalDate fecha1, LocalDate fecha2) {
        return fecha1.equals(fecha2);
    }

    private boolean esFestivo(List<Festivo> festivos, LocalDate fecha) {
        if (festivos != null) {
            festivos = calcularFestivos(festivos, fecha.getYear());

            for (final Festivo festivo : festivos) {
                if (fechasIguales(festivo.getFecha(), fecha))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean esFestivo(LocalDate fecha) {
        List<Festivo> festivos = repositorio.findAll();
        return esFestivo(festivos, fecha);
    }

    @Override
    public boolean esFechaValida(String strFecha) {
        try {
            LocalDate.parse(strFecha);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
