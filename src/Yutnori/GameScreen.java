package Yutnori;

import java.awt.*;

class GameScreen extends Canvas {

	private static final long serialVersionUID = 2465971207771778320L;
	Board main;
	Image dblbuff; // 더블버퍼링용 백버퍼
	Graphics gc;// 더블버퍼링용 그래픽 컨텍스트

	Image title; // 타이틀 화면

	Image bg; // 게임 배경화면
	Image manager;
	Image manager_sign;
	Image ready;
	Image throw_yut;
	Image exit;

	Image character_select_form;
	Image character_1_remain_token_0;
	Image character_1_remain_token_1;
	Image character_1_remain_token_2;
	Image character_1_remain_token_3;
	Image character_1_remain_token_4;

	Image character_2_remain_token_0;
	Image character_2_remain_token_1;
	Image character_2_remain_token_2;
	Image character_2_remain_token_3;
	Image character_2_remain_token_4;

	Image character_3_remain_token_0;
	Image character_3_remain_token_1;
	Image character_3_remain_token_2;
	Image character_3_remain_token_3;
	Image character_3_remain_token_4;

	Image character_4_remain_token_0;
	Image character_4_remain_token_1;
	Image character_4_remain_token_2;
	Image character_4_remain_token_3;
	Image character_4_remain_token_4;

	Image character_1_token_1;
	Image character_1_token_2;
	Image character_1_token_3;
	Image character_1_token_4;

	Image character_2_token_1;
	Image character_2_token_2;
	Image character_2_token_3;
	Image character_2_token_4;

	Image character_3_token_1;
	Image character_3_token_2;
	Image character_3_token_3;
	Image character_3_token_4;

	Image character_4_token_1;
	Image character_4_token_2;
	Image character_4_token_3;
	Image character_4_token_4;

	Image member_profile_1;
	Image member_profile_2;
	Image member_profile_3;
	Image member_profile_4;

	Image y_do;
	Image y_gae;
	Image y_gul;
	Image y_yut;
	Image y_mo;
	Image y_bdo;
	// 윷판을 대충 그려보자면
	// ↓ □ □ □ □ ↙
	// □ □ □ □
	// □ □ □ □
	// □ ↘ □
	// □ □ □
	// □ □ □
	// → □ □ □ □ ●
	// 이렇게니까 원점으로 부터 거리를 재보자면..
	// 5 : 20+(n-1) in { 1, 2, 3, 4, 5 }
	// 10 : 25+(n-1) in { 1, 2 }, 3 = 22, 25+(n-2) in { 4, 5 }
	// 20 : -1 = 5, inc in { 1, 2, 3, 4 }, 5 = 15
	// 21 : inc in { 1, 2, 3 }, 15+(n-4) in { 4, 5 }
	// 22 : -1 = 26, 1 = 27, 2 = 28, 0 in { 3, 4, 5}
	// 23 : 1 = 24, 15+(n-2) in {2, 3, 4, 5}
	// 24 : 15+(n-1) in {1, 2, 3, 4, 5}
	// 25 : -1 = 10, inc in { 1 }, 2 = 22, 25+(n-1) in { 3, 4 }, 5 = 0
	// 26 : 1 = 22, 25+(n-1) in { 2, 3}, 0 in { 4, 5 }
	// 27 : -1 = 22, inc in { 1 }, 0 in { 2, 3, 4, 5 }
	// 28 : 0 in { 2, 3, 4, 5 }
	// 위에 표기 안된 숫자들은 dec in { -1 }, inc in { 1, 2, 3, 4, 5 } 임.
	// 10   9   8   7   6   5
	// 11   25         20   4
	// 12     26      21    3
	// 13          22        2
	// 14    23         27
	//      24            28 1
	// 15 16 17 18 19 0 (99)
	// 윷확률도 계산해보면
	// 도, 개, 걸, 윷, 모, 빽도 (3/16, 3/8, 1/4, 1/16, 1/16, 1/16)
	// 좌표를 계산해봅니다.
	// 0 (525, 360) 1 (530, 265) 2 (530, 210) 3 (530, 150) 4 (530, 95) 5 (525,
	// 20)
	// 6 (408, 10) 7 (321, 10) 8 (233, 10) 9 (145, 10) 10(30, 10)
	// 11 (29, 95) 12 (29, 150) 13 (29, 210) 14 (29, 265) 15 (30, 360)
	// 16 (145, 355) 17 (233, 355) 18 (321, 355) 19 (408, 355)
	// 20 (438, 78) 21 (383, 133) 22 (283, 183) 23 (175, 253) 24(120, 290)
	// 25 (118, 75) 26 (178, 113) 27 (384, 253) 28 (444, 290)
	// 말이 겹치게 되면 각자리에 각각 100씩 더해서 저장합니다.
	// 윷그리기 끝.
	int[] horseXpos;
	int[] horseYpos;
	Font font;

