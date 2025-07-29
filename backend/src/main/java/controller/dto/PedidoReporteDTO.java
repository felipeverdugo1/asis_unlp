package controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import model.EstadoPedido;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PedidoReporteDTO {
    private String nombre;
    private String camposPedidos;
    private String estado;
    private Long creadoPor_id;
    private String comentario;
    private Long asignado_a_id;
    private Long reporte_id;



    public boolean validarCrearNull(){
        return nombre == null && camposPedidos == null  && creadoPor_id == null;
    }


}