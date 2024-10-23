<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<script type="text/javascript"    src="${pageContext.servletContext.contextPath}/dwr/engine.js"></script>
<script type="text/javascript"    src="${pageContext.servletContext.contextPath}/dwr/util.js"></script>
<script type="text/javascript"    src="${pageContext.servletContext.contextPath}/dwr/interface/DireccionesDwrAction.js"></script>
<script type="text/javascript"    src="${pageContext.servletContext.contextPath}/dwr/interface/ParteDwrAction.js"></script>
<script type="text/javascript"    src="${pageContext.servletContext.contextPath}/resources/js/dwr/direccionesDWR.js"></script>
<script type="text/javascript"    src="${pageContext.servletContext.contextPath}/resources/js/partesJS/parteJS.js"></script>
<script type="text/javascript" 	  src="${pageContext.servletContext.contextPath}/resources/js/campos.js"></script>
<script type="text/javascript" 	  src="${pageContext.servletContext.contextPath}/resources/js/validaciones.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/resources/js/FuncionesDeFechas.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/tooltips/tooltip.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/material-dialog.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/jquery-3.3.1.min.js"></script>
<%@taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    $(document).ready(function () {

        $('.state2').hide();

        var idPersona = <s:property value="idPersona" />;
        var idTramite = <s:property value="idTramite" />;



        cargaParteDeudor('divParteDWRxx2', idTramite, idPersona, '0', '1');
        cargaParteAcreedor('divParteDWRxx3', idTramite, idPersona, '0', '1');
        cargaParteOtorgante('divParteDWRxx4', idTramite, idPersona, '0', '1');
        cargaParteBienes('divParteDWRBienes', idTramite);



        //escondePartes();
    });

    function characterNotAllowed(evt) {
        var charText = evt.which;
        if (charText >= 32 && charText <= 47) {
            return false;
        } else if (charText >= 58 && charText <= 64) {
            return false;
        }
    }

    function add_bien() {
        var idTramite = document.getElementById("refInscripcion").value;
        var mdDescripcion = document.getElementById("mdDescripcion").value;
        var idTipo = document.getElementById("mdBienEspecial").value;
        var mdIdentificador = document.getElementById("mdIdentificador").value;
        var mdIdentificador1 = document.getElementById("mdIdentificador1").value;
        var mdIdentificador2 = document.getElementById("mdIdentificador2").value;
        var mdIdentificador3 = document.getElementById("mdIdentificador3").value;


        if (idTipo == '2') {

            if (mdIdentificador3.length > 0)
                mdDescripcion = 'Emitido por: ' + document.getElementById("mdFactura1").value + " Serie: " + mdIdentificador3 + " Fecha: " + document.getElementById("mdFactura2").value + " " + mdDescripcion;
            else
                mdDescripcion = 'Emitido por: ' + document.getElementById("mdFactura1").value + " Fecha: " + document.getElementById("mdFactura2").value + " " + mdDescripcion;
        }

        if (!noVacio(mdDescripcion)) {
            alertMaterialize('Debe ingresar la descripcion del bien especial');
            return false;
        }

        if (idTipo == '1') {
            if (!noVacio(mdIdentificador2)) {
                alertMaterialize('Debe ingresar el VIN del vehiculo');
                return false;
            }
        }
        ParteDwrAction.registrarBien_v2('divParteDWRBienes', idTramite, mdDescripcion, idTipo, mdIdentificador,
            mdIdentificador1, mdIdentificador2, mdIdentificador3, showParteBienes);
        $(document).ready(function () {
            $('#frmBien').modal('close');
        });
    }

    function cambiaBienesEspeciales() {
        var x = document.getElementById("mdBienEspecial").value;

        if (x == '1') {
            document.getElementById('showContainerData').style.display = 'block';
            document.getElementById("mdDescripcion").disabled = false;
            document.getElementById("secId1").style.display = 'block';
            document.getElementById("secId2").style.display = 'block';
            document.getElementById("secId4").style.display = 'none';
            document.getElementById("secId3").style.display = 'none';

            document.getElementById("lblMdDescripcion").innerHTML = 'Descripci&oacute;n del veh&iacute;culo';
            document.getElementById("lblMdIdentificador2").innerHTML = 'VIN';
        } else if (x == '2') {

            document.getElementById('showContainerData').style.display = 'block';
            document.getElementById("mdDescripcion").disabled = false;
            document.getElementById("secId1").style.display = 'none';
            document.getElementById("secId2").style.display = 'none';
            document.getElementById("secId3").style.display = 'none';
            document.getElementById("secId4").style.display = 'block';

            document.getElementById("lblMdIdentificador2").innerHTML = 'No. Factura';
            document.getElementById("lblMdIdentificador3").innerHTML = 'Serie'
            document.getElementById("lblMdDescripcion").innerHTML = 'Observaciones Generales';
        } else if (x == '3') {
            document.getElementById('showContainerData').style.display = 'block';
            document.getElementById("mdDescripcion").disabled = false;
            document.getElementById("secId1").style.display = 'none';
            document.getElementById("secId2").style.display = 'none';
            document.getElementById("secId3").style.display = 'none';
            document.getElementById("secId4").style.display = 'none';

            document.getElementById("lblMdIdentificador2").innerHTML = 'No. Serie';
            document.getElementById("lblMdDescripcion").innerHTML = 'Descripci&oacute;n del bien';
        } else if (x == '5') {
            document.getElementById('showContainerData').style.display = 'block';
            document.getElementById("mdDescripcion").disabled = false;
            document.getElementById("secId1").style.display = 'block';
            document.getElementById("secId2").style.display = 'block';
            document.getElementById("secId3").style.display = 'none';
            document.getElementById("secId4").style.display = 'none';

            document.getElementById("lblMdDescripcion").innerHTML = 'Descripci&oacute;n del veh&iacute;culo';
            document.getElementById("lblMdIdentificador2").innerHTML = 'VIN';
        } else if (x == '6') {
            document.getElementById('showContainerData').style.display = 'block';
            document.getElementById("mdDescripcion").disabled = false;
            document.getElementById("secId1").style.display = 'none';
            document.getElementById("secId2").style.display = 'none';
            document.getElementById("secId3").style.display = 'none';
            document.getElementById("secId4").style.display = 'none';
            document.getElementById("lblMdIdentificador2").innerHTML = 'No. Serie';
            document.getElementById("lblMdDescripcion").innerHTML = 'Descripci&oacute;n del bien';

        }


    }

    function eventModal(){
        let componente =  $('#mdBienEspecial');
        componente.val(2)
        componente.prop('disabled', true);

        clearForm()

        $('#invoiceSection').css('display', 'block');
        $('#vinSection').css('display', 'block');
        let boton =  $('#formBienButton');
        boton.html('Aceptar');
        boton.replaceWith('<a id="formBienButton" class="modal-action modal-close btn teal lighten-1" onclick="sendFormData();">Aceptar</a>');

    }

    function clearForm()
    {
        $('#nitContribuyente').val('');
        $('#mdInvoiceDate').val('');
        $('#mdDescription').val('');
        $('#mdFacturaElectronica').val('');
    }

    function loadFormDataToTable() {
        const storedData = JSON.parse(localStorage.getItem('formData')) || [];
        const tableBody = document.getElementById('dataTableBody'); // Asegúrate de tener una tabla en tu HTML con este ID

        // Limpiar la tabla antes de llenarla
        tableBody.innerHTML = '';

        // Iterar sobre los datos almacenados y crear filas en la tabla
        storedData.forEach((data, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `
            <td>${index + 1}</td>
            <td>${data.nitContribuyente}</td>
            <td>${data.mdInvoiceDate}</td>
            <td>${data.mdDescription}</td>
            <td>${data.mdFacturaElectronica}</td>
        `;
            tableBody.appendChild(row);
        });
    }

    function sendFormData() {
        const idTramite = document.getElementById("refInscripcion").value;
        const idTipo = 2;
        const nitContribuyente = document.getElementById('nitContribuyente').value;
        const mdDescripcion = 'Emitido por: ' + document.getElementById('nitContribuyente').value + ' Fecha: ' + document.getElementById('mdInvoiceDate').value + ' ' +  document.getElementById('mdDescription').value
        const mdFacturaElectronica = document.getElementById('mdFacturaElectronica').value;

        const formData = {
            nitContribuyente,
            mdInvoiceDate: document.getElementById('mdInvoiceDate').value,
            mdDescription: document.getElementById('mdDescription').value,
            mdFacturaElectronica
        };

        let storedData = JSON.parse(localStorage.getItem('formData')) || [];
        storedData.push(formData);

        localStorage.setItem('formData', JSON.stringify(storedData));

        ParteDwrAction.registrarFacturasSat(
            'divParteDWRBienes',
            idTramite,
            mdDescripcion,
            idTipo,
            mdFacturaElectronica,
            nitContribuyente,
            showParteBienes);

        clearForm();
    }

    function habilitarFacturas(facturaElectronica, nitContribuyente){
        ParteDwrAction.consumirApi(facturaElectronica, nitContribuyente, false)
    }

    function updateFEL(elementId, idTramite, tipoBien, tipoId, ident, desc, idTramiteGar){
        elementIDBien = elementId;
        let boton =  $('#formBienButton');
        let componente =  $('#mdBienEspecial');
        displayLoader(true);
        $("#mdDescripcion").value = desc;
        $('#invoiceSection').css('display', 'block');
        $('#vinSection').css('display', 'block');
        componente.val(2)
        const resultado = extraerDatos(desc);
        $('#nitContribuyente').val(resultado.emitidoPor);
        $('#mdInvoiceDate').val(resultado.fecha);
        $('#mdDescription').val(resultado.descripcion);
        $('#mdFacturaElectronica').val(ident);
        boton.html('Modificar');
        boton.replaceWith('<a id="formBienButton" class="modal-action modal-close btn teal lighten-1">Modificar</a>');
        let nuevoBoton = $('#formBienButton');
        nuevoBoton.on('click', function () {
            const mdDescripcion = 'Emitido por: ' + document.getElementById('nitContribuyente').value + ' Fecha: ' + document.getElementById('mdInvoiceDate').value + ' ' +  document.getElementById('mdDescription').value
            const idTipo = 2;
            const mdFacturaElectronica = document.getElementById('mdFacturaElectronica').value;

            ParteDwrAction.updateFacturaFEL(
                elementId,
                idTramite,
                mdDescripcion,
                idTipo,
                mdFacturaElectronica,
                idTramiteGar,
                showParteBienes);

            $('#frmBien').modal('close');


        })
        $('#frmBien').modal('open');

    }

    function eliminaParteBienFactoraje(elementId, idTramite,desc, ident, idTramiteGar) {
        elementIDBien = elementId;
        const resultado = extraerDatos(desc);
        displayLoader(true);
        ParteDwrAction.eliminaParteBienFactoraje(elementId, idTramite, idTramiteGar, resultado.emitidoPor,ident, showParteBienes);
        localStorage.removeItem('formData');
    }


    function limpiaCampos() {

        // show Data info
        document.getElementById('showContainerData').style.display = 'none';


        document.getElementById("secId1").style.display = 'none';
        document.getElementById("secId2").style.display = 'none';
        document.getElementById("secId3").style.display = 'none';
        document.getElementById("secId4").style.display = 'none';
        document.getElementById("secId5").style.display = 'block';
        document.getElementById("secId6").style.display = 'none';

        document.getElementById("mdIdentificador").value = '0';
        document.getElementById("mdIdentificador1").value = '';
        document.getElementById("mdIdentificador2").value = '';
        document.getElementById("mdIdentificador3").value = '';

        document.getElementById("mdFactura1").value = '';
        document.getElementById("mdFactura2").value = '';

        document.getElementById("mdDescripcion").value = '';
        document.getElementById("mdDescripcion").disabled = true;

        document.getElementById("mdBienEspecial").value = '0';


        Materialize.updateTextFields();
    }

    function otroRegistro() {
        var checkBox = document.getElementById("aRegistro");
        if (checkBox.checked == true) {
            document.getElementById("txtregistro").disabled = false;
            Materialize.updateTextFields();
        } else {
            document.getElementById("txtregistro").value = '';
            document.getElementById("txtregistro").disabled = true;
            Materialize.updateTextFields();
        }
    }

    function BindTable(jsondata, tableid) {
        /*Function used to convert the JSON array to Html Table*/
        var columns = BindTableHeader(jsondata, tableid); /*Gets all the column headings of Excel*/
        var idTramite = document.getElementById("refInscripcion").value;
        var mdDescripcion = '';
        var idTipo = document.getElementById("mdBienEspecial2").value;
        var mdIdentificador = '';
        var mdIdentificador1 = '';
        var mdIdentificador2 = '';
        var mdFactura1 = '';
        var mdFactura2 = '';
        var tipoId = '';
        var correcto = 0;
        var limite = 50;

        if (jsondata.length > limite.valueOf()) {
            alertMaterialize('Error!. Solo se pueden cargar ' + limite + ' registros');
            return false;
        }

        if (idTipo == '0') {
            return false;
        }

        for (var i = 0; i < jsondata.length; i++) {
            var row$ = $('<tr/>');
            mdDescripcion = '';
            numeroDeLaFactura = '';
            mdIdentificador = '';
            mdIdentificador1 = '';
            mdIdentificador2 = '';
            mdFactura1 = '';
            mdFactura2 = '';
            mdSerie = '';
            correcto = 0;
            tipoId = '';

            for (var colIndex = 0; colIndex < columns.length; colIndex++) {
                var cellValue = jsondata[i][columns[colIndex]];
                if (cellValue == null)
                    cellValue = "";
                if (idTipo == '1') {
                    if (colIndex == 0) {
                        tipoId = cellValue;
                        if (cellValue == '1') {
                            cellValue = 'Placa';
                        } else if (cellValue == '2') {
                            cellValue = 'VIN';
                        } else {
                            cellValue = 'Valor invalido';
                            correcto = 1;
                        }
                    }
                    if (colIndex == 1) {
                        if (cellValue.length > 25) {
                            cellValue = 'Valor invalido';
                            correcto = 1;
                        } else {
                            if (tipoId == '1') {
                                if (cellValue.includes("-")) {
                                    var str = cellValue.split("-");
                                    mdIdentificador = str[0];
                                    mdIdentificador1 = str[1];
                                } else {
                                    cellValue = 'Valor invalido';
                                    correcto = 1;
                                }
                            } else {
                                mdIdentificador2 = cellValue;
                            }
                        }
                    }
                    if (colIndex == 2) {
                        if (cellValue.length > 100) {
                            cellValue = 'Valor invalido';
                            correcto = 1;
                        } else {
                            mdDescripcion = cellValue;
                        }
                    }
                    if (colIndex > 2) {
                        correcto = 1;
                    }
                } else if (idTipo == '2') { //Facturas
                    if (colIndex == 0) {
                        if (cellValue.length > 25) {
                            cellValue = 'Valor invalido';
                            correcto = 1;
                        } else {
                            mdFactura1 = cellValue;
                        }
                    }
                    if (colIndex == 1) {
                        var patt = /^(0?[1-9]|[12][0-9]|3[01])[\/](0?[1-9]|1[012])[\/]\d{4}$/;
                        if (!patt.test(cellValue)) {
                            cellValue = 'Valor invalido';
                            correcto = 1;
                        } else {
                            mdFactura2 = cellValue;
                        }
                    }
                    if (colIndex == 2) {
                        if (cellValue.length > 25) {
                            cellValue = 'Valor invalido';
                            correcto = 1;
                        } else {
                            mdIdentificador2 = cellValue;
                        }
                    }
                    if (colIndex == 3) {
                        if (cellValue.length > 100) {
                            cellValue = 'Valor invalido';
                            correcto = 1;
                        } else {
                            mdDescripcion = 'Emitido por: ' + mdFactura1 + " Fecha: " + mdFactura2 + " " + cellValue;
                            numeroDeLaFactura = cellValue;
                        }
                    }
                    ///orellana: se esta colocando esto para la carga de facturas con serie
                    if (colIndex == 4) {
                        if (cellValue.length > 100) {
                            cellValue = 'Valor invalido';
                            correcto = 1;
                        } else {
                            if (cellValue.length >= 1) {
                                //mdDescripcion = 'Emitido por: ' + document.getElementById("mdFactura1").value + " Serie: " +mdIdentificador3 + " Fecha: " + document.getElementById("mdFactura2").value + " " + mdDescripcion;
                                mdDescripcion = 'Emitido por: ' + mdFactura1 + " Serie: " + cellValue + " Fecha: " + mdFactura2 + " " + numeroDeLaFactura;
                                mdSerie = cellValue;
                            }
                        }
                    }
                    //orellana: Carga de facturas
                    if (colIndex > 4) {
                        correcto = 1;
                    }
                } else if (idTipo == '3') {
                    if (colIndex == 0) {
                        if (cellValue.length > 25) {
                            cellValue = 'Valor invalido';
                            correcto = 1;
                        } else {
                            mdIdentificador2 = cellValue;
                        }
                    }
                    if (colIndex == 1) {
                        if (cellValue.length > 100) {
                            cellValue = 'Valor invalido';
                            correcto = 1;
                        } else {
                            mdDescripcion = cellValue;
                        }
                    }
                    if (colIndex > 1) {
                        correcto = 1;
                    }
                }
                row$.append($('<td/>').html(cellValue));
            }
            if (correcto == 0) {

                ParteDwrAction.registrarBien_v2('divParteDWRBienes', idTramite, mdDescripcion, idTipo, mdIdentificador,
                    mdIdentificador1, mdIdentificador2, mdSerie, showParteBienes);


                row$.append('<td><font color="green">Cargado</font></td>');
            } else {
                row$.append('<td><font color="red">Error verifique datos</font></td>');
            }
            $(tableid).append(row$);
        }
    }

    function BindTableHeader(jsondata, tableid) {
        /*Function used to get all column names from JSON and bind the html table header*/
        var columnSet = [];
        var headerTr$ = $('<tr/>');
        for (var i = 0; i < jsondata.length; i++) {
            var rowHash = jsondata[i];
            for (var key in rowHash) {
                if (rowHash.hasOwnProperty(key)) {
                    if ($.inArray(key, columnSet) == -1) {/*Adding each unique column names to a variable array*/
                        columnSet.push(key);
                        headerTr$.append($('<th/>').html(key));
                    }
                }
            }
        }
        headerTr$.append('<th>Resultado</th>');
        $(tableid).append(headerTr$);
        return columnSet;
    }

    function ExportToTable() {
        document.getElementById("exceltable").innerHTML = '<table id="exceltable"></table> ';

        var regex = /^([a-zA-Z0-9\s_\\.\-:])+(.xlsx|.xls)$/;
        /*Checks whether the file is a valid excel file*/
        if (regex.test($("#excelfile").val().toLowerCase())) {
            var xlsxflag = false; /*Flag for checking whether excel is .xls format or .xlsx format*/
            if ($("#excelfile").val().toLowerCase().indexOf(".xlsx") > 0) {
                xlsxflag = true;
            }
            /*Checks whether the browser supports HTML5*/
            if (typeof (FileReader) != "undefined") {
                var reader = new FileReader();
                reader.onload = function (e) {
                    var data = e.target.result;
                    /*Converts the excel data in to object*/
                    if (xlsxflag) {
                        var workbook = XLSX.read(data, {type: 'binary'});
                    } else {
                        var workbook = XLS.read(data, {type: 'binary'});
                    }
                    /*Gets all the sheetnames of excel in to a variable*/
                    var sheet_name_list = workbook.SheetNames;

                    var cnt = 0; /*This is used for restricting the script to consider only first sheet of excel*/
                    sheet_name_list.forEach(function (y) { /*Iterate through all sheets*/
                        /*Convert the cell value to Json*/
                        if (xlsxflag) {
                            var exceljson = XLSX.utils.sheet_to_json(workbook.Sheets[y]);
                        } else {
                            var exceljson = XLS.utils.sheet_to_row_object_array(workbook.Sheets[y]);
                        }
                        if (exceljson.length > 0 && cnt == 0) {
                            BindTable(exceljson, '#exceltable');
                            cnt++;
                        }
                    });
                    $('#exceltable').show();
                }
                if (xlsxflag) {/*If excel file is .xlsx extension than creates a Array Buffer from excel*/
                    reader.readAsArrayBuffer($("#excelfile")[0].files[0]);
                } else {
                    reader.readAsBinaryString($("#excelfile")[0].files[0]);
                }
            } else {
                alertMaterialize("Error! Su explorador no soporta HTML5!");
            }
        } else {
            alertMaterialize("Por favor seleccione un archivo de Excel valido!");
        }
    }

    function cambiaBienesEspecialesFile() {
        var x = document.getElementById("mdBienEspecial2").value;
        if (x == '1') {
            document.getElementById("txtspan").innerHTML = 'Los campos del excel son: '
                + '<p><b>Tipo Identificador</b>, 1 si es Placa y 2 si es VIN<p>'
                + '<p><b>Identificador</b>, maximo 25 caracteres</p>'
                + '<p><b>Descripcion</b>, maximo 100 caracteres</p>';

        } else if (x == '2') {
            document.getElementById("txtspan").innerHTML = 'Los campos del excel son: '
                + '<p><b>Numero Identificacion Contribuyente</b>, maximo 25 caracteres</p>'
                + '<p><b>Fecha</b>, formato texto DD/MM/YYYY</p>'
                + '<p><b>Numero Factura</b>, maximo 25 caracteres</p>'
                + '<p><b>Descripcion</b>, maximo 100 caracteres</p>'
                + '<p><b>Serie</b>maximo 100 caracteres</p>';
        } else if (x == '3') {
            document.getElementById("txtspan").innerHTML = 'Los campos del excel son: '
                + '<p><b>Identificador</b>, maximo 25 caracteres</p>'
                + '<p><b>Descripcion</b>, maximo 100 caracteres</p>';
        }


    }

    function limpiaCamposFile() {
        document.getElementById("mdBienEspecial2").value = '0';
        document.getElementById("txtspan").innerHTML = '';

        document.getElementById("exceltable").innerHTML = '<table id="exceltable"></table> ';
        var input = $("#excelfile");
        var name = $("#namefile");
        input.replaceWith(input.val('').clone(true));
        name.replaceWith(name.val('').clone(true));

        Materialize.updateTextFields();
    }

    function mi_funcion_editar() {
        $('.state2').hide();
        $('.state1').show();
        $('.tituloHeader2').show();
        $("a[title='Eliminar']").show();
        $("a[title='Modificar']").show();
        $(".btn.waves-effect.indigo.darken-4").show();
        $(".btn.waves-effect.red.darken-4").show();

        $("#instrumento").prop('disabled', false);
        $("#modotrosgarantia").prop('disabled', false);
        $("#tiposbienes").prop('disabled', false);


    }

    function habilitar_continuar() {
        alertMaterialize("Revisar datos antes de Aceptar datos y grabar garantia");
        $('.state2').show();
        $('.state1').hide();
        $('.tituloHeader2').hide();
        $("a[title='Eliminar']").hide();
        $("a[title='Modificar']").hide();
        $(".btn.waves-effect.indigo.darken-4").hide();
        $(".btn.waves-effect.red.darken-4").hide();
        $("#instrumento").prop('disabled', true);
        $("#modotrosgarantia").prop('disabled', true);
        $("#tiposbienes").prop('disabled', true);
    }

    function habilitar_todo() {
        //alert('Revise todos los datos ingresados antes de continuar');
        document.getElementById("bFirmar").disabled = true;
        document.getElementById("divParteDWRxx2").disabled = false;
        document.getElementById("divParteDWRxx3").disabled = false;
        document.getElementById("divParteDWRxx4").disabled = false;
        document.getElementById("divParteDWRBienes").disabled = false;
        document.getElementById("modotrosgarantia").disabled = false;
    }

    function confirmar() {
        alert('Revise todos los datos ingresados antes de continuar');

        $('.tituloHeader2').hide();
        $('.btn').hide();
        $('.btn').hide();
        $('.btn').hide();
    }

    function registrar_factoraje() {
        mi_funcion_editar();
        inscripcionFactoraje();
    }


