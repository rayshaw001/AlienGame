import java.util.Random;
import java.util.Scanner;
import static java.lang.System.out;

public class AlienGame {
	public static void main(String args[]){
		if(args!=null && args.length==3){
			int field=Integer.parseInt(args[0]);
			int life=Integer.parseInt(args[1]);	// or life?
			int aliens = Integer.parseInt(args[2]);
			if(aliens >= field*field || aliens <= 0){
				System.out.println("参数无效");
				System.exit(1);
			}else{
				Map map = new Map(field,life,aliens);
				map.play();
			}
		}else{
			System.out.println("参数无效");
			System.exit(1);
		}
	}
}

class Alien extends Life{
	private boolean isAlive = true;
	
	public boolean getIsAlive(){
		return this.isAlive;
	}
	public  void setIsAlive(boolean isAlive){
		this.isAlive = isAlive;
	}
}

class Player extends Life{
	private int life;
	
	public void setLife(int life){
		this.life = life;
	}
	public int getLife(){
		return this.life;
	}
}

class Map{
	private static final Random r = new Random();

	Life[][] map;
	Player player;
	Alien[] aliens;
	
	public Map(int field,int life,int aliens){
		this.map = new Life[field][field];
		for(Life[] lifes:map){
			lifes = new Life[field];
		}
		this.player = new Player();
		this.player.setLife(life);
		setLocation(this.player,'P');
		this.player.setLife(life);
		this.aliens = new Alien[aliens];
		for(int i=0;i<aliens;i++){
			this.aliens[i]=new Alien();
			setLocation(this.aliens[i],'A');
		}
	}
	
	private void setLocation(Life life,char type){
		int x = r.nextInt(map.length);
		int y = r.nextInt(map.length);
		if(map[x][y] == null){
			map[x][y] = life;
			life.setValue(type);
			life.setX(x);
			life.setY(y);
		}else{
			setLocation(life,type);
		}
	}

	public boolean play(){
		Scanner sc = new Scanner(System.in);
		int x = 0,y = 0;
		while(true){
			out.println(this.toString());
			out.println("Der Spieler hat noch " + this.player.getLife() + " Hitpoints");
			try {
				out.println("Wohin soll der Spieler angreifen? (X−Koordinate )");
				y = sc.nextInt();
				out.println("Wohin soll der Spieler angreifen? (Y−Koordinate )");
				x = sc.nextInt();
			}catch (Exception e){
				out.println("不合法的输入，本次攻击无效");
			}
			if(x<0 || x >= map.length || y<0 || y >= map.length){
				out.println("无效的输入，本次攻击无效");
			}
			if(map[x][y]==null){
				out.println("没有攻击目标，本次攻击无效");
			}else if(map[x][y].getValue()=='A') {
				if(this.player.attack(map[x][y])){
					out.println("玩家击中了外星人");
					map[x][y].setValue('X');
					((Alien)map[x][y]).setIsAlive(false);
					if(checkIfAliensAllDie()){
						out.println("Der Spieler hat alle Aliens besiegt!");
						sc.close();
						return true;
					}
				}else{
					out.println("玩家没有击中外星人");
				}
			}else if(map[x][y].getValue()=='P'){
				out.println("无法攻击自己，本次攻击无效");
			}else{
				out.print("外星人已经死亡，本次攻击无效");
			}
			for(Alien alien:this.aliens){
				if(alien.getIsAlive()){
					out.println("Das Alien bei (" + alien.getX() + "," + alien.getY() + ") greift den Spoeler an");
					if(alien.attack(this.player)){
						out.println("Das Alien hat den Spieler getroffen");
						this.player.setLife(this.player.getLife()-1);
						if(this.player.getLife()<=0) {
							out.println("外星人战胜了玩家");
							sc.close();
							return false;
						}
					}else{
						out.println("Das Alien hat den Spieler getroffen");
					}
				}
			}
		}
	}

	private boolean checkIfAliensAllDie(){
		for(Alien alien:this.aliens){
			if(alien.getIsAlive()){
				return false;
			}
		}
		return true;
	}
	
	public String toString(){
		String result = "";
		int size = this.map.length + 3;
		int length = this.map.length;
		for(int row=0;row<size;row++){
			if(row==0){
				result+="  ";
				for(int column=0;column<length;column++){
					result+=column;
				}
				result+=" ";
			}else if(row==1||row==size-1){
				result+=" ";
				for (int column=0;column<size-1;column++){
					result+='*';
				}
			}else{
				result+=row-2;
				result+='*';
				for(int column=0;column<length;column++){
					result+=map[row-2][column]==null?' ':map[row-2][column].getValue();
				}
				result+='*';
			}
			result += System.getProperty("line.separator");
		}
		return result;
	}
}

class Life{
	private static final Random r = new Random();

	private char value;
	private int location_x;
	private int location_y;

	public boolean attack(Life life){
		int possiblity=Math.abs(this.getX()-life.getX())+Math.abs(this.getY()-life.getY());
		return r.nextInt(possiblity)==possiblity-1;
	}

	public char getValue(){
		return this.value;
	}

	public void setValue(char value){
		this.value = value;
	}
	
	public int getX(){
		return this.location_x;
	}
	
	public void setX(int x){
		this.location_x = x;
	}
	
	public int getY(){
		return this.location_y;
	}
	
	public void setY(int y){
		this.location_y = y;
	}
}