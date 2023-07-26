package mx.gob.se.mail;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import mx.gob.se.conexion.ConexionDB;
import mx.gob.se.exception.InfrastructureException;
import mx.gob.se.mail.to.AccountMailTO;
import mx.gob.se.mail.to.MailTO;
import mx.gob.se.mail.to.MessageTO;


public class MailBitacoraDAO {

	public void logMail(MailTO mailTO) {
		ConexionDB conexionDB = new ConexionDB();
		//obtenemos el id del mail
		try {
			String sql = "{call RUG.SP_ALTA_MAIL(?,?,?" +
					",?,?,?" +
					",?,?,?" +
					",?)}";
//	         peIdTipoCorreo          IN  NUMBER,
//             peIdMailAccount         IN  NUMBER,
//             peDestinatario          IN  VARCHAR2,
//             peDestinatarioCC        IN  VARCHAR2,
//             peDestinatarioCCO       IN  VARCHAR2,
//             peAsunto                IN  VARCHAR2,
//             peMensaje               IN  CLOB,                 
	         //psIdMail               OUT  NUMBER, 
//             psResult               OUT  NUMBER,
//             psTxResult             OUT  VARCHAR2
//			                                          )

			CallableStatement cs = conexionDB.prepareCall(sql);
			cs.setInt(1, mailTO.getIdTipoMensaje());
			cs.setInt(2, mailTO.getAccountMailTO().getIdAccountSmtp());
			
			cs.setString(3, mailTO.getMessageTO().getMailTo());
			cs.setString(4, mailTO.getMessageTO().getMailCc());
			cs.setString(5, mailTO.getMessageTO().getMailCco());
			cs.setString(6, mailTO.getMessageTO().getMailSubject());
			cs.setString(7, mailTO.getMessageTO().getMailText());
			cs.registerOutParameter(8,Types.INTEGER);
			cs.registerOutParameter(9,Types.INTEGER);
			cs.registerOutParameter(10,Types.VARCHAR);


			conexionDB.executeQuery();
			
			
			if(cs.getInt(9)==0){
				System.out.println("se Actualizo Correctamente a estatus " + mailTO.getIdStatus());
			}else{
				//klanza excepcion
				throw new InfrastructureException(cs.getString("psTxResult"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfrastructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			conexionDB.destroy();
		}
	}

	public List<MailTO> getMailToSend(){
		ConexionDB conexionDB = new ConexionDB();
		List<MailTO> mailTos = new ArrayList<MailTO>();
		try {
			conexionDB.prepareStatement("Select ID_MAIL,DESTINATARIO " +
					" ,DESTINATARIO_CC,DESTINATARIO_CCO,ASUNTO,MENSAJE " +
					" ,SMTP_HOST,SMTP_PORT,SMTP_USER_MAIL,SMTP_PASSWORD,SMTP_AUTH " +
					" ,SMTP_SSL_ENABLE,MAIL_CONTENT_TYPE from V_MAIL_POOL_PENDIENTE ");
			ResultSet rs;
			rs = conexionDB.executeQuery();
			if(rs.next()){
				MailTO mailTo= new MailTO();
				mailTo.setIdMail(rs.getInt("ID_MAIL"));
				
				MessageTO messageTO= new MessageTO();
				messageTO.setMailTo(rs.getString("DESTINATARIO"));
				messageTO.setMailCc(rs.getString("DESTINATARIO_CC"));
				messageTO.setMailCco(rs.getString("DESTINATARIO_CCO"));
				messageTO.setMailSubject(rs.getString("ASUNTO"));
				messageTO.setMailText(rs.getString("MENSAJE"));
				mailTo.setMessageTO(messageTO);
				
				
				AccountMailTO accountMailTO= new AccountMailTO();
				accountMailTO.setSmtpHost(rs.getString("SMTP_HOST"));
				
				accountMailTO.setSmtpPort(rs.getString("SMTP_PORT"));
				accountMailTO.setSmtpUserMail(rs.getString("SMTP_USER_MAIL"));
				accountMailTO.setSmtpPasswordMail(rs.getString("SMTP_PASSWORD"));
				accountMailTO.setSmtpAuth(rs.getString("SMTP_AUTH"));
				accountMailTO.setSmtpSslEnable(rs.getString("SMTP_SSL_ENABLE"));
				accountMailTO.setMailContentType(rs.getString("MAIL_CONTENT_TYPE"));
				mailTo.setAccountMailTO(accountMailTO);
				
				mailTos.add(mailTo);
			}else {
				System.out.println("no mails");
			}
		} catch (InfrastructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			conexionDB.destroy();
		}
		return mailTos;
	}
	
	public void setMailStatus(MailTO mailTO) {
		ConexionDB conexionDB = new ConexionDB();
		try {
			String sql = "{call SP_MAIL_STATUS(?,?,?,?,?)}";
//			 peIdMail          IN  NUMBER,
//             peIdStatus        IN  NUMBER,
//             peExcepcionMsg    IN  CLOB,
//             psResult         OUT  NUMBER,
//             psTxResult       OUT  VARCHAR2
			CallableStatement cll = conexionDB.prepareCall(sql);
			cll.setInt(1,mailTO.getIdMail() );
			cll.setInt(2,mailTO.getIdStatus() );
			cll.setString(3, mailTO.getExceptionMail());
			cll.registerOutParameter(4, Types.INTEGER);
			cll.registerOutParameter(5, Types.VARCHAR);
			
			conexionDB.executeQuery();
			
			if(cll.getInt(4)==0){
				System.out.println("se Actualizo Correctamente a estatus " + mailTO.getIdStatus());
			}else{
				//klanza excepcion
				throw new InfrastructureException(cll.getString("psTxResult"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfrastructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			conexionDB.destroy();
		}
	}

}
