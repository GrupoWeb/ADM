<%@page import="mx.gob.se.rug.garantia.dao.GarantiasDAO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="section"></div>
<main>
	<div class="container">
		<div class="card">
<%--			<div class="card-content">--%>
<%--				<p>Consulta Principal</p>--%>
<%--			</div>--%>

			<div class="card-tabs">
				<ul class="tabs tabs-fixed-width">
					<li onclick="limpiar_filtros()" class="tab"><a class="active" href="#criterio">B&uacute;squeda  por Criterios (Sin costo)</a></li>
					<li onclick="limpiar_filtros()" class="tab"><a href="#secundaria">B&uacute;squeda  Secundaria (Sin costo)</a></li>
					<li onclick="limpiar_filtros()" class="tab"><a href="#factura">B&uacute;squeda por Factura (Sin costo)</a></li>
				</ul>
			</div>
			<div class="card-content grey lighten-4">
				<div id="criterio">
					<div class="row">
						<s:form namespace="usuario" action="resBusqueda.do" theme="simple" cssClass="col s12">
							<div class="row">
								<div class="input-field col s12">
									<s:textfield name="curpOtorgante" id="curpOtorgante" size="15" maxlength="15" onkeypress="return aceptaalfa(event);"
												 cssClass="validate tooltipped" data-position="right" data-delay="50" data-tooltip="Ingrese su n�mero sin espacios en blanco"/>
									<label for="curpOtorgante">N&uacute;mero de Identificaci&oacute;n del Deudor Garante (DPI o Pasaporte)</label>
								</div>
							</div>
							<div class="row">
								<div class="input-field col s12">
									<s:textfield name="rfcOtorgante" id="rfcOtorgante" size="15" maxlength="15" onkeypress="return aceptaalfa(event);"
												 cssClass="validate tooltipped" data-position="right" data-delay="50" data-tooltip="Ingrese su n�mero sin espacios en blanco ni gui�n"/>
									<label for="rfcOtorgante">NIT del Deudor Garante</label>
								</div>
							</div>
							<div class="row">
								<div class="input-field col s12">
									<s:textfield name="serial" id="serial" size="250"  maxlength="250" cssClass="validate tooltipped" data-position="right" data-delay="50" data-tooltip="Si busca la informaci�n de un veh�culo, colocar los datos del VIN exactamente como aparecen en la tarjeta de circulaci�n de este"/>
									<label for="serial">N&uacute;mero de serie</label>
								</div>
							</div>
                                                                        
                                                                        <h5>Datos de quien hace la consulta</h5>                                                                        
							<center>
								<div class='row'>
                                                                    <div class="input-field col s12">
                                                                        <label>Nombre de la persona que consulta</label>
                                                                        <input type="text" id="consulta_nombre_1" class="campo_busqueda" >
                                                                    </div>
                                                                    <div class="input-field col s12">
                                                                        <label>Nro. Id de la persona que consulta</label>
                                                                        <input type="text" id="consulta_id_1" class="campo_busqueda" >
                                                                    </div>
                                                                    
									<a class="btn btn-large waves-effect indigo" onclick="ejecutar_busqueda_jsp()">Consultar</a>
                                                                        <a class="btn btn-large waves-effect indigo" onclick="limpiar_filtros()">Limpiar</a>
                                                                        <p>Para asegurar que encuentre la informaci&oacute;n deseada, es mejor que solo ingrese un campo por cada b&uacute;squeda</p>
								</div>
							</center>
						</s:form>
					</div>
				</div>
				<div id="secundaria">
					<div class="row">
						<s:form namespace="usuario" action="resBusqueda.do" theme="simple" cssClass="col s12">
							<div class="row">
								<div class="input-field col s12">
									<s:textfield name="nombreOtorgante" id="nombreOtorgante" size="32" maxlength="100" cssClass="validate tooltipped" data-position="right" data-delay="50"  data-tooltip="Ingresar el nombre completo de persona que desea buscar"/>
									<label for="nombreOtorgante">Nombre o Raz&oacute;n Social del Deudor Garante</label>
								</div>
							</div>
							<div class="row">
								<div class="input-field col s12">
									<s:textfield name="idGarantia" id="idGarantia" size="17" type="number"   maxlength="20" cssClass="validate tooltipped" data-position="right" data-delay="50"  data-tooltip="Ingresar �nicamente el n�mero de garant�a" />
									<label for="idGarantia">N&uacute;mero de inscripci&oacute;n de la Garant&iacute;a</label>
								</div>
							</div>
							<h5>Datos de quien hace la consulta</h5>   
							<center>
								<div class='row'>

									<div class="input-field col s12">
										<label>Nombre de la persona que consulta</label>
										<input type="text" id="consulta_nombre_2" class="campo_busqueda" >
									</div>
									<div class="input-field col s12">
										<label>Nro. Id de la persona que consulta</label>
										<input type="text" id="consulta_id_2" class="campo_busqueda" >
									</div>
									<a class="btn btn-large waves-effect indigo" onclick="ejecutar_busqueda_jsp2()">Consultar</a>
                                                                        <a class="btn btn-large waves-effect indigo" onclick="limpiar_filtros()">Limpiar</a>

                                                                        <p>Para asegurar que encuentre la informaci&oacute;n deseada, es mejor que solo ingrese un campo por cada b&uacute;squeda</p>
								</div>
							</center>
						</s:form>
					</div>
				</div>
				<div id="factura">
					<div class="row">
						<s:form	 namespace="usuario" action="busquedaFac.do" theme="simple" cssClass="col s12">
							<div class="row">
                                <div class="input-field col s2">
									<s:textfield id="nit" name="nit" class="validate" />
									<label for="nit">Nit:</label>
								</div>
								<div class="input-field col s5">
									<s:textfield id="invoice" name="invoice" class="validate" />
									<label for="invoice">No. Factura:</label>
								</div>
								<div class="input-field col s5">
									<s:textfield type="text" id="set" name="set" class="validate" />
									<label for="set">Serie:</label>
								</div>
								<h5>Datos de quien hace la consulta</h5>
								<div class="row center-align">
									<div class="input-field col s12">
										<label>Nombre de la persona que consulta</label>
										<input type="text" id="consulta_nombre_3" class="campo_busqueda" >
									</div>
									<div class="input-field col s12">
										<label>Nro. Id de la persona que consulta</label>
										<input type="text" id="consulta_id_3" class="campo_busqueda" >
									</div>
									<a class="btn btn-large waves-effect indigo" onclick=" ejecutar_busqueda_jsp3()">Consultar</a>
                                                                        <a class="btn btn-large waves-effect indigo" onclick="limpiar_filtros()">Limpiar</a>                                                                        
                                                                        <p>Para asegurar que encuentre la informaci&oacute;n deseada, es mejor que solo ingrese un campo por cada b&uacute;squeda</p>
								</div>
							</div>
						</s:form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col s12">
				<div class="card">
					<div class="card-content">
						<span class="card-title">Resultado de la Consulta</span>
						<div id="resultadoDIV"></div>  
					</div>
				</div>
			</div>
		</div>
	</div>
