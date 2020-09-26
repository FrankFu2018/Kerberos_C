//客户端发给应用服务器的消息
import java.io.Serializable;
public class C_S implements Serializable{
	Ticket_v tv;
	Authenticator_v Auv;
	//Ticket_v || Authenticator_v
	public C_S(Ticket_v tv,Authenticator_v Auv)
	{
		this.tv = tv;
		this.Auv = Auv;
	}
	
	public Ticket_v getTicket_v(){
		return this.tv;
	}
	
	public Authenticator_v getAuthenticator_v(){
		return this.Auv;
	}
}