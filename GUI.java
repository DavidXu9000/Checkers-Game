package draughts;

import java.io.FileInputStream;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

public class GUI extends Application{



	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Checkers");
		BorderPane pane = new BorderPane();
		GridPane grid = new GridPane();
		grid.setHgap(0);
		grid.setVgap(0);
		for(int i = 0; i < 8; i++) {
			for(int k = 0; k < 8; k++) {
				if(i%2==0 && k%2==0 || i%2==1 && k%2==1) {
					Rectangle rect = new Rectangle(45, 45, Paint.valueOf("FFFFFF"));
					rect.setStroke(Paint.valueOf("FFFFFF"));
					rect.setStrokeType(StrokeType.INSIDE);
					rect.setStrokeWidth(1.5);
					grid.add(rect, k, i);
				}
				else {
					Rectangle rect = new Rectangle(45, 45, Paint.valueOf("3F48CC"));
					rect.setStroke(Paint.valueOf("3F48CC"));
					rect.setStrokeType(StrokeType.INSIDE);
					rect.setStrokeWidth(1.5);
					grid.add(rect, k, i);
				}
			}
		}		pane.setTop(grid);

		GridPane buttons = new GridPane();
		Button newGame = new Button("New Game");
		Button concede = new Button("Concede");
		Button toggle = new Button("Toggle Difficulty");

		GridPane text = new GridPane();
		TextField area = new TextField();
		TextField difficulty = new TextField();
		difficulty.setText("Difficulty: Hard");
		area.setText("Moves left without a piece being taken: 50");
		area.setMinWidth(300);
		area.setEditable(false);

		buttons.add(newGame, 0, 0);
		buttons.add(concede, 1, 0);
		buttons.add(toggle, 2, 0);
		text.add(difficulty, 0, 0);
		text.add(area, 1 , 0);
		pane.setCenter(buttons);
		pane.setBottom(text);

		FileInputStream redPieceBlueStream = new FileInputStream("RedPieceBlue.png");
		FileInputStream blackPieceBlueStream = new FileInputStream("BlackPieceBlue.png");
		FileInputStream redKingBlueStream = new FileInputStream("RedKingBlue.png");
		FileInputStream blackKingBlueStream = new FileInputStream("BlackKingBlue.png");


		Image redPieceBlue = new Image(redPieceBlueStream);
		Image blackPieceBlue = new Image(blackPieceBlueStream);
		Image blackKingBlue = new Image(blackKingBlueStream);
		Image redKingBlue = new Image(redKingBlueStream);



		ImagePattern redPieceBluePat = new ImagePattern(redPieceBlue);
		ImagePattern blackPieceBluePat = new ImagePattern(blackPieceBlue);
		ImagePattern redKingBluePat = new ImagePattern(redKingBlue);
		ImagePattern blackKingBluePat = new ImagePattern(blackKingBlue);



		for (int i = 1; i < 8; i += 2) {
			Rectangle rect = (Rectangle) getRectangle(0, i, grid);
			rect.setFill(blackPieceBluePat);
		}
		for (int i = 0; i < 8; i += 2) {
			Rectangle rect = (Rectangle) getRectangle(1, i, grid);
			rect.setFill(blackPieceBluePat);
		}
		for (int i = 1; i < 8; i += 2) {
			Rectangle rect = (Rectangle) getRectangle(2, i, grid);
			rect.setFill(blackPieceBluePat);
		}
		for (int i = 0; i < 8; i += 2) {
			Rectangle rect = (Rectangle) getRectangle(5, i, grid);
			rect.setFill(redPieceBluePat);
		}
		for (int i = 1; i < 8; i += 2) {
			Rectangle rect = (Rectangle) getRectangle(6, i, grid);
			rect.setFill(redPieceBluePat);
		}
		for (int i = 0; i < 8; i += 2) {
			Rectangle rect = (Rectangle) getRectangle(7, i, grid);
			rect.setFill(redPieceBluePat);
		}


