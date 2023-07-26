<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript"    src="${pageContext.servletContext.contextPath}/dwr/interface/UsuarioDwrAction.js"></script>
<!-- <script src="<%=request.getContextPath()%>/resources/js/validator.min.js"></script> -->
<script src="<%=request.getContextPath()%>/resources/js/validate.js"></script>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/sweetalert.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/v/zf/dt-1.12.1/af-2.4.0/b-2.2.3/fh-3.2.4/kt-2.7.0/r-2.3.0/datatables.min.js"></script>

<script type="text/javascript">

function setGarantiaPermiso(){
    let idGarantia = $('input:text[name=idGarantia]').val()
    let idUsuario = $('input:hidden[id=idUsuario]').val()
    const url = '<%= request.getContextPath() %>/rs/permisos/asignar-garantia/' ;
    axios.post(url, { idUsuario: idUsuario, idGarantia: idGarantia }).then(response => {
        if(response.data){
            messageSuccess("Garantia asignada correctamente")
            tablaGarantiaPermiso(idUsuario)
            $('#idGarantia').val('')
        }else{
            messageError("No se encuentra el numero de garantia o la garantia ya se encuentra asignada");
            $('#idGarantia').val('')
        }
    })
}

function desactivarGarantiaPermiso(idGarantia){
    let idUsuario = $('input:hidden[id=idUsuario]').val()
    const url = '<%= request.getContextPath() %>/rs/permisos/desactivar-garantia' ;
    axios.post(url, { idUsuario: idUsuario, idGarantia: idGarantia }).then(response => {
        if(response.data){
            messageSuccess("Garantia Inhabilitada con exito!")
            tablaGarantiaPermiso(idUsuario)
            $('#idGarantia').val('')
        }else{
            messageError("Error al Inhabilitar la Garantia");
            $('#idGarantia').val('')
        }
    })

}

function isAdminValidate() {
    const idPersona =  <s:property value='idPersona' />
    const url = '<%= request.getContextPath() %>/rs/usuarios-admin/verificar';
    axios.post(url, {
        idUsuario:idPersona
    }).then(response => {
        if(response.data[0] > 0){
            return true
        }
        else {
            return false
        }
    })


}

function onChangePregunta() {
	var pregunta = $('#pregunta').val();
	if (pregunta === 'Agregar otra') {
		$('#otra-pregunta').show();
	} else {
		$('#otra-pregunta').hide();
	}
}

function aceptaalfa(evt)
{
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57)
                      && (charCode < 65 || charCode > 90)
                      && (charCode < 97 || charCode > 122)
                      && (charCode <209  || charCode > 249)
        )
        return false;
    return true;
}

function inicializaFormUsuario(usuario) {

	if (usuario) {
		$('#usuarioModificar').val(usuario.idPersona);
		$('#nombre').val(usuario.nombre);
		$('#docId').val(usuario.docId);
		$('#email').val(usuario.cveUsuario);
		$('#password').val(usuario.password);
		$('#confirmacion').val(usuario.password);
		$("#pregunta").prop('selectedIndex', 0);
		$('#otraPregunta').val('');
		$('#respuesta').val('');
        if(usuario.isAdminButton > 0 ){
            $("input[name='isUpdateAdmin']").val(1)
            $("input[name='isAdmin.button']").prop('checked', true);
        }else{
            $("input[name='isUpdateAdmin']").val(0)
            $("input[name='isAdmin.button']").prop('checked', false);
        }
	} else {
		$('#usuarioModificar').val('');
		$('#nombre').val('');
		$('#docId').val('');
		$('#email').val('');
		$('#password').val('');
		$('#confirmacion').val('');
		$("#pregunta").prop('selectedIndex', 0);
		$('#otraPregunta').val('');
		$('#respuesta').val('');
        $("input[name='isAdmin.button']").prop('checked', false);

	}
	$('#pregunta').material_select();
	Materialize.updateTextFields();
	$('#frmUsuario').scrollTop(0);
}


$('#btnAgregar').on('click', function () {
	inicializaFormUsuario();
});

$(document).ready(function(){
    $('.collapsible').collapsible();

    iconoPermiso();

});


