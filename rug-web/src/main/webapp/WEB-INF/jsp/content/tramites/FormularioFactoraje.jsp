<%@page import="java.util.Iterator" %>
<%@page import="java.util.Map" %>
<%@page import="mx.gob.se.rug.seguridad.to.PrivilegioTO" %>
<%@page import="mx.gob.se.rug.seguridad.dao.PrivilegiosDAO" %>
<%@page import="mx.gob.se.rug.seguridad.to.PrivilegiosTO" %>
<%@page import="mx.gob.se.rug.to.UsuarioTO" %>
<%@page import="mx.gob.se.rug.seguridad.to.MenuTO" %>
<%@page import="mx.gob.se.rug.seguridad.serviceimpl.MenusServiceImpl" %>
<%@page import="mx.gob.se.rug.constants.Constants" %>
<%@include file="/WEB-INF/jsp/Layout/menu/applicationCtx.jsp" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<script src="<%=request.getContextPath()%>/resources/js/jquery-3.3.1.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/xlsx.core.min.js"></script>


<main>
    <%
        //Privilegios
        PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
        PrivilegiosTO privilegiosTO = new PrivilegiosTO();
        privilegiosTO.setIdRecurso(new Integer(6));
        privilegiosTO = privilegiosDAO.getPrivilegios(privilegiosTO, (UsuarioTO) session.getAttribute(Constants.USUARIO));
        Map<Integer, PrivilegioTO> priv = privilegiosTO.getMapPrivilegio();
    %>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/factoraje.css">
    <script src="<%=request.getContextPath()%>/resources/js/jquery-3.3.1.min.js"></script>
    <input type="hidden" name="tipoBienAll" value="false" id="idTipoBienAll"/>
    <div class="container-fluid">
        <div id="modifica" class="row">
            <div class="col s12">
                <div class="card">
                    <div class="col s2"></div>
                    <div class="col s8">
                        <!-- row note teal -->
                        <form id="fafactoraje" name="fafactoraje" action="saveFactoraje.do" method="post">
                            <span class="card-title"><s:property value="%{textosFormulario.get(0)}"/></span>
                            <input type="hidden" name="refInscripcion" id="refInscripcion"
                                   value="<s:property value='idTramite'/>"/>
                            <input type="hidden" name="reftipogarantia" id="reftipogarantia" value="<s:property value='idTipoGarantia'/>"/>
                            <div class="row note_tabs light-blue darken-4">
                                <span class="white-text"><s:property value="%{textosFormulario.get(1)}"/></span>
                            </div>
                            <div class="row">
                                <div id="divParteDWRxx2"></div>
                            </div>
                            <div class="row note_tabs light-blue darken-4">
                                <span class="white-text"><s:property value="%{textosFormulario.get(2)}"/></span>
                            </div>
                            <div class="row">
                                <div id="divParteDWRxx3"></div>
                            </div>
                            <s:if test="hayOtorgantes">
                                <div class="row note_tabs light-blue darken-4">
                                    <span class="white-text"><s:property value="%{textosFormulario.get(3)}"/></span>
                                </div>
                                <div class="row">
                                    <div id="divParteDWRxx4"></div>
                                </div>
                            </s:if>
                            <div class="row note_tabs light-blue darken-4">
                                <span class="white-text"><s:property value="%{textosFormulario.get(4)}"/></span>
                            </div>
                            <div class="row">
                                <div class="input-field col s12">
                                    <s:textarea name="moddescripcion" cols="70" rows="10" id="tiposbienes"
                                                value="%{moddescripcion}"/>
                                    <label for="tiposbienes">Descripci&oacute;n General:</label>
                                </div>
                            </div>
                            <div class="row">
                                <span class="blue-text text-darken-2"><s:property
                                        value="%{textosFormulario.get(5)}"/></span>
                            </div>
                            <div class="row">
                                <div class="col s12 right-align">
                                    <a class="btn-floating btn-large waves-effect indigo modal-trigger state1"
                                       onclick="eventModal()" href="#frmBien" id="btnAgregar"><i class="material-icons left">add</i></a>
                                    <a class="btn-floating btn-large waves-effect indigo modal-trigger state1"
                                       onclick="limpiaCamposFile()"
                                       href="#frmFile" id="btnFile"><i
                                            class="material-icons left">attach_file</i></a>
                                </div>
                                <div id="divParteDWRBienes"></div>
                            </div>
                            <div class="row note_tabs light-blue darken-4">
                                <span class="white-text"><s:property value="%{textosFormulario.get(6)}"/></span>
                            </div>
                            <div class="row">
                                <div class="input-field col s12">
                                    <s:textarea rows="10" cols="80" id="instrumento" name="instrumento"
                                                value="%{instrumento}"
                                                maxlength="3500"/>
                                    <label for="instrumento"><s:property value="%{textosFormulario.get(7)}"/></label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="input-field col s12">
                                    <s:textarea id="modotrosgarantia" name="modotrosgarantia" cols="80" rows="10"
                                                maxlength="3500" value="%{modotrosgarantia}"/>
                                    <label for="modotrosgarantia"><s:property
                                            value="%{textosFormulario.get(8)}"/></label>
                                </div>
                            </div>
                            <hr/>
                            <center>
                                <div class='row'>
                                    <input type="button" id="bEditar" name="button"
                                           class="btn btn-large waves-effect indigo state2" value="Corregir Datos"
                                           onclick="mi_funcion_editar();"/>
                                    <input type="button" id="bContinuar" name="button"
                                           class="btn btn-large waves-effect indigo state1" value="Continuar"
                                           onclick="habilitar_continuar();"/>
                                    <input type="button" id="bFirmar" name="button"
                                           class="btn btn-large waves-effect indigo state2" value="Aceptar"
                                           onclick="registrar_factoraje();"/>
                                </div>
                            </center>
                        </form>
                        <s:if test="aBoolean">
                            <script type="text/javascript">
                                document.getElementById('aBoolean').checked = 1;
                            </script>
                        </s:if>
                        <s:if test="aMonto">
                            <script type="text/javascript">
                                document.getElementById('aMonto').checked = 1;
                            </script>
                        </s:if>
                        <s:if test="aPrioridad">
                            <script type="text/javascript">
                                document.getElementById('aPrioridad').checked = 1;
                            </script>
                        </s:if>
                        <s:if test="aRegistro">
                            <script type="text/javascript">
                                document.getElementById('aRegistro').checked = 1;
                                document.getElementById("txtregistro").disabled = false;
                                Materialize.updateTextFields();
                            </script>
                        </s:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>


