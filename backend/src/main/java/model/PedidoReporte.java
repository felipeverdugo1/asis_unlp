package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Fetch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "pedido_reporte")
public class PedidoReporte implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "campos_pedidos", columnDefinition = "TEXT")
    private String camposPedidos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;  // hay que validarlo como un enum

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "creado_por", nullable = false)
    private Usuario creadoPor;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "asignado_a_id")
    private Usuario asignado_a;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "reporte_id")
    private Reporte reporte;


}

