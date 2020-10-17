package com.rosario.hp.goldrules.include;

public class Constantes {
    private static final String PUERTO_HOST = "";


    private static final String IP = "goldrules.infinitydesigner.com.ar/app";

    public static final String UPDATE_TOKEN = "https://" + IP + PUERTO_HOST + "/actualizar_token.php";
    public static final String GET_CANT_EN_CURSO = "https://" + IP + PUERTO_HOST + "/obtener_cant_en_curso.php";
    public static final String INSERT_PROCEDIMIENTO = "https://" + IP + PUERTO_HOST + "/agregar_procedimiento.php";
    public static final String INSERT_PROCEDIMIENTO_REGLA = "https://" + IP + PUERTO_HOST + "/agregar_procedimientoregla.php";
    public static final String GET_MAX_PROCEDIMIENTO = "https://" + IP + PUERTO_HOST + "/obtener_max_procedimiento.php";
    public static final String UPDATE_REGLA = "https://" + IP + PUERTO_HOST + "/actualizar_regla.php";
    public static final String UPDATE_REGLA_CANCELAR = "https://" + IP + PUERTO_HOST + "/actualizar_regla_cancelar.php";
    public static final String TERMINAR_PROCEDIMIENTO = "https://" + IP + PUERTO_HOST + "/terminar_procedimiento.php";
    public static final String CANCELAR_PROCEDIMIENTO = "https://" + IP + PUERTO_HOST + "/cancelar_procedimiento.php";

    public static final String GET_BY_CLAVE = "https://" + IP + PUERTO_HOST + "/obtener_clave.php";

    public static final String EXTRA_ID = "IDEXTRA";

    /*empresa*/
    public static final String GET_EMPRESAS = "https://" + IP + PUERTO_HOST + "/obtener_empresas.php";
    public static final String GET_EMPRESA_BY_ID = "https://" + IP + PUERTO_HOST + "/obtener_una_empresa.php";

    /*empleado*/
    public static final String INSERT_EMPLEADO = "https://" + IP + PUERTO_HOST + "/agregar_empleado.php";
    public static final String UPDATE_EMPLEADO = "https://" + IP + PUERTO_HOST + "/agregar_empleado.php";
    public static final String GET_BY_ID_EMPLEADO = "https://" + IP + PUERTO_HOST + "/obtener_un_empleado.php";

    /*maquina*/
    public static final String GET_SECCION_BY_ID = "https://" + IP + PUERTO_HOST + "/obtener_una_seccion.php";

    /*procedimientos*/
    public static final String GET_PROCEDIMIENTOS_EMPLEADOS = "https://" + IP + PUERTO_HOST + "/obtener_procedimientos_empleado.php";
    public static final String GET_BY_PROCEDIMIENTO_ID = "https://" + IP + PUERTO_HOST + "/obtener_un_procedimiento.php";

    /*regla*/
    public static final String GET_MIN_REGLA= "https://" + IP + PUERTO_HOST + "/obtener_regla_min.php";
    public static final String GET_REGLAS= "https://" + IP + PUERTO_HOST + "/obtener_reglas.php";
}