	GameScreen(Board main) {
		this.main = main;
		font = new Font("Default", Font.PLAIN, 9);
		horseXpos = new int[] { 525, 530, 530, 530, 530, 525, 408, 321, 233,
				145, 30, 29, 29, 29, 29, 30, 145, 233, 321, 408, 438, 383, 283,
				175, 120, 118, 178, 384, 444 };
		horseYpos = new int[] { 360, 265, 210, 150, 95, 20, 10, 10, 10, 10, 10,
				95, 150, 210, 265, 360, 355, 355, 355, 355, 78, 133, 183, 253,
				290, 75, 113, 253, 290 };
	}

	public void paint(Graphics g) {
		if (gc == null) {
			dblbuff = createImage(main.gScreenWidth, main.gScreenHeight);
			// 더블버퍼링용 오프스크린 버퍼생성. 필히 paint 함수 내에서 해줘야한다.
			// 그렇지않으면 null이 반환된다.
			if (dblbuff == null)
				System.out.println("오프스크린 버퍼 생성 실패");
			else
				gc = dblbuff.getGraphics();// 오프스크린 버퍼에 그리기 위한 그래픽 컨텍스트 획득
			return;
		}
		update(g);
	}

	public void update(Graphics g) {
		// 화면 깜박거림을 줄이기 위해, paint에서 화면을 바로 묘화하지 않고
		// update 메소드를 호출하게 한다.
		if (gc == null)
			return;
		dblpaint();// 오프스크린 버퍼에 그리기
		g.drawImage(dblbuff, 0, 0, this);// 오프스크린 버퍼를 메인화면에 그린다.
	}

	public void dblpaint() {
		try {
			switch (main.screenState) {
			case TITLE:
				gc.drawImage(title, 0, 0, this);
				break;
			case INGAME:
				gc.drawImage(bg, 0, 0, this);
				gc.drawImage(exit, 620, 300, this);
				gc.drawImage(throw_yut, 620, 250, this);
				if (!main.IsGameStart())
					Character_Select();
				switch (main.manager) {
				case 0:
					gc.drawImage(manager, 77, 449, this);
					gc.drawImage(manager_sign, 5, 451, this);
					if (main.IsGameStart2())
						gc.drawImage(ready, 215, 449, this);
					if (main.IsGameStart3())
						gc.drawImage(ready, 353, 449, this);
					if (main.IsGameStart4())
						gc.drawImage(ready, 492, 449, this);
					break;
				case 1:
					gc.drawImage(manager, 215, 449, this);
					gc.drawImage(manager_sign, 142, 451, this);
					if (main.IsGameStart1())
						gc.drawImage(ready, 77, 449, this);
					if (main.IsGameStart3())
						gc.drawImage(ready, 353, 449, this);
					if (main.IsGameStart4())
						gc.drawImage(ready, 492, 449, this);
					break;
				case 2:
					gc.drawImage(manager, 353, 449, this);
					gc.drawImage(manager_sign, 281, 451, this);
					if (main.IsGameStart1())
						gc.drawImage(ready, 77, 449, this);
					if (main.IsGameStart2())
						gc.drawImage(ready, 215, 449, this);
					if (main.IsGameStart4())
						gc.drawImage(ready, 492, 449, this);
					break;
				case 3:
					gc.drawImage(manager, 492, 449, this);
					gc.drawImage(manager_sign, 420, 451, this);
					if (main.IsGameStart1())
						gc.drawImage(ready, 77, 449, this);
					if (main.IsGameStart2())
						gc.drawImage(ready, 215, 449, this);
					if (main.IsGameStart3())
						gc.drawImage(ready, 353, 449, this);
					break;
				default:
					break;
				}
				break;
			case GAMESTART:
				gc.drawImage(bg, 0, 0, this);
				gc.drawImage(exit, 620, 300, this);
				gc.drawImage(throw_yut, 620, 250, this);
				Set_Character();
				Set_Horse();
				Set_Yut();
				break;
			default:
				break;
			}
		} catch (Exception e) {
		}
	}

