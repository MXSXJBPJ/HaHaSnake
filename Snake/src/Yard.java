import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
/**
 * 这个类代表贪吃蛇的活动场所
 * @author bpj
 * @version 1.0
 */

public class Yard extends Frame {//创建窗口
	/**
	 * @param args
	 */
	PaintThread	paintThread = new  PaintThread();
	
	private boolean gameOver = false;//游戏是否结束
	private boolean gamePause = false;//游戏是否暂停
	
	/**
	 * 行数
	 * 列数
	 */
	public static final int ROWS=50;//横 xx格   定义为常量
	public static final int COLS=50;//竖 xx格   定义为常量
	public static final int BLOCK_SIZE=15;//每格大小  定义为常量
	
	private int score = 0;//得分
	
	Snake s = new Snake(this);//创建Snake实例s
	Egg e = new Egg();//创建Egg实例e
	
	Image offScreenImage=null;//双缓冲技术 不使用会导致 repaint()方法会出现动画的闪烁
	
	
	public void launch(){//出现方法
		this.setLocation(200,200);//出现位置
		this.setSize(COLS*BLOCK_SIZE,ROWS*BLOCK_SIZE);//大小
		this.addWindowListener(new WindowAdapter() {//窗口监听
			@Override
			public void windowClosing(WindowEvent e) {//关闭窗口
				// TODO Auto-generated method stub
				/*
				 * System.exit(0)是将你的整个虚拟机里的
				 * 内容都停掉了 ，而dispose()只是关闭这个窗口，但是并没有停止整个application exit() 。
				 * 无论如何，内存都释放了！也就是说连JVM都关闭了，内存里根本不可能还有什么东西
				 * System.exit(0)是正常退出程序，而System.exit(1)或者说非0表示非正常退出程序
				 * System.exit(status)不管status为何值都会退出程序。和return 相比有以下不同点：   return是回到上一层，
				 * 而System.exit(status)是回到最上层
				 * 
				 * 在一个if-else判断中，如果我们程序是按照我们预想的执行，到最后我们需要停止程序，那么我们使用System.exit(0)，
				 * 而System.exit(1)一般放在catch块中，当捕获到异常，需要停止程序，
				 * 我们使用System.exit(1)。这个status=1是用来表示这个程序是非正常退出。
				 * 
				 * */
				System.exit(0);
			}	
		});
		this.setVisible(true);//窗体要设置可见（使用setVisible(true)）。因为最初创建窗体的时候通过构造函数创建的是一个新的，不可见的，具有指定标题的窗体。
		this.addKeyListener(new KeyMonitor());
		
		new Thread (paintThread).start();
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Yard().launch();
	}
	

	public void stop(){
		gameOver = true;
	}


	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_F2) {
				paintThread.reStart();
			}
			if(key == KeyEvent.VK_F1) {
				
				paintThread.pause();
			}
			if(key == KeyEvent.VK_F3) {
				gamePause = false;
				paintThread.resume();
			}
			s.keyPressed(e);
		}
		
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		Color c=g.getColor();
		g.setColor(Color.gray);
		g.fillRect(0, 0, COLS*BLOCK_SIZE,ROWS*BLOCK_SIZE);
		g.setColor(Color.DARK_GRAY);
		//画出横线
		for(int i=1;i<ROWS;i++){
			g.drawLine(0, BLOCK_SIZE*i, COLS*BLOCK_SIZE, BLOCK_SIZE*i);
		}
		//画出竖线
		for(int i=1;i<COLS;i++){
			g.drawLine(BLOCK_SIZE*i,0, BLOCK_SIZE*i, BLOCK_SIZE*ROWS);
		}
		g.setColor(Color.YELLOW);
		g.drawString("Score:"+score+"                   "+"F1：暂停游戏"+" "+"F2：重新开始游戏"+"  "+"F3：继续游戏", 10, 60);
		
		if (gameOver) {
			g.setFont(new Font("华文彩云",Font.BOLD,50));
			g.drawString("游戏结束", 260, 300);
			g.setFont(new Font("华文彩云",Font.BOLD,30));
			g.drawString("得分:"+score, 310, 360);
			g.setFont(new Font("宋体",Font.BOLD,16));
			g.drawString("按F2重新开始游戏", 290, 390);
			paintThread.gameOver();
		}
					
		g.setColor(c);
		
		s.eat(e);
		s.draw(g);
		e.draw(g);
		
	
	}
	public void update(Graphics g) {
		// TODO Auto-generated method stub
		if(offScreenImage == null){
			offScreenImage = this.createImage(COLS*BLOCK_SIZE,ROWS*BLOCK_SIZE);
		}
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0,null);
	}

	private class PaintThread implements Runnable{
		private boolean pause = false;
		private boolean running = true;
		public void run(){
			while (running) {
				repaint();
				try {
					
					Thread.sleep(100);
					
				} catch (InterruptedException e) {
					// TODO: handle exception
					e.printStackTrace();
					
				}
				while (pause) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}

			}
		}
		  /**
         * 暂停
         */
		public void pause() {
			this.pause = true;
			
			
		}
		public void resume(){
			this.pause = false;
			
        }
		
		public void reStart() {
			
			this.pause = false;
			s = new Snake(Yard.this);
			gameOver = false;
			score = 0;
			
		}
		
		public void gameOver() {
			running = false;
			
			running = true;//因为方法在线程里停止后无法使用同线程的方法
		}
	}

	/**
	 * 拿到所得的分数
	 * @return 分数
	 */	
	
	public int getScore() {
		return score;
	}


	
	/**
	 * 设置所得的分数
	 * @param score 分数
	 */


	public void setScore(int score) {
		this.score = score;
	}

	

	



	
	
	


}
