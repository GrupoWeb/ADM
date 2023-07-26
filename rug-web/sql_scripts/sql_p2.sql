
-- Creación de nuevo grupo

INSERT INTO RUG_GRUPOS(ID_GRUPO,ID_ACREEDOR, DESC_GRUPO, ID_PERSONA_CREA, FH_CREACION, SIT_GRUPO)
VALUES(5502,1,'SUBADMINISTRADOR',1,SYSDATE,'AC');



INSERT INTO RUG_GRUPOS(ID_GRUPO,ID_ACREEDOR, DESC_GRUPO, ID_PERSONA_CREA, FH_CREACION, SIT_GRUPO)
VALUES(SEQ_GRUPOS.nextval,1,'test',1,SYSDATE,'AC');

-- asignación de grupo con privilegios

INSERT INTO RUG_REL_GRUPO_PRIVILEGIO(ID_RELACION, ID_GRUPO, ID_PRIVILEGIO, SIT_RELACION)
VALUES(SEQ_REL_GRUPO_PRIVILEGIO.nextval, 5502, 62, 'AC');

INSERT INTO RUG_REL_GRUPO_PRIVILEGIO(ID_RELACION, ID_GRUPO, ID_PRIVILEGIO, SIT_RELACION)
VALUES(SEQ_REL_GRUPO_PRIVILEGIO.nextval, 5502, 63, 'AC');

INSERT INTO RUG_REL_GRUPO_PRIVILEGIO(ID_RELACION, ID_GRUPO, ID_PRIVILEGIO, SIT_RELACION)
VALUES(SEQ_REL_GRUPO_PRIVILEGIO.nextval, 5502, 64, 'AC');

INSERT INTO RUG_REL_GRUPO_PRIVILEGIO(ID_RELACION, ID_GRUPO, ID_PRIVILEGIO, SIT_RELACION)
VALUES(SEQ_REL_GRUPO_PRIVILEGIO.nextval, 5502, 65, 'AC');

INSERT INTO RUG_REL_GRUPO_PRIVILEGIO(ID_RELACION, ID_GRUPO, ID_PRIVILEGIO, SIT_RELACION)
VALUES(SEQ_REL_GRUPO_PRIVILEGIO.nextval, 5502, 66, 'AC');

INSERT INTO RUG_REL_GRUPO_PRIVILEGIO(ID_RELACION, ID_GRUPO, ID_PRIVILEGIO, SIT_RELACION)
VALUES(SEQ_REL_GRUPO_PRIVILEGIO.nextval, 5502, 67, 'AC');

INSERT INTO RUG_REL_GRUPO_PRIVILEGIO(ID_RELACION, ID_GRUPO, ID_PRIVILEGIO, SIT_RELACION)
VALUES(SEQ_REL_GRUPO_PRIVILEGIO.nextval, 5502, 68, 'AC');

INSERT INTO RUG_REL_GRUPO_PRIVILEGIO(ID_RELACION, ID_GRUPO, ID_PRIVILEGIO, SIT_RELACION)
VALUES(SEQ_REL_GRUPO_PRIVILEGIO.nextval, 5502, 69, 'AC');

INSERT INTO RUG_REL_GRUPO_PRIVILEGIO(ID_RELACION, ID_GRUPO, ID_PRIVILEGIO, SIT_RELACION)
VALUES(SEQ_REL_GRUPO_PRIVILEGIO.nextval, 5502, 70, 'AC');

INSERT INTO RUG_REL_GRUPO_PRIVILEGIO(ID_RELACION, ID_GRUPO, ID_PRIVILEGIO, SIT_RELACION)
VALUES(SEQ_REL_GRUPO_PRIVILEGIO.nextval, 5502, 71, 'AC');

INSERT INTO RUG_REL_GRUPO_PRIVILEGIO(ID_RELACION, ID_GRUPO, ID_PRIVILEGIO, SIT_RELACION)
VALUES(SEQ_REL_GRUPO_PRIVILEGIO.nextval, 5502, 72, 'AC');

INSERT INTO RUG_REL_GRUPO_PRIVILEGIO(ID_RELACION, ID_GRUPO, ID_PRIVILEGIO, SIT_RELACION)
VALUES(SEQ_REL_GRUPO_PRIVILEGIO.nextval, 5502, 73, 'AC');

