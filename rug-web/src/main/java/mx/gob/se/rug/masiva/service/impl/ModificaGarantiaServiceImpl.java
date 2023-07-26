package mx.gob.se.rug.masiva.service.impl;

import java.util.Date;

import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.masiva.exception.CargaMasivaException;
import mx.gob.se.rug.masiva.exception.NoDataFound;

public class ModificaGarantiaServiceImpl {
	

	
	public void validaDatos(Object objetoValidar,String nombreCampo,boolean isRequiredGatarantia,boolean isRequiredTramite,Integer idTipoFecha) throws InfrastructureException, NoDataFound, CargaMasivaException{
		
		//idTipoFecha =1 FechaInicial
		//idTipoFecha =2 FechaFinal
		
		if(isRequiredGatarantia){
			//validar que no sea NULL o 0 o ""
			if(objetoValidar!=null){
				String nombreClase=objetoValidar.getClass().getName().split("\\.")[objetoValidar.getClass().getName().split("\\.").length-1];
					switch (Constants.getIdDataType(nombreClase)) {
					case 1://String
						if (((String)objetoValidar).trim().equalsIgnoreCase("")){
							throw new NoDataFound(nombreCampo);
						}
						break;
					case 2://Integer
						if (((Integer)objetoValidar).intValue()==0){
							throw new NoDataFound(nombreCampo);
						}
						if (((Integer)objetoValidar).intValue()<0){
							throw new CargaMasivaException(101,nombreCampo);
						}
						break;
					case 3://Double
						if (((Double)objetoValidar).doubleValue()==0){
							throw new NoDataFound(nombreCampo);
						}
						if (((Double)objetoValidar).doubleValue()<0){
							throw new CargaMasivaException(101,nombreCampo);
						}
						break;
					case 4://Float
						if (((Double)objetoValidar).floatValue()==0){
							throw new NoDataFound(nombreCampo);
						}
						if (((Double)objetoValidar).floatValue()<0){
							throw new CargaMasivaException(101,nombreCampo);
						}
						break;
					case 5://Date
						Date hoy = new Date();
						if (idTipoFecha.intValue()==1){//Fecha inicio *Si es una fecha de inicio, validar que sea menor a la fecha actual.
							  if(((Date)objetoValidar).before(hoy)){
					            	System.out.println("es menor");
					            }else{
					            	throw new CargaMasivaException(99, nombreCampo);
					            }
						}	
//						}else if (idTipoFecha.intValue()==2) {//Fecha Final * Si es una fecha de terminación, validar que no sea menor a la fecha actual.
//							  if(((Date)objetoValidar).after(hoy)){
//					            	System.out.println("es mayor");
//					            }else{
//					            	throw new CargaMasivaException(98);
//					            }
//						}
						break;
					case 6://Timestamp
						break;
					case 7://CLOB
						break;
					}
			}else{//NULL
				//No se quiere Modificar el campo 
			}
		}else if(isRequiredTramite){
				//validar que no sea NULL o 0 o ""
				if(objetoValidar!=null){
					String nombreClase=objetoValidar.getClass().getName().split("\\.")[objetoValidar.getClass().getName().split("\\.").length-1];
						switch (Constants.getIdDataType(nombreClase)) {
						case 1://String
							if (((String)objetoValidar).trim().equalsIgnoreCase("")){
								throw new NoDataFound(nombreCampo);
							}
							break;
						case 2://Integer
							if (((Integer)objetoValidar).intValue()==0){
								throw new NoDataFound(nombreCampo);
							}
							if (((Integer)objetoValidar).intValue()<0){
								throw new CargaMasivaException(101,nombreCampo);
							}
							break;
						case 3://Double
							if (((Double)objetoValidar).doubleValue()==0){
								throw new NoDataFound(nombreCampo);
							}
							if (((Double)objetoValidar).doubleValue()<0){
								throw new CargaMasivaException(101,nombreCampo);
							}
							break;
						case 4://Float
							if (((Float)objetoValidar).floatValue()==0){
								throw new NoDataFound(nombreCampo);
							}
							if (((Float)objetoValidar).floatValue()<0){
								throw new CargaMasivaException(101,nombreCampo);
							}
							break;
						case 5://Date
							Date hoy = new Date();
							if (idTipoFecha.intValue()==1){//Fecha inicio *Si es una fecha de inicio, validar que sea menor a la fecha actual.
								  if(((Date)objetoValidar).before(hoy)){
						            	System.out.println("es menor");
						            }else{
						            	throw new CargaMasivaException(99);
						            }
								
//							}else if (idTipoFecha.intValue()==2) {//Fecha Final * Si es una fecha de terminación, validar que no sea menor a la fecha actual.
//								  if(((Date)objetoValidar).after(hoy)){
//						            	System.out.println("es mayor");
//						            }else{
//						            	throw new CargaMasivaException(98);
//						            }
							}
							break;
						case 6://Timestamp
							break;
						case 7://CLOB
							break;
						}
				}else{//NULL
					throw new NoDataFound(nombreCampo);
				}
			
			
		}else{//Dato OPCIONAL
		
		
		if(objetoValidar!=null){
			String nombreClase=objetoValidar.getClass().getName().split("\\.")[objetoValidar.getClass().getName().split("\\.").length-1];
				switch (Constants.getIdDataType(nombreClase)) {
				case 1://String
			
					break;
				case 2://Integer
					if (((Integer)objetoValidar).intValue()<0){
						throw new CargaMasivaException(101,nombreCampo);
					}
					break;
				case 3://Double
					if (((Double)objetoValidar).doubleValue()<0){
						throw new CargaMasivaException(101,nombreCampo);
					}
					break;
				case 4://Float
					if (((Float)objetoValidar).floatValue()<0){
						throw new CargaMasivaException(101,nombreCampo);
					}
					break;
				case 5://Date
					Date hoy = new Date();
					if (idTipoFecha.intValue()==1){//Fecha inicio *Si es una fecha de inicio, validar que sea menor a la fecha actual.
						  if(((Date)objetoValidar).before(hoy)){
				            	System.out.println("es menor");
				            }else{
				            	throw new CargaMasivaException(99);
				            }
						
//					}else if (idTipoFecha.intValue()==2) {//Fecha Final * Si es una fecha de terminación, validar que no sea menor a la fecha actual.
//						  if(((Date)objetoValidar).after(hoy)){
//				            	System.out.println("es mayor");
//				            }else{
//				            	throw new CargaMasivaException(98);
//				            }
					}
					break;
				case 6://Timestamp
					break;
				case 7://CLOB
					break;
				}
		}else{//NULL
			
		}
		
	}

	

	}

	
}