</script>

<script type="text/javascript">

function anterior(){
		document.location="<%=request.getContextPath() %>/home/anterior.do";
}

function siguiente(){
		document.location="<%=request.getContextPath() %>/home/siguiente.do";
}
function cancelacion(){
	// obtener el costo de una cancelacion: tipo_tramite=4
	$.ajax({
		url: '<%= request.getContextPath() %>/rs/tipos-tramite/4',
		success: function(result) {
			MaterialDialog.dialog(
				"El costo de una " + "Cancelaci�n" + " es de Q. " + (Math.round(result.precio * 100) / 100).toFixed(2) + ", �est� seguro que desea continuar?",
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
								window.location.href ='<%= request.getContextPath() %>/home/cancelacionTramite.do';
							}
						}
					}
				}
			);
		}
	});
}

function certificacion() {
	// obtener el costo de una certificacion: tipo_tramite=5
	$.ajax({
		url: '<%= request.getContextPath() %>/rs/tipos-tramite/5',
		success: function(result) {
		MaterialDialog.dialog(
			"El costo de una " + result.descripcion + " es de Q. " + (Math.round(result.precio * 100) / 100).toFixed(2) + ", �est� seguro que desea continuar?",
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
							return true;
						}
					}
				}
			}
		);
		}
	});
}

