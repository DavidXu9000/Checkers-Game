package draughts;

import java.util.ArrayList;

public class Piece {
	private boolean isKing;
	private boolean red;
	private Board board;
	private int x;
	private int y;
	private int weight = 100;

	public Piece(boolean red, Board board, int x, int y) {
		this.red = red;
		this.board = board;
		isKing = false;
		this.x = x;
		this.y = y;
	}

	public boolean isKing() {
		return isKing;
	}

	public void setBoard(Board b) {
		this.board=b;
	}
	
	public boolean isRed() {
		return red;
	}

	public Board getBoard() {
		return board;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public boolean getColor() {
		return red;
	}

	public boolean getStatus() {
		return isKing;
	}

	public void setStatus(boolean b) {
		isKing = b;
	}
	
	public void setColor(boolean b) {
		this.red=b;
	}
	
	public ArrayList<Move> getPossibleMoves() {
		ArrayList<Move> moves = new ArrayList<Move>();
		return getPossibleMoves(x, y, moves, red);
	}

	private boolean isValid(int x) {
		if (x >= 0 && x < 8)
			return true;
		return false;
	}

	private void copy(ArrayList<Piece> a, ArrayList<Piece> b) {
		for (Piece p : a) {
			b.add(p);
		}
	}

	private ArrayList<Move> getPossibleMoves(int x, int y, ArrayList<Move> moves, boolean red) {
		int offset = 0;
		if (!isKing && red) {
			offset = -1;
		}
		if (!isKing && !red) {
			offset = 1;
		}
		if (!isKing) {
			int leftStepx = x + offset;
			int leftStepy = y - 1;
			int rightStepx = x + offset;
			int rightStepy = y + 1;

			if (isValid(leftStepx) && isValid(leftStepy)) {
				if (board.getBoard()[leftStepx][leftStepy] == null) {
					moves.add(new Move(x, y, leftStepx, leftStepy, this));
				}
			}

			if (isValid(rightStepx) && isValid(rightStepy)) {
				if (board.getBoard()[rightStepx][rightStepy] == null) {
					moves.add(new Move(x, y, rightStepx, rightStepy, this));
				}
			}
			ArrayList<Piece> piecesTaken = new ArrayList<Piece>();
			pieceJump(piecesTaken, moves, x, y, offset, 0, x, y);
			return moves;
		} else {
			int northEastx = x - 1;
			int northEasty = y + 1;
			int northWestx = x - 1;
			int northWesty = y - 1;
			int southEastx = x + 1;
			int southEasty = y + 1;
			int southWestx = x + 1;
			int southWesty = y - 1;

			if (isValid(northEastx) && isValid(northEasty)) {
				if (board.getBoard()[northEastx][northEasty] == null) {
					moves.add(new Move(x, y, northEastx, northEasty, this));
				}
			}
			if (isValid(northWestx) && isValid(northWesty)) {
				if (board.getBoard()[northWestx][northWesty] == null) {
					moves.add(new Move(x, y, northWestx, northWesty, this));
				}
			}
			if (isValid(southEastx) && isValid(southEasty)) {
				if (board.getBoard()[southEastx][southEasty] == null) {
					moves.add(new Move(x, y, southEastx, southEasty, this));
				}
			}
			if (isValid(southWestx) && isValid(southWesty)) {
				if (board.getBoard()[southWestx][southWesty] == null) {
					moves.add(new Move(x, y, southWestx, southWesty, this));
				}
			}

			ArrayList<Piece> pieces = new ArrayList<Piece>();
			kingJump(pieces, moves, x, y, 0, x, y);
			return moves;
		}
	}

	private void pieceJump(ArrayList<Piece> piecesTaken, ArrayList<Move> moves, int x, int y, int offset, int depth,
			int startx, int starty) {
		int leftJumpx = x + 2 * offset;
		int leftJumpy = y - 2;
		int rightJumpx = x + 2 * offset;
		int rightJumpy = y + 2;
		boolean notLeaf = false;

		if (isValid(leftJumpx) && isValid(leftJumpy)) {
			if (board.getBoard()[leftJumpx][leftJumpy] == null && board.getBoard()[x + offset][y - 1] != null) {
				if (board.getBoard()[x + offset][y - 1].getColor() == !red) {
					piecesTaken.add(board.getBoard()[x + offset][y - 1]);
					pieceJump(piecesTaken, moves, leftJumpx, leftJumpy, offset, depth + 1, startx, starty);
					piecesTaken.remove(piecesTaken.size() - 1);
					notLeaf = true;
				}
			}
		}

		if (isValid(rightJumpx) && isValid(rightJumpy)) {
			if (board.getBoard()[rightJumpx][rightJumpy] == null && board.getBoard()[x + offset][y + 1] != null) {
				if (board.getBoard()[x + offset][y + 1].getColor() == !red) {
					piecesTaken.add(board.getBoard()[x + offset][y + 1]);
					pieceJump(piecesTaken, moves, rightJumpx, rightJumpy, offset, depth + 1, startx, starty);
					piecesTaken.remove(piecesTaken.size() - 1);
					notLeaf = true;
				}
			}
		}
		if (depth != 0 && !notLeaf) {
			Move m = new Move(startx, starty, x, y, this);
			moves.add(m);
			copy(piecesTaken, m.getList());
		}
		return;
	}
	
	private boolean contains(Piece p, ArrayList<Piece> list) {
		for(Piece piece: list) {
			if(piece.getX()==p.getX() && piece.getY()==p.getY())
				return true;
		}
		return false;
	}
	
	public void kingJump(ArrayList<Piece> piecesTaken, ArrayList<Move> moves, int x, int y, int depth, int startx,
			int starty) {
		int northEastx = x - 2;
		int northEasty = y + 2;
		int northWestx = x - 2;
		int northWesty = y - 2;
		int southEastx = x + 2;
		int southEasty = y + 2;
		int southWestx = x + 2;
		int southWesty = y - 2;
		boolean notLeaf = false;

		if (isValid(northEastx) && isValid(northEasty)) {
			if (board.getBoard()[northEastx][northEasty] == null && board.getBoard()[x - 1][y + 1] != null) {
				if(board.getBoard()[x - 1][y + 1].getColor() == !red && !contains(board.getBoard()[x-1][y+1],piecesTaken)) {
					piecesTaken.add(board.getBoard()[x - 1][y + 1]);
					kingJump(piecesTaken, moves, northEastx, northEasty, depth + 1, startx, starty);
					piecesTaken.remove(piecesTaken.size() - 1);
					notLeaf = true;
				}
			}
		}

		if (isValid(northWestx) && isValid(northWesty)) {
			if (board.getBoard()[northWestx][northWesty] == null && board.getBoard()[x - 1][y - 1] != null) {
				if(board.getBoard()[x - 1][y - 1].getColor() == !red && !contains(board.getBoard()[x-1][y-1],piecesTaken)) {
					piecesTaken.add(board.getBoard()[x - 1][y - 1]);
					kingJump(piecesTaken, moves, northWestx, northWesty, depth + 1, startx, starty);
					piecesTaken.remove(piecesTaken.size() - 1);
					notLeaf = true;
				}
			}
		}

		if (isValid(southEastx) && isValid(southEasty)) {
			if (board.getBoard()[southEastx][southEasty] == null && board.getBoard()[x + 1][y + 1] != null) {
				if(board.getBoard()[x + 1][y + 1].getColor() == !red && !contains(board.getBoard()[x+1][y+1],piecesTaken)) {
					piecesTaken.add(board.getBoard()[x + 1][y + 1]);
					kingJump(piecesTaken, moves, southEastx, southEasty, depth + 1, startx, starty);
					piecesTaken.remove(piecesTaken.size() - 1);
					notLeaf = true;
				}
			}
		}

		if (isValid(southWestx) && isValid(southWesty)) {
			if (board.getBoard()[southWestx][southWesty] == null && board.getBoard()[x + 1][y - 1] != null) {
				if(board.getBoard()[x + 1][y - 1].getColor() == !red && !contains(board.getBoard()[x+1][y-1],piecesTaken)) {
					piecesTaken.add(board.getBoard()[x + 1][y - 1]);
					kingJump(piecesTaken, moves, southWestx, southWesty, depth + 1, startx, starty);
					piecesTaken.remove(piecesTaken.size() - 1);
					notLeaf = true;
				}
			}
		}
		if (depth != 0 && !notLeaf) {
			Move m = new Move(startx, starty, x, y, this);
			moves.add(m);
			copy(piecesTaken, m.getList());
		}
		return;
	}

	public String toString() {
		return "" + x + " " + y;
	}

}