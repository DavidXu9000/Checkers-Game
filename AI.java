package draughts;

import java.util.ArrayList;

public class AI {

	private boolean red;
	private int depth = 8;
	public AI(boolean red) {
		this.red = red;
	}

	public void toggle() {
		if(depth == 8)
			depth = 4;
		else {
			depth = 8;
		}
	}
	
	public int getDepth() {
		return depth;
	}
	
	private double evaluateBoard(Board b) {
		double score = 0.0;
		
		Piece[][] pieces = b.getBoard();
		for(Piece[] row : pieces) {
			for(Piece p : row) {
				if(p != null) {
					if(p.getColor() == red) {
						score -= pieceValue(b,p.getColor(),p);
					} else {
						score += pieceValue(b,p.getColor(),p);
					}
				}
			}
		}
		score+=control(b);
		return score;
	}
	
	private double pieceValue(Board b, boolean red, Piece p) {
		double value = p.getWeight();
		if(p.getY()==0 || p.getY()==7)
			value += 5;
		if(red==true && p.getX()>=4 && p.getStatus()==false)
			value += 5*p.getX();
		if(red == false && p.getX()<=3 && p.getStatus()==false)
			value += 5*(7-p.getX());
		if(p.getStatus()==true && p.getPossibleMoves().size()==0)
			value -= 50;
		if(p.getStatus()==false) {
			int i = p.getPossibleMoves().size();
			value += 5*(2-i);
		}
		return value;
	}
	
	private double control(Board b) {
		double value = 0;
		for(int i = 3; i <=4; i++) {
			for(int k = 3; k<=4; k++) {
				value +=squareCheck(i, k, b, 5);
			}
		}
		for(int i = 2; i <=5; i++) {
			value += squareCheck(5, i, b, 2);
		}
		value += squareCheck(4,2,b,3);
		value += squareCheck(4,5,b,3);
		value += squareCheck(3,5,b,3);
		value += squareCheck(3,2,b,3);
		for(int i = 2; i <=5; i++) {
			value += squareCheck(2, i, b, 2);
		}
		return value;
	}
	private double squareCheck(int x, int y, Board b, int value) {
		if(b.getBoard()[x][y]!=null) {
			if(b.getBoard()[x][y].getColor()==true)
				return value;
			else {
				return value * -1;
			}
		}
		return 0;
	}
	
	private Double minimax(Board b, int depth, boolean red, double alpha, double beta) {
		
		ArrayList<Move> moves = b.getLegalMoves(red);
		
		if(depth == 0) {
			// System.out.println(evaluateBoard(b));
			return evaluateBoard(b);
		}
		double bestMove;
		if(this.red == red) {
			bestMove = Double.MAX_VALUE;
			for(Move m : moves) {
				Board testBoard = b.cloneBoard();			
				testBoard.updateBoard(m);
				double result = minimax(testBoard, depth-1, !red, alpha, beta);
				bestMove = Math.min(bestMove, result);
				beta = Math.min(beta, bestMove);
				
				if(beta <= alpha)
					break;
			}
		}
		
		else {
			bestMove = Double.MIN_VALUE;
			for(Move m : moves) {
				Board testBoard = b.cloneBoard();
				testBoard.updateBoard(m);
				double result = minimax(testBoard, depth-1, !red, alpha, beta);
				bestMove = Math.max(bestMove, result);
				alpha = Math.max(alpha, bestMove);
				
				if(beta <= alpha)
					break;
			}
		}
		return bestMove;

	}
	
	private Move minimaxRoot(Board b, int depth, boolean red) {
		ArrayList<Move> moves = b.getLegalMoves(red);
		double bestMove = Double.MAX_VALUE;
		Move bestMoveFound = null;
		double alpha = Double.MIN_VALUE;
		double beta = Double.MAX_VALUE;
		for(Move m : moves) {
			Board testBoard = b.cloneBoard();
			testBoard.updateBoard(m);
			double val = minimax(testBoard, depth-1, !red, alpha, beta);
			if(val < bestMove) {
				bestMove = val;
				bestMoveFound = m;
			}
			beta = Math.min(bestMove, beta);
		}
		
		return bestMoveFound;
		
	}
	
	
	public Move getMove(Board b) {
		return minimaxRoot(b, depth, this.red);
	}
	
}