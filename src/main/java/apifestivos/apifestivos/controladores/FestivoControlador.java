package apifestivos.apifestivos.controladores;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import apifestivos.apifestivos.entidades.dtos.FestivoDto;
import apifestivos.apifestivos.interfaces.IFestivoServicio;

@RestController
@RequestMapping("/festivos")
public class FestivoControlador {

    @Autowired
    private IFestivoServicio servicio;

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/verificar/{año}/{mes}/{dia}", method = RequestMethod.GET)
    public ResponseEntity<String> verificarFestivo(@PathVariable int año, @PathVariable int mes, @PathVariable int dia) {
    try {
        LocalDate fecha = LocalDate.of(año, mes, dia);
        String strFecha = fecha.toString(); // Convertir LocalDate a String

        if (servicio.esFechaValida(strFecha)) {
            return servicio.esFestivo(fecha)
                    ? new ResponseEntity<>("{\"mensaje\":\"Es Festivo\"}", HttpStatus.OK)
                    : new ResponseEntity<>("{\"mensaje\":\"No es festivo\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{\"mensaje\":\"Fecha no válida\"}", HttpStatus.BAD_REQUEST);
        }
    } catch (DateTimeParseException e) {
        return new ResponseEntity<>("{\"mensaje\":\"Error en el formato de la fecha\"}", HttpStatus.BAD_REQUEST);
    }
}

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/obtener/{año}", method = RequestMethod.GET)
    public List<FestivoDto> obtener(@PathVariable int año) {
        return servicio.obtenerFestivos(año);
    }
}
