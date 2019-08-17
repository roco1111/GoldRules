package com.rosario.hp.goldrules.Entidades;

public class maquina {

    String id;
    String nombre;
    String idempresa;
    String ubicacion;
    String idsistema;
    String idbeacon;


    public maquina(){
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(String idempresa) {
        this.idempresa = idempresa;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getIdsistema() {
        return idsistema;
    }

    public void setIdsistema(String idsistema) {
        this.idsistema = idsistema;
    }

    public String getIdbeacon() {
        return idbeacon;
    }

    public void setIdbeacon(String idbeacon) {
        this.idbeacon = idbeacon;
    }
}
