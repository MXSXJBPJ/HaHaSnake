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
 * ��������̰���ߵĻ����
 * @author bpj
 * @version 1.0
 */

public class Yard extends Frame {//��������
	/**
	 * @param args
	 */
	PaintThread	paintThread = new  PaintThread();
	
	private boolean gameOver = false;//��Ϸ�Ƿ����
	private boolean gamePause = false;//��Ϸ�Ƿ���ͣ
	
	/**
	 * ����
	 * ����
	 */
	public static final int ROWS=50;//�� xx��   ����Ϊ����
	public static final int COLS=50;//�� xx��   ����Ϊ����
	public static final int BLOCK_SIZE=15;//ÿ���С  ����Ϊ����
	
	private int score = 0;//�÷�
	
	Snake s = new Snake(this);//����Snakeʵ��s
	Egg e = new Egg();//����Eggʵ��e
	
	Image offScreenImage=null;//˫���弼�� ��ʹ�ûᵼ�� repaint()��������ֶ�������˸
	
	
	public void launch(){//���ַ���
		this.setLocation(200,200);//����λ��
		this.setSize(COLS*BLOCK_SIZE,ROWS*BLOCK_SIZE);//��С
		this.addWindowListener(new WindowAdapter() {//���ڼ���
			@Override
			public void windowClosing(WindowEvent e) {//�رմ���
				// TODO Auto-generated method stub
				/*
				 * System.exit(0)�ǽ����������������
				 * ���ݶ�ͣ���� ����dispose()ֻ�ǹر�������ڣ����ǲ�û��ֹͣ����application exit() ��
				 * ������Σ��ڴ涼�ͷ��ˣ�Ҳ����˵��JVM���ر��ˣ��ڴ�����������ܻ���ʲô����
				 * System.exit(0)�������˳����򣬶�System.exit(1)����˵��0��ʾ�������˳�����
				 * System.exit(status)����statusΪ��ֵ�����˳����򡣺�return ��������²�ͬ�㣺   return�ǻص���һ�㣬
				 * ��System.exit(status)�ǻص����ϲ�
				 * 
				 * ��һ��if-else�ж��У�������ǳ����ǰ�������Ԥ���ִ�У������������Ҫֹͣ������ô����ʹ��System.exit(0)��
				 * ��System.exit(1)һ�����catch���У��������쳣����Ҫֹͣ����
				 * ����ʹ��System.exit(1)�����status=1��������ʾ��������Ƿ������˳���
				 * 
				 * */
				System.exit(0);
			}	
		});
		this.setVisible(true);//����Ҫ���ÿɼ���ʹ��setVisible(true)������Ϊ������������ʱ��ͨ�����캯����������һ���µģ����ɼ��ģ�����ָ������Ĵ��塣
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
		//��������
		for(int i=1;i<ROWS;i++){
			g.drawLine(0, BLOCK_SIZE*i, COLS*BLOCK_SIZE, BLOCK_SIZE*i);
		}
		//��������
		for(int i=1;i<COLS;i++){
			g.drawLine(BLOCK_SIZE*i,0, BLOCK_SIZE*i, BLOCK_SIZE*ROWS);
		}
		g.setColor(Color.YELLOW);
		g.drawString("Score:"+score+"                   "+"F1����ͣ��Ϸ"+" "+"F2�����¿�ʼ��Ϸ"+"  "+"F3��������Ϸ", 10, 60);
		
		if (gameOver) {
			g.setFont(new Font("���Ĳ���",Font.BOLD,50));
			g.drawString("��Ϸ����", 260, 300);
			g.setFont(new Font("���Ĳ���",Font.BOLD,30));
			g.drawString("�÷�:"+score, 310, 360);
			g.setFont(new Font("����",Font.BOLD,16));
			g.drawString("��F2���¿�ʼ��Ϸ", 290, 390);
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
         * ��ͣ
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
			
			running = true;//��Ϊ�������߳���ֹͣ���޷�ʹ��ͬ�̵߳ķ���
		}
	}

	/**
	 * �õ����õķ���
	 * @return ����
	 */	
	
	public int getScore() {
		return score;
	}


	
	/**
	 * �������õķ���
	 * @param score ����
	 */


	public void setScore(int score) {
		this.score = score;
	}

	

	



	
	
	


}