</main>
<div class="section"></div>

<div id="cuerpo"><!-- Empieza div cuerpo -->
	<div align="center" >
    <table width="960px"> 
      <tr>
			<td valign="top">
        
				<table width="100%" height="100%" >
					
					
					<tr><td> 
            
            
              <table width="100%">
                <tr>
                  <td>
                    <table>  					
                      <s:if test="hayDatos">
                        <tr id="datosCadena">
                          <td>
                            <table >
                              <tr>
                                <td align="left"><span class="textoGeneralRojo"> La Cadena &Uacute;nica de Datos se encuentra relacionado al Tr&aacute;mite:</span> <span class="textoGeneral"><s:property value="tipoOperacion"/> </span></td>  
                              </tr>
                              <tr>
                                <td align="left"> <span class="textoGeneralRojo"> El n&uacute;mero de garant&iacute;a es:</span> <span class="textoGeneral"><s:property value="numGarantia"/> </span></td>
                              </tr>
                              <tr>
                                <td align="left"> <span class="textoGeneralRojo"> Fue realizada el d&iacute;a: </span> <span class="textoGeneral"> <s:property value="fechaInsc"/> </span></td> 
                              </tr>
                              <tr>	
                                <td align="left"> <span class="textoGeneralRojo"> Por el Otorgante de la Garant&iacute;a Mobiliaria: </span> <span class="textoGeneral"> <s:property value="nomOtorgante"/> </span></td>
                              </tr>
                              <tr>	
                                <td align="left"><span class="textoGeneralRojo"> Con el folio electr&oacute;nico: </span> <span class="textoGeneral"> <s:property value="folioOtorgante"/> </span></td>
                              </tr>
                              <tr>
                                <td align="center"> 
																	<h4 style="cursor: pointer">
                                    <a onclick="showBoleta();"> <u>Clic aqu&iacute; para ver la boleta</u></a>
																	</h4>
                                </td>
                              </tr>
                            </table>
                          </td>
                        </tr>
                      </s:if>
                      <s:else>
												<s:if test="pasoInicio">
													<tr id="noDatosCadena">
														<td>
															<table>
																<tr>
																	<td class="textoGeneralRojo" align="center"> No existe un Tr&aacute;mite relacionado a esta Cadena &Uacute;nica de Datos</td>  
																</tr>
															</table>
														</td>
													</tr>
												</s:if>
                      </s:else>
                    </table>
                  </td>
                </tr>
                
								<!-- 	<tr> <td height="18px" style="padding-bottom: 1px; padding-top: 18px"> &nbsp;</td> </tr> -->
                
              </table>
					</table>
					
		</td>
      </tr>
      
      <tr>
        <td colspan="2">
          <div id="resultadoOtorgantesPrevios"></div>
        </td>
      </tr>
      <tr>
        <td colspan="2">
          <div id="resultadoDIV"></div>
        </td>
      </tr>
    </table>
	</div>