function tablaGarantiaPermiso(idPersona){
    const url = '<%= request.getContextPath() %>/rs/permisos/garantias-asignadas/' + idPersona ;
    $('#table_id').DataTable().destroy();
    $('#table_id').DataTable({
        "bDeferRender": true,
        "searching": false,
        "bLengthChange": false,
        "retrieve": true,
        "ajax": {
            "url": url,
        },
        "columns": [

            { "data": "idGarantia" },
            { "data": "sit" },
            {
                "data": null,
                "render": function(data, type, row){
                    return "<button type='button' class='eliminar btn btn-danger' onclick='desactivarGarantiaPermiso("+row.idGarantia+")'><i class='fa fa-trash'></i></button>"
                }
            }
        ],
        "oLanguage": {
            "sProcessing": "Procesando...",
            "sZeroRecords": "No se encontraron resultados",
            "sEmptyTable": "Ning&uacute;n dato disponible en esta tabla",
            "sInfo": "Mostrando del (_START_ al _END_) de un total de _TOTAL_ registros",
            "sInfoEmpty": "Mostrando del 0 al 0 de un total de 0 registros",
            "sInfoFiltered": "(filtrado de un total de _MAX_ registros)",
            "sInfoPostFix": "",
            "sSearch": "Filtrar:",
            "sUrl": "",
            "sInfoThousands": ",",
            "sLoadingRecords": "Por favor espere - cargando...",
            "oPaginate": {
                "sFirst": "Primero",
                "sLast": "&Uacute;ltimo",
                "sNext": "Siguiente",
                "sPrevious": "Anterior"
            },
            "oAria": {
                "sSortAscending": ": Activar para ordenar la columna de manera ascendente",
                "sSortDescending": ": Activar para ordenar la columna de manera descendente"
            }
        }
    });
}

