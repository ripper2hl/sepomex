import com.perales.model.*;

import java.util.Objects;

public class Colonia {

    private Integer id;
    private String nombre;
    private String codigoPostal;
    private AsentamientoTipo asentamientoTipo;
    private Municipio municipio;
    private Estado estado;
    private Ciudad ciudad;
    private String codigoPostalAdministracionAsentamiento;
    private String codigoPostalAdministracionAsentamientoOficina;
    private ZonaTipo zonaTipo;

    public Colonia() {
        super();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public AsentamientoTipo getAsentamientoTipo() {
        return asentamientoTipo;
    }

    public void setAsentamientoTipo(AsentamientoTipo asentamientoTipo) {
        this.asentamientoTipo = asentamientoTipo;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodigoPostalAdministracionAsentamiento() {
        return codigoPostalAdministracionAsentamiento;
    }

    public void setCodigoPostalAdministracionAsentamiento(String codigoPostalAdministracionAsentamiento) {
        this.codigoPostalAdministracionAsentamiento = codigoPostalAdministracionAsentamiento;
    }

    public String getCodigoPostalAdministracionAsentamientoOficina() {
        return codigoPostalAdministracionAsentamientoOficina;
    }

    public void setCodigoPostalAdministracionAsentamientoOficina(String codigoPostalAdministracionAsentamientoOficina) {
        this.codigoPostalAdministracionAsentamientoOficina = codigoPostalAdministracionAsentamientoOficina;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ZonaTipo getZonaTipo() {
        return zonaTipo;
    }

    public void setZonaTipo(ZonaTipo zonaTipo) {
        this.zonaTipo = zonaTipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Colonia colonia = (Colonia) o;
        return Objects.equals(nombre, colonia.nombre) &&
                Objects.equals(codigoPostal, colonia.codigoPostal) &&
                Objects.equals(asentamientoTipo, colonia.asentamientoTipo) &&
                Objects.equals(municipio, colonia.municipio) &&
                Objects.equals(estado, colonia.estado) &&
                Objects.equals(ciudad, colonia.ciudad) &&
                Objects.equals(codigoPostalAdministracionAsentamiento, colonia.codigoPostalAdministracionAsentamiento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, codigoPostal, asentamientoTipo, municipio, estado, ciudad, codigoPostalAdministracionAsentamiento);
    }

    @Override
    public String toString() {
        return "Colonia{" +
                "nombre='" + nombre + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                ", asentamientoTipo=" + asentamientoTipo +
                ", municipio=" + municipio +
                ", estado=" + estado +
                ", ciudad=" + ciudad +
                ", codigoPostalAdministracionAsentamiento='" + codigoPostalAdministracionAsentamiento + '\'' +
                ", codigoPostalAdministracionAsentamientoOficina='" + codigoPostalAdministracionAsentamientoOficina + '\'' +
                '}';
    }
}
