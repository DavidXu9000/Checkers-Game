package draughts;

import java.util.ArrayList;

public class Move {
	private int startx; 
	private int starty;
	private int endx;
	private int endy;
	public Piece piece;
	private ArrayList<Piece> piecesTaken = new ArrayList<Piece>();
	
	public Move(int a, int b, int c, int d, Piece piece) {
		startx=a;
		starty=b;
		endx=c;
		endy=d;
		this.piece=piece;
	}
	
	public void addPiece(Piece p) {
		piecesTaken.add(p);
	}
	
	public ArrayList<Piece> getList(){
		return piecesTaken;
	}

	public int getStartx() {
		return startx;
	}

	public int getStarty() {
		return starty;
	}

	public int getEndx() {
		return endx;
	}

	public int getEndy() {
		return endy;
	}
	
	public ArrayList<Piece> taken(){
		return piecesTaken;
	}
	
	public String toString() {
		return ""+startx + " " + starty + " : " + endx + " " + endy;
	}
}