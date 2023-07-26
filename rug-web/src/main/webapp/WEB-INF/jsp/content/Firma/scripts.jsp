<script src="<%=request.getContextPath()%>/resources/js/sweetalert.js"></script>
<script type="text/javascript">


   function abrirCentrado(Url,NombreVentana,ancho,alto) {
		y=(screen.height-alto)/2;
		x=(screen.width-ancho)/2;
		ventanaHija = window.open(Url,"Poliza","left="+x+",top="+y+",width="+ancho+",height="+alto);
	}	   
    	
	function sendForm(){
		displayMessageAlert(false);	
		showBoleta();	
	}
	
	function showBoleta() {
            console.log("showboleta");
		        <%--var URL="<%=request.getContextPath()%>/home/boleta.do";--%>
		        var URL="<%=request.getContextPath()%>/home/boletaSignature.do";

                const a = document.createElement('a');
                a.style.display = 'none';
                a.href = URL;
                var evt = document.createEvent('MouseEvents');
                evt.initMouseEvent('click', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, null);
                MessageConfirm();
                a.download  = true;
                a.dispatchEvent(evt);
                        
		//window.open(URL, "_blank");
		//abrirCentrado(URL,"Boleta","500","500"); 
	}

   function MessageConfirm()
   {
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
        
</script>