/**
 * 
 */
package mx.randalf.quartz;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import mx.randalf.socket.client.Client;
import mx.randalf.socket.client.exception.ClientException;
import mx.randalf.socket.server.Server;
import mx.randalf.socket.server.ServerConn;
import mx.randalf.socket.server.exception.ServerException;

/**
 * @author massi
 *
 */
public class QuartzSocket extends Thread {

	private Logger log = Logger.getLogger(QuartzSocket.class);

	private int port = 0;
	private QuartzMaster scheduler = null;

	/**
	 * 
	 */
	public QuartzSocket(QuartzMaster scheduler, int port) {
		this.port = port;
		this.scheduler = scheduler;
	}

	/**
	 * 
	 */
	public QuartzSocket(int port) {
		this.port = port;
		closeSocket();
	}

	private void closeSocket(){
		Client client = null;
		
		try {
			System.out.println("Eseguo lo shutdown");
			client = new Client();
			client.Connect("127.0.0.1", port);
			client.Send("CLOSE");
		} catch (ClientException e) {
			log.error(e.getMessage(),e);
		} finally {
			if (client != null){
				client.Close();
			}
		}
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		openSocket();
	}

	private void openSocket(){
		Server server = null;
		ServerConn serverConn = null;
		String input = null;
		
		try {
			System.out.println("Apro la porta: "+port);
			server = new Server();
			server.OpenServer(port);
			
			while(true){
				try {
					serverConn = new ServerConn(server.OpenConnection());
					
					input = serverConn.Recive();
					if (input.equals("CLOSE")){
						System.out.println("\n\n\nRicevuta la richiesta di chiusura del programma.");
						System.out.println("Rimango in attesa che tutti i Jobs attivi finiscano l'elaborazione");
						scheduler.shutdown();
						System.out.println("\n\n\nCHIUDO");
						break;
					}
				} catch (ServerException e) {
					log.error(e.getMessage(),e);
				} catch (SchedulerException e) {
					log.error(e.getMessage(),e);
				} finally {
					if (serverConn != null){
						serverConn.CloseConnection();
					}
				}
			}
		} catch (ServerException e) {
			log.error(e.getMessage(),e);
		} finally {
			if (server != null){
				server.CloseServer();
			}
		}
	}
}
