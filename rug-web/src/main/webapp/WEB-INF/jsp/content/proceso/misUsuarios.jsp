<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set var="idPersona" value="idPersona"/>
<link href="<%=request.getContextPath()%>/resources/css/custom.css" rel="stylesheet">

<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/zf/dt-1.12.1/af-2.4.0/b-2.2.3/fh-3.2.4/kt-2.7.0/r-2.3.0/datatables.min.css"/>


<div class="section"></div>
<div id="app">
<main >
	<div class="container-fluid">
		<div class="row">
			<div class="col s2"></div>
			<div class="col s8">
				<div class="card">
					<div class="card-content">
						<div class="row">
							<div class="col s8">
								<span class="card-title">Mis Usuarios</span>
							</div>
							<div class="col s4 right-align">
								<a class="btn btn-large waves-effect indigo modal-trigger" href="#frmUsuario" id="btnAgregar"><i class="material-icons left">add</i>Agregar</a>
							</div>
							
						</div>
						<div class="row">
							<table id="usuarios">
								<thead>
									<tr>
										<th>Nombre</th>
										<th>Tipo</th>
										<th>Correo</th>
										<th>Opciones</th>
									</tr>
								</thead>
								<tbody>
								<s:iterator value="usuarios">
									<tr id='<s:property value="idPersona"/>'>
										<td><s:property value="nombre"/></td>
										<td><s:property value="isAdmin"/></td>
										<td><s:property value="cveUsuario"/></td>
										<td>
											<a href="#" class="btn waves-effect indigo" onclick='cargaUsuario(<s:property value="idPersona"/>)'><i class="material-icons">edit</i></a>
											<a href="#" class="btn waves-effect red darken-4" onclick='eliminarUsuario(<s:property value="idPersona"/>)'><i class="material-icons">delete</i></a>
											<a href="#" class="btn waves-effect blue darken-4" onclick='permisos(<s:property value="idPersona"/>)'><i class="material-icons">assignment_ind</i></a>
											<a href="#" class="opciones hidePermisos btn waves-effect amber lighten-4 darken-4" id="id_<s:property value="idPersona"/>" name=<s:property value="idPersona"/> onclick='permiso_dinamico(<s:property value="idPersona"/>,"<s:property value="nombre"/>","<s:property value="isAdmin"/>")'><i class="material-icons">merge_type</i></a>
										</td>
									</tr>
								</s:iterator>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<div class="col s2"></div>
		</div>
	</div>
</main>
<%--Modal de permisos--%>
<div class="modal" id="permissionModal">
	<div class="modal-content">
		<div class="card">
			<div class="card-content">
				<div class="row">
					<div class="col s4">
						<h5>Permisos</h5>
						<p>
							Seleccione la opci&oacute;n del men&uacute; que el usuario puede acceder y las sub-opciones de mis Operaciones.
						</p>
					</div>
					<div class="col s8" >
						<ul class="collapsible popout">
							<li>
								<div class="collapsible-header"><i class="material-icons">lock_open</i>Men&uacute;</div>
								<div class="collapsible-body" id="lista_permiso">
								</div>
							</li>
							<li>
								<div class="collapsible-header"><i class="material-icons">art_track</i>Operaciones</div>
								<div class="collapsible-body" id="lista_operaciones"></div>
							</li>
							<li>
								<div class="collapsible-header"><i class="material-icons">note_add</i>Garant&iacute;as</div>
								<div class="collapsible-body">
									<div class="row">
<%--										<form class="col s12">--%>
											<div class="row">
												<div class="input-field col s12">
													<input type="text" id="idGarantia" name="idGarantia">
													<input type="hidden" id="idUsuario" name="idUsuario">
													<label for="idGarantia">No. Garant&iacute;a</label>
												</div>
												<button class="btn waves-effect waves-light blue darken-3"  name="action" onclick="setGarantiaPermiso()">Aceptar
												</button>
											</div>
											<div class="row">
												<div class="custom-alert alert alert-danger alerta-asignacion" role="alert">No se encuentra el n&uacute;mero de garant&iacute;a &oacute; la garant&iacute;a ya se encuentra asignada</div>
											</div>
											<div class="row">
												<table id="table_id" class="hover" style="width:100%">
													<thead>
														<tr>
															<th>Garant&iacute;a</th>
															<th>Estado</th>
															<th>Control</th>
														</tr>
													</thead>
												</table>
											</div>
<%--										</form>--%>
									</div>
								</div>
<%--								<div class="collapsible-body" id="lista_operaciones"></div>--%>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<a href="#!" class="modal-close waves-effect waves-green btn-flat">Aceptar</a>
	</div>
