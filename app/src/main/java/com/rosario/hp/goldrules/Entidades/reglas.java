package com.rosario.hp.goldrules.Entidades;

public class reglas {
    String id;
    String desc_regla;
    String nro_regla;
    String idsistema;
    String hora;
    String completo;
    String observacion;
    String texto_regla;

    public reglas(String id, String nom_empresa) {
        this.id = id;
        this.desc_regla = desc_regla;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTexto_regla() {
        return texto_regla;
    }

    public void setTexto_regla(String texto_regla) {
        this.texto_regla = texto_regla;
    }

    public String getDesc_regla() {
        return desc_regla;
    }

    public void setDesc_regla(String desc_regla) {
        this.desc_regla = desc_regla;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCompleto() {
        return completo;
    }

    public void setCompleto(String completo) {
        this.completo = completo;
    }

    public String getNro_regla() {
        return nro_regla;
    }

    public void setNro_regla(String nro_regla) {
        this.nro_regla = nro_regla;
    }

    public String getIdsistema() {
        return idsistema;
    }

    public void setIdsistema(String idsistema) {
        this.idsistema = idsistema;
    }
}