<div id="frmBien" class="modal  modal-fixed-footer">
    <div class="modal-content">
        <div class="card">
            <div class="card-content">
                <span class="card-title">Facturas</span>
                <div class="row">
                    <div class="col s12">
                        <div class="row">
                            <div class="input-field col s12">
                                <s:select
                                        name="mdBienEspecial"
                                        id="mdBienEspecial"
                                        list="listaBienEspecial"
                                        listKey="idTipo"
                                        listValue="desTipo"
                                        onchange="cambiaBienesEspeciales()"/>
                                <label>Tipo Bien Especial</label>
                            </div>
                        </div>
                        <div id="showContainerData">
                            <!-- Invoice Section -->
                            <div id="invoiceSection" class="row" style="display: none;">
                                <div class="input-field col s6">
                                    <s:textfield name="nitContribuyente" id="nitContribuyente" cssClass="validate" maxlength="10"
                                                 onkeypress="return characterNotAllowed(event);"/>
                                    <label for="nitContribuyente">No. Contribuyente Emite (NIT)</label>
                                </div>
                                <div class="input-field col s6">
                                    <input type="text" name="mdInvoiceDate" class="datepicker" id="mdInvoiceDate"/>
                                    <label for="mdInvoiceDate">Fecha</label>
                                </div>
                            </div>

                            <!-- General Description Section -->
                            <div class="row">
                                <div class="input-field col s12">
                                    <s:textarea rows="10" id="mdDescription" cols="80" name="mdDescription" data-length="700"
                                                cssClass="materialize-textarea" maxlength="700"/>
                                    <label for="mdDescription">Observaciones Generales</label>
                                </div>
                            </div>

                            <!-- VIN Section -->
                            <div id="vinSection" class="row" style="display: none;">
                                <div class="input-field col s12">
                                    <s:textfield name="mdFacturaElectronica" id="mdFacturaElectronica" cssClass="validate" maxlength="150" placeholder="19627664-6353-4057-BB2A-98497519F8CE (con guiones,sin espacios)"/>
                                    <label for="mdFacturaElectronica">N&uacute;mero de autorizaci&oacute;n</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <div>
            <a id="formBienButton" class="modal-action modal-close btn teal lighten-1" onclick="sendFormData();">Aceptar</a>
        </div>
    </div>

</div>

<div id="frmFile" class="modal">
    <div class="modal-content">
        <div class="card">
            <div class="card-content">
                <span class="card-title">Bien Especial</span>
                <div class="row">
                    <div class="col s1"></div>
                    <div class="col s10">
                        <div class="row">
                            <div class="input-field col s12">
                                <s:select name="mdBienEspecial2" id="mdBienEspecial2" list="listaBienEspecial"
                                          listKey="idTipo" listValue="desTipo"
                                          onchange="cambiaBienesEspecialesFile()"/>
                                <label>Tipo Bien Especial</label>
                            </div>
                        </div>
                        <div id="secTxt3" class="row note">
                            <span id="txtspan" name="txtspan"></span>
                        </div>
                        <div class="row">
                            <div class="input-field col s8">
                                <div class="file-field input-field">
                                    <div class="btn">
                                        <span>Archivo</span>
                                        <input type="file" name="excelfile" id="excelfile"/>
                                    </div>
                                    <div class="file-path-wrapper">
                                        <input class="file-path validate" type="text" name="namefile" id="namefile"
                                               placeholder="Seleccione...">
                                    </div>
                                </div>
                            </div>
                            <div class="input-field col s4">
                                <a href="#!"
                                   class="btn-large indigo"
                                   onclick="ExportToTable();">Cargar</a>
                            </div>
                        </div>
                        <div class="row">
                            <table id="exceltable">
                            </table>
                        </div>
                        <br/>
                        <hr/>
                        <center>
                            <div class="row">
                                <a href="#!"
                                   class="modal-close btn-large indigo">Aceptar</a>
                            </div>
                        </center>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    window.addEventListener('keydown', function (event) {
        if (event.key === 'F5') {
            event.preventDefault();
            let storedData = JSON.parse(localStorage.getItem('formData')) || [];
            if (storedData.length > 0) {
                storedData.forEach((entry, index) => {
                    habilitarFacturas(entry.mdFacturaElectronica,entry.nitContribuyente)
                });
                localStorage.removeItem('formData');
                window.location.reload();
            } else {
                console.log('No hay datos en localStorage.');
            }
        }
    });
</script>



