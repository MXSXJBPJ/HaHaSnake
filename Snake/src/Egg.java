import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import org.omg.CORBA.PRIVATE_MEMBER;
/*
 * 定义蛋
 * */

public class Egg {
	int row,col;//出现在哪个位置
	int w=Yard.BLOCK_SIZE;
	int h = Yard.BLOCK_SIZE;
	private static Random r = new Random();
	private Color color = Color.GREEN;
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	

	public Egg(int row, int col) {
	
		this.row = row;
		this.col = col;
	
	}
	
	public Egg(){
		this(r.nextInt(Yard.ROWS-3)+3, r.nextInt(Yard.COLS));
	}
	
	public void reAppear(){
		this.row = r.nextInt(Yard.ROWS-3)+3;
		this.col = r.nextInt(Yard.COLS);
	}
	
	public Rectangle getRect(){
		return new Rectangle(Yard.BLOCK_SIZE*col,Yard.BLOCK_SIZE*row,w,h);
	}
	
	public void draw(Graphics g){
		 Color c=g.getColor();
		 g.setColor(color);
		 g.fillOval(Yard.BLOCK_SIZE*col, Yard.BLOCK_SIZE*row, w, h);
		 g.setColor(c);
		 if(color == Color.GREEN){
			 color = Color.RED;
			 }
		 else{
			 color = Color.GREEN; 
		 }
	
	}
}
