package pe.upc.mckchain.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "minera")
@Getter
@Setter
public class Minera implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id_minera")
    private UUID idMinera;

    @Column(name = "codigounico_minera", length = 30)
    private String codigounicoMinera;

    @Column(name = "estadoactividad_minera", length = 20)
    private String estadoactividadMinera;

    @Column(name = "ruc_minera", length = 20)
    private String rucMinera;

    @Column(name = "nombre_minera", length = 100)
    private String nombreMinera;

    @Column(name = "estadosolicitud_minera", length = 20)
    private String estadosolicitudMinera;

    @Column(name = "fecharegistro_minera")
    private LocalDateTime fecharegistroMinera;

    @Column(name = "fechasolicitud_minera")
    private LocalDateTime fechasolicitudMinera;

    @ManyToOne
    @JoinTable(name = "distrito_mineras",
            joinColumns = @JoinColumn(name = "id_minera", referencedColumnName = "id_minera"),
            inverseJoinColumns = @JoinColumn(name = "id_distrito", referencedColumnName = "id_distrito"))
    private Distrito distritoMinera;

    @OneToOne
    @JoinTable(name = "minera_data",
            joinColumns = @JoinColumn(name = "id_minera", referencedColumnName = "id_minera"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario"))
    private Usuario mineraData;

    //Constructores
    public Minera() {
    }

    //Registrar Minera como Data de REINFO
    public Minera(String codigounicoMinera, String estadoactividadMinera, String rucMinera, String nombreMinera,
                  Distrito distritoMinera) {
        this.codigounicoMinera = codigounicoMinera;
        this.estadoactividadMinera = estadoactividadMinera;
        this.rucMinera = rucMinera;
        this.nombreMinera = nombreMinera;
        this.distritoMinera = distritoMinera;
    }

    //Minera al Rechazar Solicitud
    public Minera(String codigounicoMinera, String estadoactividadMinera, String rucMinera, String nombreMinera,
                  String estadosolicitudMinera, LocalDateTime fecharegistroMinera, LocalDateTime fechasolicitudMinera,
                  Distrito distritoMinera) {
        this.codigounicoMinera = codigounicoMinera;
        this.estadoactividadMinera = estadoactividadMinera;
        this.rucMinera = rucMinera;
        this.nombreMinera = nombreMinera;
        this.estadosolicitudMinera = estadosolicitudMinera;
        this.fecharegistroMinera = fecharegistroMinera;
        this.fechasolicitudMinera = fechasolicitudMinera;
        this.distritoMinera = distritoMinera;
    }
}
