-- noinspection SpellCheckingInspectionForFile

   -------------------------------------------------------------------------------
   --                           I N S E R T                                    --
   -------------------------------------------------------------------------------
/*
    TABLA: RUG_CAT_TIPO_TRAM_PAGO
    SENTENCIA: INSERT
    OBSERVACION: tipo de facturas para factoraje
*/

INSERT INTO RUG_CAT_TIPO_TRAM_PAGO(ID_TIPO_TRAMITE, PRECIO, VIGENCIA_PRECIO, STATUS_REG, B_CARGA_MASIVA) VALUES(36, 5, SYSDATE , 'AC',0)



   -------------------------------------------------------------------------------
   --                           I N S E R T                                    --
   -------------------------------------------------------------------------------
/*
    TABLA: RUG_CAT_TIPO_TRAMITE
    SENTENCIA: INSERT
    OBSERVACION: Se agrega un nuevo registro a la tabla para sustituir a las nuevas consutlas a partir de la fecha denominada consulta arancel
*/

INSERT INTO RUG_CAT_TIPO_TRAMITE(ID_TIPO_TRAMITE, DESCRIPCION, PRECIO, VIGENCIA_TRAM, STATUS_REG, B_CARGA_MASIVA) VALUES(34,'Consulta arancel',0,null,'AC',0);


   -------------------------------------------------------------------------------
   --                           I N S E R T                                    --
   -------------------------------------------------------------------------------

/*
    TABLA: RUG_CAT_TIPO_TRAMITE
    SENTENCIA: INSERT
    OBSERVACION: Se agrega un nuevo registro a la tabla para sustituir el arancel de las certificaciones en RUG
*/
INSERT INTO RUG_CAT_TIPO_TRAMITE(ID_TIPO_TRAMITE, DESCRIPCION, PRECIO, VIGENCIA_TRAM, STATUS_REG, B_CARGA_MASIVA) VALUES(35,'Certificacion arancel',20,null,'AC',0);


   -------------------------------------------------------------------------------
   --                           I N S E R T                                    --
   -------------------------------------------------------------------------------

/*
    TABLA: RUG_CAT_TIPO_TRAMITE
    SENTENCIA: INSERT
    OBSERVACION: Se agrega el nuevo arancel para Factoraje
*/
INSERT INTO RUG_CAT_TIPO_TRAMITE(ID_TIPO_TRAMITE, DESCRIPCION, PRECIO, VIGENCIA_TRAM, STATUS_REG, B_CARGA_MASIVA) VALUES(36,'Factoraje arancel',5,null,'AC',0);

   -------------------------------------------------------------------------------
   --                           I N S E R T                                    --
   -------------------------------------------------------------------------------

/*
    TABLA: RUG_CAT_TIPO_TRAMITE
    SENTENCIA: INSERT
    OBSERVACION: Se agrega el nuevo arancel para Leasing
*/
INSERT INTO RUG_CAT_TIPO_TRAMITE(ID_TIPO_TRAMITE, DESCRIPCION, PRECIO, VIGENCIA_TRAM, STATUS_REG, B_CARGA_MASIVA) VALUES(37,'Leasing arancel',100,null,'AC',0);



   -------------------------------------------------------------------------------
   --                           I N S E R T                                    --
   -------------------------------------------------------------------------------
/*
    TABLA: RUG_CAT_TIPO_TRAMITE
    SENTENCIA: INSERT
    OBSERVACION: Se agregan nuevos aranceles para vehiculo de 2 y 3 ruedas, derecho de crédito (no facturas) otros
*/

INSERT INTO RUG_CAT_TIPO_TRAMITE(ID_TIPO_TRAMITE, DESCRIPCION, PRECIO, VIGENCIA_TRAM, STATUS_REG, B_CARGA_MASIVA) VALUES(38,'Vehiculo 2 y 3 ruedas arancel',50,null,'AC',0);
INSERT INTO RUG_CAT_TIPO_TRAMITE(ID_TIPO_TRAMITE, DESCRIPCION, PRECIO, VIGENCIA_TRAM, STATUS_REG, B_CARGA_MASIVA) VALUES(39,'Drecho de crédito arancel',100,null,'AC',0);


   -------------------------------------------------------------------------------
   --                           I N S E R T                                    --
   -------------------------------------------------------------------------------
/*
    TABLA: RUG_CAT_TIPO_TRAMITE
    SENTENCIA: INSERT
    OBSERVACION: Se agrega el nuevo arancel para modificaciones dentro de mis operaciones
*/

INSERT INTO RUG_CAT_TIPO_TRAMITE(ID_TIPO_TRAMITE, DESCRIPCION, PRECIO, VIGENCIA_TRAM, STATUS_REG, B_CARGA_MASIVA) VALUES(40,'Modificacion Arancel',100,null,'AC',0);
INSERT INTO RUG_CAT_TIPO_TRAMITE(ID_TIPO_TRAMITE, DESCRIPCION, PRECIO, VIGENCIA_TRAM, STATUS_REG, B_CARGA_MASIVA) VALUES(41,'Vehiculo 2 y 3 ruedas modificacion arancel',25,null,'AC',0);
INSERT INTO RUG_CAT_TIPO_TRAMITE(ID_TIPO_TRAMITE, DESCRIPCION, PRECIO, VIGENCIA_TRAM, STATUS_REG, B_CARGA_MASIVA) VALUES(42,'Cancelacion Aracnel',100,null,'AC',0);
INSERT INTO RUG_CAT_TIPO_TRAMITE(ID_TIPO_TRAMITE, DESCRIPCION, PRECIO, VIGENCIA_TRAM, STATUS_REG, B_CARGA_MASIVA) VALUES(43,'Ejecución arancel',100,null,'AC',0);


--- Nuevo Arancel de Leasing

INSERT INTO RUG_CAT_TIPO_TRAMITE(ID_TIPO_TRAMITE, DESCRIPCION, PRECIO, VIGENCIA_TRAM, STATUS_REG, B_CARGA_MASIVA) VALUES(44,'Modificacion Leasing Arancel',50,null,'AC',0);
INSERT INTO RUG_CAT_TIPO_TRAMITE(ID_TIPO_TRAMITE, DESCRIPCION, PRECIO, VIGENCIA_TRAM, STATUS_REG, B_CARGA_MASIVA) VALUES(45,'Cancelacion Leasing Arancel',50,null,'AC',0);
INSERT INTO RUG_CAT_TIPO_TRAMITE(ID_TIPO_TRAMITE, DESCRIPCION, PRECIO, VIGENCIA_TRAM, STATUS_REG, B_CARGA_MASIVA) VALUES(46,'Ejecucion Leasing Arancel',50,null,'AC',0);

--- Arancel Derecho de Credito y Compra venta

INSERT INTO RUG_CAT_TIPO_TRAMITE(ID_TIPO_TRAMITE, DESCRIPCION, PRECIO, VIGENCIA_TRAM, STATUS_REG, B_CARGA_MASIVA) VALUES(47,'Derecho de credito Arancel',100,null,'AC',0);
INSERT INTO RUG_CAT_TIPO_TRAMITE(ID_TIPO_TRAMITE, DESCRIPCION, PRECIO, VIGENCIA_TRAM, STATUS_REG, B_CARGA_MASIVA) VALUES(48,'Compra venta Arancel',100,null,'AC',0);


   -------------------------------------------------------------------------------
   --            U P D A T E  P R O C E D I M I E N T O S                       --
   -------------------------------------------------------------------------------
/*
    TABLA: SP_CERTIFICACION
    SENTENCIA: UPDATE PROCEDURE
    OBSERVACION: Se actualiza el procedimiento para consultar el nuevo arancel
*/


create or replace PROCEDURE SP_CERTIFICACION
    (
        peIdTramiteCer  IN NUMBER,
        peIdTramite     IN NUMBER,
        peIdGarantia    IN NUMBER,
        peIdTipoTramite IN NUMBER,
        peIdPersona     IN NUMBER,
        psResult        OUT  INTEGER,
        psTxResult      OUT  VARCHAR2
    )
    IS
    vlPrecio NUMBER;
    /*USUARIO ADMINISTRADOR*/

    dUserAdmin NUMBER;

BEGIN

        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CERTIFICACION', 'peIdTramiteCer', peIdTramiteCer, 'IN');
        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CERTIFICACION', 'peIdTramite', peIdTramite, 'IN');
        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CERTIFICACION', 'peIdGarantia', peIdGarantia, 'IN');
        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CERTIFICACION', 'peIdTipoTramite', peIdTipoTramite, 'IN');
        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CERTIFICACION', 'peIdPersona', peIdPersona, 'IN');

        dUserAdmin := 17381;

    -- CAMBIO DEL ID ARANCEL NUEVO A 35
    SELECT PRECIO
    INTO vlPrecio
    FROM RUG_CAT_TIPO_TRAMITE
    WHERE ID_TIPO_TRAMITE = 35;

    INSERT INTO RUG_CERTIFICACIONES
    VALUES(peIdTramiteCer, peIdTramite, peIdGarantia, peIdTipoTramite, sysdate, 'AC', peIdPersona, vlPrecio);
    SP_ACTUALIZAR_SALDO(vlPrecio,peIdPersona);
        psResult := 0;
        psTxResult := 'Alta exitosa';
    commit;
END;


   -------------------------------------------------------------------------------
   --            U P D A T E  P R O C E D I M I E N T O S                       --
   -------------------------------------------------------------------------------

/*
    TABLA: SP_ALTA_BITACORA_TRAMITE2
    SENTENCIA: UPDATE PROCEDURE
    OBSERVACION: Cambio en verificacion de facturas
*/

create or replace procedure SP_ALTA_BITACORA_TRAMITE2(
    peIdTramiteTemp            IN  RUG_BITAC_TRAMITES.ID_TRAMITE_TEMP%TYPE, --1
    peIdStatus                 IN  RUG_BITAC_TRAMITES.ID_STATUS%TYPE, --2
    peIdPaso                   IN  RUG_BITAC_TRAMITES.ID_PASO%TYPE, --3 
    peFechaCreacion            IN  TRAMITES.FECHA_CREACION%TYPE, --4
    peBanderaFecha             IN  CHAR, --BANDERA QUE INDICA SI EL PL PLASMA LA FECHA CON EL SYSDATE O USA LA QUE MANDA EN peFechaCreacion, VALORES POSIBLES V o F, 5
    esFactoraje                IN  varchar2, --6
    psResult                  OUT  INTEGER, --7
    psTxResult                OUT  VARCHAR2 --8
) IS


    vSaldoFactoraje         NUMBER;
    vCantidadFctoraje       NUMBER;
    vlBandera               NUMBER;
    vlTramiteTempFirmo      NUMBER;
    vlIdUsuarioFirmo        NUMBER;
    vlDomicilio             NUMBER;
    vlId_Domicilio          NUMBER;
    vIdCancelacion          NUMBER;
    vlIdInscripcion         NUMBER;
    vlTelefono              NUMBER;
    vlIdMoneda              NUMBER;
    vlContador              NUMBER;
    vlIdAcreedor            NUMBER;
    vlIdAcreedorNuevo       NUMBER;
    vlIdTramite             NUMBER;
    vlIdTramiteTemp         NUMBER;
    vlIdGarantiaTemp        NUMBER;
    vlIdGarantiaH           NUMBER;
    vlIdGarantia            NUMBER;
    vlIdRelacion            NUMBER;
    vlIdGarantiaAnterior    NUMBER;
    vlIdCountTramiteTemp    NUMBER;
    vlIdStatusTram          NUMBER;
    vlIdPersonaTramite12    NUMBER;
    vlresult                NUMBER;
    vlIdUsuario             NUMBER;
    vlIdAnotacion           NUMBER;
    vlCancTrans             NUMBER;
    vlIdGarModTrans         NUMBER;
    vlIdRelModTrans         NUMBER;
    vlAcreedorOrig          NUMBER;
    vlAcreedorModif         NUMBER;
    vlRelBienTrans          NUMBER;
    vlIdPersonaH            NUMBER;
    vlCvePerfilUsu          VARCHAR2(50);
    vlTipoPersona           VARCHAR2(2);
    vlGarantiaStatus        VARCHAR2(2);
    vlTextResult            VARCHAR2(500);
    vlCountStatTram         NUMBER;
    vlIdPersona             NUMBER;
    vlIdFirma               NUMBER;
    vIsMigracion            NUMBER;
    l_tipoInscripcion       NUMBER;
    isLeasing               NUMBER;
    isInscripcion           NUMBER;
    isFactoraje             NUMBER;
    --verificacion de inscripción
    valorInscripcionArancel NUMBER;
    intoInscripcionIn       NUMBER;
    intoInscripcionN        NUMBER;

--nuevos
    vlIdGarantiaBienTemp        NUMBER;
    vlIdGarantiaBienH           NUMBER;
    vlIdGarantiaBien            NUMBER;
    RegGarantiasBienTemp            RUG_GARANTIAS_BIENES_PEND%ROWTYPE;

    V_ID_TRAMITE_PADRE      TRAMITES.ID_TRAMITE%TYPE;
    RegTramitesIncomp       TRAMITES_RUG_INCOMP%ROWTYPE;
    RegGarantiasTemp        RUG_GARANTIAS_PENDIENTES%ROWTYPE;
    RegIncompPartes         V_TRAMITES_INCOMP_PARTES%ROWTYPE;

    RegAcreedor             V_TRAMITES_INCOMP_PARTES%ROWTYPE;
    Ex_ErrParametro         EXCEPTION;
    Ex_TramiteTerminado     EXCEPTION;
    Ex_FirmaCertificado     EXCEPTION;
    Ex_TramiteSinSaldo      EXCEPTION;

    CURSOR cursPartesIcomp(cpeIdTramiteTemp IN NUMBER)
        IS
        SELECT   ID_TRAMITE,
                 ID_PERSONA,
                 ID_PARTE,
                 DESC_PARTE,
                 PER_JURIDICA,
                 NOMBRE,
                 NOMBRE_PERSONA,
                 AP_PATERNO_PERSONA,
                 AP_MATERNO_PERSONA,
                 RAZON_SOCIAL,
                 FOLIO_MERCANTIL,
                 RFC,
                 CURP,
                 ID_DOMICILIO,
                 CALLE,
                 NUM_EXTERIOR,
                 NUM_INTERIOR,
                 ID_COLONIA,
                 CVE_COLONIA,
                 ID_LOCALIDAD,
                 CVE_LOCALIDAD,
                 CVE_DELEG_MUNICIP,
                 NOM_DELEG_MUNICIP,
                 CVE_ESTADO,
                 NOM_ESTADO,
                 CVE_PAIS,
                 NOM_PAIS,
                 CODIGO_POSTAL,
                 ID_NACIONALIDAD,
                 E_MAIL,
                 TELEFONO,
                 EXTENSION,
                 CURP_DOC,
                 LOCALIDAD,
                 NOM_COLONIA,
                 ID_PAIS_RESIDENCIA
        FROM   V_TRAMITES_INCOMP_PARTES
        WHERE   ID_TRAMITE = peIdTramiteTemp;
    cursPartesIcomp_REC  cursPartesIcomp%ROWTYPE;

    CURSOR cursBienesIcomp(cpeIdTramiteTemp IN NUMBER) IS
        SELECT DISTINCT TIPO_BIEN_ESPECIAL,
                        TIPO_IDENTIFICADOR,
                        IDENTIFICADOR,
                        DESCRIPCION_BIEN,
                        SERIE
        FROM RUG_GARANTIAS_BIENES_PEND P
        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp;

    cursBienesIcomp_REC cursBienesIcomp%ROWTYPE;