		Game game = new Game();
		Pair clicked = new Pair(-1, -1);
		ArrayList<Pair> possibleMoves = new ArrayList<Pair>();
		ArrayList<Move> listOfMoves = new ArrayList<Move>();

		grid.setOnMouseClicked(e -> {
			Board b = game.getBoard();
			int y = (int) e.getSceneX()/45;
			int x  = (int) e.getSceneY()/45;
			if(clicked.a == -1) {
				Rectangle rect = (Rectangle) getRectangle(x, y, grid);
				rect.setStroke(Paint.valueOf("00FF00"));
				clicked.a = x;
				clicked.b = y;
				if(b.getBoard()[x][y]!=null) {
					if(b.getBoard()[x][y].getColor() == true) {
						ArrayList<Move> moves = b.getBoard()[x][y].getPossibleMoves();
						for(Move m: moves) {
							possibleMoves.add(new Pair(m.getEndx(), m.getEndy()));
							listOfMoves.add(m);
							Rectangle r = (Rectangle) getRectangle(m.getEndx(), m.getEndy(), grid);
							r.setStroke(Paint.valueOf("00FF00"));
						}
					}
				}
			}
			else {
				Rectangle rect = (Rectangle) getRectangle(x, y, grid);
				Pair p = new Pair(x, y);
				int in = inList(possibleMoves, p);
				//user makes a move
				if(in != -1) {
					area.setText("Moves left without a piece being taken: " + (50 - b.draw()) );
					Move mv = listOfMoves.get(in);
					for(Piece piece: mv.taken()) {
						Rectangle taken = (Rectangle) getRectangle(piece.getX(), piece.getY(), grid);
						taken.setFill(Paint.valueOf("3F48CC"));
					}
					b.updateBoard(mv);
					if(mv.piece.getStatus()==false)
						rect.setFill(redPieceBluePat);
					else {
						rect.setFill(redKingBluePat);
					}



					Rectangle original = (Rectangle) getRectangle(clicked.a, clicked.b, grid);
					original.setFill(Paint.valueOf("3F48CC"));
					original.setStroke(Paint.valueOf("3F48CC"));

					Move m = game.getAI().getMove(b);
					for(Piece piece: m.taken()) {
						Rectangle taken = (Rectangle) getRectangle(piece.getX(), piece.getY(), grid);
						taken.setFill(Paint.valueOf("3F48CC"));
					}
					b.updateBoard(m);
					Rectangle aiStart = (Rectangle) getRectangle(m.getStartx(), m.getStarty(), grid);
					Rectangle aiEnd = (Rectangle) getRectangle(m.getEndx(), m.getEndy(), grid);
					aiStart.setFill(Paint.valueOf("3F48CC"));
					if(m.piece.getStatus() == false) {
						aiEnd.setFill(blackPieceBluePat);
					}
					else {
						aiEnd.setFill(blackKingBluePat);
					}

					listOfMoves.clear();
					for(Pair a:possibleMoves) {
						Rectangle r = (Rectangle) getRectangle(a.a, a.b, grid);
						r.setStroke(Paint.valueOf("3F48CC"));
					}
					possibleMoves.clear();
					clicked.a = -1;
					clicked.b = -1;
				}


				//deselect square
				else if(clicked.a == x && clicked.b == y) {
					if(x %2 == 0 && y %2 == 0 || x %2 == 1 && y%2 == 1) {
						rect.setStroke(Paint.valueOf("FFFFFF"));
					}
					else {
						rect.setStroke(Paint.valueOf("3F48CC"));
					}
					listOfMoves.clear();
					for(Pair a:possibleMoves) {
						Rectangle r = (Rectangle) getRectangle(a.a, a.b, grid);
						r.setStroke(Paint.valueOf("3F48CC"));
					}
					possibleMoves.clear();
					clicked.a = -1;
					clicked.b = -1;
				}

				//change selected square
				else {
					try {
						rect.setStroke(Paint.valueOf("00FF00"));
						listOfMoves.clear();
						for(Pair a:possibleMoves) {
							Rectangle r = (Rectangle) getRectangle(a.a, a.b, grid);
							r.setStroke(Paint.valueOf("3F48CC"));
						}
						possibleMoves.clear();

						Rectangle r = (Rectangle) getRectangle(clicked.a, clicked.b, grid);
						if(clicked.a %2 == 0 && clicked.b %2 == 0 || clicked.a %2 == 1 && clicked.b %2 == 1) {
							r.setStroke(Paint.valueOf("FFFFFF"));
						}
						else {
							r.setStroke(Paint.valueOf("3F48CC"));
						}
						clicked.a = x;
						clicked.b = y;
						if(b.getBoard()[x][y].getColor() == true) {
							ArrayList<Move> moves = b.getBoard()[x][y].getPossibleMoves();
							for(Move m: moves) {
								possibleMoves.add(new Pair(m.getEndx(), m.getEndy()));
								listOfMoves.add(m);
								Rectangle a = (Rectangle) getRectangle(m.getEndx(), m.getEndy(), grid);
								a.setStroke(Paint.valueOf("00FF00"));
							}
						}
					}catch(Exception exception) {
					}
				}
			}
			if(b.isWinner(true))
				area.setText("You win!");
			if(b.isWinner(false))
				area.setText("You lose!");
			if(b.isStalemate())
				area.setText("Stalemate");
		});