</div>
<%--end--%>
<div class="section"></div>
<div id="frmUsuario" class="modal">
	<div class="modal-content">
		<div class="card">
			<div class="card-content">
				<span class="card-title">Subusuario</span>
				<div class="row">
					<div class="col s1"></div>
					<!--s:form namespace="usuario" action="save.do" theme="simple" cssClass="col s10" method="post"-->
					<input type="hidden" name="usuarioModificar" id="usuarioModificar" />
					<div class="col s10">
						<div class="row">
							<div class="input-field col s12">
								<s:textfield name="usuario.nombre" id="nombre" required="true" cssClass="validate" />
								<label for="nombre">Nombre completo</label>
							</div>
						</div>
					 	<div class="row">
					    	<div class="input-field col s12">
					        	<s:textfield name="usuario.docId" id="docId" required="true" cssClass="validate" maxlength="13" size="40" onkeyup="this.value = this.value.toUpperCase()" onkeypress="return aceptaalfa(event);" />
					        	<label for="docId">Documento de identificacion</label>
					   		</div>
					 	</div>
					 	<div class="row">
					    	<div class="input-field col s12">
					        	<s:textfield name="usuario.cveUsuario" id="email" type="email" required="true" cssClass="validate" maxlength="256" size="40" onkeyup="this.value = this.value.toLowerCase()" />
					        	<label for="email">Correo electronico</label>
					   		</div>
					 	</div>
					 	<div class="row">
					    	<div class="input-field col s12">
					    		<s:text name="usuario.field.password.ayuda" var="password.ayuda" />
					        	<s:password name="usuario.password" id="password" required="true" cssClass="validate tooltipped" data-position="right" data-delay="50" data-tooltip="%{password.ayuda}" maxlength="16" size="40" />
					        	<label for="password"><s:text name="usuario.field.password" /></label>
				        	</div>
					 	</div>
					 	<div class="row">
					    	<div class="input-field col s12">
					        	<s:password name="usuario.confirmacion" id="confirmacion" required="true" cssClass="validate" maxlength="16" size="40" />
					        	<label for="confirmacion"><s:text name="usuario.field.confirmacion" /></label>
					   		</div>
					   		<s:fielderror fieldName="registroUsuario.confirmacion" />
					 	</div>
						<div class="row">
							<p>
								<input type="checkbox" name="isAdmin.button" id="isAdmin.button" value="true" />
								<label for="isAdmin.button">Es usuario Administrador</label>
							</p>
							<!-- <span>Bienes en garant&iacute;a si estos no tienen n&uacute;mero de serie:</span> -->
						</div>
						<div class="row">
							<input type="hidden" name="isUpdateAdmin" id="isUpdateAdmin">
						</div>
					 	<!--div class="row">
					    	<div class="input-field col s12">
    							<s:select name="usuario.pregRecupera" id="pregunta" list="preguntas" listKey="pregunta" listValue="pregunta" onchange="onChangePregunta()" />
					        	<label for="pregunta"><s:text name="usuario.field.pregunta" /></label>
					   		</div>
					 	</div>
					 	<div class="row" id="otra-pregunta" style="display: none;">
					    	<div class="input-field col s12">
					        	<s:textfield name="otraPregunta" id="otraPregunta" cssClass="validate" maxlength="50" size="40" />
					        	<label for="otraPregunta">Ingrese la pregunta</label>
					   		</div>
					 	</div>
					 	<div class="row">
					    	<div class="input-field col s12">
					        	<s:textfield name="usuario.respRecupera" id="respuesta" required="true" cssClass="validate" maxlength="50" size="40" />
					        	<label for="respuesta"><s:text name="usuario.field.respuesta" /></label>
					   		</div>
					   		<s:fielderror fieldName="registroUsuario.respuesta" />
					 	</div-->
					 	<div class="row note mt-5">
							<p>Para la administraci&oacute;n de las subcuentas, usted:
								<br />1. Ratifica que los t&eacute;rminos y condiciones aceptados al crear su cuenta maestra aplican para cada subcuenta creada desde dicha cuenta;
								<br />2. Acepta que las operaciones realizadas por el usuario de la subcuenta estar�n bajo su completa responsabilidad;
								<br />3. La creaci&oacute;n, modificaci&oacute;n e inactivaci&oacute;n de las subcuentas asociadas a su cuenta solo podr&aacute; ser realizada con la cuenta maestra.
							</p>
						</div>
					 	<div class="row center-align" id="error">
					 	</div>
					 	<center>
				            <div class='row'>
				            	<a href="#" class="btn btn-large waves-effect indigo" onclick="guardaUsuario(<s:property value='idPersona' />)">Aceptar</a>
				            </div>
			          	</center>
		          	</div>
					<!--/s:form-->
					<div class="col s1"></div>
				</div>
			</div>
		</div>
	</div>
</div>

</div>

