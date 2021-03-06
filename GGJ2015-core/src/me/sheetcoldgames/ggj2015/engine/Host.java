package me.sheetcoldgames.ggj2015.engine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import me.sheetcoldgames.ggj2015.Constants;

public class Host {

	private static final int PORT = 30480;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private BufferedReader bufferedReader;
	// private String inputLine;
	private int clientCmd;
	private String clientStatus;
	private String hostStatus;
	private InetAddress hostAddress;
	private PrintWriter printWriter;
	private String[] clientHash = { "0",
			String.valueOf(Constants.L0_ROBOT_INIT_POS.x),
			String.valueOf(Constants.L0_ROBOT_INIT_POS.y), "0", "0" };

	public Host(String ip) {
		try {
			if (ip == null){
				hostAddress = InetAddress.getLocalHost();
			} else{
				hostAddress = InetAddress.getByName(ip);//getLocalHost();
			}
		} catch (UnknownHostException e) {
			System.out.println("Cold not get the host address.");
			return;
		}

		System.out.println("Server host address is: " + hostAddress);

		try {
			serverSocket = new ServerSocket(PORT, 0, hostAddress);
		} catch (IOException e) {
			System.out.println("Could not open server socket");
			return;
		}

		// System.out.println("Socket "+serverSocket+ "created.");
	}

	public InetAddress getAddr() {
		return hostAddress;
	}

	public boolean clientConnected() {
		try {
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			System.out.println("Could not get a client");
			return false;
		}

		System.out.println("Client " + clientSocket + " has connected.");
		return true;
	}

	public void sendToClient(String[] hostHash) { // TODO:overload with
													// serializable
		try {
			hostStatus = String.join("/", hostHash);
			printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
			printWriter.println(hostStatus);
			// System.out.println("sent: "+hostStatus);
			printWriter = null;

			// printWriter.println(cmd);
			// printWriter = null;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void sendObjectToClient(ArrayList<Entity> obj) {
		try {
			ObjectOutputStream writer = new ObjectOutputStream(
					new BufferedOutputStream(clientSocket.getOutputStream()));
			writer.writeObject(obj);
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getFromClient() {
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			clientCmd = Integer.parseInt(bufferedReader.readLine());
			System.out.println("received: " + clientCmd);
			return clientCmd;
		} catch (IOException e) {
			System.out.println(e);
			return 99999;// connection lost
		}
	}

	InputStream is;

	public String[] getFromClientSTR() {
		try {
			is = clientSocket.getInputStream();
			bufferedReader = new BufferedReader(new InputStreamReader(is));
			if (bufferedReader.ready()) {
				clientStatus = bufferedReader.readLine();
				//System.out.println("received: " + clientStatus);
				clientHash = clientStatus.split("/");
			}

			return clientHash;

		} catch (IOException e) {
			System.out.println(e);
			return clientHash;// connection lost
		}
	}
	Entity robot;
	public Entity getObjFromClient(){
		
		try {
			System.out.println("teste");
			ObjectInputStream reader = new ObjectInputStream( new BufferedInputStream(clientSocket.getInputStream()));
			Object obj = reader.readObject();
			robot = (Entity) obj;
			
			return robot;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return robot;
		}
		
	}

}
