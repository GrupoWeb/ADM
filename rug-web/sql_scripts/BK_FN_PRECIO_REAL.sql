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