-- Creación de Privilegios <li><a href="@contextPath/home/certificacion.do"><span>Certificación</span></a></li>

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(62,'Inscripcion','<li><a href="@contextPath/inscripcion/paso1.do" class="link"><span id="cuatroMenu">Inscripción</span></a></li>', 'AC', 1, 1);

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(63,'Certificacion','<li><a href="@contextPath/home/certifica.do" class="link"><span>Certificaciones</span></a></li>', 'AC', 1, 2);

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(64,'Busqueda','<li id="unoMenuId"><a href="@contextPath/home/busqueda.do" class="link"><span id="unoMenu">Busqueda</span></a></li>', 'AC', 1, 3);

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(65,'Mis operaciones','<li><a href="@contextPath/home/misOperaciones.do" class="link"><span id="sieteMenu">Mis Operaciones</span></a></li>','AC', 1, 4);

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(66,'Mis Boletas','<li><a href="@contextPath/home/misBoletas.do" class="link"><span id="ochoMenu">Mis Boletas</span></a></li>', 'AC', 1, 5);

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(67,'Formulario Traslado','<li><a href="@contextPath/home/formularioUnico.do" class="link"><span id="nueveMenu">Formulario Traslado</span></a></li>', 'AC', 1, 16);

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(68,'Mis Usuarios','<li><a href="@contextPath/usuario/users.do" class="link"><span class="new badge red" data-badge-caption="Nueva"></span>Mis Usuarios</a></li>', 'AC', 1, 7);

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(69,'Formulario Titulo','<li><a class="waves-effect waves-indigo" style="color: #4f5050 !important;opacity: 0.7;cursor: initial;">Formularios Especiales</a></li>', 'AC', 1, 8);

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(70,'Factoraje ','<li><a href="@contextPath/home/factoraje.do?idTipoGarantia=2" class="link"><span id="nueveMenu">Factoraje</span></a></li>', 'AC', 1, 9);

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(71,'Derechos de Credito','<li><a href="@contextPath/home/factoraje.do?idTipoGarantia=3" class="link"><span class="new badge red" data-badge-caption="Nueva"></span>Derechos de Crédito</a></li>', 'AC', 1, 10);

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(72,'Compra-Venta','<li><a href="@contextPath/home/factoraje.do?idTipoGarantia=4" class="link"><span class="new badge red" data-badge-caption="Nueva"></span>Compra-Venta</a></li>', 'AC', 1, 11);

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(73,'Leasing','<li><a href="@contextPath/leasing/paso1.do" class="link"><span id="cuatroMenuLeasing">Leasing</span></a></li>', 'AC', 1, 12);


-- sub menu

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(74,'Mis Operaciones Cancelacion','<li><a href="@contextPath/home/iniciaCancelacion.do" ><span>Cancelación</span></a></li>', 'AC', 2, 13);

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(75,'Mis Operaciones Certificacion','<li><a href="@contextPath/home/certificacion.do"><span>Certificación</span></a></li>', 'AC', 2, 14);

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(76,'Mis Operaciones Modificacion','<li><a href="@contextPath/home/modificacion.do"><span>Modificación</span></a></li>', 'AC', 2, 15);

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(77,'Mis Operaciones Renovacion','<li><a href="@contextPath/home/prorroga.do"><span>Prorroga de vigencia</span></a></li>', 'AC', 2, 16);

INSERT INTO RUG_PRIVILEGIOS(ID_PRIVILEGIO, DESC_PRIVILEGIO, HTML,  SIT_PRIVILEGIO, ID_RECURSO, ORDEN)
VALUES(78,'Mis Operaciones Ejecucion','<li><a href="@contextPath/home/ejecucion.do"><span>Ejecución</span></a></li>', 'AC', 2, 17);

-- TABLA DE GARANTIAS Y PERMISOS

CREATE TABLE RUG_GARANTIA_PERMISO
(
    ID NUMBER NOT NULL
    , ID_GARANTIA NUMBER NOT NULL
    , ID_TRAMITE NUMBER NOT NULL
    , ID_USUARIO NUMBER NOT NULL
    , SIT VARCHAR2(20)
    , CREATED_AT DATE
    , UPDATE_AT DATE
    , DELETED_AT DATE
    , CONSTRAINT RUG_GARANTIA_PERMISO_PK PRIMARY KEY
    (
     ID
        )
    ENABLE
);


-- SECUENCIA DE GARANTIA PERMISO observacion(cambiar el cache y el orden)

CREATE SEQUENCE "RUG"."SEQ_GARANTIA_PERMISO" MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE;

-- test
INSERT INTO RUG_GARANTIA_PERMISO(ID, ID_GARANTIA, ID_TRAMITE, ID_USUARIO, SIT, CREATED_AT)
VALUES(SEQ_GARANTIA_PERMISO.nextval,94505,124235,205230,'AC',SYSDATE);

-- Observacion
-- Se tiene que agregar la columna IS_ADMIN a la tabla de RUG_SECU_USUARIO

-- Bitacora de creacion de grupos para usuarios
-- grupo 5502 para sub administradores
-- grupo N = numero de grupo creado inicialmente

CREATE TABLE RUG_GRUPO_SUBUSUARIOS
(
    ID NUMBER NOT NULL
    , ID_USUARIO NUMBER NOT NULL
    , ID_GRUPO NUMBER NOT NULL
    , ID_GRUPO_ANTERIOR NUMBER NOT NULL
    , CONSTRAINT RUG_GRUPO_SUBUSUARIOS_PK PRIMARY KEY
    (
     ID
        )
    ENABLE
);


CREATE SEQUENCE  "RUG"."SEQ_GRUPO_SUBUSUARIO"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 ORDER  NOCYCLE ;