package game;

/* Helper class to print a GameBoard */
public class GameBoardWriter {
	public static String convertToString(GameBoard board) {
        int size = board.getSize();
        String toret = "\n";
        toret += getColumnHeader(size);
        for(int row = 0; row < 2; ++row) {
        	
            toret += getHorizontalLine(size);
            toret += getRow(board, row);
        }
        toret += getHorizontalLine(size);
        return appendPlayerTag(toret);
    }
	
	private static String appendPlayerTag(String output) {
		String[] lines = output.split("\n");
		String toret = "";
		for(int i = 0; i < lines.length; ++i) {
			if(i == 3) lines[i] = "Player 1  " + lines[i];
			else if(i == 5) lines[i] = "Player 2  " + lines[i];
			else lines[i] = "          " + lines[i];
			toret += lines[i] + "\n";
		}
		return toret;
	}
	
	private static String getColumnHeader(int size) {
		size  = 2 * size + 1;

        String toret = "";

        for(int col = 0, i = 0; col < size; ++col) {
            if(col % 2 == 0)
                toret += " ";
            else {
                toret += String.format("%2d", i + 1);
                ++i;
            }
        }
        return toret + "\n";
	}
    private static String getRow(GameBoard board, int row) {
        int size  = 2 * board.getSize() + 1;

        String toret = "";

        for(int col = 0, i = 0; col < size; ++col) {
            if(col % 2 == 0)
                toret += "|";
            else {
                toret += String.format("%2d", board.getPeeblesAt(row, i));
                ++i;
            }
        }
        return toret + "\n";
    }

    public static String getHorizontalLine(int boardSize) {
        int size = boardSize * 3 + 1;
        String toret = "";
        for(int i = 0; i < size; ++i) {
            if(i % 3 != 0)
                toret += "-";
            else
                toret += "+";
        }
        return toret  + "\n";
    }
}
