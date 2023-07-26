<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<script type="text/javascript"
	src="${pageContext.servletContext.contextPath}/resources/js/validaciones.js"></script>
<script type="text/javascript" 
	src="<%=request.getContextPath()%>/resources/js/material-dialog.min.js"></script>
<script type="text/javascript"
	src="${pageContext.servletContext.contextPath}/resources/js/partesJS/parteJS.js"></script>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script type="text/javascript">
function sendForm(){
	document.getElementById("bFirmar").value = "Enviando";
	document.getElementById("bFirmar").disabled = true;
	getObject('fcancelacion').submit();
}

function verificarBienes(_data,_valor){
	if(_data.indexOf(_valor) > -1){
		return true
	}else{
		return false
	}
}

function validarEnvio(idGarantia){
	if(getObject('observaciones').value =="") {
		alertMaterialize('El campo observaciones no puede ser vacio');
		return false;
	}
	const verificartipo = '<%= request.getContextPath() %>/rs/garantias/verificar-tipo/' + idGarantia //verificar tipo de garantia
	const verificarBien = '<%= request.getContextPath() %>/rs/garantias-pend/verificacion-bien/' + idGarantia; // idUltimoTramite y idtipoGarantia
	const verificarBienes2 = '<%= request.getContextPath() %>/rs/garantias-pend/verificacion-bienes/' //idtipoBienEspecial
	const api = '<%= request.getContextPath() %>/rs/garantias-pend/cantidad-garantia/' + idGarantia // idTramiteTemporal de vista
	const apiCantidad = '<%= request.getContextPath() %>/rs/garantias-pend/cantidad-facturas/' //cantidad de facturas
	const costos = '<%= request.getContextPath() %>/rs/tipos-tramite/' //Costo de tramites
	const idTipoTramite = 0;
	let saldo = 0;
	let saldoTotal = 0;


	axios.get(verificartipo).then(responseOne => {
		if (responseOne.data[0] == 16) {
			// Garantia Leasing
			axios.get(costos + 44).then(costo => {
				saldoTotal = costo.data.precio
				MaterialDialog.dialog(
						"El costo de una " + "Cancelacion" + " es de Q. " + Math.round((saldoTotal * 100) / 100).toFixed(2) + ", esta seguro que desea continuar?",
						{
							title: '<table><tr><td width="10%"><i class="medium icon-green material-icons">check_circle</i></td><td style="vertical-align: middle; text-align:left;">Confirmar</td></tr></table>', // Modal title
							buttons: {
								close: {
									className: "red",
									text: "cancelar"
								},
								confirm: {
									className: "indigo",
									text: "aceptar",
									modalClose: false,
									callback: function () {
										sendForm();
									}
								}
							}
						}
				);
			})
		} else if (responseOne.data[0] == 3) {
			// Garantia Factoraje
			axios.get(costos + 47).then(costo => {
				saldoTotal = costo.data.precio
				MaterialDialog.dialog(
						"El costo de una " + "Cancelación" + " es de Q. " + Math.round((saldoTotal * 100) / 100).toFixed(2) + ", esta seguro que desea continuar?",
						{
							title: '<table><tr><td width="10%"><i class="medium icon-green material-icons">check_circle</i></td><td style="vertical-align: middle; text-align:left;">Confirmar</td></tr></table>', // Modal title
							buttons: {
								close: {
									className: "red",
									text: "cancelar"
								},
								confirm: {
									className: "indigo",
									text: "aceptar",
									modalClose: false,
									callback: function () {
										sendForm();
									}
								}
							}
						}
				);
			})
		} else if (responseOne.data[0] == 4) {
			// Garantia Factoraje
			axios.get(costos + 48).then(costo => {
				saldoTotal = costo.data.precio
				MaterialDialog.dialog(
						"El costo de una " + "Cancelación" + " es de Q. " + Math.round((saldoTotal * 100) / 100).toFixed(2) + ", esta seguro que desea continuar?",
						{
							title: '<table><tr><td width="10%"><i class="medium icon-green material-icons">check_circle</i></td><td style="vertical-align: middle; text-align:left;">Confirmar</td></tr></table>', // Modal title
							buttons: {
								close: {
									className: "red",
									text: "cancelar"
								},
								confirm: {
									className: "indigo",
									text: "aceptar",
									modalClose: false,
									callback: function () {
										sendForm();
									}
								}
							}
						}
				);
			})
		}
		else if (responseOne.data[0] == 2) {
			// Garantia Factoraje
			axios.get(api).then(garantia => {
				axios.get(apiCantidad + garantia.data[0]).then(cantidad => {
					saldo = cantidad.data[0]
					axios.get(costos + 36).then(costo => {
						saldoTotal = saldo * costo.data.precio
						MaterialDialog.dialog(
								"El costo de una " + "Cancelación" + " es de Q. " + Math.round((saldoTotal * 100) / 100).toFixed(2) + ", esta seguro que desea continuar?",
								{
									title: '<table><tr><td width="10%"><i class="medium icon-green material-icons">check_circle</i></td><td style="vertical-align: middle; text-align:left;">Confirmar</td></tr></table>', // Modal title
									buttons: {
										close: {
											className: "red",
											text: "cancelar"
										},
										confirm: {
											className: "indigo",
											text: "aceptar",
											modalClose: false,
											callback: function () {
												sendForm();
											}
										}
									}
								}
						);
					})
				})
			})
		}
		else {
			// Garantia Normal
			axios.get(verificarBien).then(bien => {
				axios.get(verificarBienes2 + bien.data[0]).then(idArancel => {
					if (verificarBienes(idArancel.data, 1) || verificarBienes(idArancel.data, 2) || verificarBienes(idArancel.data, 3)) {
						idtipoTramite = 42
					} else if (verificarBienes(idArancel.data, 6)) {
						idtipoTramite = 39
					} else if (verificarBienes(idArancel.data, 5)) {
						idtipoTramite = 41
					} else {
						idtipoTramite = 42
					}
					axios.get(costos + idtipoTramite).then(costo => {
						saldoTotal = costo.data.precio
						MaterialDialog.dialog(
								"El costo de una " + "Cancelación" + " es de Q. " + Math.round((saldoTotal * 100) / 100).toFixed(2) + ", esta seguro que desea continuar?",
								{
									title: '<table><tr><td width="10%"><i class="medium icon-green material-icons">check_circle</i></td><td style="vertical-align: middle; text-align:left;">Confirmar</td></tr></table>', // Modal title
									buttons: {
										close: {
											className: "red",
											text: "cancelar"
										},
										confirm: {
											className: "indigo",
											text: "aceptar",
											modalClose: false,
											callback: function () {
												sendForm();
											}
										}
									}
								}
						);
					})
				})
			})
		}
	})
}

