package Entidades;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-09-09T10:47:48")
@StaticMetamodel(Parametro.class)
public class Parametro_ { 

    public static volatile SingularAttribute<Parametro, Integer> idParametro;
    public static volatile SingularAttribute<Parametro, String> tipoMtto;
    public static volatile SingularAttribute<Parametro, String> frecuencia;
    public static volatile SingularAttribute<Parametro, String> usuarioRegistro;
    public static volatile SingularAttribute<Parametro, Date> fechaRegistro;
    public static volatile SingularAttribute<Parametro, String> ejecucion;
    public static volatile SingularAttribute<Parametro, String> grupo;
    public static volatile SingularAttribute<Parametro, String> tipoEquipo;
    public static volatile SingularAttribute<Parametro, String> actividad;

}