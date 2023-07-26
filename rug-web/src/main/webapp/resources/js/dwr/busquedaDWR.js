function busquedaDwr(ruta, idPersona, tipoBusqueda, tipoTramite,consulta_nombre="", consulta_id="") {
		var idGarantia = trim(getObject('idGarantia').value); // numero de inscripcion de  garantia form 2
		var nombre = trim(getObject('nombreOtorgante').value); // nombre o razon social form 2
		var folioMercantil = '';
		var descGarantia = '';
		var rfcOtorgante = trim(getObject('rfcOtorgante').value); // nit Deudor garante form 1
		var curpOtorgante = trim(getObject('curpOtorgante').value); //DPI o Pasaporte form 1
		var noSerial = trim(getObject('serial').value); // Numero de Serie form 1

		
		// console.log(" Datos formulario 1: Garantia ", idGarantia , " Nombre ", nombre, " Folio	", folioMercantil, " desGarantia ", descGarantia, " RtOtorgante ", rfcOtorgante, " CurpOtorgante ", curpOtorgante, "Serial ", noSerial);
		if (tipoBusqueda == 1) {
			idGarantia = '';
			nombre = '';
			BusquedaDwrAction.buscar(idPersona, noSerial, idGarantia, nombre, folioMercantil, descGarantia, curpOtorgante, rfcOtorgante, ruta, tipoTramite, escribeTablaBusqueda);
		} 
		if (tipoBusqueda == 2) {
			rfcOtorgante = '';
			curpOtorgante = '';
			noSerial = '';
			BusquedaDwrAction.buscar(idPersona, noSerial, idGarantia, nombre, folioMercantil, descGarantia, curpOtorgante, rfcOtorgante, ruta, tipoTramite, escribeTablaBusqueda);
		}

        if (tipoBusqueda == 4) {
			idGarantia = '';
			nombre = '';
			//BusquedaDwrAction.buscarSinSaldo(idPersona, noSerial, idGarantia, nombre, folioMercantil, descGarantia, curpOtorgante, rfcOtorgante, ruta, tipoTramite, escribeTablaBusqueda,consulta_nombre,consulta_id);
			BusquedaDwrAction.buscarSinSaldo(idPersona, noSerial, idGarantia, nombre, folioMercantil, descGarantia, curpOtorgante, rfcOtorgante, ruta, tipoTramite, consulta_nombre,consulta_id,escribeTablaBusqueda);
		} 

		if (tipoBusqueda == 5) {
                        rfcOtorgante = '';
			curpOtorgante = '';
			noSerial = '';
			//BusquedaDwrAction.buscarSinSaldo(idPersona, noSerial, idGarantia, nombre, folioMercantil, descGarantia, curpOtorgante, rfcOtorgante, ruta, tipoTramite, escribeTablaBusqueda,consulta_nombre,consulta_id);
			BusquedaDwrAction.buscarSinSaldo(idPersona, noSerial, idGarantia, nombre, folioMercantil, descGarantia, curpOtorgante, rfcOtorgante, ruta, tipoTramite, consulta_nombre,consulta_id,escribeTablaBusqueda);
		} 
                

			
		// if (!isBlank(idGarantia) || !isBlank(nombre) || !isBlank(folioMercantil) || !isBlank(noSerial) || !isBlank(curpOtorgante) || !isBlank(rfcOtorgante)) {
		// 	console.log(idPersona, noSerial, idGarantia, nombre, folioMercantil, descGarantia, curpOtorgante, rfcOtorgante, ruta, tipoTramite, escribeTablaBusqueda);
		// 	BusquedaDwrAction.buscar(idPersona, noSerial, idGarantia, nombre, folioMercantil, descGarantia, curpOtorgante, rfcOtorgante, ruta, tipoTramite, escribeTablaBusqueda);
		// } else {
		// 	alert("Falta criterio de busqueda.");
		// }
}

function searchInvoiceform2(ruta, idPersona, tipoBusqueda, tipoTramite,consulta_nombre="", consulta_id="") {
	// search data invoice

	var invoice = trim(getObject('invoice').value);
	var set = trim(getObject('set').value);
        var nit = trim(getObject('nit').value);

	if (!isBlank(invoice) || !isBlank(set) || !isBlank(nit) ) {
		BusquedaDwrAction.searchInvoice2(invoice, set, idPersona, tipoTramite, ruta,nit,consulta_nombre,consulta_id,escribeTablaBusqueda);
	}
       
}