</div>

<script type="text/javascript">
    function ejecutar_busqueda_jsp()
    {
        if ($("#consulta_nombre_1").val() == ""  || $("#consulta_id_1").val() == "")
        {
               alert('Ingrese nombre y documento de identificacion para poder realizar la busqueda');
               return;
        }
        busquedaJSP(51071,4,$("#consulta_nombre_1").val(),$("#consulta_id_1").val());
            


    }   
	function ejecutar_busqueda_jsp2()
    {
        if ($("#consulta_nombre_2").val() == ""  || $("#consulta_id_2").val() == "")
        {
               alert('Ingrese nombre y documento de identificacion para poder realizar la busqueda');
               return;
        }
        //busquedaJSP(51071,4,$("#consulta_nombre_2").val(),$("#consulta_id_2").val());
		busquedaJSP(51071,5,$("#consulta_nombre_2").val(),$("#consulta_id_2").val());
                 
    }   	

    function limpiar_filtros()
    {
        console.log('limpiando');
        $("#curpOtorgante").val ("");
        $("#rfcOtorgante").val ("");
        $("#serial").val ("");
        $("#consulta_nombre_1").val ("");
        $("#consulta_id_1").val ("");
        $("#nombreOtorgante").val ("");
        $("#nombreOtorgante").val ("");
        $("#consulta_nombre_2").val ("");
        $("#consulta_id_2").val ("");
        
                $("#nit").val ("");
        $("#invoice").val ("");        $("#set").val ("");
        $("#consulta_nombre_3").val ("");        
        $("#consulta_id_3").val ("");
        
         $("#idGarantia").val ("");
        
        
        
    }
	

    function ejecutar_busqueda_jsp3()
    {
        if ($("#consulta_nombre_3").val() == ""  || $("#consulta_id_3").val() == "")
        {
               alert('Ingrese nombre y documento de identificacion para poder realizar la busqueda');
               return;
        }
        //busquedaJSP(51071,4,$("#consulta_nombre_2").val(),$("#consulta_id_2").val());
		//busquedaJSP(51071,5,$("#consulta_nombre_3").val(),$("#consulta_id_3").val());
		checkText2(51071,3,$("#consulta_nombre_3").val(),$("#consulta_id_3").val());
                    
                   

    }

</script>

 