import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;


public class ChatServer 
{
	static ArrayList<String> userNames = new ArrayList<String>();
	static ArrayList<PrintWriter> printWriter = new ArrayList<PrintWriter>();
	
	public static void main(String[] args) throws Exception
	{
		System.out.println("Waiting for client...");
		ServerSocket ss = new ServerSocket(9806);
		//while loop to wait more user to come in
		while(true)
		{
			Socket soc = ss.accept();
			System.out.println("Connection estableished");
			ConversationHandler handler = new ConversationHandler(soc);
			handler.start();
		}
		
	}	
}

class ConversationHandler extends Thread
{
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	String name;
	
	public ConversationHandler(Socket socket) throws IOException
	{
		
		this.socket = socket;
	}
	
	public void run()
	{
		try
		{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			int count = 0;
			while(true)
			{
				if(count>0)
				{		
					out.println("NAME ALREADY EXISTS");
				}
				else
				{
					out.println("NAME REQUIRED");
				}
				
				name = in.readLine();
				
				if(name == null)
					return;
				
				if(!ChatServer.userNames.contains(name))
				{
					ChatServer.userNames.add(name);
					break;
				}
				count++;
			}
			
			out.println("NAME ACCEPTED");
			ChatServer.printWriter.add(out);
			
			while(true)
			{
				String message = in.readLine();
				
				if(message == null)
					return;
				for(PrintWriter writer : ChatServer.printWriter)
					writer.println(name + ":" + message);	
			}
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}	
	}
}