function searchInvoiceform(ruta, idPersona, tipoBusqueda, tipoTramite) {
	// search data invoice

	var invoice = trim(getObject('invoice').value);
	var set = trim(getObject('set').value);
        var nit = trim(getObject('nit').value);

	if (!isBlank(invoice) || !isBlank(set) || !isBlank(nit) ) {
		BusquedaDwrAction.searchInvoice(invoice, set, idPersona, tipoTramite, ruta,nit,escribeTablaBusqueda);
	}
}

function clearData(tipoBusqueda) {
	if (tipoBusqueda == 3) {
		invoice = '';
		set = '';
	}
}

function certificacionDwr(ruta) {

	var idGarantia = trim(getObject('idGarantia').value);

	if (!isBlank(idGarantia)) {
		BusquedaDwrAction.tramites(idGarantia, ruta, escribeTablaBusqueda);
	} else {
		alert("Falta criterio de busqueda.");
	}

}

function pagBusquedaSinCostoDwr(ruta, registroTotales, pagActiva, regPagina) {
	displayLoader(true);
	var idGarantia = trim(getObject('idGarantia').value);
	var nombre = trim(getObject('nombreOtorgante').value);
	//var folioMercantil = trim(getObject('folioMercantil').value);
	var folioMercantil = '';
	var descGarantia = '';
	//var curpOtorgante = trim(getObject('curpOtorgante').value);
	var rfcOtorgante = trim(getObject('rfcOtorgante').value);
	var curpOtorgante = trim(getObject('curpOtorgante').value);
	var noSerial = trim(getObject('serial').value);

	if (!isBlank(idGarantia) || !isBlank(nombre) || !isBlank(folioMercantil) || !isBlank(noSerial) || !isBlank(curpOtorgante) || !isBlank(rfcOtorgante)) {
		BusquedaDwrAction.pagBuscarSinCosto(idGarantia, nombre, folioMercantil, noSerial, curpOtorgante, rfcOtorgante, ruta, registroTotales, pagActiva, '20', escribeTablaBusqueda);
	} else {
		alert("Falta criterio de búsqueda.");
		//displayLoader(false);
	}
}

function pagBusquedaDwr(ruta, registroTotales, pagActiva, regPagina) {
	displayLoader(true);
	var idGarantia = trim(getObject('idGarantia').value);
	var nombre = trim(getObject('nombreOtorgante').value);
	//var folioMercantil = trim(getObject('folioMercantil').value);
	var folioMercantil = '';
	var descGarantia = '';
	//var curpOtorgante = trim(getObject('curpOtorgante').value);
	var rfcOtorgante = trim(getObject('rfcOtorgante').value);
	var curpOtorgante = trim(getObject('curpOtorgante').value);
	var noSerial = trim(getObject('serial').value);

	if (!isBlank(idGarantia) || !isBlank(nombre) || !isBlank(folioMercantil) || !isBlank(noSerial) || !isBlank(curpOtorgante) || !isBlank(rfcOtorgante)) {
		BusquedaDwrAction.pagBuscar(idGarantia, nombre, folioMercantil, noSerial, curpOtorgante, rfcOtorgante, ruta, registroTotales, pagActiva, '20', escribeTablaBusqueda);
	} else {
		alert("Falta criterio de búsqueda.");
		//displayLoader(false);
	}
}

function pagBusquedaSinCostoDwr(ruta, registroTotales, pagActiva, regPagina) {
	displayLoader(true);
	var idGarantia = trim(getObject('idGarantia').value);
	var nombre = trim(getObject('nombreOtorgante').value);
	//var folioMercantil = trim(getObject('folioMercantil').value);
	var folioMercantil = '';
	var descGarantia = '';
	//var curpOtorgante = trim(getObject('curpOtorgante').value);
	var rfcOtorgante = trim(getObject('rfcOtorgante').value);
	var curpOtorgante = trim(getObject('curpOtorgante').value);
	var noSerial = trim(getObject('serial').value);

	if (!isBlank(idGarantia) || !isBlank(nombre) || !isBlank(folioMercantil) || !isBlank(noSerial) || !isBlank(curpOtorgante) || !isBlank(rfcOtorgante)) {
		BusquedaDwrAction.pagBuscarSinCosto(idGarantia, nombre, folioMercantil, noSerial, curpOtorgante, rfcOtorgante, ruta, registroTotales, pagActiva, '20', escribeTablaBusqueda);
	} else {
		alert("Falta criterio de búsqueda.");
		//displayLoader(false);
	}
}


function escribeTablaBusqueda(response) {
      
	fillObject('resultadoDIV', response.message);
     setTimeout(() => {   window.scrollTo({ left: 0, top: document.body.scrollHeight, behavior: "smooth" }); }, 2000);
      
	//displayLoader(false);
}

function escribeTablaOtorgantesPrevios(response) {
	fillObject('resultadoOtorgantesPrevios', response.message);
}