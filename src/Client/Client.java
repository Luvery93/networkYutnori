package Client;

import javax.swing.*;

import java.net.*;
import java.util.StringTokenizer;
import java.io.*;

import Yutnori.Board;

public class Client {
	Board main; // Board Class - Client Class 는 1:1 Mapping 식으로 되어있습니다.
				    // TCP/IP 채팅을 구현하기 위해 main의 변수들을 Mapping합니다.
	JTextField login_id; 
	JTextField chat_in;
	JTextArea chat_out;
	BufferedReader br;
	PrintWriter pw;
	Socket sock;
	ServerThread st = null;
	String id;
	String password;
	String[] info;
	String line, check, count;
	int mem_count;
	boolean check_select_flag=false;
	
	public Client(String ip, Board main){
		super();
		this.main = main;
		login_id = main.getLoginId();
		chat_in = main.getChatIn();
		chat_out = main.getChatOut();

		try {
			sock = new Socket(ip, 10002);
			pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

		} catch (Exception ex) {
			System.out.println(ex);
		}

	}
	
	public boolean Login(String id, String password) throws IOException{
		this.id = id;
		this.password = password;
		main.setBoundsTitleToZero();
		pw.println(id.trim() + "\t" + password.trim());
		pw.flush();
//		System.out.println(id.trim() + password.trim());
		line = br.readLine();
		info = line.split("\t");
		check = info[0];
		count = info[1];
		if(!(count.equals(" "))){			
			try {
				mem_count = Integer.parseInt(count);
				main.setMember(mem_count);
			} catch (Exception e) { }
			main.setMember(mem_count);
		}
		if(check.equals("true")){			
			st = new ServerThread(sock, br);
			st.start();
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "로그인 정보가 잘못되었습니다. 게임을 종료합니다.\n게임을 원하시면 재시작하십시오." , "알림",
					JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
	}
	public void Chat(String msg){
		pw.println(msg);
		pw.flush();
		if (msg.equals("/quit")) {
			try {
				sock.close();
			} catch (Exception ex) { }
			System.exit(1);
		}
	}
	public void GameStartUpdate(){
		String query = "[GameStartUpdate]\t";
		query += "doit\t";
		pw.println(query);
		pw.flush();
	}
	public void CharacterSelectStateUpdate(){
		String query =  "[Select_Character_flag]\t";
		query += main.IsSelectCharacter1()+"\t";
		query += main.IsSelectCharacter2()+"\t";
		query += main.IsSelectCharacter3()+"\t";
		query += main.IsSelectCharacter4()+"\t";
		query += main.IsGameStart1()+"\t";
		query += main.IsGameStart2()+"\t";
		query += main.IsGameStart3()+"\t";
		query += main.IsGameStart4()+"\t";
		
		query += main.getCharacter()+"\t";
		query += main.getMember()+"\t";
		pw.println(query);
		pw.flush();
	}
	public void HorseAndTurnUpdate(){
		String query = "[HorseAndTurnUpdate]\t";
		query += main.getHorse_1()[0]+"\t";
		query += main.getHorse_1()[1]+"\t";
		query += main.getHorse_1()[2]+"\t";
		query += main.getHorse_1()[3]+"\t";
		query += main.getHorse_2()[0]+"\t";
		query += main.getHorse_2()[1]+"\t";
		query += main.getHorse_2()[2]+"\t";
		query += main.getHorse_2()[3]+"\t";
		query += main.getHorse_3()[0]+"\t";
		query += main.getHorse_3()[1]+"\t";
		query += main.getHorse_3()[2]+"\t";
		query += main.getHorse_3()[3]+"\t";
		query += main.getHorse_4()[0]+"\t";
		query += main.getHorse_4()[1]+"\t";
		query += main.getHorse_4()[2]+"\t";
		query += main.getHorse_4()[3]+"\t";
		query += main.getTurn()+"\t";
		pw.println(query);
		pw.flush();
	}
	public String getId(){
		return this.id;
	}
	// chat Thread
	class ServerThread extends Thread {
		private Socket sock = null;
		private BufferedReader br = null;

		public ServerThread(Socket sock, BufferedReader br) {
			this.sock = sock;
			this.br = br;
		}

		public void run() {
			try {
				String line = null;
				
				while ((line = br.readLine())!=null) {
					StringTokenizer linest = new StringTokenizer(line, "\t");
					if( linest.countTokens() == 1){
						chat_out.append(line + "\n");
					}else{
						String linestqueryflag = linest.nextToken();
						if(linestqueryflag.equals("[Select_Character_flag]")){
							if(linest.nextToken().equals("false"))
								main.setSelectCharacter1(false);
							else
								main.setSelectCharacter1(true);
							if(linest.nextToken().equals("false"))
								main.setSelectCharacter2(false);
							else
								main.setSelectCharacter2(true);
							if(linest.nextToken().equals("false"))
								main.setSelectCharacter3(false);
							else
								main.setSelectCharacter3(true);
							if(linest.nextToken().equals("false"))
								main.setSelectCharacter4(false);
							else
								main.setSelectCharacter4(true);
				
							if( linest.nextToken().equals("false"))
								main.setGameStart1(false);
							else 
								main.setGameStart1(true);

							if( linest.nextToken().equals("false"))
								main.setGameStart2(false);
							else 
								main.setGameStart2(true);

							if( linest.nextToken().equals("false"))
								main.setGameStart3(false);
							else 
								main.setGameStart3(true);

							if( linest.nextToken().equals("false"))
								main.setGameStart4(false);
							else 
								main.setGameStart4(true);
							
							int characternum = Integer.parseInt(linest.nextToken());;
							String characterflag = linest.nextToken();
							if( characterflag.equals("0"))
								main.setCharacter1(characternum);
							if( characterflag.equals("1"))
								main.setCharacter2(characternum);
							if( characterflag.equals("2"))
								main.setCharacter3(characternum);
							if( characterflag.equals("3"))
								main.setCharacter4(characternum);
							
						}
						if(linestqueryflag.equals("[GameStartUpdate]")){
							main.setGameStart(true);
						}
						if(linestqueryflag.equals("[HorseAndTurnUpdate]")){					
							main.setHorse_1(new int[]{Integer.parseInt(linest.nextToken()),
									Integer.parseInt(linest.nextToken()),
									Integer.parseInt(linest.nextToken()),
									Integer.parseInt(linest.nextToken())});
							main.setHorse_2(new int[]{Integer.parseInt(linest.nextToken()),
									Integer.parseInt(linest.nextToken()),
									Integer.parseInt(linest.nextToken()),
									Integer.parseInt(linest.nextToken())});	
							main.setHorse_3(new int[]{Integer.parseInt(linest.nextToken()),
									Integer.parseInt(linest.nextToken()),
									Integer.parseInt(linest.nextToken()),
									Integer.parseInt(linest.nextToken())});
							main.setHorse_4(new int[]{Integer.parseInt(linest.nextToken()),
									Integer.parseInt(linest.nextToken()),
									Integer.parseInt(linest.nextToken()),
									Integer.parseInt(linest.nextToken())});
							main.setTurn(Integer.parseInt(linest.nextToken()));
						}
					}
				}
			} catch (Exception ex) {
				System.out.println(ex);
			} finally {
				main.memeberEXIT();
				try { if (br != null) br.close(); } catch (Exception ex) { }
				try { if (sock != null) sock.close(); } catch (Exception ex) { }
			}
		}
		
	}

}
