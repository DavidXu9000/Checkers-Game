package draughts;

import java.util.ArrayList;

public class Board {
	private int draw = 0;
	private Piece[][] pieces;

	public Piece[][] getBoard() {
		return pieces;
	}

	public Board(Piece[][] pieces, int draw) {
		this.pieces = pieces;
		this.draw = draw;
	}

	// unconditionally updates the board
	// GUI responsible for restricting user to legal moves
	public void updateBoard(Move m) {
		int startx = m.getStartx();
		int endx = m.getEndx();
		int starty = m.getStarty();
		int endy = m.getEndy();

		// reset piece location
		pieces[endx][endy] = pieces[startx][starty];
		pieces[endx][endy].setX(endx);
		pieces[endx][endy].setY(endy);

		pieces[startx][starty] = null;

		// promote to King
		if (endx == 7 && pieces[endx][endy].getStatus() == false && pieces[endx][endy].getColor() == false) {
			pieces[endx][endy].setStatus(true);
			pieces[endx][endy].setWeight(300);
		}
		if (endx == 0 && pieces[endx][endy].getStatus() == false && pieces[endx][endy].getColor() == true) {
			pieces[endx][endy].setStatus(true);
			pieces[endx][endy].setWeight(300);
		}
		if (m.getList().size() == 0)
			draw++;
		else {
			draw = 0;
		}

		// remove dead pieces
		for (Piece p : m.getList()) {
			pieces[p.getX()][p.getY()] = null;
			p = null;
		}
	}

	public ArrayList<Move> getLegalMoves(boolean red) {
		ArrayList<Move> list = new ArrayList<Move>();
		for (int i = 0; i < 8; i++) {
			for (int k = 0; k < 8; k++) {
				if (pieces[i][k] != null)
					if (pieces[i][k].getColor() == red) {

						list.addAll(pieces[i][k].getPossibleMoves());
					}
			}
		}
		return list;
	}

	public ArrayList<Piece> getPieces(boolean red) {
		ArrayList<Piece> list = new ArrayList<Piece>();
		for (int i = 0; i < 8; i++) {
			for (int k = 0; k < 8; k++) {
				if (pieces[i][k] != null) {
					if (pieces[i][k].getColor() == red)
						list.add(pieces[i][k]);
				}
			}
		}
		return list;
	}

	public boolean isStalemate() {
		if (draw > 50)
			return true;
		return false;
	}

	public boolean isWinner(boolean red) {
		if (count(!red) == 0)
			return true;
		if (getLegalMoves(!red).size() == 0)
			return true;
		return false;
	}

	private int count(boolean red) {
		int count = 0;
		for (int i = 0; i < 8; i++) {
			for (int k = 0; k < 8; k++) {
				if (pieces[i][k] != null)
					if (pieces[i][k].getColor() == red)
						count++;
			}
		}
		return count;
	}
	
	public int draw() { return draw;}
	
	public Board cloneBoard() {
		Piece[][] clone = new Piece[8][8];
		for (int i = 0; i < 8; i++) {
			for (int k = 0; k < 8; k++) {
				if (pieces[i][k] != null)
					clone[i][k] = clonePiece(pieces[i][k]);
			}
		}

		Board cloneBoard = new Board(clone, this.draw);
		for (int i = 0; i < 8; i++) {
			for (int k = 0; k < 8; k++) {
				if (clone[i][k] != null)
					clone[i][k].setBoard(cloneBoard);
			}
		}
		return cloneBoard;
	}

	public Piece clonePiece(Piece piece) {
		Piece copy = new Piece(piece.getColor(), piece.getBoard(), piece.getX(), piece.getY());
		copy.setStatus(piece.getStatus());
		copy.setWeight(piece.getWeight());
		return copy;
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < 8; i++) {
			s += (i);
			for (int k = 0; k < 8; k++) {
				if (pieces[i][k] == null)
					s += "__ ";
				else {
					if (pieces[i][k].getStatus() && pieces[i][k].getColor()) {
						s += "rk ";
					} else if (pieces[i][k].getStatus() && !pieces[i][k].getColor()) {
						s += "bk ";
					} else if (!pieces[i][k].getStatus() && pieces[i][k].getColor()) {
						s += "r  ";
					} else {
						s += "b  ";
					}
				}
			}
			s += "\n";
		}
		s += " ";
		for (int i = 0; i < 8; i++) {
			s += i + "  ";
		}
		return s;
	}
}