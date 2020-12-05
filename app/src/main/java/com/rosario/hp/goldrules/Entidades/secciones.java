package com.rosario.hp.goldrules.Entidades;

public class secciones {
    String id;
    String seccion;
    String ubicacion;
    String tipo_seccion;

    public String getCod_tipo_seccion() {
        return cod_tipo_seccion;
    }

    public void setCod_tipo_seccion(String cod_tipo_seccion) {
        this.cod_tipo_seccion = cod_tipo_seccion;
    }

    String cod_tipo_seccion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTipo_seccion() {
        return tipo_seccion;
    }

    public void setTipo_seccion(String tipo_seccion) {
        this.tipo_seccion = tipo_seccion;
    }

    public String getSistema() {
        return sistema;
    }

    public void setSistema(String sistema) {
        this.sistema = sistema;
    }

    String sistema;
}