BEGIN

    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'peIdTramiteTemp', peIdTramiteTemp, 'IN');
    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'peIdStatus', peIdStatus, 'IN');
    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'peIdPaso', peIdPaso, 'IN');
    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'peFechaCreacion', peFechaCreacion, 'IN');
    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'peBanderaFecha', peBanderaFecha, 'IN');

    /* Obtiene informacion del registro de tramites incompletos */
    SELECT ID_PERSONA, ID_TIPO_TRAMITE, FECH_PRE_INSCR, FECHA_INSCR
    into RegTramitesIncomp.id_persona, RegTramitesIncomp.id_tipo_tramite, RegTramitesIncomp.fech_pre_inscr, RegTramitesIncomp.fecha_inscr
    FROM TRAMITES_RUG_INCOMP
    WHERE ID_TRAMITE_TEMP= peIdTramiteTemp;

    SELECT COUNT(*)
    INTO vlIdCountTramiteTemp
    FROM TRAMITES_RUG_INCOMP
    WHERE ID_TRAMITE_TEMP = peIdTramiteTemp;


    IF vlIdCountTramiteTemp > 0 THEN

        SELECT ID_STATUS_TRAM
        INTO vlIdStatusTram
        FROM TRAMITES_RUG_INCOMP
        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp;

        IF peIdStatus = 3 THEN

            IF peIdPaso = 100 THEN

                UPDATE RUG_BITAC_TRAMITES
                SET FECHA_STATUS = peFechaCreacion
                WHERE ID_TRAMITE_TEMP = peIdTramiteTemp AND ID_STATUS = 3;
                COMMIT;
                RAISE Ex_FirmaCertificado;

            ELSE

                IF vlIdStatusTram = 3 THEN

                    RAISE Ex_TramiteTerminado;

                END IF;

            END IF;

        END IF;

    END IF;

    IF (peIdStatus = 3)  THEN /* Operacion terminada, se genera un tramite permanente */
        BEGIN

            /** ver si del grupo Migracion
            SELECT COUNT(*) INTO vIsMigracion
            FROM RUG_GRUPOS
            WHERE ID_ACREEDOR = RegTramitesIncomp.id_persona
            AND DESC_GRUPO = 'MIGRACION';

            IF vIsMigracion = 1 and RegTramitesIncomp.id_tipo_tramite=1 THEN
                RegTramitesIncomp.id_tipo_tramite := 31;
            END IF; **/

            /* ver si tiene saldo **/
            IF FN_TIENE_SALDO(RegTramitesIncomp.id_persona,RegTramitesIncomp.id_tipo_tramite,peIdTramiteTemp) = 0 THEN
                RAISE Ex_TramiteSinSaldo;
            END IF;

            vlIdTramite:=  SEQ_TRAMITES.NEXTVAL;           /* secuencia de la tabla de tramites */

            /* Se da de alta el tramite definitivo  */
            /* EN ESTE INSERT SE AGREGA A LA BITACORA DE COBROS PARA VERIFICAR EN BOLETAS */
            INSERT INTO RUG.TRAMITES(ID_TRAMITE, ID_PERSONA, ID_TIPO_TRAMITE, FECH_PRE_INSCR, FECHA_INSCR,ID_STATUS_TRAM, FECHA_CREACION,
                                     ID_TRAMITE_TEMP, ID_PASO,FECHA_STATUS, STATUS_REG)
            VALUES (vlIdTramite, RegTramitesIncomp.id_persona, RegTramitesIncomp.id_tipo_tramite, RegTramitesIncomp.Fech_Pre_Inscr,
                    RegTramitesIncomp.fecha_inscr,peIdStatus, sysdate, peIdTramiteTemp, peIdPaso,sysdate, 'AC');

            /* Cambia es status de tramite de incompleto a terminado */
            UPDATE TRAMITES_RUG_INCOMP
            SET STATUS_REG='IN',
                ID_STATUS_TRAM = peIdStatus,
                ID_PASO = peIdPaso,
                FECHA_STATUS = sysdate
            where id_tramite_temp=peIdTramiteTemp;


            BEGIN

                OPEN cursPartesIcomp(peIdTramiteTemp);
                LOOP
                    FETCH cursPartesIcomp INTO RegIncompPartes.ID_TRAMITE,
                        RegIncompPartes.ID_PERSONA,
                        RegIncompPartes.ID_PARTE,
                        RegIncompPartes.DESC_PARTE,
                        RegIncompPartes.PER_JURIDICA,
                        RegIncompPartes.NOMBRE,
                        RegIncompPartes.NOMBRE_PERSONA,
                        RegIncompPartes.AP_PATERNO_PERSONA,
                        RegIncompPartes.AP_MATERNO_PERSONA,
                        RegIncompPartes.RAZON_SOCIAL,
                        RegIncompPartes.FOLIO_MERCANTIL,
                        RegIncompPartes.RFC,
                        RegIncompPartes.CURP,
                        RegIncompPartes.ID_DOMICILIO,
                        RegIncompPartes.CALLE,
                        RegIncompPartes.NUM_EXTERIOR,
                        RegIncompPartes.NUM_INTERIOR,
                        RegIncompPartes.ID_COLONIA,
                        RegIncompPartes.CVE_COLONIA,
                        RegIncompPartes.ID_LOCALIDAD,
                        RegIncompPartes.CVE_LOCALIDAD,
                        RegIncompPartes.CVE_DELEG_MUNICIP,
                        RegIncompPartes.NOM_DELEG_MUNICIP,
                        RegIncompPartes.CVE_ESTADO,
                        RegIncompPartes.NOM_ESTADO,
                        RegIncompPartes.CVE_PAIS,
                        RegIncompPartes.NOM_PAIS,
                        RegIncompPartes.CODIGO_POSTAL,
                        RegIncompPartes.ID_NACIONALIDAD,
                        RegIncompPartes.E_MAIL,
                        RegIncompPartes.TELEFONO,
                        RegIncompPartes.EXTENSION,
                        RegIncompPartes.CURP_DOC,
                        RegIncompPartes.LOCALIDAD,
                        RegIncompPartes.NOM_COLONIA,
                        RegIncompPartes.ID_PAIS_RESIDENCIA;
                    EXIT WHEN cursPartesIcomp%NOTFOUND;


                    vlIdPersona := RegIncompPartes.ID_PERSONA;

                    SELECT COUNT(*)
                    INTO vlContador
                    FROM TRAMITES_RUG_INCOMP
                    WHERE ID_TRAMITE_TEMP = peIdTramiteTemp
                      AND ID_TIPO_TRAMITE = 19;


                    IF(vlContador != 0) THEN

                        SELECT ID_ACREEDOR
                        INTO vlIdPersona
                        FROM RUG_REL_MODIFICA_ACREEDOR
                        WHERE ID_TRAMITE_TEMP =  peIdTramiteTemp
                          AND STATUS_REG = 'AC'
                          AND B_FIRMADO = 'N';

                    END IF;


                    INSERT INTO RUG_PERSONAS_H
                    VALUES(vlIdTramite, RegIncompPartes.ID_PARTE, vlIdPersona,
                           RegIncompPartes.NOMBRE_PERSONA, RegIncompPartes.AP_PATERNO_PERSONA, RegIncompPartes.AP_MATERNO_PERSONA,
                           RegIncompPartes.RAZON_SOCIAL, RegIncompPartes.PER_JURIDICA, RegIncompPartes.ID_NACIONALIDAD,
                           RegIncompPartes.RFC, RegIncompPartes.CURP, RegIncompPartes.CURP_DOC, RegIncompPartes.E_MAIL,
                           RegIncompPartes.FOLIO_MERCANTIL, NULL, RegIncompPartes.DESC_PARTE);


                    SELECT COUNT(*)
                    INTO vlDomicilio
                    FROM RUG_DOMICILIOS
                    WHERE ID_DOMICILIO = RegIncompPartes.ID_DOMICILIO;

                    IF(vlDomicilio > 0) THEN

                        INSERT INTO RUG_DOMICILIOS_H
                        VALUES(vlIdTramite, RegIncompPartes.ID_PARTE, vlIdPersona,
                               RegIncompPartes.ID_DOMICILIO, RegIncompPartes.CALLE, RegIncompPartes.NUM_EXTERIOR,
                               RegIncompPartes.NUM_INTERIOR, RegIncompPartes.ID_COLONIA, RegIncompPartes.CVE_COLONIA,
                               RegIncompPartes.NOM_COLONIA, RegIncompPartes.CVE_DELEG_MUNICIP, RegIncompPartes.NOM_DELEG_MUNICIP,
                               RegIncompPartes.CVE_ESTADO, RegIncompPartes.NOM_ESTADO, RegIncompPartes.CODIGO_POSTAL, RegIncompPartes.CVE_PAIS,
                               RegIncompPartes.NOM_PAIS, RegIncompPartes.LOCALIDAD, RegIncompPartes.CVE_LOCALIDAD, RegIncompPartes.ID_LOCALIDAD);

                    ELSE

                        INSERT INTO RUG_DOMICILIOS_EXT_H
                            (SELECT vlIdTramite, RegIncompPartes.ID_PARTE, vlIdPersona, ID_DOMICILIO, RegIncompPartes.ID_PAIS_RESIDENCIA,
                                    UBICA_DOMICILIO_1, UBICA_DOMICILIO_2, POBLACION, ZONA_POSTAL
                             FROM RUG_DOMICILIOS_EXT
                             WHERE ID_DOMICILIO = RegIncompPartes.ID_DOMICILIO);

                    END IF;


                    INSERT INTO RUG_TELEFONOS_H
                    SELECT vlIdTramite, RegIncompPartes.ID_PARTE, vlIdPersona,
                           NULL, TELEFONO,EXTENSION, FECHA_REG, STATUS_REG
                    FROM RUG_TELEFONOS
                    WHERE ID_PERSONA =  RegIncompPartes.ID_PERSONA;

                END LOOP;
                CLOSE cursPartesIcomp;


                INSERT INTO RUG_PERSONAS_H
                SELECT vlIdTramite, 5,  ID_PERSONA, NOMBRE_PERSONA, AP_PATERNO, AP_MATERNO, NULL, 'PF', 1, RFC, NULL, NULL,
                       CVE_USUARIO,
                       NULL, CVE_PERFIL, 'USUARIO'
                FROM V_USUARIO_LOGIN_RUG
                WHERE ID_PERSONA = RegTramitesIncomp.id_persona;


                IF ( RegTramitesIncomp.id_tipo_tramite != 5) THEN

                    IF (RegTramitesIncomp.id_tipo_tramite in (20,21)) THEN
                        vlIdUsuarioFirmo := 0;
                    ELSE

                        vlContador := 0;

                        SELECT COUNT(*)
                        INTO vlContador
                        FROM RUG_FIRMA_MASIVA
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp;

                        -- para firma masiva se objtiene el tramite de firma masiva para obtener el usuario que firma dicho tramite

                        vlTramiteTempFirmo := peIdTramiteTemp;


                        IF(vlContador > 0) THEN

                            SELECT ID_FIRMA_MASIVA
                            INTO vlTramiteTempFirmo
                            FROM RUG_FIRMA_MASIVA
                            WHERE ID_TRAMITE_TEMP = peIdTramiteTemp;

                        END IF;

                        /* SELECT Decode(RegTramitesIncomp.id_tipo_tramite, 21, 0, ID_USUARIO_FIRMO)
                            INTO vlIdUsuarioFirmo
                            --FROM DOCTOS_TRAM_FIRMADOS_RUG
                           FROM V_TRAMITES_FIRMA
                           WHERE ID_TRAMITE_TEMP = vlTramiteTempFirmo;*/

                        vlIdUsuarioFirmo := RegTramitesIncomp.id_persona;
                    END IF;


                    INSERT INTO RUG_PERSONAS_H
                    SELECT vlIdTramite, 6,  ID_PERSONA, NOMBRE_PERSONA, AP_PATERNO, AP_MATERNO, NULL, 'PF', 1, RFC, NULL, NULL,
                           CVE_USUARIO, NULL, CVE_PERFIL, (SELECT DESC_PARTE
                                                           FROM RUG_PARTES
                                                           WHERE ID_PARTE = 6) DESC_PARTE
                    FROM V_USUARIO_LOGIN_RUG
                    WHERE ID_PERSONA = vlIdUsuarioFirmo;


                END IF;

                vlIdAcreedor      := NULL;

            END;

            CASE

                
                WHEN RegTramitesIncomp.id_tipo_tramite=1 or RegTramitesIncomp.id_tipo_tramite=31 THEN /* inscripcion */ /* Entrada id_garantia_pend SI GENERA COSTO*/
                    BEGIN

                        /* se obtiene el id_garantia_temp, y lo datos a modificar */
                        vlIdGarantia:=SEQ_GARANTIAs.NEXTVAL;           /* secuencia de la tabla de tramites */
                        vlIdRelacion:= SEQ_RELACIONES.NEXTVAL;           /* secuencia de la tabla RUG_REG_GARANTIA_PARTES */
                        vlIdFirma:= SEQ_RUG_CTRL_FIRMA.NEXTVAL;  /* secuencia para la firma */

                        SELECT b.ID_TIPO_GARANTIA, b.NUM_GARANTIA, b.DESC_GARANTIA, b.MESES_GARANTIA, b.ID_PERSONA, b.ID_ANOTACION, b.ID_RELACION,
                               b.RELACION_BIEN, b.VALOR_BIENES, b.TIPOS_BIENES_MUEBLES, b.UBICACION_BIENES, b.FOLIO_MERCANTIL, b.PATH_DOC_GARANTIA,
                               b.OTROS_TERMINOS_GARANTIA, b.FECHA_INSCR, b.FECHA_FIN_GAR, b.VIGENCIA, b.GARANTIA_CERTIFICADA, b.GARANTIA_STATUS, b.ID_ULTIMO_TRAMITE,
                               B_ULTIMO_TRAMITE, b.MONTO_MAXIMO_GARANTIZADO, b.ID_GARANTIA_PEND, B.ID_MONEDA, B.INSTRUMENTO_PUBLICO, B.CAMBIOS_BIENES_MONTO, B.NO_GARANTIA_PREVIA_OT,
                               B.ES_PRIORITARIA, B.OTROS_REGISTROS, B.TXT_REGISTROS
                        INTO RegGarantiasTemp.ID_TIPO_GARANTIA, RegGarantiasTemp.NUM_GARANTIA, RegGarantiasTemp.DESC_GARANTIA,
                            RegGarantiasTemp.MESES_GARANTIA, RegGarantiasTemp.ID_PERSONA, RegGarantiasTemp.ID_ANOTACION, RegGarantiasTemp.ID_RELACION,
                            RegGarantiasTemp.RELACION_BIEN, RegGarantiasTemp.VALOR_BIENES, RegGarantiasTemp.TIPOS_BIENES_MUEBLES, RegGarantiasTemp.UBICACION_BIENES,
                            RegGarantiasTemp.FOLIO_MERCANTIL, RegGarantiasTemp.PATH_DOC_GARANTIA, RegGarantiasTemp.OTROS_TERMINOS_GARANTIA, RegGarantiasTemp.FECHA_INSCR,
                            RegGarantiasTemp.FECHA_FIN_GAR, RegGarantiasTemp.VIGENCIA, RegGarantiasTemp.GARANTIA_CERTIFICADA, RegGarantiasTemp.GARANTIA_STATUS,
                            RegGarantiasTemp.ID_ULTIMO_TRAMITE, RegGarantiasTemp.B_ULTIMO_TRAMITE, RegGarantiasTemp.MONTO_MAXIMO_GARANTIZADO, RegGarantiasTemp.ID_GARANTIA_PEND,
                            RegGarantiasTemp.ID_MONEDA, RegGarantiasTemp.INSTRUMENTO_PUBLICO, RegGarantiasTemp.CAMBIOS_BIENES_MONTO, RegGarantiasTemp.NO_GARANTIA_PREVIA_OT,
                            RegGarantiasTemp.ES_PRIORITARIA, RegGarantiasTemp.OTROS_REGISTROS, RegGarantiasTemp.TXT_REGISTROS
                        FROM RUG_GARANTIAS_PENDIENTES B, RUG_REL_TRAM_INC_GARAN C
                        WHERE B.ID_GARANTIA_PEND=C.ID_GARANTIA_PEND
                          AND C.ID_TRAMITE_TEMP=PEIDTRAMITETEMP
                          AND ROWNUM = 1;

                        INSERT INTO RUG_GARANTIAS
                        (ID_GARANTIA,ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA, ID_PERSONA, ID_ANOTACION, ID_RELACION,
                         RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES, UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA,
                         OTROS_TERMINOS_GARANTIA, FECHA_INSCR, FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                         B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO, ID_GARANTIA_PEND, FECHA_REG, STATUS_REG, ID_MONEDA, INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO,NO_GARANTIA_PREVIA_OT,
                         ES_PRIORITARIA, OTROS_REGISTROS, TXT_REGISTROS)
                        VALUES (vlIdGarantia, RegGarantiasTemp.ID_TIPO_GARANTIA, RegGarantiasTemp.NUM_GARANTIA, RegGarantiasTemp.DESC_GARANTIA,
                                RegGarantiasTemp.MESES_GARANTIA, RegGarantiasTemp.ID_PERSONA, RegGarantiasTemp.ID_ANOTACION, vlIdRelacion,
                                RegGarantiasTemp.RELACION_BIEN, RegGarantiasTemp.VALOR_BIENES, RegGarantiasTemp.TIPOS_BIENES_MUEBLES, RegGarantiasTemp.UBICACION_BIENES,
                                RegGarantiasTemp.FOLIO_MERCANTIL, RegGarantiasTemp.PATH_DOC_GARANTIA, RegGarantiasTemp.OTROS_TERMINOS_GARANTIA, RegGarantiasTemp.FECHA_INSCR,
                                RegGarantiasTemp.FECHA_FIN_GAR, RegGarantiasTemp.VIGENCIA, RegGarantiasTemp.GARANTIA_CERTIFICADA, RegGarantiasTemp.GARANTIA_STATUS,
                                vlIdTramite, RegGarantiasTemp.B_ULTIMO_TRAMITE, RegGarantiasTemp.MONTO_MAXIMO_GARANTIZADO, RegGarantiasTemp.ID_GARANTIA_PEND, SYSDATE, 'AC',
                                RegGarantiasTemp.ID_MONEDA, RegGarantiasTemp.INSTRUMENTO_PUBLICO, RegGarantiasTemp.CAMBIOS_BIENES_MONTO,RegGarantiasTemp.NO_GARANTIA_PREVIA_OT,
                                RegGarantiasTemp.ES_PRIORITARIA, RegGarantiasTemp.OTROS_REGISTROS, RegGarantiasTemp.TXT_REGISTROS);

                        INSERT INTO RUG_REL_TRAM_GARAN(ID_TRAMITE,ID_GARANTIA,B_TRAMITE_COMPLETO, STATUS_REG, FECHA_REG)
                        VALUES( vlIdTramite,vlIdGarantia, 'S', 'AC', SYSDATE);

                        vlIdGarantiaH:= SEQ_REG_GARANTIA_H.NEXTVAL;

                        INSERT INTO RUG_GARANTIAS_H(ID_REGISTRO, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                                                    ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                                                    UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                                                    FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                                                    B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,FECHA_MODIF_REG, FECHA_REG, STATUS_REG, ID_GARANTIA_PEND, ID_MONEDA, INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO,NO_GARANTIA_PREVIA_OT,
                                                    ES_PRIORITARIA, OTROS_REGISTROS, TXT_REGISTROS)
                        SELECT vlIdGarantiaH, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                               ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                               UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                               FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                               B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO, sysdate, SYSDATE, 'AC', ID_GARANTIA_PEND, ID_MONEDA, INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO,RUG_GARANTIAS.NO_GARANTIA_PREVIA_OT,
                               ES_PRIORITARIA, OTROS_REGISTROS, TXT_REGISTROS
                        FROM RUG_GARANTIAS
                        WHERE ID_GARANTIA = vlIdGarantia;

                        --lo de bienes inicia
                        -- INSERTAR A TABLAS DE BIENES PARA FACTORAJE
                        OPEN cursBienesIcomp(peIdTramiteTemp);
                        LOOP
                            FETCH cursBienesIcomp INTO RegGarantiasBienTemp.TIPO_BIEN_ESPECIAL,
                                RegGarantiasBienTemp.TIPO_IDENTIFICADOR,
                                RegGarantiasBienTemp.IDENTIFICADOR,
                                RegGarantiasBienTemp.DESCRIPCION_BIEN,
                                RegGarantiasBienTemp.SERIE; -- se agrego la columna serie para factoraje
                            EXIT WHEN cursBienesIcomp%NOTFOUND;

                            vlIdGarantiaBien:=SEQ_GARAN_BIENES.NEXTVAL;
                            vlIdGarantiaBienH:=SEQ_GARAN_BIENES_H.NEXTVAL;

                            INSERT INTO RUG_GARANTIAS_BIENES(
                                                             ID_GARAN_BIEN,
                                                             ID_TRAMITE,
                                                             TIPO_BIEN_ESPECIAL,
                                                             TIPO_IDENTIFICADOR,
                                                             IDENTIFICADOR,
                                                             DESCRIPCION_BIEN,
                                                             SERIE)
                            VALUES(
                                   vlIdGarantiaBien,
                                   vlIdTramite,
                                   RegGarantiasBienTemp.TIPO_BIEN_ESPECIAL,
                                   RegGarantiasBienTemp.TIPO_IDENTIFICADOR,
                                   RegGarantiasBienTemp.IDENTIFICADOR,
                                   RegGarantiasBienTemp.DESCRIPCION_BIEN,
                                   RegGarantiasBienTemp.SERIE);

                            INSERT INTO RUG_GARANTIAS_BIENES_H(ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL,TIPO_IDENTIFICADOR,
                                                               IDENTIFICADOR,DESCRIPCION_BIEN,SERIE)
                            SELECT ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL,TIPO_IDENTIFICADOR,IDENTIFICADOR,DESCRIPCION_BIEN,SERIE
                            FROM RUG_GARANTIAS_BIENES
                            WHERE ID_GARAN_BIEN = vlIdGarantiaBien;

                        END LOOP;
                        CLOSE cursBienesIcomp;
                        -- lo de bienes finaliza

                        BEGIN

                            INSERT INTO RUG_REL_TRAM_PARTES(ID_TRAMITE, ID_PERSONA, ID_PARTE, PER_JURIDICA, STATUS_REG, FECHA_REG)
                            SELECT vlIdTramite,ID_PERSONA, ID_PARTE, PER_JURIDICA, 'AC', SYSDATE
                            FROM RUG_REL_TRAM_INC_PARTES
                            WHERE ID_TRAMITE_TEMP = peIdTramiteTemp
                              AND STATUS_REG = 'AC';

                        EXCEPTION
                            WHEN NO_DATA_FOUND THEN
                                dbms_output.put_line('No Existe personas asociadas al tramite en la tabla RUG_REL_TRAM_INC_PARTES');
                        END;

                        INSERT INTO RUG_REL_GARANTIA_PARTES(ID_GARANTIA, ID_PERSONA, ID_PARTE, ID_RELACION, FECHA_REG, STATUS_REG)
                        SELECT vlIdGarantia, ID_PERSONA, ID_PARTE,vlIdRelacion, SYSDATE, 'AC'
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp
                          AND STATUS_REG = 'AC';

                        /** Firma **/
                        INSERT INTO RUG_FIRMA_DOCTOS(ID_FIRMA, ID_TRAMITE_TEMP, ID_USUARIO_FIRMO) VALUES (vlIdFirma,peIdTramiteTemp,vlIdUsuarioFirmo);


                        -- Verificación de tipo de garantia (Inscripcion = 1, Factoraje = 2, Leasing = 16);

                        SELECT ID_TIPO_GARANTIA
                            INTO l_tipoInscripcion
                        FROM RUG_GARANTIAS
                            WHERE ID_GARANTIA = vlIdGarantia;


                        IF l_tipoInscripcion = 1 THEN
                            isInscripcion := 1;
                            
                            SELECT NVL(MAX(TIPO_BIEN_ESPECIAL),0) RESULTADO INTO intoInscripcionIn FROM RUG_GARANTIAS_BIENES WHERE ID_TRAMITE = (SELECT ID_ULTIMO_TRAMITE FROM RUG_GARANTIAS WHERE ID_GARANTIA = vlIdGarantia)
                            AND TIPO_BIEN_ESPECIAL IN (1,2,3);
                            
                            IF intoInscripcionIn = 0 THEN
                                SELECT NVL(MAX(TIPO_BIEN_ESPECIAL),0) RESULTADO INTO intoInscripcionN FROM RUG_GARANTIAS_BIENES WHERE ID_TRAMITE = (SELECT ID_ULTIMO_TRAMITE FROM RUG_GARANTIAS WHERE ID_GARANTIA = vlIdGarantia)
                                AND TIPO_BIEN_ESPECIAL IN (5,6);
                                IF intoInscripcionN = 5 THEN
                                    SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 38;
                                ELsIF intoInscripcionN = 6 THEN
                                    SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 39;
                                ELSE
                                    SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 1;
                                END IF;
                            ELSE
                                SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 1;
                            END IF;
                        
                        
                            REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'TIPO_INSCRIPCION', 'INSCRIPCION', 'IN');
                            SP_ACTUALIZAR_SALDO(valorInscripcionArancel,RegGarantiasTemp.ID_PERSONA);
                        ELSIF l_tipoInscripcion = 2 THEN
                            isFactoraje := 1;
                            FOR C IN (SELECT COUNT(BI.ID_TRAMITE) AS CANTIDAD INTO vCantidadFctoraje FROM RUG_GARANTIAS GA
                                        INNER JOIN RUG_GARANTIAS_BIENES BI
                                        ON GA.ID_ULTIMO_TRAMITE = BI.ID_TRAMITE
                                        WHERE GA.ID_GARANTIA = vlIdGarantia AND BI.TIPO_BIEN_ESPECIAL = 2)

                            LOOP
                                IF C.CANTIDAD > 0 THEN
                                    vSaldoFactoraje := 5 * C.CANTIDAD;
                                ELSE
                                     vSaldoFactoraje := C.CANTIDAD;
                                end if;
                            END LOOP;

                            IF vSaldoFactoraje > 0  and esFactoraje = 'factoraje' THEN
                                REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'TIPO_INSCRIPCION', 'FACTORAJE', 'IN');
                                SP_ACTUALIZAR_SALDO(vSaldoFactoraje,RegGarantiasTemp.ID_PERSONA);
                            end if;
                        ELSIF l_tipoInscripcion = 16 THEN
                            isLeasing := 1;
                            SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 37;
                            REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'TIPO_INSCRIPCION', 'LEASING', 'IN');
                            SP_ACTUALIZAR_SALDO(valorInscripcionArancel,RegGarantiasTemp.ID_PERSONA);
                        end if;
                    END; /* case 1 */


                
          
                WHEN RegTramitesIncomp.id_tipo_tramite= 2  THEN /* Anotacion con garantia NO GENERA COSTO*/
                    BEGIN

                        Dbms_output.put_line('Entra al tipo tramite 2');

                        /* se obtiene el id_garantia_temp, y lo datos a modificar */

                        vlIdRelacion:= SEQ_RELACIONES.NEXTVAL;

                        SELECT RGP.ID_GARANTIA_PEND, RGP.ID_GARANTIA_MODIFICAR, RGP.VIGENCIA
                        INTO RegGarantiasTemp.ID_GARANTIA_PEND, RegGarantiasTemp.ID_GARANTIA_MODIFICAR, RegGarantiasTemp.VIGENCIA
                        FROM RUG_GARANTIAS_PENDIENTES RGP
                                 LEFT JOIN RUG_REL_TRAM_INC_GARAN RRL
                                           ON RGP.ID_GARANTIA_PEND = RRL.ID_GARANTIA_PEND
                        WHERE RRL.ID_TRAMITE_TEMP = peIdTramiteTemp;

                        SELECT ID_ANOTACION
                        INTO vlIdAnotacion
                        FROM RUG_ANOTACIONES
                        WHERE ID_TRAMITE_TEMP =  peIdTramiteTemp;

                        UPDATE RUG_GARANTIAS a
                        SET a.GARANTIA_STATUS='AC',
                            A.FECHA_REG = SYSDATE,
                            A.STATUS_REG = 'AC',
                            A.ID_ULTIMO_TRAMITE = vlIdTramite,
                            A.ID_ANOTACION = vlIdAnotacion,
                            A.ID_RELACION = vlIdRelacion
                        WHERE A.ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        /* se inserta en un historico */
                        vlIdGarantiaH:= SEQ_REG_GARANTIA_H.nextval;

                        INSERT INTO RUG_GARANTIAS_H(ID_REGISTRO, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                                                    ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                                                    UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                                                    FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                                                    B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,FECHA_MODIF_REG, FECHA_REG, STATUS_REG, ID_GARANTIA_PEND, ID_MONEDA,
                                                    INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO,NO_GARANTIA_PREVIA_OT)
                        SELECT vlIdGarantiaH, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                               ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                               UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                               FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                               B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,sysdate, SYSDATE, 'AC', ID_GARANTIA_PEND, ID_MONEDA, INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO, NO_GARANTIA_PREVIA_OT
                        FROM RUG_GARANTIAS
                        WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        INSERT INTO RUG_REL_TRAM_GARAN(ID_TRAMITE,ID_GARANTIA,B_TRAMITE_COMPLETO, STATUS_REG, FECHA_REG)
                        VALUES( vlIdTramite,RegGarantiasTemp.ID_GARANTIA_MODIFICAR, 'S', 'AC', SYSDATE);



                        BEGIN
                            INSERT INTO RUG_REL_TRAM_PARTES(ID_TRAMITE, ID_PERSONA, ID_PARTE, PER_JURIDICA, STATUS_REG, FECHA_REG)
                            SELECT vlIdTramite,ID_PERSONA, ID_PARTE, PER_JURIDICA, 'AC', SYSDATE
                            FROM RUG_REL_TRAM_INC_PARTES
                            WHERE id_tramite_temp=peIdTramiteTemp;
                        Exception
                            WHEN NO_DATA_FOUND THEN
                                dbms_output.put_line('No Existe personas asociadas al tramite en la tabla RUG_REL_TRAM_INC_PARTES');
                        END;

                        INSERT INTO RUG_REL_GARANTIA_PARTES(ID_GARANTIA, ID_PERSONA, ID_PARTE, ID_RELACION, FECHA_REG, STATUS_REG)
                        SELECT RegGarantiasTemp.ID_GARANTIA_MODIFICAR, ID_PERSONA, ID_PARTE,vlIdRelacion, SYSDATE, 'AC'
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp AND STATUS_REG = 'AC';

                    END;


                WHEN RegTramitesIncomp.id_tipo_tramite=3  THEN /* AVISO PREVENTIVO  NO GENERA COSTO*/
                    BEGIN

                        INSERT INTO RUG_REL_TRAM_PARTES(ID_TRAMITE, ID_PERSONA, ID_PARTE, PER_JURIDICA, STATUS_REG, FECHA_REG)
                        SELECT vlIdTramite,ID_PERSONA, ID_PARTE, PER_JURIDICA, 'AC', SYSDATE
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE id_tramite_temp=peIdTramiteTemp AND STATUS_REG = 'AC';

                    END;


                WHEN RegTramitesIncomp.id_tipo_tramite IN (4, 21, 13, 17)  THEN /* Cancelacion  y Fin de Vigencia SI GENERA COSTO TRAMTIE 4*/
                    BEGIN

                        /* se obtiene el id_garantia_temp, y lo datos a modificar */
                        --cambiar query

                        vlGarantiaStatus := 'CA';

                        IF(RegTramitesIncomp.id_tipo_tramite = 21) THEN
                            vlGarantiaStatus := 'FV';
                        ELSIF (RegTramitesIncomp.id_tipo_tramite = 13) THEN
                            vlGarantiaStatus := 'EM';
                        ELSIF (RegTramitesIncomp.id_tipo_tramite = 17) THEN
                            vlGarantiaStatus := 'AC';
                        END IF;


                        vlIdRelacion:= SEQ_RELACIONES.NEXTVAL;

                        SELECT RGP.ID_GARANTIA_PEND, RGP.ID_GARANTIA_MODIFICAR
                        INTO RegGarantiasTemp.ID_GARANTIA_PEND,
                            RegGarantiasTemp.ID_GARANTIA_MODIFICAR
                        FROM RUG_GARANTIAS_PENDIENTES RGP
                                 LEFT JOIN RUG_REL_TRAM_INC_GARAN RRL
                                           ON RGP.ID_GARANTIA_PEND = RRL.ID_GARANTIA_PEND
                        WHERE RRL.ID_TRAMITE_TEMP = peIdTramiteTemp;

                        /* se actualiza la garantia final */
                        UPDATE RUG_GARANTIAS A
                        SET A.GARANTIA_STATUS= vlGarantiaStatus, --'CA',
                            A.FECHA_REG = SYSDATE,
                            A.STATUS_REG = 'AC',
                            A.ID_ULTIMO_TRAMITE = vlIdTramite,
                            A.ID_RELACION = vlIdRelacion,
                            A.ID_GARANTIA_PEND = RegGarantiasTemp.ID_GARANTIA_PEND
                        WHERE a.ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        /* se inserta en un historico */
                        vlIdGarantiaH:= SEQ_REG_GARANTIA_H.nextval;

                        INSERT INTO RUG_GARANTIAS_H(ID_REGISTRO, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                                                    ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                                                    UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                                                    FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                                                    B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,FECHA_MODIF_REG, FECHA_REG, STATUS_REG, ID_GARANTIA_PEND, ID_MONEDA,
                                                    INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO, NO_GARANTIA_PREVIA_OT, ES_PRIORITARIA, OTROS_REGISTROS, TXT_REGISTROS)
                        SELECT vlIdGarantiaH, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                               ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                               UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                               FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                               B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,sysdate, SYSDATE, 'AC', ID_GARANTIA_PEND,ID_MONEDA,INSTRUMENTO_PUBLICO,
                               CAMBIOS_BIENES_MONTO, NO_GARANTIA_PREVIA_OT, ES_PRIORITARIA, OTROS_REGISTROS, TXT_REGISTROS
                        FROM RUG_GARANTIAS
                        WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        --eliminamos primero
                        BEGIN
                            DELETE FROM RUG_GARANTIAS_BIENES
                            WHERE ID_TRAMITE = peIdTramiteTemp;
                        END;

                        --lo de bienes inicia
                        -- SERIE PARA FACTORAJE
                        OPEN cursBienesIcomp(peIdTramiteTemp);
                        LOOP
                            FETCH cursBienesIcomp INTO RegGarantiasBienTemp.TIPO_BIEN_ESPECIAL,
                                RegGarantiasBienTemp.TIPO_IDENTIFICADOR,
                                RegGarantiasBienTemp.IDENTIFICADOR,
                                RegGarantiasBienTemp.DESCRIPCION_BIEN,
                                RegGarantiasBienTemp.SERIE;
                            EXIT WHEN cursBienesIcomp%NOTFOUND;

                            vlIdGarantiaBien:=SEQ_GARAN_BIENES.NEXTVAL;
                            vlIdGarantiaBienH:=SEQ_GARAN_BIENES_H.NEXTVAL;

                            INSERT INTO RUG_GARANTIAS_BIENES(ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL,TIPO_IDENTIFICADOR,
                                                             IDENTIFICADOR,DESCRIPCION_BIEN,SERIE)
                            VALUES(vlIdGarantiaBien,vlIdTramite,RegGarantiasBienTemp.TIPO_BIEN_ESPECIAL,RegGarantiasBienTemp.TIPO_IDENTIFICADOR,
                                   RegGarantiasBienTemp.IDENTIFICADOR,RegGarantiasBienTemp.DESCRIPCION_BIEN,RegGarantiasBienTemp.SERIE);

                            INSERT INTO RUG_GARANTIAS_BIENES_H(ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL,TIPO_IDENTIFICADOR,
                                                               IDENTIFICADOR,DESCRIPCION_BIEN,SERIE)
                            SELECT ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL,TIPO_IDENTIFICADOR,IDENTIFICADOR,DESCRIPCION_BIEN,SERIE
                            FROM RUG_GARANTIAS_BIENES
                            WHERE ID_GARAN_BIEN = vlIdGarantiaBien;

                        END LOOP;
                        CLOSE cursBienesIcomp;
                        -- lo de bienes finaliza

                        INSERT INTO RUG_REL_TRAM_GARAN(ID_TRAMITE,ID_GARANTIA,B_TRAMITE_COMPLETO, STATUS_REG, FECHA_REG)
                        VALUES( vlIdTramite,RegGarantiasTemp.ID_GARANTIA_MODIFICAR, 'S', 'AC', SYSDATE);

                        BEGIN
                            INSERT INTO RUG_REL_TRAM_PARTES(ID_TRAMITE, ID_PERSONA, ID_PARTE, PER_JURIDICA, STATUS_REG, FECHA_REG)
                            SELECT vlIdTramite,ID_PERSONA, ID_PARTE, PER_JURIDICA, 'AC', SYSDATE
                            FROM RUG_REL_TRAM_INC_PARTES
                            WHERE id_tramite_temp = peIdTramiteTemp;
                        EXCEPTION
                            WHEN NO_DATA_FOUND THEN
                                dbms_output.put_line('No Existe personas asociadas al tramite en la tabla RUG_REL_TRAM_INC_PARTES');
                        END;

                        INSERT INTO RUG_REL_GARANTIA_PARTES(ID_GARANTIA, ID_PERSONA, ID_PARTE, ID_RELACION, FECHA_REG, STATUS_REG)
                        SELECT RegGarantiasTemp.ID_GARANTIA_MODIFICAR, ID_PERSONA, ID_PARTE,vlIdRelacion, SYSDATE, 'AC'
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp AND STATUS_REG = 'AC';

                        vlIdFirma:= SEQ_RUG_CTRL_FIRMA.NEXTVAL;  /* secuencia para la firma */
                        INSERT INTO RUG_FIRMA_DOCTOS(ID_FIRMA, ID_TRAMITE_TEMP, ID_USUARIO_FIRMO) VALUES (vlIdFirma,peIdTramiteTemp,vlIdUsuarioFirmo);

                        --SP_ACTUALIZAR_SALDO(160,vlIdUsuarioFirmo);
                        SELECT ID_TIPO_GARANTIA
                            INTO l_tipoInscripcion
                        FROM RUG_GARANTIAS
                            WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;
                        
                        IF l_tipoInscripcion = 1 THEN
                            isInscripcion := 1;
                            
                            SELECT NVL(MAX(TIPO_BIEN_ESPECIAL),0) RESULTADO INTO intoInscripcionIn FROM RUG_GARANTIAS_BIENES WHERE ID_TRAMITE = (SELECT ID_ULTIMO_TRAMITE FROM RUG_GARANTIAS WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR)
                            AND TIPO_BIEN_ESPECIAL IN (1,2,3);
                            
                            IF intoInscripcionIn = 0 THEN
                                SELECT NVL(MAX(TIPO_BIEN_ESPECIAL),0) RESULTADO INTO intoInscripcionN FROM RUG_GARANTIAS_BIENES WHERE ID_TRAMITE = (SELECT ID_ULTIMO_TRAMITE FROM RUG_GARANTIAS WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR)
                                AND TIPO_BIEN_ESPECIAL IN (5,6);
                                IF intoInscripcionN = 5 THEN
                                    SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 41;
                                ELsIF intoInscripcionN = 6 THEN
                                    SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 39;
                                ELSE
                                    SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 42;
                                END IF;
                            ELSE
                                SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 42;
                            END IF;
                        
                        
                            REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'MODIFICACION_ARANCEL',valorInscripcionArancel , 'IN');
                            SP_ACTUALIZAR_SALDO(valorInscripcionArancel,vlIdUsuarioFirmo);
                        ELSIF l_tipoInscripcion = 2 THEN
                            isFactoraje := 1;
                            FOR C IN (SELECT COUNT(BI.ID_TRAMITE) AS CANTIDAD INTO vCantidadFctoraje FROM RUG_GARANTIAS GA
                                        INNER JOIN RUG_GARANTIAS_BIENES BI
                                        ON GA.ID_ULTIMO_TRAMITE = BI.ID_TRAMITE
                                        WHERE GA.ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR AND BI.TIPO_BIEN_ESPECIAL = 2)

                            LOOP
                                IF C.CANTIDAD > 0 THEN
                                    vSaldoFactoraje := 5 * C.CANTIDAD;
                                ELSE
                                     vSaldoFactoraje := C.CANTIDAD;
                                end if;
                            END LOOP;

                            IF vSaldoFactoraje > 0  and esFactoraje = 'factoraje' THEN
                                REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'TIPO_INSCRIPCION', 'FACTORAJE', 'IN');
                                SP_ACTUALIZAR_SALDO(vSaldoFactoraje,vlIdUsuarioFirmo);
                            end if;
                            REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'MODIFICACION_ARANCEL_FACTORAJE',vSaldoFactoraje , 'IN');
                        ELSIF l_tipoInscripcion = 16 THEN
                            isLeasing := 1;
                            REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'TIPO_INSCRIPCION', 'LEASING', 'IN');
                            SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 45;
                            SP_ACTUALIZAR_SALDO(valorInscripcionArancel,vlIdUsuarioFirmo);
                            REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'CANCELACION_ARANCEL_LEASING',valorInscripcionArancel , 'IN');
                        end if;

                    END;


                WHEN RegTramitesIncomp.id_tipo_tramite= 5  THEN /* CERTIFICACION SI GENERA COSTO*/
                    BEGIN
                        dbms_output.put_line('ENTRO A LA CERTIFICACION');
                    END;


                WHEN RegTramitesIncomp.id_tipo_tramite = 6  THEN /* Rectificacion por error NO GENERA COSTO*/
                    BEGIN

                        vlIdRelacion:= SEQ_RELACIONES.NEXTVAL;      /* secuencia de la tabla RUG_REG_GARANTIA_PARTES */
                        /* se obtiene el id_garantia_temp, y lo datos a modificar */ --(datos que se modificaron en la pantalla)
                        vlIdFirma:= SEQ_RUG_CTRL_FIRMA.NEXTVAL;  /* secuencia para la firma */

                        SELECT RGP.ID_GARANTIA_PEND,RGP.ID_TIPO_GARANTIA,  RGP.DESC_GARANTIA, RGP.ID_PERSONA, RGP.RELACION_BIEN,
                               RGP.OTROS_TERMINOS_GARANTIA,RGP.FECHA_INSCR,RGP.VIGENCIA,RGP.GARANTIA_STATUS,RGP.ID_ULTIMO_TRAMITE,
                               RGP.MONTO_MAXIMO_GARANTIZADO,RGP.ID_GARANTIA_MODIFICAR ,RGP.CAMBIOS_BIENES_MONTO,RGP.INSTRUMENTO_PUBLICO,RGP.ID_MONEDA,RGP.NO_GARANTIA_PREVIA_OT
                        INTO RegGarantiasTemp.ID_GARANTIA_PEND, RegGarantiasTemp.ID_TIPO_GARANTIA, RegGarantiasTemp.DESC_GARANTIA,
                            RegGarantiasTemp.ID_PERSONA, RegGarantiasTemp.RELACION_BIEN, RegGarantiasTemp.OTROS_TERMINOS_GARANTIA,
                            RegGarantiasTemp.FECHA_INSCR,RegGarantiasTemp.VIGENCIA, RegGarantiasTemp.GARANTIA_STATUS,
                            RegGarantiasTemp.ID_ULTIMO_TRAMITE,RegGarantiasTemp.MONTO_MAXIMO_GARANTIZADO, RegGarantiasTemp.ID_GARANTIA_MODIFICAR,
                            RegGarantiasTemp.CAMBIOS_BIENES_MONTO,RegGarantiasTemp.INSTRUMENTO_PUBLICO, RegGarantiasTemp.ID_MONEDA, RegGarantiasTemp.NO_GARANTIA_PREVIA_OT
                        FROM RUG_GARANTIAS_PENDIENTES RGP
                                 LEFT JOIN RUG_REL_TRAM_INC_GARAN RRL
                                           ON RGP.ID_GARANTIA_PEND = RRL.ID_GARANTIA_PEND
                        WHERE RRL.ID_TRAMITE_TEMP = peIdTramiteTemp;


                        INSERT INTO RUG_REL_GARANTIA_PARTES(ID_GARANTIA, ID_PERSONA, ID_PARTE, ID_RELACION, FECHA_REG, STATUS_REG)
                        SELECT RegGarantiasTemp.ID_GARANTIA_MODIFICAR, ID_PERSONA, ID_PARTE,vlIdRelacion, SYSDATE, 'AC'
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp AND STATUS_REG = 'AC';

                        /* se actualiza la garantia final */

                        UPDATE RUG_GARANTIAS a
                        SET A.ID_TIPO_GARANTIA           = RegGarantiasTemp.ID_TIPO_GARANTIA,
                            a.DESC_GARANTIA              = RegGarantiasTemp.DESC_GARANTIA,
                            A.FECHA_INSCR                = RegGarantiasTemp.FECHA_INSCR,
                            A.ID_PERSONA                 = RegGarantiasTemp.ID_PERSONA,
                            a.MONTO_MAXIMO_GARANTIZADO   = RegGarantiasTemp.MONTO_MAXIMO_GARANTIZADO,
                            a.OTROS_TERMINOS_GARANTIA    = RegGarantiasTemp.OTROS_TERMINOS_GARANTIA,
                            a.RELACION_BIEN              = RegGarantiasTemp.RELACION_BIEN,
                            a.STATUS_REG                 = 'AC',
                            A.FECHA_REG                  = SYSDATE,
                            A.VIGENCIA                   = RegGarantiasTemp.VIGENCIA,
                            A.ID_GARANTIA_PEND           = RegGarantiasTemp.ID_GARANTIA_PEND,
                            A.ID_ULTIMO_TRAMITE          = vlIdTramite,
                            A.ID_RELACION                = vlIdRelacion,
                            A.INSTRUMENTO_PUBLICO        = RegGarantiasTemp.INSTRUMENTO_PUBLICO,
                            A.CAMBIOS_BIENES_MONTO       = RegGarantiasTemp.CAMBIOS_BIENES_MONTO,
                            A.ID_MONEDA                  = RegGarantiasTemp.ID_MONEDA
                        WHERE A.ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        /* se inserta en un historico */
                        vlIdGarantiaH:= SEQ_REG_GARANTIA_H.nextval;

                        INSERT INTO RUG_GARANTIAS_H(ID_REGISTRO, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                                                    ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                                                    UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                                                    FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                                                    B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,FECHA_MODIF_REG, FECHA_REG, STATUS_REG, ID_GARANTIA_PEND, ID_MONEDA,
                                                    INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO,NO_GARANTIA_PREVIA_OT)
                        SELECT vlIdGarantiaH, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                               ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                               UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                               FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                               B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,sysdate, SYSDATE, 'AC', ID_GARANTIA_PEND, ID_MONEDA,INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO,NO_GARANTIA_PREVIA_OT
                        FROM RUG_GARANTIAS
                        WHERE ID_GARANTIA =  RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        INSERT INTO RUG_REL_TRAM_GARAN(ID_TRAMITE,ID_GARANTIA,B_TRAMITE_COMPLETO, STATUS_REG, FECHA_REG)
                        VALUES( vlIdTramite,RegGarantiasTemp.ID_GARANTIA_MODIFICAR, 'S', 'AC', SYSDATE);

                        BEGIN
                            INSERT INTO RUG_REL_TRAM_PARTES(ID_TRAMITE, ID_PERSONA, ID_PARTE, PER_JURIDICA, STATUS_REG, FECHA_REG)
                            SELECT vlIdTramite,ID_PERSONA, ID_PARTE, PER_JURIDICA, 'AC', SYSDATE
                            FROM RUG_REL_TRAM_INC_PARTES
                            WHERE ID_TRAMITE_TEMP = peIdTramiteTemp AND STATUS_REG = 'AC';
                        Exception
                            WHEN NO_DATA_FOUND THEN
                                dbms_output.put_line('No Existe personas asociadas al tramite en la tabla RUG_REL_TRAM_INC_PARTES');
                        END;


                        UPDATE RUG_AUTORIDAD
                        SET ANOTACION_JUEZ = (
                            SELECT ANOTACION_JUEZ
                            FROM RUG_AUTORIDAD_PEND
                            WHERE ID_TRAMITE_TEMP_NVO =  peIdTramiteTemp)
                        WHERE ID_TRAMITE_TEMP = (
                            SELECT ID_TRAMITE_TEMP
                            FROM RUG_AUTORIDAD_PEND
                            WHERE ID_TRAMITE_TEMP_NVO =  peIdTramiteTemp);

                        INSERT INTO RUG_FIRMA_DOCTOS(ID_FIRMA, ID_TRAMITE_TEMP, ID_USUARIO_FIRMO) VALUES (vlIdFirma,peIdTramiteTemp,vlIdUsuarioFirmo);
                    END;


                WHEN RegTramitesIncomp.id_tipo_tramite= 7  THEN /* Modificacion,   ya existe la garantia final o sea el id_garantia SI GENERA COSTO*/
                    BEGIN

                        vlIdRelacion:= SEQ_RELACIONES.NEXTVAL;           /* secuencia de la tabla RUG_REG_GARANTIA_PARTES */

                        /* se obtiene el id_garantia_temp, y lo datos a modificar */

                        SELECT  RGP.ID_GARANTIA_PEND, RGP.MONTO_MAXIMO_GARANTIZADO, RGP.OTROS_TERMINOS_GARANTIA,
                                RGP.RELACION_BIEN, RGP.DESC_GARANTIA, RGP.ID_GARANTIA_MODIFICAR, RGP.ID_MONEDA,
                                RGP.INSTRUMENTO_PUBLICO, RGP.CAMBIOS_BIENES_MONTO,RGP.NO_GARANTIA_PREVIA_OT,
                                RGP.ES_PRIORITARIA, RGP.OTROS_REGISTROS, RGP.TXT_REGISTROS
                        INTO RegGarantiasTemp.ID_GARANTIA_PEND, RegGarantiasTemp.MONTO_MAXIMO_GARANTIZADO, RegGarantiasTemp.OTROS_TERMINOS_GARANTIA,
                            RegGarantiasTemp.RELACION_BIEN, RegGarantiasTemp.DESC_GARANTIA, RegGarantiasTemp.ID_GARANTIA_MODIFICAR,
                            RegGarantiasTemp.ID_MONEDA, RegGarantiasTemp.INSTRUMENTO_PUBLICO, RegGarantiasTemp.CAMBIOS_BIENES_MONTO,
                            RegGarantiasTemp.NO_GARANTIA_PREVIA_OT, RegGarantiasTemp.ES_PRIORITARIA, RegGarantiasTemp.OTROS_REGISTROS,
                            RegGarantiasTemp.TXT_REGISTROS
                        FROM RUG_GARANTIAS_PENDIENTES RGP
                                 LEFT JOIN RUG_REL_TRAM_INC_GARAN RRL
                                           ON RGP.ID_GARANTIA_PEND = RRL.ID_GARANTIA_PEND
                        WHERE RRL.ID_TRAMITE_TEMP = peIdTramiteTemp;

                        INSERT INTO RUG_REL_GARANTIA_PARTES(ID_GARANTIA, ID_PERSONA, ID_PARTE, ID_RELACION, FECHA_REG, STATUS_REG)
                        SELECT RegGarantiasTemp.ID_GARANTIA_MODIFICAR, ID_PERSONA, ID_PARTE,vlIdRelacion, SYSDATE, 'AC'
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp AND STATUS_REG = 'AC';

                        UPDATE RUG_GARANTIAS a
                        SET a.MONTO_MAXIMO_GARANTIZADO=RegGarantiasTemp.MONTO_MAXIMO_GARANTIZADO,
                            a.OTROS_TERMINOS_GARANTIA=RegGarantiasTemp.OTROS_TERMINOS_GARANTIA,
                            a.RELACION_BIEN=RegGarantiasTemp.RELACION_BIEN,
                            a.DESC_GARANTIA=RegGarantiasTemp.DESC_GARANTIA, a.STATUS_REG = 'AC', A.FECHA_REG = SYSDATE ,
                            A.ID_GARANTIA_PEND = RegGarantiasTemp.ID_GARANTIA_PEND,
                            A.ID_ULTIMO_TRAMITE = vlIdTramite,
                            A.ID_RELACION = vlIdRelacion,
                            A.INSTRUMENTO_PUBLICO = RegGarantiasTemp.INSTRUMENTO_PUBLICO,
                            A.CAMBIOS_BIENES_MONTO = RegGarantiasTemp.CAMBIOS_BIENES_MONTO,
                            A.ID_MONEDA = RegGarantiasTemp.ID_MONEDA,
                            A.NO_GARANTIA_PREVIA_OT = RegGarantiasTemp.NO_GARANTIA_PREVIA_OT,
                            A.ES_PRIORITARIA = RegGarantiasTemp.ES_PRIORITARIA,
                            A.OTROS_REGISTROS = RegGarantiasTemp.OTROS_REGISTROS,
                            A.TXT_REGISTROS = RegGarantiasTemp.TXT_REGISTROS
                        WHERE a.ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        /* se inserta en un historico */
                        vlIdGarantiaH:= SEQ_REG_GARANTIA_H.nextval;
                        vlIdFirma:= SEQ_RUG_CTRL_FIRMA.NEXTVAL;  /* secuencia para la firma */

                        INSERT INTO RUG_GARANTIAS_H(ID_REGISTRO, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                                                    ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                                                    UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                                                    FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                                                    B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,FECHA_MODIF_REG, FECHA_REG, STATUS_REG, ID_GARANTIA_PEND, ID_MONEDA, INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO,
                                                    NO_GARANTIA_PREVIA_OT, ES_PRIORITARIA, OTROS_REGISTROS, TXT_REGISTROS)

                        SELECT vlIdGarantiaH, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                               ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                               UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                               FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                               B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,sysdate, SYSDATE, 'AC', ID_GARANTIA_PEND, ID_MONEDA, INSTRUMENTO_PUBLICO,
                               CAMBIOS_BIENES_MONTO,NO_GARANTIA_PREVIA_OT, ES_PRIORITARIA, OTROS_REGISTROS, TXT_REGISTROS
                        FROM RUG_GARANTIAS
                        WHERE ID_GARANTIA =  RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        --eliminamos primero
                        BEGIN
                            DELETE FROM RUG_GARANTIAS_BIENES
                            WHERE ID_TRAMITE = peIdTramiteTemp;
                        END;

                        --lo de bienes inicia
                        --SERIE FACTORAJE
                        OPEN cursBienesIcomp(peIdTramiteTemp);
                        LOOP
                            FETCH cursBienesIcomp INTO RegGarantiasBienTemp.TIPO_BIEN_ESPECIAL,
                                RegGarantiasBienTemp.TIPO_IDENTIFICADOR,
                                RegGarantiasBienTemp.IDENTIFICADOR,
                                RegGarantiasBienTemp.DESCRIPCION_BIEN,
                                RegGarantiasBienTemp.SERIE;
                            EXIT WHEN cursBienesIcomp%NOTFOUND;

                            vlIdGarantiaBien:=SEQ_GARAN_BIENES.NEXTVAL;
                            vlIdGarantiaBienH:=SEQ_GARAN_BIENES_H.NEXTVAL;

                            INSERT INTO RUG_GARANTIAS_BIENES(ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL,TIPO_IDENTIFICADOR,
                                                             IDENTIFICADOR,DESCRIPCION_BIEN,SERIE)
                            VALUES(vlIdGarantiaBien,vlIdTramite,RegGarantiasBienTemp.TIPO_BIEN_ESPECIAL,RegGarantiasBienTemp.TIPO_IDENTIFICADOR,
                                   RegGarantiasBienTemp.IDENTIFICADOR,RegGarantiasBienTemp.DESCRIPCION_BIEN, RegGarantiasBienTemp.SERIE);

                            INSERT INTO RUG_GARANTIAS_BIENES_H(ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL,TIPO_IDENTIFICADOR,
                                                               IDENTIFICADOR,DESCRIPCION_BIEN,SERIE)
                            SELECT ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL,TIPO_IDENTIFICADOR,IDENTIFICADOR,DESCRIPCION_BIEN,SERIE
                            FROM RUG_GARANTIAS_BIENES
                            WHERE ID_GARAN_BIEN = vlIdGarantiaBien;

                        END LOOP;
                        CLOSE cursBienesIcomp;
                        -- lo de bienes finaliza

                        INSERT INTO RUG_REL_TRAM_GARAN(ID_TRAMITE,ID_GARANTIA,B_TRAMITE_COMPLETO, STATUS_REG, FECHA_REG)
                        VALUES( vlIdTramite,RegGarantiasTemp.ID_GARANTIA_MODIFICAR, 'S', 'AC', SYSDATE);

                        BEGIN

                            INSERT INTO RUG_REL_TRAM_PARTES(ID_TRAMITE, ID_PERSONA, ID_PARTE, PER_JURIDICA, STATUS_REG, FECHA_REG)
                            SELECT vlIdTramite,ID_PERSONA, ID_PARTE, PER_JURIDICA, 'AC', SYSDATE
                            FROM RUG_REL_TRAM_INC_PARTES
                            WHERE ID_TRAMITE_TEMP = peIdTramiteTemp
                              AND STATUS_REG = 'AC';

                        Exception
                            WHEN NO_DATA_FOUND THEN
                                dbms_output.put_line('No Existe personas asociadas al tramite en la tabla RUG_REL_TRAM_INC_PARTES');
                        END;

                        /** Firma **/
                        INSERT INTO RUG_FIRMA_DOCTOS(ID_FIRMA, ID_TRAMITE_TEMP, ID_USUARIO_FIRMO) VALUES (vlIdFirma,peIdTramiteTemp,vlIdUsuarioFirmo);

                        -- SP_ACTUALIZAR_SALDO(160,vlIdUsuarioFirmo);
                        SELECT ID_TIPO_GARANTIA
                            INTO l_tipoInscripcion
                        FROM RUG_GARANTIAS
                            WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;


                        IF l_tipoInscripcion = 1 THEN
                            isInscripcion := 1;
                            
                            SELECT NVL(MAX(TIPO_BIEN_ESPECIAL),0) RESULTADO INTO intoInscripcionIn FROM RUG_GARANTIAS_BIENES WHERE ID_TRAMITE = (SELECT ID_ULTIMO_TRAMITE FROM RUG_GARANTIAS WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR)
                            AND TIPO_BIEN_ESPECIAL IN (1,2,3);
                            
                            IF intoInscripcionIn = 0 THEN
                                SELECT NVL(MAX(TIPO_BIEN_ESPECIAL),0) RESULTADO INTO intoInscripcionN FROM RUG_GARANTIAS_BIENES WHERE ID_TRAMITE = (SELECT ID_ULTIMO_TRAMITE FROM RUG_GARANTIAS WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR)
                                AND TIPO_BIEN_ESPECIAL IN (5,6);
                                IF intoInscripcionN = 5 THEN
                                    SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 41;
                                ELSIF intoInscripcionN = 6 THEN
                                    SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 39;
                                ELSE
                                    SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 40;
                                END IF;
                            ELSE
                                SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 40;
                            END IF;
                        
                        
                            REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'MODIFICACION_ARANCEL',valorInscripcionArancel , 'IN');
                            SP_ACTUALIZAR_SALDO(valorInscripcionArancel,vlIdUsuarioFirmo);
                        ELSIF l_tipoInscripcion = 2 THEN
                            isFactoraje := 1;
                            FOR C IN (SELECT COUNT(BI.ID_TRAMITE) AS CANTIDAD INTO vCantidadFctoraje FROM RUG_GARANTIAS GA
                                        INNER JOIN RUG_GARANTIAS_BIENES BI
                                        ON GA.ID_ULTIMO_TRAMITE = BI.ID_TRAMITE
                                        WHERE GA.ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR AND BI.TIPO_BIEN_ESPECIAL = 2)

                            LOOP
                                IF C.CANTIDAD > 0 THEN
                                    vSaldoFactoraje := 5 * C.CANTIDAD;
                                ELSE
                                     vSaldoFactoraje := C.CANTIDAD;
                                end if;
                            END LOOP;

                            IF vSaldoFactoraje > 0  and esFactoraje = 'factoraje' THEN
                                REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'TIPO_INSCRIPCION', 'FACTORAJE', 'IN');
                                SP_ACTUALIZAR_SALDO(vSaldoFactoraje,vlIdUsuarioFirmo);
                            end if;
                            REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'MODIFICACION_ARANCEL_FACTORAJE',vSaldoFactoraje , 'IN');
                        ELSIF l_tipoInscripcion = 16 THEN
                            isLeasing := 1;
                            REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'TIPO_INSCRIPCION', 'LEASING', 'IN');
                            SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 44;
                            SP_ACTUALIZAR_SALDO(valorInscripcionArancel,vlIdUsuarioFirmo);
                            REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'MODIFICACION_ARANCEL_LEASING',valorInscripcionArancel , 'IN');
                        end if;

                    END;


                WHEN RegTramitesIncomp.id_tipo_tramite= 8  THEN /* Transmision NO GENERA COSTO*/
                    BEGIN

                        --SE QUITO TODAS LAS VALIDACIONES PARA DISPARAR LA CANCELACION POR TRANSMISION Y LA INSCRIPCION POR TRANSMISION
                        -- FECHA DEL CAMBIO: 06/09/2010; RESPONSABLE: EDST
                        SELECT RGP.ID_GARANTIA_MODIFICAR, RGP.ID_RELACION
                        INTO vlIdGarModTrans, vlIdRelModTrans
                        FROM RUG_GARANTIAS_PENDIENTES RGP,
                             RUG_REL_TRAM_INC_GARAN RRL
                        WHERE RGP.ID_GARANTIA_PEND = RRL.ID_GARANTIA_PEND
                          AND RRL.ID_TRAMITE_TEMP = peIdTramiteTemp;


                        /* se inserta en un historico */
                        vlIdGarantiaH:= SEQ_REG_GARANTIA_H.nextval;
                        vlIdRelacion:= SEQ_RELACIONES.NEXTVAL;           /* secuencia de la tabla RUG_REG_GARANTIA_PARTES */

                        SELECT RGP.ID_GARANTIA_MODIFICAR, RGP.ID_GARANTIA_PEND
                        INTO RegGarantiasTemp.ID_GARANTIA_MODIFICAR, RegGarantiasTemp.ID_GARANTIA_PEND
                        FROM RUG_GARANTIAS_PENDIENTES RGP
                                 LEFT JOIN RUG_REL_TRAM_INC_GARAN RRL
                                           ON RGP.ID_GARANTIA_PEND = RRL.ID_GARANTIA_PEND
                        WHERE RRL.ID_TRAMITE_TEMP = peIdTramiteTemp;

                        -- :::Message Error::: 100:ORA-01403: no data found:ORA-06512: at "RUG.SP_ALTA_BITACORA_TRAMITE2", line 755 :428773
                        SELECT RELACION_BIEN
                        INTO vlRelBienTrans
                        FROM RUG_REL_GAR_TIPO_BIEN
                        WHERE ID_GARANTIA_PEND = RegGarantiasTemp.ID_GARANTIA_PEND
                          AND ROWNUM = 1;
                        --  :::Message Error::: 100:ORA-01403: no data found:ORA-06512: at "RUG.SP_ALTA_BITACORA_TRAMITE2", line 755 :428773

                        UPDATE RUG_GARANTIAS
                        SET ID_RELACION = vlIdRelacion, ID_ULTIMO_TRAMITE = vlIdTramite, B_ULTIMO_TRAMITE = 'V',
                            ID_GARANTIA_PEND = RegGarantiasTemp.ID_GARANTIA_PEND , RELACION_BIEN = vlRelBienTrans,
                            FECHA_REG = SYSDATE, STATUS_REG = 'AC'
                        WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        INSERT INTO RUG_GARANTIAS_H(ID_REGISTRO, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                                                    ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                                                    UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                                                    FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                                                    B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,FECHA_MODIF_REG, FECHA_REG, STATUS_REG, ID_GARANTIA_PEND, ID_MONEDA,
                                                    INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO,NO_GARANTIA_PREVIA_OT)
                        SELECT vlIdGarantiaH, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                               ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                               UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                               FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                               B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,sysdate, SYSDATE, 'AC', ID_GARANTIA_PEND, ID_MONEDA,INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO,NO_GARANTIA_PREVIA_OT
                        FROM RUG_GARANTIAS
                        WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        INSERT INTO RUG_REL_TRAM_GARAN(ID_TRAMITE,ID_GARANTIA,B_TRAMITE_COMPLETO, STATUS_REG, FECHA_REG)
                        VALUES( vlIdTramite,RegGarantiasTemp.ID_GARANTIA_MODIFICAR, 'S', 'AC', SYSDATE);

                        BEGIN
                            INSERT INTO RUG_REL_TRAM_PARTES(ID_TRAMITE, ID_PERSONA, ID_PARTE, PER_JURIDICA, STATUS_REG, FECHA_REG)
                            SELECT vlIdTramite,ID_PERSONA, ID_PARTE, PER_JURIDICA, 'AC', SYSDATE
                            FROM RUG_REL_TRAM_INC_PARTES
                            WHERE ID_TRAMITE_TEMP = peIdTramiteTemp AND STATUS_REG = 'AC';
                        Exception
                            WHEN NO_DATA_FOUND THEN
                                dbms_output.put_line('No Existe personas asociadas al tramite en la tabla RUG_REL_TRAM_INC_PARTES');
                        END;

                        INSERT INTO RUG_REL_GARANTIA_PARTES(ID_GARANTIA, ID_PERSONA, ID_PARTE, ID_RELACION, FECHA_REG, STATUS_REG)
                        SELECT RegGarantiasTemp.ID_GARANTIA_MODIFICAR, ID_PERSONA, ID_PARTE,vlIdRelacion, SYSDATE, 'AC'
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp AND STATUS_REG = 'AC';

                    END;


                WHEN RegTramitesIncomp.id_tipo_tramite= 9  THEN /* Prorroga o reduccion de vigencia SI GENERA COSTO*/
                    BEGIN

                        vlIdRelacion:= SEQ_RELACIONES.NEXTVAL;

                        /* se obtiene el id_garantia_temp, y lo datos a modificar */

                        SELECT RGP.ID_GARANTIA_PEND, RGP.ID_GARANTIA_MODIFICAR, RGP.VIGENCIA
                        INTO RegGarantiasTemp.ID_GARANTIA_PEND, RegGarantiasTemp.ID_GARANTIA_MODIFICAR, RegGarantiasTemp.VIGENCIA
                        FROM RUG_GARANTIAS_PENDIENTES RGP
                                 LEFT JOIN RUG_REL_TRAM_INC_GARAN RRL
                                           ON RGP.ID_GARANTIA_PEND = RRL.ID_GARANTIA_PEND
                        WHERE RRL.ID_TRAMITE_TEMP = peIdTramiteTemp;

                        /* se actualiza la garantia final */

                        UPDATE RUG_GARANTIAS a
                        SET a.VIGENCIA =RegGarantiasTemp.VIGENCIA,
                            A.STATUS_REG = 'AC',
                            A.FECHA_REG = SYSDATE,
                            A.ID_GARANTIA_PEND = RegGarantiasTemp.ID_GARANTIA_PEND,
                            A.ID_ULTIMO_TRAMITE = vlIdTramite,
                            A.ID_RELACION = vlIdRelacion
                        WHERE a.ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        /* se inserta en un historico */
                        vlIdGarantiaH:= SEQ_REG_GARANTIA_H.nextval;

                        INSERT INTO RUG_GARANTIAS_H(ID_REGISTRO, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                                                    ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                                                    UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                                                    FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                                                    B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,FECHA_MODIF_REG, FECHA_REG, STATUS_REG, ID_GARANTIA_PEND, ID_MONEDA,
                                                    INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO,NO_GARANTIA_PREVIA_OT, ES_PRIORITARIA, OTROS_REGISTROS, TXT_REGISTROS)
                        SELECT vlIdGarantiaH, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                               ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                               UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                               FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                               B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,sysdate, SYSDATE, 'AC', ID_GARANTIA_PEND, ID_MONEDA, INSTRUMENTO_PUBLICO,
                               CAMBIOS_BIENES_MONTO,NO_GARANTIA_PREVIA_OT, ES_PRIORITARIA, OTROS_REGISTROS, TXT_REGISTROS
                        FROM RUG_GARANTIAS
                        WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        --eliminamos primero
                        BEGIN
                            DELETE FROM RUG_GARANTIAS_BIENES
                            WHERE ID_TRAMITE = peIdTramiteTemp;
                        END;

                        --lo de bienes inicia
                        -- SERIE FACTORAJE
                        OPEN cursBienesIcomp(peIdTramiteTemp);
                        LOOP
                            FETCH cursBienesIcomp INTO RegGarantiasBienTemp.TIPO_BIEN_ESPECIAL,
                                RegGarantiasBienTemp.TIPO_IDENTIFICADOR,
                                RegGarantiasBienTemp.IDENTIFICADOR,
                                RegGarantiasBienTemp.DESCRIPCION_BIEN,
                                RegGarantiasBienTemp.SERIE;
                            EXIT WHEN cursBienesIcomp%NOTFOUND;

                            vlIdGarantiaBien:=SEQ_GARAN_BIENES.NEXTVAL;
                            vlIdGarantiaBienH:=SEQ_GARAN_BIENES_H.NEXTVAL;

                            INSERT INTO RUG_GARANTIAS_BIENES(ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL,TIPO_IDENTIFICADOR,
                                                             IDENTIFICADOR,DESCRIPCION_BIEN,SERIE)
                            VALUES(vlIdGarantiaBien,vlIdTramite,RegGarantiasBienTemp.TIPO_BIEN_ESPECIAL,RegGarantiasBienTemp.TIPO_IDENTIFICADOR,
                                   RegGarantiasBienTemp.IDENTIFICADOR,RegGarantiasBienTemp.DESCRIPCION_BIEN, RegGarantiasbienTemp.SERIE);

                            INSERT INTO RUG_GARANTIAS_BIENES_H(ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL,TIPO_IDENTIFICADOR,
                                                               IDENTIFICADOR,DESCRIPCION_BIEN,SERIE)
                            SELECT ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL,TIPO_IDENTIFICADOR,IDENTIFICADOR,DESCRIPCION_BIEN,SERIE
                            FROM RUG_GARANTIAS_BIENES
                            WHERE ID_GARAN_BIEN = vlIdGarantiaBien;

                        END LOOP;
                        CLOSE cursBienesIcomp;
                        -- lo de bienes finaliza


                        INSERT INTO RUG_REL_TRAM_GARAN(ID_TRAMITE,ID_GARANTIA,B_TRAMITE_COMPLETO, STATUS_REG, FECHA_REG)
                        VALUES( vlIdTramite,RegGarantiasTemp.ID_GARANTIA_MODIFICAR, 'S', 'AC', SYSDATE);

                        BEGIN
                            INSERT INTO RUG_REL_TRAM_PARTES(ID_TRAMITE, ID_PERSONA, ID_PARTE, PER_JURIDICA, STATUS_REG, FECHA_REG)
                            SELECT vlIdTramite,ID_PERSONA, ID_PARTE, PER_JURIDICA, 'AC', SYSDATE
                            FROM RUG_REL_TRAM_INC_PARTES
                            WHERE id_tramite_temp=peIdTramiteTemp;

                        Exception
                            WHEN NO_DATA_FOUND THEN
                                dbms_output.put_line('No Existe personas asociadas al tramite en la tabla RUG_REL_TRAM_INC_PARTES');
                        END;

                        INSERT INTO RUG_REL_GARANTIA_PARTES(ID_GARANTIA, ID_PERSONA, ID_PARTE, ID_RELACION, FECHA_REG, STATUS_REG)
                        SELECT RegGarantiasTemp.ID_GARANTIA_MODIFICAR, ID_PERSONA, ID_PARTE,vlIdRelacion, SYSDATE, 'AC'
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp AND STATUS_REG = 'AC';

                        /** Firma **/
                        vlIdFirma:= SEQ_RUG_CTRL_FIRMA.NEXTVAL;  /* secuencia para la firma */
                        INSERT INTO RUG_FIRMA_DOCTOS(ID_FIRMA, ID_TRAMITE_TEMP, ID_USUARIO_FIRMO) VALUES (vlIdFirma,peIdTramiteTemp,vlIdUsuarioFirmo);

                        SP_ACTUALIZAR_SALDO(100,vlIdUsuarioFirmo);

                    END;


                WHEN RegTramitesIncomp.id_tipo_tramite = 10 THEN /* ANOTACION SIN GARANTIA NO GENERA COSTO*/
                    BEGIN

                        vlBandera := 0;

                        SELECT COUNT(*)
                        INTO vlBandera
                        FROM RUG_REL_TRAM_PARTES A,
                             RUG_REL_TRAM_INC_PARTES B
                        WHERE A.ID_TRAMITE = vlIdTramite
                          AND A.ID_PERSONA = B.ID_PERSONA
                          AND A.ID_PARTE = B.ID_PARTE
                          AND B.ID_TRAMITE_TEMP = peIdTramiteTemp
                          AND B.STATUS_REG = 'AC';


                        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'vlIdTramite', vlIdTramite, 'IN');
                        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'vlBandera', vlBandera, 'IN');


                        IF vlBandera = 0 THEN

                            INSERT INTO RUG_REL_TRAM_PARTES(ID_TRAMITE, ID_PERSONA, ID_PARTE, PER_JURIDICA, STATUS_REG, FECHA_REG)
                            SELECT vlIdTramite,ID_PERSONA, ID_PARTE, PER_JURIDICA, 'AC', SYSDATE
                            FROM RUG_REL_TRAM_INC_PARTES
                            WHERE id_tramite_temp=peIdTramiteTemp AND STATUS_REG = 'AC';

                        END IF;

                    END;
                WHEN RegTramitesIncomp.id_tipo_tramite= 11  THEN /* Prorroga o reduccion de vigencia SI GENERA COSTO*/
                    BEGIN
                        dbms_output.put_line('ENTRO A LA CONSULTA');
                    END;


                WHEN RegTramitesIncomp.id_tipo_tramite=12  THEN /* ALTA DE ACREEDOR REPRESENTADO NO GENERA COSTO*/
                    BEGIN

                        INSERT INTO RUG_REL_TRAM_PARTES
                        SELECT vlIdTramite, ID_PERSONA, ID_PARTE, PER_JURIDICA, 'AC', SYSDATE
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp;

                        SELECT ID_PERSONA
                        INTO vlIdPersonaTramite12
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp;

                        UPDATE REL_USU_ACREEDOR
                        SET B_FIRMADO = 'Y', FECHA_REG= SYSDATE, STATUS_REG='AC'
                        WHERE ID_ACREEDOR = vlIdPersonaTramite12;

                        SELECT ID_USUARIO
                        INTO vlIdUsuario
                        FROM REL_USU_ACREEDOR
                        WHERE ID_ACREEDOR = vlIdPersonaTramite12;

                        SP_MODIFICA_PERFIL(vlIdUsuario, 1, vlresult, vlTextResult);

                    END;


                WHEN RegTramitesIncomp.id_tipo_tramite = 13  THEN /* Cancelacion por transmision NO GENERA COSTO*/
                    BEGIN

                        vlIdRelacion:= SEQ_RELACIONES.NEXTVAL;


                        -- se obtienen los datos de la garntia pendiente
                        SELECT RGP.ID_GARANTIA_PEND, RGP.ID_GARANTIA_MODIFICAR, RGP.VIGENCIA, RGP.RELACION_BIEN
                        INTO RegGarantiasTemp.ID_GARANTIA_PEND, RegGarantiasTemp.ID_GARANTIA_MODIFICAR, RegGarantiasTemp.VIGENCIA,
                            RegGarantiasTemp.RELACION_BIEN
                        FROM RUG_GARANTIAS_PENDIENTES RGP
                                 LEFT JOIN RUG_REL_TRAM_INC_GARAN RRL
                                           ON RGP.ID_GARANTIA_PEND = RRL.ID_GARANTIA_PEND
                        WHERE RRL.ID_TRAMITE_TEMP = peIdTramiteTemp;

                        -- insertar la relacion de personas de tramites incompletos  a la relacion de garantias partes
                        INSERT INTO RUG_REL_GARANTIA_PARTES(ID_GARANTIA, ID_PERSONA, ID_PARTE, ID_RELACION, FECHA_REG, STATUS_REG)
                        SELECT RegGarantiasTemp.ID_GARANTIA_MODIFICAR, ID_PERSONA, ID_PARTE,vlIdRelacion, SYSDATE, 'AC'
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp AND STATUS_REG = 'AC';

                        -- insertar el tramite incompleto en tramites completos
                        INSERT INTO RUG_REL_TRAM_PARTES(ID_TRAMITE, ID_PERSONA, ID_PARTE, PER_JURIDICA, STATUS_REG, FECHA_REG)
                        SELECT vlIdTramite,ID_PERSONA, ID_PARTE, PER_JURIDICA, 'AC', SYSDATE
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp AND STATUS_REG = 'AC';

                        -- cambiar la garantia a cancelacion por transmision (CT)
                        UPDATE RUG_GARANTIAS
                        SET STATUS_REG = 'AC',
                            FECHA_REG = SYSDATE,
                            GARANTIA_STATUS = 'CT',--DECODE(RegTramitesIncomp.id_tipo_tramite, 13, 'CT', 'CR'),
                            ID_GARANTIA_PEND = RegGarantiasTemp.ID_GARANTIA_PEND,
                            ID_ULTIMO_TRAMITE = vlIdTramite,
                            ID_RELACION = vlIdRelacion,
                            RELACION_BIEN = RegGarantiasTemp.RELACION_BIEN
                        WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        -- relacionar el tramite con la garantia
                        INSERT INTO RUG_REL_TRAM_GARAN(ID_TRAMITE,ID_GARANTIA,B_TRAMITE_COMPLETO, STATUS_REG, FECHA_REG)
                        VALUES( vlIdTramite, RegGarantiasTemp.ID_GARANTIA_MODIFICAR, 'S', 'AC', SYSDATE);

                        vlIdGarantiaH:= SEQ_REG_GARANTIA_H.nextval;

                        -- insertar el cambio en historico
                        INSERT INTO RUG_GARANTIAS_H(ID_REGISTRO, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                                                    ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                                                    UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                                                    FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                                                    B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,FECHA_MODIF_REG, FECHA_REG, STATUS_REG, ID_GARANTIA_PEND, ID_MONEDA,
                                                    INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO,NO_GARANTIA_PREVIA_OT)
                        SELECT vlIdGarantiaH, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                               ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                               UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                               FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                               B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,sysdate, SYSDATE, 'AC', ID_GARANTIA_PEND, ID_MONEDA, INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO,NO_GARANTIA_PREVIA_OT
                        FROM RUG_GARANTIAS
                        WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;


                        INSERT INTO RUG.RUG_BITAC_TRAMITES (ID_TRAMITE_TEMP, ID_STATUS,  ID_PASO,FECHA_STATUS,ID_TIPO_TRAMITE, FECHA_REG, STATUS_REG)
                        VALUES (peIdTramiteTemp,3,peIdPaso,sysdate,RegTramitesIncomp.id_tipo_tramite, SYSDATE, 'AC');

                    END;


                WHEN RegTramitesIncomp.id_tipo_tramite = 14  THEN /* ALTA DE USUARIOS NO GENERA COSTO*/
                    BEGIN

                        INSERT INTO RUG_REL_TRAM_PARTES
                        SELECT vlIdTramite, ID_PERSONA, ID_PARTE, PER_JURIDICA, 'AC', SYSDATE
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp;


                        SELECT ID_PERSONA
                        INTO vlIdAcreedor
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp
                          AND ID_PARTE = 4;

                        SELECT ID_PERSONA
                        INTO vlIdPersonaTramite12
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp
                          AND ID_PARTE = 5;

                        UPDATE RUG_SECU_USUARIOS
                        SET B_FIRMADO = 'V',
                            FH_REGISTRO= SYSDATE,
                            SIT_USUARIO='AC'
                        WHERE ID_PERSONA = vlIdPersonaTramite12;

                        UPDATE REL_USU_ACREEDOR
                        SET B_FIRMADO = 'Y'
                        WHERE ID_USUARIO = vlIdPersonaTramite12
                          AND ID_ACREEDOR = vlIdAcreedor;

                        DELETE RUG_TRAMITES_REASIGNADOS
                        WHERE ID_ACREEDOR = vlIdAcreedor
                          AND ID_TRAMITE_TEMP IN (SELECT ID_TRAMITE_TEMP
                                                  FROM RUG.V_TRAMITES_TERMINADOS
                                                  WHERE ID_PERSONA_LOGIN = vlIdPersonaTramite12
                                                    AND ID_ACREEDOR = vlIdAcreedor);

                        DELETE RUG_TRAMITES_REASIGNADOS
                        WHERE ID_ACREEDOR = vlIdAcreedor
                          AND ID_TRAMITE_TEMP IN (
                            SELECT ID_TRAMITE_TEMP
                            FROM RUG.V_TRAMITES_PENDIENTES
                            WHERE ID_PERSONA_LOGIN = vlIdPersonaTramite12
                              AND ID_ACREEDOR = vlIdAcreedor);


                    END;


                WHEN RegTramitesIncomp.id_tipo_tramite = 15  THEN /* Inscripcion por transmision NO GENERA COSTSO*/
                    BEGIN

                        ----------   I N S C R I P C I O N   ----------

                        vlIdGarantia:=SEQ_GARANTIAs.NEXTVAL;           /* secuencia de la tabla de tramites */
                        vlIdRelacion:= SEQ_RELACIONES.NEXTVAL;           /* secuencia de la tabla RUG_REG_GARANTIA_PARTES */


                        -- obtener la inforacion para generar la nueva garantia
                        SELECT b.ID_TIPO_GARANTIA, b.NUM_GARANTIA, b.DESC_GARANTIA, b.MESES_GARANTIA, b.ID_PERSONA, b.ID_ANOTACION, b.ID_RELACION,
                               b.RELACION_BIEN, b.VALOR_BIENES, b.TIPOS_BIENES_MUEBLES, b.UBICACION_BIENES, b.FOLIO_MERCANTIL, b.PATH_DOC_GARANTIA,
                               b.OTROS_TERMINOS_GARANTIA, b.FECHA_INSCR, b.FECHA_FIN_GAR, b.VIGENCIA, b.GARANTIA_CERTIFICADA, b.GARANTIA_STATUS, b.ID_ULTIMO_TRAMITE,
                               B_ULTIMO_TRAMITE, b.MONTO_MAXIMO_GARANTIZADO, b.ID_GARANTIA_PEND, B.ID_MONEDA, B.INSTRUMENTO_PUBLICO, B.CAMBIOS_BIENES_MONTO,
                               B.ID_GARANTIA_MODIFICAR, b.NO_GARANTIA_PREVIA_OT
                        INTO RegGarantiasTemp.ID_TIPO_GARANTIA, RegGarantiasTemp.NUM_GARANTIA, RegGarantiasTemp.DESC_GARANTIA,
                            RegGarantiasTemp.MESES_GARANTIA, RegGarantiasTemp.ID_PERSONA, RegGarantiasTemp.ID_ANOTACION, RegGarantiasTemp.ID_RELACION,
                            RegGarantiasTemp.RELACION_BIEN, RegGarantiasTemp.VALOR_BIENES, RegGarantiasTemp.TIPOS_BIENES_MUEBLES, RegGarantiasTemp.UBICACION_BIENES,
                            RegGarantiasTemp.FOLIO_MERCANTIL, RegGarantiasTemp.PATH_DOC_GARANTIA, RegGarantiasTemp.OTROS_TERMINOS_GARANTIA, RegGarantiasTemp.FECHA_INSCR,
                            RegGarantiasTemp.FECHA_FIN_GAR, RegGarantiasTemp.VIGENCIA, RegGarantiasTemp.GARANTIA_CERTIFICADA, RegGarantiasTemp.GARANTIA_STATUS,
                            RegGarantiasTemp.ID_ULTIMO_TRAMITE, RegGarantiasTemp.B_ULTIMO_TRAMITE, RegGarantiasTemp.MONTO_MAXIMO_GARANTIZADO, RegGarantiasTemp.ID_GARANTIA_PEND,
                            RegGarantiasTemp.ID_MONEDA, RegGarantiasTemp.INSTRUMENTO_PUBLICO, RegGarantiasTemp.CAMBIOS_BIENES_MONTO, RegGarantiasTemp.ID_GARANTIA_MODIFICAR,RegGarantiasTemp.NO_GARANTIA_PREVIA_OT
                        FROM RUG_GARANTIAS_PENDIENTES B,RUG_REL_TRAM_INC_GARAN C
                        WHERE B.ID_GARANTIA_PEND = c.id_garantia_pend
                          AND C.ID_TRAMITE_TEMP = peIdTramiteTemp;


                        -- insertar la garantia y que el tramite sea por transmision
                        UPDATE RUG_GARANTIAS
                        SET STATUS_REG = 'AC',
                            FECHA_REG = SYSDATE,
                            GARANTIA_STATUS = 'AC',--DECODE(RegTramitesIncomp.id_tipo_tramite, 13, 'CT', 'CR'),
                            ID_GARANTIA_PEND = RegGarantiasTemp.ID_GARANTIA_PEND,
                            ID_ULTIMO_TRAMITE = vlIdTramite,
                            ID_RELACION = vlIdRelacion,
                            RELACION_BIEN =  RegGarantiasTemp.RELACION_BIEN
                        WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        -- relacionar el tramite con la garantia
                        INSERT INTO RUG_REL_TRAM_GARAN(ID_TRAMITE,ID_GARANTIA,B_TRAMITE_COMPLETO, STATUS_REG, FECHA_REG)
                        VALUES( vlIdTramite, RegGarantiasTemp.ID_GARANTIA_MODIFICAR, 'S', 'AC', SYSDATE);

                        vlIdGarantiaH:= SEQ_REG_GARANTIA_H.nextval;

                        -- guardar el historico
                        INSERT INTO RUG_GARANTIAS_H(ID_REGISTRO, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                                                    ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                                                    UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                                                    FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                                                    B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,FECHA_MODIF_REG, FECHA_REG, STATUS_REG, ID_GARANTIA_PEND, ID_MONEDA,
                                                    INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO,NO_GARANTIA_PREVIA_OT)
                        SELECT vlIdGarantiaH, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                               ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                               UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                               FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                               B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO, sysdate, SYSDATE, 'AC', ID_GARANTIA_PEND, ID_MONEDA, INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO,RUG_GARANTIAS.NO_GARANTIA_PREVIA_OT
                        FROM RUG_GARANTIAS
                        WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        BEGIN

                            INSERT INTO RUG_REL_TRAM_PARTES(ID_TRAMITE, ID_PERSONA, ID_PARTE, PER_JURIDICA, STATUS_REG, FECHA_REG)
                            SELECT vlIdTramite,ID_PERSONA, ID_PARTE, PER_JURIDICA, 'AC', SYSDATE
                            FROM RUG_REL_TRAM_INC_PARTES
                            WHERE ID_TRAMITE_TEMP = peIdTramiteTemp
                              AND STATUS_REG = 'AC';

                        Exception
                            WHEN NO_DATA_FOUND THEN
                                dbms_output.put_line('No Existe personas asociadas al tramite en la tabla RUG_REL_TRAM_INC_PARTES');
                        END;

                        INSERT INTO RUG_REL_GARANTIA_PARTES(ID_GARANTIA, ID_PERSONA, ID_PARTE, ID_RELACION, FECHA_REG, STATUS_REG)
                        SELECT RegGarantiasTemp.ID_GARANTIA_MODIFICAR, ID_PERSONA, ID_PARTE,vlIdRelacion, SYSDATE, 'AC'
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp AND STATUS_REG = 'AC';

                        INSERT INTO RUG.RUG_BITAC_TRAMITES (ID_TRAMITE_TEMP, ID_STATUS,  ID_PASO,FECHA_STATUS,ID_TIPO_TRAMITE, FECHA_REG, STATUS_REG)
                        VALUES (peIdTramiteTemp,3,peIdPaso,sysdate,15, SYSDATE, 'AC');

                    END;


                WHEN RegTramitesIncomp.id_tipo_tramite = 19  THEN /* Modificacion de Acreedor NO GENERA COSTO*/
                    BEGIN

                        vlIdAcreedor      := NULL;
                        vlIdAcreedorNuevo := NULL;
                        vlId_Domicilio    := NULL;
                        vlDomicilio       := NULL;
                        vlTelefono        := NULL;

                        SELECT ID_ACREEDOR, ID_ACREEDOR_NUEVO
                        INTO vlIdAcreedor, vlIdAcreedorNuevo
                        FROM RUG_REL_MODIFICA_ACREEDOR
                        WHERE ID_TRAMITE_TEMP =  peIdTramiteTemp
                          AND STATUS_REG = 'AC'
                          AND B_FIRMADO = 'N';


                        SELECT ID_DOMICILIO, PER_JURIDICA
                        INTO  vlId_Domicilio, vlTipoPersona
                        FROM RUG_PERSONAS
                        WHERE ID_PERSONA = vlIdAcreedor;


                        SELECT PER_JURIDICA, NOMBRE_PERSONA, AP_PATERNO_PERSONA, AP_MATERNO_PERSONA, RAZON_SOCIAL, FOLIO_MERCANTIL, RFC, CURP, CALLE,
                               NUM_EXTERIOR, NUM_INTERIOR, ID_COLONIA, ID_NACIONALIDAD, E_MAIL, TELEFONO, EXTENSION,  CURP_DOC, UBICA_DOMICILIO_1,
                               UBICA_DOMICILIO_2, POBLACION,ZONA_POSTAL, ID_DOMICILIO, LOCALIDAD,ID_PAIS_RESIDENCIA, ID_DOMICILIO
                        INTO RegAcreedor.PER_JURIDICA, RegAcreedor.NOMBRE_PERSONA, RegAcreedor.AP_PATERNO_PERSONA, RegAcreedor.AP_MATERNO_PERSONA,
                            RegAcreedor.RAZON_SOCIAL, RegAcreedor.FOLIO_MERCANTIL, RegAcreedor.RFC, RegAcreedor.CURP, RegAcreedor.CALLE,
                            RegAcreedor.NUM_EXTERIOR, RegAcreedor.NUM_INTERIOR, RegAcreedor.ID_COLONIA, RegAcreedor.ID_NACIONALIDAD, RegAcreedor.E_MAIL,
                            RegAcreedor.TELEFONO, RegAcreedor.EXTENSION, RegAcreedor.CURP_DOC, RegAcreedor.UBICA_DOMICILIO_1, RegAcreedor.UBICA_DOMICILIO_2,
                            RegAcreedor.POBLACION, RegAcreedor.ZONA_POSTAL, RegAcreedor.ID_DOMICILIO, RegAcreedor.LOCALIDAD, RegAcreedor.ID_PAIS_RESIDENCIA,
                            RegAcreedor.ID_DOMICILIO
                        FROM V_TRAMITES_INCOMP_PARTES
                        WHERE ID_TRAMITE = peIdTramiteTemp
                          AND ID_PARTE = 4;


                        UPDATE RUG_PERSONAS
                        SET RFC = RegAcreedor.RFC, ID_NACIONALIDAD = RegAcreedor.ID_NACIONALIDAD, PER_JURIDICA = RegAcreedor.PER_JURIDICA,
                            FOLIO_MERCANTIL = RegAcreedor.FOLIO_MERCANTIL, E_MAIL = RegAcreedor.E_MAIL, CURP_DOC = RegAcreedor.CURP_DOC, ID_DOMICILIO = RegAcreedor.ID_DOMICILIO
                        WHERE ID_PERSONA = vlIdAcreedor;


                        IF(RegAcreedor.PER_JURIDICA = 'PF') THEN

                            IF(vlTipoPersona = 'PF') THEN

                                UPDATE RUG_PERSONAS_FISICAS
                                SET NOMBRE_PERSONA = RegAcreedor.NOMBRE_PERSONA, AP_PATERNO = RegAcreedor.AP_PATERNO_PERSONA,
                                    AP_MATERNO = RegAcreedor.AP_MATERNO_PERSONA, CURP = RegAcreedor.CURP
                                WHERE ID_PERSONA = vlIdAcreedor;

                            ELSE

                                INSERT INTO RUG_PERSONAS_FISICAS(ID_PERSONA, NOMBRE_PERSONA, AP_PATERNO, AP_MATERNO, CURP)
                                VALUES(vlIdAcreedor, RegAcreedor.NOMBRE_PERSONA, RegAcreedor.AP_PATERNO_PERSONA, RegAcreedor.AP_MATERNO_PERSONA, RegAcreedor.CURP);

                            END IF;

                            DELETE RUG_PERSONAS_MORALES
                            WHERE ID_PERSONA = vlIdAcreedor;

                        ELSE

                            IF(vlTipoPersona = 'PM') THEN

                                UPDATE RUG_PERSONAS_MORALES
                                SET RAZON_SOCIAL = RegAcreedor.RAZON_SOCIAL
                                WHERE ID_PERSONA = vlIdAcreedor;

                            ELSE

                                INSERT INTO RUG_PERSONAS_MORALES (ID_PERSONA, RAZON_SOCIAL)
                                VALUES(vlIdAcreedor, RegAcreedor.RAZON_SOCIAL);


                            END IF;

                            DELETE RUG_PERSONAS_FISICAS
                            WHERE ID_PERSONA = vlIdAcreedor;


                        END IF;


                        IF(TRIM(RegAcreedor.UBICA_DOMICILIO_1) IS NULL OR TRIM(RegAcreedor.UBICA_DOMICILIO_2) IS NULL OR
                           TRIM(RegAcreedor.POBLACION) IS NULL OR TRIM(RegAcreedor.ZONA_POSTAL) IS NULL ) THEN


                            SELECT COUNT(*)
                            INTO vlDomicilio
                            FROM RUG_DOMICILIOS
                            WHERE ID_DOMICILIO = vlId_Domicilio;

                            IF(vlDomicilio != 0) THEN

                                UPDATE RUG_DOMICILIOS
                                SET CALLE = RegAcreedor.CALLE, NUM_EXTERIOR = RegAcreedor.NUM_EXTERIOR, NUM_INTERIOR = RegAcreedor.NUM_INTERIOR,
                                    ID_COLONIA = RegAcreedor.ID_COLONIA, LOCALIDAD = RegAcreedor.LOCALIDAD
                                WHERE ID_DOMICILIO = vlId_Domicilio;

                            ELSE

                                INSERT INTO RUG_DOMICILIOS(ID_DOMICILIO, CALLE, NUM_EXTERIOR, NUM_INTERIOR, ID_COLONIA, LOCALIDAD)
                                VALUES(vlId_Domicilio, RegAcreedor.CALLE, RegAcreedor.NUM_EXTERIOR, RegAcreedor.NUM_INTERIOR, RegAcreedor.ID_COLONIA, RegAcreedor.LOCALIDAD);

                            END IF;



                            DELETE RUG_DOMICILIOS_EXT
                            WHERE ID_DOMICILIO = vlId_Domicilio;


                        ELSE


                            SELECT COUNT(*)
                            INTO vlDomicilio
                            FROM RUG_DOMICILIOS_EXT
                            WHERE ID_DOMICILIO = vlId_Domicilio;


                            IF(vlDomicilio != 0) THEN

                                UPDATE RUG_DOMICILIOS_EXT
                                SET UBICA_DOMICILIO_1 = RegAcreedor.UBICA_DOMICILIO_1, UBICA_DOMICILIO_2 = RegAcreedor.UBICA_DOMICILIO_2,
                                    POBLACION = RegAcreedor.POBLACION, ZONA_POSTAL = RegAcreedor.ZONA_POSTAL, ID_PAIS_RESIDENCIA = RegAcreedor.ID_PAIS_RESIDENCIA
                                WHERE ID_DOMICILIO = vlId_Domicilio;


                            ELSE

                                INSERT INTO RUG_DOMICILIOS_EXT(ID_DOMICILIO, UBICA_DOMICILIO_1, UBICA_DOMICILIO_2, POBLACION, ZONA_POSTAL, ID_PAIS_RESIDENCIA)
                                VALUES(vlId_Domicilio, RegAcreedor.UBICA_DOMICILIO_1, RegAcreedor.UBICA_DOMICILIO_2, RegAcreedor.POBLACION, RegAcreedor.ZONA_POSTAL, RegAcreedor.ID_PAIS_RESIDENCIA);


                            END IF;



                            DELETE RUG_DOMICILIOS
                            WHERE ID_DOMICILIO = vlId_Domicilio;


                        END IF;


                        SELECT COUNT(*)
                        INTO vlTelefono
                        FROM RUG_TELEFONOS
                        WHERE ID_PERSONA = vlIdAcreedor;

                        IF(vlIdAcreedor != 0) THEN

                            UPDATE RUG_TELEFONOS
                            SET TELEFONO = RegAcreedor.TELEFONO, EXTENSION = RegAcreedor.EXTENSION
                            WHERE ID_PERSONA = vlIdAcreedor;

                        ELSE

                            INSERT INTO RUG_TELEFONOS(ID_PERSONA,  TELEFONO, EXTENSION, FECHA_REG, STATUS_REG)
                            VALUES(vlIdAcreedor, RegAcreedor.TELEFONO, RegAcreedor.EXTENSION, SYSDATE, 'AC');

                        END IF;


                        UPDATE RUG_REL_MODIFICA_ACREEDOR
                        SET B_FIRMADO = 'Y', STATUS_REG = 'IN'
                        WHERE ID_TRAMITE_TEMP =  peIdTramiteTemp
                          AND STATUS_REG = 'AC';



                        UPDATE RUG_REL_MODIFICA_ACREEDOR
                        SET STATUS_REG = 'IN'
                        WHERE ID_ACREEDOR =  vlIdAcreedor;


                        UPDATE TRAMITES_RUG_INCOMP
                        SET STATUS_REG = 'IN', ID_STATUS_TRAM = 7
                        WHERE ID_TRAMITE_TEMP IN (SELECT ID_TRAMITE_TEMP FROM RUG_REL_MODIFICA_ACREEDOR
                                                  WHERE ID_ACREEDOR =  vlIdAcreedor);


                        UPDATE RUG_PERSONAS
                        SET SIT_PERSONA = 'IN'
                        WHERE ID_PERSONA = vlIdAcreedorNuevo;

                    END;

                WHEN RegTramitesIncomp.id_tipo_tramite = 18 THEN
                    BEGIN
                        INSERT INTO RUG.RUG_REL_TRAM_PARTES
                        SELECT vlIdTramite, ID_PERSONA, ID_PARTE, PER_JURIDICA, 'AC', SYSDATE
                        FROM RUG.RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp;

                    END;

                WHEN RegTramitesIncomp.id_tipo_tramite= 30  THEN /* Ejecucion SI GENERA COSTO*/
                    BEGIN

                        vlIdRelacion:= SEQ_RELACIONES.NEXTVAL;

                        /* se obtiene el id_garantia_temp, y lo datos a modificar */

                        SELECT RGP.ID_GARANTIA_PEND, RGP.ID_GARANTIA_MODIFICAR
                        INTO RegGarantiasTemp.ID_GARANTIA_PEND, RegGarantiasTemp.ID_GARANTIA_MODIFICAR
                        FROM RUG_GARANTIAS_PENDIENTES RGP
                                 LEFT JOIN RUG_REL_TRAM_INC_GARAN RRL
                                           ON RGP.ID_GARANTIA_PEND = RRL.ID_GARANTIA_PEND
                        WHERE RRL.ID_TRAMITE_TEMP = peIdTramiteTemp;

                        /* se actualiza la garantia final */

                        UPDATE RUG_GARANTIAS a
                        SET A.STATUS_REG = 'AC',
                            A.FECHA_REG = SYSDATE,
                            A.ID_GARANTIA_PEND = RegGarantiasTemp.ID_GARANTIA_PEND,
                            A.ID_ULTIMO_TRAMITE = vlIdTramite,
                            A.ID_RELACION = vlIdRelacion
                        WHERE a.ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        /* se inserta en un historico */
                        vlIdGarantiaH:= SEQ_REG_GARANTIA_H.nextval;

                        INSERT INTO RUG_GARANTIAS_H(ID_REGISTRO, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                                                    ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                                                    UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                                                    FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                                                    B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,FECHA_MODIF_REG, FECHA_REG, STATUS_REG, ID_GARANTIA_PEND, ID_MONEDA,
                                                    INSTRUMENTO_PUBLICO, CAMBIOS_BIENES_MONTO,NO_GARANTIA_PREVIA_OT,ES_PRIORITARIA, OTROS_REGISTROS, TXT_REGISTROS)
                        SELECT vlIdGarantiaH, ID_GARANTIA, ID_TIPO_GARANTIA, NUM_GARANTIA, DESC_GARANTIA, MESES_GARANTIA,
                               ID_PERSONA, ID_ANOTACION, ID_RELACION, RELACION_BIEN, VALOR_BIENES, TIPOS_BIENES_MUEBLES,
                               UBICACION_BIENES, FOLIO_MERCANTIL, PATH_DOC_GARANTIA, OTROS_TERMINOS_GARANTIA, FECHA_INSCR,
                               FECHA_FIN_GAR, VIGENCIA, GARANTIA_CERTIFICADA, GARANTIA_STATUS, ID_ULTIMO_TRAMITE,
                               B_ULTIMO_TRAMITE, MONTO_MAXIMO_GARANTIZADO,sysdate, SYSDATE, 'AC', ID_GARANTIA_PEND, ID_MONEDA, INSTRUMENTO_PUBLICO,
                               CAMBIOS_BIENES_MONTO,NO_GARANTIA_PREVIA_OT, ES_PRIORITARIA, OTROS_REGISTROS, TXT_REGISTROS
                        FROM RUG_GARANTIAS
                        WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;

                        --eliminamos primero
                        BEGIN
                            DELETE FROM RUG_GARANTIAS_BIENES
                            WHERE ID_TRAMITE = peIdTramiteTemp;
                        END;

                        --lo de bienes inicia
                        -- SERIE FACTORAJE
                        OPEN cursBienesIcomp(peIdTramiteTemp);
                        LOOP
                            FETCH cursBienesIcomp INTO RegGarantiasBienTemp.TIPO_BIEN_ESPECIAL,
                                RegGarantiasBienTemp.TIPO_IDENTIFICADOR,
                                RegGarantiasBienTemp.IDENTIFICADOR,
                                RegGarantiasBienTemp.DESCRIPCION_BIEN,
                                RegGarantiasBienTemp.SERIE;
                            EXIT WHEN cursBienesIcomp%NOTFOUND;

                            vlIdGarantiaBien:=SEQ_GARAN_BIENES.NEXTVAL;
                            vlIdGarantiaBienH:=SEQ_GARAN_BIENES_H.NEXTVAL;

                            INSERT INTO RUG_GARANTIAS_BIENES(ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL,TIPO_IDENTIFICADOR,
                                                             IDENTIFICADOR,DESCRIPCION_BIEN,SERIE)
                            VALUES(vlIdGarantiaBien,vlIdTramite,RegGarantiasBienTemp.TIPO_BIEN_ESPECIAL,RegGarantiasBienTemp.TIPO_IDENTIFICADOR,
                                   RegGarantiasBienTemp.IDENTIFICADOR,RegGarantiasBienTemp.DESCRIPCION_BIEN, RegGarantiasBienTemp.SERIE);

                            INSERT INTO RUG_GARANTIAS_BIENES_H(ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL,TIPO_IDENTIFICADOR,
                                                               IDENTIFICADOR,DESCRIPCION_BIEN,SERIE)
                            SELECT ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL,TIPO_IDENTIFICADOR,IDENTIFICADOR,DESCRIPCION_BIEN,SERIE
                            FROM RUG_GARANTIAS_BIENES
                            WHERE ID_GARAN_BIEN = vlIdGarantiaBien;

                        END LOOP;
                        CLOSE cursBienesIcomp;
                        -- lo de bienes finaliza

                        INSERT INTO RUG_REL_TRAM_GARAN(ID_TRAMITE,ID_GARANTIA,B_TRAMITE_COMPLETO, STATUS_REG, FECHA_REG)
                        VALUES( vlIdTramite,RegGarantiasTemp.ID_GARANTIA_MODIFICAR, 'S', 'AC', SYSDATE);

                        BEGIN
                            INSERT INTO RUG_REL_TRAM_PARTES(ID_TRAMITE, ID_PERSONA, ID_PARTE, PER_JURIDICA, STATUS_REG, FECHA_REG)
                            SELECT vlIdTramite,ID_PERSONA, ID_PARTE, PER_JURIDICA, 'AC', SYSDATE
                            FROM RUG_REL_TRAM_INC_PARTES
                            WHERE id_tramite_temp=peIdTramiteTemp;

                        Exception
                            WHEN NO_DATA_FOUND THEN
                                dbms_output.put_line('No Existe personas asociadas al tramite en la tabla RUG_REL_TRAM_INC_PARTES');
                        END;

                        INSERT INTO RUG_REL_GARANTIA_PARTES(ID_GARANTIA, ID_PERSONA, ID_PARTE, ID_RELACION, FECHA_REG, STATUS_REG)
                        SELECT RegGarantiasTemp.ID_GARANTIA_MODIFICAR, ID_PERSONA, ID_PARTE,vlIdRelacion, SYSDATE, 'AC'
                        FROM RUG_REL_TRAM_INC_PARTES
                        WHERE ID_TRAMITE_TEMP = peIdTramiteTemp AND STATUS_REG = 'AC';

                        /** Firma **/
                        vlIdFirma:= SEQ_RUG_CTRL_FIRMA.NEXTVAL;  /* secuencia para la firma */
                        INSERT INTO RUG_FIRMA_DOCTOS(ID_FIRMA, ID_TRAMITE_TEMP, ID_USUARIO_FIRMO) VALUES (vlIdFirma,peIdTramiteTemp,vlIdUsuarioFirmo);

                        --SP_ACTUALIZAR_SALDO(160,vlIdUsuarioFirmo);
                        SELECT ID_TIPO_GARANTIA
                            INTO l_tipoInscripcion
                        FROM RUG_GARANTIAS
                            WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR;
                        
                        IF l_tipoInscripcion = 1 THEN
                            isInscripcion := 1;
                            
                            SELECT NVL(MAX(TIPO_BIEN_ESPECIAL),0) RESULTADO INTO intoInscripcionIn FROM RUG_GARANTIAS_BIENES WHERE ID_TRAMITE = (SELECT ID_ULTIMO_TRAMITE FROM RUG_GARANTIAS WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR)
                            AND TIPO_BIEN_ESPECIAL IN (1,2,3);
                            
                            IF intoInscripcionIn = 0 THEN
                                SELECT NVL(MAX(TIPO_BIEN_ESPECIAL),0) RESULTADO INTO intoInscripcionN FROM RUG_GARANTIAS_BIENES WHERE ID_TRAMITE = (SELECT ID_ULTIMO_TRAMITE FROM RUG_GARANTIAS WHERE ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR)
                                AND TIPO_BIEN_ESPECIAL IN (5,6);
                                IF intoInscripcionN = 5 THEN
                                    SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 41;
                                ELsIF intoInscripcionN = 6 THEN
                                    SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 39;
                                ELSE
                                    SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 43;
                                END IF;
                            ELSE
                                SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 43;
                            END IF;
                        
                        
                            REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'MODIFICACION_ARANCEL',valorInscripcionArancel , 'IN');
                            SP_ACTUALIZAR_SALDO(valorInscripcionArancel,vlIdUsuarioFirmo);
                        ELSIF l_tipoInscripcion = 2 THEN
                            isFactoraje := 1;
                            FOR C IN (SELECT COUNT(BI.ID_TRAMITE) AS CANTIDAD INTO vCantidadFctoraje FROM RUG_GARANTIAS GA
                                        INNER JOIN RUG_GARANTIAS_BIENES BI
                                        ON GA.ID_ULTIMO_TRAMITE = BI.ID_TRAMITE
                                        WHERE GA.ID_GARANTIA = RegGarantiasTemp.ID_GARANTIA_MODIFICAR AND BI.TIPO_BIEN_ESPECIAL = 2)

                            LOOP
                                IF C.CANTIDAD > 0 THEN
                                    vSaldoFactoraje := 5 * C.CANTIDAD;
                                ELSE
                                     vSaldoFactoraje := C.CANTIDAD;
                                end if;
                            END LOOP;

                            IF vSaldoFactoraje > 0  and esFactoraje = 'factoraje' THEN
                                REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'TIPO_INSCRIPCION', 'FACTORAJE', 'IN');
                                SP_ACTUALIZAR_SALDO(vSaldoFactoraje,vlIdUsuarioFirmo);
                            end if;
                            REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'MODIFICACION_ARANCEL_FACTORAJE',vSaldoFactoraje , 'IN');
                        ELSIF l_tipoInscripcion = 16 THEN
                            isLeasing := 1;
                            REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'TIPO_INSCRIPCION', 'LEASING', 'IN');
                            SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 46;
                            SP_ACTUALIZAR_SALDO(valorInscripcionArancel,vlIdUsuarioFirmo);
                            REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ALTA_BITACORA_TRAMITE_2', 'EJECUCION_ARANCEL_LEASING',valorInscripcionArancel , 'IN');
                        end if;

                    END;


                WHEN RegTramitesIncomp.id_tipo_tramite  IN(26, 27, 28, 29)  THEN /* ANOTACION SIN GARANTIA - TRAMITES */ /* GGR 12.14.2013   MMSECN2013-81 NO GENERA COSTO*/
                    BEGIN

                        vlBandera := 0;

                        SELECT COUNT(*)
                        INTO vlBandera
                        FROM RUG_REL_TRAM_PARTES A,
                             RUG_REL_TRAM_INC_PARTES B
                        WHERE A.ID_TRAMITE = vlIdTramite
                          AND A.ID_PERSONA = B.ID_PERSONA
                          AND A.ID_PARTE = B.ID_PARTE
                          AND B.ID_TRAMITE_TEMP = peIdTramiteTemp
                          AND B.STATUS_REG = 'AC';


                        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'vlIdTramite', vlIdTramite, 'IN');
                        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'RegTramitesIncomp.id_tipo_tramite', RegTramitesIncomp.id_tipo_tramite, 'IN');
                        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'vlBandera', vlBandera, 'IN');


                        IF vlBandera = 0 THEN

                            INSERT INTO RUG_REL_TRAM_PARTES(ID_TRAMITE, ID_PERSONA, ID_PARTE, PER_JURIDICA, STATUS_REG, FECHA_REG)
                            SELECT vlIdTramite,ID_PERSONA, ID_PARTE, PER_JURIDICA, 'AC', SYSDATE
                            FROM RUG_REL_TRAM_INC_PARTES
                            WHERE id_tramite_temp=peIdTramiteTemp AND STATUS_REG = 'AC';

                        END IF;

                        UPDATE RUG.RUG_ANOTACIONES_SEG_INC_CSG
                        SET ID_TRAMITE = vlIdTramite
                        WHERE ID_ANOTACION_TEMP = peIdTramiteTemp;

                        RUG.SP_ANOTAC_SEG_INC_CSG_INS_H(peIdTramiteTemp, vlresult, vlTextResult);


                        /** CANCELA LOGICAMENTE EL TRAMITE PADRE Y HERMANOS  **/
                        IF RegTramitesIncomp.id_tipo_tramite = 27 THEN


                            /* CANCELA TRAMITES TERMINADOS */

                            SELECT ID_TRAMITE_PADRE
                            INTO V_ID_TRAMITE_PADRE
                            FROM RUG.RUG_ANOTACIONES_SEG_INC_CSG
                            WHERE ID_TRAMITE = vlIdTramite;

                            UPDATE TRAMITES
                            SET STATUS_REG = 'CA'
                            WHERE ID_TRAMITE IN (SELECT ID_TRAMITE
                                                 FROM RUG_ANOTACIONES_SEG_INC_CSG
                                                 WHERE ID_TRAMITE IS NOT NULL
                                                   AND ID_TRAMITE_PADRE = V_ID_TRAMITE_PADRE
                            )
                               OR ID_TRAMITE = V_ID_TRAMITE_PADRE
                                AND ID_TIPO_TRAMITE <> 27;



                            /* CANCELA TRAMITES PENDIENTES */

                            UPDATE RUG.TRAMITES_RUG_INCOMP
                            SET STATUS_REG = 'CA'
                            WHERE ID_TRAMITE_TEMP IN (SELECT ID_ANOTACION_TEMP
                                                      FROM RUG_ANOTACIONES_SEG_INC_CSG
                                                      WHERE ID_TRAMITE IS NULL
                                                        AND ID_TRAMITE_PADRE = V_ID_TRAMITE_PADRE
                            );

                        END IF;

                    END;
                ELSE

                    NULL;

                END case;

        END; /* THEN  Fin de alta de tramite nuevo */

    ELSE /*  id_status <> 3 */
        vlIdTramite:=peIdTramiteTemp; /* valor de tramite temporal porque el tramite no es terminado status <>3 */
    end IF;