function guardaUsuario(idPersona) {

    var checkBox = document.getElementById("isAdmin.button");
    var isUpdate = $('input:hidden[id=isUpdateAdmin]').val()
    const isAdmin = '';
    if (checkBox.checked == true) {
        this.isAdmin = '1';
    }else{
        this.isAdmin = '';
    }
	var nombre = $('#nombre').val();
	var docId = $('#docId').val();
	var email = $('#email').val();
	var password = $('#password').val();
	var confirmacion = $('#confirmacion').val();
	var pregunta = $('#pregunta').val();
	var otraPregunta = $('#otraPregunta').val();
	var respuesta = $('#respuesta').val();
	var usuarioModificar = $('#usuarioModificar').val();
	// validar los datos ingresados
	$('#error').empty();
	$('.error-label').remove();
    if(usuarioModificar){

        if (nombre && docId) {

            if (!validator.isEmail(email)) {
                $('#email').parent().append('<span class="error-label" style="position: relative; top: -1rem; left: 0rem; font-size: 0.8rem; color: #FF4081; display: block; text-align: center;">El correo electr&oacute;nico debe ser v&aacute;lido.</span>');
                return;
            }

            const idPersona =  <s:property value='idPersona' />
            const url = '<%= request.getContextPath() %>/rs/usuarios-admin/verificar';
            if(this.isAdmin == '1' || usuarioModificar === idPersona ){
                axios.post(url, {
                    idUsuario:idPersona
                }).then(response => {
                    if(isUpdate == 1){
                        UsuarioDwrAction.actualizaSubusuario(this.isAdmin, idPersona, usuarioModificar, nombre, docId, email, password, pregunta, otraPregunta, respuesta,  resultadoActualizar);
                        return;
                    }
                    if(response.data[0] > 0){
                        Swal.fire({
                            icon: 'error',
                            title: 'Ingreso de Sub Usuarios',
                            text: 'No se puede crear otro usuario administrador, ya existen 2 usuarios',
                            allowOutsideClick: false
                        })
                        return;
                    }
                    else {
                        if (usuarioModificar) {
                            // actualiza al usuario existente
                            UsuarioDwrAction.actualizaSubusuario(this.isAdmin, idPersona, usuarioModificar, nombre, docId, email, password, pregunta, otraPregunta, respuesta,  resultadoActualizar);
                        } else {
                            // crea un nuevo usuario
                            UsuarioDwrAction.guardaSubusuario(this.isAdmin,idPersona, nombre, docId, email, password, pregunta, otraPregunta, respuesta, resultadoGuardar);
                        }
                    }
                })
            }else{
                if (usuarioModificar) {
                    // actualiza al usuario existente
                    UsuarioDwrAction.actualizaSubusuario(this.isAdmin, idPersona, usuarioModificar, nombre, docId, email, password, pregunta, otraPregunta, respuesta,  resultadoActualizar);
                } else {
                    // crea un nuevo usuario
                    UsuarioDwrAction.guardaSubusuario(this.isAdmin,idPersona, nombre, docId, email, password, pregunta, otraPregunta, respuesta, resultadoGuardar);
                }
            }

        } else {
            $('#error').append('<p style="color: red;">Todos los datos son obligatorios.</p>');
        }
    }else{
        if (nombre && docId && email && password && confirmacion /*&& pregunta && respuesta*/) {
            if (password != confirmacion) {
                $('#password').parent().append('<span class="error-label" style="position: relative; top: -1rem; left: 0rem; font-size: 0.8rem; color: #FF4081; display: block; text-align: center;">La contraseña y la confirmaci&oacute;n deben ser iguales.</span>');
                return;
            }

            if (!validator.matches(password, '(?=.*[0-9])(?=.+[a-z])(?=.*[A-Z]).{8,16}')) {
                $('#password').parent().append('<span class="error-label" style="position: relative; top: -1rem; left: 0rem; font-size: 0.8rem; color: #FF4081; display: block; text-align: center;">Su contraseña debe tener una longitud de entre 8 y 16 caracteres, debe incluir letras min&uacute;sculas, al menos una letra may&uacute;scula y al menos un n&uacute;mero.</span>');
                return;
            }

            if (!validator.isEmail(email)) {
                $('#email').parent().append('<span class="error-label" style="position: relative; top: -1rem; left: 0rem; font-size: 0.8rem; color: #FF4081; display: block; text-align: center;">El correo electr&oacute;nico debe ser v&aacute;lido.</span>');
                return;
            }

            const idPersona =  <s:property value='idPersona' />
            const url = '<%= request.getContextPath() %>/rs/usuarios-admin/verificar';
            if(this.isAdmin == '1'){
                axios.post(url, {
                    idUsuario:idPersona
                }).then(response => {
                    if(response.data[0] > 0){
                        Swal.fire({
                            icon: 'error',
                            title: 'Ingreso de Sub Usuarios',
                            text: 'No se puede crear otro usuario administrador, ya existen 2 usuarios',
                            allowOutsideClick: false
                        })
                        return;
                    }
                    else {
                        if (usuarioModificar) {
                            // actualiza al usuario existente
                            UsuarioDwrAction.actualizaSubusuario(this.isAdmin, idPersona, usuarioModificar, nombre, docId, email, password, pregunta, otraPregunta, respuesta,  resultadoActualizar);
                        } else {
                            // crea un nuevo usuario
                            UsuarioDwrAction.guardaSubusuario(this.isAdmin,idPersona, nombre, docId, email, password, pregunta, otraPregunta, respuesta, resultadoGuardar);
                        }
                    }
                })
            }else{
                if (usuarioModificar) {
                    // actualiza al usuario existente
                    UsuarioDwrAction.actualizaSubusuario(this.isAdmin, idPersona, usuarioModificar, nombre, docId, email, password, pregunta, otraPregunta, respuesta,  resultadoActualizar);
                } else {
                    // crea un nuevo usuario
                    UsuarioDwrAction.guardaSubusuario(this.isAdmin,idPersona, nombre, docId, email, password, pregunta, otraPregunta, respuesta, resultadoGuardar);
                }
            }

        } else {
            $('#error').append('<p style="color: red;">Todos los datos son obligatorios.</p>');
        }
    }
}

