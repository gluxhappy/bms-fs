package me.glux.omd.rest.view;

public final class StandardError {
    public static final int SI_UNKNWN_ERROR=-32700;
    public static final int SI_INVOKE_METHOD_ERROR=-32701;
    public static final int SI_PARAM_MISSING_ANNO=-32702;
    public static final int SI_PARAM_NAME_BLANK=-32703;
    public static final int SI_PARAM_DEFAULT_ERROR=-32704;
    public static final int SI_READ_INPUT_ERROR=-32705;

    public static final int FM_UNKNWON_ERROR=-32800;
    public static final int FM_PARAM_MISSING_ERROR=-32801;
    public static final int FM_PARAM_VALUE_ERROR=-32802;
    public static final int FM_UNSUPPORT_CONTENT_ERROR=-32803;
    public static final int FM_NOT_JSON_INPUT_ERROR=-32804;
    public static final int FM_UNSUPPORT_JSON_TYPE=-32805;    

    public static final int API_UNDEFINED_INTER_ERROR=-40000;
    
    private StandardError(){};
}