/* Insertar en la bitacora de tramites */
    IF peBanderaFecha NOT IN ('V','F') THEN
        RAISE Ex_ErrParametro;
    END IF;


    IF(RegTramitesIncomp.id_tipo_tramite IN (1,2,6,7,8,3,10
        , 27, 28 -- /* GGR 23.08.2013   MMSECN2013-81 */
        , 23, 24 ) -- /* GGR 23.08.2013   MMSECN2013-82 */
        ) THEN

        IF(peIdStatus = 3) THEN

            SP_ALTA_CATALOGO_PALABRAS(vlIdTramite, RegTramitesIncomp.id_tipo_tramite, vlresult, vlTextResult);

            IF(vlresult != 0 OR vlresult IS NULL) THEN
                RAISE Ex_ErrParametro;
            END IF;

        END IF;

    END IF;

    SELECT COUNT(*)
    INTO vlCountStatTram
    FROM RUG.RUG_BITAC_TRAMITES
    WHERE ID_TRAMITE_TEMP = peIdTramiteTemp
      AND ID_STATUS = 3;

    IF vlCountStatTram = 0 THEN

        IF peBanderaFecha = 'V' THEN

            SELECT ID_PERSONA, ID_TIPO_TRAMITE, FECH_PRE_INSCR, FECHA_INSCR
            INTO RegTramitesIncomp.id_persona, RegTramitesIncomp.id_tipo_tramite, RegTramitesIncomp.fech_pre_inscr, RegTramitesIncomp.fecha_inscr
            FROM TRAMITES_RUG_INCOMP
            WHERE ID_TRAMITE_TEMP= peIdTramiteTemp;


            UPDATE RUG.RUG_BITAC_TRAMITES
            SET STATUS_REG = 'IN'
            WHERE ID_TRAMITE_TEMP = peIdTramiteTemp;

            INSERT INTO RUG.RUG_BITAC_TRAMITES
            (ID_TRAMITE_TEMP, ID_STATUS,  ID_PASO,FECHA_STATUS,ID_TIPO_TRAMITE, FECHA_REG, STATUS_REG)
            VALUES
            (peIdTramiteTemp,peIdStatus,peIdPaso,sysdate,RegTramitesIncomp.id_tipo_tramite, SYSDATE, 'AC');

        ELSE
            IF peFechaCreacion IS NOT NULL THEN


                UPDATE RUG.RUG_BITAC_TRAMITES
                SET STATUS_REG = 'IN'
                WHERE ID_TRAMITE_TEMP = peIdTramiteTemp;

                INSERT INTO RUG.RUG_BITAC_TRAMITES
                (ID_TRAMITE_TEMP, ID_STATUS,  ID_PASO,FECHA_STATUS,ID_TIPO_TRAMITE, FECHA_REG, STATUS_REG)
                VALUES
                (peIdTramiteTemp,peIdStatus,peIdPaso,peFechaCreacion,RegTramitesIncomp.id_tipo_tramite, SYSDATE, 'AC');
            ELSE
                RAISE Ex_ErrParametro;
            END IF;
        END IF;

        UPDATE TRAMITES_RUG_INCOMP
        SET ID_STATUS_TRAM = peIdStatus,
            ID_PASO = peIdPaso,
            STATUS_REG = 'AC',
            FECHA_STATUS = SYSDATE
        WHERE ID_TRAMITE_TEMP  = peIdTramiteTemp;

    ELSIF vlCountStatTram = 1 THEN

        RAISE Ex_TramiteTerminado;

    END IF;

    COMMIT;

    psResult := 0;
    psTxResult := RUG.FN_MENSAJE_ERROR(psResult);

    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'psResult', psResult, 'OUT');
    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'psTxResult', psTxResult, 'OUT');