function resultadoGuardar(message) {
	if (message.codeError == 0) {
		// agregar el nuevo usuario al listado
		var data = JSON.parse(message.data);
        console.log("data ", data)
		var row = $('<tr>', {id: data.idPersona}).append(
			$('<td>').html(data.nombre)
		).append(
            $('<td>').html(data.isAdmin)
        ).append(
			$('<td>').html(data.cveUsuario)
		).append(
			$('<td>').append(
				$('<a>', {class: 'btn waves-effect indigo', onclick: 'cargaUsuario(' + data.idPersona + ')'}).append(
					$('<i>', {class: 'material-icons'}).html('edit')
				)
			).append(" ").append(
				$('<a>', {class: 'btn waves-effect red darken-4', onclick: 'eliminarUsuario(' + data.idPersona + ')'}).append(
						$('<i>', {class: 'material-icons'}).html('delete')
				)
			).append(" ").append(
                $('<a>', {class: 'btn waves-effect blue darken-4', onclick: 'permisos(' + data.idPersona + ')'}).append(
                    $('<i>', {class: 'material-icons'}).html('assignment_ind')
                )
            )
		);
		
		$('#usuarios tbody').append(row);
		
		$('#frmUsuario').modal('close');
		//inicializaFormAcreedor();
		// TODO: mostrar mensaje de exito
	} else {
		// TODO: mostrar errores al grabar usuario
	}
}

function resultadoActualizar(message) {
	if (message.codeError == 0) {
		// actualizar datos de usuario
		var data = JSON.parse(message.data);
		var idPersona = data.idPersona;
		var previous = $('#' + idPersona).prev();
		$('#' + idPersona).remove();
		var row = $('<tr>', {id: data.idPersona}).append(
			$('<td>').html(data.nombre)
		).append(
			$('<td>').html(data.isAdmin)
		).append(
            $('<td>').html(data.cveUsuario)
        ).append(
			$('<td>').append(
				$('<a>', {class: 'btn waves-effect indigo', onclick: 'cargaUsuario(' + data.idPersona + ')'}).append(
					$('<i>', {class: 'material-icons'}).html('edit')
				)
			).append(" ").append(
				$('<a>', {class: 'btn waves-effect red darken-4', onclick: 'eliminarUsuario(' + data.idPersona + ')'}).append(
					$('<i>', {class: 'material-icons'}).html('delete')
				)
			).append(" ").append(
				$('<a>', {class: 'btn waves-effect blue darken-4', onclick: 'permisos(' + data.idPersona + ')'}).append(
					$('<i>', {class: 'material-icons'}).html('assignment_ind')
				)
			)
		);


		if(previous) {
			$(previous).after(row);
		} else {

			$('#usuarios tbody').append(row);
		}

        if(previous.length === 0){
            $('#usuarios tbody').append(row);
        }
		
		$('#frmUsuario').modal('close');
		//inicializaFormAcreedor();
		// TODO: mostrar mensaje de exito
	} else {
		// TODO: mostrar errores al grabar usuario
	}
}

function cargaUsuario(usuarioId) {
	UsuarioDwrAction.getUsuario(usuarioId, recibeUsuario);
}

function permisos(idPersona) {
    $('.modal').modal({
        opacity: 0.5,
        dismissible: true,
    })
    $('#permissionModal').modal('open')



    const url = '<%= request.getContextPath() %>/rs/permisos/user';
    axios.post(url, {idPersona:idPersona}).then(response => {
        let permiso_list = []
        permiso_list = response.data
        $("#lista_permiso").empty();
        $.each(permiso_list, function (index, rol) {
            if(rol.idPrivilegio === 74 || rol.idPrivilegio === 75 || rol.idPrivilegio === 76 || rol.idPrivilegio === 77 || rol.idPrivilegio === 78 ){
            }else{
                $("#lista_permiso").append('<input type="checkbox" value="' + rol.idRelacion + '" id="id_' + rol.idRelacion + '" onClick="cambioPermiso('+rol.idRelacion+')" /><label for="id_' + rol.idRelacion + '">'+rol.privilegio.descripcion+'</label>').append(document.createElement('br'));
                if(rol.sitRelacion === 'AC'){
                    $('#id_'+rol.idRelacion).prop('checked', true)
                }
            }

            if(rol.idPrivilegio === 74 && rol.sitRelacion === 'AC'){
                $('#id_'+rol.idPrivilegio).prop('checked', true)
            }

            if(rol.idPrivilegio === 75 && rol.sitRelacion === 'AC'){
                $('#id_'+rol.idPrivilegio).prop('checked', true)
            }

            if(rol.idPrivilegio === 76 && rol.sitRelacion === 'AC'){
                $('#id_'+rol.idPrivilegio).prop('checked', true)
            }

            if(rol.idPrivilegio === 77 && rol.sitRelacion === 'AC'){
                $('#id_'+rol.idPrivilegio).prop('checked', true)
            }

            if(rol.idPrivilegio === 78 && rol.sitRelacion === 'AC'){
                    $('#id_'+rol.idPrivilegio).prop('checked', true)
            }
        });
    })

    getOperacionesPermiso(idPersona)

    tablaGarantiaPermiso(idPersona)

    $('#idUsuario').val(idPersona)

}

