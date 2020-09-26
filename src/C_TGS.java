//客户端发送给TGS的消息
import java.io.Serializable;

public class C_TGS implements Serializable{
	
	String IDv;
	Ticket_tgs tt;
	Authenticator_tgs at;

	public C_TGS(String IDv,Ticket_tgs tt,Authenticator_tgs at)
	{
		this.IDv = IDv;
		this.tt = tt;
		this.at = at;
	}
	
	public String getIDv(){
		return this.IDv;
	}
	
	public Ticket_tgs getTicket_tgs(){
		return this.tt;
	}
	
	public Authenticator_tgs getAuthenticator_tgs(){
		return this.at;
	}
	
}