EXCEPTION
    WHEN Ex_FirmaCertificado  THEN
        dbms_output.put_line('FIN');
        psResult := 0;
        psTxResult := RUG.FN_MENSAJE_ERROR(psResult);
        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'psResult', psResult, 'OUT');
        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'psTxResult', psTxResult, 'OUT');
    WHEN Ex_TramiteTerminado  THEN
        psResult   :=11;
        psTxResult := RUG.FN_MENSAJE_ERROR(psResult);
        ROLLBACK;
        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'psResult', psResult, 'OUT');
        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'psTxResult', psTxResult, 'OUT');
        dbms_output.put_line(psTxResult);
    WHEN Ex_ErrParametro  THEN
        psTxResult:= substr(psResult,1,250);
        ROLLBACK;
        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'psResult', psResult, 'OUT');
        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'psTxResult', psTxResult, 'OUT');
        dbms_output.put_line(psTxResult);
    WHEN Ex_TramiteSinSaldo  THEN
        psResult   :=789;
        psTxResult:= 'NO TIENE SALDO PARA HACER LA OPERACION';
        ROLLBACK;
        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'psResult', psResult, 'OUT');
        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'psTxResult', psTxResult, 'OUT');
        dbms_output.put_line(psTxResult);
    WHEN OTHERS THEN
        psResult  := 999;
        psTxResult:= substr(SQLCODE||':'||SQLERRM,1,250)||':'||DBMS_UTILITY.FORMAT_ERROR_BACKTRACE||':'||RegGarantiasTemp.ID_GARANTIA_PEND;
        ROLLBACK;
        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'psResult', psResult, 'OUT');
        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_Alta_Bitacora_Tramite2', 'psTxResult', psTxResult, 'OUT');
        dbms_output.put_line(psTxResult);

