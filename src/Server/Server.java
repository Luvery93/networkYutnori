package Server;

import java.awt.*;

import javax.swing.*;

import Client.Client;

import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.StringTokenizer;

public class Server extends JFrame {
	
	private static final long serialVersionUID = -4969984843508461676L;
	Container ct;
	JTextArea display;
	JScrollPane scroll;
	ServerSocket serversock;
	Socket sock;
	HashMap<String, PrintWriter> hm;
	Vector <String> clist;
	BufferedReader br;
	
	Server() {
		super();
		// JFrame 위에 JTextArea를 올림.
		ct = this.getContentPane();
		ct.setLayout(null);
		display = new JTextArea();
		display.setEditable(false);
		scroll = new JScrollPane(display);
		scroll.setBounds(0, 0, 480, 480);
		ct.add(scroll);
		hm = new HashMap<String, PrintWriter>();
		clist = new Vector<String>();
		
		setTitle("Server Yut");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setResizable(true);
		setVisible(true);

		try {
			serversock = new ServerSocket(10002);
			display.append(" 대기자를 기다리는 중입니다 ... \n");
			while (true) {
				Socket sock = serversock.accept();
				ClientThread ct = new ClientThread(sock, hm);
				ct.start();
			}
		} catch (Exception ex) {
			System.out.println(ex);
			System.exit(1);
		}
	}

	// Server의 실행
	public static void main(String[] args) {
		new Server();
	}

	// Thread 구현
	class ClientThread extends Thread {
		Socket sock;
		String info[];
		String id, password;
		BufferedReader br;
		HashMap<String, PrintWriter> hm = new HashMap<String, PrintWriter>();
		boolean initFlag = false;
		boolean selectflag = false;
		String line = null;
		String check = null;
		
		ClientThread(Socket sock, HashMap<String, PrintWriter> hm) {

			this.sock = sock;
			this.hm = hm;
			try {
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(
						sock.getOutputStream()));
				br = new BufferedReader(new InputStreamReader(
						sock.getInputStream()));
				line = br.readLine();
				info = line.split("\t");
				id = info[0];
				password = info[1];
				check = checkUserInfo(id, password);
				if (check.equals("true")) {
					broadcast(id + "님이 접속했습니다.");
					display.append("접속한 사용자의 아이디는 " + id + "입니다.");
					display.append("\n");

					synchronized (hm) {
						hm.put(this.id, pw);
					}

					clist.addElement(id);

					pw.println("true" + "\t" + (clist.size() - 1));
					pw.flush();
					initFlag = true;
				} else {
					pw.println("false" + "\t" + " ");
					pw.flush();
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}

		public void run() {
			try {
				String line = null;
				while ((line = br.readLine()) != null) {
					if (line.equals("/quit"))
						break;
	
					StringTokenizer linest = new StringTokenizer(line, "\t");
					if( linest.countTokens() == 1){
						if (line.indexOf("/to") == 0) {
							sendmsg(line);
						} else {
							broadcast(id + " : " + line);
						}
					} else {
						String linestqueryflag = linest.nextToken();
						if(linestqueryflag.equals("[Select_Character_flag]"))
							CheckCharacterSelectFlag(linest);
						if(linestqueryflag.equals("[GameStartUpdate]"))
							SetGameStart();
						if(linestqueryflag.equals("[HorseAndTurnUpdate]"))
							SetHorse(linest);
					}
					// JTextArea의 스크롤이 자동으로 아래로 맞춰지도록 함.
					if( display.getDocument().getLength() > 1)
						display.setCaretPosition(display.getDocument().getLength()-1);
				}
				
			} catch (Exception e) {
				System.out.println(e);

			} finally {
				synchronized (hm) {
					hm.remove(id);
					clist.removeElement(id);
				}
				broadcast(id + " 님이 접속 종료했습니다.");
				display.append(id + " 님이 접속 종료했습니다. ");
				display.append("\n");
				
				try { if (sock != null) sock.close();
				} catch (Exception ex) {
				}
			}
		}

		public void sendmsg(String msg) {
			int start = msg.indexOf(" ") + 1;
			int end = msg.indexOf(" ", start);

			if (end != -1) {
				String to = msg.substring(start, end);
				String msg2 = msg.substring(end + 1);
				Object obj = hm.get(to);
				if (obj != null) {
					PrintWriter pw = (PrintWriter) obj;
					pw.println(id + "님이 다음의 귓속말을 보내셨습니다. :" + msg2);
					pw.flush();
				}
			}
		}
		public void CheckCharacterSelectFlag(StringTokenizer st){
				synchronized(this){
					String request = "[Select_Character_flag]\t";
					while (st.hasMoreTokens()){
						request +=st.nextToken()+"\t";
					}
					try {
						for(int i =0; i<clist.size(); i++ ){
							PrintWriter pw = hm.get(clist.get(i));
							pw.println(request);
							pw.flush();
						}
					} catch (Exception e) {	}	
				}
		}
		public void SetGameStart(){
			synchronized(this){
				String request = "[GameStartUpdate]\t";
				request += "doit\t";
				try {
					for(int i =0; i<clist.size(); i++ ){
						synchronized(this){
							PrintWriter pw = hm.get(clist.get(i));
							pw.println(request);
							pw.flush();
						}
					}
				} catch (Exception e) {	}	
			}
		}
		public void SetHorse(StringTokenizer st){
			synchronized(this){
				String request = "[HorseAndTurnUpdate]\t";
				while (st.hasMoreTokens()){
					String plus = st.nextToken();
					request += plus+"\t";
				}
				try {
					for(int i =0; i<clist.size(); i++ ){
						PrintWriter pw = hm.get(clist.get(i));
						pw.println(request);
						pw.flush();
					}
				} catch (Exception e) {	}	
			}
		}
		public void broadcast(String msg) {
			synchronized (hm) {
				Collection<PrintWriter> collection = hm.values();
				Iterator<PrintWriter> iter = collection.iterator();
				while (iter.hasNext()) {
					PrintWriter pw = (PrintWriter) iter.next();
					pw.println(msg);
					pw.flush();
				}
			}
		}
	}

	public String checkUserInfo(String inputId, String inputPassword)
			throws SQLException {

		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		int realId = 0;
		String searchPassword = null;
		String check = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("드라이버 로딩 실패");
		}
		try {

			String jdbcUrl = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
			String userId = "class_b";
			String userPass = "delab";
			conn = DriverManager.getConnection(jdbcUrl, userId, userPass);

			String sql = "select count(*) from YUTMEMBER where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, inputId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				realId = rs.getInt("count(*)");
			}

			if (realId == 1) {
				String sql2 = "select password from YUTMEMBER where id=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, inputId);
				rs2 = pstmt2.executeQuery();

				while (rs2.next()) {
					searchPassword = rs2.getString("password");
				}

				if (!(searchPassword.equals(inputPassword))) {
					check = "false";
					return check;
				}
			} else {
				check = "false";
				return check;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (rs2 != null) {
				rs2.close();
			}
			if (pstmt2 != null) {
				pstmt2.close();
			}
			conn.close();
		}
		check = "true";
		return check;
	}
}
