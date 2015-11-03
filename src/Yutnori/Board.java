package Yutnori;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import Client.*;

public class Board extends JFrame  implements Runnable{

	private static final long serialVersionUID = -8613215520288461738L;
	public static enum EScreenState{
		TITLE, LOBBY, INGAME, GAMESTART,EXIT
	}
	GameScreen Gs; // ���� ȭ�鿡 �̹����� �׸��� ��ü
	int gScreenWidth = 806;// ���� ȭ�� �ʺ�
	int gScreenHeight = 628;// ���� ȭ�� ����
	EScreenState screenState;// board�� ����
	Thread mainwork;// ������ ��ü
	boolean roof = true;// ������ ���� ����
	int cnt;// ���� ����� ��Ʈ�� ����
	Container ct; // ä���� �����ϱ� ���� Container
	JTextField chat_in; // String �Է��� �ޱ� ���� JTextField ��ü
	JTextArea chat_out; // �Է¹��� String�� �����ִ� JTextArea ��ü
	JScrollPane scroll; // JTextArea ��ü�� ��ũ���� �߰����ִ� JScrollPane ��ü
	JButton title_enter_button; // title���� �����ϱ� ���� JButton ��ü
	JButton title_join_button; // title���� ȸ�������� ���� JButton ��ü
	
	String id; // �α��� ���̵�
	String password; // �α��� �н�����
	String ip; // ������ ip�ּ�
	int port; // ������ port ��ȣ
	int member; // player�� ��ȣ
	int manager; // ���� player�� ��ȣ
	JTextField login_id; // �α��� ���̵� �Է¹��� JTextField ��ü
	JTextField login_password;
	Client cs; // Board Class - Client Class �� 1:1 Mapping ������ �Ǿ��ֽ��ϴ�.

	// �������
	JButton character_select_button_1;
	JButton character_select_button_2;
	JButton character_select_button_3;
	JButton character_select_button_4;
	JButton character_select_button_startAndready;
	ImageIcon character_select_1;
	ImageIcon character_select_2;
	ImageIcon character_select_3;
	ImageIcon character_select_4;
	ImageIcon character_unselect_1;
	ImageIcon character_unselect_2;
	ImageIcon character_unselect_3;
	ImageIcon character_unselect_4;
	ImageIcon start_button;
	ImageIcon ready_button;
	boolean gamestart;
	boolean gamestart_1;
	boolean gamestart_2;
	boolean gamestart_3;
	boolean gamestart_4;
	boolean character_select_flag;
	boolean character_select_flag_1;
	boolean character_select_flag_2;
	boolean character_select_flag_3;
	boolean character_select_flag_4;
	boolean readyflag;
	boolean Allreadyflag;
	int my_character;
	int character_1;
	int character_2;
	int character_3;
	int character_4;

	// ������� ���� �ɸ��͸� ���� ���� ������
	