END;

   -------------------------------------------------------------------------------
   --            U P D A T E  F U N C I O N                    --
   -------------------------------------------------------------------------------

/*
    TABLA: FN_PRECIO_REAL
    SENTENCIA: UPDATE FUNCTION
    OBSERVACION: Verificación de tipo de inscripción 
        tipo Garantia 1  = Inscripción
        tipo Garantia 2  = Factoraje
        tipo Garantia 16 = Leasing
*/
create or replace FUNCTION             FN_PRECIO_REAL
                                                     (
                                                         pUserId      IN VARCHAR2
                                                       , pTipoTramite IN NUMBER
                                                       , pIdTramite   IN NUMBER
													   , pPendiente   IN NUMBER default 0
                                                       , idGarantia IN NUMBER
                                                     ) -- 0 NO_TIENE 1 TIENE
    RETURN NUMBER
IS    
    l_arancel   NUMBER;
    l_facturas  NUMBER;
    l_vehiculos NUMBER;
    l_precio    NUMBER;
    l_tipoInscripcion   NUMBER; -- verificación de tipo de inscrip
    l_fecha_Inscripcion VARCHAR2(200);
    l_precioInscripcion NUMBER;
    --NUEVO 
    intoInscripcionIn   NUMBER;
    intoInscripcionN    NUMBER;
    valorInscripcionArancel  NUMBER;
    tipo_inscripcionM   NUMBER;

	l_fecha_creacion TRAMITES.FECHA_CREACION%TYPE;
    vlDescMensajeError  VARCHAR(4000);
