<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/tooltips/tooltip.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/material-dialog.min.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/resources/js/partesJS/parteJS.js"></script>

<script type="text/javascript">
	var idTramite = <s:property value = "idInscripcion"/> ;


function noVacio(valor){
	   for ( i = 0; i < valor.length; i++ ) {  
	     if ( valor.charAt(i) != " " ) {
	      return true; 
	    }  
	   }  
	 return false; 
}

function IsNumber(evt) {
    var key = (document.all) ? evt.keyCode : evt.which;
    
    return (key <= 13 || (key >= 48 && key <= 57));
}

var statSend = false;

function paso2() {
	if (!statSend) {
    	document.getElementById("buttonID").value = "Enviando";
    	document.getElementById("buttonID").disabled = true;
    	var idIns = document.getElementById("refInscripcion").value;
       	statSend = true;
       	window.location.href = '<%= request.getContextPath() %>/inscripcion/paso2.do?idInscripcion=' + idIns;       	
       	return true;
    } else {		
        return false;
    }		
 }
	function sendForm(){
		document.getElementById("baceptar").value = "Enviando";
    	document.getElementById("baceptar").disabled = true;
		document.formAcVig.submit();
	}

	function verificarBienes(_data,_valor){
		if(_data.indexOf(_valor) > -1){
			return true
		}else{
			return false
		}

	}

	function validaMesesPaso3(){
            console.log("ENTRA EN ACEPTAR");
		var descripcionBienes = document.getElementById('descripcion_bien').value;
		var idArancel = Array;
		var idtipoTramite;
		var valor = document.getElementById('formAcVig_inscripcionTO_meses').value;
		if (valor == '' || valor == '0'){
			alertMaterialize('La Vigencia debe ser de por lo menos un año');
			return false;
		}
		
		if(valor > 5) {
			alertMaterialize('La Vigencia no puede ser mayor a cinco años');
			return false;
		}
		
		var saldo = document.getElementById('mdSaldo').value;		
		if(saldo=="0"){
			alertMaterialize('No tiene saldo suficiente para realizar la inscripcion');
			return false;
		}

		path = '<%= request.getContextPath() %>/rs/garantias-pend/verificacion/' + idTramite
                
                console.log(path);
		$.ajax({
			url: path,
			success: function(response){
				idArancel = response
                                console.log("idArancel: "+idArancel);
				if(descripcionBienes.length > 0){
					idtipoTramite = 1
				}else{
					if (verificarBienes(idArancel,1) || verificarBienes(idArancel,2) || verificarBienes(idArancel,3) ){
						idtipoTramite = 1
					}else if(verificarBienes(idArancel,6)){
						idtipoTramite = 39
					}else if(verificarBienes(idArancel,5)){
						idtipoTramite = 38
					}else{
						idtipoTramite = 1
					}
				}
                                console.log(idtipoTramite);
                                path2 = '<%= request.getContextPath() %>/rs/tipos-tramite/' + idtipoTramite
                                console.log("path2 : "+path2);
				$.ajax({
					url: path2,
					success: function(result) {
						MaterialDialog.dialog(
							"El costo de una " + "Inscripcion" + " es de Q. " + (Math.round(result.precio * 100) / 100).toFixed(2) + ", esta seguro que desea continuar?",
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
		})
		
	}

	function msg_guardar() {
		alertMaterialize('El sistema le guardara la garant�a temporalmente por 72 horas, esto no constituye una inscripci�n y por lo tanto no otorga prelacion');
		return false;
	}
	
</script>