		newGame.setOnAction( e ->{
			area.setText("Moves left without a piece being taken: 50");
			game.reset();
			for(int i = 0; i < 8; i++) {
				for(int k = 0; k < 8; k++) {
					if(i%2==0 && k%2==0 || i%2==1 && k%2==1) {
						Rectangle r = (Rectangle) getRectangle(i, k, grid);
						r.setFill(Paint.valueOf("FFFFFF"));
						r.setStroke(Paint.valueOf("FFFFFF"));
					}
					else {
						Rectangle r = (Rectangle) getRectangle(i, k, grid);
						r.setFill(Paint.valueOf("3F48CC"));
						r.setStroke(Paint.valueOf("3F48CC"));
					}
				}
			}
			for (int i = 1; i < 8; i += 2) {
				Rectangle rect = (Rectangle) getRectangle(0, i, grid);
				rect.setFill(blackPieceBluePat);
			}
			for (int i = 0; i < 8; i += 2) {
				Rectangle rect = (Rectangle) getRectangle(1, i, grid);
				rect.setFill(blackPieceBluePat);
			}
			for (int i = 1; i < 8; i += 2) {
				Rectangle rect = (Rectangle) getRectangle(2, i, grid);
				rect.setFill(blackPieceBluePat);
			}
			for (int i = 0; i < 8; i += 2) {
				Rectangle rect = (Rectangle) getRectangle(5, i, grid);
				rect.setFill(redPieceBluePat);
			}
			for (int i = 1; i < 8; i += 2) {
				Rectangle rect = (Rectangle) getRectangle(6, i, grid);
				rect.setFill(redPieceBluePat);
			}
			for (int i = 0; i < 8; i += 2) {
				Rectangle rect = (Rectangle) getRectangle(7, i, grid);
				rect.setFill(redPieceBluePat);
			}
		});

		concede.setOnAction(e ->{
			area.setText("You lose!");
		});

		toggle.setOnAction(e -> {
			game.getAI().toggle();
			if(game.getAI().getDepth() == 8) {
				difficulty.setText("Difficulty: Hard");
			}
			else {
				difficulty.setText("Difficulty: Easy");
			}
		});

		Scene scene = new Scene(pane, 500, 420);
		primaryStage.setScene(scene);
		primaryStage.show();


	}

	private static Node getRectangle(int row, int column, GridPane pane) {
		ObservableList<Node> list = pane.getChildren();
		for(Node n: list) {
			if(pane.getRowIndex(n) == row && pane.getColumnIndex(n) == column) {
				return n;
			}
		}
		return null;
	}

	private static int inList(ArrayList<Pair> list, Pair p) {
		 for(int i = 0; i < list.size(); i++) {
			 if(list.get(i).a == p.a & list.get(i).b == p.b) {
				 return i;
			 }
		 }
		return -1;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