BEGIN

    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'FN_PRECIO_REAL', 'pUserId', pUserId, 'IN');
    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'FN_PRECIO_REAL', 'pTipoTramite', pTipoTramite, 'IN');
    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'FN_PRECIO_REAL', 'pIdTramite', pIdTramite, 'IN');
    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'FN_PRECIO_REAL', 'idGarantia', idGarantia, 'IN');

	IF pPendiente = 0 THEN  
        SELECT 
                FECHA_CREACION
        INTO    l_fecha_creacion
        FROM    
                TRAMITES
        WHERE   ID_TRAMITE = pIdTramite;
    ELSE
        l_fecha_creacion := SYSDATE;
    END IF;


	-- validamos vigencia precio
        BEGIN
            SELECT
                precio
            INTO l_precio
            FROM RUG_CAT_TIPO_TRAM_PAGO
            WHERE id_tipo_tramite = pTipoTramite
            AND VIGENCIA_PRECIO > TO_TIMESTAMP(l_fecha_creacion);

            EXCEPTION WHEN no_data_found THEN
            BEGIN
                SELECT
                    precio
                INTO l_precio
                FROM RUG_CAT_TIPO_TRAM_PAGO
                WHERE id_tipo_tramite = pTipoTramite
                AND VIGENCIA_PRECIO IS NULL;

                EXCEPTION WHEN no_data_found THEN
                BEGIN
                    SELECT
                       PRECIO
                    INTO   l_precio
                    FROM
                           RUG_CAT_TIPO_TRAMITE
                    WHERE
                           ID_TIPO_TRAMITE = pTipoTramite
                    ;
                END;
            END;
        END;
        
        --dbms_output.put_line('Valor '|| pIdTramite);
        -- Tipos de Garantias (1 inscripcion, 2 factoraje, 16 leasing)

         BEGIN
            SELECT ID_TIPO_GARANTIA
                INTO l_tipoInscripcion
            FROM RUG_GARANTIAS
                WHERE ID_GARANTIA = idGarantia;
                --DBMS_OUTPUT.PUT_LINE('Tipo Inscripcion = ' || l_tipoInscripcion);
            EXCEPTION WHEN no_data_found THEN
            
            IF pTipoTramite = 7 THEN
                SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 40;
                l_arancel := valorInscripcionArancel;
                --DBMS_OUTPUT.PUT_LINE('v_Return = ' || l_arancel);
                l_arancel := l_precio;
            END IF;
         END;
         DBMS_OUTPUT.PUT_LINE('Tipo de tramite = ' || pTipoTramite);
         DBMS_OUTPUT.PUT_LINE('ID Tramite = ' || pIdTramite);
    IF  pTipoTramite = 1 THEN   -- Inscripcion
        
        --  Verificación de Tipo de Garantía
        IF l_tipoInscripcion = 16 THEN  -- Leasing
                SELECT
                        PRECIO
                    INTO   l_precioInscripcion
                    FROM
                            RUG_CAT_TIPO_TRAMITE
                    WHERE
                            ID_TIPO_TRAMITE = 37;

            l_arancel := l_precioInscripcion;
        ELSIF l_tipoInscripcion = 2 THEN -- Factoraje
        -- IF pTipoTramite = 1 THEN

            IF pPendiente = 1 THEN         
                SELECT
                    COUNT(*) AS FACTURAS
                INTO   l_facturas
                FROM
                    RUG_GARANTIAS_BIENES_PEND
                WHERE
                    ID_TRAMITE_TEMP        = pIdTramite
                    AND TIPO_BIEN_ESPECIAL = 2;                        
            ELSE 
                SELECT
                    COUNT(*) AS FACTURAS
                INTO   l_facturas
                FROM
                    RUG_GARANTIAS_BIENES_H
                WHERE
                    ID_TRAMITE        = pIdTramite
                    AND TIPO_BIEN_ESPECIAL = 2
                ;
            END IF;
           
            BEGIN
                SELECT
                    precio
                INTO l_precio
                FROM RUG_CAT_TIPO_TRAM_PAGO
                WHERE id_tipo_tramite = 36
                AND TO_CHAR(VIGENCIA_PRECIO, 'DD/MM/YY') <= TO_TIMESTAMP(l_fecha_creacion);
                
                l_arancel := l_precio * l_facturas;
                
                EXCEPTION WHEN no_data_found THEN
                l_arancel := 20*l_facturas;
            END;
        
        
            IF(NVL(l_arancel,0) = 0) THEN
            l_arancel := l_precio;
            END IF;

        ELSE -- inscripción
        
            SELECT NVL(MAX(TIPO_BIEN_ESPECIAL),0) RESULTADO INTO intoInscripcionIn FROM RUG_GARANTIAS_BIENES WHERE ID_TRAMITE = pIdTramite
            AND TIPO_BIEN_ESPECIAL IN (1,2,3);
            
            IF intoInscripcionIn = 0 THEN
                SELECT NVL(MAX(TIPO_BIEN_ESPECIAL),0) RESULTADO INTO intoInscripcionN FROM RUG_GARANTIAS_BIENES WHERE ID_TRAMITE = pIdTramite
                AND TIPO_BIEN_ESPECIAL IN (5,6);
                IF intoInscripcionN = 5 THEN
                    SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 38;
                ELSIF intoInscripcionN = 6 THEN
                    SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 39;
                ELSE
                
                    SELECT ID_TIPO_TRAMITE INTO tipo_inscripcionM FROM RUG.TRAMITES where id_tramite = pIdTramite;
                    
                    IF tipo_inscripcionM = 7 THEN
                        SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 40;
                    ELSE
                        SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 1;
                    END IF;
                    
                    
                END IF;
            ELSE
                SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 1;
            END IF;


            l_arancel := valorInscripcionArancel;

            --l_arancel := l_precio;

        END IF;
    ELSIF  pTipoTramite = 4 THEN -- Cancelacion
        IF l_tipoInscripcion = 16 THEN
            BEGIN
                SELECT ID_TIPO_GARANTIA
                    INTO l_fecha_Inscripcion
                    FROM RUG_GARANTIAS
                        WHERE ID_GARANTIA = idGarantia AND EXTRACT(MONTH FROM FECHA_REG) = 09;
                        DBMS_OUTPUT.PUT_LINE('Tipo de Garantia = ' || l_fecha_Inscripcion);
                SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 45;
                     DBMS_OUTPUT.PUT_LINE('Costo 1= ' || valorInscripcionArancel);

                l_arancel := valorInscripcionArancel;

                EXCEPTION WHEN no_data_found THEN

                SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 4;
                DBMS_OUTPUT.PUT_LINE('Costo 2 = ' || valorInscripcionArancel);

                l_arancel := valorInscripcionArancel;
            END;
        END IF;
    ELSIF  pTipoTramite = 30 THEN -- Ejecucion
        IF l_tipoInscripcion = 16 THEN
            BEGIN
                SELECT ID_TIPO_GARANTIA
                    INTO l_fecha_Inscripcion
                    FROM RUG_GARANTIAS
                        WHERE ID_GARANTIA = idGarantia AND EXTRACT(MONTH FROM FECHA_REG) = 09;
                        DBMS_OUTPUT.PUT_LINE('Tipo de Garantia = ' || l_fecha_Inscripcion);
                SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 46;
                     DBMS_OUTPUT.PUT_LINE('Costo 1= ' || valorInscripcionArancel);

                l_arancel := valorInscripcionArancel;

                EXCEPTION WHEN no_data_found THEN

                SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 30;
                DBMS_OUTPUT.PUT_LINE('Costo 2 = ' || valorInscripcionArancel);

                l_arancel := valorInscripcionArancel;
            END;
        END IF;
    ELSIF  pTipoTramite = 7 THEN -- Modificacion
        IF l_tipoInscripcion = 16 THEN
            BEGIN
                SELECT ID_TIPO_GARANTIA
                    INTO l_fecha_Inscripcion
                    FROM RUG_GARANTIAS
                        WHERE ID_GARANTIA = idGarantia AND EXTRACT(MONTH FROM FECHA_REG) = 09;
                        DBMS_OUTPUT.PUT_LINE('Tipo de Garantia = ' || l_fecha_Inscripcion);
                SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 44;
                     DBMS_OUTPUT.PUT_LINE('Costo 1= ' || valorInscripcionArancel);

                l_arancel := valorInscripcionArancel;

                EXCEPTION WHEN no_data_found THEN

                SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 7;
                DBMS_OUTPUT.PUT_LINE('Costo 2 = ' || valorInscripcionArancel);

                l_arancel := valorInscripcionArancel;
            END;
        END IF;
    
    ELSE

    l_arancel := l_precio;

    END IF;

    --REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'FN_PRECIO_REAL', 'arancel', l_arancel, 'OUT');

    RETURN l_arancel;

	EXCEPTION 
        WHEN OTHERS THEN
            
            vlDescMensajeError := SUBSTR(SQLCODE||'-'||SQLERRM,1,1000);  
            dbms_output.put_line('Valor' || vlDescMensajeError);
            REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'FN_PRECIO_REAL', 'ERROR', vlDescMensajeError, 'OUT');
            RETURN NULL;
END;



   -------------------------------------------------------------------------------
   --            U P D A T E  P R O C E D I M I E N T O S                       --
   -------------------------------------------------------------------------------

/*
    TABLA: SP_CONSULTA_GARANTIAS_REG
    SENTENCIA: UPDATE PROCEDURE
    OBSERVACIONES: Se modifico para que en el PDF resultante muestre todos los registros
*/

create or replace PROCEDURE     SP_CONSULTA_GARANTIAS_REG
                                                         (
                                                             peDescBienMueble        IN VARCHAR2
                                                           , peNombOtorganteGarantia IN VARCHAR2
                                                           , peNumGarantiaMobiliaria IN NUMBER
                                                           , peFolioElectronicoOtorg IN VARCHAR
                                                           , peCurpOtorganteGarantia IN VARCHAR2
                                                           , peRfcOtorganteGarantia  IN VARCHAR2
                                                           , peInicio                IN NUMBER
                                                           , peFin                   IN NUMBER
														   , peNumeroSerial			 IN VARCHAR2
                                                           , peIdPersona             IN NUMBER
														   , peTipoTramite           IN NUMBER
                                                           , pePagineo               IN NUMBER
														   , peNumRegistros OUT         NUMBER
                                                           , psCursorConsulta OUT SYS_REFCURSOR
                                                         )
IS

    v_costo_tramite NUMBER;
    vlNombre         VARCHAR2 (3000);
    vlGarantia       VARCHAR2 (100);
    vlFolio          VARCHAR2 (3000);
    vlDescBienMueble VARCHAR2 (4000);
    vlConsulta CLOB;
    vlCurp VARCHAR2 (100);
    vlRfc  VARCHAR2 (100);
    vlNombreOt CLOB;
    vlDescBienes CLOB;
	vlSerialBienes VARCHAR2(20000);
    vlidtramite VARCHAR2(20000);
    vl_Curp CLOB;
    vl_Rfc CLOB;
    vl_Folio CLOB;

    vlcont  NUMBER;
    vlcont1 NUMBER;
    vlcont2 NUMBER;
    vlcont3 NUMBER;
    vlmil   NUMBER;
    vlmil1  NUMBER;
    vlmil2  NUMBER;
    vlmil3  NUMBER;
    vltrunc VARCHAR2(4000);

    vlTotalReg CLOB;
    vlTotal  NUMBER;
    vlInicio NUMBER;
    vlFin    NUMBER;

