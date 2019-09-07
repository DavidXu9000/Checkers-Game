package draughts;



public class Game {
	private static Board board;
	private static AI ai;

	public Game() {
		Piece[][] pieces = new Piece[8][8];
		board = new Board(pieces, 0);
		for (int i = 1; i < 8; i += 2)
			pieces[0][i] = new Piece(false, board, 0, i);
		for (int i = 0; i < 8; i += 2)
			pieces[1][i] = new Piece(false, board, 1, i);
		for (int i = 1; i < 8; i += 2)
			pieces[2][i] = new Piece(false, board, 2, i);

		for (int i = 0; i < 8; i += 2)
			pieces[5][i] = new Piece(true, board, 5, i);
		for (int i = 1; i < 8; i += 2)
			pieces[6][i] = new Piece(true, board, 6, i);
		for (int i = 0; i < 8; i += 2)
			pieces[7][i] = new Piece(true, board, 7, i);
		ai = new AI(false);
		
	}

	public Board getBoard() {
		return board;
	}

	public AI getAI() {
		return ai;
	}
	
	public void reset() {
		Piece[][] pieces = new Piece[8][8];
		board = new Board(pieces, 0);
		for (int i = 1; i < 8; i += 2)
			pieces[0][i] = new Piece(false, board, 0, i);
		for (int i = 0; i < 8; i += 2)
			pieces[1][i] = new Piece(false, board, 1, i);
		for (int i = 1; i < 8; i += 2)
			pieces[2][i] = new Piece(false, board, 2, i);

		for (int i = 0; i < 8; i += 2)
			pieces[5][i] = new Piece(true, board, 5, i);
		for (int i = 1; i < 8; i += 2)
			pieces[6][i] = new Piece(true, board, 6, i);
		for (int i = 0; i < 8; i += 2)
			pieces[7][i] = new Piece(true, board, 7, i);
		ai = new AI(false);
	}

}