function buscaPunto(texto){
	for(i=0;i<texto.length;i++){
		if(texto.charAt(i)==".") return true;
	}
	return false;
}

function IsNumber(evt) {
var key = (document.all) ? evt.keyCode : evt.which;
var cadena = document.getElementById('modmonto').value;
var onlyPunto = buscaPunto(cadena);    
if (onlyPunto){    	
	return (key <= 13 || (key >= 48 && key <= 57));
}else{
	return ( key <= 13 || (key >= 48 && key <= 57) || key==46);    	 
}    
}
	function sendFormM(){
		document.getElementById("bFirmar").value = "Enviando";
		document.getElementById("bFirmar").disabled = true;
		getObject('faformulario').submit();
	}
	
	function sendFormF(){
		document.getElementById("bFirmar").value = "Enviando";
		document.getElementById("bFirmar").disabled = true;
		getObject('fafactoraje').submit();
	}
	
	function sendForm(){
		document.getElementById("bFirmar").value = "Enviando";
		document.getElementById("bFirmar").disabled = true;
		getObject('famodificacion').submit();
	}

	function isNullOrEmpty(valor){
		if ( valor == null )
			true;
		for ( i = 0; i < valor.length; i++ )
				if ( valor.charAt(i) != " " )
					return false; 
		return true;
	}

	function verificarBienes(_data,_valor){
		if(_data.indexOf(_valor) > -1){
			return true
		}else{
			return false
		}
	}

	function validarSinAutoridad(idTramite, idGarantia){

		var descripcion_flag = document.getElementById('tiposbienes').value
		//var tipoGarantia = document.getElementById('modtipogarantia').value;
		var tipoGarantia = 0;
		var dia = new Date();
		var bandera = true;
        const resultFacturas = 0;

		if(document.getElementById('tableDeudores')==null){
			alertMaterialize('No se puede continuar sin un Deudor');
			return false;
		}
		if(document.getElementById('tableAcreedores')==null){
			alertMaterialize('No se puede continuar sin un Acreedor');
			return false;
		}

        const api = '<%= request.getContextPath() %>/rs/garantias-pend/cantidad-facturas/' + idTramite
		path = '<%= request.getContextPath() %>/rs/garantias-pend/verificacion/' + idTramite
        const urlApi = '<%= request.getContextPath() %>/rs/garantias/verificar-tipo/' + idGarantia

        $.ajax({
            type: 'GET',
            url: urlApi,
            contentType: 'application/json',
            async: true,
            success: function(data){
                $.ajax({
                    url: path,
                    success: function(response){
                        if(data[0] == 16){
                            idtipoTramite = 44
                        } else if(data[0] == 2){
                            idtipoTramite = 36
                            axios.get(api).then(response => {
                                this.resultFacturas = response.data[0]
                                const costos = '<%= request.getContextPath() %>/rs/tipos-tramite/' + idtipoTramite
                                axios.get(costos).then(responseTwo => {
                                    MaterialDialog.dialog(
                                        "El costo de una " + "modificación" + " es de Q. " + Math.round(((responseTwo.data.precio * 100) / 100) * this.resultFacturas).toFixed(2) + ", esta seguro que desea continuar?",
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
                        } else if(data[0] == 3){

                            idtipoTramite = 47
                            axios.get(api).then(response => {
                                this.resultFacturas = response.data[0]
                                const costos = '<%= request.getContextPath() %>/rs/tipos-tramite/' + idtipoTramite
                                axios.get(costos).then(responseTwo => {
                                    MaterialDialog.dialog(
                                        "El costo de una " + "modificación" + " es de Q. " + Math.round((responseTwo.data.precio * 100) / 100).toFixed(2) + ", esta seguro que desea continuar?",
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
                        } else if(data[0] == 4){
                            idtipoTramite = 48
                            axios.get(api).then(response => {
                                this.resultFacturas = response.data[0]
                                const costos = '<%= request.getContextPath() %>/rs/tipos-tramite/' + idtipoTramite
                                axios.get(costos).then(responseTwo => {
                                    MaterialDialog.dialog(
                                        "El costo de una " + "modificación" + " es de Q. " + Math.round((responseTwo.data.precio * 100) / 100).toFixed(2) + ", esta seguro que desea continuar?",
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
                        else{
                            idArancel = response
                            if (verificarBienes(idArancel,1) || verificarBienes(idArancel,2) || verificarBienes(idArancel,3) ){
                                idtipoTramite = 40
                            }else if(verificarBienes(idArancel,6)){
                                idtipoTramite = 39
                            }else if(verificarBienes(idArancel,5)){
                                idtipoTramite = 41
                            }else{
                                if(descripcion_flag.length > 0){
                                    idtipoTramite = 40
                                }else{
                                    idtipoTramite = 40
                                }
                            }
                        }

                        $.ajax({
                            url: '<%= request.getContextPath() %>/rs/tipos-tramite/' + idtipoTramite,
                            success: function(result) {
                                MaterialDialog.dialog(
                                    "El costo de una " + "modificación" + " es de Q. " + (Math.round(result.precio * 100) / 100).toFixed(2) + ", esta seguro que desea continuar?",
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
        })
		
		/*$.ajax({
			url: '<%= request.getContextPath() %>/rs/tipos-tramite/7',
			success: function(result) {
				MaterialDialog.dialog(
					"El costo de una " + "modificación" + " es de Q. " + (Math.round(result.precio * 100) / 100).toFixed(2) + ", �est� seguro que desea continuar?",
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
		*/
	}
	
	function inscripcionFormulario() {
		var tipoGarantia = 0;
		var dia = new Date();
		var bandera = true;
		
		if(document.getElementById('tableDeudores')==null){
			MaterialDialog.alert(
					'No se puede continuar sin un Deudor.', // Alert Body (Acepts html tags)
					{
						title:'<table><tr><td width="10%"><i class="medium icon-yellow material-icons">warning</i></td><td style="vertical-align: middle; text-align:left;">Alerta</td></tr></table>', // Modal title
						buttons:{ // Receive buttons (Alert only use close buttons)
							close:{
								text:'cerrar', //Text of close button
								className:'red' // Class of the close button								
							}
						}
					}
				);		
			return false;
		}
		if(document.getElementById('tableAcreedores')==null){			
			MaterialDialog.alert(
					'No se puede continuar sin un Acreedor.', // Alert Body (Acepts html tags)
					{
						title:'<table><tr><td width="10%"><i class="medium icon-yellow material-icons">warning</i></td><td style="vertical-align: middle; text-align:left;">Alerta</td></tr></table>', // Modal title
						buttons:{ // Receive buttons (Alert only use close buttons)
							close:{
								text:'cerrar', //Text of close button
								className:'red' // Class of the close button								
							}
						}
					}
				);		
			return false;
		}
		if(document.getElementById('txtregistro').value==''){			
			MaterialDialog.alert(
					'Debe ingresar la informacion del registro de la prenda', // Alert Body (Acepts html tags)
					{
						title:'<table><tr><td width="10%"><i class="medium icon-yellow material-icons">warning</i></td><td style="vertical-align: middle; text-align:left;">Alerta</td></tr></table>', // Modal title
						buttons:{ // Receive buttons (Alert only use close buttons)
							close:{
								text:'cerrar', //Text of close button
								className:'red' // Class of the close button								
							}
						}
					}
				);		
			return false;
		}
		
		MaterialDialog.dialog(
			"Esta seguro que la informacion ingresada es correcta; el costo del traslado es de Q 300.00, desea continuar?",
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
							sendFormM();
						}
					}
				}
			}
		);
				
	}
	
	function inscripcionFactoraje() {
		var tipoGarantia = document.getElementById('reftipogarantia').value;
		var dia = new Date();
		var bandera = true;		
		var rows = 0; 
		var costo = 0;

		if(tipoGarantia==2) {						
			if(document.getElementById('bienes')==null){			
				MaterialDialog.alert(
						'No se puede continuar sin una factura ingresada.', // Alert Body (Acepts html tags)
						{
							title:'<table><tr><td width="10%"><i class="medium icon-yellow material-icons">warning</i></td><td style="vertical-align: middle; text-align:left;">Alerta</td></tr></table>', // Modal title
							buttons:{ // Receive buttons (Alert only use close buttons)
								close:{
									text:'cerrar', //Text of close button
									className:'red' // Class of the close button								
								}
							}
						}
					);		
				return false;
			}
			rows = document.getElementById('bienes').getElementsByTagName('tr').length;
			costo = 5 * (rows-1);
		}else if(tipoGarantia == 3){
            costo = 100;
        }else if(tipoGarantia == 4){
            costo = 100;
        }
        else {
			costo = 300;
		}
		
		if(document.getElementById('tableAcreedores')==null){			
			MaterialDialog.alert(
					'No se puede continuar sin un Factor o Cesionario.', // Alert Body (Acepts html tags)
					{
						title:'<table><tr><td width="10%"><i class="medium icon-yellow material-icons">warning</i></td><td style="vertical-align: middle; text-align:left;">Alerta</td></tr></table>', // Modal title
						buttons:{ // Receive buttons (Alert only use close buttons)
							close:{
								text:'cerrar', //Text of close button
								className:'red' // Class of the close button								
							}
						}
					}
				);		
			return false;
		}		
		if(document.getElementById('tableDeudores')==null){
			MaterialDialog.alert(
					'No se puede continuar sin un Cedente.', // Alert Body (Acepts html tags)
					{
						title:'<table><tr><td width="10%"><i class="medium icon-yellow material-icons">warning</i></td><td style="vertical-align: middle; text-align:left;">Alerta</td></tr></table>', // Modal title
						buttons:{ // Receive buttons (Alert only use close buttons)
							close:{
								text:'cerrar', //Text of close button
								className:'red' // Class of the close button								
							}
						}
					}
				);		
			return false;
		}
		
		if(tipoGarantia!=4) {
			if(document.getElementById('tableOtorgantes')==null){			
				MaterialDialog.alert(
						'No se puede continuar sin un Deudor Cedido.', // Alert Body (Acepts html tags)
						{
							title:'<table><tr><td width="10%"><i class="medium icon-yellow material-icons">warning</i></td><td style="vertical-align: middle; text-align:left;">Alerta</td></tr></table>', // Modal title
							buttons:{ // Receive buttons (Alert only use close buttons)
								close:{
									text:'cerrar', //Text of close button
									className:'red' // Class of the close button								
								}
							}
						}
					);		
				return false;
			}	
		}
		
				
				
		MaterialDialog.dialog(
			"Esta seguro que la informacion ingresada es correcta; el costo de la inscripcion es de Q " + costo + ".00, desea continuar?",
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
							sendFormF();
						}
					}
				}
			}
		);
				
	}

	function validarAutoridad(){
		// validando campos obligatorios con autoridad
		var tipoGarantia = document.getElementById('modtipogarantia').value;
		var dia = new Date();
		var bandera = true;
		
		if(tipoGarantia == 10 || tipoGarantia == 12){
			if (isNullOrEmpty(document.getElementById("modtipoacto").value)
					|| isNullOrEmpty(document.getElementById("autoridad").value)
						|| isNullOrEmpty(document.getElementById("modtipobien").value)
							|| isNullOrEmpty(document.getElementById("tiposbienes").value)) {
				alert('Los campos marcados con * son obligatorios.');
				bandera = false;
			}else{
				// si fecha 1 es null
				if(isNullOrEmpty(document.getElementById("datepicker1").value)){
					alert('Los campos marcados con * son obligatorios.');
					bandera = false;
				}
			}
		}else{
			// si no es ni retencion ni privilegios debe exigir el campo del acto
			// contrato que obliga.
			if (isNullOrEmpty(document.getElementById("modtipoacto").value)
					|| isNullOrEmpty(document.getElementById("autoridad").value)
						|| isNullOrEmpty(document.getElementById("tipoContOb").value)
							|| isNullOrEmpty(document.getElementById("modtipobien").value)
								|| isNullOrEmpty(document.getElementById("tiposbienes").value)) {
				alert('Los campos marcados con * son obligatorios.');
				bandera = false;
			}else{
				// si fecha 1 es null
				if(isNullOrEmpty(document.getElementById("datepicker1").value)){
					alert('Los campos marcados con * son obligatorios.');
					bandera = false;
				}
			}
		}

		if(bandera){			     
			sendForm();
		}			
		
	}

	todos = new Array();
	function marcar(s) {	
		cual=s.selectedIndex;
		for(y=0;y<s.options.length;y++){
		if(y==cual){
			s.options[y].selected=(todos[y]==true)?false:true;
			todos[y]=(todos[y]==true)?false:true;
		}else{
		s.options[y].selected=todos[y];
			}
		}
	} 

	function escondePartes(){
		var valor = document.getElementById('modtipogarantia').value;
		var bfecha1 = true;
		var bfecha2 = false;
		var bfecha3 = false;

		switch(valor){
			case "1":
				/*
				fechadato= "* Fecha de celebraci�n del acto o contrato"				
				document.getElementById('terIDcond').innerHTML = '<span class="textoGeneralRojo"> T�rminos y Condiciones del Acto o Contrato de la Garant�a Mobiliaria que desee incluir'; */			
				displayObject('fecha1',true);
				displayObject('fecha2',false);	
				displayObject('fecha3',false);				
				displayObject('terminos1',true);
				displayObject('terminos2',false);
				displayObject('terminos3',false);
				//document.getElementById("titulocopia").style.display="block";
				//document.getElementById("copia").style.display="block";  
				document.getElementById("titulo").style.display="block";  
				document.getElementById("tittipoacto").style.display="block"; 
				document.getElementById("tipoContOb").style.display="block";
				document.getElementById("titdate2").style.display="block"; 
				document.getElementById("datepicker4").style.display="block";
				document.getElementById("titdate3").style.display="block"; 
				document.getElementById("datepicker5").style.display="block";
				document.getElementById("titterminos").style.display="block"; 
				document.getElementById("farectificacion_modotrosterminos").style.display="block";
				displayObject('_modotrosgarantia',true);
				displayObject('_noEditablemodotrosgarantia',false);
				break;
			case "2":
				/*
				document.getElementById('fechaCeleb').innerHTML = '<span class="textoGeneralRojo"> * Fecha de celebraci�n del acto o contrato :</span>';
				document.getElementById('terIDcond').innerHTML = '<span class="textoGeneralRojo"> T�rminos y Condiciones del Acto o Contrato de la Garant�a Mobiliaria que desee incluir';*/
				displayObject('fecha1', true);
				displayObject('fecha2',false);
				displayObject('fecha3',false);
				displayObject('terminos1',true);
				displayObject('terminos2',false);
				displayObject('terminos3',false);
				//document.getElementById("titulocopia").style.display="block";
				//document.getElementById("copia").style.display="block";  
				document.getElementById("titulo").style.display="block";  
				document.getElementById("tittipoacto").style.display="block"; 
				document.getElementById("tipoContOb").style.display="block";
				document.getElementById("titdate2").style.display="block"; 
				document.getElementById("datepicker4").style.display="block";
				document.getElementById("titdate3").style.display="block"; 
				document.getElementById("datepicker5").style.display="block";
				document.getElementById("titterminos").style.display="block"; 
				document.getElementById("farectificacion_modotrosterminos").style.display="block";
				displayObject('_modotrosgarantia',true);
				displayObject('_noEditablemodotrosgarantia',false);
				break;
			case "3":
				/*
				document.getElementById('fechaCeleb').innerHTML = '<span class="textoGeneralRojo"> * Fecha de celebraci�n del acto o contrato :</span>';
				document.getElementById('terIDcond').innerHTML = '<span class="textoGeneralRojo"> T�rminos y Condiciones del Acto o Contrato de la Garant�a Mobiliaria que desee incluir';*/
				displayObject('fecha1',true);
				displayObject('fecha2',false);
				displayObject('fecha3',false);
				displayObject('terminos1',true);
				displayObject('terminos2',false);
				displayObject('terminos3',false);
				//document.getElementById("titulocopia").style.display="block";
				//document.getElementById("copia").style.display="block";  
				document.getElementById("titulo").style.display="block";  
				document.getElementById("tittipoacto").style.display="block"; 
				document.getElementById("tipoContOb").style.display="block";
				document.getElementById("titdate2").style.display="block"; 
				document.getElementById("datepicker4").style.display="block";
				document.getElementById("titdate3").style.display="block"; 
				document.getElementById("datepicker5").style.display="block";
				document.getElementById("titterminos").style.display="block"; 
				document.getElementById("farectificacion_modotrosterminos").style.display="block";
				displayObject('_modotrosgarantia',true);
				displayObject('_noEditablemodotrosgarantia',false);
				break;
			case "4":
				/*
				document.getElementById('fechaCeleb').innerHTML = '<span class="textoGeneralRojo"> * Fecha de celebraci�n del acto o contrato :</span>';
				document.getElementById('terIDcond').innerHTML = '<span class="textoGeneralRojo"> T�rminos y Condiciones del Acto o Contrato de la Garant�a Mobiliaria que desee incluir';*/
				displayObject('fecha1',true);
				displayObject('fecha2',false);
				displayObject('fecha3',false);
				displayObject('terminos1',true);
				displayObject('terminos2',false);
				displayObject('terminos3',false);
				//document.getElementById("titulocopia").style.display="block";
				//document.getElementById("copia").style.display="block";  
				document.getElementById("titulo").style.display="block";  
				document.getElementById("tittipoacto").style.display="block"; 
				document.getElementById("tipoContOb").style.display="block";
				document.getElementById("titdate2").style.display="block"; 
				document.getElementById("datepicker4").style.display="block";
				document.getElementById("titdate3").style.display="block"; 
				document.getElementById("datepicker5").style.display="block";
				document.getElementById("titterminos").style.display="block"; 
				document.getElementById("farectificacion_modotrosterminos").style.display="block";
				displayObject('_modotrosgarantia',true);
				displayObject('_noEditablemodotrosgarantia',false);
				break;
			case "5":
				/*
				document.getElementById('fechaCeleb').innerHTML = '<span class="textoGeneralRojo"> * Fecha de celebraci�n del acto o contrato :</span>';
				document.getElementById('terIDcond').innerHTML = '<span class="textoGeneralRojo"> T�rminos y Condiciones del Acto o Contrato de la Garant�a Mobiliaria que desee incluir';*/
				displayObject('fecha1',true);
				displayObject('fecha2',false);
				displayObject('fecha3',false);
				displayObject('terminos1',true);
				displayObject('terminos2',false);
				displayObject('terminos3',false);
				//document.getElementById("titulocopia").style.display="block";
				//document.getElementById("copia").style.display="block";  
				document.getElementById("titulo").style.display="block";  
				document.getElementById("tittipoacto").style.display="block"; 
				document.getElementById("tipoContOb").style.display="block";
				document.getElementById("titdate2").style.display="block"; 
				document.getElementById("datepicker4").style.display="block";
				document.getElementById("titdate3").style.display="block"; 
				document.getElementById("datepicker5").style.display="block";
				document.getElementById("titterminos").style.display="block"; 
				document.getElementById("farectificacion_modotrosterminos").style.display="block";
				displayObject('_modotrosgarantia',true);
				displayObject('_noEditablemodotrosgarantia',false);
				break;
			case "6":
				/*
				document.getElementById('fechaCeleb').innerHTML = '<span class="textoGeneralRojo"> * Fecha de celebraci�n del acto o contrato :</span>';
				document.getElementById('terIDcond').innerHTML = '<span class="textoGeneralRojo"> T�rminos y Condiciones del Acto o Contrato de la Garant�a Mobiliaria que desee incluir';*/
				displayObject('fecha1',true);
				displayObject('fecha2',false);
				displayObject('fecha3',false);
				displayObject('terminos1',true);
				displayObject('terminos2',false);
				displayObject('terminos3',false);
				//document.getElementById("titulocopia").style.display="block";
				//document.getElementById("copia").style.display="block";  
				document.getElementById("titulo").style.display="block";  
				document.getElementById("tittipoacto").style.display="block"; 
				document.getElementById("tipoContOb").style.display="block";
				document.getElementById("titdate2").style.display="block"; 
				document.getElementById("datepicker4").style.display="block";
				document.getElementById("titdate3").style.display="block"; 
				document.getElementById("datepicker5").style.display="block";
				document.getElementById("titterminos").style.display="block"; 
				document.getElementById("farectificacion_modotrosterminos").style.display="block";
				displayObject('_modotrosgarantia',true);
				displayObject('_noEditablemodotrosgarantia',false);
				break;
			case "7":
				/*
				document.getElementById('fechaCeleb').innerHTML = '<span class="textoGeneralRojo"> * Fecha de celebraci�n del acto o contrato :</span>';
				document.getElementById('terIDcond').innerHTML = '<span class="textoGeneralRojo"> T�rminos y Condiciones del Acto o Contrato de la Garant�a Mobiliaria que desee incluir';*/
				displayObject('fecha1',true);
				displayObject('fecha2',false);
				displayObject('fecha3',false);
				displayObject('terminos1',true);
				displayObject('terminos2',false);
				displayObject('terminos3',false);
				//document.getElementById("titulocopia").style.display="block";
				//document.getElementById("copia").style.display="block";  
				document.getElementById("titulo").style.display="block";  
				document.getElementById("tittipoacto").style.display="block"; 
				document.getElementById("tipoContOb").style.display="block";
				document.getElementById("titdate2").style.display="block"; 
				document.getElementById("datepicker4").style.display="block";
				document.getElementById("titdate3").style.display="block"; 
				document.getElementById("datepicker5").style.display="block";
				document.getElementById("titterminos").style.display="block"; 
				document.getElementById("farectificacion_modotrosterminos").style.display="block";
				displayObject('_modotrosgarantia',true);
				displayObject('_noEditablemodotrosgarantia',false);
				break;
			case "8":
				/*
				document.getElementById('fechaCeleb').innerHTML = '<span class="textoGeneralRojo"> * Fecha de surgimiento del Derecho de Retenci�n :</span>';
				document.getElementById('terIDcond').innerHTML = '<span class="textoGeneralRojo"> T�rminos y Condiciones del Acto o Contrato de la Garant�a Mobiliaria que desee incluir';*/
				displayObject('fecha1',true);
				displayObject('fecha2',false);
				displayObject('fecha3',false);
				displayObject('terminos1',true);
				displayObject('terminos2',false);
				displayObject('terminos3',false);
				//document.getElementById("titulocopia").style.display="block";
				//document.getElementById("copia").style.display="block";  
				document.getElementById("titulo").style.display="block";  
				document.getElementById("tittipoacto").style.display="block"; 
				document.getElementById("tipoContOb").style.display="block";
				document.getElementById("titdate2").style.display="block"; 
				document.getElementById("datepicker5").style.display="block";
				document.getElementById("titdate3").style.display="block"; 
				document.getElementById("datepicker5").style.display="block";
				document.getElementById("titterminos").style.display="block"; 
				document.getElementById("farectificacion_modotrosterminos").style.display="block";
				displayObject('_modotrosgarantia',true);
				displayObject('_noEditablemodotrosgarantia',false);
				break;
			case "9":
				/*
				document.getElementById('fechaCeleb').innerHTML = '<span class="textoGeneralRojo"> * Fecha de surgimiento del Derecho de Retenci�n :</span>';
				document.getElementById('terIDcond').innerHTML = '<span class="textoGeneralRojo"> T�rminos y Condiciones del Acto o Contrato de la Garant�a Mobiliaria que desee incluir';*/
				displayObject('fecha1',true);
				displayObject('fecha2',false);
				displayObject('fecha3',false);
				displayObject('terminos1',true);
				displayObject('terminos2',false);
				displayObject('terminos3',false);
				//document.getElementById("titulocopia").style.display="block";
				//document.getElementById("copia").style.display="block";  
				document.getElementById("titulo").style.display="block";  
				document.getElementById("tittipoacto").style.display="block"; 
				document.getElementById("tipoContOb").style.display="block";
				document.getElementById("titdate2").style.display="block"; 
				document.getElementById("datepicker4").style.display="block";
				document.getElementById("titdate3").style.display="block"; 
				document.getElementById("datepicker5").style.display="block";
				document.getElementById("titterminos").style.display="block"; 
				document.getElementById("farectificacion_modotrosterminos").style.display="block";
				displayObject('_modotrosgarantia',true);
				displayObject('_noEditablemodotrosgarantia',false);
				break;
			case "10":				
				/*document.getElementById('obligacionDIV').style.visibility = 'hidden';
				document.getElementById('obligacionDIV').style.display = 'none';					
				document.getElementById('fechaCeleb').innerHTML = '<span class="textoGeneralRojo"> * Fecha de surgimiento del Derecho de Retenci�n :</span>';
				document.getElementById('terIDcond').innerHTML = '<span class="textoGeneralRojo"> * Fundamento Legal del cual surge el Derecho de Retenci�n';*/
				displayObject('fecha1',false);
				displayObject('fecha2',true);
				displayObject('fecha3',false);
				displayObject('terminos1',false);
				displayObject('terminos2',true);
				displayObject('terminos3',false);
				//document.getElementById("titulocopia").style.display="block";
				//document.getElementById("copia").style.display="block";  
				document.getElementById("titulo").style.display="none";  
				document.getElementById("tittipoacto").style.display="none"; 
				document.getElementById("tipoContOb").style.display="none";
				document.getElementById("titdate2").style.display="none"; 
				document.getElementById("datepicker4").style.display="none";
				document.getElementById("titdate3").style.display="none"; 
				document.getElementById("datepicker5").style.display="none";
				document.getElementById("titterminos").style.display="none"; 
				document.getElementById("farectificacion_modotrosterminos").style.display="none";
				displayObject('_modotrosgarantia',false);
				displayObject('_noEditablemodotrosgarantia',true);
				break;
			case "11":
				/*
				document.getElementById('fechaCeleb').innerHTML = '<span class="textoGeneralRojo"> * Fecha de celebraci�n del acto o contrato :</span>';
				document.getElementById('terIDcond').innerHTML = '<span class="textoGeneralRojo"> T�rminos y Condiciones del Acto o Contrato de la Garant�a Mobiliaria que desee incluir';*/
				displayObject('fecha1',true);
				displayObject('fecha2',false);
				displayObject('fecha3',false);
				displayObject('terminos1',true);
				displayObject('terminos2',false);
				displayObject('terminos3',false);
				//document.getElementById("titulocopia").style.display="block";
				//document.getElementById("copia").style.display="block";  
				document.getElementById("titulo").style.display="block";  
				document.getElementById("tittipoacto").style.display="block"; 
				document.getElementById("tipoContOb").style.display="block";
				document.getElementById("titdate2").style.display="block"; 
				document.getElementById("datepicker4").style.display="block";
				document.getElementById("titdate3").style.display="block"; 
				document.getElementById("datepicker5").style.display="block";
				document.getElementById("titterminos").style.display="block"; 
				document.getElementById("farectificacion_modotrosterminos").style.display="block";
				displayObject('_modotrosgarantia',true);
				displayObject('_noEditablemodotrosgarantia',false);
				break;
			case "12":
				/*document.getElementById('obligacionDIV').style.visibility = 'hidden';
				document.getElementById('obligacionDIV').style.display = 'none';					
				document.getElementById('fechaCeleb').innerHTML = '<span class="textoGeneralRojo"> * Fecha de surgimiento del Privilegio Especial :</span>';
				document.getElementById('terIDcond').innerHTML = '<span class="textoGeneralRojo"> * Fundamento Legal del cual surge el Privilegio Especial';*/
				displayObject('fecha1',true);
				displayObject('fecha2',true);
				displayObject('fecha3',true);
				displayObject('terminos1',true);
				displayObject('terminos2',true);
				displayObject('terminos3',true);
				//document.getElementById("titulocopia").style.display="block";
				//document.getElementById("copia").style.display="block";  
				document.getElementById("titulo").style.display="block";  
				document.getElementById("tittipoacto").style.display="block"; 
				document.getElementById("tipoContOb").style.display="block";
				document.getElementById("titdate2").style.display="block"; 
				document.getElementById("datepicker4").style.display="block";
				document.getElementById("titdate3").style.display="block"; 
				document.getElementById("datepicker5").style.display="block";
				document.getElementById("titterminos").style.display="block"; 
				document.getElementById("farectificacion_modotrosterminos").style.display="block";
				displayObject('_modotrosgarantia',true);
				displayObject('_noEditablemodotrosgarantia',true);
				break;
				
		}
	}
	
 	function selecciona(frm) {
 		  for (i = 0; ele = frm.modtipobien.options[i]; i++){
 			  
 		   	  ele.selected = true;
 		  }
 		  getObject('idTipoBienAll').value="true";
 			} 
 	 	
 		function desSelecciona(frm) {
 		  for (i = 0; ele = frm.modtipobien.options[i]; i++){
 		   	  ele.selected = false;
 		  }
 		  getObject('idTipoBienAll').value="false";
 			} 	
 		
 		function fechasCorrectas(){
 			
 		}
</script>