	public void Character_Select() {
		gc.drawImage(character_select_form, 140, 140, this);
	}

	public void Set_Character() {
		gc.drawImage(member_profile_1, 20, 480, this);
		gc.drawImage(member_profile_2, 160, 480, this);
		gc.drawImage(member_profile_3, 300, 480, this);
		gc.drawImage(member_profile_4, 440, 480, this);
		if (main.character_1 == 1){
			int count=0;
			for(int i=0; i<4;i++) if( main.horse_1[i] == 99) count++;
			if( count == 0 ) gc.drawImage(character_1_remain_token_4, 3, 566, this);
			if( count == 1 ) gc.drawImage(character_1_remain_token_3, 3, 566, this);
			if( count == 2 ) gc.drawImage(character_1_remain_token_2, 3, 566, this);
			if( count == 3 ) gc.drawImage(character_1_remain_token_1, 3, 566, this);
			if( count == 4 ) gc.drawImage(character_1_remain_token_0, 3, 566, this);
		}
		if (main.character_1 == 2){
			int count=0;
			for(int i=0; i<4;i++) if( main.horse_2[i] == 99) count++;
			if( count == 0 ) gc.drawImage(character_2_remain_token_4, 3, 566, this);
			if( count == 1 ) gc.drawImage(character_2_remain_token_3, 3, 566, this);
			if( count == 2 ) gc.drawImage(character_2_remain_token_2, 3, 566, this);
			if( count == 3 ) gc.drawImage(character_2_remain_token_1, 3, 566, this);
			if( count == 4 ) gc.drawImage(character_2_remain_token_0, 3, 566, this);
		}
		if (main.character_1 == 3){
			int count=0;
			for(int i=0; i<4;i++) if( main.horse_3[i] == 99) count++;
			if( count == 0 ) gc.drawImage(character_3_remain_token_4, 3, 566, this);
			if( count == 1 ) gc.drawImage(character_3_remain_token_3, 3, 566, this);
			if( count == 2 ) gc.drawImage(character_3_remain_token_2, 3, 566, this);
			if( count == 3 ) gc.drawImage(character_3_remain_token_1, 3, 566, this);
			if( count == 4 ) gc.drawImage(character_3_remain_token_0, 3, 566, this);
		}
		if (main.character_1 == 4){
			int count=0;
			for(int i=0; i<4;i++) if( main.horse_4[i] == 99) count++;
			if( count == 0 ) gc.drawImage(character_4_remain_token_4, 3, 566, this);
			if( count == 1 ) gc.drawImage(character_4_remain_token_3, 3, 566, this);
			if( count == 2 ) gc.drawImage(character_4_remain_token_2, 3, 566, this);
			if( count == 3 ) gc.drawImage(character_4_remain_token_1, 3, 566, this);
			if( count == 4 ) gc.drawImage(character_4_remain_token_0, 3, 566, this);
		}
		if (main.character_2 == 1){
			int count=0;
			for(int i=0; i<4;i++) if( main.horse_1[i] == 99) count++;
			if( count == 0 ) gc.drawImage(character_1_remain_token_4, 141, 566, this);
			if( count == 1 ) gc.drawImage(character_1_remain_token_3, 141, 566, this);
			if( count == 2 ) gc.drawImage(character_1_remain_token_2, 141, 566, this);
			if( count == 3 ) gc.drawImage(character_1_remain_token_1, 141, 566, this);
			if( count == 4 ) gc.drawImage(character_1_remain_token_0, 141, 566, this);
		}
		if (main.character_2 == 2){
			int count=0;
			for(int i=0; i<4;i++) if( main.horse_2[i] == 99) count++;
			if( count == 0 ) gc.drawImage(character_2_remain_token_4, 141, 566, this);
			if( count == 1 ) gc.drawImage(character_2_remain_token_3, 141, 566, this);
			if( count == 2 ) gc.drawImage(character_2_remain_token_2, 141, 566, this);
			if( count == 3 ) gc.drawImage(character_2_remain_token_1, 141, 566, this);
			if( count == 4 ) gc.drawImage(character_2_remain_token_0, 141, 566, this);
		}
		if (main.character_2 == 3){
			int count=0;
			for(int i=0; i<4;i++) if( main.horse_3[i] == 99) count++;
			if( count == 0 ) gc.drawImage(character_3_remain_token_4, 141, 566, this);
			if( count == 1 ) gc.drawImage(character_3_remain_token_3, 141, 566, this);
			if( count == 2 ) gc.drawImage(character_3_remain_token_2, 141, 566, this);
			if( count == 3 ) gc.drawImage(character_3_remain_token_1, 141, 566, this);
			if( count == 4 ) gc.drawImage(character_3_remain_token_0, 141, 566, this);
		}
		if (main.character_2 == 4){
			int count=0;
			for(int i=0; i<4;i++) if( main.horse_4[i] == 99) count++;
			if( count == 0 ) gc.drawImage(character_4_remain_token_4, 141, 566, this);
			if( count == 1 ) gc.drawImage(character_4_remain_token_3, 141, 566, this);
			if( count == 2 ) gc.drawImage(character_4_remain_token_2, 141, 566, this);
			if( count == 3 ) gc.drawImage(character_4_remain_token_1, 141, 566, this);
			if( count == 4 ) gc.drawImage(character_4_remain_token_0, 141, 566, this);
		}

		if (main.character_3 == 1){
			int count=0;
			for(int i=0; i<4;i++) if( main.horse_1[i] == 99) count++;
			if( count == 0 ) gc.drawImage(character_1_remain_token_4, 279, 566, this);
			if( count == 1 ) gc.drawImage(character_1_remain_token_3, 279, 566, this);
			if( count == 2 ) gc.drawImage(character_1_remain_token_2, 279, 566, this);
			if( count == 3 ) gc.drawImage(character_1_remain_token_1, 279, 566, this);
			if( count == 4 ) gc.drawImage(character_1_remain_token_0, 279, 566, this);
		}
		if (main.character_3 == 2){
			int count=0;
			for(int i=0; i<4;i++) if( main.horse_2[i] == 99) count++;
			if( count == 0 ) gc.drawImage(character_2_remain_token_4, 279, 566, this);
			if( count == 1 ) gc.drawImage(character_2_remain_token_3, 279, 566, this);
			if( count == 2 ) gc.drawImage(character_2_remain_token_2, 279, 566, this);
			if( count == 3 ) gc.drawImage(character_2_remain_token_1, 279, 566, this);
			if( count == 4 ) gc.drawImage(character_2_remain_token_0, 279, 566, this);
		}
		if (main.character_3 == 3){
			int count=0;
			for(int i=0; i<4;i++) if( main.horse_3[i] == 99) count++;
			if( count == 0 ) gc.drawImage(character_3_remain_token_4, 279, 566, this);
			if( count == 1 ) gc.drawImage(character_3_remain_token_3, 279, 566, this);
			if( count == 2 ) gc.drawImage(character_3_remain_token_2, 279, 566, this);
			if( count == 3 ) gc.drawImage(character_3_remain_token_1, 279, 566, this);
			if( count == 4 ) gc.drawImage(character_3_remain_token_0, 279, 566, this);
		}
		if (main.character_3 == 4){
			int count=0;
			for(int i=0; i<4;i++) if( main.horse_4[i] == 99) count++;
			if( count == 0 ) gc.drawImage(character_4_remain_token_4, 279, 566, this);
			if( count == 1 ) gc.drawImage(character_4_remain_token_3, 279, 566, this);
			if( count == 2 ) gc.drawImage(character_4_remain_token_2, 279, 566, this);
			if( count == 3 ) gc.drawImage(character_4_remain_token_1, 279, 566, this);
			if( count == 4 ) gc.drawImage(character_4_remain_token_0, 279, 566, this);
		}

		if (main.character_4 == 1){
			int count=0;
			for(int i=0; i<4;i++) if( main.horse_1[i] == 99) count++;
			if( count == 0 ) gc.drawImage(character_1_remain_token_4, 417, 566, this);
			if( count == 1 ) gc.drawImage(character_1_remain_token_3, 417, 566, this);
			if( count == 2 ) gc.drawImage(character_1_remain_token_2, 417, 566, this);
			if( count == 3 ) gc.drawImage(character_1_remain_token_1, 417, 566, this);
			if( count == 4 ) gc.drawImage(character_1_remain_token_0, 417, 566, this);
		}
		if (main.character_4 == 2){
			int count=0;
			for(int i=0; i<4;i++) if( main.horse_2[i] == 99) count++;
			if( count == 0 ) gc.drawImage(character_2_remain_token_4, 417, 566, this);
			if( count == 1 ) gc.drawImage(character_2_remain_token_3, 417, 566, this);
			if( count == 2 ) gc.drawImage(character_2_remain_token_2, 417, 566, this);
			if( count == 3 ) gc.drawImage(character_2_remain_token_1, 417, 566, this);
			if( count == 4 ) gc.drawImage(character_2_remain_token_0, 417, 566, this);
		}
		if (main.character_4 == 3){
			int count=0;
			for(int i=0; i<4;i++) if( main.horse_3[i] == 99) count++;
			if( count == 0 ) gc.drawImage(character_3_remain_token_4, 417, 566, this);
			if( count == 1 ) gc.drawImage(character_3_remain_token_3, 417, 566, this);
			if( count == 2 ) gc.drawImage(character_3_remain_token_2, 417, 566, this);
			if( count == 3 ) gc.drawImage(character_3_remain_token_1, 417, 566, this);
			if( count == 4 ) gc.drawImage(character_3_remain_token_0, 417, 566, this);
		}
		if (main.character_4 == 4){
			int count=0;
			for(int i=0; i<4;i++) if( main.horse_4[i] == 99) count++;
			if( count == 0 ) gc.drawImage(character_4_remain_token_4, 417, 566, this);
			if( count == 1 ) gc.drawImage(character_4_remain_token_3, 417, 566, this);
			if( count == 2 ) gc.drawImage(character_4_remain_token_2, 417, 566, this);
			if( count == 3 ) gc.drawImage(character_4_remain_token_1, 417, 566, this);
			if( count == 4 ) gc.drawImage(character_4_remain_token_0, 417, 566, this);
		}

	}
	public void Set_Horse() {
		int horsechecknum = 0;
		for (int index = 0; index < 4; index++) {
			for (int i = 0; i < 4; i++)
				if (index == 0) {
					horsechecknum = main.horse_1[i];
					if (main.character_1 == 1) {
						if (horsechecknum < 100 && horsechecknum != 99 )
							gc.drawImage(character_1_token_1,
									horseXpos[horsechecknum],
									horseYpos[horsechecknum], this);
						if (horsechecknum >= 100 && horsechecknum < 200)
							gc.drawImage(character_1_token_2,
									horseXpos[horsechecknum - 100],
									horseYpos[horsechecknum - 100], this);
						if (horsechecknum >= 200 && horsechecknum < 300)
							gc.drawImage(character_1_token_3,
									horseXpos[horsechecknum - 200],
									horseYpos[horsechecknum - 200], this);
						if (horsechecknum >= 300)
							gc.drawImage(character_1_token_4,
									horseXpos[horsechecknum - 300],
									horseYpos[horsechecknum - 300], this);
					}
					if (main.character_1 == 2) {
						if (horsechecknum < 100 && horsechecknum != 99 )
							gc.drawImage(character_2_token_1,
									horseXpos[horsechecknum],
									horseYpos[horsechecknum], this);
						if (horsechecknum >= 100 && horsechecknum < 200)
							gc.drawImage(character_2_token_2,
									horseXpos[horsechecknum - 100],
									horseYpos[horsechecknum - 100], this);
						if (horsechecknum >= 200 && horsechecknum < 300)
							gc.drawImage(character_2_token_3,
									horseXpos[horsechecknum - 200],
									horseYpos[horsechecknum - 200], this);
						if (horsechecknum >= 300)
							gc.drawImage(character_2_token_4,
									horseXpos[horsechecknum - 300],
									horseYpos[horsechecknum - 300], this);
					}
					if (main.character_1 == 3) {
						if (horsechecknum < 100 && horsechecknum != 99 )
							gc.drawImage(character_3_token_1,
									horseXpos[horsechecknum],
									horseYpos[horsechecknum], this);
						if (horsechecknum >= 100 && horsechecknum < 200)
							gc.drawImage(character_3_token_2,
									horseXpos[horsechecknum - 100],
									horseYpos[horsechecknum - 100], this);
						if (horsechecknum >= 200 && horsechecknum < 300)
							gc.drawImage(character_3_token_3,
									horseXpos[horsechecknum - 200],
									horseYpos[horsechecknum - 200], this);
						if (horsechecknum >= 300)
							gc.drawImage(character_3_token_4,
									horseXpos[horsechecknum - 300],
									horseYpos[horsechecknum - 300], this);
					}
					if (main.character_1 == 4) {
						if (horsechecknum < 100 && horsechecknum != 99 )
							gc.drawImage(character_4_token_1,
									horseXpos[horsechecknum],
									horseYpos[horsechecknum], this);
						if (horsechecknum >= 100 && horsechecknum < 200)
							gc.drawImage(character_4_token_2,
									horseXpos[horsechecknum - 100],
									horseYpos[horsechecknum - 100], this);
						if (horsechecknum >= 200 && horsechecknum < 300)
							gc.drawImage(character_4_token_3,
									horseXpos[horsechecknum - 200],
									horseYpos[horsechecknum - 200], this);
						if (horsechecknum >= 300)
							gc.drawImage(character_4_token_4,
									horseXpos[horsechecknum - 300],
									horseYpos[horsechecknum - 300], this);
					}
				} else if (index == 1) {
					horsechecknum = main.horse_2[i];
					if (main.character_2 == 1) {
						if (horsechecknum < 100 && horsechecknum != 99 )
							gc.drawImage(character_1_token_1,
									horseXpos[horsechecknum],
									horseYpos[horsechecknum], this);
						if (horsechecknum >= 100 && horsechecknum < 200)
							gc.drawImage(character_1_token_2,
									horseXpos[horsechecknum - 100],
									horseYpos[horsechecknum - 100], this);
						if (horsechecknum >= 200 && horsechecknum < 300)
							gc.drawImage(character_1_token_3,
									horseXpos[horsechecknum - 200],
									horseYpos[horsechecknum - 200], this);
						if (horsechecknum >= 300)
							gc.drawImage(character_1_token_4,
									horseXpos[horsechecknum - 300],
									horseYpos[horsechecknum - 300], this);
					}
					if (main.character_2 == 2) {
						if (horsechecknum < 100 && horsechecknum != 99 )
							gc.drawImage(character_2_token_1,
									horseXpos[horsechecknum],
									horseYpos[horsechecknum], this);
						if (horsechecknum >= 100 && horsechecknum < 200)
							gc.drawImage(character_2_token_2,
									horseXpos[horsechecknum - 100],
									horseYpos[horsechecknum - 100], this);
						if (horsechecknum >= 200 && horsechecknum < 300)
							gc.drawImage(character_2_token_3,
									horseXpos[horsechecknum - 200],
									horseYpos[horsechecknum - 200], this);
						if (horsechecknum >= 300)
							gc.drawImage(character_2_token_4,
									horseXpos[horsechecknum - 300],
									horseYpos[horsechecknum - 300], this);
					}
					if (main.character_2 == 3) {
						if (horsechecknum < 100 && horsechecknum != 99 )
							gc.drawImage(character_3_token_1,
									horseXpos[horsechecknum],
									horseYpos[horsechecknum], this);
						if (horsechecknum >= 100 && horsechecknum < 200)
							gc.drawImage(character_3_token_2,
									horseXpos[horsechecknum - 100],
									horseYpos[horsechecknum - 100], this);
						if (horsechecknum >= 200 && horsechecknum < 300)
							gc.drawImage(character_3_token_3,
									horseXpos[horsechecknum - 200],
									horseYpos[horsechecknum - 200], this);
						if (horsechecknum >= 300)
							gc.drawImage(character_3_token_4,
									horseXpos[horsechecknum - 300],
									horseYpos[horsechecknum - 300], this);
					}
					if (main.character_2 == 4) {
						if (horsechecknum < 100 && horsechecknum != 99  )
							gc.drawImage(character_4_token_1,
									horseXpos[horsechecknum],
									horseYpos[horsechecknum], this);
						if (horsechecknum >= 100 && horsechecknum < 200)
							gc.drawImage(character_4_token_2,
									horseXpos[horsechecknum - 100],
									horseYpos[horsechecknum - 100], this);
						if (horsechecknum >= 200 && horsechecknum < 300)
							gc.drawImage(character_4_token_3,
									horseXpos[horsechecknum - 200],
									horseYpos[horsechecknum - 200], this);
						if (horsechecknum >= 300)
							gc.drawImage(character_4_token_4,
									horseXpos[horsechecknum - 300],
									horseYpos[horsechecknum - 300], this);
					}
				} else if (index == 2) {
					horsechecknum = main.horse_3[i];
					if (main.character_3 == 1) {
						if (horsechecknum < 100 && horsechecknum != 99  )
							gc.drawImage(character_1_token_1,
									horseXpos[horsechecknum],
									horseYpos[horsechecknum], this);
						if (horsechecknum >= 100 && horsechecknum < 200)
							gc.drawImage(character_1_token_2,
									horseXpos[horsechecknum - 100],
									horseYpos[horsechecknum - 100], this);
						if (horsechecknum >= 200 && horsechecknum < 300)
							gc.drawImage(character_1_token_3,
									horseXpos[horsechecknum - 200],
									horseYpos[horsechecknum - 200], this);
						if (horsechecknum >= 300)
							gc.drawImage(character_1_token_4,
									horseXpos[horsechecknum - 300],
									horseYpos[horsechecknum - 300], this);
					}
					if (main.character_3 == 2) {
						if (horsechecknum < 100 && horsechecknum != 99  )
							gc.drawImage(character_2_token_1,
									horseXpos[horsechecknum],
									horseYpos[horsechecknum], this);
						if (horsechecknum >= 100 && horsechecknum < 200)
							gc.drawImage(character_2_token_2,
									horseXpos[horsechecknum - 100],
									horseYpos[horsechecknum - 100], this);
						if (horsechecknum >= 200 && horsechecknum < 300)
							gc.drawImage(character_2_token_3,
									horseXpos[horsechecknum - 200],
									horseYpos[horsechecknum - 200], this);
						if (horsechecknum >= 300)
							gc.drawImage(character_2_token_4,
									horseXpos[horsechecknum - 300],
									horseYpos[horsechecknum - 300], this);
					}
					if (main.character_3 == 3) {
						if (horsechecknum < 100 && horsechecknum != 99 )
							gc.drawImage(character_3_token_1,
									horseXpos[horsechecknum],
									horseYpos[horsechecknum], this);
						if (horsechecknum >= 100 && horsechecknum < 200)
							gc.drawImage(character_3_token_2,
									horseXpos[horsechecknum - 100],
									horseYpos[horsechecknum - 100], this);
						if (horsechecknum >= 200 && horsechecknum < 300)
							gc.drawImage(character_3_token_3,
									horseXpos[horsechecknum - 200],
									horseYpos[horsechecknum - 200], this);
						if (horsechecknum >= 300)
							gc.drawImage(character_3_token_4,
									horseXpos[horsechecknum - 300],
									horseYpos[horsechecknum - 300], this);
					}
					if (main.character_3 == 4) {
						if (horsechecknum < 100 && horsechecknum != 99 )
							gc.drawImage(character_4_token_1,
									horseXpos[horsechecknum],
									horseYpos[horsechecknum], this);
						if (horsechecknum >= 100 && horsechecknum < 200)
							gc.drawImage(character_4_token_2,
									horseXpos[horsechecknum - 100],
									horseYpos[horsechecknum - 100], this);
						if (horsechecknum >= 200 && horsechecknum < 300)
							gc.drawImage(character_4_token_3,
									horseXpos[horsechecknum - 200],
									horseYpos[horsechecknum - 200], this);
						if (horsechecknum >= 300)
							gc.drawImage(character_4_token_4,
									horseXpos[horsechecknum - 300],
									horseYpos[horsechecknum - 300], this);
					}
				} else if (index == 3) {
					horsechecknum = main.horse_4[i];
					if (main.character_4 == 1) {
						if (horsechecknum < 100 && horsechecknum != 99 )
							gc.drawImage(character_1_token_1,
									horseXpos[horsechecknum],
									horseYpos[horsechecknum], this);
						if (horsechecknum >= 100 && horsechecknum < 200)
							gc.drawImage(character_1_token_2,
									horseXpos[horsechecknum - 100],
									horseYpos[horsechecknum - 100], this);
						if (horsechecknum >= 200 && horsechecknum < 300)
							gc.drawImage(character_1_token_3,
									horseXpos[horsechecknum - 200],
									horseYpos[horsechecknum - 200], this);
						if (horsechecknum >= 300)
							gc.drawImage(character_1_token_4,
									horseXpos[horsechecknum - 300],
									horseYpos[horsechecknum - 300], this);
					}
					if (main.character_4 == 2) {
						if (horsechecknum < 100 && horsechecknum != 99 )
							gc.drawImage(character_2_token_1,
									horseXpos[horsechecknum],
									horseYpos[horsechecknum], this);
						if (horsechecknum >= 100 && horsechecknum < 200)
							gc.drawImage(character_2_token_2,
									horseXpos[horsechecknum - 100],
									horseYpos[horsechecknum - 100], this);
						if (horsechecknum >= 200 && horsechecknum < 300)
							gc.drawImage(character_2_token_3,
									horseXpos[horsechecknum - 200],
									horseYpos[horsechecknum - 200], this);
						if (horsechecknum >= 300)
							gc.drawImage(character_2_token_4,
									horseXpos[horsechecknum - 300],
									horseYpos[horsechecknum - 300], this);
					}
					if (main.character_4 == 3) {
						if (horsechecknum < 100 && horsechecknum != 99  )
							gc.drawImage(character_3_token_1,
									horseXpos[horsechecknum],
									horseYpos[horsechecknum], this);
						if (horsechecknum >= 100 && horsechecknum < 200)
							gc.drawImage(character_3_token_2,
									horseXpos[horsechecknum - 100],
									horseYpos[horsechecknum - 100], this);
						if (horsechecknum >= 200 && horsechecknum < 300)
							gc.drawImage(character_3_token_3,
									horseXpos[horsechecknum - 200],
									horseYpos[horsechecknum - 200], this);
						if (horsechecknum >= 300)
							gc.drawImage(character_3_token_4,
									horseXpos[horsechecknum - 300],
									horseYpos[horsechecknum - 300], this);
					}
					if (main.character_4 == 4) {
						if (horsechecknum < 100 && horsechecknum != 99  )
							gc.drawImage(character_4_token_1,
									horseXpos[horsechecknum],
									horseYpos[horsechecknum], this);
						if (horsechecknum >= 100 && horsechecknum < 200)
							gc.drawImage(character_4_token_2,
									horseXpos[horsechecknum - 100],
									horseYpos[horsechecknum - 100], this);
						if (horsechecknum >= 200 && horsechecknum < 300)
							gc.drawImage(character_4_token_3,
									horseXpos[horsechecknum - 200],
									horseYpos[horsechecknum - 200], this);
						if (horsechecknum >= 300)
							gc.drawImage(character_4_token_4,
									horseXpos[horsechecknum - 300],
									horseYpos[horsechecknum - 300], this);
					}
				}
		}
	}
	public void Set_Yut(){
		if( main.member == main.turn ){
			if( main.run == -1 )
				gc.drawImage(y_bdo, 600,80, this	);
			if( main.run == 1 )
				gc.drawImage(y_do, 600,80, this);
			if( main.run == 2 )
				gc.drawImage(y_gae, 600,80, this);
			if( main.run == 3 )
				gc.drawImage(y_gul, 600,80, this);
			if( main.run == 4 )
				gc.drawImage(y_yut, 600,80, this);
			if( main.run == 5 )
				gc.drawImage(y_mo, 600,80, this);
		}
	}
}