	// ���� ���ǿ� ������ ���ƺ��� ..
	int[] horse_1;
	int[] horse_2;
	int[] horse_3;
	int[] horse_4;
	int turn;
	int run;
	JButton yut_throw;
	Random yut_random;
	JButton go_1;
	JButton go_2;
	JButton go_3;
	JButton go_4;
	boolean[] goflag_1;
	boolean[] goflag_2;
	boolean[] goflag_3;
	boolean[] goflag_4;
	Board(){
		super();
		
		// argument ó��
		// Container ���� �� Layout ����
		ct = this.getContentPane();
		ct.setLayout(null);

		
		// JTextField ����
		chat_in = new JTextField();
		// EScreenState�� INGAME�� ��쿡�� ��ġ ����
		chat_in.setBounds(0,0,0,0);
		
		// JTextArea ����
		chat_out = new JTextArea();
		// JTextArea�� ���� �Ұ���
		chat_out.setEditable(false);
		
		// JTextArea�� Scroll ����.
		scroll = new JScrollPane(chat_out);
		// EScreenState�� INGAME�� ��쿡�� ��ġ ����
		scroll.setBounds(0,0,0,0);
	
		// JButton ����
		title_enter_button = new JButton(new ImageIcon("./rsc/title/enter.png"));
		// Border ����
		title_enter_button.setBorderPainted(false);	
		// EScreenState �� TITLE�� ��쿡�� ��ġ����
		title_enter_button.setBounds(0,0,0,0);

		// JButton ����
		title_join_button = new JButton(new ImageIcon("./rsc/title/join.png"));
		// Border ����
		title_join_button.setBorderPainted(false);
		// EScreenState�� TITLE�� ��쿡�� ��ġ ����
		title_join_button.setBounds(0, 0,	0, 0);
		
		// JTextField ����
		login_id = new JTextField();
		login_id.setBounds(0,0,0,0);
		login_id.setBorder(null);
		login_password = new JTextField();
		login_password.setBounds(0,0,0,0);
		login_password.setBorder(null);
		
		// ImageIcone ����
		character_select_1 = new ImageIcon("./rsc/ingame/character_select_1.png");
		character_select_2 = new ImageIcon("./rsc/ingame/character_select_2.png");
		character_select_3 = new ImageIcon("./rsc/ingame/character_select_3.png");
		character_select_4 = new ImageIcon("./rsc/ingame/character_select_4.png");
		character_unselect_1 = new ImageIcon("./rsc/ingame/character_unselect_1.png");
		character_unselect_2 = new ImageIcon("./rsc/ingame/character_unselect_2.png");
		character_unselect_3 = new ImageIcon("./rsc/ingame/character_unselect_3.png");
		character_unselect_4 = new ImageIcon("./rsc/ingame/character_unselect_4.png");
		start_button = new ImageIcon("./rsc/ingame/start_button.png");
		ready_button = new ImageIcon("./rsc/ingame/ready_button.png");
		
		// JButton ����
		character_select_button_1 = new JButton(character_unselect_1);
		character_select_button_2 = new JButton(character_unselect_2);
		character_select_button_3 = new JButton(character_unselect_3);
		character_select_button_4 = new JButton(character_unselect_4);
		// Border ����
		character_select_button_1.setBorderPainted(false);
		character_select_button_2.setBorderPainted(false);
		character_select_button_3.setBorderPainted(false);
		character_select_button_4.setBorderPainted(false);
		// EScreenState�� INGAME�� ��쿡�� ��ġ ����
		character_select_button_1.setBounds(0, 0,	0, 0);
		character_select_button_2.setBounds(0, 0,	0, 0);
		character_select_button_3.setBounds(0, 0,	0, 0);
		character_select_button_4.setBounds(0, 0,	0, 0);
		
		// JButton ����
		character_select_button_startAndready = new JButton(start_button);
		// Border ����
		character_select_button_startAndready.setBorderPainted(false);
		// EScreenState�� INGAME �̰� gamestart �� false �ϰ�쿡�� ��ġ ����
		character_select_button_startAndready.setBounds(0,0,0,0);
		
		// JButton ����
		yut_throw = new JButton(new ImageIcon("./rsc/ingame/throw_yut.png"));
		go_1 = new JButton(new ImageIcon("./rsc/gamestart/1.png"));
		go_2 = new JButton(new ImageIcon("./rsc/gamestart/2.png"));
		go_3 = new JButton(new ImageIcon("./rsc/gamestart/3.png"));
		go_4 = new JButton(new ImageIcon("./rsc/gamestart/4.png"));

		// Border ����
		yut_throw.setBorderPainted(false);
		go_1.setBorderPainted(false);
		go_2.setBorderPainted(false);
		go_3.setBorderPainted(false);
		go_4.setBorderPainted(false);
		// EScreenState�� GameStart�϶��� ��ġ ����
		yut_throw.setBounds(0,0,0,0);
		go_1.setBounds(0,0,0,0);
		go_2.setBounds(0,0,0,0);
		go_3.setBounds(0,0,0,0);
		go_4.setBounds(0,0,0,0);
		
		// EventListener ����
		chat_in.addActionListener(new ChattingInGameListner());
		title_enter_button.addActionListener(new EnterOfTitleListener());
		character_select_button_1.addActionListener(new CharacterSelectInGameListener());
		character_select_button_2.addActionListener(new CharacterSelectInGameListener());
		character_select_button_3.addActionListener(new CharacterSelectInGameListener());
		character_select_button_4.addActionListener(new CharacterSelectInGameListener());
		character_select_button_startAndready.addActionListener(new SetCharacterInGameListener());
		yut_throw.addActionListener(new ThrowYutListener());
		go_1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				setGoButtonToZero();
				setGoButton1();
				Gs.repaint();
		}});
		go_2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				setGoButtonToZero();
				setGoButton2();
				Gs.repaint();
		}});
		go_3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				setGoButtonToZero();
				setGoButton3();
				Gs.repaint();
		}});
		go_4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				setGoButtonToZero();
				setGoButton4();
				Gs.repaint();
		}});
		// Container�� �߰�
		ct.add(go_4);
		ct.add(go_3);
		ct.add(go_2);
		ct.add(go_1);
		ct.add(yut_throw);
		ct.add(character_select_button_startAndready);
		ct.add(character_select_button_1);
		ct.add(character_select_button_2);
		ct.add(character_select_button_3);
		ct.add(character_select_button_4);
		ct.add(title_enter_button);
		ct.add(title_join_button);
		ct.add(chat_in);
		ct.add(scroll);
		ct.add(login_id);
		ct.add(login_password);
		
		setTitle("Yunori Final"); // Ÿ��Ʋ ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 'X'��ư�� ������ �� ������ ��
		setBounds(100,100,gScreenWidth, gScreenHeight);	// ũ�⸦ 800x600���� ��
		setResizable(false);	// ũ�⸦ ������ �� ������ ��
		setLocationRelativeTo(null);//�������� ��Ÿ�� ��ġ�� ����
		setVisible(true);//�������� ���̰� ��
		
		Gs = new GameScreen(this);// ȭ�� ��ȭ�� ���� ĵ���� ��ü
		Gs.setBounds(0, 0, gScreenWidth, gScreenHeight); // ĵ���� ��ġ ����
		add(Gs);// Canvas ��ü�� �����ӿ� �ø���

		initialize();
		systeminit();
	}
	// ���α׷� �ʱ�ȭ
	public void systeminit() {
		screenState = EScreenState.TITLE;
		cnt = 0;
		ip = "127.0.0.1";
		port = 10002;
		manager = 0;
		member = 0;
		my_character = 99;
		character_1 = 99;
		character_2 = 99;
		character_3 = 3;
		character_4 = 4;
		gamestart = false;
		gamestart_1 = false;
		gamestart_2 = false;
		gamestart_3 = false;
		gamestart_4 = false;
		character_select_flag = false;
		character_select_flag_1 = false;
		character_select_flag_2 = false;
		character_select_flag_3 = false;
		character_select_flag_4 = false;
		readyflag = false;
		Allreadyflag = false;
		horse_1 = new int[]{0,0,0,0};
		horse_2 = new int[]{0,0,0,0};
		horse_3 = new int[]{0,0,0,0};
		horse_4 = new int[]{0,0,0,0};
		yut_random = new Random();
		turn = 0;
		run = 0;
		goflag_1 = new boolean[]{false, false, false, false };
		goflag_2 = new boolean[]{false, false, false, false };
		goflag_3 = new boolean[]{false, false, false, false };
		goflag_4 = new boolean[]{false, false, false, false };
		cs = new Client(ip, this);
		mainwork = new Thread(this);
		mainwork.start();
	}
	// ���� �ʱ�ȭ
	public void initialize(){
		init_Title();
		init_Game();
		init_InGame();
		Gs.repaint();
	}
	
	public void init_Title(){
		Gs.title = makeImage("./rsc/title/title.png");
	}
	public void init_Game(){
		Gs.bg = makeImage("./rsc/ingame/bg.png");
		Gs.exit = makeImage("./rsc/ingame/exit.png");
		Gs.throw_yut = makeImage("./rsc/ingame/throw_yut.png");
		Gs.manager = makeImage("./rsc/ingame/manager.png");
		Gs.manager_sign = makeImage("./rsc/ingame/manager_sign.png");
		Gs.ready = makeImage("./rsc/ingame/ready.png");
		Gs.character_select_form = makeImage("./rsc/ingame/character_select_form.png");
		
		// ���� �������Ŀ� UI�� ��Ÿ���� ���� Image ����
		Gs.character_1_remain_token_0 = makeImage("./rsc/ingame/character_token_1/character_remain_token_0.png");
		Gs.character_1_remain_token_1 = makeImage("./rsc/ingame/character_token_1/character_remain_token_1.png");
		Gs.character_1_remain_token_2 = makeImage("./rsc/ingame/character_token_1/character_remain_token_2.png");
		Gs.character_1_remain_token_3 = makeImage("./rsc/ingame/character_token_1/character_remain_token_3.png");
		Gs.character_1_remain_token_4 = makeImage("./rsc/ingame/character_token_1/character_remain_token_4.png");

		Gs.character_2_remain_token_0 = makeImage("./rsc/ingame/character_token_2/character_remain_token_0.png");
		Gs.character_2_remain_token_1 = makeImage("./rsc/ingame/character_token_2/character_remain_token_1.png");
		Gs.character_2_remain_token_2 = makeImage("./rsc/ingame/character_token_2/character_remain_token_2.png");
		Gs.character_2_remain_token_3 = makeImage("./rsc/ingame/character_token_2/character_remain_token_3.png");
		Gs.character_2_remain_token_4 = makeImage("./rsc/ingame/character_token_2/character_remain_token_4.png");
		
		Gs.character_3_remain_token_0 = makeImage("./rsc/ingame/character_token_3/character_remain_token_0.png");
		Gs.character_3_remain_token_1 = makeImage("./rsc/ingame/character_token_3/character_remain_token_1.png");
		Gs.character_3_remain_token_2 = makeImage("./rsc/ingame/character_token_3/character_remain_token_2.png");
		Gs.character_3_remain_token_3 = makeImage("./rsc/ingame/character_token_3/character_remain_token_3.png");
		Gs.character_3_remain_token_4 = makeImage("./rsc/ingame/character_token_3/character_remain_token_4.png");
		
		Gs.character_4_remain_token_0 = makeImage("./rsc/ingame/character_token_4/character_remain_token_0.png");
		Gs.character_4_remain_token_1 = makeImage("./rsc/ingame/character_token_4/character_remain_token_1.png");
		Gs.character_4_remain_token_2 = makeImage("./rsc/ingame/character_token_4/character_remain_token_2.png");
		Gs.character_4_remain_token_3 = makeImage("./rsc/ingame/character_token_4/character_remain_token_3.png");
		Gs.character_4_remain_token_4 = makeImage("./rsc/ingame/character_token_4/character_remain_token_4.png");
		
		Gs.character_1_token_1 = makeImage("./rsc/ingame/character_token_1/character_token_1.png");
		Gs.character_1_token_2 = makeImage("./rsc/ingame/character_token_1/character_token_2.png");
		Gs.character_1_token_3 = makeImage("./rsc/ingame/character_token_1/character_token_3.png");
		Gs.character_1_token_4 = makeImage("./rsc/ingame/character_token_1/character_token_4.png");
		
		Gs.character_2_token_1 = makeImage("./rsc/ingame/character_token_2/character_token_1.png");
		Gs.character_2_token_2 = makeImage("./rsc/ingame/character_token_2/character_token_2.png");
		Gs.character_2_token_3 = makeImage("./rsc/ingame/character_token_2/character_token_3.png");
		Gs.character_2_token_4 = makeImage("./rsc/ingame/character_token_2/character_token_4.png");
		
		Gs.character_3_token_1 = makeImage("./rsc/ingame/character_token_3/character_token_1.png");
		Gs.character_3_token_2 = makeImage("./rsc/ingame/character_token_3/character_token_2.png");
		Gs.character_3_token_3 = makeImage("./rsc/ingame/character_token_3/character_token_3.png");
		Gs.character_3_token_4 = makeImage("./rsc/ingame/character_token_3/character_token_4.png");
		
		Gs.character_4_token_1 = makeImage("./rsc/ingame/character_token_4/character_token_1.png");
		Gs.character_4_token_2 = makeImage("./rsc/ingame/character_token_4/character_token_2.png");
		Gs.character_4_token_3 = makeImage("./rsc/ingame/character_token_4/character_token_3.png");
		Gs.character_4_token_4 = makeImage("./rsc/ingame/character_token_4/character_token_4.png");
		
		Gs.member_profile_1 = makeImage("./rsc/ingame/member_profile_1.png");
		Gs.member_profile_2 = makeImage("./rsc/ingame/member_profile_2.png");
		Gs.member_profile_3 = makeImage("./rsc/ingame/member_profile_3.png");
		Gs.member_profile_4 = makeImage("./rsc/ingame/member_profile_4.png");
	}
	public void init_InGame(){
		Gs.y_do = makeImage("./rsc/gamestart/do.png");
		Gs.y_gae = makeImage("./rsc/gamestart/gae.png");
		Gs.y_gul = makeImage("./rsc/gamestart/gul.png");
		Gs.y_yut = makeImage("./rsc/gamestart/yut.png");
		Gs.y_mo = makeImage("./rsc/gamestart/mo.png");
		Gs.y_bdo = makeImage("./rsc/gamestart/backdo.png");
	}
	// ������ ��Ʈ
	public void run() {
		try {
			while (roof) {


				Gs.repaint();// ȭ�� ������Ʈ
				process();

					
				if( screenState == EScreenState.INGAME && !readyflag){
					cnt++;
					if( cnt == 10000 ){
						cs.CharacterSelectStateUpdate();
						cnt = 0;
					}	
				}
					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public void process(){
		switch(screenState){
			case TITLE:
				process_Title();
				break;
			case INGAME:
				process_Game();
				break;
			case GAMESTART:
				process_GameStart();
				break;
			case EXIT:
				// ������ ����Ǹ� �ٲ���
				System.exit(0);
			default:
				break;
		}
	}
	public void process_Title(){
		// �ش� �Լ��� Enter�� Join JButton�� ������ �Լ��Դϴ�.
		// title_enter_button, title_join_button�� ��ġ�� ���� ����.
		setBoundsTitleToSet();
	}
	public void process_Game(){
		setBoundsTitleToZero();
		// �ش� �Լ��� ä���� ���� Container ct�� �����ִ� ������ ����
		// chat_in �� scroll�� ��ġ�� ���� ����. ( ct�� Layout�� null�� �Ͽ��� )
		
		// JTextArea�� ��ũ���� �ڵ����� �Ʒ��� ���������� ��.
		if( chat_out.getDocument().getLength() > 1)
			chat_out.setCaretPosition(chat_out.getDocument().getLength()-1);
		
		chat_in.setBounds(555,565,245,30);
		scroll.setBounds(555,451,245,115);
		// INGAME ���¸� ��� �÷��̾ �غ� ��ģ ���� �׷��� ���� ���� ����
		if( (member==0 && !gamestart_1)
				|| (member==1 && !gamestart_2)
				|| (member==2 && !gamestart_3)
				|| (member==3 && !gamestart_4) ) {
			character_select_button_1.setBounds(208,266,70,70);
			character_select_button_2.setBounds(304,266,70,70);
			character_select_button_3.setBounds(402,266,70,70);
			character_select_button_4.setBounds(500,266,70,70);
			character_select_button_startAndready.setBounds(325,382,134,49);
			
			if( character_select_flag_1 ) character_select_button_1.setIcon(character_select_1);
			else character_select_button_1.setIcon(character_unselect_1);
			if( character_select_flag_2 ) character_select_button_2.setIcon(character_select_2);
			else character_select_button_2.setIcon(character_unselect_2);
			if( character_select_flag_3 ) character_select_button_3.setIcon(character_select_3);
			else character_select_button_3.setIcon(character_unselect_3);
			if( character_select_flag_4 ) character_select_button_4.setIcon(character_select_4);
			else character_select_button_4.setIcon(character_unselect_4);
			
		} else {
			setBoundsInGame_GetReady();
		}
		// ������ ��� �����ϱ� ��ư�� , �����̾ƴ� ��� �غ��ϱ� ��ư��
		if( member == manager ){
			character_select_button_startAndready.setIcon(start_button);
		}else{
			character_select_button_startAndready.setIcon(ready_button);
		}
		if( gamestart )
			screenState = EScreenState.GAMESTART;

	}
	public void process_GameStart(){
		// JTextArea�� ��ũ���� �ڵ����� �Ʒ��� ���������� ��.
		if( chat_out.getDocument().getLength() > 1)
			chat_out.setCaretPosition(chat_out.getDocument().getLength()-1);
		chat_in.setBounds(555,565,245,30);
		scroll.setBounds(555,451,245,115);
		yut_throw.setBounds(620,250,140,40);
		int count, i;
		for(i=0, count=0; i<4; i++)
			if(horse_1[i] == 99 ) count ++;
		if( count == 4) screenState = EScreenState.EXIT;
		for(i=0, count=0; i<4; i++)
			if(horse_2[i] == 99 ) count ++;
		if( count == 4) screenState = EScreenState.EXIT;
		for(i=0, count=0; i<4; i++)
			if(horse_3[i] == 99 ) count ++;
		if( count == 4) screenState = EScreenState.EXIT;
		for(i=0, count=0; i<4; i++)
			if(horse_4[i] == 99 ) count ++;
		if( count == 4) screenState = EScreenState.EXIT;
	}
	
	// Image �о���̴� �Լ�.
	public Image makeImage(String furl) {
		Image img;
		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.getImage(furl);
		try {
			// �������
			MediaTracker mt = new MediaTracker(this);
			mt.addImage(img, 0);
			mt.waitForID(0);
			// �������, getImage�� �о���� �̹����� �ε��� �Ϸ�ƴ��� Ȯ���ϴ� �κ�
		} catch (Exception ee) {
			ee.printStackTrace();
			return null;
		}
		return img;
	}
	
	// �̺�Ʈ ������ ó��
	// Client�� ���� ä���� ���� ������
	private class ChattingInGameListner implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			// JTextField�� ���� String�� ������.
			String text = chat_in.getText();
			cs.Chat(text);
			
			// JTextField�� �Է��� JTextArea�� ÷��. -> ����
			//chat_out.append(text+'\n');
		
			//chat_out.setCaretPosition(chat_out.getText().length()-1);
			
			// JTextField�� ���.
			chat_in.setText("");
		}
	}
	// TITLE���� String id �� ������ �α����� ���� ������
	private class  EnterOfTitleListener implements ActionListener{
		public void actionPerformed(ActionEvent evt){	
			boolean checkType = false;
			
			title_enter_button = (JButton) evt.getSource();
			if(title_enter_button.getText().equals("")){
			//	setBoundsTitleToZero();
				id = login_id.getText();
				password = login_password.getText();
				try {
					checkType = cs.Login(id, password);
					if(checkType == true){
						setBoundsTitleToZero();
						screenState=EScreenState.INGAME;
					}else{
						System.exit(0);			
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	// INGAME���� ������ �����ϱ����� �� �� ������ ���� ���� ������
	// ready���Ŀ��� ������ ���� ����
	private class CharacterSelectInGameListener implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			if( (member == 0 && !gamestart_1)
					|| (member == 1 && !gamestart_2)
					|| (member == 2 && !gamestart_3)
					|| (member == 3 && !gamestart_4) ){
				JButton click_button = (JButton) evt.getSource();
				// ó�� Ŭ��
				if ( my_character == 99 ){
					if( click_button.getIcon().equals(character_unselect_1)){
							my_character = 1;
							character_select_flag_1 = true;					
					}
					if( click_button.getIcon().equals(character_unselect_2)){	
							my_character = 2;
							character_select_flag_2 = true;
					}
					if( click_button.getIcon().equals(character_unselect_3)){
							my_character = 3;
							character_select_flag_3 = true;
					}
					if( click_button.getIcon().equals(character_unselect_4)){
							my_character = 4;
							character_select_flag_4 = true;
					}
				}
				// ������ Ŭ��
				if( click_button.getIcon().equals(character_unselect_1)){
						if(my_character == 2) character_select_flag_2 = false;
						if(my_character == 3) character_select_flag_3 = false;
						if(my_character == 4) character_select_flag_4 = false;
						character_select_flag_1 = true;
						my_character = 1;
				}
				
				if( click_button.getIcon().equals(character_unselect_2)){
						if(my_character == 1) character_select_flag_1 = false;
						if(my_character == 3) character_select_flag_3 = false;
						if(my_character == 4) character_select_flag_4 = false;
						character_select_flag_2 = true;
						my_character = 2;
				}
	
				if( click_button.getIcon().equals(character_unselect_3)){
						if(my_character == 1) character_select_flag_1 = false;
						if(my_character == 2) character_select_flag_2 = false;
						if(my_character == 4) character_select_flag_4 = false;
						character_select_flag_3 = true;
						my_character = 3;
				}
	
				if( click_button.getIcon().equals(character_unselect_4)){
						if(my_character == 1) character_select_flag_1 = false;
						if(my_character == 2) character_select_flag_2 = false;
						if(my_character == 3) character_select_flag_3 = false;
						character_select_flag_4 = true;
						my_character = 4;
				}
			
				if( click_button.getIcon().equals(character_select_1) && character_select_flag_1
						&& my_character == 1){
						character_select_flag_1 = false;
						my_character = 99;
				}
				if( click_button.getIcon().equals(character_select_2) && character_select_flag_2
						&& my_character == 2){
						character_select_flag_2 = false;
						my_character = 99;
				}
				if( click_button.getIcon().equals(character_select_3) && character_select_flag_3
						&& my_character == 3){
						character_select_flag_3 = false;
						my_character = 99;
				}
				if( click_button.getIcon().equals(character_select_4) && character_select_flag_4
						&& my_character == 4){
						character_select_flag_4 = false;
						my_character = 99;
				}
			}
			cs.CharacterSelectStateUpdate();
		}
	}
	// �� �������� ���� �Ϸ�ȵ�, ���� ���� ��ư�� ���������.
	// Client���� ������ ���� �� �� ���� �� �ϼ� ����.
	private class SetCharacterInGameListener implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			// ������ ���ΰ� ready�� ���¿����� ������ ������ �� �־�� �Ѵ�.
			// ����ڴ� �ɸ��͸� ���ڿ� �غ� �� �� �־�� �Ѵ�.
			// �غ� �����ϸ� �ɸ��͸� ������ �� ������ �ؾ� �Ѵ�.

			if( member == manager ){
				if( (manager == 0 && gamestart_2 && gamestart_3 && gamestart_4) 
						|| (manager == 1 && gamestart_1 && gamestart_3 && gamestart_4)
						|| (manager == 2 && gamestart_1 && gamestart_2 && gamestart_4)
						|| (manager == 3 && gamestart_1 && gamestart_2 && gamestart_3) ){
					if( ( my_character==1 && character_select_flag_1)
							|| ( my_character==2 && character_select_flag_2 )
							|| ( my_character==3 && character_select_flag_3 )
							|| ( my_character==4 && character_select_flag_4 ) ){
						if( manager == 0) gamestart_1 = true;
						if( manager == 1) gamestart_2 = true;
						if( manager == 2) gamestart_3 = true;
						if( manager == 3) gamestart_4 = true;
					}
					
				}
				if(  (gamestart_1 && gamestart_2 && gamestart_3 && gamestart_4) ){
					cs.Chat("������ �����մϴ�.");
					gamestart = true;
					setBoundsInGame_GetReady();
					cs.GameStartUpdate();
					Allreadyflag = true;
					
					screenState = EScreenState.GAMESTART;

				} else {
					cs.Chat("����ο��� �غ� �ؾ� �մϴ�.");
					cs.Chat(gamestart_1+" "+gamestart_2+" "+gamestart_3+" "+gamestart_4);
				}
			} else {
				if( ( my_character==1 && character_select_flag_1)
						|| ( my_character==2 && character_select_flag_2 )
						|| ( my_character==3 && character_select_flag_3 )
						|| ( my_character==4 && character_select_flag_4 ) ){
					if ( !gamestart_1 && member == 0 ){
						cs.Chat("����"+member+cs.getId()+"���� Ready�ϼ̽��ϴ�.");
						gamestart_1 = true;
						readyflag = true;
					}
					if ( !gamestart_2 && member == 1 ){
						cs.Chat("����"+member+cs.getId()+"���� Ready�ϼ̽��ϴ�.");
						gamestart_2 = true;
						readyflag = true;
					}
					if ( !gamestart_3 && member == 2 ){
						cs.Chat("����"+member+cs.getId()+"���� Ready�ϼ̽��ϴ�.");
						gamestart_3 = true;
						readyflag = true;
					}
					if ( !gamestart_4 && member == 3 ){
						cs.Chat("����"+member+cs.getId()+"���� Ready�ϼ̽��ϴ�.");
						gamestart_4 = true;
						readyflag = true;
					}
				}
				if( !readyflag && gamestart_1 && member == 0 ){
					cs.Chat("����"+member+cs.getId()+"���� ReadyǪ�̽��ϴ�.");
					gamestart_1 = false;
				}
				if( !readyflag && gamestart_2 && member == 1 ){
					cs.Chat("����"+member+cs.getId()+"���� ReadyǪ�̽��ϴ�.");
					gamestart_2 = false;
				}
				if( !readyflag && gamestart_3 && member == 2 ){
					cs.Chat("����"+member+cs.getId()+"���� ReadyǪ�̽��ϴ�.");
					gamestart_3 = false;
				}
				if( !readyflag && gamestart_4 && member == 3 ){
					cs.Chat("����"+member+cs.getId()+"���� ReadyǪ�̽��ϴ�.");
					gamestart_4 = false;
				}
			}
			if( !Allreadyflag )
				cs.CharacterSelectStateUpdate();
		}
	}
	// �� ������ ������
	private class ThrowYutListener implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			int random = yut_random.nextInt(16) +1;
			if( random <= 3 ) run = 1;
			else if( random <= 9 ) run = 2;
			else if( random <= 13 ) run = 3;
			else if( random <= 14 ) run = 4;
			else if( random <= 15 ) run = 5;
			else run = -1;
			if(member == 0 && turn == 0){
				int c_point1 = Checkpoint(horse_1[0], run);
				int c_point2 = Checkpoint(horse_1[1], run);
				int c_point3 = Checkpoint(horse_1[2], run);
				int c_point4 = Checkpoint(horse_1[3], run);
				// ������ġ������ ������ ����� ���
				// ��ȣ�� ���� ���� �켱������ ����Ѵ�
				// 1-2, 1-3, 1-4, 2-3, 2-4, 3-4 �� ������ �ȴ�.
				// �ش������� ������ ���� ���� ��� �׸��� 0���� ������.
				// ���� �ش������� �ڽ��� ���� ������� ��ģ��.
				if( c_point1 != 99 ) go_1.setBounds(Gs.horseXpos[c_point1]+15, Gs.horseYpos[c_point1]+30,19,19);
				else go_1.setBounds(Gs.horseXpos[0], Gs.horseYpos[0],19,19);
				if( c_point2 != 99 ) go_2.setBounds(Gs.horseXpos[c_point2]+15, Gs.horseYpos[c_point2]+30,19,19);
				else go_2.setBounds(Gs.horseXpos[0]+15, Gs.horseYpos[0]+30,19,19);
				if( c_point3 != 99 ) go_3.setBounds(Gs.horseXpos[c_point3]+15, Gs.horseYpos[c_point3]+30,19,19);
				else go_3.setBounds(Gs.horseXpos[0]+15, Gs.horseYpos[0]+30,19,19);
				if( c_point4 != 99 ) go_4.setBounds(Gs.horseXpos[c_point4]+15, Gs.horseYpos[c_point4]+30,19,19);
				else go_4.setBounds(Gs.horseXpos[0]+15, Gs.horseYpos[0]+30,19,19);
				if( c_point1 == c_point2 && !goflag_1[0] ) go_2.setBounds(0,0,0,0);
				if( c_point1 == c_point3 && !goflag_1[0] ) go_3.setBounds(0,0,0,0);
				if( c_point1 == c_point4 && !goflag_1[0] ) go_4.setBounds(0,0,0,0);
				if( c_point2 == c_point3 && !goflag_1[1] ) go_3.setBounds(0,0,0,0);
				if( c_point2 == c_point4 && !goflag_1[1] ) go_4.setBounds(0,0,0,0);
				if( c_point3 == c_point4 && !goflag_1[2] ) go_4.setBounds(0,0,0,0);
				if( goflag_1[0] ) go_1.setBounds(0,0,0,0);
				if( goflag_1[1] ) go_2.setBounds(0,0,0,0);
				if( goflag_1[2] ) go_3.setBounds(0,0,0,0);
				if( goflag_1[3] ) go_4.setBounds(0,0,0,0);
			}
			if(member == 1 && turn == 1 ){
				int c_point1 = Checkpoint(horse_2[0], run);
				int c_point2 = Checkpoint(horse_2[1], run);
				int c_point3 = Checkpoint(horse_2[2], run);
				int c_point4 = Checkpoint(horse_2[3], run);
				if( c_point1 != 99 ) go_1.setBounds(Gs.horseXpos[c_point1]+15, Gs.horseYpos[c_point1]+30,19,19);
				else go_1.setBounds(Gs.horseXpos[0]+15, Gs.horseYpos[0]+30,19,19);
				if( c_point2 != 99 ) go_2.setBounds(Gs.horseXpos[c_point2]+15, Gs.horseYpos[c_point2]+30,19,19);
				else go_2.setBounds(Gs.horseXpos[0]+15, Gs.horseYpos[0]+30,19,19);
				if( c_point3 != 99 ) go_3.setBounds(Gs.horseXpos[c_point3]+15, Gs.horseYpos[c_point3]+30,19,19);
				else go_3.setBounds(Gs.horseXpos[0]+15, Gs.horseYpos[0]+30,19,19);
				if( c_point4 != 99 ) go_4.setBounds(Gs.horseXpos[c_point4]+15, Gs.horseYpos[c_point4]+30,19,19);
				else go_4.setBounds(Gs.horseXpos[0]+15, Gs.horseYpos[0]+30,19,19);
				if( c_point1 == c_point2 && !goflag_2[0] ) go_2.setBounds(0,0,0,0);
				if( c_point1 == c_point3 && !goflag_2[0] ) go_3.setBounds(0,0,0,0);
				if( c_point1 == c_point4 && !goflag_2[0] ) go_4.setBounds(0,0,0,0);
				if( c_point2 == c_point3 && !goflag_2[1] ) go_3.setBounds(0,0,0,0);
				if( c_point2 == c_point4 && !goflag_2[1] ) go_4.setBounds(0,0,0,0);
				if( c_point3 == c_point4 && !goflag_2[2] ) go_4.setBounds(0,0,0,0);
				if( goflag_2[0] ) go_1.setBounds(0,0,0,0);
				if( goflag_2[1] ) go_2.setBounds(0,0,0,0);
				if( goflag_2[2] ) go_3.setBounds(0,0,0,0);
				if( goflag_2[3] ) go_4.setBounds(0,0,0,0);
			}
			if(member == 2 && turn == 2 ){
				int c_point1 = Checkpoint(horse_3[0], run);
				int c_point2 = Checkpoint(horse_3[1], run);
				int c_point3 = Checkpoint(horse_3[2], run);
				int c_point4 = Checkpoint(horse_3[3], run);
				if( c_point1 != 99 ) go_1.setBounds(Gs.horseXpos[c_point1]+15, Gs.horseYpos[c_point1]+30,19,19);
				else go_1.setBounds(Gs.horseXpos[0]+15, Gs.horseYpos[0]+30,19,19);
				if( c_point2 != 99 ) go_2.setBounds(Gs.horseXpos[c_point2]+15, Gs.horseYpos[c_point2]+30,19,19);
				else go_2.setBounds(Gs.horseXpos[0]+15, Gs.horseYpos[0]+30,19,19);
				if( c_point3 != 99 ) go_3.setBounds(Gs.horseXpos[c_point3]+15, Gs.horseYpos[c_point3]+30,19,19);
				else go_3.setBounds(Gs.horseXpos[0]+15, Gs.horseYpos[0]+30,19,19);
				if( c_point4 != 99 ) go_4.setBounds(Gs.horseXpos[c_point4]+15, Gs.horseYpos[c_point4]+30,19,19);
				else go_4.setBounds(Gs.horseXpos[0]+15, Gs.horseYpos[0]+30,19,19);
				if( c_point1 == c_point2 && !goflag_3[0] ) go_2.setBounds(0,0,0,0);
				if( c_point1 == c_point3 && !goflag_3[0] ) go_3.setBounds(0,0,0,0);
				if( c_point1 == c_point4 && !goflag_3[0] ) go_4.setBounds(0,0,0,0);
				if( c_point2 == c_point3 && !goflag_3[1] ) go_3.setBounds(0,0,0,0);
				if( c_point2 == c_point4 && !goflag_3[1] ) go_4.setBounds(0,0,0,0);
				if( c_point3 == c_point4 && !goflag_3[2] ) go_4.setBounds(0,0,0,0);
				if( goflag_3[0] ) go_1.setBounds(0,0,0,0);
				if( goflag_3[1] ) go_2.setBounds(0,0,0,0);
				if( goflag_3[2] ) go_3.setBounds(0,0,0,0);
				if( goflag_3[3] ) go_4.setBounds(0,0,0,0);
			}
			if(member == 3 && turn == 3 ){
				int c_point1 = Checkpoint(horse_4[0], run);
				int c_point2 = Checkpoint(horse_4[1], run);
				int c_point3 = Checkpoint(horse_4[2], run);
				int c_point4 = Checkpoint(horse_4[3], run);
				if( c_point1 != 99 ) go_1.setBounds(Gs.horseXpos[c_point1]+15, Gs.horseYpos[c_point1]+30,19,19);
				else go_1.setBounds(Gs.horseXpos[0]+15, Gs.horseYpos[0]+30,19,19);
				if( c_point2 != 99 ) go_2.setBounds(Gs.horseXpos[c_point2]+15, Gs.horseYpos[c_point2]+30,19,19);
				else go_2.setBounds(Gs.horseXpos[0]+15, Gs.horseYpos[0]+30,19,19);
				if( c_point3 != 99 ) go_3.setBounds(Gs.horseXpos[c_point3]+15, Gs.horseYpos[c_point3]+30,19,19);
				else go_3.setBounds(Gs.horseXpos[0]+15, Gs.horseYpos[0]+30,19,19);
				if( c_point4 != 99 ) go_4.setBounds(Gs.horseXpos[c_point4]+15, Gs.horseYpos[c_point4]+30,19,19);
				else go_4.setBounds(Gs.horseXpos[0]+15, Gs.horseYpos[0]+30,19,19);
				if( c_point1 == c_point2 && !goflag_4[0] ) go_2.setBounds(0,0,0,0);
				if( c_point1 == c_point3 && !goflag_4[0] ) go_3.setBounds(0,0,0,0);
				if( c_point1 == c_point4 && !goflag_4[0] ) go_4.setBounds(0,0,0,0);
				if( c_point2 == c_point3 && !goflag_4[1] ) go_3.setBounds(0,0,0,0);
				if( c_point2 == c_point4 && !goflag_4[1] ) go_4.setBounds(0,0,0,0);
				if( c_point3 == c_point4 && !goflag_4[2] ) go_4.setBounds(0,0,0,0);
				if( goflag_4[0] ) go_1.setBounds(0,0,0,0);
				if( goflag_4[1] ) go_2.setBounds(0,0,0,0);
				if( goflag_4[2] ) go_3.setBounds(0,0,0,0);
				if( goflag_4[3] ) go_4.setBounds(0,0,0,0);
			}
		}
	}
	// go button �� �����ʿ� ���� �Լ�
	public void setGoButton1(){
		int buf = 0;
		int checknum = 0;
		if( member == 0 ){
			int checkpoint = Checkpoint(horse_1[0], run);
			if( checkpoint != 99 ){
				if( horse_1[0] >= 300 ){
					buf = 300;
					if( checkpoint == 0 ){
						horse_1[0] = 99;
						horse_1[1] = 99;
						horse_1[2] = 99;
						horse_1[3] = 99;
					}else{
						horse_1[0] = checkpoint+buf;
						horse_1[1] = checkpoint+buf;
						horse_1[2] = checkpoint+buf;
						horse_1[3] = checkpoint+buf;
					}
				}else if( horse_1[0] >= 200 ){
					buf = 200;
					if( horse_1[0] != horse_1[1]){
						if( checkpoint == 0 ){
							horse_1[0] = 99;
							horse_1[2] = 99;
							horse_1[3] = 99;
						}else{
							horse_1[0] = checkpoint+buf;
							horse_1[2] = checkpoint+buf;
							horse_1[3] = checkpoint+buf;
							checknum = horse_1[0] - horse_1[1];
							if( checknum == 200){
								horse_1[0] = 300+checkpoint;
								horse_1[1] = horse_1[0];
								horse_1[2] = horse_1[0];
								horse_1[3] = horse_1[0];
							}
						}
					}else if ( horse_1[0] != horse_1[2]){
						if( checkpoint == 0 ){
							horse_1[0] = 99;
							horse_1[1] = 99;
							horse_1[3] = 99;
						}else{
							horse_1[0] = checkpoint+buf;
							horse_1[1] = checkpoint+buf;
							horse_1[3] = checkpoint+buf;
							checknum = horse_1[0] - horse_1[2];
							if( checknum == 200){
								horse_1[0] = 300+checkpoint;
								horse_1[1] = horse_1[0];
								horse_1[2] = horse_1[0];
								horse_1[3] = horse_1[0];
							}
						}
					}else{
						if( checkpoint == 0 ){
							horse_1[0] = 99;
							horse_1[1] = 99;
							horse_1[2] = 99;
						}else{
							horse_1[0] = checkpoint+buf;
							horse_1[1] = checkpoint+buf;
							horse_1[2] = checkpoint+buf;
							checknum = horse_1[0] - horse_1[3];
							if( checknum == 200){
								horse_1[0] = 300+checkpoint;
								horse_1[1] = horse_1[0];
								horse_1[2] = horse_1[0];
								horse_1[3] = horse_1[0];
							}
						}
					}
				}else if( horse_1[0] >= 100 ){
					buf = 100;
					if( horse_1[0] == horse_1[1]){
						if( checkpoint == 0 ){
							horse_1[0] = 99;
							horse_1[1] = 99;
						}else{
							horse_1[0] = checkpoint+buf;
							horse_1[1] = checkpoint+buf;
							checknum = horse_1[0] - horse_1[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[0] = 300+checkpoint;
									horse_1[1] = horse_1[0];
									horse_1[2] = horse_1[0];
									horse_1[3] = horse_1[0];
								}else{
									horse_1[0] = 200+checkpoint;
									horse_1[1] = horse_1[0];
									horse_1[2] = horse_1[0];
								}
							}
							checknum = checkpoint+buf - horse_1[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[0] = 300+checkpoint;
									horse_1[1] = horse_1[0];
									horse_1[3] = horse_1[0];
									horse_1[2] = horse_1[0];
								}else{
									horse_1[0] = 200+checkpoint;
									horse_1[1] = horse_1[0];
									horse_1[3] = horse_1[0];
								}
							}
						}
					}else if( horse_1[0] == horse_1[2]){
						if( checkpoint == 0 ){
							horse_1[0] = 99;
							horse_1[2] = 99;
						}else{
							horse_1[0] = checkpoint+buf;
							horse_1[2] = checkpoint+buf;
							checknum = horse_1[0] - horse_1[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[0] = 300+checkpoint;
									horse_1[2] = horse_1[0];
									horse_1[1] = horse_1[0];
									horse_1[3] = horse_1[0];
								}else{
									horse_1[0] = 200+checkpoint;
									horse_1[2] = horse_1[0];
									horse_1[1] = horse_1[0];
								}
							}
							checknum = checkpoint+buf - horse_1[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[0] = 300+checkpoint;
									horse_1[2] = horse_1[0];
									horse_1[3] = horse_1[0];
									horse_1[1] = horse_1[0];
								}else{
									horse_1[0] = 200+checkpoint;
									horse_1[2] = horse_1[0];
									horse_1[3] = horse_1[0];
								}
							}
						}
					}else if( horse_1[0] == horse_1[3]){
						if( checkpoint == 0 ){
							horse_1[0] = 99;
							horse_1[3] = 99;
						}else{
							horse_1[0] = checkpoint+buf;
							horse_1[3] = checkpoint+buf;
							checknum = horse_1[0] - horse_1[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[0] = 300+checkpoint;
									horse_1[3] = horse_1[0];
									horse_1[1] = horse_1[0];
									horse_1[2] = horse_1[0];
								}else{
									horse_1[0] = 200+checkpoint;
									horse_1[3] = horse_1[0];
									horse_1[1] = horse_1[0];
								}
							}
							checknum = checkpoint+buf - horse_1[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[0] = 300+checkpoint;
									horse_1[3] = horse_1[0];
									horse_1[2] = horse_1[0];
									horse_1[1] = horse_1[0];
								}else{
									horse_1[0] = 200+checkpoint;
									horse_1[3] = horse_1[0];
									horse_1[2] = horse_1[0];
								}
							}
						}
					}
				}else{
					if( checkpoint == 0 ){
						horse_1[0] = 99;
					}else{
						horse_1[0] = checkpoint;
						int comparepoint_1 = horse_1[1];
						int comparepoint_2 = horse_1[2];
						int comparepoint_3 = horse_1[3];
						checknum = checkpoint - comparepoint_1;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_1[0] <= 300 ){
								horse_1[0] = 300+checkpoint;
								horse_1[1] = horse_1[0];
								horse_1[2] = horse_1[0];
								horse_1[3] = horse_1[0];
							}else if( checknum == -100 && horse_1[0] <= 200  ){
								horse_1[0] = 200+checkpoint;
								if( horse_1[1] != horse_1[2] ){
									horse_1[1] = horse_1[0];
									horse_1[3] = horse_1[0];
								}else{
									horse_1[1] = horse_1[0];
									horse_1[2] = horse_1[0];
								}
							}else if( horse_1[0] <= 100 ){
								horse_1[0] = 100+checkpoint;
								horse_1[1] = horse_1[0];
							}
						}
						checknum = checkpoint - comparepoint_2;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_1[0] <= 300 ){
								horse_1[0] = 300+checkpoint;
								horse_1[1] = horse_1[0];
								horse_1[2] = horse_1[0];
								horse_1[3] = horse_1[0];
							}else if( checknum == -100 && horse_1[0] <= 200  ){
								horse_1[0] = 200+checkpoint;
								if( horse_1[2] != horse_1[1] ){
									horse_1[2] = horse_1[0];
									horse_1[3] = horse_1[0];
								}else{
									horse_1[2] = horse_1[0];
									horse_1[1] = horse_1[0];
								}
							}else if( horse_1[0] <= 100 ){
								horse_1[0] = 100+checkpoint;
								horse_1[2] = horse_1[0];
							}
						}
						checknum = checkpoint - comparepoint_3;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_1[0] <= 300 ){
								horse_1[0] = 300+checkpoint;
								horse_1[1] = horse_1[0];
								horse_1[2] = horse_1[0];
								horse_1[3] = horse_1[0];
							}else if( checknum == -100 && horse_1[0] <= 200  ){
								horse_1[0] = 200+checkpoint;
								if( horse_1[3] != horse_1[1] ){
									horse_1[2] = horse_1[0];
									horse_1[3] = horse_1[0];
								}else{
									horse_1[3] = horse_1[0];
									horse_1[1] = horse_1[0];
								}
							}else if( horse_1[0] <= 100 ){
								horse_1[0] = 100+checkpoint;
								horse_1[3] = horse_1[0];
							}
						}
					}
				}
				overlapCharacter(horse_1[0], member);
			}else{
				for(int i=0, goal = horse_1[0]; i<4; i++)
					if(horse_1[i] == goal) horse_1[i] = 99;
				goflag_1[0] = true;
			}
		}
		if( member == 1 ){
			int checkpoint = Checkpoint(horse_2[0], run);
			if( checkpoint != 99 ){
				if( horse_2[0] >= 300 ){
					buf = 300;
					if( checkpoint == 0 ){
						horse_2[0] = 99;
						horse_2[1] = 99;
						horse_2[2] = 99;
						horse_2[3] = 99;
					}else{
						horse_2[0] = checkpoint+buf;
						horse_2[1] = checkpoint+buf;
						horse_2[2] = checkpoint+buf;
						horse_2[3] = checkpoint+buf;
					}	
				}else if( horse_2[0] >= 200 ){
					buf = 200;
					if( horse_2[0] != horse_2[1]){
						if( checkpoint == 0 ){
							horse_2[0] = 99;
							horse_2[2] = 99;
							horse_2[3] = 99;
						}else{
							horse_2[0] = checkpoint+buf;
							horse_2[2] = checkpoint+buf;
							horse_2[3] = checkpoint+buf;
							checknum = horse_2[0] - horse_2[1];
							if( checknum == 200){
								horse_2[0] = 300+checkpoint;
								horse_2[1] = horse_2[0];
								horse_2[2] = horse_2[0];
								horse_2[3] = horse_2[0];
							}
						}
					}else if ( horse_2[0] != horse_2[2]){
						if( checkpoint == 0 ){
							horse_2[0] = 99;
							horse_2[1] = 99;
							horse_2[3] = 99;
						}else{
							horse_2[0] = checkpoint+buf;
							horse_2[1] = checkpoint+buf;
							horse_2[3] = checkpoint+buf;
							checknum = horse_2[0] - horse_2[2];
							if( checknum == 200){
								horse_2[0] = 300+checkpoint;
								horse_2[1] = horse_2[0];
								horse_2[2] = horse_2[0];
								horse_2[3] = horse_2[0];
							}
						}
					}else{
						if( checkpoint == 0 ){
							horse_2[0] = 99;
							horse_2[1] = 99;
							horse_2[2] = 99;
						}else{
							horse_2[0] = checkpoint+buf;
							horse_2[1] = checkpoint+buf;
							horse_2[2] = checkpoint+buf;
							checknum = horse_2[0] - horse_2[3];
							if( checknum == 200){
								horse_2[0] = 300+checkpoint;
								horse_2[1] = horse_2[0];
								horse_2[2] = horse_2[0];
								horse_2[3] = horse_2[0];
							}
						}
					}
				}else if( horse_2[0] >= 100 ){
					buf = 100;
					if( horse_2[0] == horse_2[1]){
						if( checkpoint == 0 ){
							horse_2[0] = 99;
							horse_2[1] = 99;
						}else{
							horse_2[0] = checkpoint+buf;
							horse_2[1] = checkpoint+buf;
							checknum = horse_2[0] - horse_2[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[0] = 300+checkpoint;
									horse_2[1] = horse_2[0];
									horse_2[2] = horse_2[0];
									horse_2[3] = horse_2[0];
								}else{
									horse_2[0] = 200+checkpoint;
									horse_2[1] = horse_2[0];
									horse_2[2] = horse_2[0];
								}
							}
							checknum = checkpoint+buf - horse_2[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[0] = 300+checkpoint;
									horse_2[1] = horse_2[0];
									horse_2[3] = horse_2[0];
									horse_2[2] = horse_2[0];
								}else{
									horse_2[0] = 200+checkpoint;
									horse_2[1] = horse_2[0];
									horse_2[3] = horse_2[0];
								}
							}
						}
					}else if( horse_2[0] == horse_2[2]){
						if( checkpoint == 0 ){
							horse_2[0] = 99;
							horse_2[2] = 99;
						}else{
							horse_2[0] = checkpoint+buf;
							horse_2[2] = checkpoint+buf;
							checknum = horse_2[0] - horse_2[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[0] = 300+checkpoint;
									horse_2[2] = horse_2[0];
									horse_2[1] = horse_2[0];
									horse_2[3] = horse_2[0];
								}else{
									horse_2[0] = 200+checkpoint;
									horse_2[2] = horse_2[0];
									horse_2[1] = horse_2[0];
								}
							}
							checknum = checkpoint+buf - horse_2[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[0] = 300+checkpoint;
									horse_2[2] = horse_2[0];
									horse_2[3] = horse_2[0];
									horse_2[1] = horse_2[0];
								}else{
									horse_2[0] = 200+checkpoint;
									horse_2[2] = horse_2[0];
									horse_2[3] = horse_2[0];
								}
							}
						}
					}else if( horse_2[0] == horse_2[3]){
						if( checkpoint == 0 ){
							horse_2[0] = 99;
							horse_2[3] = 99;
						}else{
							horse_2[0] = checkpoint+buf;
							horse_2[3] = checkpoint+buf;
							checknum = horse_2[0] - horse_2[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[0] = 300+checkpoint;
									horse_2[3] = horse_2[0];
									horse_2[1] = horse_2[0];
									horse_2[2] = horse_2[0];
								}else{
									horse_2[0] = 200+checkpoint;
									horse_2[3] = horse_2[0];
									horse_2[1] = horse_2[0];
								}
							}
							checknum = checkpoint+buf - horse_2[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[0] = 300+checkpoint;
									horse_2[3] = horse_2[0];
									horse_2[2] = horse_2[0];
									horse_2[1] = horse_2[0];
								}else{
									horse_2[0] = 200+checkpoint;
									horse_2[3] = horse_2[0];
									horse_2[2] = horse_2[0];
								}
							}
						}
					}
				}else{
					if( checkpoint == 0 ){
						horse_2[0] = 99;
					}else{
						horse_2[0] = checkpoint;
						int comparepoint_1 = horse_2[1];
						int comparepoint_2 = horse_2[2];
						int comparepoint_3 = horse_2[3];
						checknum = checkpoint - comparepoint_1;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_2[0] <= 300 ){
								horse_2[0] = 300+checkpoint;
								horse_2[1] = horse_2[0];
								horse_2[2] = horse_2[0];
								horse_2[3] = horse_2[0];
							}else if( checknum == -100 && horse_2[0] <= 200  ){
								horse_2[0] = 200+checkpoint;
								if( horse_2[1] != horse_2[2] ){
									horse_2[1] = horse_2[0];
									horse_2[3] = horse_2[0];
								}else{
									horse_2[1] = horse_2[0];
									horse_2[2] = horse_2[0];
								}
							}else if( horse_2[0] <= 100 ){
								horse_2[0] = 100+checkpoint;
								horse_2[1] = horse_2[0];
							}
						}
						checknum = checkpoint - comparepoint_2;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_2[0] <= 300 ){
								horse_2[0] = 300+checkpoint;
								horse_2[1] = horse_2[0];
								horse_2[2] = horse_2[0];
								horse_2[3] = horse_2[0];
							}else if( checknum == -100 && horse_2[0] <= 200  ){
								horse_2[0] = 200+checkpoint;
								if( horse_2[2] != horse_2[1] ){
									horse_2[2] = horse_2[0];
									horse_2[3] = horse_2[0];
								}else{
									horse_2[2] = horse_2[0];
									horse_2[1] = horse_2[0];
								}
							}else if( horse_2[0] <= 100 ){
								horse_2[0] = 100+checkpoint;
								horse_2[2] = horse_2[0];
							}
						}
						checknum = checkpoint - comparepoint_3;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_2[0] <= 300 ){
								horse_2[0] = 300+checkpoint;
								horse_2[1] = horse_2[0];
								horse_2[2] = horse_2[0];
								horse_2[3] = horse_2[0];
							}else if( checknum == -100 && horse_2[0] <= 200  ){
								horse_2[0] = 200+checkpoint;
								if( horse_2[3] != horse_2[1] ){
									horse_2[2] = horse_2[0];
									horse_2[3] = horse_2[0];
								}else{
									horse_2[3] = horse_2[0];
									horse_2[1] = horse_2[0];
								}
							}else if( horse_2[0] <= 100 ){
								horse_2[0] = 100+checkpoint;
								horse_2[3] = horse_2[0];
							}
						}
					}
				}
			overlapCharacter(horse_2[0], member);
			}else{
				for(int i=0, goal = horse_2[0]; i<4; i++)
					if(horse_2[i] == goal) horse_2[i] = 99;
				goflag_2[0] = true;
			}
		}
		if( member == 2 ){
			int checkpoint = Checkpoint(horse_3[0], run);
			if( checkpoint != 99 ){
				if( horse_3[0] >= 300 ){
					buf = 300;
					if( checkpoint == 0 ){
						horse_3[0] = 99;
						horse_3[1] = 99;
						horse_3[2] = 99;
						horse_3[3] = 99;
					}else{
						horse_3[0] = checkpoint+buf;
						horse_3[1] = checkpoint+buf;
						horse_3[2] = checkpoint+buf;
						horse_3[3] = checkpoint+buf;
					}	
				}else if( horse_3[0] >= 200 ){
					buf = 200;
					if( horse_3[0] != horse_3[1]){
						if( checkpoint == 0 ){
							horse_3[0] = 99;
							horse_3[2] = 99;
							horse_3[3] = 99;
						}else{
							horse_3[0] = checkpoint+buf;
							horse_3[2] = checkpoint+buf;
							horse_3[3] = checkpoint+buf;
							checknum = horse_3[0] - horse_3[1];
							if( checknum == 200){
								horse_3[0] = 300+checkpoint;
								horse_3[1] = horse_3[0];
								horse_3[2] = horse_3[0];
								horse_3[3] = horse_3[0];
							}
						}
					}else if ( horse_3[0] != horse_3[2]){
						if( checkpoint == 0 ){
							horse_3[0] = 99;
							horse_3[1] = 99;
							horse_3[3] = 99;
						}else{
							horse_3[0] = checkpoint+buf;
							horse_3[1] = checkpoint+buf;
							horse_3[3] = checkpoint+buf;
							checknum = horse_3[0] - horse_3[2];
							if( checknum == 200){
								horse_3[0] = 300+checkpoint;
								horse_3[1] = horse_3[0];
								horse_3[2] = horse_3[0];
								horse_3[3] = horse_3[0];
							}
						}
					}else{
						if( checkpoint == 0 ){
							horse_3[0] = 99;
							horse_3[1] = 99;
							horse_3[2] = 99;
						}else{
							horse_3[0] = checkpoint+buf;
							horse_3[1] = checkpoint+buf;
							horse_3[2] = checkpoint+buf;
							checknum = horse_3[0] - horse_3[3];
							if( checknum == 200){
								horse_3[0] = 300+checkpoint;
								horse_3[1] = horse_3[0];
								horse_3[2] = horse_3[0];
								horse_3[3] = horse_3[0];
							}
						}
					}
				}else if( horse_3[0] >= 100 ){
					buf = 100;
					if( horse_3[0] == horse_3[1]){
						if( checkpoint == 0 ){
							horse_3[0] = 99;
							horse_3[1] = 99;
						}else{
							horse_3[0] = checkpoint+buf;
							horse_3[1] = checkpoint+buf;
							checknum = horse_3[0] - horse_3[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[0] = 300+checkpoint;
									horse_3[1] = horse_3[0];
									horse_3[2] = horse_3[0];
									horse_3[3] = horse_3[0];
								}else{
									horse_3[0] = 200+checkpoint;
									horse_3[1] = horse_3[0];
									horse_3[2] = horse_3[0];
								}
							}
							checknum = checkpoint+buf - horse_3[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[0] = 300+checkpoint;
									horse_3[1] = horse_3[0];
									horse_3[3] = horse_3[0];
									horse_3[2] = horse_3[0];
								}else{
									horse_3[0] = 200+checkpoint;
									horse_3[1] = horse_3[0];
									horse_3[3] = horse_3[0];
								}
							}
						}
					}else if( horse_3[0] == horse_3[2]){
						if( checkpoint == 0 ){
							horse_3[0] = 99;
							horse_3[2] = 99;
						}else{
							horse_3[0] = checkpoint+buf;
							horse_3[2] = checkpoint+buf;
							checknum = horse_3[0] - horse_3[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[0] = 300+checkpoint;
									horse_3[2] = horse_3[0];
									horse_3[1] = horse_3[0];
									horse_3[3] = horse_3[0];
								}else{
									horse_3[0] = 200+checkpoint;
									horse_3[2] = horse_3[0];
									horse_3[1] = horse_3[0];
								}
							}
							checknum = checkpoint+buf - horse_3[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[0] = 300+checkpoint;
									horse_3[2] = horse_3[0];
									horse_3[3] = horse_3[0];
									horse_3[1] = horse_3[0];
								}else{
									horse_3[0] = 200+checkpoint;
									horse_3[2] = horse_3[0];
									horse_3[3] = horse_3[0];
								}
							}
						}
					}else if( horse_3[0] == horse_3[3]){
						if( checkpoint == 0 ){
							horse_3[0] = 99;
							horse_3[3] = 99;
						}else{
							horse_3[0] = checkpoint+buf;
							horse_3[3] = checkpoint+buf;
							checknum = horse_3[0] - horse_3[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[0] = 300+checkpoint;
									horse_3[3] = horse_3[0];
									horse_3[1] = horse_3[0];
									horse_3[2] = horse_3[0];
								}else{
									horse_3[0] = 200+checkpoint;
									horse_3[3] = horse_3[0];
									horse_3[1] = horse_3[0];
								}
							}
							checknum = checkpoint+buf - horse_3[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[0] = 300+checkpoint;
									horse_3[3] = horse_3[0];
									horse_3[2] = horse_3[0];
									horse_3[1] = horse_3[0];
								}else{
									horse_3[0] = 200+checkpoint;
									horse_3[3] = horse_3[0];
									horse_3[2] = horse_3[0];
								}
							}
						}
					}
				}else{
					if( checkpoint == 0 ){
						horse_3[0] = 99;
					}else{
						horse_3[0] = checkpoint;
						int comparepoint_1 = horse_3[1];
						int comparepoint_2 = horse_3[2];
						int comparepoint_3 = horse_3[3];
						checknum = checkpoint - comparepoint_1;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_3[0] <= 300 ){
								horse_3[0] = 300+checkpoint;
								horse_3[1] = horse_3[0];
								horse_3[2] = horse_3[0];
								horse_3[3] = horse_3[0];
							}else if( checknum == -100 && horse_3[0] <= 200  ){
								horse_3[0] = 200+checkpoint;
								if( horse_3[1] != horse_3[2] ){
									horse_3[1] = horse_3[0];
									horse_3[3] = horse_3[0];
								}else{
									horse_3[1] = horse_3[0];
									horse_3[2] = horse_3[0];
								}
							}else if( horse_3[0] <= 100 ){
								horse_3[0] = 100+checkpoint;
								horse_3[1] = horse_3[0];
							}
						}
						checknum = checkpoint - comparepoint_2;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_3[0] <= 300 ){
								horse_3[0] = 300+checkpoint;
								horse_3[1] = horse_3[0];
								horse_3[2] = horse_3[0];
								horse_3[3] = horse_3[0];
							}else if( checknum == -100 && horse_3[0] <= 200  ){
								horse_3[0] = 200+checkpoint;
								if( horse_3[2] != horse_3[1] ){
									horse_3[2] = horse_3[0];
									horse_3[3] = horse_3[0];
								}else{
									horse_3[2] = horse_3[0];
									horse_3[1] = horse_3[0];
								}
							}else if( horse_3[0] <= 100 ){
								horse_3[0] = 100+checkpoint;
								horse_3[2] = horse_3[0];
							}
						}
						checknum = checkpoint - comparepoint_3;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_3[0] <= 300 ){
								horse_3[0] = 300+checkpoint;
								horse_3[1] = horse_3[0];
								horse_3[2] = horse_3[0];
								horse_3[3] = horse_3[0];
							}else if( checknum == -100 && horse_3[0] <= 200  ){
								horse_3[0] = 200+checkpoint;
								if( horse_3[3] != horse_3[1] ){
									horse_3[2] = horse_3[0];
									horse_3[3] = horse_3[0];
								}else{
									horse_3[3] = horse_3[0];
									horse_3[1] = horse_3[0];
								}
							}else if( horse_3[0] <= 100 ){
								horse_3[0] = 100+checkpoint;
								horse_3[3] = horse_3[0];
							}
						}
					}
				}
				overlapCharacter(horse_3[0], member);
			}else{
				for(int i=0, goal = horse_3[0]; i<4; i++)
					if(horse_3[i] == goal) horse_3[i] = 99;
				goflag_3[0] = true;
			}
		}
		if( member == 3){
			int checkpoint = Checkpoint(horse_4[0], run);
			if( checkpoint != 99 ){
				if( horse_4[0] >= 300 ){
					buf = 300;
					if( checkpoint == 0 ){
						horse_4[0] = 99;
						horse_4[1] = 99;
						horse_4[2] = 99;
						horse_4[3] = 99;
					}else{
						horse_4[0] = checkpoint+buf;
						horse_4[1] = checkpoint+buf;
						horse_4[2] = checkpoint+buf;
						horse_4[3] = checkpoint+buf;
					}	
				}else if( horse_4[0] >= 200 ){
					buf = 200;
					if( horse_4[0] != horse_4[1]){
						if( checkpoint == 0 ){
							horse_4[0] = 99;
							horse_4[2] = 99;
							horse_4[3] = 99;
						}else{
							horse_4[0] = checkpoint+buf;
							horse_4[2] = checkpoint+buf;
							horse_4[3] = checkpoint+buf;
							checknum = horse_4[0] - horse_4[1];
							if( checknum == 200){
								horse_4[0] = 300+checkpoint;
								horse_4[1] = horse_4[0];
								horse_4[2] = horse_4[0];
								horse_4[3] = horse_4[0];
							}
						}
					}else if ( horse_4[0] != horse_4[2]){
						if( checkpoint == 0 ){
							horse_4[0] = 99;
							horse_4[1] = 99;
							horse_4[3] = 99;
						}else{
							horse_4[0] = checkpoint+buf;
							horse_4[1] = checkpoint+buf;
							horse_4[3] = checkpoint+buf;
							checknum = horse_4[0] - horse_4[2];
							if( checknum == 200){
								horse_4[0] = 300+checkpoint;
								horse_4[1] = horse_4[0];
								horse_4[2] = horse_4[0];
								horse_4[3] = horse_4[0];
							}
						}
					}else{
						if( checkpoint == 0 ){
							horse_4[0] = 99;
							horse_4[1] = 99;
							horse_4[2] = 99;
						}else{
							horse_4[0] = checkpoint+buf;
							horse_4[1] = checkpoint+buf;
							horse_4[2] = checkpoint+buf;
							checknum = horse_4[0] - horse_4[3];
							if( checknum == 200){
								horse_4[0] = 300+checkpoint;
								horse_4[1] = horse_4[0];
								horse_4[2] = horse_4[0];
								horse_4[3] = horse_4[0];
							}
						}
					}
				}else if( horse_4[0] >= 100 ){
					buf = 100;
					if( horse_4[0] == horse_4[1]){
						if( checkpoint == 0 ){
							horse_4[0] = 99;
							horse_4[1] = 99;
						}else{
							horse_4[0] = checkpoint+buf;
							horse_4[1] = checkpoint+buf;
							checknum = horse_4[0] - horse_4[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[0] = 300+checkpoint;
									horse_4[1] = horse_4[0];
									horse_4[2] = horse_4[0];
									horse_4[3] = horse_4[0];
								}else{
									horse_4[0] = 200+checkpoint;
									horse_4[1] = horse_4[0];
									horse_4[2] = horse_4[0];
								}
							}
							checknum = checkpoint+buf - horse_4[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[0] = 300+checkpoint;
									horse_4[1] = horse_4[0];
									horse_4[3] = horse_4[0];
									horse_4[2] = horse_4[0];
								}else{
									horse_4[0] = 200+checkpoint;
									horse_4[1] = horse_4[0];
									horse_4[3] = horse_4[0];
								}
							}
						}
					}else if( horse_4[0] == horse_4[2]){
						if( checkpoint == 0 ){
							horse_4[0] = 99;
							horse_4[2] = 99;
						}else{
							horse_4[0] = checkpoint+buf;
							horse_4[2] = checkpoint+buf;
							checknum = horse_4[0] - horse_4[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[0] = 300+checkpoint;
									horse_4[2] = horse_4[0];
									horse_4[1] = horse_4[0];
									horse_4[3] = horse_4[0];
								}else{
									horse_4[0] = 200+checkpoint;
									horse_4[2] = horse_4[0];
									horse_4[1] = horse_4[0];
								}
							}
							checknum = checkpoint+buf - horse_4[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[0] = 300+checkpoint;
									horse_4[2] = horse_4[0];
									horse_4[3] = horse_4[0];
									horse_4[1] = horse_4[0];
								}else{
									horse_4[0] = 200+checkpoint;
									horse_4[2] = horse_4[0];
									horse_4[3] = horse_4[0];
								}
							}
						}
					}else if( horse_4[0] == horse_4[3]){
						if( checkpoint == 0 ){
							horse_4[0] = 99;
							horse_4[3] = 99;
						}else{
							horse_4[0] = checkpoint+buf;
							horse_4[3] = checkpoint+buf;
							checknum = horse_4[0] - horse_4[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[0] = 300+checkpoint;
									horse_4[3] = horse_4[0];
									horse_4[1] = horse_4[0];
									horse_4[2] = horse_4[0];
								}else{
									horse_4[0] = 200+checkpoint;
									horse_4[3] = horse_4[0];
									horse_4[1] = horse_4[0];
								}
							}
							checknum = checkpoint+buf - horse_4[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[0] = 300+checkpoint;
									horse_4[3] = horse_4[0];
									horse_4[2] = horse_4[0];
									horse_4[1] = horse_4[0];
								}else{
									horse_4[0] = 200+checkpoint;
									horse_4[3] = horse_4[0];
									horse_4[2] = horse_4[0];
								}
							}
						}
					}
				}else{
					if( checkpoint == 0 ){
						horse_4[0] = 99;
					}else{
						horse_4[0] = checkpoint;
						int comparepoint_1 = horse_4[1];
						int comparepoint_2 = horse_4[2];
						int comparepoint_3 = horse_4[3];
						checknum = checkpoint - comparepoint_1;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_4[0] <= 300 ){
								horse_4[0] = 300+checkpoint;
								horse_4[1] = horse_4[0];
								horse_4[2] = horse_4[0];
								horse_4[3] = horse_4[0];
							}else if( checknum == -100 && horse_4[0] <= 200  ){
								horse_4[0] = 200+checkpoint;
								if( horse_4[1] != horse_4[2] ){
									horse_4[1] = horse_4[0];
									horse_4[3] = horse_4[0];
								}else{
									horse_4[1] = horse_4[0];
									horse_4[2] = horse_4[0];
								}
							}else if( horse_4[0] <= 100 ){
								horse_4[0] = 100+checkpoint;
								horse_4[1] = horse_4[0];
							}
						}
						checknum = checkpoint - comparepoint_2;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_4[0] <= 300 ){
								horse_4[0] = 300+checkpoint;
								horse_4[1] = horse_4[0];
								horse_4[2] = horse_4[0];
								horse_4[3] = horse_4[0];
							}else if( checknum == -100 && horse_4[0] <= 200  ){
								horse_4[0] = 200+checkpoint;
								if( horse_4[2] != horse_4[1] ){
									horse_4[2] = horse_4[0];
									horse_4[3] = horse_4[0];
								}else{
									horse_4[2] = horse_4[0];
									horse_4[1] = horse_4[0];
								}
							}else if( horse_4[0] <= 100 ){
								horse_4[0] = 100+checkpoint;
								horse_4[2] = horse_4[0];
							}
						}
						checknum = checkpoint - comparepoint_3;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_4[0] <= 300 ){
								horse_4[0] = 300+checkpoint;
								horse_4[1] = horse_4[0];
								horse_4[2] = horse_4[0];
								horse_4[3] = horse_4[0];
							}else if( checknum == -100 && horse_4[0] <= 200  ){
								horse_4[0] = 200+checkpoint;
								if( horse_4[3] != horse_4[1] ){
									horse_4[2] = horse_4[0];
									horse_4[3] = horse_4[0];
								}else{
									horse_4[3] = horse_4[0];
									horse_4[1] = horse_4[0];
								}
							}else if( horse_4[0] <= 100 ){
								horse_4[0] = 100+checkpoint;
								horse_4[3] = horse_4[0];
							}
						}
					}
				}
				overlapCharacter(horse_4[0], member);
			}else{
				for(int i=0, goal = horse_4[0]; i<4; i++)
					if(horse_4[i] == goal) horse_4[i] = 99;
				goflag_4[0] = true;
			}
		}
		run = 0;
		turn = (turn+1)%4;
		cs.HorseAndTurnUpdate();
	}
	public void setGoButton2(){
		int buf = 0;
		int checknum = 0;
		if( member == 0 ){
			int checkpoint = Checkpoint(horse_1[1], run);
			if( checkpoint != 99 ){
				if(horse_1[1] >= 300){
					buf = 300;
					if( checkpoint == 0 ){
						horse_1[0] = 99;
						horse_1[1] = 99;
						horse_1[2] = 99;
						horse_1[3] = 99;
					}else{
						horse_1[0] = checkpoint + buf;
						horse_1[1] = checkpoint + buf;
						horse_1[2] = checkpoint + buf;
						horse_1[3] = checkpoint + buf;
					}
				}else if( horse_1[1] >= 200 ){
					buf = 200;
					if( horse_1[1] != horse_1[0]){
						if( checkpoint == 0 ){
							horse_1[1] = 99;
							horse_1[2] = 99;
							horse_1[3] = 99;
						}else{
							horse_1[1] = checkpoint+buf;
							horse_1[2] = checkpoint+buf;
							horse_1[3] = checkpoint+buf;
							checknum = horse_1[1] - horse_1[0];
							if( checknum == 200){
								horse_1[1] = 300+checkpoint;
								horse_1[0] = horse_1[1];
								horse_1[2] = horse_1[1];
								horse_1[3] = horse_1[1];
							}
						}
					}else if( horse_1[1] != horse_1[2] ){
						if( checkpoint == 0 ){
							horse_1[0] = 99;
							horse_1[1] = 99;
							horse_1[3] = 99;
						}else{
							horse_1[0] = checkpoint+buf;
							horse_1[1] = checkpoint+buf;
							horse_1[3] = checkpoint+buf;
							checknum = horse_1[1] - horse_1[2];
							if( checknum == 200){
								horse_1[1] = 300+checkpoint;
								horse_1[0] = horse_1[1];
								horse_1[2] = horse_1[1];
								horse_1[3] = horse_1[1];
							}
						}
					}else{
						if( checkpoint == 0 ){
							horse_1[0] = 99;
							horse_1[1] = 99;
							horse_1[2] = 99;
						}else{
							horse_1[0] = checkpoint+buf;
							horse_1[1] = checkpoint+buf;
							horse_1[2] = checkpoint+buf;
							checknum = horse_1[1] - horse_1[3];
							if( checknum == 200){
								horse_1[1] = 300+checkpoint;
								horse_1[0] = horse_1[1];
								horse_1[2] = horse_1[1];
								horse_1[3] = horse_1[1];
							}
						}
					}
				}else if(horse_1[1] >= 100 ){
					buf = 100;
					if( horse_1[1] == horse_1[0] ){
						if( checkpoint == 0){
							horse_1[1] = 99;
							horse_1[0] = 99;
						}else{
							horse_1[1] = checkpoint+buf;
							horse_1[0] = checkpoint+buf;
							checknum = horse_1[1] - horse_1[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[1] = 300+checkpoint;
									horse_1[0] = horse_1[1];
									horse_1[2] = horse_1[1];
									horse_1[3] = horse_1[1];
								}else{
									horse_1[1] = 200+checkpoint;
									horse_1[0] = horse_1[1];
									horse_1[2] = horse_1[1];
								}
							}
							checknum = checkpoint+buf - horse_1[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[1] = 300+checkpoint;
									horse_1[0] = horse_1[1];
									horse_1[3] = horse_1[1];
									horse_1[2] = horse_1[1];
								}else{
									horse_1[1] = 200+checkpoint;
									horse_1[0] = horse_1[1];
									horse_1[3] = horse_1[1];
								}
							}
						}
					}else if( horse_1[1] == horse_1[2] ){
						if( checkpoint == 0){
							horse_1[1] = 99;
							horse_1[2] = 99;
						}else{
							horse_1[1] = checkpoint+buf;
							horse_1[2] = checkpoint+buf;
							checknum = horse_1[1] - horse_1[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[1] = 300+checkpoint;
									horse_1[0] = horse_1[1];
									horse_1[2] = horse_1[1];
									horse_1[3] = horse_1[1];
								}else{
									horse_1[1] = 200+checkpoint;
									horse_1[0] = horse_1[1];
									horse_1[2] = horse_1[1];
								}
							}
							checknum = checkpoint+buf - horse_1[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[1] = 300+checkpoint;
									horse_1[0] = horse_1[1];
									horse_1[3] = horse_1[1];
									horse_1[2] = horse_1[1];
								}else{
									horse_1[1] = 200+checkpoint;
									horse_1[0] = horse_1[1];
									horse_1[3] = horse_1[1];
								}
							}
						}
					}else if( horse_1[1] == horse_1[3] ){
						if( checkpoint == 0){
							horse_1[1] = 99;
							horse_1[3] = 99;
						}else{
							horse_1[1] = checkpoint+buf;
							horse_1[3] = checkpoint+buf;
							checknum = horse_1[1] - horse_1[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[1] = 300+checkpoint;
									horse_1[0] = horse_1[1];
									horse_1[2] = horse_1[1];
									horse_1[3] = horse_1[1];
								}else{
									horse_1[1] = 200+checkpoint;
									horse_1[0] = horse_1[1];
									horse_1[3] = horse_1[1];
								}
							}
							checknum = checkpoint+buf - horse_1[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[1] = 300+checkpoint;
									horse_1[0] = horse_1[1];
									horse_1[3] = horse_1[1];
									horse_1[2] = horse_1[1];
								}else{
									horse_1[1] = 200+checkpoint;
									horse_1[0] = horse_1[1];
									horse_1[3] = horse_1[1];
								}
							}
						}
					}
				}else{
					if( checkpoint == 0 ){
						horse_1[1] = 99;
					}else{
						horse_1[1] = checkpoint;
						int comparepoint_1 = horse_1[0];
						int comparepoint_2 = horse_1[2];
						int comparepoint_3 = horse_1[3];
						checknum = checkpoint - comparepoint_1;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_1[1] <= 300 ){
								horse_1[1] = 300+checkpoint;
								horse_1[0] = horse_1[1];
								horse_1[2] = horse_1[1];
								horse_1[3] = horse_1[1];
							}else if( checknum == -100 && horse_1[1] <= 200  ){
								horse_1[1] = 200+checkpoint;
								if( horse_1[0] != horse_1[2] ){
									horse_1[0] = horse_1[1];
									horse_1[3] = horse_1[1];
								}else{
									horse_1[0] = horse_1[1];
									horse_1[2] = horse_1[1];
								}
							}else if( horse_1[1] <= 100 ){
								horse_1[1] = 100+checkpoint;
								horse_1[0] = horse_1[1];
							}
						}
						checknum = checkpoint - comparepoint_2;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_1[1] <= 300 ){
								horse_1[1] = 300+checkpoint;
								horse_1[0] = horse_1[1];
								horse_1[2] = horse_1[1];
								horse_1[3] = horse_1[1];
							}else if( checknum == -100 && horse_1[1] <= 200  ){
								horse_1[1] = 200+checkpoint;
								if( horse_1[2] != horse_1[0] ){
									horse_1[2] = horse_1[1];
									horse_1[3] = horse_1[1];
								}else{
									horse_1[2] = horse_1[1];
									horse_1[0] = horse_1[1];
								}
							}else if( horse_1[1] <= 100 ){
								horse_1[1] = 100+checkpoint;
								horse_1[2] = horse_1[1];
							}
						}
						checknum = checkpoint - comparepoint_3;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_1[1] <= 300 ){
								horse_1[1] = 300+checkpoint;
								horse_1[0] = horse_1[1];
								horse_1[2] = horse_1[1];
								horse_1[3] = horse_1[1];
							}else if( checknum == -100 && horse_1[1] <= 200  ){
								horse_1[1] = 200+checkpoint;
								if( horse_1[3] != horse_1[0] ){
									horse_1[2] = horse_1[1];
									horse_1[3] = horse_1[1];
								}else{
									horse_1[3] = horse_1[1];
									horse_1[0] = horse_1[1];
								}
							}else if( horse_1[1] <= 100 ){
								horse_1[1] = 100+checkpoint;
								horse_1[3] = horse_1[1];
							}
						}
					}
				}
				overlapCharacter(horse_1[1], member);
			}else{
				for(int i=0, goal = horse_1[1]; i<4; i++)
					if(horse_1[i] == goal) horse_1[i] = 99;
				goflag_1[1] = true;
			}
		}
		if( member == 1 ){
			int checkpoint = Checkpoint(horse_2[1], run);
			if( checkpoint != 99 ){
				if(horse_2[1] >= 300){
					buf = 300;
					if( checkpoint == 0 ){
						horse_2[0] = 99;
						horse_2[1] = 99;
						horse_2[2] = 99;
						horse_2[3] = 99;
					}else{
						horse_2[0] = checkpoint + buf;
						horse_2[1] = checkpoint + buf;
						horse_2[2] = checkpoint + buf;
						horse_2[3] = checkpoint + buf;
					}
				}else if( horse_2[1] >= 200 ){
					buf = 200;
					if( horse_2[1] != horse_2[0]){
						if( checkpoint == 0 ){
							horse_2[1] = 99;
							horse_2[2] = 99;
							horse_2[3] = 99;
						}else{
							horse_2[1] = checkpoint+buf;
							horse_2[2] = checkpoint+buf;
							horse_2[3] = checkpoint+buf;
							checknum = horse_2[1] - horse_2[0];
							if( checknum == 200){
								horse_2[1] = 300+checkpoint;
								horse_2[0] = horse_2[1];
								horse_2[2] = horse_2[1];
								horse_2[3] = horse_2[1];
							}
						}
					}else if( horse_2[1] != horse_2[2] ){
						if( checkpoint == 0 ){
							horse_2[0] = 99;
							horse_2[1] = 99;
							horse_2[3] = 99;
						}else{
							horse_2[0] = checkpoint+buf;
							horse_2[1] = checkpoint+buf;
							horse_2[3] = checkpoint+buf;
							checknum = horse_2[1] - horse_2[2];
							if( checknum == 200){
								horse_2[1] = 300+checkpoint;
								horse_2[0] = horse_2[1];
								horse_2[2] = horse_2[1];
								horse_2[3] = horse_2[1];
							}
						}
					}else{
						if( checkpoint == 0 ){
							horse_2[0] = 99;
							horse_2[1] = 99;
							horse_2[2] = 99;
						}else{
							horse_2[0] = checkpoint+buf;
							horse_2[1] = checkpoint+buf;
							horse_2[2] = checkpoint+buf;
							checknum = horse_2[1] - horse_2[3];
							if( checknum == 200){
								horse_2[1] = 300+checkpoint;
								horse_2[0] = horse_2[1];
								horse_2[2] = horse_2[1];
								horse_2[3] = horse_2[1];
							}
						}
					}
				}else if(horse_2[1] >= 100 ){
					buf = 100;
					if( horse_2[1] == horse_2[0] ){
						if( checkpoint == 0){
							horse_2[1] = 99;
							horse_2[0] = 99;
						}else{
							horse_2[1] = checkpoint+buf;
							horse_2[0] = checkpoint+buf;
							checknum = horse_2[1] - horse_2[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[1] = 300+checkpoint;
									horse_2[0] = horse_2[1];
									horse_2[2] = horse_2[1];
									horse_2[3] = horse_2[1];
								}else{
									horse_2[1] = 200+checkpoint;
									horse_2[0] = horse_2[1];
									horse_2[2] = horse_2[1];
								}
							}
							checknum = checkpoint+buf - horse_2[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[1] = 300+checkpoint;
									horse_2[0] = horse_2[1];
									horse_2[3] = horse_2[1];
									horse_2[2] = horse_2[1];
								}else{
									horse_2[1] = 200+checkpoint;
									horse_2[0] = horse_2[1];
									horse_2[3] = horse_2[1];
								}
							}
						}
					}else if( horse_2[1] == horse_2[2] ){
						if( checkpoint == 0){
							horse_2[1] = 99;
							horse_2[2] = 99;
						}else{
							horse_2[1] = checkpoint+buf;
							horse_2[2] = checkpoint+buf;
							checknum = horse_2[1] - horse_2[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[1] = 300+checkpoint;
									horse_2[0] = horse_2[1];
									horse_2[2] = horse_2[1];
									horse_2[3] = horse_2[1];
								}else{
									horse_2[1] = 200+checkpoint;
									horse_2[0] = horse_2[1];
									horse_2[2] = horse_2[1];
								}
							}
							checknum = checkpoint+buf - horse_2[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[1] = 300+checkpoint;
									horse_2[0] = horse_2[1];
									horse_2[3] = horse_2[1];
									horse_2[2] = horse_2[1];
								}else{
									horse_2[1] = 200+checkpoint;
									horse_2[0] = horse_2[1];
									horse_2[3] = horse_2[1];
								}
							}
						}
					}else if( horse_2[1] == horse_2[3] ){
						if( checkpoint == 0){
							horse_2[1] = 99;
							horse_2[3] = 99;
						}else{
							horse_2[1] = checkpoint+buf;
							horse_2[3] = checkpoint+buf;
							checknum = horse_2[1] - horse_2[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[1] = 300+checkpoint;
									horse_2[0] = horse_2[1];
									horse_2[2] = horse_2[1];
									horse_2[3] = horse_2[1];
								}else{
									horse_2[1] = 200+checkpoint;
									horse_2[0] = horse_2[1];
									horse_2[3] = horse_2[1];
								}
							}
							checknum = checkpoint+buf - horse_2[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[1] = 300+checkpoint;
									horse_2[0] = horse_2[1];
									horse_2[3] = horse_2[1];
									horse_2[2] = horse_2[1];
								}else{
									horse_2[1] = 200+checkpoint;
									horse_2[0] = horse_2[1];
									horse_2[3] = horse_2[1];
								}
							}
						}
					}
				}else{
					if( checkpoint == 0 ){
						horse_2[1] = 99;
					}else{
						horse_2[1] = checkpoint;
						int comparepoint_1 = horse_2[0];
						int comparepoint_2 = horse_2[2];
						int comparepoint_3 = horse_2[3];
						checknum = checkpoint - comparepoint_1;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_2[1] <= 300 ){
								horse_2[1] = 300+checkpoint;
								horse_2[0] = horse_2[1];
								horse_2[2] = horse_2[1];
								horse_2[3] = horse_2[1];
							}else if( checknum == -100 && horse_2[1] <= 200  ){
								horse_2[1] = 200+checkpoint;
								if( horse_2[0] != horse_2[2] ){
									horse_2[0] = horse_2[1];
									horse_2[3] = horse_2[1];
								}else{
									horse_2[0] = horse_2[1];
									horse_2[2] = horse_2[1];
								}
							}else if( horse_2[1] <= 100 ){
								horse_2[1] = 100+checkpoint;
								horse_2[0] = horse_2[1];
							}
						}
						checknum = checkpoint - comparepoint_2;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_2[1] <= 300 ){
								horse_2[1] = 300+checkpoint;
								horse_2[0] = horse_2[1];
								horse_2[2] = horse_2[1];
								horse_2[3] = horse_2[1];
							}else if( checknum == -100 && horse_2[1] <= 200  ){
								horse_2[1] = 200+checkpoint;
								if( horse_2[2] != horse_2[0] ){
									horse_2[2] = horse_2[1];
									horse_2[3] = horse_2[1];
								}else{
									horse_2[2] = horse_2[1];
									horse_2[0] = horse_2[1];
								}
							}else if( horse_2[1] <= 100 ){
								horse_2[1] = 100+checkpoint;
								horse_2[2] = horse_2[1];
							}
						}
						checknum = checkpoint - comparepoint_3;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_2[1] <= 300 ){
								horse_2[1] = 300+checkpoint;
								horse_2[0] = horse_2[1];
								horse_2[2] = horse_2[1];
								horse_2[3] = horse_2[1];
							}else if( checknum == -100 && horse_2[1] <= 200  ){
								horse_2[1] = 200+checkpoint;
								if( horse_2[3] != horse_2[0] ){
									horse_2[2] = horse_2[1];
									horse_2[3] = horse_2[1];
								}else{
									horse_2[3] = horse_2[1];
									horse_2[0] = horse_2[1];
								}
							}else if( horse_2[1] <= 100 ){
								horse_2[1] = 100+checkpoint;
								horse_2[3] = horse_2[1];
							}
						}
					}
				}
				overlapCharacter(horse_2[1], member);
			}else{
				for(int i=0, goal = horse_2[1]; i<4; i++)
					if(horse_2[i] == goal) horse_2[i] = 99;
				goflag_2[1] = true;
			}
		}
		if( member == 2 ){
			int checkpoint = Checkpoint(horse_3[1], run);
			if( checkpoint != 99 ){
				if(horse_3[1] >= 300){
					buf = 300;
					if( checkpoint == 0 ){
						horse_3[0] = 99;
						horse_3[1] = 99;
						horse_3[2] = 99;
						horse_3[3] = 99;
					}else{
						horse_3[0] = checkpoint + buf;
						horse_3[1] = checkpoint + buf;
						horse_3[2] = checkpoint + buf;
						horse_3[3] = checkpoint + buf;
					}
				}else if( horse_3[1] >= 200 ){
					buf = 200;
					if( horse_3[1] != horse_3[0]){
						if( checkpoint == 0 ){
							horse_3[1] = 99;
							horse_3[2] = 99;
							horse_3[3] = 99;
						}else{
							horse_3[1] = checkpoint+buf;
							horse_3[2] = checkpoint+buf;
							horse_3[3] = checkpoint+buf;
							checknum = horse_3[1] - horse_3[0];
							if( checknum == 200){
								horse_3[1] = 300+checkpoint;
								horse_3[0] = horse_3[1];
								horse_3[2] = horse_3[1];
								horse_3[3] = horse_3[1];
							}
						}
					}else if( horse_3[1] != horse_3[2] ){
						if( checkpoint == 0 ){
							horse_3[0] = 99;
							horse_3[1] = 99;
							horse_3[3] = 99;
						}else{
							horse_3[0] = checkpoint+buf;
							horse_3[1] = checkpoint+buf;
							horse_3[3] = checkpoint+buf;
							checknum = horse_3[1] - horse_3[2];
							if( checknum == 200){
								horse_3[1] = 300+checkpoint;
								horse_3[0] = horse_3[1];
								horse_3[2] = horse_3[1];
								horse_3[3] = horse_3[1];
							}
						}
					}else{
						if( checkpoint == 0 ){
							horse_3[0] = 99;
							horse_3[1] = 99;
							horse_3[2] = 99;
						}else{
							horse_3[0] = checkpoint+buf;
							horse_3[1] = checkpoint+buf;
							horse_3[2] = checkpoint+buf;
							checknum = horse_3[1] - horse_3[3];
							if( checknum == 200){
								horse_3[1] = 300+checkpoint;
								horse_3[0] = horse_3[1];
								horse_3[2] = horse_3[1];
								horse_3[3] = horse_3[1];
							}
						}
					}
				}else if(horse_3[1] >= 100 ){
					buf = 100;
					if( horse_3[1] == horse_3[0] ){
						if( checkpoint == 0){
							horse_3[1] = 99;
							horse_3[0] = 99;
						}else{
							horse_3[1] = checkpoint+buf;
							horse_3[0] = checkpoint+buf;
							checknum = horse_3[1] - horse_3[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[1] = 300+checkpoint;
									horse_3[0] = horse_3[1];
									horse_3[2] = horse_3[1];
									horse_3[3] = horse_3[1];
								}else{
									horse_3[1] = 200+checkpoint;
									horse_3[0] = horse_3[1];
									horse_3[2] = horse_3[1];
								}
							}
							checknum = checkpoint+buf - horse_3[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[1] = 300+checkpoint;
									horse_3[0] = horse_3[1];
									horse_3[3] = horse_3[1];
									horse_3[2] = horse_3[1];
								}else{
									horse_3[1] = 200+checkpoint;
									horse_3[0] = horse_3[1];
									horse_3[3] = horse_3[1];
								}
							}
						}
					}else if( horse_3[1] == horse_3[2] ){
						if( checkpoint == 0){
							horse_3[1] = 99;
							horse_3[2] = 99;
						}else{
							horse_3[1] = checkpoint+buf;
							horse_3[2] = checkpoint+buf;
							checknum = horse_3[1] - horse_3[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[1] = 300+checkpoint;
									horse_3[0] = horse_3[1];
									horse_3[2] = horse_3[1];
									horse_3[3] = horse_3[1];
								}else{
									horse_3[1] = 200+checkpoint;
									horse_3[0] = horse_3[1];
									horse_3[2] = horse_3[1];
								}
							}
							checknum = checkpoint+buf - horse_3[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[1] = 300+checkpoint;
									horse_3[0] = horse_3[1];
									horse_3[3] = horse_3[1];
									horse_3[2] = horse_3[1];
								}else{
									horse_3[1] = 200+checkpoint;
									horse_3[0] = horse_3[1];
									horse_3[3] = horse_3[1];
								}
							}
						}
					}else if( horse_3[1] == horse_3[3] ){
						if( checkpoint == 0){
							horse_3[1] = 99;
							horse_3[3] = 99;
						}else{
							horse_3[1] = checkpoint+buf;
							horse_3[3] = checkpoint+buf;
							checknum = horse_3[1] - horse_3[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[1] = 300+checkpoint;
									horse_3[0] = horse_3[1];
									horse_3[2] = horse_3[1];
									horse_3[3] = horse_3[1];
								}else{
									horse_3[1] = 200+checkpoint;
									horse_3[0] = horse_3[1];
									horse_3[3] = horse_3[1];
								}
							}
							checknum = checkpoint+buf - horse_3[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[1] = 300+checkpoint;
									horse_3[0] = horse_3[1];
									horse_3[3] = horse_3[1];
									horse_3[2] = horse_3[1];
								}else{
									horse_3[1] = 200+checkpoint;
									horse_3[0] = horse_3[1];
									horse_3[3] = horse_3[1];
								}
							}
						}
					}
				}else{
					if( checkpoint == 0 ){
						horse_3[1] = 99;
					}else{
						horse_3[1] = checkpoint;
						int comparepoint_1 = horse_3[0];
						int comparepoint_2 = horse_3[2];
						int comparepoint_3 = horse_3[3];
						checknum = checkpoint - comparepoint_1;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_3[1] <= 300 ){
								horse_3[1] = 300+checkpoint;
								horse_3[0] = horse_3[1];
								horse_3[2] = horse_3[1];
								horse_3[3] = horse_3[1];
							}else if( checknum == -100 && horse_3[1] <= 200  ){
								horse_3[1] = 200+checkpoint;
								if( horse_3[0] != horse_3[2] ){
									horse_3[0] = horse_3[1];
									horse_3[3] = horse_3[1];
								}else{
									horse_3[0] = horse_3[1];
									horse_3[2] = horse_3[1];
								}
							}else if( horse_3[1] <= 100 ){
								horse_3[1] = 100+checkpoint;
								horse_3[0] = horse_3[1];
							}
						}
						checknum = checkpoint - comparepoint_2;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_3[1] <= 300 ){
								horse_3[1] = 300+checkpoint;
								horse_3[0] = horse_3[1];
								horse_3[2] = horse_3[1];
								horse_3[3] = horse_3[1];
							}else if( checknum == -100 && horse_3[1] <= 200  ){
								horse_3[1] = 200+checkpoint;
								if( horse_3[2] != horse_3[0] ){
									horse_3[2] = horse_3[1];
									horse_3[3] = horse_3[1];
								}else{
									horse_3[2] = horse_3[1];
									horse_3[0] = horse_3[1];
								}
							}else if( horse_3[1] <= 100 ){
								horse_3[1] = 100+checkpoint;
								horse_3[2] = horse_3[1];
							}
						}
						checknum = checkpoint - comparepoint_3;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_3[1] <= 300 ){
								horse_3[1] = 300+checkpoint;
								horse_3[0] = horse_3[1];
								horse_3[2] = horse_3[1];
								horse_3[3] = horse_3[1];
							}else if( checknum == -100 && horse_3[1] <= 200  ){
								horse_3[1] = 200+checkpoint;
								if( horse_3[3] != horse_3[0] ){
									horse_3[2] = horse_3[1];
									horse_3[3] = horse_3[1];
								}else{
									horse_3[3] = horse_3[1];
									horse_3[0] = horse_3[1];
								}
							}else if( horse_3[1] <= 100 ){
								horse_3[1] = 100+checkpoint;
								horse_3[3] = horse_3[1];
							}
						}
					}
				}
				overlapCharacter(horse_3[1], member);
			}else{
				for(int i=0, goal = horse_3[1]; i<4; i++)
					if(horse_3[i] == goal) horse_3[i] = 99;
				goflag_3[1] = true;
			}
		}
		if( member == 3 ){
			int checkpoint = Checkpoint(horse_4[1], run);
			if( checkpoint != 99 ){
				if(horse_4[1] >= 300){
					buf = 300;
					if( checkpoint == 0 ){
						horse_4[0] = 99;
						horse_4[1] = 99;
						horse_4[2] = 99;
						horse_4[3] = 99;
					}else{
						horse_4[0] = checkpoint + buf;
						horse_4[1] = checkpoint + buf;
						horse_4[2] = checkpoint + buf;
						horse_4[3] = checkpoint + buf;
					}
				}else if( horse_4[1] >= 200 ){
					buf = 200;
					if( horse_4[1] != horse_4[0]){
						if( checkpoint == 0 ){
							horse_4[1] = 99;
							horse_4[2] = 99;
							horse_4[3] = 99;
						}else{
							horse_4[1] = checkpoint+buf;
							horse_4[2] = checkpoint+buf;
							horse_4[3] = checkpoint+buf;
							checknum = horse_4[1] - horse_4[0];
							if( checknum == 200){
								horse_4[1] = 300+checkpoint;
								horse_4[0] = horse_4[1];
								horse_4[2] = horse_4[1];
								horse_4[3] = horse_4[1];
							}
						}
					}else if( horse_4[1] != horse_4[2] ){
						if( checkpoint == 0 ){
							horse_4[0] = 99;
							horse_4[1] = 99;
							horse_4[3] = 99;
						}else{
							horse_4[0] = checkpoint+buf;
							horse_4[1] = checkpoint+buf;
							horse_4[3] = checkpoint+buf;
							checknum = horse_4[1] - horse_4[2];
							if( checknum == 200){
								horse_4[1] = 300+checkpoint;
								horse_4[0] = horse_4[1];
								horse_4[2] = horse_4[1];
								horse_4[3] = horse_4[1];
							}
						}
					}else{
						if( checkpoint == 0 ){
							horse_4[0] = 99;
							horse_4[1] = 99;
							horse_4[2] = 99;
						}else{
							horse_4[0] = checkpoint+buf;
							horse_4[1] = checkpoint+buf;
							horse_4[2] = checkpoint+buf;
							checknum = horse_4[1] - horse_4[3];
							if( checknum == 200){
								horse_4[1] = 300+checkpoint;
								horse_4[0] = horse_4[1];
								horse_4[2] = horse_4[1];
								horse_4[3] = horse_4[1];
							}
						}
					}
				}else if(horse_4[1] >= 100 ){
					buf = 100;
					if( horse_4[1] == horse_4[0] ){
						if( checkpoint == 0){
							horse_4[1] = 99;
							horse_4[0] = 99;
						}else{
							horse_4[1] = checkpoint+buf;
							horse_4[0] = checkpoint+buf;
							checknum = horse_4[1] - horse_4[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[1] = 300+checkpoint;
									horse_4[0] = horse_4[1];
									horse_4[2] = horse_4[1];
									horse_4[3] = horse_4[1];
								}else{
									horse_4[1] = 200+checkpoint;
									horse_4[0] = horse_4[1];
									horse_4[2] = horse_4[1];
								}
							}
							checknum = checkpoint+buf - horse_4[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[1] = 300+checkpoint;
									horse_4[0] = horse_4[1];
									horse_4[3] = horse_4[1];
									horse_4[2] = horse_4[1];
								}else{
									horse_4[1] = 200+checkpoint;
									horse_4[0] = horse_4[1];
									horse_4[3] = horse_4[1];
								}
							}
						}
					}else if( horse_4[1] == horse_4[2] ){
						if( checkpoint == 0){
							horse_4[1] = 99;
							horse_4[2] = 99;
						}else{
							horse_4[1] = checkpoint+buf;
							horse_4[2] = checkpoint+buf;
							checknum = horse_4[1] - horse_4[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[1] = 300+checkpoint;
									horse_4[0] = horse_4[1];
									horse_4[2] = horse_4[1];
									horse_4[3] = horse_4[1];
								}else{
									horse_4[1] = 200+checkpoint;
									horse_4[0] = horse_4[1];
									horse_4[2] = horse_4[1];
								}
							}
							checknum = checkpoint+buf - horse_4[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[1] = 300+checkpoint;
									horse_4[0] = horse_4[1];
									horse_4[3] = horse_4[1];
									horse_4[2] = horse_4[1];
								}else{
									horse_4[1] = 200+checkpoint;
									horse_4[0] = horse_4[1];
									horse_4[3] = horse_4[1];
								}
							}
						}
					}else if( horse_4[1] == horse_4[3] ){
						if( checkpoint == 0){
							horse_4[1] = 99;
							horse_4[3] = 99;
						}else{
							horse_4[1] = checkpoint+buf;
							horse_4[3] = checkpoint+buf;
							checknum = horse_4[1] - horse_4[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[1] = 300+checkpoint;
									horse_4[0] = horse_4[1];
									horse_4[2] = horse_4[1];
									horse_4[3] = horse_4[1];
								}else{
									horse_4[1] = 200+checkpoint;
									horse_4[0] = horse_4[1];
									horse_4[3] = horse_4[1];
								}
							}
							checknum = checkpoint+buf - horse_4[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[1] = 300+checkpoint;
									horse_4[0] = horse_4[1];
									horse_4[3] = horse_4[1];
									horse_4[2] = horse_4[1];
								}else{
									horse_4[1] = 200+checkpoint;
									horse_4[0] = horse_4[1];
									horse_4[3] = horse_4[1];
								}
							}
						}
					}
				}else{
					if( checkpoint == 0 ){
						horse_4[1] = 99;
					}else{
						horse_4[1] = checkpoint;
						int comparepoint_1 = horse_4[0];
						int comparepoint_2 = horse_4[2];
						int comparepoint_3 = horse_4[3];
						checknum = checkpoint - comparepoint_1;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_4[1] <= 300 ){
								horse_4[1] = 300+checkpoint;
								horse_4[0] = horse_4[1];
								horse_4[2] = horse_4[1];
								horse_4[3] = horse_4[1];
							}else if( checknum == -100 && horse_4[1] <= 200  ){
								horse_4[1] = 200+checkpoint;
								if( horse_4[0] != horse_4[2] ){
									horse_4[0] = horse_4[1];
									horse_4[3] = horse_4[1];
								}else{
									horse_4[0] = horse_4[1];
									horse_4[2] = horse_4[1];
								}
							}else if( horse_4[1] <= 100 ){
								horse_4[1] = 100+checkpoint;
								horse_4[0] = horse_4[1];
							}
						}
						checknum = checkpoint - comparepoint_2;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_4[1] <= 300 ){
								horse_4[1] = 300+checkpoint;
								horse_4[0] = horse_4[1];
								horse_4[2] = horse_4[1];
								horse_4[3] = horse_4[1];
							}else if( checknum == -100 && horse_4[1] <= 200  ){
								horse_4[1] = 200+checkpoint;
								if( horse_4[2] != horse_4[0] ){
									horse_4[2] = horse_4[1];
									horse_4[3] = horse_4[1];
								}else{
									horse_4[2] = horse_4[1];
									horse_4[0] = horse_4[1];
								}
							}else if( horse_4[1] <= 100 ){
								horse_4[1] = 100+checkpoint;
								horse_4[2] = horse_4[1];
							}
						}
						checknum = checkpoint - comparepoint_3;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200  && horse_4[1] <= 300 ){
								horse_4[1] = 300+checkpoint;
								horse_4[0] = horse_4[1];
								horse_4[2] = horse_4[1];
								horse_4[3] = horse_4[1];
							}else if( checknum == -100 && horse_4[1] <= 200  ){
								horse_4[1] = 200+checkpoint;
								if( horse_4[3] != horse_4[0] ){
									horse_4[2] = horse_4[1];
									horse_4[3] = horse_4[1];
								}else{
									horse_4[3] = horse_4[1];
									horse_4[0] = horse_4[1];
								}
							}else if( horse_4[1] <= 100 ){
								horse_4[1] = 100+checkpoint;
								horse_4[3] = horse_4[1];
							}
						}
					}
				}
				overlapCharacter(horse_4[1], member);
			}else{
				for(int i=0, goal = horse_4[1]; i<4; i++)
					if(horse_4[i] == goal) horse_4[i] = 99;
				goflag_4[1] = true;
			}
		}
		run = 0;
		turn = (turn+1)%4;
		cs.HorseAndTurnUpdate();
	}
	public void setGoButton3(){
		int buf = 0;
		int checknum = 0;
		if( member == 0 ){
			int checkpoint = Checkpoint(horse_1[2], run);
			if( checkpoint != 99 ){
				if(horse_1[2] >= 300){
					buf = 300;
					if( checkpoint == 0 ){
						horse_1[0] = 99;
						horse_1[1] = 99;
						horse_1[2] = 99;
						horse_1[3] = 99;
					}else{
						horse_1[0] = checkpoint + buf;
						horse_1[1] = checkpoint + buf;
						horse_1[2] = checkpoint + buf;
						horse_1[3] = checkpoint + buf;
					}
				}else if( horse_1[2] >= 200 ){
					buf = 200;
					if( horse_1[2] != horse_1[0]){
						if( checkpoint == 0 ){
							horse_1[1] = 99;
							horse_1[2] = 99;
							horse_1[3] = 99;
						}else{
							horse_1[1] = checkpoint+buf;
							horse_1[2] = checkpoint+buf;
							horse_1[3] = checkpoint+buf;
							checknum = horse_1[2] - horse_1[0];
							if( checknum == 200){
								horse_1[2] = 300+checkpoint;
								horse_1[0] = horse_1[2];
								horse_1[1] = horse_1[2];
								horse_1[3] = horse_1[2];
							}
						}
					}else if( horse_1[2] != horse_1[1] ){
						if( checkpoint == 0 ){
							horse_1[0] = 99;
							horse_1[2] = 99;
							horse_1[3] = 99;
						}else{
							horse_1[0] = checkpoint+buf;
							horse_1[2] = checkpoint+buf;
							horse_1[3] = checkpoint+buf;
							checknum = horse_1[2] - horse_1[1];
							if( checknum == 200){
								horse_1[2] = 300+checkpoint;
								horse_1[0] = horse_1[2];
								horse_1[1] = horse_1[2];
								horse_1[3] = horse_1[2];
							}
						}
					}else{
						if( checkpoint == 0 ){
							horse_1[0] = 99;
							horse_1[1] = 99;
							horse_1[2] = 99;
						}else{
							horse_1[0] = checkpoint+buf;
							horse_1[1] = checkpoint+buf;
							horse_1[2] = checkpoint+buf;
							checknum = horse_1[2] - horse_1[3];
							if( checknum == 200){
								horse_1[2] = 300+checkpoint;
								horse_1[0] = horse_1[2];
								horse_1[1] = horse_1[2];
								horse_1[3] = horse_1[2];
							}
						}
					}
				}else if(horse_1[2] >= 100 ){
					buf = 100;
					if( horse_1[2] == horse_1[0] ){
						if( checkpoint == 0){
							horse_1[2] = 99;
							horse_1[0] = 99;
						}else{
							horse_1[2] = checkpoint+buf;
							horse_1[0] = checkpoint+buf;
							checknum = horse_1[2] - horse_1[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[2] = 300+checkpoint;
									horse_1[0] = horse_1[2];
									horse_1[1] = horse_1[2];
									horse_1[3] = horse_1[2];
								}else{
									horse_1[2] = 200+checkpoint;
									horse_1[0] = horse_1[2];
									horse_1[1] = horse_1[2];
								}
							}
							checknum = checkpoint+buf - horse_1[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[2] = 300+checkpoint;
									horse_1[0] = horse_1[2];
									horse_1[1] = horse_1[2];
									horse_1[3] = horse_1[2];
								}else{
									horse_1[2] = 200+checkpoint;
									horse_1[0] = horse_1[2];
									horse_1[3] = horse_1[2];
								}
							}
						}
					}else if( horse_1[2] == horse_1[1] ){
						if( checkpoint == 0){
							horse_1[2] = 99;
							horse_1[1] = 99;
						}else{
							horse_1[2] = checkpoint+buf;
							horse_1[1] = checkpoint+buf;
							checknum = horse_1[2] - horse_1[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[2] = 300+checkpoint;
									horse_1[1] = horse_1[2];
									horse_1[0] = horse_1[2];
									horse_1[3] = horse_1[2];
								}else{
									horse_1[2] = 200+checkpoint;
									horse_1[1] = horse_1[2];
									horse_1[0] = horse_1[2];
								}
							}
							checknum = checkpoint+buf - horse_1[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[2] = 300+checkpoint;
									horse_1[1] = horse_1[2];
									horse_1[0] = horse_1[2];
									horse_1[3] = horse_1[2];
								}else{
									horse_1[2] = 200+checkpoint;
									horse_1[1] = horse_1[2];
									horse_1[3] = horse_1[2];
								}
							}
						}
					}else if( horse_1[2] == horse_1[3] ){
						if( checkpoint == 0){
							horse_1[2] = 99;
							horse_1[3] = 99;
						}else{
							horse_1[2] = checkpoint+buf;
							horse_1[3] = checkpoint+buf;
							checknum = horse_1[2] - horse_1[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[2] = 300+checkpoint;
									horse_1[3] = horse_1[2];
									horse_1[0] = horse_1[2];
									horse_1[1] = horse_1[2];
								}else{
									horse_1[2] = 200+checkpoint;
									horse_1[3] = horse_1[2];
									horse_1[0] = horse_1[2];
								}
							}
							checknum = checkpoint+buf - horse_1[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[2] = 300+checkpoint;
									horse_1[3] = horse_1[2];
									horse_1[0] = horse_1[2];
									horse_1[1] = horse_1[2];
								}else{
									horse_1[2] = 200+checkpoint;
									horse_1[3] = horse_1[2];
									horse_1[1] = horse_1[2];
								}
							}
						}
					}
				}else{
					if( checkpoint == 0){
						horse_1[2] = 99;
					}else{
						horse_1[2] = checkpoint;
						int comparepoint_1 = horse_1[0];
						int comparepoint_2 = horse_1[1];
						int comparepoint_3 = horse_1[3];
						checknum = checkpoint - comparepoint_1;
						if( checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_1[2] <= 300 ){
								
								horse_1[2] = 300+checkpoint;
								horse_1[0] = horse_1[2];
								horse_1[1] = horse_1[2];
								horse_1[3] = horse_1[2];
							}else if( checknum == -100 && horse_1[2] <= 200  ){
							
								horse_1[2] = 200+checkpoint;
								if( horse_1[0] != horse_1[1] ){
									horse_1[0] = horse_1[2];
									horse_1[3] = horse_1[2];
								}else{
									horse_1[0] = horse_1[2];
									horse_1[1] = horse_1[2];
								}
							}else if( horse_1[2] <= 100 ){
						
								horse_1[2] = 100+checkpoint;
								horse_1[0] = horse_1[2];
							}
						}
						checknum = checkpoint - comparepoint_2;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_1[2] <= 300 ){
								horse_1[2] = 300+checkpoint;
								horse_1[0] = horse_1[2];
								horse_1[1] = horse_1[2];
								horse_1[3] = horse_1[2];
							}else if( checknum == -100 && horse_1[2] <= 200  ){
								horse_1[2] = 200+checkpoint;
								if( horse_1[1] != horse_1[0] ){
									horse_1[1] = horse_1[2];
									horse_1[3] = horse_1[2];
								}else{
									horse_1[1] = horse_1[2];
									horse_1[0] = horse_1[2];
								}
							}else if( horse_1[2] <= 100 ){
								
								horse_1[2] = 100+checkpoint;
								horse_1[1] = horse_1[2];
							}
						}
						checknum = checkpoint -comparepoint_3;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_1[2] <= 300 ){
								horse_1[2] = 300+checkpoint;
								horse_1[0] = horse_1[2];
								horse_1[1] = horse_1[2];
								horse_1[3] = horse_1[2];
							}else if( checknum == -100 && horse_1[2] <= 200  ){
								horse_1[2] = 200+checkpoint;
								if( horse_1[3] != horse_1[0] ){
									horse_1[3] = horse_1[2];
									horse_1[1] = horse_1[2];
								}else{
									horse_1[3] = horse_1[2];
									horse_1[0] = horse_1[2];
								}
							}else if( horse_1[2] <= 100 ){
								horse_1[2] = 100+checkpoint;
								horse_1[3] = horse_1[2];
							}
						}
					}
				}
				overlapCharacter(horse_1[2], member);
			}else{
				for(int i=0, goal = horse_1[2]; i<4; i++)
					if(horse_1[i] == goal) horse_1[i] = 99;
				goflag_1[2] = true;
			}
		}
		if( member == 1 ){
			int checkpoint = Checkpoint(horse_2[2], run);
			if( checkpoint != 99 ){
				if(horse_2[2] >= 300){
					buf = 300;
					if( checkpoint == 0 ){
						horse_2[0] = 99;
						horse_2[1] = 99;
						horse_2[2] = 99;
						horse_2[3] = 99;
					}else{
						horse_2[0] = checkpoint + buf;
						horse_2[1] = checkpoint + buf;
						horse_2[2] = checkpoint + buf;
						horse_2[3] = checkpoint + buf;
					}
				}else if( horse_2[2] >= 200 ){
					buf = 200;
					if( horse_2[2] != horse_2[0]){
						if( checkpoint == 0 ){
							horse_2[1] = 99;
							horse_2[2] = 99;
							horse_2[3] = 99;
						}else{
							horse_2[1] = checkpoint+buf;
							horse_2[2] = checkpoint+buf;
							horse_2[3] = checkpoint+buf;
							checknum = horse_2[2] - horse_2[0];
							if( checknum == 200){
								horse_2[2] = 300+checkpoint;
								horse_2[0] = horse_2[2];
								horse_2[1] = horse_2[2];
								horse_2[3] = horse_2[2];
							}
						}
					}else if( horse_2[2] != horse_2[1] ){
						if( checkpoint == 0 ){
							horse_2[0] = 99;
							horse_2[2] = 99;
							horse_2[3] = 99;
						}else{
							horse_2[0] = checkpoint+buf;
							horse_2[2] = checkpoint+buf;
							horse_2[3] = checkpoint+buf;
							checknum = horse_2[2] - horse_2[1];
							if( checknum == 200){
								horse_2[2] = 300+checkpoint;
								horse_2[0] = horse_2[2];
								horse_2[1] = horse_2[2];
								horse_2[3] = horse_2[2];
							}
						}
					}else{
						if( checkpoint == 0 ){
							horse_2[0] = 99;
							horse_2[1] = 99;
							horse_2[2] = 99;
						}else{
							horse_2[0] = checkpoint+buf;
							horse_2[1] = checkpoint+buf;
							horse_2[2] = checkpoint+buf;
							checknum = horse_2[2] - horse_2[3];
							if( checknum == 200){
								horse_2[2] = 300+checkpoint;
								horse_2[0] = horse_2[2];
								horse_2[1] = horse_2[2];
								horse_2[3] = horse_2[2];
							}
						}
					}
				}else if(horse_2[2] >= 100 ){
					buf = 100;
					if( horse_2[2] == horse_2[0] ){
						if( checkpoint == 0){
							horse_2[2] = 99;
							horse_2[0] = 99;
						}else{
							horse_2[2] = checkpoint+buf;
							horse_2[0] = checkpoint+buf;
							checknum = horse_2[2] - horse_2[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[2] = 300+checkpoint;
									horse_2[0] = horse_2[2];
									horse_2[1] = horse_2[2];
									horse_2[3] = horse_2[2];
								}else{
									horse_2[2] = 200+checkpoint;
									horse_2[0] = horse_2[2];
									horse_2[1] = horse_2[2];
								}
							}
							checknum = checkpoint+buf - horse_2[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[2] = 300+checkpoint;
									horse_2[0] = horse_2[2];
									horse_2[1] = horse_2[2];
									horse_2[3] = horse_2[2];
								}else{
									horse_2[2] = 200+checkpoint;
									horse_2[0] = horse_2[2];
									horse_2[3] = horse_2[2];
								}
							}
						}
					}else if( horse_2[2] == horse_2[1] ){
						if( checkpoint == 0){
							horse_2[2] = 99;
							horse_2[1] = 99;
						}else{
							horse_2[2] = checkpoint+buf;
							horse_2[1] = checkpoint+buf;
							checknum = horse_2[2] - horse_2[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[2] = 300+checkpoint;
									horse_2[1] = horse_2[2];
									horse_2[0] = horse_2[2];
									horse_2[3] = horse_2[2];
								}else{
									horse_2[2] = 200+checkpoint;
									horse_2[1] = horse_2[2];
									horse_2[0] = horse_2[2];
								}
							}
							checknum = checkpoint+buf - horse_2[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[2] = 300+checkpoint;
									horse_2[1] = horse_2[2];
									horse_2[0] = horse_2[2];
									horse_2[3] = horse_2[2];
								}else{
									horse_2[2] = 200+checkpoint;
									horse_2[1] = horse_2[2];
									horse_2[3] = horse_2[2];
								}
							}
						}
					}else if( horse_2[2] == horse_2[3] ){
						if( checkpoint == 0){
							horse_2[2] = 99;
							horse_2[3] = 99;
						}else{
							horse_2[2] = checkpoint+buf;
							horse_2[3] = checkpoint+buf;
							checknum = horse_2[2] - horse_2[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[2] = 300+checkpoint;
									horse_2[3] = horse_2[2];
									horse_2[0] = horse_2[2];
									horse_2[1] = horse_2[2];
								}else{
									horse_2[2] = 200+checkpoint;
									horse_2[3] = horse_2[2];
									horse_2[0] = horse_2[2];
								}
							}
							checknum = checkpoint+buf - horse_2[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[2] = 300+checkpoint;
									horse_2[3] = horse_2[2];
									horse_2[0] = horse_2[2];
									horse_2[1] = horse_2[2];
								}else{
									horse_2[2] = 200+checkpoint;
									horse_2[3] = horse_2[2];
									horse_2[1] = horse_2[2];
								}
							}
						}
					}
				}else{
					if( checkpoint == 0){
						horse_2[2] = 99;
					}else{
						horse_2[2] = checkpoint;
						int comparepoint_1 = horse_2[0];
						int comparepoint_2 = horse_2[1];
						int comparepoint_3 = horse_2[3];
						checknum = checkpoint - comparepoint_1;
						if( checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_2[2] <= 300 ){
								
								horse_2[2] = 300+checkpoint;
								horse_2[0] = horse_2[2];
								horse_2[1] = horse_2[2];
								horse_2[3] = horse_2[2];
							}else if( checknum == -100 && horse_2[2] <= 200  ){
							
								horse_2[2] = 200+checkpoint;
								if( horse_2[0] != horse_2[1] ){
									horse_2[0] = horse_2[2];
									horse_2[3] = horse_2[2];
								}else{
									horse_2[0] = horse_2[2];
									horse_2[1] = horse_2[2];
								}
							}else if( horse_2[2] <= 100 ){
						
								horse_2[2] = 100+checkpoint;
								horse_2[0] = horse_2[2];
							}
						}
						checknum = checkpoint - comparepoint_2;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_2[2] <= 300 ){
								horse_2[2] = 300+checkpoint;
								horse_2[0] = horse_2[2];
								horse_2[1] = horse_2[2];
								horse_2[3] = horse_2[2];
							}else if( checknum == -100 && horse_2[2] <= 200  ){
								horse_2[2] = 200+checkpoint;
								if( horse_2[1] != horse_2[0] ){
									horse_2[1] = horse_2[2];
									horse_2[3] = horse_2[2];
								}else{
									horse_2[1] = horse_2[2];
									horse_2[0] = horse_2[2];
								}
							}else if( horse_2[2] <= 100 ){
								
								horse_2[2] = 100+checkpoint;
								horse_2[1] = horse_2[2];
							}
						}
						checknum = checkpoint -comparepoint_3;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_2[2] <= 300 ){
								horse_2[2] = 300+checkpoint;
								horse_2[0] = horse_2[2];
								horse_2[1] = horse_2[2];
								horse_2[3] = horse_2[2];
							}else if( checknum == -100 && horse_2[2] <= 200  ){
								horse_2[2] = 200+checkpoint;
								if( horse_2[3] != horse_2[0] ){
									horse_2[3] = horse_2[2];
									horse_2[1] = horse_2[2];
								}else{
									horse_2[3] = horse_2[2];
									horse_2[0] = horse_2[2];
								}
							}else if( horse_2[2] <= 100 ){
								horse_2[2] = 100+checkpoint;
								horse_2[3] = horse_2[2];
							}
						}
					}
				}
				overlapCharacter(horse_2[2], member);
			}else{
				for(int i=0, goal = horse_2[2]; i<4; i++)
					if(horse_2[i] == goal) horse_2[i] = 99;
				goflag_2[2] = true;
			}
		}
		if( member == 2 ){
			int checkpoint = Checkpoint(horse_3[2], run);
			if( checkpoint != 99 ){
				if(horse_3[2] >= 300){
					buf = 300;
					if( checkpoint == 0 ){
						horse_3[0] = 99;
						horse_3[1] = 99;
						horse_3[2] = 99;
						horse_3[3] = 99;
					}else{
						horse_3[0] = checkpoint + buf;
						horse_3[1] = checkpoint + buf;
						horse_3[2] = checkpoint + buf;
						horse_3[3] = checkpoint + buf;
					}
				}else if( horse_3[2] >= 200 ){
					buf = 200;
					if( horse_3[2] != horse_3[0]){
						if( checkpoint == 0 ){
							horse_3[1] = 99;
							horse_3[2] = 99;
							horse_3[3] = 99;
						}else{
							horse_3[1] = checkpoint+buf;
							horse_3[2] = checkpoint+buf;
							horse_3[3] = checkpoint+buf;
							checknum = horse_3[2] - horse_3[0];
							if( checknum == 200){
								horse_3[2] = 300+checkpoint;
								horse_3[0] = horse_3[2];
								horse_3[1] = horse_3[2];
								horse_3[3] = horse_3[2];
							}
						}
					}else if( horse_3[2] != horse_3[1] ){
						if( checkpoint == 0 ){
							horse_3[0] = 99;
							horse_3[2] = 99;
							horse_3[3] = 99;
						}else{
							horse_3[0] = checkpoint+buf;
							horse_3[2] = checkpoint+buf;
							horse_3[3] = checkpoint+buf;
							checknum = horse_3[2] - horse_3[1];
							if( checknum == 200){
								horse_3[2] = 300+checkpoint;
								horse_3[0] = horse_3[2];
								horse_3[1] = horse_3[2];
								horse_3[3] = horse_3[2];
							}
						}
					}else{
						if( checkpoint == 0 ){
							horse_3[0] = 99;
							horse_3[1] = 99;
							horse_3[2] = 99;
						}else{
							horse_3[0] = checkpoint+buf;
							horse_3[1] = checkpoint+buf;
							horse_3[2] = checkpoint+buf;
							checknum = horse_3[2] - horse_3[3];
							if( checknum == 200){
								horse_3[2] = 300+checkpoint;
								horse_3[0] = horse_3[2];
								horse_3[1] = horse_3[2];
								horse_3[3] = horse_3[2];
							}
						}
					}
				}else if(horse_3[2] >= 100 ){
					buf = 100;
					if( horse_3[2] == horse_3[0] ){
						if( checkpoint == 0){
							horse_3[2] = 99;
							horse_3[0] = 99;
						}else{
							horse_3[2] = checkpoint+buf;
							horse_3[0] = checkpoint+buf;
							checknum = horse_3[2] - horse_3[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[2] = 300+checkpoint;
									horse_3[0] = horse_3[2];
									horse_3[1] = horse_3[2];
									horse_3[3] = horse_3[2];
								}else{
									horse_3[2] = 200+checkpoint;
									horse_3[0] = horse_3[2];
									horse_3[1] = horse_3[2];
								}
							}
							checknum = checkpoint+buf - horse_3[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[2] = 300+checkpoint;
									horse_3[0] = horse_3[2];
									horse_3[1] = horse_3[2];
									horse_3[3] = horse_3[2];
								}else{
									horse_3[2] = 200+checkpoint;
									horse_3[0] = horse_3[2];
									horse_3[3] = horse_3[2];
								}
							}
						}
					}else if( horse_3[2] == horse_3[1] ){
						if( checkpoint == 0){
							horse_3[2] = 99;
							horse_3[1] = 99;
						}else{
							horse_3[2] = checkpoint+buf;
							horse_3[1] = checkpoint+buf;
							checknum = horse_3[2] - horse_3[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[2] = 300+checkpoint;
									horse_3[1] = horse_3[2];
									horse_3[0] = horse_3[2];
									horse_3[3] = horse_3[2];
								}else{
									horse_3[2] = 200+checkpoint;
									horse_3[1] = horse_3[2];
									horse_3[0] = horse_3[2];
								}
							}
							checknum = checkpoint+buf - horse_3[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[2] = 300+checkpoint;
									horse_3[1] = horse_3[2];
									horse_3[0] = horse_3[2];
									horse_3[3] = horse_3[2];
								}else{
									horse_3[2] = 200+checkpoint;
									horse_3[1] = horse_3[2];
									horse_3[3] = horse_3[2];
								}
							}
						}
					}else if( horse_3[2] == horse_3[3] ){
						if( checkpoint == 0){
							horse_3[2] = 99;
							horse_3[3] = 99;
						}else{
							horse_3[2] = checkpoint+buf;
							horse_3[3] = checkpoint+buf;
							checknum = horse_3[2] - horse_3[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[2] = 300+checkpoint;
									horse_3[3] = horse_3[2];
									horse_3[0] = horse_3[2];
									horse_3[1] = horse_3[2];
								}else{
									horse_3[2] = 200+checkpoint;
									horse_3[3] = horse_3[2];
									horse_3[0] = horse_3[2];
								}
							}
							checknum = checkpoint+buf - horse_3[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[2] = 300+checkpoint;
									horse_3[3] = horse_3[2];
									horse_3[0] = horse_3[2];
									horse_3[1] = horse_3[2];
								}else{
									horse_3[2] = 200+checkpoint;
									horse_3[3] = horse_3[2];
									horse_3[1] = horse_3[2];
								}
							}
						}
					}
				}else{
					if( checkpoint == 0){
						horse_3[2] = 99;
					}else{
						horse_3[2] = checkpoint;
						int comparepoint_1 = horse_3[0];
						int comparepoint_2 = horse_3[1];
						int comparepoint_3 = horse_3[3];
						checknum = checkpoint - comparepoint_1;
						if( checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_3[2] <= 300 ){
								
								horse_3[2] = 300+checkpoint;
								horse_3[0] = horse_3[2];
								horse_3[1] = horse_3[2];
								horse_3[3] = horse_3[2];
							}else if( checknum == -100 && horse_3[2] <= 200  ){
							
								horse_3[2] = 200+checkpoint;
								if( horse_3[0] != horse_3[1] ){
									horse_3[0] = horse_3[2];
									horse_3[3] = horse_3[2];
								}else{
									horse_3[0] = horse_3[2];
									horse_3[1] = horse_3[2];
								}
							}else if( horse_3[2] <= 100 ){
						
								horse_3[2] = 100+checkpoint;
								horse_3[0] = horse_3[2];
							}
						}
						checknum = checkpoint - comparepoint_2;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_3[2] <= 300 ){
								horse_3[2] = 300+checkpoint;
								horse_3[0] = horse_3[2];
								horse_3[1] = horse_3[2];
								horse_3[3] = horse_3[2];
							}else if( checknum == -100 && horse_3[2] <= 200  ){
								horse_3[2] = 200+checkpoint;
								if( horse_3[1] != horse_3[0] ){
									horse_3[1] = horse_3[2];
									horse_3[3] = horse_3[2];
								}else{
									horse_3[1] = horse_3[2];
									horse_3[0] = horse_3[2];
								}
							}else if( horse_3[2] <= 100 ){
								
								horse_3[2] = 100+checkpoint;
								horse_3[1] = horse_3[2];
							}
						}
						checknum = checkpoint -comparepoint_3;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_3[2] <= 300 ){
								horse_3[2] = 300+checkpoint;
								horse_3[0] = horse_3[2];
								horse_3[1] = horse_3[2];
								horse_3[3] = horse_3[2];
							}else if( checknum == -100 && horse_3[2] <= 200  ){
								horse_3[2] = 200+checkpoint;
								if( horse_3[3] != horse_3[0] ){
									horse_3[3] = horse_3[2];
									horse_3[1] = horse_3[2];
								}else{
									horse_3[3] = horse_3[2];
									horse_3[0] = horse_3[2];
								}
							}else if( horse_3[2] <= 100 ){
								horse_3[2] = 100+checkpoint;
								horse_3[3] = horse_3[2];
							}
						}
					}
				}
				overlapCharacter(horse_3[2], member);
			}else{
				for(int i=0, goal = horse_3[2]; i<4; i++)
					if(horse_3[i] == goal) horse_3[i] = 99;
				goflag_3[2] = true;
			}
		}
		if( member == 3 ){
			int checkpoint = Checkpoint(horse_4[2], run);
			if( checkpoint != 99 ){
				if(horse_4[2] >= 300){
					buf = 300;
					if( checkpoint == 0 ){
						horse_4[0] = 99;
						horse_4[1] = 99;
						horse_4[2] = 99;
						horse_4[3] = 99;
					}else{
						horse_4[0] = checkpoint + buf;
						horse_4[1] = checkpoint + buf;
						horse_4[2] = checkpoint + buf;
						horse_4[3] = checkpoint + buf;
					}
				}else if( horse_4[2] >= 200 ){
					buf = 200;
					if( horse_4[2] != horse_4[0]){
						if( checkpoint == 0 ){
							horse_4[1] = 99;
							horse_4[2] = 99;
							horse_4[3] = 99;
						}else{
							horse_4[1] = checkpoint+buf;
							horse_4[2] = checkpoint+buf;
							horse_4[3] = checkpoint+buf;
							checknum = horse_4[2] - horse_4[0];
							if( checknum == 200){
								horse_4[2] = 300+checkpoint;
								horse_4[0] = horse_4[2];
								horse_4[1] = horse_4[2];
								horse_4[3] = horse_4[2];
							}
						}
					}else if( horse_4[2] != horse_4[1] ){
						if( checkpoint == 0 ){
							horse_4[0] = 99;
							horse_4[2] = 99;
							horse_4[3] = 99;
						}else{
							horse_4[0] = checkpoint+buf;
							horse_4[2] = checkpoint+buf;
							horse_4[3] = checkpoint+buf;
							checknum = horse_4[2] - horse_4[1];
							if( checknum == 200){
								horse_4[2] = 300+checkpoint;
								horse_4[0] = horse_4[2];
								horse_4[1] = horse_4[2];
								horse_4[3] = horse_4[2];
							}
						}
					}else{
						if( checkpoint == 0 ){
							horse_4[0] = 99;
							horse_4[1] = 99;
							horse_4[2] = 99;
						}else{
							horse_4[0] = checkpoint+buf;
							horse_4[1] = checkpoint+buf;
							horse_4[2] = checkpoint+buf;
							checknum = horse_4[2] - horse_4[3];
							if( checknum == 200){
								horse_4[2] = 300+checkpoint;
								horse_4[0] = horse_4[2];
								horse_4[1] = horse_4[2];
								horse_4[3] = horse_4[2];
							}
						}
					}
				}else if(horse_4[2] >= 100 ){
					buf = 100;
					if( horse_4[2] == horse_4[0] ){
						if( checkpoint == 0){
							horse_4[2] = 99;
							horse_4[0] = 99;
						}else{
							horse_4[2] = checkpoint+buf;
							horse_4[0] = checkpoint+buf;
							checknum = horse_4[2] - horse_4[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[2] = 300+checkpoint;
									horse_4[0] = horse_4[2];
									horse_4[1] = horse_4[2];
									horse_4[3] = horse_4[2];
								}else{
									horse_4[2] = 200+checkpoint;
									horse_4[0] = horse_4[2];
									horse_4[1] = horse_4[2];
								}
							}
							checknum = checkpoint+buf - horse_4[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[2] = 300+checkpoint;
									horse_4[0] = horse_4[2];
									horse_4[1] = horse_4[2];
									horse_4[3] = horse_4[2];
								}else{
									horse_4[2] = 200+checkpoint;
									horse_4[0] = horse_4[2];
									horse_4[3] = horse_4[2];
								}
							}
						}
					}else if( horse_4[2] == horse_4[1] ){
						if( checkpoint == 0){
							horse_4[2] = 99;
							horse_4[1] = 99;
						}else{
							horse_4[2] = checkpoint+buf;
							horse_4[1] = checkpoint+buf;
							checknum = horse_4[2] - horse_4[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[2] = 300+checkpoint;
									horse_4[1] = horse_4[2];
									horse_4[0] = horse_4[2];
									horse_4[3] = horse_4[2];
								}else{
									horse_4[2] = 200+checkpoint;
									horse_4[1] = horse_4[2];
									horse_4[0] = horse_4[2];
								}
							}
							checknum = checkpoint+buf - horse_4[3];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[2] = 300+checkpoint;
									horse_4[1] = horse_4[2];
									horse_4[0] = horse_4[2];
									horse_4[3] = horse_4[2];
								}else{
									horse_4[2] = 200+checkpoint;
									horse_4[1] = horse_4[2];
									horse_4[3] = horse_4[2];
								}
							}
						}
					}else if( horse_4[2] == horse_4[3] ){
						if( checkpoint == 0){
							horse_4[2] = 99;
							horse_4[3] = 99;
						}else{
							horse_4[2] = checkpoint+buf;
							horse_4[3] = checkpoint+buf;
							checknum = horse_4[2] - horse_4[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[2] = 300+checkpoint;
									horse_4[3] = horse_4[2];
									horse_4[0] = horse_4[2];
									horse_4[1] = horse_4[2];
								}else{
									horse_4[2] = 200+checkpoint;
									horse_4[3] = horse_4[2];
									horse_4[0] = horse_4[2];
								}
							}
							checknum = checkpoint+buf - horse_4[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[2] = 300+checkpoint;
									horse_4[3] = horse_4[2];
									horse_4[0] = horse_4[2];
									horse_4[1] = horse_4[2];
								}else{
									horse_4[2] = 200+checkpoint;
									horse_4[3] = horse_4[2];
									horse_4[1] = horse_4[2];
								}
							}
						}
					}
				}else{
					if( checkpoint == 0){
						horse_4[2] = 99;
					}else{
						horse_4[2] = checkpoint;
						int comparepoint_1 = horse_4[0];
						int comparepoint_2 = horse_4[1];
						int comparepoint_3 = horse_4[3];
						checknum = checkpoint - comparepoint_1;
						if( checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_4[2] <= 300 ){
								
								horse_4[2] = 300+checkpoint;
								horse_4[0] = horse_4[2];
								horse_4[1] = horse_4[2];
								horse_4[3] = horse_4[2];
							}else if( checknum == -100 && horse_4[2] <= 200  ){
							
								horse_4[2] = 200+checkpoint;
								if( horse_4[0] != horse_4[1] ){
									horse_4[0] = horse_4[2];
									horse_4[3] = horse_4[2];
								}else{
									horse_4[0] = horse_4[2];
									horse_4[1] = horse_4[2];
								}
							}else if( horse_4[2] <= 100 ){
						
								horse_4[2] = 100+checkpoint;
								horse_4[0] = horse_4[2];
							}
						}
						checknum = checkpoint - comparepoint_2;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_4[2] <= 300 ){
								horse_4[2] = 300+checkpoint;
								horse_4[0] = horse_4[2];
								horse_4[1] = horse_4[2];
								horse_4[3] = horse_4[2];
							}else if( checknum == -100 && horse_4[2] <= 200  ){
								horse_4[2] = 200+checkpoint;
								if( horse_4[1] != horse_4[0] ){
									horse_4[1] = horse_4[2];
									horse_4[3] = horse_4[2];
								}else{
									horse_4[1] = horse_4[2];
									horse_4[0] = horse_4[2];
								}
							}else if( horse_4[2] <= 100 ){
								
								horse_4[2] = 100+checkpoint;
								horse_4[1] = horse_4[2];
							}
						}
						checknum = checkpoint -comparepoint_3;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_4[2] <= 300 ){
								horse_4[2] = 300+checkpoint;
								horse_4[0] = horse_4[2];
								horse_4[1] = horse_4[2];
								horse_4[3] = horse_4[2];
							}else if( checknum == -100 && horse_4[2] <= 200  ){
								horse_4[2] = 200+checkpoint;
								if( horse_4[3] != horse_4[0] ){
									horse_4[3] = horse_4[2];
									horse_4[1] = horse_4[2];
								}else{
									horse_4[3] = horse_4[2];
									horse_4[0] = horse_4[2];
								}
							}else if( horse_4[2] <= 100 ){
								horse_4[2] = 100+checkpoint;
								horse_4[3] = horse_4[2];
							}
						}
					}
				}
				overlapCharacter(horse_4[2], member);
			}else{
				for(int i=0, goal = horse_4[2]; i<4; i++)
					if(horse_4[i] == goal) horse_4[i] = 99;
				goflag_4[2] = true;
			}
		}
		run = 0;
		turn = (turn+1)%4;
		cs.HorseAndTurnUpdate();
	}
	public void setGoButton4(){
		int buf = 0;
		int checknum = 0;
		if( member == 0 ){
			int checkpoint = Checkpoint(horse_1[3], run);
			if( checkpoint != 99 ){
				if(horse_1[3] >= 300){
					buf = 300;
					if( checkpoint == 0 ){
						horse_1[0] = 99;
						horse_1[1] = 99;
						horse_1[2] = 99;
						horse_1[3] = 99;
					}else{
						horse_1[0] = checkpoint + buf;
						horse_1[1] = checkpoint + buf;
						horse_1[2] = checkpoint + buf;
						horse_1[3] = checkpoint + buf;
					}
				}else if( horse_1[3] >= 200 ){
					buf = 200;
					if( horse_1[3] != horse_1[0]){
						if( checkpoint == 0 ){
							horse_1[1] = 99;
							horse_1[2] = 99;
							horse_1[3] = 99;
						}else{
							horse_1[1] = checkpoint+buf;
							horse_1[2] = checkpoint+buf;
							horse_1[3] = checkpoint+buf;
							checknum = horse_1[3] - horse_1[0];
							if( checknum == 200){
								horse_1[3] = 300+checkpoint;
								horse_1[0] = horse_1[3];
								horse_1[1] = horse_1[3];
								horse_1[2] = horse_1[3];
							}
						}
					}else if( horse_1[3] != horse_1[1] ){
						if( checkpoint == 0 ){
							horse_1[0] = 99;
							horse_1[2] = 99;
							horse_1[3] = 99;
						}else{
							horse_1[0] = checkpoint+buf;
							horse_1[2] = checkpoint+buf;
							horse_1[3] = checkpoint+buf;
							checknum = horse_1[3] - horse_1[1];
							if( checknum == 200){
								horse_1[3] = 300+checkpoint;
								horse_1[0] = horse_1[3];
								horse_1[1] = horse_1[3];
								horse_1[2] = horse_1[3];
							}
						}
					}else{
						if( checkpoint == 0 ){
							horse_1[0] = 99;
							horse_1[1] = 99;
							horse_1[3] = 99;
						}else{
							horse_1[0] = checkpoint+buf;
							horse_1[1] = checkpoint+buf;
							horse_1[3] = checkpoint+buf;
							checknum = horse_1[3] - horse_1[2];
							if( checknum == 200){
								horse_1[3] = 300+checkpoint;
								horse_1[0] = horse_1[3];
								horse_1[1] = horse_1[3];
								horse_1[2] = horse_1[3];
							}
						}
					}
				}else if(horse_1[3] >= 100 ){
					buf = 100;
					if( horse_1[3] == horse_1[0] ){
						if( checkpoint == 0){
							horse_1[3] = 99;
							horse_1[0] = 99;
						}else{
							horse_1[3] = checkpoint+buf;
							horse_1[0] = checkpoint+buf;
							checknum = horse_1[3] - horse_1[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[3] = 300+checkpoint;
									horse_1[0] = horse_1[3];
									horse_1[1] = horse_1[3];
									horse_1[2] = horse_1[3];
								}else{
									horse_1[3] = 200+checkpoint;
									horse_1[0] = horse_1[3];
									horse_1[1] = horse_1[3];
								}
							}
							checknum = checkpoint+buf - horse_1[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[3] = 300+checkpoint;
									horse_1[0] = horse_1[3];
									horse_1[1] = horse_1[3];
									horse_1[2] = horse_1[3];
								}else{
									horse_1[3] = 200+checkpoint;
									horse_1[0] = horse_1[3];
									horse_1[2] = horse_1[3];
								}
							}
						}
					}else if( horse_1[3] == horse_1[1] ){
						if( checkpoint == 0){
							horse_1[3] = 99;
							horse_1[1] = 99;
						}else{
							horse_1[3] = checkpoint+buf;
							horse_1[1] = checkpoint+buf;
							checknum = horse_1[3] - horse_1[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[3] = 300+checkpoint;
									horse_1[1] = horse_1[3];
									horse_1[0] = horse_1[3];
									horse_1[2] = horse_1[3];
								}else{
									horse_1[3] = 200+checkpoint;
									horse_1[1] = horse_1[3];
									horse_1[0] = horse_1[3];
								}
							}
							checknum = checkpoint+buf - horse_1[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[3] = 300+checkpoint;
									horse_1[1] = horse_1[3];
									horse_1[0] = horse_1[3];
									horse_1[2] = horse_1[3];
								}else{
									horse_1[3] = 200+checkpoint;
									horse_1[1] = horse_1[3];
									horse_1[2] = horse_1[3];
								}
							}
						}
					}else if( horse_1[3] == horse_1[2] ){
						if( checkpoint == 0){
							horse_1[3] = 99;
							horse_1[2] = 99;
						}else{
							horse_1[3] = checkpoint+buf;
							horse_1[2] = checkpoint+buf;
							checknum = horse_1[3] - horse_1[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[3] = 300+checkpoint;
									horse_1[2] = horse_1[3];
									horse_1[0] = horse_1[3];
									horse_1[1] = horse_1[3];
								}else{
									horse_1[3] = 200+checkpoint;
									horse_1[2] = horse_1[3];
									horse_1[0] = horse_1[3];
								}
							}
							checknum = checkpoint+buf - horse_1[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_1[3] = 300+checkpoint;
									horse_1[2] = horse_1[3];
									horse_1[0] = horse_1[3];
									horse_1[2] = horse_1[3];
								}else{
									horse_1[3] = 200+checkpoint;
									horse_1[2] = horse_1[3];
									horse_1[1] = horse_1[3];
								}
							}
						}
					}
				}else{
					if( checkpoint == 0 ){
						horse_1[3] = 99;
					}else{
						horse_1[3] = checkpoint;
						int comparepoint_1 = horse_1[0];
						int comparepoint_2 = horse_1[1];
						int comparepoint_3 = horse_1[2];
						checknum = checkpoint - comparepoint_1;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_1[3] <= 300 ){
								horse_1[3] = 300+checkpoint;
								horse_1[0] = horse_1[3];
								horse_1[1] = horse_1[3];
								horse_1[2] = horse_1[3];
							}else if( checknum == -100 && horse_1[3] <= 200 ){
								horse_1[3] = 200+checkpoint;
								if( horse_1[0] != horse_1[1] ){
									horse_1[0] = horse_1[3];
									horse_1[2] = horse_1[3];
								}else{
									horse_1[0] = horse_1[3];
									horse_1[1] = horse_1[3];
								}
							}else if( horse_1[3] <= 100 ){
								horse_1[3] = 100+checkpoint;
								horse_1[0] = horse_1[3];
							}
						}
						checknum = checkpoint - comparepoint_2;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_1[3] <= 300 ){
								horse_1[3] = 300+checkpoint;
								horse_1[0] = horse_1[3];
								horse_1[1] = horse_1[3];
								horse_1[2] = horse_1[3];
							}else if( checknum == -100 && horse_1[3] <= 200 ){
								horse_1[3] = 200+checkpoint;
								if( horse_1[1] != horse_1[0] ){
									horse_1[1] = horse_1[3];
									horse_1[2] = horse_1[3];
								}else{
									horse_1[1] = horse_1[3];
									horse_1[0] = horse_1[3];
								}
							}else if( horse_1[3] <= 100 ){
								horse_1[3] = 100+checkpoint;
								horse_1[1] = horse_1[3];
							}
						}
						checknum = checkpoint - comparepoint_3;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_1[3] <= 300 ){
								horse_1[3] = 300+checkpoint;
								horse_1[0] = horse_1[3];
								horse_1[1] = horse_1[3];
								horse_1[2] = horse_1[3];
							}else if( checknum == -100 && horse_1[3] <= 200 ){
								horse_1[3] = 200+checkpoint;
								if( horse_1[2] != horse_1[0] ){
									horse_1[2] = horse_1[3];
									horse_1[1] = horse_1[3];
								}else{
									horse_1[2] = horse_1[3];
									horse_1[0] = horse_1[3];
								}
							}else if( horse_1[3] <= 100 ){
								horse_1[3] = 100+checkpoint;
								horse_1[2] = horse_1[3];
							}
						}
					}
				}
				overlapCharacter(horse_1[3], member);
			}else{
				for(int i=0, goal = horse_1[3]; i<4; i++)
					if(horse_1[i] == goal) horse_1[i] = 99;
				goflag_1[3] = true;
			}
		}
		if( member == 1 ){
			int checkpoint = Checkpoint(horse_2[3], run);
			if( checkpoint != 99 ){
				if(horse_2[3] >= 300){
					buf = 300;
					if( checkpoint == 0 ){
						horse_2[0] = 99;
						horse_2[1] = 99;
						horse_2[2] = 99;
						horse_2[3] = 99;
					}else{
						horse_2[0] = checkpoint + buf;
						horse_2[1] = checkpoint + buf;
						horse_2[2] = checkpoint + buf;
						horse_2[3] = checkpoint + buf;
					}
				}else if( horse_2[3] >= 200 ){
					buf = 200;
					if( horse_2[3] != horse_2[0]){
						if( checkpoint == 0 ){
							horse_2[1] = 99;
							horse_2[2] = 99;
							horse_2[3] = 99;
						}else{
							horse_2[1] = checkpoint+buf;
							horse_2[2] = checkpoint+buf;
							horse_2[3] = checkpoint+buf;
							checknum = horse_2[3] - horse_2[0];
							if( checknum == 200){
								horse_2[3] = 300+checkpoint;
								horse_2[0] = horse_2[3];
								horse_2[1] = horse_2[3];
								horse_2[2] = horse_2[3];
							}
						}
					}else if( horse_2[3] != horse_2[1] ){
						if( checkpoint == 0 ){
							horse_2[0] = 99;
							horse_2[2] = 99;
							horse_2[3] = 99;
						}else{
							horse_2[0] = checkpoint+buf;
							horse_2[2] = checkpoint+buf;
							horse_2[3] = checkpoint+buf;
							checknum = horse_2[3] - horse_2[1];
							if( checknum == 200){
								horse_2[3] = 300+checkpoint;
								horse_2[0] = horse_2[3];
								horse_2[1] = horse_2[3];
								horse_2[2] = horse_2[3];
							}
						}
					}else{
						if( checkpoint == 0 ){
							horse_2[0] = 99;
							horse_2[1] = 99;
							horse_2[3] = 99;
						}else{
							horse_2[0] = checkpoint+buf;
							horse_2[1] = checkpoint+buf;
							horse_2[3] = checkpoint+buf;
							checknum = horse_2[3] - horse_2[2];
							if( checknum == 200){
								horse_2[3] = 300+checkpoint;
								horse_2[0] = horse_2[3];
								horse_2[1] = horse_2[3];
								horse_2[2] = horse_2[3];
							}
						}
					}
				}else if(horse_2[3] >= 100 ){
					buf = 100;
					if( horse_2[3] == horse_2[0] ){
						if( checkpoint == 0){
							horse_2[3] = 99;
							horse_2[0] = 99;
						}else{
							horse_2[3] = checkpoint+buf;
							horse_2[0] = checkpoint+buf;
							checknum = horse_2[3] - horse_2[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[3] = 300+checkpoint;
									horse_2[0] = horse_2[3];
									horse_2[1] = horse_2[3];
									horse_2[2] = horse_2[3];
								}else{
									horse_2[3] = 200+checkpoint;
									horse_2[0] = horse_2[3];
									horse_2[1] = horse_2[3];
								}
							}
							checknum = checkpoint+buf - horse_2[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[3] = 300+checkpoint;
									horse_2[0] = horse_2[3];
									horse_2[1] = horse_2[3];
									horse_2[2] = horse_2[3];
								}else{
									horse_2[3] = 200+checkpoint;
									horse_2[0] = horse_2[3];
									horse_2[2] = horse_2[3];
								}
							}
						}
					}else if( horse_2[3] == horse_2[1] ){
						if( checkpoint == 0){
							horse_2[3] = 99;
							horse_2[1] = 99;
						}else{
							horse_2[3] = checkpoint+buf;
							horse_2[1] = checkpoint+buf;
							checknum = horse_2[3] - horse_2[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[3] = 300+checkpoint;
									horse_2[1] = horse_2[3];
									horse_2[0] = horse_2[3];
									horse_2[2] = horse_2[3];
								}else{
									horse_2[3] = 200+checkpoint;
									horse_2[1] = horse_2[3];
									horse_2[0] = horse_2[3];
								}
							}
							checknum = checkpoint+buf - horse_2[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[3] = 300+checkpoint;
									horse_2[1] = horse_2[3];
									horse_2[0] = horse_2[3];
									horse_2[2] = horse_2[3];
								}else{
									horse_2[3] = 200+checkpoint;
									horse_2[1] = horse_2[3];
									horse_2[2] = horse_2[3];
								}
							}
						}
					}else if( horse_2[3] == horse_2[2] ){
						if( checkpoint == 0){
							horse_2[3] = 99;
							horse_2[2] = 99;
						}else{
							horse_2[3] = checkpoint+buf;
							horse_2[2] = checkpoint+buf;
							checknum = horse_2[3] - horse_2[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[3] = 300+checkpoint;
									horse_2[2] = horse_2[3];
									horse_2[0] = horse_2[3];
									horse_2[1] = horse_2[3];
								}else{
									horse_2[3] = 200+checkpoint;
									horse_2[2] = horse_2[3];
									horse_2[0] = horse_2[3];
								}
							}
							checknum = checkpoint+buf - horse_2[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_2[3] = 300+checkpoint;
									horse_2[2] = horse_2[3];
									horse_2[0] = horse_2[3];
									horse_2[2] = horse_2[3];
								}else{
									horse_2[3] = 200+checkpoint;
									horse_2[2] = horse_2[3];
									horse_2[1] = horse_2[3];
								}
							}
						}
					}
				}else{
					if( checkpoint == 0 ){
						horse_2[3] = 99;
					}else{
						horse_2[3] = checkpoint;
						int comparepoint_1 = horse_2[0];
						int comparepoint_2 = horse_2[1];
						int comparepoint_3 = horse_2[2];
						checknum = checkpoint - comparepoint_1;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_2[3] <= 300 ){
								horse_2[3] = 300+checkpoint;
								horse_2[0] = horse_2[3];
								horse_2[1] = horse_2[3];
								horse_2[2] = horse_2[3];
							}else if( checknum == -100 && horse_2[3] <= 200 ){
								horse_2[3] = 200+checkpoint;
								if( horse_2[0] != horse_2[1] ){
									horse_2[0] = horse_2[3];
									horse_2[2] = horse_2[3];
								}else{
									horse_2[0] = horse_2[3];
									horse_2[1] = horse_2[3];
								}
							}else if( horse_2[3] <= 100 ){
								horse_2[3] = 100+checkpoint;
								horse_2[0] = horse_2[3];
							}
						}
						checknum = checkpoint - comparepoint_2;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_2[3] <= 300 ){
								horse_2[3] = 300+checkpoint;
								horse_2[0] = horse_2[3];
								horse_2[1] = horse_2[3];
								horse_2[2] = horse_2[3];
							}else if( checknum == -100 && horse_2[3] <= 200 ){
								horse_2[3] = 200+checkpoint;
								if( horse_2[1] != horse_2[0] ){
									horse_2[1] = horse_2[3];
									horse_2[2] = horse_2[3];
								}else{
									horse_2[1] = horse_2[3];
									horse_2[0] = horse_2[3];
								}
							}else if( horse_2[3] <= 100 ){
								horse_2[3] = 100+checkpoint;
								horse_2[1] = horse_2[3];
							}
						}
						checknum = checkpoint - comparepoint_3;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_2[3] <= 300 ){
								horse_2[3] = 300+checkpoint;
								horse_2[0] = horse_2[3];
								horse_2[1] = horse_2[3];
								horse_2[2] = horse_2[3];
							}else if( checknum == -100 && horse_2[3] <= 200 ){
								horse_2[3] = 200+checkpoint;
								if( horse_2[2] != horse_2[0] ){
									horse_2[2] = horse_2[3];
									horse_2[1] = horse_2[3];
								}else{
									horse_2[2] = horse_2[3];
									horse_2[0] = horse_2[3];
								}
							}else if( horse_2[3] <= 100 ){
								horse_2[3] = 100+checkpoint;
								horse_2[2] = horse_2[3];
							}
						}
					}
				}
				overlapCharacter(horse_2[3], member);
			}else{
				for(int i=0, goal = horse_2[3]; i<4; i++)
					if(horse_2[i] == goal) horse_2[i] = 99;
				goflag_2[3] = true;
			}
		}
		if( member == 3 ){
			int checkpoint = Checkpoint(horse_3[3], run);
			if( checkpoint != 99 ){
				if(horse_3[3] >= 300){
					buf = 300;
					if( checkpoint == 0 ){
						horse_3[0] = 99;
						horse_3[1] = 99;
						horse_3[2] = 99;
						horse_3[3] = 99;
					}else{
						horse_3[0] = checkpoint + buf;
						horse_3[1] = checkpoint + buf;
						horse_3[2] = checkpoint + buf;
						horse_3[3] = checkpoint + buf;
					}
				}else if( horse_3[3] >= 200 ){
					buf = 200;
					if( horse_3[3] != horse_3[0]){
						if( checkpoint == 0 ){
							horse_3[1] = 99;
							horse_3[2] = 99;
							horse_3[3] = 99;
						}else{
							horse_3[1] = checkpoint+buf;
							horse_3[2] = checkpoint+buf;
							horse_3[3] = checkpoint+buf;
							checknum = horse_3[3] - horse_3[0];
							if( checknum == 200){
								horse_3[3] = 300+checkpoint;
								horse_3[0] = horse_3[3];
								horse_3[1] = horse_3[3];
								horse_3[2] = horse_3[3];
							}
						}
					}else if( horse_3[3] != horse_3[1] ){
						if( checkpoint == 0 ){
							horse_3[0] = 99;
							horse_3[2] = 99;
							horse_3[3] = 99;
						}else{
							horse_3[0] = checkpoint+buf;
							horse_3[2] = checkpoint+buf;
							horse_3[3] = checkpoint+buf;
							checknum = horse_3[3] - horse_3[1];
							if( checknum == 200){
								horse_3[3] = 300+checkpoint;
								horse_3[0] = horse_3[3];
								horse_3[1] = horse_3[3];
								horse_3[2] = horse_3[3];
							}
						}
					}else{
						if( checkpoint == 0 ){
							horse_3[0] = 99;
							horse_3[1] = 99;
							horse_3[3] = 99;
						}else{
							horse_3[0] = checkpoint+buf;
							horse_3[1] = checkpoint+buf;
							horse_3[3] = checkpoint+buf;
							checknum = horse_3[3] - horse_3[2];
							if( checknum == 200){
								horse_3[3] = 300+checkpoint;
								horse_3[0] = horse_3[3];
								horse_3[1] = horse_3[3];
								horse_3[2] = horse_3[3];
							}
						}
					}
				}else if(horse_3[3] >= 100 ){
					buf = 100;
					if( horse_3[3] == horse_3[0] ){
						if( checkpoint == 0){
							horse_3[3] = 99;
							horse_3[0] = 99;
						}else{
							horse_3[3] = checkpoint+buf;
							horse_3[0] = checkpoint+buf;
							checknum = horse_3[3] - horse_3[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[3] = 300+checkpoint;
									horse_3[0] = horse_3[3];
									horse_3[1] = horse_3[3];
									horse_3[2] = horse_3[3];
								}else{
									horse_3[3] = 200+checkpoint;
									horse_3[0] = horse_3[3];
									horse_3[1] = horse_3[3];
								}
							}
							checknum = checkpoint+buf - horse_3[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[3] = 300+checkpoint;
									horse_3[0] = horse_3[3];
									horse_3[1] = horse_3[3];
									horse_3[2] = horse_3[3];
								}else{
									horse_3[3] = 200+checkpoint;
									horse_3[0] = horse_3[3];
									horse_3[2] = horse_3[3];
								}
							}
						}
					}else if( horse_3[3] == horse_3[1] ){
						if( checkpoint == 0){
							horse_3[3] = 99;
							horse_3[1] = 99;
						}else{
							horse_3[3] = checkpoint+buf;
							horse_3[1] = checkpoint+buf;
							checknum = horse_3[3] - horse_3[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[3] = 300+checkpoint;
									horse_3[1] = horse_3[3];
									horse_3[0] = horse_3[3];
									horse_3[2] = horse_3[3];
								}else{
									horse_3[3] = 200+checkpoint;
									horse_3[1] = horse_3[3];
									horse_3[0] = horse_3[3];
								}
							}
							checknum = checkpoint+buf - horse_3[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[3] = 300+checkpoint;
									horse_3[1] = horse_3[3];
									horse_3[0] = horse_3[3];
									horse_3[2] = horse_3[3];
								}else{
									horse_3[3] = 200+checkpoint;
									horse_3[1] = horse_3[3];
									horse_3[2] = horse_3[3];
								}
							}
						}
					}else if( horse_3[3] == horse_3[2] ){
						if( checkpoint == 0){
							horse_3[3] = 99;
							horse_3[2] = 99;
						}else{
							horse_3[3] = checkpoint+buf;
							horse_3[2] = checkpoint+buf;
							checknum = horse_3[3] - horse_3[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[3] = 300+checkpoint;
									horse_3[2] = horse_3[3];
									horse_3[0] = horse_3[3];
									horse_3[1] = horse_3[3];
								}else{
									horse_3[3] = 200+checkpoint;
									horse_3[2] = horse_3[3];
									horse_3[0] = horse_3[3];
								}
							}
							checknum = checkpoint+buf - horse_3[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_3[3] = 300+checkpoint;
									horse_3[2] = horse_3[3];
									horse_3[0] = horse_3[3];
									horse_3[2] = horse_3[3];
								}else{
									horse_3[3] = 200+checkpoint;
									horse_3[2] = horse_3[3];
									horse_3[1] = horse_3[3];
								}
							}
						}
					}
				}else{
					if( checkpoint == 0 ){
						horse_3[3] = 99;
					}else{
						horse_3[3] = checkpoint;
						int comparepoint_1 = horse_3[0];
						int comparepoint_2 = horse_3[1];
						int comparepoint_3 = horse_3[2];
						checknum = checkpoint - comparepoint_1;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_3[3] <= 300 ){
								horse_3[3] = 300+checkpoint;
								horse_3[0] = horse_3[3];
								horse_3[1] = horse_3[3];
								horse_3[2] = horse_3[3];
							}else if( checknum == -100 && horse_3[3] <= 200 ){
								horse_3[3] = 200+checkpoint;
								if( horse_3[0] != horse_3[1] ){
									horse_3[0] = horse_3[3];
									horse_3[2] = horse_3[3];
								}else{
									horse_3[0] = horse_3[3];
									horse_3[1] = horse_3[3];
								}
							}else if( horse_3[3] <= 100 ){
								horse_3[3] = 100+checkpoint;
								horse_3[0] = horse_3[3];
							}
						}
						checknum = checkpoint - comparepoint_2;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_3[3] <= 300 ){
								horse_3[3] = 300+checkpoint;
								horse_3[0] = horse_3[3];
								horse_3[1] = horse_3[3];
								horse_3[2] = horse_3[3];
							}else if( checknum == -100 && horse_3[3] <= 200 ){
								horse_3[3] = 200+checkpoint;
								if( horse_3[1] != horse_3[0] ){
									horse_3[1] = horse_3[3];
									horse_3[2] = horse_3[3];
								}else{
									horse_3[1] = horse_3[3];
									horse_3[0] = horse_3[3];
								}
							}else if( horse_3[3] <= 100 ){
								horse_3[3] = 100+checkpoint;
								horse_3[1] = horse_3[3];
							}
						}
						checknum = checkpoint - comparepoint_3;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_3[3] <= 300 ){
								horse_3[3] = 300+checkpoint;
								horse_3[0] = horse_3[3];
								horse_3[1] = horse_3[3];
								horse_3[2] = horse_3[3];
							}else if( checknum == -100 && horse_3[3] <= 200 ){
								horse_3[3] = 200+checkpoint;
								if( horse_3[2] != horse_3[0] ){
									horse_3[2] = horse_3[3];
									horse_3[1] = horse_3[3];
								}else{
									horse_3[2] = horse_3[3];
									horse_3[0] = horse_3[3];
								}
							}else if( horse_3[3] <= 100 ){
								horse_3[3] = 100+checkpoint;
								horse_3[2] = horse_3[3];
							}
						}
					}
				}
				overlapCharacter(horse_3[3], member);
			}else{
				for(int i=0, goal = horse_3[3]; i<4; i++)
					if(horse_3[i] == goal) horse_3[i] = 99;
				goflag_3[3] = true;
			}
		}
		if( member == 3){
			int checkpoint = Checkpoint(horse_4[3], run);
			if( checkpoint != 99 ){
				if(horse_4[3] >= 300){
					buf = 300;
					if( checkpoint == 0 ){
						horse_4[0] = 99;
						horse_4[1] = 99;
						horse_4[2] = 99;
						horse_4[3] = 99;
					}else{
						horse_4[0] = checkpoint + buf;
						horse_4[1] = checkpoint + buf;
						horse_4[2] = checkpoint + buf;
						horse_4[3] = checkpoint + buf;
					}
				}else if( horse_4[3] >= 200 ){
					buf = 200;
					if( horse_4[3] != horse_4[0]){
						if( checkpoint == 0 ){
							horse_4[1] = 99;
							horse_4[2] = 99;
							horse_4[3] = 99;
						}else{
							horse_4[1] = checkpoint+buf;
							horse_4[2] = checkpoint+buf;
							horse_4[3] = checkpoint+buf;
							checknum = horse_4[3] - horse_4[0];
							if( checknum == 200){
								horse_4[3] = 300+checkpoint;
								horse_4[0] = horse_4[3];
								horse_4[1] = horse_4[3];
								horse_4[2] = horse_4[3];
							}
						}
					}else if( horse_4[3] != horse_4[1] ){
						if( checkpoint == 0 ){
							horse_4[0] = 99;
							horse_4[2] = 99;
							horse_4[3] = 99;
						}else{
							horse_4[0] = checkpoint+buf;
							horse_4[2] = checkpoint+buf;
							horse_4[3] = checkpoint+buf;
							checknum = horse_4[3] - horse_4[1];
							if( checknum == 200){
								horse_4[3] = 300+checkpoint;
								horse_4[0] = horse_4[3];
								horse_4[1] = horse_4[3];
								horse_4[2] = horse_4[3];
							}
						}
					}else{
						if( checkpoint == 0 ){
							horse_4[0] = 99;
							horse_4[1] = 99;
							horse_4[3] = 99;
						}else{
							horse_4[0] = checkpoint+buf;
							horse_4[1] = checkpoint+buf;
							horse_4[3] = checkpoint+buf;
							checknum = horse_4[3] - horse_4[2];
							if( checknum == 200){
								horse_4[3] = 300+checkpoint;
								horse_4[0] = horse_4[3];
								horse_4[1] = horse_4[3];
								horse_4[2] = horse_4[3];
							}
						}
					}
				}else if(horse_4[3] >= 100 ){
					buf = 100;
					if( horse_4[3] == horse_4[0] ){
						if( checkpoint == 0){
							horse_4[3] = 99;
							horse_4[0] = 99;
						}else{
							horse_4[3] = checkpoint+buf;
							horse_4[0] = checkpoint+buf;
							checknum = horse_4[3] - horse_4[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[3] = 300+checkpoint;
									horse_4[0] = horse_4[3];
									horse_4[1] = horse_4[3];
									horse_4[2] = horse_4[3];
								}else{
									horse_4[3] = 200+checkpoint;
									horse_4[0] = horse_4[3];
									horse_4[1] = horse_4[3];
								}
							}
							checknum = checkpoint+buf - horse_4[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[3] = 300+checkpoint;
									horse_4[0] = horse_4[3];
									horse_4[1] = horse_4[3];
									horse_4[2] = horse_4[3];
								}else{
									horse_4[3] = 200+checkpoint;
									horse_4[0] = horse_4[3];
									horse_4[2] = horse_4[3];
								}
							}
						}
					}else if( horse_4[3] == horse_4[1] ){
						if( checkpoint == 0){
							horse_4[3] = 99;
							horse_4[1] = 99;
						}else{
							horse_4[3] = checkpoint+buf;
							horse_4[1] = checkpoint+buf;
							checknum = horse_4[3] - horse_4[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[3] = 300+checkpoint;
									horse_4[1] = horse_4[3];
									horse_4[0] = horse_4[3];
									horse_4[2] = horse_4[3];
								}else{
									horse_4[3] = 200+checkpoint;
									horse_4[1] = horse_4[3];
									horse_4[0] = horse_4[3];
								}
							}
							checknum = checkpoint+buf - horse_4[2];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[3] = 300+checkpoint;
									horse_4[1] = horse_4[3];
									horse_4[0] = horse_4[3];
									horse_4[2] = horse_4[3];
								}else{
									horse_4[3] = 200+checkpoint;
									horse_4[1] = horse_4[3];
									horse_4[2] = horse_4[3];
								}
							}
						}
					}else if( horse_4[3] == horse_4[2] ){
						if( checkpoint == 0){
							horse_4[3] = 99;
							horse_4[2] = 99;
						}else{
							horse_4[3] = checkpoint+buf;
							horse_4[2] = checkpoint+buf;
							checknum = horse_4[3] - horse_4[0];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[3] = 300+checkpoint;
									horse_4[2] = horse_4[3];
									horse_4[0] = horse_4[3];
									horse_4[1] = horse_4[3];
								}else{
									horse_4[3] = 200+checkpoint;
									horse_4[2] = horse_4[3];
									horse_4[0] = horse_4[3];
								}
							}
							checknum = checkpoint+buf - horse_4[1];
							if( checknum == 0 || checknum == 100 ){
								if( checknum == 0 ){
									horse_4[3] = 300+checkpoint;
									horse_4[2] = horse_4[3];
									horse_4[0] = horse_4[3];
									horse_4[2] = horse_4[3];
								}else{
									horse_4[3] = 200+checkpoint;
									horse_4[2] = horse_4[3];
									horse_4[1] = horse_4[3];
								}
							}
						}
					}
				}else{
					if( checkpoint == 0 ){
						horse_4[3] = 99;
					}else{
						horse_4[3] = checkpoint;
						int comparepoint_1 = horse_4[0];
						int comparepoint_2 = horse_4[1];
						int comparepoint_3 = horse_4[2];
						checknum = checkpoint - comparepoint_1;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_4[3] <= 300 ){
								horse_4[3] = 300+checkpoint;
								horse_4[0] = horse_4[3];
								horse_4[1] = horse_4[3];
								horse_4[2] = horse_4[3];
							}else if( checknum == -100 && horse_4[3] <= 200 ){
								horse_4[3] = 200+checkpoint;
								if( horse_4[0] != horse_4[1] ){
									horse_4[0] = horse_4[3];
									horse_4[2] = horse_4[3];
								}else{
									horse_4[0] = horse_4[3];
									horse_4[1] = horse_4[3];
								}
							}else if( horse_4[3] <= 100 ){
								horse_4[3] = 100+checkpoint;
								horse_4[0] = horse_4[3];
							}
						}
						checknum = checkpoint - comparepoint_2;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_4[3] <= 300 ){
								horse_4[3] = 300+checkpoint;
								horse_4[0] = horse_4[3];
								horse_4[1] = horse_4[3];
								horse_4[2] = horse_4[3];
							}else if( checknum == -100 && horse_4[3] <= 200 ){
								horse_4[3] = 200+checkpoint;
								if( horse_4[1] != horse_4[0] ){
									horse_4[1] = horse_4[3];
									horse_4[2] = horse_4[3];
								}else{
									horse_4[1] = horse_4[3];
									horse_4[0] = horse_4[3];
								}
							}else if( horse_4[3] <= 100 ){
								horse_4[3] = 100+checkpoint;
								horse_4[1] = horse_4[3];
							}
						}
						checknum = checkpoint - comparepoint_3;
						if(checknum == -200 || checknum == -100 || checknum == 0){
							if( checknum == -200 && horse_4[3] <= 300 ){
								horse_4[3] = 300+checkpoint;
								horse_4[0] = horse_4[3];
								horse_4[1] = horse_4[3];
								horse_4[2] = horse_4[3];
							}else if( checknum == -100 && horse_4[3] <= 200 ){
								horse_4[3] = 200+checkpoint;
								if( horse_4[2] != horse_4[0] ){
									horse_4[2] = horse_4[3];
									horse_4[1] = horse_4[3];
								}else{
									horse_4[2] = horse_4[3];
									horse_4[0] = horse_4[3];
								}
							}else if( horse_4[3] <= 100 ){
								horse_4[3] = 100+checkpoint;
								horse_4[2] = horse_4[3];
							}
						}
					}
				}
				overlapCharacter(horse_4[3], member);
			}else{
				for(int i=0, goal = horse_4[3]; i<4; i++)
					if(horse_4[i] == goal) horse_4[i] = 99;
				goflag_4[3] = true;
			}
		}
		run = 0;
		turn = (turn+1)%4;
		cs.HorseAndTurnUpdate();
	}
	public void overlapCharacter(int checkpoint, int member){
		if( checkpoint >= 300 ) checkpoint -= 300;
		if( checkpoint >= 200 ) checkpoint -= 200;
		if( checkpoint >= 100 ) checkpoint -= 100;
		if( member == 0 ){
			for( int i = 0; i<4; i++){
				if( horse_2[i] == checkpoint 
						|| horse_2[i] == checkpoint+100
						|| horse_2[i] == checkpoint+200
						|| horse_2[i] == checkpoint+300 ){
					horse_2[i] = 0;
				}
				if( horse_3[i] == checkpoint 
						|| horse_3[i] == checkpoint+100
						|| horse_3[i] == checkpoint+200
						|| horse_3[i] == checkpoint+300 ){
					horse_3[i] = 0;
				}
				if( horse_4[i] == checkpoint 
						|| horse_4[i] == checkpoint+100
						|| horse_4[i] == checkpoint+200
						|| horse_4[i] == checkpoint+300 ){
					horse_4[i] = 0;
				}
			}
		}
		if( member == 1 ){
			for( int i = 0; i<4; i++){
				if( horse_1[i] == checkpoint 
						|| horse_1[i] == checkpoint+100
						|| horse_1[i] == checkpoint+200
						|| horse_1[i] == checkpoint+300 ){
					horse_1[i] = 0;
				}
				if( horse_3[i] == checkpoint 
						|| horse_3[i] == checkpoint+100
						|| horse_3[i] == checkpoint+200
						|| horse_3[i] == checkpoint+300 ){
					horse_3[i] = 0;
				}
				if( horse_4[i] == checkpoint 
						|| horse_4[i] == checkpoint+100
						|| horse_4[i] == checkpoint+200
						|| horse_4[i] == checkpoint+300 ){
					horse_4[i] = 0;
				}
			}
		}
		if( member == 2 ){
			for( int i = 0; i<4; i++){
				if( horse_1[i] == checkpoint 
						|| horse_1[i] == checkpoint+100
						|| horse_1[i] == checkpoint+200
						|| horse_1[i] == checkpoint+300 ){
					horse_1[i] = 0;
				}
				if( horse_2[i] == checkpoint 
						|| horse_2[i] == checkpoint+100
						|| horse_2[i] == checkpoint+200
						|| horse_2[i] == checkpoint+300 ){
					horse_2[i] = 0;
				}
				if( horse_4[i] == checkpoint 
						|| horse_4[i] == checkpoint+100
						|| horse_4[i] == checkpoint+200
						|| horse_4[i] == checkpoint+300 ){
					horse_4[i] = 0;
				}
			}
		}
		if( member == 3 ){
			for( int i = 0; i<4; i++){
				if( horse_1[i] == checkpoint 
						|| horse_1[i] == checkpoint+100
						|| horse_1[i] == checkpoint+200
						|| horse_1[i] == checkpoint+300 ){
					horse_1[i] = 0;
				}
				if( horse_2[i] == checkpoint 
						|| horse_2[i] == checkpoint+100
						|| horse_2[i] == checkpoint+200
						|| horse_2[i] == checkpoint+300 ){
					horse_2[i] = 0;
				}
				if( horse_3[i] == checkpoint 
						|| horse_3[i] == checkpoint+100
						|| horse_3[i] == checkpoint+200
						|| horse_3[i] == checkpoint+300 ){
					horse_3[i] = 0;
				}
			}
		}
	}
	// �Ÿ�������
	public int Checkpoint(int checkpoint, int run){
	
		if( checkpoint >= 300 )checkpoint -= 300;
		if( checkpoint >= 200 )	checkpoint -= 200;
		if( checkpoint >= 100 ) checkpoint -=100;
		
		if( checkpoint == 5 ){
			if( run == -1 ) checkpoint += run;
			else checkpoint = 20 + run -1;
		}else if (checkpoint ==  10){
			if( run == 1 || run == 2 ) checkpoint = 25 + run - 1;
			if( run == 3 ) checkpoint = 22;
			if( run ==4 || run == 5 ) checkpoint = 25 + run - 2;
			if( run == -1 ) checkpoint += run;
		}else if( checkpoint == 20 ){
			if( run == -1 ) checkpoint = 5;
			if( run >= 1 && run <= 4 ) checkpoint += run;
			if ( run == 5 ) checkpoint = 15;
		}else if( checkpoint == 21 ){
			if( run >=1 && run <=3 ) checkpoint += run;
			if( run >=4 && run <=5 ) checkpoint = 15+run-4;
		}else if( checkpoint == 22){
			if( run == -1 ) checkpoint = 26;
			if( run == 1 ) checkpoint = 27;
			if( run == 2 ) checkpoint = 28;
			if( run >= 3 && run <= 5 ) checkpoint = 99;
		}else if( checkpoint == 23  ){
			if( run == -1 || run == 1 ) checkpoint += run;
			if( run>=2 && run <=5 ) checkpoint = 15 + run -1;
		}else if ( checkpoint == 24 ){
			if( run == -1 ) checkpoint += run;
			if( run>=1 && run <=5 ) checkpoint = 15 + run -1;
		}else if( checkpoint == 25 ){
			if( run == -1 ) checkpoint = 10;
			if( run == 1 ) checkpoint += run;
			if( run == 2 ) checkpoint = 22;
			if( run ==3 || run == 4 ) checkpoint = 25+run-1;
			if( run == 5 ) checkpoint = 99;
		}else if( checkpoint == 26 ){
			if( run == -1 ) checkpoint += run;
			if( run == 1 ) checkpoint = 22;
			if( run == 2 || run == 3) checkpoint = 25+run-1;
			if( run == 4 || run == 5 ) checkpoint = 99;
		}else if( checkpoint == 27 ){
			if( run == -1 ) checkpoint = 22;
			if( run == 1 ) checkpoint += run;
			if( run >=2 && run <=5 ) checkpoint = 99;
		}else if( checkpoint == 28 ){
			if( run == -1 ) checkpoint +=run;
			if( run >=1 && run <=5 ) checkpoint = 99;
		}else if( checkpoint == 1){
			if( run == -1 ) checkpoint = 99;
			else checkpoint += run;
		}else if(checkpoint == 0){
			if( run == -1) checkpoint = 0;
			else checkpoint += run;
		}else{
			checkpoint +=run;
			if( checkpoint > 19) checkpoint = 99;
		}
		return checkpoint;
	}
	// �غ����λ��¿��� ���� ������ ���
	public void memeberEXIT(){
		if( my_character == 0 && character_select_flag_1 ) character_select_flag_1 = false;
		if( my_character == 1 && character_select_flag_2 ) character_select_flag_2 = false;
		if( my_character == 2 && character_select_flag_3 ) character_select_flag_3 = false;
		if( my_character == 3 && character_select_flag_4 ) character_select_flag_4 = false;
		
	}
	
	// ȭ�鿡 ���� ���ʿ��� swing component���� ġ��� �Լ�.
	public void setBoundsTitleToZero(){
		title_enter_button.setBounds(0,0,0,0);
		title_join_button.setBounds(0,0,0,0);
		login_id.setBounds(0,0,0,0);
		login_password.setBounds(0,0,0,0);
	}
	public void setBoundsTitleToSet(){
		title_enter_button.setBounds(402, 482, 125,50);
		title_join_button.setBounds(555,482,125,50);
		login_id.setBounds(420,355,280,40);
		login_password.setBounds(430, 420, 280, 40);
	}
	public void setBoundsInGame_GetReady(){
		character_select_button_1.setBounds(0,0,0,0);
		character_select_button_2.setBounds(0,0,0,0);
		character_select_button_3.setBounds(0,0,0,0);
		character_select_button_4.setBounds(0,0,0,0);
		character_select_button_startAndready.setBounds(0,0,0,0);
	}
	public void setGoButtonToZero(){
		go_1.setBounds(0,0,0,0);
		go_2.setBounds(0,0,0,0);
		go_3.setBounds(0,0,0,0);
		go_4.setBounds(0,0,0,0);
	}
	// �⺻ getter, setter �Լ���
	// �ʿ��� ��쿡�� ����
	public int getTurn(){
		return this.turn;
	}
	public void setTurn(int turn){
		this.turn = turn;
	}
	public void setHorse_1(int[] horse_1){
		this.horse_1 = horse_1;
	}
	public void setHorse_2(int[] horse_2){
		this.horse_2 = horse_2;
	}
	public void setHorse_3(int[] horse_3){
		this.horse_3 = horse_3;
	}
	public void setHorse_4(int[] horse_4){
		this.horse_4 = horse_4;
	}
	public int[] getHorse_1(){
		return horse_1;
	}
	public int[] getHorse_2(){
		return horse_2;
	}
	public int[] getHorse_3(){
		return horse_3;
	}
	public int[] getHorse_4(){
		return horse_4;
	}
	public int getCharacter(){
		return this.my_character;
	}
	public int getMember(){
		return this.member;
	}
	public void setCharacter1(int character_1){
		this.character_1 = character_1;
	}
	public void setCharacter2(int character_2){
		this.character_2 = character_2;
	}
	public void setCharacter3(int character_3){
		this.character_3 = character_3;
	}
	public void setCharacter4(int character_4){
		this.character_4 = character_4;
	}
	public boolean IsGameStart1(){
		return this.gamestart_1;
	}
	public boolean IsGameStart2(){
		return this.gamestart_2;
	}
	public boolean IsGameStart3(){
		return this.gamestart_3;
	}
	public boolean IsGameStart4(){
		return this.gamestart_4;
	}
	public void setGameStart1(boolean gamestart_1){
		this.gamestart_1 = gamestart_1;
	}
	public void setGameStart2(boolean gamestart_2){
		this.gamestart_2 = gamestart_2;
	}
	public void setGameStart3(boolean gamestart_3){
		this.gamestart_3 = gamestart_3;
	}
	public void setGameStart4(boolean gamestart_4){
		this.gamestart_4 = gamestart_4;
	}
	public boolean IsSelectCharacter1(){
		return character_select_flag_1;
	}
	public boolean IsSelectCharacter2(){
		return character_select_flag_2;
	}
	public boolean IsSelectCharacter3(){
		return character_select_flag_3;
	}
	public boolean IsSelectCharacter4(){
		return character_select_flag_4;
	}
	public void setSelectCharacter1(boolean character_select_flag_1){
		this.character_select_flag_1 = character_select_flag_1;
	}
	public void setSelectCharacter2(boolean character_select_flag_2){
		this.character_select_flag_2 = character_select_flag_2;
	}
	public void setSelectCharacter3(boolean character_select_flag_3){
		this.character_select_flag_3 = character_select_flag_3;
	}
	public void setSelectCharacter4(boolean character_select_flag_4){
		this.character_select_flag_4 = character_select_flag_4;
	}
	public boolean IsGameStart(){
		if( (member==0 && !gamestart_1)
				|| (member==1 && !gamestart_2)
				|| (member==2 && !gamestart_3)
				|| (member==3 && !gamestart_4) )
			return false;
		else return true;
	}
	public void setGameStart(boolean gamestart){
		this.gamestart = gamestart;
		screenState = EScreenState.GAMESTART;
	}
	public void setMember(int member){
		this.member = member;
	}
	public JTextField getLoginId(){
		return this.login_id;
	}
	public JTextField getChatIn(){
		return this.chat_in;
	}
	public JTextArea getChatOut(){
		return this.chat_out;
	}
}
