<%@page contentType="text/html" %>
	<%@page pageEncoding="UTF-8" %>
		<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/material-dialog.min.js"></script>
		<script src="<%=request.getContextPath()%>/resources/js/sweetalert.js"></script>
		<script type="text/javascript"
			src="${pageContext.servletContext.contextPath}/dwr/interface/BusquedaDwrAction.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/dwr/busquedaDWR.js"></script>
		<script type="text/javascript"
			src="${pageContext.servletContext.contextPath}/resources/js/validaciones/validaciones.js"></script>

		<script type="text/javascript">

			let datos = false;

			function tramitesJSP(persona) {
				checkUser(persona);
				var ruta = '${pageContext.servletContext.contextPath}';
				console.log(ruta);
				certificacionDwr(ruta);
			}



			function certificacion(garantia, tramite) {
                              
				const signature_api = 'https://firma.rgm.gob.gt/api/files/';
				const apiSignature = '<%= request.getContextPath() %>/rs/signature/certificacion/'+ garantia +'/'+ tramite +'/51071/35'

				/*
				var ruta = '<%= request.getContextPath() %>/home/certificaTramite.do?idGarantia=' + garantia + '&idTramite=' + tramite;
				*/
				
				var ruta = '<%= request.getContextPath() %>/home/certificaTramite.do?idGarantia=' + garantia + '&idTramite=' + tramite;
				console.log("Ruta" + ruta)
				const a = document.createElement('a');
				a.style.display = 'none';
				a.href = ruta;
				var evt = document.createEvent('MouseEvents');
				evt.initMouseEvent('click', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, null);

				// obtener el costo de una certificacion: tipo_tramite=5
				$.ajax({
					url: '<%= request.getContextPath() %>/rs/tipos-tramite/35',
					success: function (result) {
						MaterialDialog.dialog(
							"El costo de una Certificación es de Q. " + (Math.round(result.precio * 100) / 100).toFixed(2) + ", ¿Esta seguro que desea continuar?",
							{
								title: '<table><tr><td width="10%"><i class="medium icon-green material-icons">check_circle</i></td><td style="vertical-align: middle; text-align:left;">Confirmar</td></tr></table>', // Modal title
								buttons: {
									// Use by default close and confirm buttons
									close: {
										className: "red",
										text: "cancelar"
									},
									confirm: {
										className: "indigo",
										text: "aceptar",
										modalClose: true,
										callback: function () {
											/*$.ajax({
												url: apiSignature,
												type: "GET",
												success: function(resp){
													console.log("Respuesta files ",resp)
												}

											})*/
											MessageConfirm();
											a.download = true;
											a.dispatchEvent(evt);

											//a.click();
											//window.open(ruta, "_blank");
											//window.open(ruta);
										}
									}
								}
							}
						);

					}

				});
				// }
			}

			function MessageConfirm() {
				Swal.fire({
					icon: 'warning',
					title: 'Descarga',
					text: 'Espere mientras se muestra la ventana de descarga',
					allowOutsideClick: false,
					showClass: {
						popup: 'animate__animated animate__fadeInDown'
					},
					hideClass: {
						popup: 'animate__animated animate__fadeOutUp'
					}
				})
			}

			function checkUser(user) {
				if (user == 51071) {
					datos = true;
				}
			}




		</script>