function getOperacionesPermiso(idPersona){
    const url = '<%= request.getContextPath() %>/rs/permisos/operaciones-listado';
    axios.get(url).then(response => {
        let operacion_list = []
        operacion_list = response.data
        $("#lista_operaciones").empty();
        $.each(operacion_list, function (index, operacion){
            $("#lista_operaciones").append('<input type="checkbox" value="' + operacion.idPrivilegio + '" id="id_' + operacion.idPrivilegio + '" onClick="activarOperacion('+operacion.idPrivilegio+','+idPersona+')" /><label for="id_' + operacion.idPrivilegio + '">'+operacion.descripcion+'</label>').append(document.createElement('br'));
        })

    })
}

function activarOperacion(idOperacion, idPersona){
    const url = '<%= request.getContextPath() %>/rs/permisos/asignar-operacion';
    axios.post(url, {
        idPersona: idPersona,
        idOperacion: idOperacion
    }).then(response => {
        if(response.data === 'AC'){
            $('#id_'+idOperacion).prop('checked', true)
        }else{
            $('#id_'+idOperacion).prop('checked', false)
        }
    })
}

function iconoPermiso(){
    let cambioPermiso =  $('.opciones');
    $.each(cambioPermiso, function (index, rol) {
        const url = '<%= request.getContextPath() %>/rs/permisos/grupo';
        axios.post(url, { idPersona:rol.name }).then(response => {
            if(response.data === 8){
                $('#'+rol.id).removeClass('hidePermisos');
            }
        })
    })
}

function permiso_dinamico(idPersona, nombre, isAdmin){
    const url = '<%= request.getContextPath() %>/rs/permisos/rol-dinamico';
    axios.post(url, {
        idPersona:idPersona,
        nombre:nombre,
        isAdmin:isAdmin
    }).then(response => {
        if(response.data){
            messageSuccess('Permisos actualizados')
            $('#id_'+idPersona).addClass('hidePermisos');
        }
    })
}

function cambioPermiso(idRelacion){
    var checkBox = document.getElementById("id_"+idRelacion);
    const url = '<%= request.getContextPath() %>/rs/permisos/deshabilitar';
    if (checkBox.checked == true) {
        axios.post(url, {
            idRelacion: idRelacion,
            sitRelacion: "AC"
        }).then(response => {
            console.log(response.data)
        })
    }else{
        axios.post(url, {
            idRelacion: idRelacion,
            sitRelacion: "IN"
        }).then(response => {
            console.log(response.data)
        })
    }
}

function recibeUsuario(data) {
	var usuario = JSON.parse(data);
	inicializaFormUsuario(usuario);
	$('#frmUsuario').modal('open');	
}

function eliminarUsuario(usuarioId) {
    const idPersona =  <s:property value='idPersona' />
    if(idPersona === usuarioId){
        Swal.fire({
            icon: 'error',
            title: 'Ingreso de Sub Usuarios',
            text: 'No se puede eliminar este usuario',
            allowOutsideClick: false
        })
    }else{
        if (confirm('Esta seguro de eliminar al usuario?')) {
            UsuarioDwrAction.eliminaSubusuario(usuarioId, resultadoEliminar);
        }
    }

}

function resultadoEliminar(message) {
	if (message.codeError == 0) {
		// quitaral usuario de la tabla
		var data = JSON.parse(message.data);
		var idPersona = data.idPersona;
		$('#' + idPersona).remove();
	}
}

function messageSuccess(text){
    Swal.fire({
        icon: 'success',
        text: text,
        allowOutsideClick: false
    })
}

function messageError(text){
    Swal.fire({
        icon: 'error',
        text: text,
        allowOutsideClick: false
    })
}



</script>