function validarEnvioold(idGarantia) {
	const urlApi = '<%= request.getContextPath() %>/rs/garantias/verificar-tipo/' + idGarantia
	if(getObject('observaciones').value =="") {
		alertMaterialize('El campo observaciones no puede ser vacio');
		return false;
	}

	const api = '<%= request.getContextPath() %>/rs/garantias-pend/cantidad-facturas/' + idTramite
	console.log("Garantia Cancelacion ", idGarantia)
	// obtener el costo de una cancelacion: tipo_tramite=4
	url_bienes = '<%= request.getContextPath() %>/rs/garantias-pend/verificacion-bien/' + idGarantia;
	$.ajax({
		type: 'GET',
		url: urlApi,
		contentType: 'application/json',
		async: true,
		success: function(data){
			$.ajax({
				url: url_bienes,
				success: function(res) {
					path = '<%= request.getContextPath() %>/rs/garantias-pend/verificacion-bienes/' + res
					$.ajax({
						url: path,
						success: function(response) {
							if(data[0] == 16){
								idtipoTramite = 44
							}else if(data[0] == 2){
								idtipoTramite = 36
								axios.get(api).then(response => {
									this.resultFacturas = response.data[0]
									const costos = '<%= request.getContextPath() %>/rs/tipos-tramite/' + idtipoTramite
									axios.get(costos).then(responseTwo => {
										MaterialDialog.dialog(
												"El costo de una " + "Cancelación" + " es de Q. " + Math.round(((responseTwo.data.precio * 100) / 100) * this.resultFacturas).toFixed(2) + ", esta seguro que desea continuar?",
												{
													title:'<table><tr><td width="10%"><i class="medium icon-green material-icons">check_circle</i></td><td style="vertical-align: middle; text-align:left;">Confirmar</td></tr></table>', // Modal title
													buttons:{
														close:{
															className:"red",
															text:"cancelar"
														},
														confirm:{
															className:"indigo",
															text:"aceptar",
															modalClose:false,
															callback:function(){
																sendForm();
															}
														}
													}
												}
										);
									})

								})
							}
							else {
								idArancel = response
								if (verificarBienes(idArancel, 1) || verificarBienes(idArancel, 2) || verificarBienes(idArancel, 3)) {
									idtipoTramite = 42
								} else if (verificarBienes(idArancel, 6)) {
									idtipoTramite = 39
								} else if (verificarBienes(idArancel, 5)) {
									idtipoTramite = 41
								} else {
									idtipoTramite = 42
								}
							}
							$.ajax({
								url: '<%= request.getContextPath() %>/rs/tipos-tramite/'+ idtipoTramite,
								success: function(result) {
									MaterialDialog.dialog(
										"El costo de una " + "Cancelación" + " es de Q. " + (Math.round(result.precio * 100) / 100).toFixed(2) + ", ¿está seguro que desea continuar?",
										{
											title:'<table><tr><td width="10%"><i class="medium icon-green material-icons">check_circle</i></td><td style="vertical-align: middle; text-align:left;">Confirmar</td></tr></table>', // Modal title
											buttons:{
												// Use by default close and confirm buttons
												close:{
													className:"red",
													text:"cancelar"
												},
												confirm:{
													className:"indigo",
													text:"aceptar",
													modalClose:false,
													callback:function(){
														sendForm();
													}
												}
											}
										}
									);
								}
							});
						}
					});
				}
			});
		}
	})
	
}
</script>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="mx.gob.se.rug.seguridad.to.PrivilegioTO"%>
<%@page import="mx.gob.se.rug.seguridad.dao.PrivilegiosDAO"%>
<%@page import="mx.gob.se.rug.seguridad.to.PrivilegiosTO"%>
<%@page import="mx.gob.se.rug.to.UsuarioTO"%>
<%@page import="mx.gob.se.rug.constants.Constants"%>
<%@page import="mx.gob.se.rug.seguridad.to.MenuTO"%>
<%@page import="mx.gob.se.rug.seguridad.serviceimpl.MenusServiceImpl"%>
<%@include file="/WEB-INF/jsp/Layout/menu/applicationCtx.jsp" %>
<main>
<%
//Privilegios
PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
PrivilegiosTO privilegiosTO = new PrivilegiosTO();
privilegiosTO.setIdRecurso(new Integer(6));
privilegiosTO=privilegiosDAO.getPrivilegios(privilegiosTO,(UsuarioTO)session.getAttribute(Constants.USUARIO));
Map<Integer,PrivilegioTO> priv= privilegiosTO.getMapPrivilegio();
%>
<div class="container-fluid">
	<div class="row">
		<div id="menuh">
			<ul>
				<%
				UsuarioTO usuarioTO = (UsuarioTO)session.getAttribute(Constants.USUARIO);
				MenuTO menuTO= new MenuTO(1,request.getContextPath());	
				MenusServiceImpl menuService = (MenusServiceImpl)ctx.getBean("menusServiceImpl");
				
				Boolean noHayCancel= (Boolean) request.getAttribute("noHayCancel");
				Boolean noVigencia = (Boolean) request.getAttribute("vigenciaValida");
				if(noHayCancel==null ||(noHayCancel!=null && noHayCancel.booleanValue()==true)){
					Integer idAcreedorRepresentado=(Integer) session.getAttribute(Constants.ID_ACREEDOR_REPRESENTADO);
					MenuTO menuSecundarioTO = new MenuTO(2, request.getContextPath());
					menuSecundarioTO = menuService.cargaMenuSecundario(idAcreedorRepresentado,usuarioTO,menuSecundarioTO,noVigencia);
					Iterator<String> iterator2 = menuSecundarioTO.getHtml().iterator();
					while (iterator2.hasNext()) {
						String menuItem = iterator2.next();
				%><%=menuItem%>
				<%
					}
				}
					
				%>
			</ul>
		</div>
	</div>
	<div class="row">
		<div class="col s12"><div class="card">
			<div class="col s2"></div>
			<div class="col s8">
				<form id="fcancelacion" name="fcancelacion" action="cancelacion.do">
					<span class="card-title">Cancelaci&oacute;n de la Inscripci&oacute;n</span>
					<div class="row note">
						<p>
							<span>Es la finalizaci&oacute;n de la Inscripci&oacute;n de la Garant&iacute;a Mobiliaria dentro del sistema.</span>
						</p>
					</div>
					<div class="row">
						<div class="input-field col s12">
							<span class="blue-text text-darken-2">Garant&iacute;a
								Mobiliaria No. </span> <span> <s:property value="idGarantia" /> <br>
							</span>
						</div>
					</div>																	
					<div class="row">					
						<div class="input-field col s12">
							<s:textarea rows="10" cols="80" name="observaciones"
							        id="observaciones" />
							<label for="observaciones">Observaciones o motivos de la cancelaci&oacute;n: </label>
						</div>							
					</div>					
					 <hr />
			 	<center>
		            <div class='row'>			            	
		            	<input type="button" id="bFirmar" name="button" class="btn btn-large waves-effect indigo" value="Aceptar" onclick="validarEnvio(<s:property value='idGarantia' />);"/>			            				            							            	
		            </div>
	          	</center>		          					
				</form>
			</div>
			<div class="col s2"></div>
		</div>
	</div>
	</div>
</div>
</main>