/*USUARIO ADMINISTRADOR*/

    dUserAdmin NUMBER;

TYPE t_folio
IS
    TABLE OF RUG.RUG_PERSONAS_H.ID_TRAMITE%TYPE;

    v_folio t_folio;

TYPE t_curp
IS
    TABLE OF RUG.RUG_PERSONAS_H.ID_TRAMITE%TYPE;

    v_curp t_curp;

TYPE t_rfc
IS
    TABLE OF RUG.RUG_PERSONAS_H.ID_TRAMITE%TYPE;

    v_rfc t_rfc;

TYPE t_nombreot
IS
    TABLE OF RUG.RUG_PERSONAS_H.ID_TRAMITE%TYPE;

    v_nombreot t_nombreot;

TYPE t_descripcion
IS
    TABLE OF RUG.RUG_PERSONAS_H.ID_TRAMITE%TYPE;

    v_descripcion t_descripcion;


TYPE t_serial
IS
    TABLE OF RUG.RUG_PERSONAS_H.ID_TRAMITE%TYPE;

v_serial t_serial;

    vlPsTxtResult VARCHAR(4000);
    vlPsResult    NUMBER;

    Ex_Error EXCEPTION;
    Ex_TramiteSinSaldo      EXCEPTION;

BEGIN
    REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'peDescBienMueble', peDescBienMueble, 'IN');
    REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'peNombOtorganteGarantia', peNombOtorganteGarantia, 'IN');
    REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'peNumGarantiaMobiliaria', peNumGarantiaMobiliaria, 'IN');
    REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'peCurpOtorganteGarantia', peCurpOtorganteGarantia, 'IN');
    REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'peRfcOtorganteGarantia', peRfcOtorganteGarantia, 'IN');
    REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'peFolioElectronicoOtorg', peFolioElectronicoOtorg, 'IN');
    REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'peInicio', peInicio, 'IN');
    REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'peFin', peFin, 'IN');
	REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'peIdPersona', peIdPersona, 'IN');

     /* Limpiando Los Campos */
    vlNombre         := TRIM(UPPER(RUG_ACENTOS(peNombOtorganteGarantia)));
    vlGarantia       := TRIM(peNumGarantiaMobiliaria);
    vlFolio          := TRIM(UPPER(peFolioElectronicoOtorg));
    vlDescBienMueble := TRIM(UPPER(RUG_CARACTER_ESP(peDescBienMueble)));
    vlCurp           := TRIM(UPPER(peCurpOtorganteGarantia));
    vlRfc            := TRIM(UPPER(peRfcOtorganteGarantia));
    vltrunc          := NULL;
    dUserAdmin       := 17381;


    IF peIdPersona <> dUserAdmin THEN

        IF pePagineo = 0 and FN_TIENE_SALDO(peIdPersona,peTipoTramite,0) = 0 THEN
            RAISE Ex_TramiteSinSaldo;
        END IF;
    END IF;



    IF peInicio IS NULL THEN
        vlInicio := 1;
    ELSE
        vlInicio := peInicio;
    END IF;

    IF peFin IS NULL THEN
        vlFin := 20;
    ELSE
        vlFin := peFin;
    END IF;

    IF ( vlDescBienMueble = '''') THEN

        vlDescBienMueble := NULL;

    END IF;

    IF (vlGarantia = '0' ) THEN

        REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', '1', 1, 'OUT');

        OPEN psCursorConsulta FOR
        SELECT
               ''
        from
               dual
        ;

    ELSIF LENGTH(vlnombre) < 3
        OR
        LENGTH(vldescbienmueble) < 3 THEN

        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'psResult', 1, 'OUT');
        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'psTxResult', 'Ingresar un valor valido', 'OUT');

        OPEN psCursorConsulta FOR
        SELECT
               ''
        from
               dual
        ;

    ELSE

        REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', '2', 2, 'OUT');

        vlmil   := 0;
        vlmil1  := 0;
        vlmil2  := 0;
        vlmil3  := 0;
        vlcont  := 0;
        vlcont1 := 0;
        vlcont2 := 0;
        vlcont3 := 0;

         /***********************************************************/
         /******** Query Base Que Obtiene Los Tramites Vigentes *****/
         /***********************************************************/
        vlConsulta := ' WITH TRAMITE_BASE
AS
(SELECT ROWNUM RN,
ID_TRAMITE,
ID_TIPO_TRAMITE,
FECHA_CREACION,
DESCRIPCION,
FN_GETACREEDOR (ID_TRAMITE, 1) NOMBRE,
FN_GETACREEDOR (ID_TRAMITE, 2) FOLIO_MERCANTIL,
--FNCONCATOTORGANTE (ID_TRAMITE, 5) CURP,
ID_GARANTIA,
FN_DESC_GARANTIA (ID_GARANTIA, 85) DESC_TIPOS_BIENES
FROM RUG.V_BUSQUEDA_TRAMITE_BASE
WHERE 1 = 1 ';


         /************************/
         /****** F O L  I O ******/
         /************************/
        IF vlFolio IS NOT NULL THEN

             /*VERIFICA SI EXISTE ESE FOLIO */
            SELECT
                       ID_TRAMITE BULK COLLECT
            INTO       v_folio
            FROM
                       RUG.RUG_REL_TRAM_PARTES A
                       INNER JOIN
                                  RUG.RUG_PERSONAS B
                                  ON
                                             A.ID_PERSONA = B.ID_PERSONA
            WHERE
                       TRIM(UPPER(B.FOLIO_MERCANTIL)) = vlFolio
                       AND A.ID_PARTE                 = 1
                       AND A.STATUS_REG               = 'AC'
            ;


            IF v_folio.FIRST IS NOT NULL THEN

                FOR i IN v_folio.FIRST .. v_folio.LAST
                LOOP

                    IF v_folio(i) IS NOT NULL THEN

                        SELECT
                               COUNT(ID_TRAMITE)
                        INTO   vlcont2
                        FROM
                               RUG.V_BUSQUEDA_TRAMITE_BASE
                        WHERE
                               ID_TRAMITE = TO_NUMBER(v_folio(i))
                        ;

                        IF vlcont2 > 0
                            AND
                            vlmil2 < 1000 THEN

                            vlmil2 := vlmil2 + 1;

                            vl_folio := vl_folio
                            || TO_CHAR(v_folio(i))
                            || ',';

                        END IF;

                        vlcont2 := NULL;

                        EXIT WHEN vlmil2 > 1000;

                    END IF;

                END LOOP;


                IF (vlmil2 IS NULL
                    OR
                    vlmil2 = 0)
                    AND
                    vl_folio IS NULL THEN

                    vlConsulta := vlConsulta
                    || 'AND ID_TRAMITE IN (1) ';
                ELSE
                    vl_folio := RTRIM(vl_folio, ',');

                    vlConsulta := vlConsulta
                    || 'AND ID_TRAMITE IN ( '
                    || vl_folio
                    || ') ';

                END IF;

            ELSE
                vlConsulta := vlConsulta
                || 'AND ID_TRAMITE IN (1) ';
            END IF;

        END IF;

         /************************/
         /*** G A R A N T I A ****/
         /************************/
        IF vlGarantia IS NOT NULL THEN

            vlConsulta := vlConsulta
            || 'AND (ID_GARANTIA ='
            || vlGarantia
            --|| ' OR ID_TRAMITE = '
            --|| vlGarantia
            || ') ';

        END IF;

         /************************/
         /*******  C U R P *******/
         /************************/

        IF vlCurp IS NOT NULL THEN



             /*VERIFICA SI EXISTE ESE CURP */
            SELECT
                       ID_TRAMITE BULK COLLECT
            INTO       v_curp
            FROM
                       RUG.RUG_REL_TRAM_PARTES A
                       INNER JOIN
                                  RUG.RUG_PERSONAS_FISICAS B
                                  ON
                                             A.ID_PERSONA = B.ID_PERSONA
            WHERE
                       B.CURP       = vlCurp
                       AND A.ID_PARTE   IN (1,2)
                       AND A.STATUS_REG = 'AC'
                       AND ROWNUM      <= 2500
            ;


            IF v_curp.FIRST IS NOT NULL THEN

                FOR i IN v_curp.FIRST .. v_curp.LAST
                LOOP

                    IF v_curp(i) IS NOT NULL THEN

                        SELECT
                               COUNT(ID_TRAMITE)
                        INTO   vlcont3
                        FROM
                               RUG.V_BUSQUEDA_TRAMITE_BASE
                        WHERE
                               ID_TRAMITE = TO_NUMBER(v_curp(i))
                        ;

                        IF vlcont3 > 0
                            AND
                            vlmil3 < 1000 THEN

                            vlmil3 := vlmil3 + 1;

                            vl_curp := vl_curp
                            || TO_CHAR(v_curp(i))
                            || ',';

                        END IF;

                        vlcont3 := NULL;

                        EXIT WHEN vlmil3 > 1000;

                    END IF;

                END LOOP;


                IF (vlmil3 IS NULL
                    OR
                    vlmil3 = 0)
                    AND
                    vl_curp IS NULL THEN

                    vlConsulta := vlConsulta
                    || 'AND ID_TRAMITE IN (1) ';

                ELSE

                    vl_curp := RTRIM(vl_curp, ',');

                    vlConsulta := vlConsulta
                    || 'AND ID_TRAMITE IN ( '
                    || vl_curp
                    || ') ';

                END IF;

            ELSE
                vlConsulta := vlConsulta
                || 'AND ID_TRAMITE IN (1) ';
            END IF;

        END IF;


         /************************/
         /*******  RFC      *******/

         /************************/

        IF vlRfc IS NOT NULL THEN
             /*VERIFICA SI EXISTE ESE RFC */
            SELECT
                       A.ID_TRAMITE BULK COLLECT
            INTO       v_rfc
            FROM
                       RUG.RUG_REL_TRAM_PARTES A
                       INNER JOIN
                                  RUG.RUG_PERSONAS B
                                  ON
                                             A.ID_PERSONA = B.ID_PERSONA
            WHERE
                       B.RFC          = vlRfc
                       AND A.ID_PARTE IN (1,2)

                       AND A.STATUS_REG = 'AC'
                       AND ROWNUM      <= 2500
            ;


            IF v_rfc.FIRST IS NOT NULL THEN

                FOR i IN v_rfc.FIRST .. v_rfc.LAST
                LOOP

                    IF v_rfc(i) IS NOT NULL THEN

                        SELECT
                               COUNT(ID_TRAMITE)
                        INTO   vlcont3
                        FROM
                               RUG.V_BUSQUEDA_TRAMITE_BASE
                        WHERE
                               ID_TRAMITE = TO_NUMBER(v_rfc(i))
                        ;

                        IF vlcont3 > 0
                            AND
                            vlmil3 < 1000 THEN

                            vlmil3 := vlmil3 + 1;

                            vl_rfc := vl_rfc
                            || TO_CHAR(v_rfc(i))
                            || ',';

                        END IF;

                        vlcont3 := NULL;

                        EXIT WHEN vlmil3 > 1000;

                    END IF;

                END LOOP;


                IF (vlmil3 IS NULL
                    OR
                    vlmil3 = 0)
                    AND
                    vl_rfc IS NULL THEN

                    vlConsulta := vlConsulta
                    || 'AND ID_TRAMITE IN (1) ';

                ELSE

                    vl_rfc := RTRIM(vl_rfc, ',');

                    vlConsulta := vlConsulta
                    || 'AND ID_TRAMITE IN ( '
                    || vl_rfc
                    || ') ';

                END IF;

            ELSE
                vlConsulta := vlConsulta
                || 'AND ID_TRAMITE IN (1) ';
            END IF;

        END IF;



         /************************************************/
         /*** N O M B R E   D E L  DEUDOR *****/
         /************************************************/

        IF vlNombre IS NOT NULL THEN


             /*Verifica Si Existe El Nombre Del Deudor garante*/
            SELECT
                   ID_TRAMITE BULK COLLECT
            INTO   v_nombreot
            FROM
                   (
                            SELECT DISTINCT
                                     (ID_TRAMITE)
                            FROM
                                     RUG.RUG_PERSONAS_H
                            WHERE
                                     ID_PARTE IN (1,2)
                                     AND TRIM(UPPER(TRIM(NOMBRE_PERSONA)
                                              || ' '
                                              || TRIM(AP_PATERNO)
                                              || ' '
                                              || TRIM(AP_MATERNO))
                                              || ' '
                                              || TRIM(UPPER(RAZON_SOCIAL))) LIKE ('%'
                                              || vlNombre
                                              || '%')
                            ORDER BY
                                     ID_TRAMITE DESC
                   )
            WHERE
                   ROWNUM <= 2500
            ;


             /*Limita El Proceso A 1000 Registros */
            IF v_nombreot.FIRST IS NOT NULL THEN

                FOR i IN v_nombreot.FIRST .. v_nombreot.LAST
                LOOP

                    IF v_nombreot(i) IS NOT NULL THEN

                        SELECT
                               COUNT(ID_TRAMITE)
                        INTO   vlcont1
                        FROM
                               RUG.V_BUSQUEDA_TRAMITE_BASE
                        WHERE
                               ID_TRAMITE = TO_NUMBER(v_nombreot(i))
                        ;

                        IF vlcont1 > 0
                            AND
                            vlmil < 1000 THEN

                            vlmil := vlmil + 1;

                            vlNombreOt := vlNombreOt
                            || TO_CHAR(v_nombreot(i))
                            || ',';

                        END IF;


                        vlcont1 := NULL;

                        EXIT WHEN vlmil > 1000;
                    END IF;
                END LOOP;


                IF (vlmil IS NULL
                    or
                    vlmil= 0)
                    AND
                    vlNombreOt IS NULL THEN

                    vlConsulta := vlConsulta
                    || 'AND ID_TRAMITE IN (1) ';
                ELSE
                    vlNombreOt := RTRIM(vlNombreOt, ',');

                    vlConsulta := vlConsulta
                    || 'AND ID_TRAMITE IN ( '
                    || vlNombreOt
                    || ') ';
                END IF;



            ELSE
                vlConsulta := vlConsulta
                || 'AND ID_TRAMITE IN (1) ';
            END IF;

        END IF;


		/** NO. SERIAL **/

		IF peNumeroSerial IS NOT NULL THEN

			/* verifica si existe ese numero serial */
			SELECT
				ID_TRAMITE BULK COLLECT
			INTO 	v_serial
			FROM 	(
						SELECT
                                     ID_TRAMITE
                            FROM
                                     RUG_GARANTIAS_BIENES
                            WHERE
                                     trim(IDENTIFICADOR) = peNumeroSerial
                            ORDER BY
                                     ID_TRAMITE DESC
					)
			WHERE
				ROWNUM <= 1000
            ;

			IF v_serial.FIRST IS NOT NULL THEN

                FOR i IN v_serial.FIRST .. v_serial.LAST
                LOOP

                    IF v_serial(i) IS NOT NULL THEN

                        vlSerialBienes := vlSerialBienes
                        || TO_CHAR(v_serial(i))
                        || ',';

                    END IF;

                END LOOP;

                vlSerialBienes := RTRIM(vlSerialBienes, ',');

                vlConsulta := vlConsulta
                || 'AND ID_TRAMITE IN ( '
                || vlSerialBienes
                || ') ';

            ELSE

                vlConsulta := vlConsulta
                || 'AND ID_TRAMITE IN (1) ';

            END IF;

		END IF;

         /***********************************************************/
         /****** D E S C R I P C I O N    D E   B I E N E S *********/
         /***********************************************************/

        IF vlDescBienMueble IS NOT NULL THEN

             /*Verifica si existe ese descripcion*/
            SELECT
                   ID_TRAMITE BULK COLLECT
            INTO   v_descripcion
            FROM
                   (
                            SELECT
                                     ID_TRAMITE
                            FROM
                                     RUG.RUG_TBL_BUSQUEDA
                            WHERE
                                     CONTAINS (DESC_GARANTIA, vlDescBienMueble) > 0
                            ORDER BY
                                     ID_TRAMITE DESC
                   )
            WHERE
                   ROWNUM <= 1000
            ;


            IF v_descripcion.FIRST IS NOT NULL THEN

                FOR i IN v_descripcion.FIRST .. v_descripcion.LAST
                LOOP

                    IF v_descripcion(i) IS NOT NULL THEN

                        vlDescBienes := vlDescBienes
                        || TO_CHAR(v_descripcion(i))
                        || ',';

                    END IF;

                END LOOP;

                vlDescBienes := RTRIM(vlDescBienes, ',');

                vlConsulta := vlConsulta
                || 'AND ID_TRAMITE IN ( '
                || vlDescBienes
                || ') ';

            ELSE

                vlConsulta := vlConsulta
                || 'AND ID_TRAMITE IN (1) ';

            END IF;


        END IF;


        vlConsulta := vlConsulta
        || ' )
SELECT ID_TRAMITE, ID_TIPO_TRAMITE, FECHA_CREACION, DESCRIPCION,
NOMBRE, FOLIO_MERCANTIL, ID_GARANTIA, DESC_TIPOS_BIENES
FROM TRAMITE_BASE ';

        vlTotalReg := 'SELECT COUNT(ID_TRAMITE) FROM ( '
        || vlConsulta
        || ')';

        EXECUTE IMMEDIATE vlTotalReg INTO vlTotal;

        peNumRegistros:= vlTotal;

        vlConsulta := vlConsulta
        /*|| '  WHERE' RN BETWEEN '
        || vlInicio
        || ' AND '
        || vlFin*/
        || ' ORDER BY ID_GARANTIA DESC ';

        REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'peNumRegistros', peNumRegistros , 'OUT');

        SELECT
               CASE
                      WHEN LENGTH(vlconsulta) < 4000
                             THEN SUBSTR( vlconsulta, 1, LENGTH(vlconsulta))
                             ELSE SUBSTR( vlconsulta, 1, 4000)
               END
        INTO   vltrunc
        FROM
               DUAL
        ;



       IF peIdPersona = dUserAdmin  THEN
            REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'Consulta', vltrunc , 'OUT');
            OPEN psCursorConsulta FOR vlConsulta;
        ELSE
            IF pePagineo = 0 THEN
            	INSERT INTO RUG_CONSULTA_REGISTRO(ID_CONSULTA_REG,ID_PERSONA,TOTAL_RESULTADO,CONSULTA,RESPUESTA,ID_TIPO_TRAMITE,FECHAHORA,ESTATUS)
	            VALUES(SEQ_CONSULTA_REG.NEXTVAL,peIdPersona,peNumRegistros,vltrunc,null,peTipoTramite,sysdate,'AC');
                
                
                SELECT PRECIO INTO v_costo_tramite FROM RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = peTipoTramite;
                
                
                SP_ACTUALIZAR_SALDO(v_costo_tramite,peIdPersona);
            END IF;

            REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'Consulta', vltrunc , 'OUT');
            OPEN psCursorConsulta FOR vlConsulta;


       END IF;



    END IF;


EXCEPTION
WHEN Ex_Error THEN

    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'psResult', vlPsResult, 'OUT');
    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'psTxResult', vlPsTxtResult, 'OUT');

WHEN Ex_TramiteSinSaldo  THEN
    vlPsResult   :=789;
    vlPsTxtResult:= 'NO TIENE SALDO PARA HACER LA OPERACION';
    ROLLBACK;
    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'psResult', vlPsResult, 'OUT');
    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'psTxResult', vlPsTxtResult, 'OUT');

WHEN OTHERS THEN

    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'psResult', 999, 'OUT');
    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_CONSULTA_GARANTIAS_REG', 'psTxResult', substr(SQLCODE
    ||':'
    ||SQLERRM,1,250), 'OUT');
    


END SP_CONSULTA_GARANTIAS_REG;





/* Cambios agregados para funcionalidad de nuevos aranceles */

   -------------------------------------------------------------------------------
   --                           C R E A T E                                    --
   -------------------------------------------------------------------------------
/*
    TABLA: RUG_SALDO_USUARIOS
    SENTENCIA: CREATE TABLE
    OBSERVACION: Tabla para control de saldos de los usuarios por años
*/

CREATE TABLE RUG_SALDO_USUARIOS 
(
  ID INTEGER NOT NULL 
, ID_USUARIO INTEGER NOT NULL 
, USUARIO_PADRE VARCHAR(20) NOT NULL 
, ANIO VARCHAR2(20) NOT NULL 
, SALDO NUMBER(18,6) NOT NULL 
, TIPO_CUENTA VARCHAR2(200) NOT NULL 
, CONSTRAINT RUG_SALDO_USUARIOS_PK PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);




   -------------------------------------------------------------------------------
   --                           C R E A T E                                    --
   -------------------------------------------------------------------------------
/*
    SENTENCIA: CREATE SEQUENCE
    OBSERVACION: Secuencia para la tabla de saldos
*/


CREATE SEQUENCE SEQ_SALDO INCREMENT BY 1 MAXVALUE 9999999999999999999999999999999999 MINVALUE 1 NOCACHE;



   -------------------------------------------------------------------------------
   --            U P D A T E  F U N C I O N                    --
   -------------------------------------------------------------------------------

/*
    FUNCION: FN_CALCULAR_SALDO
    SENTENCIA: UPDATE FUNCTION
    OBSERVACION: 
*/

create or replace FUNCTION     FN_CALCULAR_SALDO(pUserId IN VARCHAR2) RETURN NUMBER IS
l_saldo       NUMBER;
	v_usuario_maestro NUMBER;
    v_saldo_tabla NUMBER;
    v_usuario_padre NUMBER;
	v_usuarios_cuentas VARCHAR(1000);
    v_anios_user VARCHAR(1000);
	v_tramites_vinculados VARCHAR(1000);
	vlDescMensajeError  VARCHAR(4000);
    flag_saldo VARCHAR(200);
    l_saldo_real NUMBER;
	v_sql varchar2(3000);
	CURSOR c_cuentas(userId VARCHAR2) IS
      SELECT id_persona  
        FROM rug_secu_usuarios  
        WHERE id_persona = userId
        UNION  
        SELECT id_persona  
        FROM rug_secu_usuarios  
        WHERE cve_usuario_padre = (  
            SELECT cve_usuario_padre  
            FROM rug_secu_usuarios  
            WHERE id_persona = userId
        )  
        UNION  
        SELECT id_persona  
        FROM rug_secu_usuarios  
        WHERE cve_usuario = (  
            SELECT cve_usuario_padre  
            FROM rug_secu_usuarios  
            WHERE id_persona = userId
        )  
        UNION  
        SELECT id_persona  
        FROM rug_secu_usuarios  
        WHERE cve_usuario_padre = (  
            SELECT cve_usuario  
            FROM rug_secu_usuarios  
            WHERE id_persona = userId
        );
    
    CURSOR anios_user(userId VARCHAR2) IS
        SELECT EXTRACT(YEAR FROM FECHA_CREACION) FECHA
            FROM V_TRAMITES_TERMINADOS 
                WHERE ID_PERSONA_LOGIN = userId 
            GROUP BY 
                EXTRACT(YEAR FROM FECHA_CREACION) 
            ORDER BY EXTRACT(YEAR FROM FECHA_CREACION);
    
    v_c_tramites_vinculados VARCHAR(1000) := '
		SELECT htf.id_tramite
		FROM homologado_tramite htf
		WHERE EXISTS (
			SELECT * FROM (
				SELECT htr.id_tramite, min(htr.HOMOLOGADO_ID) AS homologado_id
				FROM rug.homologado_tramite htr
				WHERE EXISTS (
					SELECT 1
					FROM HOMOLOGADO_TRAMITE ht
					WHERE ht.id_persona IN (#usuarios#)
					AND ht.id_tramite = htr.id_tramite
				)
				GROUP BY htr.id_tramite	
			) sub
			WHERE sub.id_tramite = htf.id_tramite
			AND sub.homologado_id = htf.homologado_id
		)
		AND htf.ID_PERSONA IN (#usuarios#)';
		
	c_tramites_vinculados SYS_REFCURSOR;
	TYPE MYREC IS RECORD (
		ID_TRAMITE VARCHAR(1000)
	);
	r_tramites MYREC;
BEGIN
	REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'FN_CALCULAR_SALDO', 'pUserId', pUserId, 'IN');
    -- Verificación de saldo en tabla
    
    SELECT
        (CASE  
            WHEN S.CVE_USUARIO_PADRE IS NULL THEN ID_PERSONA
            ELSE
                (SELECT ID_PERSONA FROM RUG_SECU_USUARIOS WHERE CVE_USUARIO = S.CVE_USUARIO_PADRE)
        END) AS CODIGO
        INTO v_usuario_padre
    FROM RUG_SECU_USUARIOS S WHERE S.ID_PERSONA = pUserId;

    SELECT SALDO INTO v_saldo_tabla FROM rug_personas WHERE ID_PERSONA = v_usuario_padre;
      
    
    IF v_saldo_tabla IS NULL THEN
                    -- obtener cuenta maestra
            SELECT id_persona 
            INTO v_usuario_maestro
            FROM rug_secu_usuarios 
            WHERE cve_usuario = (
                SELECT NVL(cve_usuario_padre, cve_usuario) AS cve_usuario_padre 
                FROM rug_secu_usuarios 
                WHERE id_persona = pUserId
            );
            -- obtener todas las cuentas
            v_usuarios_cuentas := '';
            FOR r_cuentas IN c_cuentas(pUserId)
            LOOP
                v_usuarios_cuentas := v_usuarios_cuentas || r_cuentas.id_persona || ',';
            END LOOP;
                  
            
            v_usuarios_cuentas := SUBSTR(v_usuarios_cuentas, 1, LENGTH(v_usuarios_cuentas) - 1);
            
            
           
            -- obtener tramites vinculados
            v_tramites_vinculados := '';
            /*FOR r_tramites IN c_tramites_vinculados(pUserId)
            LOOP
                v_tramites_vinculados := v_tramites_vinculados || r_tramites.id_tramite || ',';
            END LOOP;*/
            v_c_tramites_vinculados := REPLACE(v_c_tramites_vinculados, '#usuarios#', v_usuarios_cuentas);
            OPEN c_tramites_vinculados FOR v_c_tramites_vinculados;
            LOOP
                FETCH c_tramites_vinculados INTO r_tramites;
                EXIT WHEN c_tramites_vinculados%NOTFOUND;
                v_tramites_vinculados := v_tramites_vinculados || r_tramites.id_tramite || ',';
            END LOOP;
            v_tramites_vinculados := SUBSTR(v_tramites_vinculados, 1, LENGTH(v_tramites_vinculados) - 1);
            
            -- dbms_output.put_line('1 '||v_sql);
            
            -- obtener los años del usuario
            
            
            FOR r_anios IN anios_user(pUserId)
            LOOP 
                BEGIN
                    SELECT saldo INTO flag_saldo FROM  RUG.RUG_SALDO_USUARIOS WHERE ANIO = r_anios.FECHA AND ID_USUARIO = pUserId;
                                        
                    EXCEPTION WHEN no_data_found THEN
                    
                            v_sql := 'SELECT NVL(SUM(CREDITOS) - SUM(DEBITOS),0) AS SALDO
                                FROM (
                                    SELECT SUM(PRECIO) DEBITOS, 0 as CREDITOS 
                                    FROM RUG.V_TRAMITES_PAGADOS TRA
                                    WHERE TRA.ID_PERSONA_LOGIN IN (
                                        '||v_usuarios_cuentas||'
                                    ) AND TRA.ANIO IN ('|| r_anios.FECHA||')
                                    AND NOT EXISTS (
                                        SELECT 1
                                        FROM homologado_tramite ht
                                        WHERE ht.id_tramite = tra.id_tramite
                                    )';
                                IF LENGTH(v_tramites_vinculados) > 0 THEN
                                    v_sql := v_sql ||
                                    ' UNION 
                                    SELECT SUM(PRECIO) DEBITOS, 0 as CREDITOS  
                                    FROM V_TRAMITES_PAGADOS TP 
                                    WHERE TP.ID_TRAMITE IN ( 
                                        '||v_tramites_vinculados||'
                                    )
                                    AND TP.ID_PERSONA_LOGIN NOT IN (
                                        '||v_usuarios_cuentas||'
                                    )';
                                END IF;
                                    v_sql := v_sql ||
                                    ' UNION
                                    SELECT 0 as DEBITOS, SUM(MONTO) CREDITOS 
                                    FROM RUG_UTIL.BOLETA
                                    WHERE IDENTIFICADOR = '||v_usuario_maestro||' AND USADA = 1 AND EXTRACT(YEAR FROM FECHA_HORA) IN ('|| r_anios.FECHA ||')
                                    GROUP BY IDENTIFICADOR
                                ) M';

                            execute immediate v_sql into l_saldo;
                            
                            --dbms_output.put_line('Saldo' || flag_saldo );
                            INSERT INTO RUG.RUG_SALDO_USUARIOS(ID, ID_USUARIO, USUARIO_PADRE, ANIO, SALDO, TIPO_CUENTA)
                            VALUES(SEQ_SALDO.NEXTVAL, pUserId, v_usuario_maestro, r_anios.FECHA, l_saldo, 'REGISTRO');
                END;                
            END LOOP;
            
            
            
            SELECT SUM(SALDO) AS SALDO INTO l_saldo_real FROM RUG.RUG_SALDO_USUARIOS WHERE ID_USUARIO = pUserId;

            UPDATE rug_personas SET SALDO = l_saldo_real, FECHA_ACTUALIZACION = SYSDATE WHERE ID_PERSONA = pUserId;
            
            l_saldo := l_saldo_real;
            
            -- dbms_output.put_line('Saldo' || v_sql );
            
    ELSE
        l_saldo := v_saldo_tabla;
    END IF;
    
	RETURN l_saldo;
EXCEPTION 
    WHEN OTHERS THEN
    	vlDescMensajeError := SUBSTR(SQLCODE||'-'||SQLERRM,1,1000);  
        REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'FN_CALCULAR_SALDO', 'error', vlDescMensajeError, 'OUT');
		-- dbms_output.put_line('error' || vlDescMensajeError);
        RETURN NULL;
END;

   -------------------------------------------------------------------------------
   --            U P D A T E  F U N C I O N                    --
   -------------------------------------------------------------------------------

/*
    FUNCION: FN_PRECIO_REAL
    SENTENCIA: UPDATE FUNCTION
    OBSERVACION: 
*/

create or replace FUNCTION             FN_PRECIO_REAL
                                                     (
                                                         pUserId      IN VARCHAR2
                                                       , pTipoTramite IN NUMBER
                                                       , pIdTramite   IN NUMBER
													   , pPendiente   IN NUMBER default 0
                                                     ) -- 0 NO_TIENE 1 TIENE
    RETURN NUMBER
IS    
    l_arancel   NUMBER;
    l_facturas  NUMBER;
    l_vehiculos NUMBER;
    l_precio    NUMBER;
    l_tipoInscripcion   NUMBER; -- verificación de tipo de inscrip
    l_precioInscripcion NUMBER;
    --NUEVO 
    intoInscripcionIn   NUMBER;
    intoInscripcionN    NUMBER;
    valorInscripcionArancel  NUMBER;
    tipo_inscripcionM   NUMBER;

	l_fecha_creacion TRAMITES.FECHA_CREACION%TYPE;
    vlDescMensajeError  VARCHAR(4000);
BEGIN

    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'FN_PRECIO_REAL', 'pUserId', pUserId, 'IN');
    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'FN_PRECIO_REAL', 'pTipoTramite', pTipoTramite, 'IN');
    REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'FN_PRECIO_REAL', 'pIdTramite', pIdTramite, 'IN');

	IF pPendiente = 0 THEN  
        SELECT 
                FECHA_CREACION
        INTO    l_fecha_creacion
        FROM    
                TRAMITES
        WHERE   ID_TRAMITE = pIdTramite;
    ELSE
        l_fecha_creacion := SYSDATE;
    END IF;


	-- validamos vigencia precio
        BEGIN
            SELECT
                precio
            INTO l_precio
            FROM RUG_CAT_TIPO_TRAM_PAGO
            WHERE id_tipo_tramite = pTipoTramite
            AND VIGENCIA_PRECIO > TO_TIMESTAMP(l_fecha_creacion);

            EXCEPTION WHEN no_data_found THEN
            BEGIN
                SELECT
                    precio
                INTO l_precio
                FROM RUG_CAT_TIPO_TRAM_PAGO
                WHERE id_tipo_tramite = pTipoTramite
                AND VIGENCIA_PRECIO IS NULL;

                EXCEPTION WHEN no_data_found THEN
                BEGIN
                    SELECT
                       PRECIO
                    INTO   l_precio
                    FROM
                           RUG_CAT_TIPO_TRAMITE
                    WHERE
                           ID_TIPO_TRAMITE = pTipoTramite
                    ;
                END;
            END;
        END;
        
        --dbms_output.put_line('Valor '|| pIdTramite);

         BEGIN
            SELECT ID_TIPO_GARANTIA
                INTO l_tipoInscripcion
            FROM RUG_GARANTIAS
                WHERE ID_ULTIMO_TRAMITE = pIdTramite;
            --DBMS_OUTPUT.PUT_LINE('Tipo Inscripcion = ' || l_tipoInscripcion);
            EXCEPTION WHEN no_data_found THEN
            
            IF pTipoTramite = 7 THEN
                SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 40;
                l_arancel := valorInscripcionArancel;
                --DBMS_OUTPUT.PUT_LINE('v_Return = ' || l_arancel);
                l_arancel := l_precio;
            END IF;
         END;
     IF  pTipoTramite = 1 THEN  
    --  Verificación de Tipo de Garantía
        IF l_tipoInscripcion = 16 THEN  -- Leasing
                SELECT
                        PRECIO
                    INTO   l_precioInscripcion
                    FROM
                            RUG_CAT_TIPO_TRAMITE
                    WHERE
                            ID_TIPO_TRAMITE = 37;

            l_arancel := l_precioInscripcion;
        ELSIF l_tipoInscripcion = 2 THEN
        -- IF pTipoTramite = 1 THEN

            IF pPendiente = 1 THEN         
                SELECT
                    COUNT(*) AS FACTURAS
                INTO   l_facturas
                FROM
                    RUG_GARANTIAS_BIENES_PEND
                WHERE
                    ID_TRAMITE_TEMP        = pIdTramite
                    AND TIPO_BIEN_ESPECIAL = 2;                        
            ELSE 
                SELECT
                    COUNT(*) AS FACTURAS
                INTO   l_facturas
                FROM
                    RUG_GARANTIAS_BIENES_H
                WHERE
                    ID_TRAMITE        = pIdTramite
                    AND TIPO_BIEN_ESPECIAL = 2
                ;
            END IF;
           
         BEGIN
            SELECT
                precio
            INTO l_precio
            FROM RUG_CAT_TIPO_TRAM_PAGO
            WHERE id_tipo_tramite = 36
            AND TO_CHAR(VIGENCIA_PRECIO, 'DD/MM/YY') <= TO_TIMESTAMP(l_fecha_creacion);
            
            l_arancel := l_precio * l_facturas;
            
            EXCEPTION WHEN no_data_found THEN
            l_arancel := 20*l_facturas;
        END;
        
        
        
        --REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'FN_PRECIO_REAL', 'precios_hoy', l_fecha_creacion, 'IN');

        
            
            IF(NVL(l_arancel,0) = 0) THEN
            l_arancel := l_precio;
            END IF;

        ELSE
        
            SELECT NVL(MAX(TIPO_BIEN_ESPECIAL),0) RESULTADO INTO intoInscripcionIn FROM RUG_GARANTIAS_BIENES WHERE ID_TRAMITE = pIdTramite
            AND TIPO_BIEN_ESPECIAL IN (1,2,3);
            
            IF intoInscripcionIn = 0 THEN
                SELECT NVL(MAX(TIPO_BIEN_ESPECIAL),0) RESULTADO INTO intoInscripcionN FROM RUG_GARANTIAS_BIENES WHERE ID_TRAMITE = pIdTramite
                AND TIPO_BIEN_ESPECIAL IN (5,6);
                IF intoInscripcionN = 5 THEN
                    SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 38;
                ELSIF intoInscripcionN = 6 THEN
                    SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 39;
                ELSE
                
                    SELECT ID_TIPO_TRAMITE INTO tipo_inscripcionM FROM RUG.TRAMITES where id_tramite = pIdTramite;
                    
                    IF tipo_inscripcionM = 7 THEN
                        SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 40;
                    ELSE
                        SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 1;
                    END IF;
                    
                    
                END IF;
            ELSE
                SELECT PRECIO INTO valorInscripcionArancel  FROM  RUG_CAT_TIPO_TRAMITE WHERE ID_TIPO_TRAMITE = 1;
            END IF;


            l_arancel := valorInscripcionArancel;

            --l_arancel := l_precio;

        END IF;
        
    ELSE

    l_arancel := l_precio;

    END IF;

    --REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'FN_PRECIO_REAL', 'arancel', l_arancel, 'OUT');

    RETURN l_arancel;

	EXCEPTION 
        WHEN OTHERS THEN
            
            vlDescMensajeError := SUBSTR(SQLCODE||'-'||SQLERRM,1,1000);  
            dbms_output.put_line('Valor' || vlDescMensajeError);
            REG_PARAM_PLS(SEQ_RUG_PARAM_PLS.NEXTVAL, 'FN_PRECIO_REAL', 'ERROR', vlDescMensajeError, 'OUT');
            RETURN NULL;
END;



   -------------------------------------------------------------------------------
   --            U P D A T E  V I S T A                   --
   -------------------------------------------------------------------------------

/*
    VISTA: V_TRAMITES_PAGADOS
    SENTENCIA: UPDATE VISTA
    OBSERVACION: 
*/

SELECT "ID_TRAMITE","ID_GARANTIA","DESCRIPCION","PRECIO","ID_PERSONA_LOGIN","FECHA", "ANIO" FROM (
SELECT TRA.id_tramite, TER.ID_GARANTIA, TER.DESCRIPCION, PRECIO, TER.ID_PERSONA_LOGIN, TO_CHAR(TER.FECHA_CREACION, 'dd/MM/yyyy hh24:mi:ss') AS FECHA,
TO_CHAR(TER.FECHA_CREACION, 'yyyy ') AS ANIO
FROM V_TRAMITES_TERMINADOS TER , TRAMITES TRA
WHERE TRA.ID_TRAMITE = TER.ID_TRAMITE
AND TRA.B_CARGA_MASIVA <> -1
UNION ALL
SELECT 0, ID_GARANTIA, 'Certificación' AS DESCRIPCION, PRECIO, ID_PERSONA_LOGIN, TO_CHAR(FECHA_CERT, 'dd/MM/yyyy hh24:mi:ss') AS FECHA,
TO_CHAR(FECHA_CERT, 'yyyy ') AS ANIO
FROM RUG_CERTIFICACIONES
WHERE PRECIO IS NOT NULL
AND ID_PERSONA_LOGIN IS NOT NULL
UNION ALL
SELECT 0, 0 AS ID_GARANTIA, 'Consulta' AS DESCRIPCION, TT.PRECIO AS PRECIO, CR.ID_PERSONA, TO_CHAR(CR.FECHAHORA, 'dd/MM/yyyy hh24:mi:ss') AS FECHA,
TO_CHAR(CR.FECHAHORA, 'yyyy ') AS ANIO
FROM RUG_CONSULTA_REGISTRO CR, RUG_CAT_TIPO_TRAMITE TT
WHERE TT.ID_TIPO_TRAMITE = CR.ID_TIPO_TRAMITE
)


   -------------------------------------------------------------------------------
   --            U P D A T E  P R O C E D U R E                   --
   -------------------------------------------------------------------------------

/*
    PROCEDURE: REINICIO_SALDO
    SENTENCIA: UPDATE SP
    OBSERVACION: 
*/

create or replace PROCEDURE reinicio_saldo (codigo_persona IN varchar2, psResult OUT  VARCHAR2)
IS
psTxResult VARCHAR2(2500);
v_saldo_tabla NUMBER;
v_id_persona number;
s_persona number;
saldo_real NUMBER;
l_saldo NUMBER;

BEGIN 

    -- Encontrar ID de persona según codigo
    select id_persona into v_id_persona from V_CODIGO_PERSONAS where codigo = UPPER(codigo_persona);
    
    
     -- Saber el saldo anterior
    
     select saldo into  s_persona  from rug_personas WHERE ID_PERSONA = v_id_persona;
     
     -- Reiniciar Saldo a cero
    UPDATE rug_personas SET SALDO = NULL , FECHA_ACTUALIZACION = NULL WHERE ID_PERSONA = v_id_persona;
    
    DELETE FROM RUG.RUG_SALDO_USUARIOS where ANIO = EXTRACT(YEAR FROM SYSDATE) AND ID_USUARIO = v_id_persona;
    
    COMMIT;
    
    -- Recalcular
    l_saldo := FN_CALCULAR_SALDO(v_id_persona);

    REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_REINICIO_SALDO', 'ID_PERSONA', v_id_persona, 'IN');
    REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_REINICIO_SALDO', 'SALDO_NUEVO', l_saldo, 'IN');
    
    psResult := 'El saldo fue actualizado, Saldo Anterior: ' || s_persona || ' , Saldo Actual ' || l_saldo;
    
    
   

 EXCEPTION 
    WHEN NO_DATA_FOUND THEN
         psResult := 'No se encontro ningún usuario con el código ' || codigo_persona;
         ROLLBACK;
   WHEN OTHERS THEN 
      psTxResult:= substr(SQLCODE||':'||SQLERRM,1,250);
      ROLLBACK;  
      REG_PARAM_PLS (SEQ_RUG_PARAM_PLS.NEXTVAL, 'SP_ACTUALIZAR_SALDO', 'ERROR_SALDO', psTxResult, 'OUT');
    dbms_output.put_line(psTxResult);  
END reinicio_saldo;



   -------------------------------------------------------------------------------
   --            U P D A T E  P R O C E D U R E                   --
   -------------------------------------------------------------------------------

/*
    PROCEDURE: REINICIO_SALDO MASIVO
    SENTENCIA: CREATE SP
    OBSERVACION: 
*/


create or replace PROCEDURE ReiniciarMasivo ( TipoUsuarioRug IN varchar2, psResult OUT NUMBER)
IS
psTxResult VARCHAR2(2500);
tResult VARCHAR2(2500);
codigoUsuario VARCHAR2(100);
success_processes NUMBER := 0;
faileds_processes NUMBER := 0;

    CURSOR listaUsuarios(tipoUsuario VARCHAR2) IS
        select CODIGO from V_CODIGO_PERSONAS WHERE per_juridica = tipoUsuario;

BEGIN 

    FOR l_usuarios IN listaUsuarios(TipoUsuarioRug)
    LOOP
        codigoUsuario := l_usuarios.CODIGO;
        REINICIO_SALDO(codigoUsuario, tResult);
        success_processes := success_processes + 1;
        --dbms_output.put_line('Resultado ' || success_processes);
    END LOOP;

    COMMIT;

    psResult := success_processes;
    
    dbms_output.put_line('Resultado ' || success_processes);
    

    EXCEPTION
        WHEN OTHERS THEN
            psTxResult := 'Error en la Transacción ' || substr(SQLCODE||':'||SQLERRM,1,250);
            faileds_processes := (faileds_processes + 1);
            psResult := faileds_processes;
            dbms_output.put_line('Resultado ' || faileds_processes);
            ROLLBACK;
END ReiniciarMasivo;



   -------------------------------------------------------------------------------
   --            U P D A T E  V I S T A                  --
   -------------------------------------------------------------------------------

/*
    PROCEDURE: V_TRAMITES_TERMINADOS
    SENTENCIA: MODIFICACION
    OBSERVACION: 
*/




SELECT   TRAM.ID_TRAMITE,
            TRAM.ID_TIPO_TRAMITE,
            GTT.DESCRIPCION,
            TRAM.FECH_PRE_INSCR,
            TRAM.FECHA_INSCR,
            RELT.ID_GARANTIA,
            TRAM.ID_STATUS_TRAM,
            STT.DESCRIP_STATUS,
            FN_PRECIO_REAL(TRAM.ID_PERSONA,TRAM.ID_TIPO_TRAMITE,TRAM.ID_TRAMITE,0,RELT.ID_GARANTIA) as PRECIO,
            RBB.FECHA_STATUS as FECHA_creacion,
            NULL as url_paso,
            DECODE (TRAM.ID_PERSONA, 0, TT.ID_PERSONA, TRAM.ID_PERSONA)
               AS ID_PERSONA_LOGIN,
            TRAM.ID_TRAMITE_TEMP,
            (SELECT   ID_PERSONA
               FROM   rug.RUG_REL_TRAM_PARTES
              WHERE   ID_TRAMITE = TRAM.ID_TRAMITE AND ID_PARTE = 4)
               ID_ACREEDOR,
            DECODE ( (SELECT   COUNT ( * )
                        FROM   RUG_TRAMITES_REASIGNADOS
                       WHERE   ID_TRAMITE_TEMP = TRAM.ID_TRAMITE_TEMP),
                    0, 'F',
                    'V')
               TRAMITE_REASIGNADO
     FROM   TRAMITES TRAM,
            RUG_CAT_TIPO_TRAMITE GTT,
            RUG_REL_TRAM_GARAN RELT,
            STATUS_TRAMITE STT,
            RUG_BITAC_TRAMITES RBB,
            RUG_REL_TRAM_GARAN RRTG,
            TRAMITES TT
    WHERE       TRAM.ID_TIPO_TRAMITE = GTT.ID_TIPO_TRAMITE
            AND TRAM.ID_TRAMITE = RELT.ID_TRAMITE(+)
            AND STT.ID_STATUS_TRAM = TRAM.ID_STATUS_TRAM
            AND TRAM.ID_TRAMITE_TEMP = RBB.ID_TRAMITE_TEMP
            AND RBB.ID_STATUS = 3
            AND RELT.STATUS_REG = 'AC'
            AND TRAM.STATUS_REG = 'AC'
            AND RRTG.ID_GARANTIA = RELT.ID_GARANTIA
            AND TT.ID_TRAMITE = RRTG.ID_TRAMITE
            AND TT.ID_TIPO_TRAMITE in (1,31)
   UNION ALL
   SELECT   TRAM.ID_TRAMITE,
            TRAM.ID_TIPO_TRAMITE,
            CASE  
                WHEN TRAM.ID_TIPO_TRAMITE IN (26, 27, 28, 29) THEN
                    FN_BORRA_SIN_CON_GARANTIA(GTT.DESCRIPCION)
                ELSE
                    GTT.DESCRIPCION
            END AS TIPO_TRAMITE,
            TRAM.FECH_PRE_INSCR,
            TRAM.FECHA_INSCR,
            0 ID_GARANTIA,
            TRAM.ID_STATUS_TRAM,
            STT.DESCRIP_STATUS,
            FN_PRECIO_REAL(TRAM.ID_PERSONA,TRAM.ID_TIPO_TRAMITE,TRAM.ID_TRAMITE,0,0),
            RBB.FECHA_STATUS,
            NULL,
            TRAM.ID_PERSONA AS ID_PERSONA_LOGIN,
            TRAM.ID_TRAMITE_TEMP,
            (SELECT   ID_PERSONA
               FROM   rug.RUG_REL_TRAM_PARTES
              WHERE   ID_TRAMITE = TRAM.ID_TRAMITE AND ID_PARTE = 4)
               ID_ACREEDOR,
            DECODE ( (SELECT   COUNT ( * )
                        FROM   RUG_TRAMITES_REASIGNADOS
                       WHERE   ID_TRAMITE_TEMP = TRAM.ID_TRAMITE_TEMP),
                    0, 'F',
                    'V')
               TRAMITE_REASIGNADO
     FROM   TRAMITES TRAM,
            RUG_CAT_TIPO_TRAMITE GTT,
            RUG_REL_TRAM_PARTES RTP,
            STATUS_TRAMITE STT,
            RUG_BITAC_TRAMITES RBB
    WHERE       TRAM.ID_TIPO_TRAMITE = GTT.ID_TIPO_TRAMITE
            AND TRAM.ID_TRAMITE = RTP.ID_TRAMITE(+)
            AND RTP.ID_PARTE = 1
            AND TRAM.ID_STATUS_TRAM IN (3, 10)
            AND STT.ID_STATUS_TRAM = TRAM.ID_STATUS_TRAM
            AND TRAM.ID_TRAMITE_TEMP = RBB.ID_TRAMITE_TEMP
            AND RBB.ID_STATUS = 3
            AND TRAM.STATUS_REG IN ('AC', 'CA')
            AND GTT.ID_TIPO_TRAMITE IN (3, 5, 10, 11, 26, 27, 28, 29, 34, 35)